//*****************************************************************************
//
// File Name       :  NonceController.java
// Date Created    :  Jun 25, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Jun 25, 2013 4:18:53 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.action.rest;

import java.io.IOException;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.service.RestRequestAuthenticationService;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class NonceController
{
    private static final Logger log = LoggerFactory.getLogger(NonceController.class);
    public static final String SESSION_NONCE = "NONCE_KEY";
    
    @Autowired
    private SecureRandom secureRandom;
    
    @Autowired
    private transient RestRequestAuthenticationService restRequestAuthenticationService;
    
    @Actions({
        @Action(interceptorRefs = @InterceptorRef("defaultStack")) })
    public void index() throws IOException
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        
        String nonce = generateNonce();
        
        log.info("Generating nonce [" + nonce + "], for client [" + request.getRemoteHost() + "].");
        request.getSession().setAttribute(SESSION_NONCE, nonce);
        
        JSONObject json = new JSONObject();
        
        try
        {
            json.put("nonce", nonce);
        }
        catch(JSONException e)
        {
            ErrorHelper.getInstance().logError(log, e);
            
            restRequestAuthenticationService.sendResponseToClient(response,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

        restRequestAuthenticationService.sendResponseToClient(response,
            HttpServletResponse.SC_OK, json.toString());
        
    }
    
    
    private String generateNonce()
    {
        byte[] nonceBytes = new byte[16];
        secureRandom.nextBytes(nonceBytes);
        String nonce = String.valueOf(Hex.encodeHex(nonceBytes));

        return nonce;
    }
}
