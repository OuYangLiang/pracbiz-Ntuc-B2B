package com.pracbiz.b2bportal.core.holder;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestResponseHolder implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private int httpStatusCode;

    private String result;

    private String nonce;

    private String resource;

    private List<RestErrorHolder> errorList;
    
    
    public String toJsonString() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("result", this.getResult());
        
        if (null != this.getNonce() && !this.getNonce().trim().isEmpty())
        {
            json.put("nonce", this.getNonce());
        }
        
        if (null != this.getResource() && !this.getResource().trim().isEmpty())
        {
            json.put("resource", this.getResource());
        }
        
        if (null != this.getErrorList() && !this.getErrorList().isEmpty())
        {
            JSONArray jsonArr = new JSONArray();
            
            for (RestErrorHolder err : this.getErrorList())
            {
                jsonArr.put(new JSONObject().put("errorCode", err.getErrorCode()).put("errorDesc", err.getErrorDesc()));
            }
            
            json.put("errorList", jsonArr);
        }
        
        
        return json.toString();
    }


    public int getHttpStatusCode()
    {
        return httpStatusCode;
    }


    public void setHttpStatusCode(int httpStatusCode)
    {
        this.httpStatusCode = httpStatusCode;
    }


    public String getResult()
    {
        return result;
    }


    public void setResult(String result)
    {
        this.result = result;
    }


    public String getNonce()
    {
        return nonce;
    }


    public void setNonce(String nonce)
    {
        this.nonce = nonce;
    }


    public String getResource()
    {
        return resource;
    }


    public void setResource(String resource)
    {
        this.resource = resource;
    }


    public List<RestErrorHolder> getErrorList()
    {
        return errorList;
    }


    public void setErrorList(List<RestErrorHolder> errorList)
    {
        this.errorList = errorList;
    }

}
