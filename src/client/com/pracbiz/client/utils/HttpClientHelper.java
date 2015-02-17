package com.pracbiz.client.utils;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.btr.proxy.search.ProxySearch;
import com.btr.proxy.search.ProxySearch.Strategy;
import com.btr.proxy.util.PlatformUtil;
import com.btr.proxy.util.PlatformUtil.Platform;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.exception.GnupgException;


/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class HttpClientHelper implements CommonConstants
{
    private static final String RESPONSE_PROP_NONCE="nonce";
    private static final String RESPONSE_PRPO_KEYIMPORTED = "keyImported";
    private static final String RESPONSE_PROP_ITEM_COUNT="itemCount";
    private static final String RESPONSE_HEADER_FILENAME="filename";
    private static final String RESPONSE_HEADER_CONTENT = "content";
    private static final String HEADER_WS_AUTHORIZATION = "WS-Authorization";
    private static final String RESPONSE_HEADER_UPLOAD_FILES = "uploadFiles";
    
    private static final String RECEIVED_ERROR_STATUS_WHILE_HTTP_GET_STRING_FILE_STATUS_CODE = 
        "Received error status while HTTP GET string/file. StatusCode = [";

    private HttpClient httpClient = null;
	@Autowired
    private ClientConfigHelper clientConfig;
    
    private void prepare() throws Exception
    {
        if (httpClient != null)
        {
            return;
        }

        httpClient = new HttpClient();
        
        if (clientConfig.useProxy())
        {
            Map<String,String> proxyProp = clientConfig.getProxyProp();
            if (clientConfig.autoProxy())
            {
                this.autoProxy(proxyProp);
                return ;
            }
           
            this.manualProxy(proxyProp);
        }
    }
    
    
    public String authorization(String serverId, Logger log) throws Exception
    {
        prepare();
        HttpMethod method = null;

        try
        {
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(clientConfig.getChannelTimeout(serverId));
            String url = clientConfig.getB2bHost(serverId) + "/service/nonce";
            
            log.info( "Get nonce from server [" + url + "].");
            
            method = new GetMethod(url);

            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK)
            {
                log.error(RECEIVED_ERROR_STATUS_WHILE_HTTP_GET_STRING_FILE_STATUS_CODE
                    + statusCode + "].");
                
                throw new Exception(method.getResponseBodyAsString());
            }
            
            String responseBody = method.getResponseBodyAsString();
            
            log.info("Response body: ==>");
            log.info(responseBody);
            
            JSONObject json = new JSONObject(new JSONTokener(responseBody));
            String nonce = json.getString(RESPONSE_PROP_NONCE);
            
            log.info( "Nonce [" + nonce + "] received from server.");
            
            return nonce;

        }
        finally
        {
            if (method != null)
            {
                method.releaseConnection();
                method = null;
            }
        }
    }
    
    
    public String getCredential(String nonce, String mboxId) throws GnupgException, UnsupportedEncodingException
    {
        String encryptedNonce = nonce;
        return StringUtils.replace(EncodeUtil.getInstance().encodeString(mboxId + "::::" + encryptedNonce), "\n", "");
    }
    
    
    public boolean checkKey(String serverId, String credential, Logger log) throws Exception
    {
        prepare();
        HttpMethod method = null;
        
        try
        {
            log.info("Checking whether pgp keys is setup.");
            
            int timeout = clientConfig.getChannelTimeout(serverId);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
            
            String url = clientConfig.getB2bHost(serverId) + "/service/key";
            
            method = new GetMethod(url);
            method.setRequestHeader(HEADER_WS_AUTHORIZATION,credential);
            
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK)
            {
                log.error(RECEIVED_ERROR_STATUS_WHILE_HTTP_GET_STRING_FILE_STATUS_CODE
                    + statusCode + "].");
            
                throw new Exception(method.getResponseBodyAsString());
            }
            
            String responseBody = method.getResponseBodyAsString();
            
            log.info("Response body: ==>");
            log.info(responseBody);
            
            JSONObject json = new JSONObject(new JSONTokener(responseBody));
           
            return json.getBoolean(RESPONSE_PRPO_KEYIMPORTED);
        }
        finally
        {
            if (method != null)
            {
                method.releaseConnection();
                method = null;
            }
        }
    }

    
    public void uploadKey(String serverId, String credential, File file, Logger log) throws Exception
    {
        prepare();
        PostMethod method = null;

        try
        {
            int timeout = clientConfig.getChannelTimeout(serverId);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);

            String url = clientConfig.getB2bHost(serverId) + "/service/key";
            
            method = new PostMethod(url);
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
            method.setRequestHeader(HEADER_WS_AUTHORIZATION, credential);

            FilePart[] parts = new FilePart[1];
            parts[0] = new FilePart(RESPONSE_HEADER_UPLOAD_FILES, file);

            MultipartRequestEntity entity = new MultipartRequestEntity(parts,
                method.getParams());

            method.setRequestEntity(entity);
            int statusCode = httpClient.executeMethod(method);
            
            if (statusCode != HttpStatus.SC_OK)
            {
                log.error(RECEIVED_ERROR_STATUS_WHILE_HTTP_GET_STRING_FILE_STATUS_CODE
                    + statusCode + "].");
            
                throw new Exception(method.getResponseBodyAsString());
            }
            
        }
        finally
        {
            if(method != null)
            {
                method.releaseConnection();
                method = null;
            }
        }
    }
    
    
    public File retrievePortalPubKey(String serverId, String credential, String savePath, Logger log) throws Exception
    {
        prepare();
        RandomAccessFile file = null;
        HttpMethod method = null;

        try
        {
            int timeout = clientConfig.getChannelTimeout(serverId);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
            String url = clientConfig.getB2bHost(serverId) + "/service/key/download";
            
            method = new GetMethod(url);
            method.setRequestHeader(HEADER_WS_AUTHORIZATION,credential);
            
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK)
            {
                log.error(RECEIVED_ERROR_STATUS_WHILE_HTTP_GET_STRING_FILE_STATUS_CODE
                    + statusCode + "].");
            
                throw new Exception(method.getResponseBodyAsString());
            }
            
            Header header = method.getResponseHeader(RESPONSE_HEADER_CONTENT);
            
            if(header != null
                && header.getValue().startsWith("application/pgp-encrypted-signed"))
            {
                // zip file stream
                header= method.getResponseHeader(RESPONSE_HEADER_FILENAME);
                if (header == null)
                {
                    throw new Exception(
                            "Filename not found in Response Header.");
                }

                String fileName = header.getValue();
                byte[] b = method.getResponseBody();
                
                try
                {
                    file = new RandomAccessFile(savePath + File.separator + fileName, "rw");
                    file.write(b);
                    
                    return new File(savePath, fileName);
                }
                finally
                {
                    if (null != file)
                    {
                        file.close();
                        file = null;
                    }
                }
            }
            
            return null;
        }
        finally
        {
            if (method != null)
            {
                method.releaseConnection();
                method = null;
            }
        }

    }
    
    
    public int qureyInbox(String serverId, String credential, Logger log) throws Exception
    {
        prepare();
        HttpMethod method = null;

        try
        {
            int timeout = clientConfig.getChannelTimeout(serverId);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
            
            String url = clientConfig.getB2bHost(serverId) + "/service/inbox";
            
            log.info("Start to query inbox from server via url [" + url + "].");
            log.info("Credential: [" + credential + "].");
            
            method = new GetMethod(url);
            method.setRequestHeader(HEADER_WS_AUTHORIZATION,credential);
            
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK)
            {
                log.error(RECEIVED_ERROR_STATUS_WHILE_HTTP_GET_STRING_FILE_STATUS_CODE
                    + statusCode + "].");
            
                throw new Exception(method.getResponseBodyAsString());
            }
            
            String responseBody = method.getResponseBodyAsString();
            
            log.info("Response body: ==>");
            log.info(responseBody);
            
            JSONObject json = new JSONObject(new JSONTokener(responseBody));
           
            int itemCount = json.getInt(RESPONSE_PROP_ITEM_COUNT);
            
            log.info( "[" + itemCount + "] file(s) have been detected on the server's inbox.");
            
            return itemCount;
        }
        finally
        {
            if (method != null)
            {
                method.releaseConnection();
                method = null;
            }
        }
    }


    public void retrieveInbox(String serverId, String credential, File savePath, Logger log) throws Exception
    {
        prepare();
        RandomAccessFile file = null;
        HttpMethod method = null;

        try
        {
            int timeout = clientConfig.getChannelTimeout(serverId);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
            String url = clientConfig.getB2bHost(serverId) + "/service/inbox/batch";
            
            log.info("Start to download from server via url [" + url + "].");
            
            method = new GetMethod(url);
            method.setRequestHeader(HEADER_WS_AUTHORIZATION,credential);
            
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK)
            {
                log.error(RECEIVED_ERROR_STATUS_WHILE_HTTP_GET_STRING_FILE_STATUS_CODE
                    + statusCode + "].");
            
                throw new Exception(method.getResponseBodyAsString());
            }
            
            Header header = method.getResponseHeader(RESPONSE_HEADER_CONTENT);
            
            if(header != null
                && header.getValue().startsWith("application/pgp-encrypted-signed"))
            {
                // zip file stream
                header= method.getResponseHeader(RESPONSE_HEADER_FILENAME);
                if (header == null)
                {
                    throw new Exception(
                            "Filename not found in Response Header.");
                }

                String fileName = header.getValue();
                byte[] b = method.getResponseBody();
                
                try
                {
                    file = new RandomAccessFile(savePath + File.separator + fileName, "rw");
                    file.write(b);
                    
                    FileUtil.getInstance().touchTok(savePath,
                        fileName.substring(0, fileName.lastIndexOf('.')));
                    
                    log.info("File [" + fileName + "] download successfully into folder [" + savePath + "].");
                    
                    return;
                }
                finally
                {
                    if (null != file)
                    {
                        file.close();
                        file = null;
                    }
                }
            }
        }
        finally
        {
            if (method != null)
            {
                method.releaseConnection();
                method = null;
            }
        }

    }

    
    public void postOutbox(String serverId, String credential, File file, boolean isOver, Logger log)
        throws Exception
    {
        prepare();
        PostMethod method = null;

        try
        {
            int timeout = clientConfig.getChannelTimeout(serverId);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);

            String url = clientConfig.getB2bHost(serverId) + "/service/outbox";
            
            method = new PostMethod(url);
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
            method.setRequestHeader(HEADER_WS_AUTHORIZATION, credential);

            FilePart[] parts = new FilePart[1];
            parts[0] = new FilePart(RESPONSE_HEADER_UPLOAD_FILES, file);

            MultipartRequestEntity entity = new MultipartRequestEntity(parts,
                method.getParams());

            if(isOver)
            {
                NameValuePair[] params = new NameValuePair[1];
                params[0] = new NameValuePair("IS_OVER", "Y");
                method.setQueryString(params);
            }

            method.setRequestEntity(entity);
            int statusCode = httpClient.executeMethod(method);
            
            if (statusCode != HttpStatus.SC_OK)
            {
                log.error(RECEIVED_ERROR_STATUS_WHILE_HTTP_GET_STRING_FILE_STATUS_CODE
                    + statusCode + "].");
            
                throw new Exception(method.getResponseBodyAsString());
            }
            
        }
        finally
        {
            if(method != null)
            {
                method.releaseConnection();
                method = null;
            }
        }
    }


    public void releaseHttpClient()
    {
        if (httpClient != null)
        {
            httpClient = null;
        }
    }

    
    //*****************************************************
    // private methods
    //*****************************************************
    
    private void autoProxy(Map<String, String> proxyProp) throws Exception
    {
        ProxySearch proxySearch = new ProxySearch();
        if(PlatformUtil.getCurrentPlattform() == Platform.WIN)
        {
            proxySearch.addStrategy(Strategy.IE);
            proxySearch.addStrategy(Strategy.FIREFOX);
            proxySearch.addStrategy(Strategy.JAVA);
        }
        else if(PlatformUtil.getCurrentPlattform() == Platform.LINUX)
        {
            proxySearch.addStrategy(Strategy.GNOME);
            proxySearch.addStrategy(Strategy.KDE);
            proxySearch.addStrategy(Strategy.FIREFOX);
        }
        else
        {
            proxySearch.addStrategy(Strategy.OS_DEFAULT);
        }
        
        ProxySelector proxySelector = proxySearch.getProxySelector();
        ProxySelector.setDefault(proxySelector);
        
        List<Proxy> proxys = proxySelector.select(new URI("localhost"));
        
        if (proxys == null || proxys.isEmpty() )
        {
            throw new Exception("Can not found proxy setting.");
        }
        
        Proxy proxy = proxys.get(0);
        InetSocketAddress address = (InetSocketAddress)proxy.address();
        String proxyHost = address.getHostName();
        int proxyPort = address.getPort();
        httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
        
        String proxyUser = (String)proxyProp.get(ClientConfigHelper.PROXY_AUTO_PROP_USERNAME);
        String proxyPassword = (String)proxyProp.get(ClientConfigHelper.PROXY_AUTO_PROP_PASSWORD);
        httpClient.getState().setProxyCredentials(
            new AuthScope(proxyHost, proxyPort, null),
            new UsernamePasswordCredentials(proxyUser,
                    proxyPassword));
        
        List<String> authPrefs = new ArrayList<String>(3);
        authPrefs.add(AuthPolicy.BASIC);
        authPrefs.add(AuthPolicy.NTLM);
        authPrefs.add(AuthPolicy.DIGEST);
        httpClient.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
    }
    
    
    private void manualProxy(Map<String, String> proxyProp)
    {
        String proxyHost = (String)proxyProp.get(ClientConfigHelper.PROXY_PROP_HOST);
        int proxyPort = Integer.parseInt((String)proxyProp.get(ClientConfigHelper.PROXY_PROP_PORT));
        String proxyUser = (String)proxyProp.get(ClientConfigHelper.PROXY_PROP_USERNAME);
        String proxyPassword = (String)proxyProp.get(ClientConfigHelper.PROXY_PROP_PASSWORD);
        
        httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
        
        if (proxyUser != null && proxyUser.length() > 0)
        {
            if (clientConfig.useNTLM())
            {
                String proxyNtlmDomain = (String) proxyProp
                    .get(ClientConfigHelper.PROXY_PROP_NTLMDOMAIN);

                httpClient.getState().setProxyCredentials(
                    new AuthScope(proxyHost, proxyPort, null),
                    new NTCredentials(proxyUser, proxyPassword,
                            proxyHost, proxyNtlmDomain));
            }
            else
            {
                httpClient.getState().setProxyCredentials(
                    new AuthScope(proxyHost, proxyPort, null),
                    new UsernamePasswordCredentials(proxyUser,
                            proxyPassword));
            }
            
        }
        
        List<String> authPrefs = new ArrayList<String>(3);
        authPrefs.add(AuthPolicy.BASIC);
        authPrefs.add(AuthPolicy.NTLM);
        authPrefs.add(AuthPolicy.DIGEST);
        httpClient.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
    }

}
