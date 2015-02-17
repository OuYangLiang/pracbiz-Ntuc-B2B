package com.pracbiz.b2bportal.core.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.ItemStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.ItemMasterHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.ItemMasterSummaryHolder;
import com.pracbiz.b2bportal.core.mapper.ItemMasterMapper;
import com.pracbiz.b2bportal.core.service.ItemMasterService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

public class ItemMasterServiceImpl extends DBActionServiceDefaultImpl<ItemMasterHolder>
        implements ItemMasterService, CoreCommonConstants, ApplicationContextAware
{
    @Autowired ItemMasterMapper mapper;
    @Autowired MsgTransactionsService msgTransactionsService;
    private ApplicationContext ctx;
    @Autowired private MailBoxUtil mboxUtil;
    @Autowired private SupplierService supplierService;
    
    @Override
    public int getCountOfSummary(ItemMasterSummaryHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }


    @Override
    public List<ItemMasterSummaryHolder> getListOfSummary(ItemMasterSummaryHolder param)
            throws Exception
    {
        return mapper.getListOfSummary(param);
    }


    @Override
    public List<ItemMasterHolder> select(ItemMasterHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(ItemMasterHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(ItemMasterHolder oldObj_,
            ItemMasterHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(ItemMasterHolder oldObj_,
            ItemMasterHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(ItemMasterHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public void saveItemIn(CommonParameterHolder cp, File file, String path, ItemMasterHolder item,
            MsgTransactionsHolder msg) throws Exception
    {
        if (file == null || path == null || path.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
		String fileName = MsgType.ITEMIN.name() + DOC_FILENAME_DELIMITOR
				+ msg.getBuyerCode() + DOC_FILENAME_DELIMITOR
				+ msg.getSupplierCode() + DOC_FILENAME_DELIMITOR
				+ msg.getMsgRefNo() + ".xls";
		String targetFile = path + PS + fileName;
        
        if (!(new File(path)).exists() || (new File(path)).isDirectory())
        {
        	FileUtil.getInstance().createDir(new File(path));
        }
        msg.setOriginalFilename(fileName);
        msgTransactionsService.insert(msg);
        this.getMeBean().auditInsert(cp, item);
        
        FileUtil.getInstance().writeByteToDisk(
                FileUtil.getInstance().readByteFromDisk(file.getPath()),
                targetFile);
    }


    @Override
    public void sendItemIn(CommonParameterHolder cp, ItemMasterHolder oldItem, MsgTransactionsHolder msg, File targetFile, String mboxId) throws Exception
    {
        ItemMasterHolder newItem = new ItemMasterHolder();
        BeanUtils.copyProperties(oldItem, newItem);
        newItem.setItemStatus(ItemStatus.SENT);
        this.getMeBean().auditUpdateByPrimaryKeySelective(cp, oldItem, newItem);
        FileUtil.getInstance().moveFile(targetFile, mboxUtil.getSupplierOutPath(mboxId));
    }


    @Override
    public ItemMasterHolder selectByKey(BigDecimal itemOid) throws Exception
    {
        if (itemOid == null)
        {
            throw new IllegalArgumentException();
        }
        ItemMasterHolder param = new ItemMasterHolder();
        param.setItemOid(itemOid);
        List<ItemMasterHolder> list = select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }
    
    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
        this.ctx = ctx;
    }
    
    
    private ItemMasterService getMeBean()
    {
        return ctx.getBean("itemMasterService", ItemMasterService.class);
    }


    @Override
    public void deleteItemIn(CommonParameterHolder cp,
            BigDecimal itemOid) throws Exception
    {
        if (itemOid == null)
        {
            throw new IllegalArgumentException();
        }
        ItemMasterHolder param = selectByKey(itemOid);
        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(itemOid);
        
        getMeBean().auditDelete(cp, param);
        msgTransactionsService.delete(msg);
        
        SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
        
        File targetFile = new File(mboxUtil.getSupplierTmp(supplier.getMboxId()), msg.getOriginalFilename());
        FileUtil.getInstance().deleleAllFile(targetFile);
    }

}
