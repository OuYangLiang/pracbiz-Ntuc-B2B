package com.pracbiz.b2bportal.base.holder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class MessageHolder implements Serializable
{
    private static final long serialVersionUID = 6184765923398786889L;

    private final static String VALUE_SUCCESS_SHORT = "S";
    private final static String VALUE_ERROR_SHORT = "E";
    private final static String KEY_MSG_TYPE = "MSG_TYPE";
    private final static String KEY_MSG_CONTENT = "MSG_CONTENT";

    private List<MessageTargetHolder> targets;
    private List<Map<String, String>> contents = null;

    private String title;

    public MessageHolder()
    {
        // Default Constructor
    }


    public void clearTargets()
    {
        targets = null;
    }


    public List<Map<String, String>> getContents()
    {
        return contents;
    }


    public void setContents(List<Map<String, String>> _contents)
    {
        this.contents = _contents;
    }


    public void saveError(String content_)
    {
        if (contents == null) contents = new ArrayList<Map<String, String>>();

        Map<String, String> map = new HashMap<String, String>();
        map.put(KEY_MSG_CONTENT, content_);
        map.put(KEY_MSG_TYPE, VALUE_ERROR_SHORT);

        this.contents.add(map);
    }


    public void saveSuccess(String content_)
    {
        if (contents == null) contents = new ArrayList<Map<String, String>>();

        Map<String, String> map = new HashMap<String, String>();
        map.put(KEY_MSG_CONTENT, content_);
        map.put(KEY_MSG_TYPE, VALUE_SUCCESS_SHORT);

        this.contents.add(map);
    }


    public List<MessageTargetHolder> getTargets()
    {
        return targets;
    }


    public void addMessageTarget(MessageTargetHolder target_)
    {
        if (targets == null) targets = new ArrayList<MessageTargetHolder>();

        targets.add(target_);
    }


    public void addMessageTarget(MessageTargetHolder target_, int idx_)
    {
        if (targets == null) targets = new ArrayList<MessageTargetHolder>();

        List<MessageTargetHolder> tmpRlt = new ArrayList<MessageTargetHolder>();

        if (targets.size() < 1)
        {
            targets.add(target_);
        }
        else
        {
            boolean blnAdded = false;
            for (int i = 0; i < targets.size(); i++)
            {
                if (idx_ <= i)
                {
                    tmpRlt.add(target_);

                    blnAdded = true;
                }

                tmpRlt.add(targets.get(i));
            }

            if (!blnAdded)
            {
                tmpRlt.add(target_);
            }

            targets = tmpRlt;
        }
    }


    public String getTitle()
    {
        return title;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }
    
    
    public String toString()
    {
        try
        {
            return BeanUtils.describe(this).toString();
        }
        catch (Exception exception)
        {
            return exception.getMessage();
        }
    }

}
