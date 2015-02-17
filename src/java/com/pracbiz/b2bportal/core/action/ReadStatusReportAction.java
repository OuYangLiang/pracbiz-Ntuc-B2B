package com.pracbiz.b2bportal.core.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.constants.ReadStatusReportActionStatus;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.ReadStatusReportHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.RtvHeaderService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author yinchi
 */
public class ReadStatusReportAction extends ProjectBaseAction implements
    CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(ReadStatusReportAction.class);
    private static final long serialVersionUID = 3523235911279618709L;
    
    private static final String EXCEL_IS_GENERATING = "isGenerating";
    private static final String EXCEL_DATA = "excelData";
    
    @Autowired transient private BuyerService buyerService;
    @Autowired transient private PoHeaderService poHeaderService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private RtvHeaderService rtvHeaderService;
    
    private transient InputStream rptResult;
    private String rptFileName;
    private String errorMsg;
    private String data;
    protected List<? extends Object> buyers;
    private MsgTransactionsExHolder param;
    private Map<String,String> docType;
    
    // *****************************************************
    // search page
    // *****************************************************
    public String init()
    {
        try
        {
            if (param == null)
            {
                param = new MsgTransactionsExHolder();
                param.setBeforeHour("6");
                initSearchParam();
            }
        }
        catch (Exception e)
        {
            this.handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public String exportExcel()
    {
        try
        {
            this.getSession().put(EXCEL_IS_GENERATING, "Y");
            
            //get eligible msg transactions from session
            @SuppressWarnings("unchecked")
            List<ReadStatusReportHolder> msgs = (List<ReadStatusReportHolder>)this.getSession().get(EXCEL_DATA);
            
            //group by msg type
            Map<String, List<ReadStatusReportHolder>> msgMap = this.groupMsgsByType(msgs);
            
            //generate excel
            byte[] datas = this.exportExcel(msgMap);
            
            rptResult = new ByteArrayInputStream(datas);
            rptFileName = "Read Status Report_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec();
            
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        finally
        {
            this.getSession().remove(EXCEL_IS_GENERATING);
            this.getSession().remove(EXCEL_DATA);
        }
        
        return SUCCESS;
    }
    
    
    public String checkExcelData()
    {
        try
        {
            boolean flag = this.hasErrors();
            
            if (!flag && param.getBeforeHour().trim().isEmpty())
            {
                data = "beforeHourNull";
                flag = true;
            }
            
            if (!flag && !param.getBeforeHour().trim().isEmpty() && !param.getBeforeHour().trim().matches("\\d{0,4}"))
            {
                data = "beforeHourWrong";
                flag = true;
            }
            
            if (!flag && null != this.getSession().get(EXCEL_IS_GENERATING))
            {
                data = "generating";
                flag = true;
            }
            
            if (!flag)
            {
                param.setCurrentUserOid(this.getProfileOfCurrentUser().getUserOid());
                param.setCurrentUserBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                param.setReadStatus(ReadStatus.UNREAD);
                param.trimAllString();
                param.setAllEmptyStringToNull();
                
                //get all msg transactions which supplier in current buyer user's group
                List<ReadStatusReportHolder> msgs = msgTransactionsService.selectMsgsForReport(param);
                
                //no records
                if(null == msgs || msgs.isEmpty())
                {
                    data = "empty";
                }
                //put eligible msg transaction in session
                else
                {
                    this.getSession().put(EXCEL_DATA, msgs);
                }
            }
            
        }
        catch(Exception e)
        {
            this.handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    // *****************************************************
    // private method
    // *****************************************************
    private void initSearchParam() throws Exception
    {
        docType = ReadStatusReportActionStatus.toMapValue(this);
        buyers = buyerService.selectAvailableBuyersByUserOid(getProfileOfCurrentUser());
    }
    
    
    private Map<String, List<ReadStatusReportHolder>> groupMsgsByType(List<ReadStatusReportHolder> msgs)
    {
        Map<String, List<ReadStatusReportHolder>> msgMap = new HashMap<String, List<ReadStatusReportHolder>>();
        
        for (ReadStatusReportHolder msg : msgs)
        {
            if (msgMap.containsKey(msg.getMsgType()))
            {
                msgMap.get(msg.getMsgType()).add(msg);
            }
            else
            {
                List<ReadStatusReportHolder> value = new ArrayList<ReadStatusReportHolder>();
                value.add(msg);
                msgMap.put(msg.getMsgType(), value);
            }
        }
        
        return msgMap;
    }
    
    
    private byte[] exportExcel(Map<String, List<ReadStatusReportHolder>> msgMap) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        int sheetIndex = 0;
        for (Map.Entry<String, List<ReadStatusReportHolder>> entry : msgMap.entrySet())
        {
            List<ReadStatusReportHolder> value = entry.getValue();
            this.initSheet(wwb, this.groupByBuyer(value), sheetIndex, value.get(0).getMsgType());
            sheetIndex++;
        }
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }
    
    
    private Map<String, List<ReadStatusReportHolder>>groupByBuyer(List<ReadStatusReportHolder> data)
    {
        Map<String, List<ReadStatusReportHolder>> map = new HashMap<String, List<ReadStatusReportHolder>>();
        for (ReadStatusReportHolder holder : data)
        {
            if (map.containsKey(holder.getBuyerCode()))
            {
                map.get(holder.getBuyerCode()).add(holder);
            }
            else
            {
                List<ReadStatusReportHolder> value = new ArrayList<ReadStatusReportHolder>();
                value.add(holder);
                map.put(holder.getBuyerCode(), value);
            }
         }
        
        return map;
    }
    
    
    private void initSheet(WritableWorkbook wwb, Map<String, List<ReadStatusReportHolder>> rsr, int sheetIndex, String msgType) throws Exception
    {
        //create sheet
        WritableSheet ws = wwb.createSheet(msgType, sheetIndex);
        //create excel style
        WritableCellFormat titleFormat = new WritableCellFormat(
            new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
        
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format1.setAlignment(Alignment.RIGHT);
        format1.setIndentation(1);
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setAlignment(Alignment.LEFT);
        
        ws.addCell(new Label(0, 0,"Unread Document Report",titleFormat));
        ws.mergeCells(0, 0, 6, 0);
        ws.addCell(new Label(0, 1, "Printed:", format2));
        ws.addCell(new Label(1, 1, DateUtil.getInstance().convertDateToString(new Date(),DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS),format2));
        ws.mergeCells(1, 1, 5, 1);
        
        int col = 5;
        
        for (Map.Entry<String, List<ReadStatusReportHolder>> entry : rsr.entrySet())
        {
            List<ReadStatusReportHolder> value = entry.getValue();
            //add cell
            
            ws.addCell(new Label(0, col - 2, "Buyer:", format2));
            ws.addCell(new Label(1, col - 2, value.get(0).getBuyerName() + "(" + value.get(0).getBuyerCode() + ")",format2));
            ws.mergeCells(1, col - 2, 5, 2);
            ws.addCell(new Label(0, col - 1, "S/No", format2));
            ws.addCell(new Label(1, col - 1, "Doc Type", format2));
            ws.addCell(new Label(2, col - 1, "Supplier Code", format2));
            ws.addCell(new Label(3, col - 1, "Doc No", format2));
            ws.addCell(new Label(4, col - 1, "Trans Date", format2));
            ws.addCell(new Label(5, col - 1, "Unread Time(hour)", format2));
            
            if (msgType.equals(ReadStatusReportActionStatus.PO.name()))
            {
                ws.addCell(new Label(6, col - 1, "PO Delivery Date From", format2));
                ws.addCell(new Label(7, col - 1, "PO Delivery Date To", format2));
            }
            else if (msgType.equals(ReadStatusReportActionStatus.RTV.name()))
            {
                ws.addCell(new Label(6, col - 1, "Collection Date", format2));
            }
            
            int lineSeq = 1;
            PoHeaderHolder poHeader = null;
            RtvHeaderHolder rtvHeader = null;
            for (ReadStatusReportHolder r : value)
            {
                if (r.getMsgType().equals(ReadStatusReportActionStatus.PO.name()))
                {
                    poHeader = poHeaderService.selectPoHeaderByKey(r.getDocOid());
                    if (poHeader == null)
                    {
                        continue;
                    }
                }
                else if (r.getMsgType().equals(ReadStatusReportActionStatus.RTV.name()))
                {
                    rtvHeader = rtvHeaderService.selectRtvHeaderByKey(r.getDocOid());
                    if (rtvHeader == null)
                    {
                        continue;
                    }
                }
                
                ws.addCell(new Label(0, col, lineSeq + "", format2));
                ws.addCell(new Label(1, col, r.getMsgType(), format2));
                ws.addCell(new Label(2, col, r.getSupplierCode(), format2));
                ws.addCell(new Label(3, col, r.getMsgRefNo(), format2));
                ws.addCell(new Label(4, col, DateUtil.getInstance().convertDateToString(r.getSentDate(), DateUtil.DATE_FORMAT_DDMMYYYY), format2));
                ws.addCell(new Label(5, col, DateUtil.getInstance().getDistanceHours(new Date(), r.getSentDate()) + "", format2));
                
                if (r.getMsgType().equals(ReadStatusReportActionStatus.PO.name()))
                {
                    ws.addCell(new Label(6, col, DateUtil.getInstance().convertDateToString(
                        poHeader.getDeliveryDateFrom(), 
                        DateUtil.DATE_FORMAT_DDMMYYYY), format2));
                    ws.addCell(new Label(7, col, DateUtil.getInstance().convertDateToString(
                        poHeader.getDeliveryDateTo(), 
                        DateUtil.DATE_FORMAT_DDMMYYYY), format2));
                }
                else if (r.getMsgType().equals(ReadStatusReportActionStatus.RTV.name()))
                {
                    ws.addCell(new Label(6, col, DateUtil.getInstance().convertDateToString(
                        rtvHeader.getCollectionDate(), 
                        DateUtil.DATE_FORMAT_DDMMYYYY), format2));
                }
                
                lineSeq++;
                col++;
            }
            col = col + 3;
        }
        
        //set column's width
        ws.setColumnView(0, 10);
        ws.setColumnView(1, 15);
        ws.setColumnView(2, 15);
        ws.setColumnView(3, 15);
        ws.setColumnView(4, 15);
        ws.setColumnView(5, 17);
        ws.setColumnView(6, 20);
        ws.setColumnView(7, 20);
    }
    
    
    
    // *****************************************************
    // handelException
    // *****************************************************
    private void handleException(Exception e)
    {
        String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

        msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
        msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                new String[] { tickNo }));

        MessageTargetHolder mt = new MessageTargetHolder();
        mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
        mt.setTargetURI(INIT);
        mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);

        msg.addMessageTarget(mt);
    }
    
    
    // *****************************************************
    // getter and setter
    // *****************************************************
    
    public MsgTransactionsExHolder getParam()
    {
        return param;
    }
    
    public void setParam(MsgTransactionsExHolder param)
    {
        this.param = param;
    }

    public Map<String, String> getDocType()
    {
        return docType;
    }

    public void setDocType(Map<String, String> docType)
    {
        this.docType = docType;
    }

    public InputStream getRptResult()
    {
        return rptResult;
    }

    public void setRptResult(InputStream rptResult)
    {
        this.rptResult = rptResult;
    }

    public String getRptFileName()
    {
        return rptFileName;
    }

    public void setRptFileName(String rptFileName)
    {
        this.rptFileName = rptFileName;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }


    public List<? extends Object> getBuyers()
    {
        return buyers;
    }


    public void setBuyers(List<? extends Object> buyers)
    {
        this.buyers = buyers;
    }
    
}
