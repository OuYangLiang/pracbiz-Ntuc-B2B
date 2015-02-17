package com.pracbiz.b2bportal.base.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DateUtil
{
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_LOGIC_TIMESTAMP = "yyyyMMddHHmmssSSS";
    public static final String DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MIN = "yyyyMMddHH";
    public static final String DATE_FORMAT_DDMMYYYYHHMMSS = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_FORMAT_DDMMYYYY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_FORMAT_MMDDYYYY = "MM/dd/yyyy";
    public static final String DATE_FORMAT_MMDDYYYYHHMMSS = "MM/dd/yyyy HH:mm:ss";
    public static final String EBXML_DATE_FORMAT_YYYYMMDDHHMMSS="yyyy-MM-dd HH:mm:ss";
    public static final String EXCEL_DATE_FORMAT_DDMMYY = "dd-MM-yyyy";
    public static final String DATE_FORMAT_YYYYMMDDHH = "yyyyMMddHH";

    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

    private static DateUtil instance;

    private DateUtil()
    {
    }

    
    public static DateUtil getInstance()
    {
        synchronized(DateUtil.class)
        {
            if(instance == null)
            {
                instance = new DateUtil();
            }
        }

        return instance;
    }

    
    public String convertDateToString(Date inDate)
    {
        if(inDate == null)
            return "";

        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT,
            Locale.US);

        return df.format(inDate);
    }

    
    public String convertDateToString(Date inDate, String dateFormat)
    {
        String resultValue = "";

        try
        {
            SimpleDateFormat df = new SimpleDateFormat(dateFormat == null ? DEFAULT_DATE_FORMAT
                    : dateFormat, Locale.US);
            resultValue = df.format(inDate);
        }
        catch(Exception e)
        {
            log.error("convertDateToString failed [inDate :::: " + inDate
                + " && dateFormat :::: " + dateFormat + "]", e.getCause());
        }

        return resultValue;
    }

    
    public Date convertStringToDate(String inDate, String format)
    {
        if(inDate == null || inDate.isEmpty())
        {
            return null;
        }

        SimpleDateFormat df;
        Date resultDate = null;

        try
        {
            df = new SimpleDateFormat(format == null ? DEFAULT_DATE_FORMAT
                : format, Locale.US);
            df.setLenient(false);
            resultDate = df.parse(inDate);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }

        return resultDate;
    }

    
    public Date convertStringToDate(String inDate)
    {
        if(inDate == null || inDate.isEmpty())
        {
            return null;
        }

        SimpleDateFormat df;
        Date resultDate = null;

        try
        {
            df = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.US);
            df.setLenient(false);
            resultDate = df.parse(inDate);
        }
        catch(Exception e)
        {
            log.error("convertStringToDate failed [inDate :::: " + inDate
                + " && dateFormat :::: " + DEFAULT_DATE_FORMAT + "]",
                e.getCause());
        }

        return resultDate;
    }

    
    public int compareDate(Date inDate1, Date inDate2)
    {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(inDate1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(inDate2);

        return calendar1.compareTo(calendar2);
    }

    
    public Date dateAfterDays(Date inDate, int days)
    {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(inDate);
        calendar1.add(Calendar.DATE, days);

        return calendar1.getTime();
    }
    
    
    public int daysAfterDate(Date originalDate, Date compareDate)
    {
        if (originalDate == null || compareDate == null)
        {
            return 0;
        }
        Calendar c1 = Calendar.getInstance();  
        c1.setTime(originalDate);
        Calendar c2 = Calendar.getInstance();  
        c2.setTime(compareDate);
        
        int n = 0;
        
        while (!c1.after(c2))
        {
            c1.add(Calendar.DATE, 1);
            n ++;
        }
        
        return n - 1;
    }

    
    public boolean isAfterDays(Date _value1, Date _value2, int intervalDays)
    {
        Date newDate = dateAfterDays(_value1, intervalDays);

        if(compareDate(newDate, _value2) > 0)
        {
            return true;
        }

        return false;
    }

    
    public long getTimeStamp(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.getTimeInMillis();
    }

    
    public static String getLogicTimeStamp(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(
            DATE_FORMAT_LOGIC_TIMESTAMP, Locale.US);

        return sdf.format(date);
    }

    
    public String getCurrentLogicTimeStamp()
    {
        Date now = new Date();

        return getLogicTimeStamp(now);
    }

    
    public String getLogicTimeStampWithoutMsec(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(
            DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC, Locale.US);

        return sdf.format(date);
    }

    
    public String getCurrentLogicTimeStampWithoutMsec()
    {
        Date now = new Date();

        return getLogicTimeStampWithoutMsec(now);
    }
    
    
    public String getLogicTimeStampWithoutMin(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(
            DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MIN, Locale.US);

        return sdf.format(date);
    }
    
    
    public String getCurrentLogicTimeStampWithoutMin()
    {
        Date now = new Date();
        
        return getLogicTimeStampWithoutMin(now);
    }

    
    public String getCurrentYearAndMonth()
    {
        Date now = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM", Locale.US);

        return sdf.format(now);
    }

    
    public String getYearAndMonth(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM", Locale.US);

        return sdf.format(date);
    }

    
    public Date getDateFromTimeStamp(long timeStamp)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);

        return c.getTime();
    }
    
    public Date getFirstTimeOfDay(Date inDate)
    {
        if (inDate == null) return null;
        
        String inValue_ = getInstance().convertDateToString(inDate, DEFAULT_DATE_FORMAT)+" 00:00:00";
        
        return getInstance().convertStringToDate(inValue_, DEFAULT_DATE_FORMAT+" HH:mm:ss");
    }
    
    public Date getLastTimeOfDay(Date inDate)
    {
        if (inDate == null) return null;
        
        String inValue_ = getInstance().convertDateToString(inDate, DEFAULT_DATE_FORMAT)+" 23:59:59";
        
        return getInstance().convertStringToDate(inValue_, DEFAULT_DATE_FORMAT+" HH:mm:ss");
    }

    
    public Date getMaxTimeOfDate(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        
        return c.getTime();
    }
    
    
    public Date getMinTimeOfDate(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        
        return c.getTime();
    }
    
    public Date getCurrentFirstDayOfMonth()
    {
        Calendar c = Calendar.getInstance();
        c.setTime(getMinTimeOfDate(new Date()));
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
    public Date getCurrentLastDayOfMonth()
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }
    
    public long getDistanceHours(Date inDate1, Date inDate2)
    {
        long hours = 0;
        try
        {
            long time1 = inDate1.getTime();
            long time2 = inDate2.getTime();
            long diff;
            if(time1 < time2)
            {
                diff = time2 - time1;
            }
            else
            {
                diff = time1 - time2;
            }

            hours = diff / (60 * 60 * 1000);
        }
        catch(Exception e)
        {
            log.error("getDistanceHours failed [inDate1 :::: " + inDate1
                + " && inDate2 :::: " + inDate2 + "]", e.getCause());
        }
        
        return hours;
    }
    
//    public static void main(String[] args)
//    {
//        System.out.println(DateUtil.getInstance().getCurrentFirstDayOfMonth());
//        System.out.println(DateUtil.getInstance().getCurrentLastDayOfMonth());
//        System.out.println(DateUtil.getInstance().getDistanceHours(new Date(), DateUtil.getInstance().convertStringToDate("2012-06-20 14:11:31")));
//        System.out.println(DateUtil.getInstance().convertStringToDate("12/31/2013 14:12:11", DateUtil.DATE_FORMAT_MMDDYYYYHHMMSS));
//   }
    
}
