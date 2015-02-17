package com.pracbiz.b2bportal.core.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.core.action.rest.NonceController;
import com.pracbiz.b2bportal.core.holder.RestErrorHolder;
import com.pracbiz.b2bportal.core.holder.RestResponseHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.service.RestRequestAuthenticationService;
import com.pracbiz.b2bportal.core.service.SupplierService;

public class RestRequestAuthenticationServiceImpl implements
        RestRequestAuthenticationService, CommonConstants
{
    private static final Logger log = Logger.getLogger(RestRequestAuthenticationService.class);
    
    public static final String VALUE_ERROR = "error";
    private static final String ACCESS_DENIED = "B2BPS0008";
    private static final String NO_SUPPORT_METHOD = "B2BPS0007";

    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private SupplierService supplierService;


    public List<RestErrorHolder> doRestAuthentication(
            HttpServletRequest request, RestResponseHolder restResHolder)
            throws Exception
    {
        List<RestErrorHolder> errorList = new ArrayList<RestErrorHolder>();
        restResHolder.setResult(VALUE_ERROR);
        restResHolder.setResource(request.getRequestURI());
        
        
        String[] uris = request.getRequestURI().split("/");
        String requestMethod = uris[uris.length - 1];
        
        // To check the http method
        if("outbox".equalsIgnoreCase(requestMethod))
        {
            if (!"POST".equalsIgnoreCase(request.getMethod()))
            {
                restResHolder.setHttpStatusCode(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                addRestErrorToList(NO_SUPPORT_METHOD, null, errorList);
                log.info(obtainMsg(NO_SUPPORT_METHOD, null));
                return errorList;
            }
        }
        else if (!"key".equalsIgnoreCase(requestMethod) && (!"GET".equalsIgnoreCase(request.getMethod())))
        {
            restResHolder.setHttpStatusCode(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            addRestErrorToList(NO_SUPPORT_METHOD, null, errorList);
            log.info(obtainMsg(NO_SUPPORT_METHOD, null));
            return errorList;
        }

        
        // To check header param WS-Authorization
        String nonceWithMailBox = request.getHeader("WS-Authorization");
        String[] nonceMbs = splitMailBoxAndNonceFromRequestHeader(nonceWithMailBox);
        if (nonceMbs == null)
        {
            restResHolder.setHttpStatusCode(HttpServletResponse.SC_PRECONDITION_FAILED);
            addRestErrorToList(ACCESS_DENIED, null, errorList);
            log.info(obtainMsg(ACCESS_DENIED, null) + " :::: nonce and mailbox id cannot found from HTTP Header.");
            return errorList;
        }
        
        
        // To check supplier with mailbox nonceMbs[0]
        String mailbox = nonceMbs[0];
        SupplierHolder supplier = supplierService
            .selectSupplierByMboxId(mailbox);
        if (supplier == null || !supplier.getActive() || supplier.getBlocked())
        {
            restResHolder.setHttpStatusCode(HttpServletResponse.SC_PRECONDITION_FAILED);
            addRestErrorToList("B2BPS0010", new String[] { mailbox }, errorList);
            log.info(obtainMsg("B2BPS0010", new String[] { mailbox }));
        }
        else if (!supplier.getClientEnabled())
        {
            restResHolder.setHttpStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
            addRestErrorToList("B2BPS0005", new String[] { mailbox }, errorList);
            log.info(obtainMsg("B2BPS0005", new String[] { mailbox }));
            return errorList;
        }

        
        // To check the encryped nonce
        //String nonce = this.decryptNonce(nonceMbs[1], mailbox);
        String nonce = nonceMbs[1];
        restResHolder.setNonce(nonce);
        if (StringUtils.isEmpty(nonce))
        {
            restResHolder.setHttpStatusCode(HttpServletResponse.SC_PRECONDITION_FAILED);
            addRestErrorToList("B2BPS0011", null, errorList);
            log.info(obtainMsg("B2BPS0011", null));
            return errorList;
        }
        
        Object oriNonce = request.getSession().getAttribute(NonceController.SESSION_NONCE);
        
        if (null == oriNonce || !nonce.equals(oriNonce))
        {
            restResHolder.setHttpStatusCode(HttpServletResponse.SC_PRECONDITION_FAILED);
            addRestErrorToList("B2BPS0012",
                    new String[] { nonce, mailbox }, errorList);
            log.info(obtainMsg("B2BPS0012", new String[] { nonce, mailbox }));

            return errorList;
        }
        
        return null;
    }


    public void addRestErrorToList(String errorKey, String[] errorArgs,
            List<RestErrorHolder> errorList)
    {
        RestErrorHolder restError = new RestErrorHolder();
        restError.setErrorCode(errorKey);
        restError.setErrorDesc(obtainMsg(errorKey, errorArgs));
        errorList.add(restError);
    }


    public String[] splitMailBoxAndNonceFromRequestHeader(
            String nonceWithMailBox) throws IOException
    {
        if (StringUtils.isEmpty(nonceWithMailBox))
        {
            return null;
        }
        String nonceMailBoxStr = EncodeUtil.getInstance().decodeString(
                nonceWithMailBox);
        String[] nonceMbs = nonceMailBoxStr.split("::::");
        
        if (nonceMbs == null || nonceMbs.length != 2)
        {
            return null;
        }
        return nonceMbs;
    }


    private String obtainMsg(String code, Object[] args)
    {
        return messageSource.getMessage(code, args, Locale.US);
    }


    public void sendResponseToClient(HttpServletResponse response,
        int httpStatusCode, String responseData) throws IOException
    {
        response.setStatus(httpStatusCode);
        response.setHeader("Content-Type", "application/json");
        response.setContentType("application/json");

        response.getOutputStream().write(responseData.getBytes(CommonConstants.ENCODING_UTF8));
        response.getOutputStream().flush();
    }

}
