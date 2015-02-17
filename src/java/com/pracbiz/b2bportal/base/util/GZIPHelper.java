package com.pracbiz.b2bportal.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class GZIPHelper
{
    private static GZIPHelper instance;


    private GZIPHelper()
    {
    }


    /*
     * public static void main(String[] arsg) throws IOException { File f1 = new
     * File("/Users/ouyang/Desktop/abc/PO_FP_52261_25682428.xml"); File f2 = new
     * File("/Users/ouyang/Desktop/abc/PO_FP_57570_25676475.xml"); File f3 = new
     * File("/Users/ouyang/Desktop/abc/PO_FP_74735_25677902.xml"); File f4 = new
     * File("/Users/ouyang/Desktop/abc/summary.csv");
     * 
     * List<File> files = new ArrayList<File>(); files.add(f1); files.add(f2);
     * //files.add(f3); files.add(f4);
     * 
     * GZIPHelper.getInstance().doZip(files, "/Users/ouyang/Desktop/abc",
     * "PO_FP_20130514135905.zip");
     * 
     * }
     */

    public static GZIPHelper getInstance()
    {
        synchronized (GZIPHelper.class)
        {
            if (instance == null)
            {
                instance = new GZIPHelper();
            }
        }

        return instance;
    }


    public void doZip(List<File> files, String outputPath, String outputName)
            throws IOException
    {
        ZipOutputStream zos = null;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        byte[] buf = new byte[1024];
        int readbytes = 0;

        File zipFile = new File(outputPath, outputName);

        try
        {
            BufferedInputStream bis = null;
            FileInputStream fis = null;

            fos = new FileOutputStream(zipFile);
            bos = new BufferedOutputStream(fos);
            zos = new ZipOutputStream(bos);

            for (File file : files)
            {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);

                String filename = file.getName();
                ZipEntry entry = new ZipEntry(filename);

                zos.putNextEntry(entry);

                try
                {
                    while ((readbytes = bis.read(buf)) != -1)
                    {
                        zos.write(buf, 0, readbytes);
                    }
                    zos.closeEntry();
                }
                finally
                {
                    if (bis != null)
                    {
                        bis.close();
                        bis = null;
                    }
                    if (fis != null)
                    {
                        fis.close();
                        fis = null;
                    }
                }
            }

        }
        finally
        {
            if (zos != null)
            {
                zos.close();
                zos = null;
            }
            if (bos != null)
            {
                bos.close();
                bos = null;
            }
            if (fos != null)
            {
                fos.close();
                fos = null;
            }
        }
    }


    public void unZip(File zipFile, String outputPath) throws IOException
    {
        ZipInputStream zis = null;
        BufferedInputStream bis = null;
        FileInputStream fis = null;

        byte[] buf = new byte[1024];
        int readBytes;

        try
        {
            fis = new FileInputStream(zipFile);
            bis = new BufferedInputStream(fis);
            zis = new ZipInputStream(bis);

            ZipEntry entry = null;

            while ((entry = zis.getNextEntry()) != null)
            {
                String filename = entry.getName();
                File newFile = new File(outputPath, filename);

                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                try
                {
                    fos = new FileOutputStream(newFile);
                    bos = new BufferedOutputStream(fos);

                    while ((readBytes = zis.read(buf)) != -1)
                    {
                        bos.write(buf, 0, readBytes);
                    }
                    bos.flush();
                }
                finally
                {
                    if (bos != null)
                    {
                        bos.close();
                        bos = null;
                    }
                    if (fos != null)
                    {
                        fos.close();
                        fos = null;
                    }
                }

            }
        }
        finally
        {
            if (zis != null)
            {
                zis.close();
                zis = null;
            }
            if (bis != null)
            {
                bis.close();
                bis = null;
            }
            if (fis != null)
            {
                fis.close();
                fis = null;
            }
        }
    }


    public List<File> unZipAndReturnExtractFiles(File zipFile, String outputPath)
            throws IOException
    {
        List<File> rlt = new ArrayList<File>();

        ZipInputStream zis = null;
        BufferedInputStream bis = null;
        FileInputStream fis = null;

        byte[] buf = new byte[1024];
        int readBytes;

        try
        {
            fis = new FileInputStream(zipFile);
            bis = new BufferedInputStream(fis);
            zis = new ZipInputStream(bis);

            ZipEntry entry = null;

            while ((entry = zis.getNextEntry()) != null)
            {
                String filename = entry.getName();
                File newFile = new File(outputPath, filename);

                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                try
                {
                    fos = new FileOutputStream(newFile);
                    bos = new BufferedOutputStream(fos);

                    while ((readBytes = zis.read(buf)) != -1)
                    {
                        bos.write(buf, 0, readBytes);
                    }
                    bos.flush();

                    rlt.add(newFile);
                }
                finally
                {
                    if (bos != null)
                    {
                        bos.close();
                        bos = null;
                    }
                    if (fos != null)
                    {
                        fos.close();
                        fos = null;
                    }
                }

            }
        }
        finally
        {
            if (zis != null)
            {
                zis.close();
                zis = null;
            }
            if (bis != null)
            {
                bis.close();
                bis = null;
            }
            if (fis != null)
            {
                fis.close();
                fis = null;
            }
        }

        return rlt;
    }


    public List<String> getFilenamesFromZipFilePath(String filePath)
            throws Exception
    {
        ZipInputStream zin = null;
        InputStream in = null;
        List<String> rlt = null;
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(filePath);
            in = new BufferedInputStream(inputStream);
            zin = new ZipInputStream(in);
            ZipEntry ze;

            rlt = new ArrayList<String>();
            while ((ze = zin.getNextEntry()) != null)
            {
                if (ze.isDirectory())
                {
                    continue;
                }
                else
                {
                    rlt.add(ze.getName());
                }
            }
        }
        finally
        {
            if (zin != null)
            {
                zin.close();
                zin = null;
            }
            if (in != null)
            {
                in.close();
                in = null;
            }
            if (inputStream != null)
            {
                inputStream.close();
                inputStream = null;
            }
            
        }

        return rlt;
    }
    
    
    public Map<String, List<String>> readFileContentFromZip(File file) throws IOException
    {
    	Map<String, List<String>> rlt = new HashMap<String, List<String>>();
    	
    	Map<String, byte[]> fileMap = this.readFileFromZip(file);
    	for (Entry<String, byte[]> entry : fileMap.entrySet())
    	{
    		rlt.put(entry.getKey(), FileParserUtil.getInstance().readLines(entry.getValue()));
    	}
    	
    	return rlt;
    }


    public Map<String, byte[]> readFileFromZip(File file) throws IOException
    {
    	Map<String, byte[]> rlt = new HashMap<String, byte[]>();
    	
    	ZipInputStream zis = null;
        BufferedInputStream bis = null;
        FileInputStream fis = null;

        byte[] buf = new byte[1024];
        int readBytes;

        try
        {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            zis = new ZipInputStream(bis);

            ZipEntry entry = null;

            while ((entry = zis.getNextEntry()) != null)
            {
                String filename = entry.getName();

                ByteArrayOutputStream bos = null;
                try
                {
                	bos = new ByteArrayOutputStream();

                    while ((readBytes = zis.read(buf)) != -1)
                    {
                        bos.write(buf, 0, readBytes);
                    }
                    bos.flush();
                    
                    rlt.put(filename, bos.toByteArray());
                }
                finally
                {
                    if (bos != null)
                    {
                        bos.close();
                        bos = null;
                    }
                }

            }
        }
        finally
        {
            if (zis != null)
            {
                zis.close();
                zis = null;
            }
            if (bis != null)
            {
                bis.close();
                bis = null;
            }
            if (fis != null)
            {
                fis.close();
                fis = null;
            }
        }
        
        return rlt;
    }
    
    
    public byte[] zipByte(Map<String, byte[]> inputs) throws IOException
    {
        byte[] result = null;
        ZipOutputStream zos = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try
        {

            zos = new ZipOutputStream(out);

            for (Map.Entry<String, byte[]> input : inputs.entrySet())
            {
                ZipEntry entry = new ZipEntry(input.getKey());

                zos.putNextEntry(entry);
                zos.write(input.getValue());
                zos.closeEntry();
            }
            
            result = out.toByteArray();
        }
        finally
        {
            if (out != null)
            {
                out.close();
                out = null;
            }
            if (zos != null)
            {
                zos.close();
                zos = null;
            }
        }
        
        
        return result;
        
    }
    
    
    public void doStandardZip(List<File> fileList, String fileType, String buyerCode, String targetPath) throws Exception
    {
        if (fileList != null && !fileList.isEmpty())
        {
            String zipFilename = this.getStandardZipFilename(fileType, buyerCode);
            
            File zipFile = new File(targetPath, zipFilename);
            
            while (zipFile.exists())
            {
                zipFilename = this.getStandardZipFilename(fileType, buyerCode);;
                
                zipFile = new File(targetPath, zipFilename);
            }
            
            GZIPHelper.getInstance().doZip(fileList, targetPath, zipFilename);
            
            for (File file : fileList)
            {
                FileUtil.getInstance().deleleAllFile(file);
            }
        }
    }
    
    //**************************
    //private method
    //**************************
    private String getStandardZipFilename(String fileType, String buyerCode) throws Exception
    {
        String zipFilename = fileType + "_"
            + buyerCode
            + "_"
            + DateUtil.getInstance().convertDateToString(
                new Date(),
                DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC) + ".zip";
        
        return zipFilename;
    }
    
}
