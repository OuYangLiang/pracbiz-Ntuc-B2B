package com.pracbiz.b2bportal.core.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pracbiz.b2bportal.core.holder.RestErrorHolder;
import com.pracbiz.b2bportal.core.holder.RestResponseHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public interface RestRequestAuthenticationService
{

    public List<RestErrorHolder> doRestAuthentication(
            HttpServletRequest request, RestResponseHolder restResHolder)
            throws Exception;


    public void addRestErrorToList(String errorKey, String[] errorArgs,
            List<RestErrorHolder> errorList);


    public void sendResponseToClient(HttpServletResponse response,
        int httpStatusCode, String responseData) throws IOException;


    public String[] splitMailBoxAndNonceFromRequestHeader(
            String nonceWithMailBox) throws IOException;

}
