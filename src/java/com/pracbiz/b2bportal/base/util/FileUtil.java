package com.pracbiz.b2bportal.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtil
{
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String SYMBOL_LEFT="[";
    private static final String SYMBOL_RIGHT="]";
    private static final String TO="] to [";

    private static FileUtil instance;
    private FileUtil(){}
    
    public static FileUtil getInstance()
    {
        synchronized(FileUtil.class)
        {
            if (instance == null)
            {
                instance = new FileUtil();
            }
        }
        
        return instance;
    }
    
    
    public void appendLine(File file, String str) throws IOException
    {
        RandomAccessFile raf = null;

        try
        {
            raf = new RandomAccessFile(file, "rws");
            raf.seek(raf.length());
            raf.writeBytes(str);
        }
        finally
        {
            if (null != raf)
            {
            	raf.close();
            	raf = null;
            }
        }
    }
    
    
    public String trimAllExtension(String filename)
    {
        return filename.replaceAll("\\..*", "");
    }


    public String getFileExtension(String filename)
    {
        char point= '.';
        int pot = filename.lastIndexOf(point);

        return filename.substring(pot + 1, filename.length());
    }


    public boolean isFileUsing(File file)
    {
        try
        {
            return !file.renameTo(file);
        }
        catch (Exception e)
        {
            return true;
        }
    }


    public boolean isFileUsing(String fileName)
    {
        File file = new File(fileName);

        boolean rlt = isFileUsing(file);

        file = null;

        return rlt;
    }


    public void deleleAllFile(File f) throws IOException
    {
        if (f.exists() && f.isFile() && !f.delete())
        {
            throw new IOException("faild delete file " + f.getPath());
        }
        else if (f.exists() && f.isDirectory())
        {
            File[] fx = f.listFiles();

            for (int i = 0; i < fx.length; i++)
            {
                deleleAllFile(fx[i]);
            }

            if (!f.delete())
            {
                throw new IOException();
            }
        }

        if (f.exists() && f.isFile() && !f.delete())
        {
            throw new IOException();
        }
    }


    public void copyFile(File source, File target, boolean outputLog)
            throws IOException
    {
        if(outputLog)
        {
            log.info("Start to copy file " + SYMBOL_LEFT + source.getPath()
                + TO + target.getPath() + SYMBOL_RIGHT + ".");
        }

        InputStream is = null;
        FileInputStream fis = null;
        OutputStream os = null;
        FileOutputStream fos = null;
        int readbyte = 0;
        byte[] buf = new byte[512];

        try
        {
            fis = new FileInputStream(source);
            is = new BufferedInputStream(fis);

            fos = new FileOutputStream(target);
            os = new BufferedOutputStream(fos);

            while ((readbyte = is.read(buf)) != -1)
            {
                os.write(buf, 0, readbyte);
                os.flush();
            }

            if(outputLog)
            {
                log.info("Copy file " + SYMBOL_LEFT + source.getPath() + TO
                    + target.getPath() + SYMBOL_RIGHT + " successfully.");
            }

        }
        finally
        {
            if (fis != null)
            {
                fis.close();
                fis = null;
            }
            if (is != null)
            {
                is.close();
                is = null;
            }
            if (fos != null)
            {
                fos.close();
                fos = null;
            }
            if (os != null)
            {
                os.close();
                os = null;
            }
        }
    }


    public void renameFile(File source, File target) throws IOException
    {
        copyFile(source, target, false);

        if (!source.delete())
        {
            throw new IOException();
        }
    }


    public boolean moveFile(File source, String toPath) throws IOException
    {
        String filename = source.getName();

        File path = new File(toPath);
        if (!path.isDirectory()) 
        {
            createDir(path);
        }
            

        File newFile = new File(path, filename);

        try
        {
            log.info("Start to move file " + SYMBOL_LEFT + source.getPath()
                + TO + toPath + SYMBOL_RIGHT + ".");

            renameFile(source, newFile);

            log.info("Move file " + SYMBOL_LEFT + source.getPath() + TO
                + toPath + SYMBOL_RIGHT + " successfully.");

            return true;
        }
        catch (Exception e)
        {
            log.warn("Fail to move file " + SYMBOL_LEFT + source.getPath() + TO
                + toPath + SYMBOL_RIGHT + ".");
            return false;
        }
    }


    public boolean isFileStable(File file)
    {
        long originalSize = file.length();
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            // ignore
        }
        File newFile = new File(file.getParent(), file.getName());
        long size = newFile.length();

        return originalSize == size;
    }


    public File[] getStableFiles(File[] files)
    {
        long[] originalSize = new long[files.length];
        long[] finalSize = new long[files.length];

        for (int i = 0; i < files.length; i++)
        {
            File file = (File) files[i];
            originalSize[i] = file.length();
        }

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {}

        for (int i = 0; i < files.length; i++)
        {
            File newFile = new File(files[i].getPath());
            finalSize[i] = newFile.length();
        }

        List<File> tmpList = new ArrayList<File>();

        for (int i = 0; i < finalSize.length; i++)
        {
            if (originalSize[i] == finalSize[i])
            {
                tmpList.add(files[i]);
            }
        }

        File[] result = new File[tmpList.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = tmpList.get(i);
        }

        return result;
    }


    public void createDir(File file) throws IOException
    {
        synchronized(FileUtil.class)
        {
            if(!file.isDirectory() && !file.exists() && !file.mkdirs())
            {
                throw new IOException();
            }
        }
    }
    
    
    public boolean isSourcePathExist(File sourcePath)
    {
        if (!sourcePath.exists())
        {
            log.error("[FileUtil]  ::::  " + SYMBOL_LEFT + sourcePath + SYMBOL_RIGHT +" does not exist.");

            return false;
        }

        if (!sourcePath.isDirectory())
        {
            log.error("[FileUtil] ::::  " + SYMBOL_LEFT + sourcePath + SYMBOL_RIGHT +" is not a directory.");

            return false;
        }

        return true;
    }

    
    public void writeByteToDisk(byte[] data, String targetFile)
            throws Exception
    {
        FileOutputStream out = null;

        try
        {
            out = new FileOutputStream(targetFile);
            out.write(data);
            out.flush();
        }
        finally
        {
            if (out != null)
            {
                out.close();
                out = null;
            }
        }
    }


    public boolean isAnyFilesExistInFolder(File folder)
    {
        if (folder == null || !folder.exists())
            return false;
        else if (!folder.isDirectory()) return true;

        File[] files = folder.listFiles();

        boolean tag = false;

        if (files != null)
        {
            for (int i = 0; i < files.length; i++)
            {
                if (isAnyFilesExistInFolder(files[i]))
                {
                    tag = true;
                    break;
                }
            }
        }
        return tag;
    }


    public boolean isEmptyPath(String path)
    {
        File file = new File(path);
        File[] inFiles = file.listFiles();
        if (inFiles != null && inFiles.length > 0) return false;
        return true;
    }


    public String getFileFullPath(String _filePath, String _fileName)
    {
        String rlt = "";

        if (_filePath.substring(_filePath.length() - 1, _filePath.length())
                .equals("/"))
        {
            rlt = _filePath + _fileName;
        }
        else if (_filePath
                .substring(_filePath.length() - 1, _filePath.length()).equals(
                        "\\"))
        {
            rlt = _filePath + _fileName;
        }
        else
        {
            rlt = _filePath + "/" + _fileName;
        }

        return rlt;
    }


    public String getExtension(File f)
    {
        return (f == null) ? "" : getExtension(f.getName());
    }


    public String getExtension(String filename)
    {
        return getExtension(filename, "");
    }


    public String getExtension(String filename, String defExt)
    {
        if ((filename != null) && (filename.length() > 0))
        {
            int i = filename.lastIndexOf('.');

            if ((i > 0) && (i < (filename.length() - 1)))
            {
                return filename.substring(i + 1);
            }
        }

        return defExt;
    }


    public String trimExtension(String filename)
    {
        if ((filename != null) && (filename.length() > 0))
        {
            int i = filename.lastIndexOf('.');

            if ((i > -1) && (i < (filename.length())))
            {
                return filename.substring(0, i);
            }
        }

        return filename;
    }


    public String getFileName(String fileFullPathName)
    {
        if (fileFullPathName != null && fileFullPathName.length() > 0)
        {
            int index = fileFullPathName.lastIndexOf(FILE_SEPARATOR);

            if ((index > 0) && (index < (fileFullPathName.length() - 1)))
            {
                return fileFullPathName.substring(index + 1);
            }
            else
            {
                return fileFullPathName;
            }
        }
        else
        {
            return fileFullPathName;
        }
            
    }


    public boolean isFileExist(String filePath, String fileName)
    {
        File file = null;
        try
        {
            file = new File(filePath + FILE_SEPARATOR + fileName);

            boolean rlt = file.exists();

            return rlt;
        }
        catch (Exception e)
        {
            log.error(e.toString());
            return false;
        }
        finally
        {
            file = null;
        }
    }


    public String replaceFileNameExtension(String fileName, String newExt)
    {
        return FilenameUtils.removeExtension(fileName) + "." + newExt;
    }


    public void saveFile(String filename, String filePath, File file)
            throws IOException
    {
        InputStream in = null;
        OutputStream out = null;
        FileInputStream fileinput = null;
        FileOutputStream fileoutput = null;

        try
        {
            File target = new File(filePath, filename);
            fileinput = new FileInputStream(file);
            fileoutput = new FileOutputStream(target);
            in = new BufferedInputStream(fileinput, 1024);
            out = new BufferedOutputStream(fileoutput, 1024);
            byte[] buffer = new byte[1024];
            while (in.read(buffer) > 0)
            {
                out.write(buffer);
            }
        }
        finally
        {
            if (in != null) 
            {
                in.close();
            }
            
            if (out != null)
            {
                out.close();
            }
            
            if (fileinput != null) 
            {
                fileinput.close();
            }
            
            if (fileoutput != null) 
            {
                fileoutput.close();
            }
        }
    }


    public FileInputStream downloadFile(String filePath, String filename)
            throws Exception
    {
        File f = new File(filePath, filename);

        return new FileInputStream(f);
    }


    public byte[] readByteFromDisk(String targetFile) throws Exception
    {
        FileInputStream in = null;
        ByteArrayOutputStream out = null;
        byte[] rlt = null;

        byte[] buf = new byte[1024];
        int readByte;
        try
        {
            if (targetFile!=null)
            {
                in = new FileInputStream(targetFile);

                out = new ByteArrayOutputStream();

                while ((readByte = in.read(buf)) != -1)
                {
                    out.write(buf, 0, readByte);
                }

                rlt = out.toByteArray();
            }
        }
        finally
        {
            buf = null;

            if (in != null)
            {
                in.close();
                in = null;
            }
            if (out != null)
            {
                out.close();
                out = null;
            }
        }

        return rlt;
    }
    
    
    public byte[] readByteFromInputStream(InputStream input) throws Exception
    {
        BufferedInputStream bis = null;
        ByteArrayOutputStream out = null;
        byte[] rlt = null;
        
        byte[] buf = new byte[1024];
        int readByte;
        try
        {
            bis = new BufferedInputStream(input);
            
            out = new ByteArrayOutputStream();
            
            while ((readByte = bis.read(buf)) != -1)
            {
                out.write(buf, 0, readByte);
            }
            
            rlt = out.toByteArray();
        }
        finally
        {
            buf = null;
            
            if (bis != null)
            {
                bis.close();
                bis = null;
            }
            if (out != null)
            {
                out.close();
                out = null;
            }
        }
        
        return rlt;
    }
    
    
    public void touchTok(File path, String name) throws IOException
    {
        if (!path.isDirectory())
        {
            this.createDir(path);
        }
        
        File tok = new File(path, name + ".tok");
        File proc= new File(path, name + ".tok.proc");
        
        if(!tok.exists() && !proc.exists() && !tok.createNewFile())
        {
            throw new IOException("Failed to create token file ["
                + tok.getPath() + "].");
        }
    }
    
    
    public void changeFileExtension(File file, String extension)
        throws IOException
    {
        int index = file.getName().lastIndexOf(".");
        if (index == -1) index = file.getName().length();
        
        File renamed = new File(file.getParent(), file.getName().substring(0,
            index) + extension);

        if(!file.renameTo(renamed))
        {
            throw new IOException("Failed to rename file [" + file.getName()
                + "] to [" + renamed.getName() + "].");
        }
    }
    
    // *****************************************************
    // static methods
    // *****************************************************

    public static void main(String[] args)
    {
        // add test code here.
        
    }
}
