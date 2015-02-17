package com.pracbiz.client.utils;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

public class FileMonitor
{
    private static final FileMonitor instance = new FileMonitor();
    private final Timer timer;
    private final Hashtable<String, FileMonitorTask> timerEntries;

    public static FileMonitor getInstance()
    {
        return instance;
    }

    private FileMonitor()
    {
        // Create timer, run timer thread as daemon.
        timer = new Timer(true);
        timerEntries = new Hashtable<String, FileMonitorTask>();
    }

    
    public void addFileChangeListener(FileChangeListener listener,
        String fileName, long period) throws IOException
    {
        addFileChangeListener(listener, new File(fileName), period);
    }

    
    public void addFileChangeListener(FileChangeListener listener, File file,
        long period) throws IOException
    {
        removeFileChangeListener(listener, file);
        FileMonitorTask task = new FileMonitorTask(listener, file);
        timerEntries.put(file.toString() + listener.hashCode(), task);
        timer.schedule(task, period, period);
    }

    
    public void removeFileChangeListener(FileChangeListener listener,
        String fileName)
    {
        removeFileChangeListener(listener, new File(fileName));
    }

    
    public void removeFileChangeListener(FileChangeListener listener, File file)
    {
        FileMonitorTask task = timerEntries.remove(file.toString()
            + listener.hashCode());

        if(task != null)
        {
            task.cancel();
        }
    }

    
    protected void fireFileChangeEvent(FileChangeListener listener, File file)
    {
        listener.fileChanged(file);
    }

    
    class FileMonitorTask extends TimerTask
    {
        FileChangeListener listener;

        File monitoredFile;

        long lastModified;

        public FileMonitorTask(FileChangeListener listener, File file)
            throws IOException
        {
            this.listener = listener;
            this.lastModified = 0;
            monitoredFile = file;
            if(!monitoredFile.exists() && !monitoredFile.createNewFile())
            {
                throw new IOException("File failed to create.");
            }
            this.lastModified = monitoredFile.lastModified();
        }

        
        public void run()
        {
            long lastModified = monitoredFile.lastModified();
            if(lastModified != this.lastModified)
            {
                this.lastModified = lastModified;
                fireFileChangeEvent(this.listener, monitoredFile);
            }
        }
    }
}
