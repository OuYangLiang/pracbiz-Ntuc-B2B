package com.pracbiz.b2bportal.base.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JdomXmlHelper
{
    private static final Logger log = LoggerFactory.getLogger(JdomXmlHelper.class);

    private static JdomXmlHelper instance;


    private JdomXmlHelper()
    {
    }


    public static JdomXmlHelper getInstance()
    {
        synchronized (JdomXmlHelper.class)
        {
            if (instance == null)
            {
                instance = new JdomXmlHelper();
            }
        }

        return instance;
    }


    public String obtainValueFrom(Document doc, String location)
    {
        try
        {
            Element root = doc.getRootElement();
            String[] str = location.split("\\.");
            String attrName = null;

            Element target = root;
            for (int i = 0; i < str.length; i++)
            {
                String strt = str[i];
                if (strt.startsWith("@"))
                {
                    attrName = strt.substring(1);
                }
                else
                {
                    target = getTargetElement(target, strt);
                }
            }

            if (attrName == null)
            {
                return target.getText();
            }
            else
            {
                return target.getAttributeValue(attrName);
            }
        }
        catch (Exception e)
        {
            log.info("No such location exists -----" + location);
            return null;
        }
    }

    
    public String obtainValueFrom(Document doc, String location, String name,
            Namespace ns)
    {
        try
        {
            Element root = doc.getRootElement().getChild(name, ns);
            String[] str = location.split("\\.");
            String attrName = null;

            Element target = root;
            for (int i = 0; i < str.length; i++)
            {
                String strt = str[i];
                if (strt.startsWith("@"))
                {
                    attrName = strt.substring(1);
                }
                else
                {
                    target = getTargetElement(target, strt);
                }
            }

            if (attrName == null)
            {
                return target.getText();
            }
            else
            {
                return target.getAttributeValue(attrName);
            }
        }
        catch (Exception e)
        {
            log.info("No such location exists -----" + location);
            return null;
        }
    }
    
    
    private Element getTargetElement(Element target, String strt)
    {
        if (strt.endsWith(")"))
        {
            int startIndex = strt.indexOf('(');
            int endIndex = strt.indexOf(')');
            
            int index = Integer.parseInt(strt.substring(startIndex + 1,
                    endIndex));
            
            return (Element) target.getChildren(
                    strt.substring(0, startIndex)).get(index);
        }
        else if (strt.endsWith("]"))
        {
            int startIndex = strt.indexOf('[');
            int endIndex = strt.indexOf(']');
            
            Map<String, String> attrMap = convertAttrsToMap(strt.substring(startIndex + 1, endIndex).split(","));
            
            List<Element> children = target.getChildren(
                    strt.substring(0, startIndex));
            for (Element child : children)
            {
                boolean flag = true;
                Map<String, String> elementAttrs = convertAttrsToMap(child);
                for (Entry<String, String> entry : attrMap.entrySet())
                {
                    String key = entry.getKey();
                    if (!elementAttrs.containsKey(key))
                    {
                        flag = false;
                        break;
                    }
                    if (!attrMap.get(key).equalsIgnoreCase(elementAttrs.get(key)))
                    {
                        flag = false;
                        break;
                    }
                }
                if (flag)
                {
                    return child;
                }
            }
            return null;
        }
        else 
        {
            return (Element) target.getChild(strt);
        }
    }
    
    
    private Map<String, String> convertAttrsToMap(String[] attrs)
    {
        Map<String, String> map = new HashMap<String, String>();
        for (String attr : attrs)
        {
            if (attr.trim().isEmpty())
            {
                continue;
            }
            String[] str = attr.split("=");
            if (str.length != 2)
            {
                continue;
            }
            map.put(str[0], str[1]);
        }
        return map;
    }
    
    
    private Map<String, String> convertAttrsToMap(Element element)
    {
        Map<String, String> map = new HashMap<String, String>();
        List<Attribute> attributes = element.getAttributes();
        for (Attribute attr : attributes)
        {
            map.put(attr.getName(), attr.getValue());
        }
        return map;
    }


    public void setValueToDoc(Document doc, String location, String value)
    {
        Element target = doc.getRootElement();
        String[] str = location.split("\\.");
        Element child = null;
        String attrName = null;

        for (int i = 0; i < str.length; i++)
        {
            String strt = str[i];

            if (strt.endsWith(")"))
            {
                int startIndex = strt.indexOf('(');
                int endIndex = strt.indexOf(')');

                int index = Integer.parseInt(strt.substring(startIndex + 1,
                        endIndex));

                strt = strt.substring(0, startIndex);

                List<Element> list = target.getChildren(strt);

                if (list == null || list.size() <= index)
                {
                    Element newEle = new Element(strt);

                    target.addContent(newEle);

                    target = newEle;
                }
                else
                {
                    target = (Element) list.get(index);
                }
            }
            else if (strt.startsWith("@"))
            {
                attrName = strt.substring(1);
            }
            else
            {
                child = target.getChild(strt);
                if (child == null)
                {
                    child = new Element(strt);
                    target.addContent(child);
                }

                target = child;
            }
        }

        if (attrName == null)
        {
            target.setText(value);
        }
        else
        {
            target.setAttribute(new Attribute(attrName, value));
        }
    }
}
