//*****************************************************************************
//
// File Name       :  LDAPUtil.java
// Date Created    :  Mar 27, 2013
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Mar 27, 2013 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.util;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.core.holder.extension.ActiveDirectoryHolder;
import com.pracbiz.b2bportal.core.holder.extension.AdGroupHolder;
import com.pracbiz.b2bportal.core.holder.extension.AdUserHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class LDAPUtil
{
    private static final Logger log = LoggerFactory.getLogger(LDAPUtil.class);
    private static final String[] ATTRIBUTES_GROUP={"description","sAMAccountName"};
    private static final String[] ATTRIBUTES_USER={"mail","sAMAccountName"};
    private static final String SEARCH_FILTER_GROUP="(OU=*)";
    //private static final String SEARCH_FILTER_USER= "(|(memberOf=cn=Users,dc=pracbiz,dc=com)(primaryGroupID=513))";
    
    public List<AdGroupHolder> getOrganizationalUnit(ActiveDirectoryHolder parameter) throws NamingException
    {
        // Search for objects using the filter
        String searchBase = this.getSearchBase(parameter.getDomain());
        
        InitialLdapContext context = this.getInitialContext(
            parameter.getHostname(), Integer.parseInt(parameter.getPort()),
            parameter.getUserName(), parameter.getPassword());
        
        List<AdGroupHolder> list;
        try
        {
            
            NamingEnumeration<?> answer = this.executeSearch(context,
                SearchControls.SUBTREE_SCOPE, searchBase, SEARCH_FILTER_GROUP, ATTRIBUTES_GROUP);
            
            if (answer == null)
            {
                log.info(" :::: Can not found any orgainzational unit from the AD server.");
                return null;
            }
            
            list = new LinkedList<AdGroupHolder>();
            while (answer.hasMoreElements())
            {
                SearchResult sr = (SearchResult)answer.next();
                AdGroupHolder group = new AdGroupHolder();
                group.setGroupCode(this.extractGroupCode(sr.getName()));
                list.add(group);
                Attributes Attrs = sr.getAttributes();
                if (Attrs == null)
                    continue;
                
                Attribute Attr = Attrs.get("description");
                if (Attr == null)
                    continue;
                String desc = (String)Attr.get();
                group.setGroupDesc(desc);
            }
        }
        finally
        {
            context.close();
        }
        
        return list;
    }
    
    
    public List<AdUserHolder> getUsersByGroup(ActiveDirectoryHolder parameter,String groupCode) throws NamingException
    {
        String searchBase = "OU=" + groupCode + "," + this.getSearchBase(parameter.getDomain());
        String searchFilterUser = this.getSearchFilterUser(parameter.getDomain()); 
        InitialLdapContext context = this.getInitialContext(
            parameter.getHostname(), Integer.parseInt(parameter.getPort()),
            parameter.getUserName(), parameter.getPassword());
        
        
        try
        {
            NamingEnumeration<?> answer = this.executeSearch(context,
                SearchControls.SUBTREE_SCOPE, searchBase, searchFilterUser,
                ATTRIBUTES_USER);
            return this.getUsers(answer);
            
        }
        finally
        {
            context.close();
        }
        
    }
        
    
    
    public List<AdUserHolder> getAllUsers(ActiveDirectoryHolder parameter) throws NamingException
    {
        String searchBase = this.getSearchBase(parameter.getDomain());
        String searchFilterUser = this.getSearchFilterUser(parameter.getDomain()); 
        InitialLdapContext context = this.getInitialContext(
            parameter.getHostname(), Integer.parseInt(parameter.getPort()),
            parameter.getUserName(), parameter.getPassword());
        
        
        try
        {
            NamingEnumeration<?> answer = this.executeSearch(context,
                SearchControls.SUBTREE_SCOPE, searchBase, searchFilterUser,
                ATTRIBUTES_USER);
            
            if (answer == null)
            {
                log.info(" :::: Can not found any user from the AD server.");
                return null;
            }
            
            return this.getUsers(answer);
        }
        finally
        {
            context.close();
        }
    }
    
    
    public InitialLdapContext getInitialContext(String hostname, int port,
        String username, String password) throws NamingException
    {

        String providerURL = new StringBuffer("ldap://").append(hostname)
            .append(":").append(port).toString();
        
        Hashtable<String,String> hashEnv = new Hashtable<String,String>();
        hashEnv.put(Context.INITIAL_CONTEXT_FACTORY,
            "com.sun.jndi.ldap.LdapCtxFactory");
        hashEnv.put(Context.PROVIDER_URL, providerURL);

        if(StringUtils.isNotBlank(username))
        {
            hashEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
            hashEnv.put(Context.SECURITY_PRINCIPAL, username); 
            hashEnv.put(Context.SECURITY_CREDENTIALS, password);
            //hashEnv.put(Context.REFERRAL, "follow");
        }
        return new InitialLdapContext(hashEnv, null);
    }
    
    
    private List<AdUserHolder> getUsers(NamingEnumeration<?> answer) throws NamingException
    {
        if (answer == null)
        {
            log.info(" :::: Can not found any user from the AD server.");
            return null;
        }
        
        List<AdUserHolder> list = new LinkedList<AdUserHolder>();
        while(answer.hasMoreElements())
        {
            SearchResult sr = (SearchResult)answer.next();
            AdUserHolder user = new AdUserHolder();
            user.setUserName(extractUserName(sr.getName()));
            list.add(user);
            Attributes Attrs = sr.getAttributes();
            if(Attrs == null)
            {
                continue;
            }
                
            Attribute Attr = Attrs.get("mail");
            if (Attr == null)
                continue;
            
            String email = (String)Attr.get();
            user.setEmail(email);
        }
        
        return list;
    }
    
    private NamingEnumeration<?> executeSearch(InitialLdapContext context, int searchScope, String searchBase,
        String searchFilter, String[] attributes) throws NamingException
    {
        // Create the search controls
        SearchControls searchCtls = new SearchControls();

        // Specify the attributes to return
        if(attributes != null)
        {
            searchCtls.setReturningAttributes(attributes);
        }

        // Specify the search scope
        searchCtls.setSearchScope(searchScope);

        // Search for objects using the filter
        return context.search(searchBase, searchFilter,searchCtls);
    }
    
    
    private String getSearchBase(String domain)
    {
        String[] dcs = StringUtils.split(domain, '.');
        StringBuffer searchBase = new StringBuffer();
        for (int i = 0; i < dcs.length; i ++)
        {
            searchBase.append("DC=").append(dcs[i]);
            if (i + 1 == dcs.length)
            {
                continue;
            }
            searchBase.append(',');
        }
        
        return searchBase.toString();
    }
    
    
    private String extractUserName(String source)
    {
        if (StringUtils.isBlank(source))
        {
            return null;
        }
        
        if (source.toUpperCase(Locale.ENGLISH).startsWith("CN="))
        {
            String temp = source.substring(3);
            
            int position = temp.indexOf(',');
            if(position == -1)
            {
                return temp;
            }
            else
            {
                return temp.substring(0, position);
            }
        }
        
        return source;
        
    }
    
    
    private String extractGroupCode(String source)
    {
        if (StringUtils.isBlank(source))
        {
            return null;
        }
        
        if (source.toUpperCase(Locale.ENGLISH).startsWith("OU="))
        {
            String temp = source.substring(3);
            
            int position = temp.indexOf(',');
            if(position == -1)
            {
                return temp;
            }
            else
            {
                return temp.substring(0, position);
            }
        }
        
        return source;
    }
    
	private String getSearchFilterUser(String domain) 
	{
		String[] dcs = StringUtils.split(domain, '.');
		StringBuffer searchFilterUser = new StringBuffer(80);
		searchFilterUser.append("(|(memberOf=cn=Users,");
		for (int i = 0; i < dcs.length; i++) 
		{
			searchFilterUser.append("DC=").append(dcs[i]);
			if (i + 1 == dcs.length) 
			{
				continue;
			}
			searchFilterUser.append(',');
		}
		searchFilterUser.append(")(primaryGroupID=513))");
		return searchFilterUser.toString();
	}
    
}
