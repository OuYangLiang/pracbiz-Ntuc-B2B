package com.pracbiz.b2bportal.core.eai.file.translator;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author YinChi
 */
public abstract class SourceTranslator implements CoreCommonConstants
{
    @Autowired private MailBoxUtil mboxUtil;
    
    public final void doTranslation(File sourceFile, String fileType, BuyerHolder buyer) throws Exception
    {
        Map<String , byte[]> fileMap = this.readFile(sourceFile);
        
        if (fileMap != null && !fileMap.isEmpty())
        {
            Map<String, List<String>> errors = this.validateFile(fileMap);
            
            if (errors != null && !errors.isEmpty())
            {
                this.sendErrorNotification(errors);
                String invalidPath = mboxUtil.getFolderInBuyerInvalidPath(buyer.getMboxId(), DateUtil.getInstance().getCurrentYearAndMonth());
                FileUtil.getInstance().moveFile(sourceFile, invalidPath);
                return;
            }
            
            String outPath = mboxUtil.getBuyerOutPath(buyer.getMboxId());
            
            Map<String, byte[]> targetFiles = this.translate(fileMap, outPath, buyer, sourceFile);
            
            if (targetFiles != null && !targetFiles.isEmpty())
            {
                byte[] data = ("type:Portal\r\n" + sourceFile.getName()).getBytes(CommonConstants.ENCODING_UTF8);
                
                
                targetFiles.put(outPath + File.separator + "source_" + 
                    DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".txt", data);
                
                this.writeTranslated(targetFiles, fileType, buyer, outPath);
            }
            
            String archivePath = mboxUtil.getFolderInBuyerArchOutPath(buyer.getMboxId(), DateUtil.getInstance().getCurrentYearAndMonth());
            FileUtil.getInstance().moveFile(sourceFile, archivePath);
        }
    }
    
    protected Map<String, byte[]> readFile(File file) throws Exception
    {
        Map<String , byte[]> fileMap = new HashMap<String, byte[]>();
        
        if (file.getName().endsWith(".zip"))
        {
            fileMap = GZIPHelper.getInstance().readFileFromZip(file);
        }
        else
        {
            fileMap.put(file.getName(), FileUtils.readFileToByteArray(file));
        }
        
        return fileMap;
    }
    
    
    protected abstract Map<String, List<String>> validateFile(Map<String, byte[]> files) throws Exception;
    
    
    protected abstract Map<String, byte[]> translate(Map<String, byte[]> files, String outPath, BuyerHolder buyer,
        File sourceFile) throws Exception;
    
    private void sendErrorNotification(Map<String, List<String>> errorMsgs) throws Exception
    {
        if (errorMsgs == null || errorMsgs.isEmpty())
        {
            return;
        }
    }
    
    
    private void writeTranslated(Map<String, byte[]> filesMap, String fileType, BuyerHolder buyer, String targetPath) throws Exception
    {
        List<File> files = new ArrayList<File>();
        for (Map.Entry<String, byte[]> entry : filesMap.entrySet())
        {
            File file = new File(entry.getKey());
            FileParserUtil.getInstance().writeContent(file, new String(entry.getValue(), Charset.defaultCharset()));

            files.add(file);
        }
        
        GZIPHelper.getInstance().doStandardZip(files, fileType, buyer.getBuyerCode(), targetPath);
    }
    
//    private byte[] generateSourceNameFile(String outPath, String sourceFileName) throws Exception
//    {
//        File file = new File(outPath + File.separator + "source_" + 
//            DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".txt");
//        byte[] data = ("type:Portal\r\n" + sourceFileName).getBytes(CommonConstants.ENCODING_UTF8);
//        //FileUtil.getInstance().writeByteToDisk(data, file.getPath());
//        
//        return data;
//    }
}
