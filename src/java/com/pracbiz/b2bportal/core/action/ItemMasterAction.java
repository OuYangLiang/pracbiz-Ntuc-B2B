package com.pracbiz.b2bportal.core.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.ItemStatus;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.eai.file.item.FileParser;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.ItemMasterHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.extension.ItemMasterSummaryHolder;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.ItemMasterService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public class ItemMasterAction extends TransactionalDocsBaseAction implements
        CoreCommonConstants
{

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory
            .getLogger(ItemMasterAction.class);
    public static final String SESSION_KEY_SEARCH_PARAMETER_ITEM_MASTER = "SEARCH_PARAMETER_ITEM_MASTER";
    private static final String SESSION_OID_PARAMETER = "session.parameter.itemMaster.selectedOids";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_ITEM_MASTER_MSG_TYPE = "session.itemMaster.msgType";
    
    private ItemMasterSummaryHolder param;
    private File upload;
    private String uploadContentType;
    private String uploadFileName;
    transient private InputStream resultStream;
    private List<TradingPartnerHolder> tpList;
    private String moduleMsgType;
    private Map<String, String> status;
    private static final Map<String, String> sortMap;
    
    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("buyerCode", "BUYER_CODE");
        sortMap.put("buyerName", "BUYER_NAME");
        sortMap.put("supplierCode", "SUPPLIER_CODE");
        sortMap.put("supplierName", "SUPPLIER_NAME");
        sortMap.put("msgType", "MSG_TYPE");
        sortMap.put("originalFilename", "ORIGINAL_FILENAME");
        sortMap.put("createDate", "CREATE_DATE");
        sortMap.put("sentDate", "SENT_DATE");
        sortMap.put("itemStatus", "CTRL_STATUS");
    }

    @Autowired
    private transient SupplierService supplierService;
    @Autowired
    private transient BuyerService buyerService;
    @Autowired
    private transient ItemMasterService itemMasterService;
    @Autowired
    private transient BuyerMsgSettingService buyerMsgSettingService;
    @Autowired
    private transient CustomAppConfigHelper appConfig;
    @Autowired
    private transient MailBoxUtil mboxUtil;
    @Autowired
    private transient OidService oidService;
    @Autowired
    private transient FileParser cktangFileParser;
    @Autowired
    private transient MsgTransactionsService msgTransactionsService;
    @Autowired 
    private transient ControlParameterService controlParameterService;

    public ItemMasterAction()
    {
        this.initMsg();
        this.setPageId(PageId.P008.name());
    }
    
    
    public String putParamIntoSession()
    {
        this.getSession().put(SESSION_OID_PARAMETER,
            this.getRequest().getParameter(REQUEST_PARAMTER_OID));

        return SUCCESS;
    }
    

    public String init()
    {
        this.getSession().put(SESSION_ITEM_MASTER_MSG_TYPE, moduleMsgType);
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_ITEM_MASTER);
        
        if (param == null)
        {
            param = (ItemMasterSummaryHolder)getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_ITEM_MASTER);
        }
        
        try
        {
            if (param == null)
            {
                param = new ItemMasterSummaryHolder();
                if(getProfileOfCurrentUser().getBuyerOid() != null)
                {
                    ControlParameterHolder documentWindow = controlParameterService
                            .selectCacheControlParameterBySectIdAndParamId(
                                    "CTRL", "DOCUMENT_WINDOW_BUYER");
                    if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
                    {
                        param.setCreateDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                    }
                    else
                    {
                        param.setCreateDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                    }
                    param.setCreateDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
                }
                if (getProfileOfCurrentUser().getSupplierOid() != null)
                {
                    ControlParameterHolder documentWindow = controlParameterService
                        .selectCacheControlParameterBySectIdAndParamId(
                                "CTRL", "DOCUMENT_WINDOW_SUPPLIER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
                {
                    param.setCreateDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    param.setCreateDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                param.setCreateDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
                }
            }
            status = ItemStatus.toMapValue(this);
            this.initSearchCriteria();
        }
        catch (Exception e)
        {
            handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        this.getSession().put(SESSION_KEY_SEARCH_PARAMETER_ITEM_MASTER, param);
        return SUCCESS;
    }


    public String search()
    {
        if (null == param)
        {
            param = new ItemMasterSummaryHolder();
        }
        try
        {
            param.setCreateDateFrom(DateUtil.getInstance().getFirstTimeOfDay(
                    param.getCreateDateFrom()));
            param.setCreateDateTo(DateUtil.getInstance().getLastTimeOfDay(
                    param.getCreateDateTo()));
            param.trimAllString();
            param.setAllEmptyStringToNull();
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_ITEM_MASTER, param);

        return SUCCESS;
    }


    public String data()
    {

        try
        {
            ItemMasterSummaryHolder searchParam = (ItemMasterSummaryHolder) getSession()
                    .get(SESSION_KEY_SEARCH_PARAMETER_ITEM_MASTER);

            if (searchParam == null)
            {
                searchParam = new ItemMasterSummaryHolder();
                if(getProfileOfCurrentUser().getBuyerOid() != null)
                {
                    ControlParameterHolder documentWindow = controlParameterService
                            .selectCacheControlParameterBySectIdAndParamId(
                                    "CTRL", "DOCUMENT_WINDOW_BUYER");
                    if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
                    {
                        searchParam.setCreateDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                    }
                    else
                    {
                        searchParam.setCreateDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                    }
                    searchParam.setCreateDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
                }
                if (getProfileOfCurrentUser().getSupplierOid() != null)
                {
                    ControlParameterHolder documentWindow = controlParameterService
                        .selectCacheControlParameterBySectIdAndParamId(
                                "CTRL", "DOCUMENT_WINDOW_SUPPLIER");
                    if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
                    {
                        searchParam.setCreateDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                    }
                    else
                    {
                        searchParam.setCreateDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                    }
                    searchParam.setCreateDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
                }
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_ITEM_MASTER,
                        searchParam);
            }
            searchParam.setMsgType((String) this.getSession().get(SESSION_ITEM_MASTER_MSG_TYPE));
            initCurrentUserSearchParam(searchParam);
            searchParam = initSortField(searchParam);
            searchParam.trimAllString();
            searchParam.setAllEmptyStringToNull();
            this.obtainListRecordsOfPagination(itemMasterService, searchParam,
                    null);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }

        return SUCCESS;
    }
    
    
    private ItemMasterSummaryHolder initSortField(ItemMasterSummaryHolder param)
    {
        param.setSortField(SORT_FIELD_SENT_DATE);
        param.setSortOrder(SORT_ORDER_DESC);
        
        if(null != getProfileOfCurrentUser().getSupplierOid()) 
        {
            param.setSortField(SORT_FIELD_CREATE_DATE);
            param.setSortOrder(SORT_ORDER_DESC);
        }
        
        return param;
    }


    public String initUpload()
    {
        return SUCCESS;
    }


    public void validateSave()
    {
        boolean flag = false;
        try
        {
            if (param == null || param.getTradingPartner() == null)
            {
                this.addActionError(this.getText("B2BPC2801"));
                flag = true;
            }
            if (upload == null)
            {
                this.addActionError(this.getText("B2BPC2802"));
                flag = true;
            }
            if (!flag)
            {
                String[] tp = param.getTradingPartner().split("-");
                param.setBuyerCode(tp[0].trim());
                param.setSupplierCode(tp[1].trim());
                BuyerHolder currBuyer = buyerService.selectBuyerByBuyerCode(param.getBuyerCode());
                BuyerMsgSettingHolder setting = buyerMsgSettingService.selectByKey(currBuyer.getBuyerOid(), MsgType.ITEMIN.name());
                String fileFormat = null;
                if (setting == null)
                {
                    fileFormat = appConfig.getDefaultFileFormat(MsgType.ITEMIN.name());
                }
                else
                {
                    fileFormat = setting.getFileFormat();
                }
                List<String> errors = new ArrayList<String>();
                try
                {
                    errors = cktangFileParser.validateItemInFile(fileFormat, upload, uploadFileName, this);
                }
                catch (Exception e)
                {
                    this.addActionError(this.getText("B2BPC2813"));
                    flag = true;
                }
                if (errors != null && !errors.isEmpty())
                {
                    for (String error : errors)
                    {
                        this.addActionError(error);
                    }
                    flag = true;
                }
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
    }


    public String save()
    {
        try
        {
            SupplierHolder supplier = supplierService.selectSupplierByKey(getProfileOfCurrentUser().getSupplierOid());
            String targetPath = mboxUtil.getSupplierTmp(supplier.getMboxId());
            BuyerHolder currBuyer = buyerService.selectBuyerByBuyerCode(param.getBuyerCode());
            ItemMasterHolder item = new ItemMasterHolder();
            item.setItemOid(oidService.getOid());
            item.setItemNo(DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date()));
            item.setItemType(MsgType.ITEMIN.name());
            item.setFileName(uploadFileName);
            item.setItemStatus(ItemStatus.NEW);
            item.setBuyerCode(currBuyer.getBuyerCode());
            item.setBuyerName(currBuyer.getBuyerName());
            item.setSupplierCode(param.getSupplierCode());
            item.setSupplierName(supplier.getSupplierName());
            
            MsgTransactionsHolder msg = new MsgTransactionsHolder();
            msg.setDocOid(item.getItemOid());
            msg.setMsgType(MsgType.ITEMIN.name());
            msg.setMsgRefNo(item.getItemNo());
            msg.setBuyerOid(currBuyer.getBuyerOid());
            msg.setBuyerCode(currBuyer.getBuyerCode());
            msg.setBuyerName(currBuyer.getBuyerName());
            msg.setSupplierOid(supplier.getSupplierOid());
            msg.setSupplierCode(param.getSupplierCode());
            msg.setSupplierName(supplier.getSupplierName());
            msg.setCreateDate(new Date());
            msg.setActive(Boolean.TRUE);
            msg.setValid(Boolean.TRUE);
            
            
            itemMasterService.saveItemIn(this.getCommonParameter(), upload, targetPath, item, msg);
        }
        catch (Exception e)
        {
            handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public void validateSaveAndSend()
    {
        validateSave();
    }
    
    
    public String saveAndSend()
    {
        try
        {
            SupplierHolder supplier = supplierService.selectSupplierByKey(getProfileOfCurrentUser().getSupplierOid());
            String targetPath = mboxUtil.getSupplierOutPath(supplier.getMboxId());
            BuyerHolder currBuyer = buyerService.selectBuyerByBuyerCode(param.getBuyerCode());
            ItemMasterHolder item = new ItemMasterHolder();
            item.setItemOid(oidService.getOid());
            item.setItemNo(DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date()));
            item.setItemType(MsgType.ITEMIN.name());
            item.setFileName(uploadFileName);
            item.setItemStatus(ItemStatus.SENT);
            item.setBuyerCode(currBuyer.getBuyerCode());
            item.setBuyerName(currBuyer.getBuyerName());
            item.setSupplierCode(param.getSupplierCode());
            item.setSupplierName(supplier.getSupplierName());
            
            MsgTransactionsHolder msg = new MsgTransactionsHolder();
            msg.setDocOid(item.getItemOid());
            msg.setMsgType(MsgType.ITEMIN.name());
            msg.setMsgRefNo(item.getItemNo());
            msg.setBuyerOid(currBuyer.getBuyerOid());
            msg.setBuyerCode(currBuyer.getBuyerCode());
            msg.setBuyerName(currBuyer.getBuyerName());
            msg.setSupplierOid(supplier.getSupplierOid());
            msg.setSupplierCode(param.getSupplierCode());
            msg.setSupplierName(supplier.getSupplierName());
            msg.setCreateDate(new Date());
            msg.setActive(Boolean.TRUE);
            msg.setValid(Boolean.TRUE);
            
            
            itemMasterService.saveItemIn(this.getCommonParameter(), upload, targetPath, item, msg);
        }
        catch (Exception e)
        {
            handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public String send()
    {
        try
        {
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);

            if(null == selectedOids)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }

            this.getSession().remove(SESSION_OID_PARAMETER);

            String[] parts = selectedOids.toString().split(
                REQUEST_OID_DELIMITER);
            
            for(String part : parts)
            {
                BigDecimal itemOid = new BigDecimal(part);
                ItemMasterHolder oldItem = itemMasterService.selectByKey(itemOid);
                MsgTransactionsHolder msgTransactions = msgTransactionsService.selectByKey(itemOid);
                if (!ItemStatus.NEW.equals(oldItem.getItemStatus()))
                {
                    msg.saveError(getText("B2BPC2808", new String[]{msgTransactions.getOriginalFilename()}));
                    continue;
                }
                SupplierHolder supplier = supplierService.selectSupplierByKey(msgTransactions.getSupplierOid());
                File targetFile = new File(mboxUtil.getSupplierTmp(supplier.getMboxId()), msgTransactions.getOriginalFilename());
                if (!targetFile.exists())
                {
                    msg.saveError(getText("B2BPC2809", new String[]{msgTransactions.getOriginalFilename(), msgTransactions.getSupplierCode()}));
                    continue;
                }
                FileUtil.getInstance().moveFile(targetFile, mboxUtil.getSupplierOutPath(supplier.getMboxId()));
                itemMasterService.sendItemIn(getCommonParameter(), oldItem, msgTransactions, targetFile, supplier.getMboxId());
                msg.saveSuccess(getText("B2BPC2810", new String[]{msgTransactions.getOriginalFilename()}));
                
            }
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            
            msg.addMessageTarget(mt);

        }
        catch(Exception e)
        {
            handleException(e);
        }

        return FORWARD_COMMON_MESSAGE;
    }
    
    
    public String delete()
    {
        try
        {
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            
            if(null == selectedOids)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }
            
            this.getSession().remove(SESSION_OID_PARAMETER);
            
            String[] parts = selectedOids.toString().split(
                    REQUEST_OID_DELIMITER);
            
            for(String part : parts)
            {
                BigDecimal itemOid = new BigDecimal(part);
                ItemMasterHolder oldItem = itemMasterService.selectByKey(itemOid);
                MsgTransactionsHolder msgTransactions = msgTransactionsService.selectByKey(itemOid);
                if (!ItemStatus.NEW.equals(oldItem.getItemStatus()))
                {
                    msg.saveError(getText("B2BPC2811", new String[]{msgTransactions.getOriginalFilename()}));
                    continue;
                }
                
                itemMasterService.deleteItemIn(getCommonParameter(), itemOid);
                msg.saveSuccess(getText("B2BPC2812", new String[]{msgTransactions.getOriginalFilename()}));
                
            }
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            
            msg.addMessageTarget(mt);
            
        }
        catch(Exception e)
        {
            handleException(e);
        }
        
        return FORWARD_COMMON_MESSAGE;
    }
    
    public String downloadOriginalFile()
    {
        try
        {
            ItemMasterHolder item = itemMasterService.selectByKey(param.getItemOid());
            MsgTransactionsHolder msgTransaction = msgTransactionsService.selectByKey(param.getItemOid());
            SupplierHolder supplier = supplierService.selectSupplierByKey(msgTransaction.getSupplierOid());
            BuyerHolder currBuyer = buyerService.selectBuyerByBuyerCode(msgTransaction.getBuyerCode());
            BuyerMsgSettingHolder setting = buyerMsgSettingService.selectByKey(currBuyer.getBuyerOid(), MsgType.ITEMIN.name());
            String fileFormat = null;
            if (setting == null)
            {
                fileFormat = appConfig.getDefaultFileFormat(MsgType.ITEMIN.name());
            }
            else
            {
                fileFormat = setting.getFileFormat();
            }
            this.setUploadContentType(cktangFileParser.itemInFileContent(fileFormat));
            this.setUploadFileName(msgTransaction.getOriginalFilename());
            File file = null;
            if (ItemStatus.NEW.equals(item.getItemStatus()))
            {
                file = new File(mboxUtil.getSupplierTmp(supplier.getMboxId()), msgTransaction.getOriginalFilename());
            }
            else if (ItemStatus.SENT.equals(item.getItemStatus()))
            {
                if (msgTransaction.getProcDate() == null)
                {
                    msg.saveError(this.getText("B2BPC2814"));
                }
                else if (msgTransaction.getValid() && msgTransaction.getActive())
                {
                    file = new File(mboxUtil.getSupplierArchivePath(supplier.getMboxId()) + PS + DIR_OUT + PS + msgTransaction.getMsgRefNo().substring(0, 6) + PS, msgTransaction.getOriginalFilename());
                }
                
            }
            if ((msg.getContents() == null || msg.getContents().isEmpty()) && (file == null || !file.exists()))
            {
                msg.saveError(this.getText("B2BPC2815"));
            }
            if (msg.getContents() != null && !msg.getContents().isEmpty())
            {
                msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
                MessageTargetHolder mt = new MessageTargetHolder();
                mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
                mt.setTargetURI(INIT);
                mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
                msg.addMessageTarget(mt);
                return FORWARD_COMMON_MESSAGE;
            }
            resultStream = new FileInputStream(file);
            
        }
        catch(Exception e)
        {
            handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    

    public ItemMasterSummaryHolder getParam()
    {
        return param;
    }


    public void setParam(ItemMasterSummaryHolder param)
    {
        this.param = param;
    }


	public void setUpload(File upload)
	{
		this.upload = upload;
	}


	public String getUploadContentType()
    {
        return uploadContentType;
    }


    public void setUploadContentType(String uploadContentType)
	{
		this.uploadContentType = uploadContentType;
	}


	public String getUploadFileName()
    {
        return uploadFileName;
    }


    public void setUploadFileName(String uploadFileName)
	{
		this.uploadFileName = uploadFileName;
	}


    public InputStream getResultStream()
    {
        return resultStream;
    }


    public void setResultStream(InputStream resultStream)
    {
        this.resultStream = resultStream;
    }


    public List<TradingPartnerHolder> getTpList()
    {
        return tpList;
    }

    
    public void setModuleMsgType(String moduleMsgType)
    {
        this.moduleMsgType = moduleMsgType;
    }


    public Map<String, String> getStatus()
    {
        return status;
    }

}
