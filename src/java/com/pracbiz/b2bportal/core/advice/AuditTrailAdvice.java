//*****************************************************************************
//
// File Name       :  AuditTrailAdvice.java
// Date Created    :  Sep 6, 2012
// Last Changed By :  $Author: OuYang $
// Last Changed On :  $Date: 2011-07-01 10:56:27  $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.advice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.ActorAction;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.holder.AuditTrailHolder;
import com.pracbiz.b2bportal.core.mapper.AuditTrailMapper;
import com.pracbiz.b2bportal.core.service.OidService;

public class AuditTrailAdvice implements AfterReturningAdvice
{
    private static final String CUSTOM_IDENTIFICATION_METHOD = "getCustomIdentification";
    
    private static final String ELEMENT_AUDIT_SNAPSHOT = "auditSnapshot";
    private static final String ELEMENT_LIST = "list";
    private static final String ELEMENT_RECORD = "record";
    private static final String ELEMENT_FIELD = "field";
    
    private static final String ATTR_NAME = "name";
    private static final String ATTR_RECORD_TYPE = "recordType";
    private static final String ATTR_ACTION_DATE = "actionDate";
    private static final String ATTR_ACTION_TYPE = "actionType";
    
    
    @Autowired OidService oidService;
    @Autowired AuditTrailMapper mapper;

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args,
            Object target) throws Throwable
    {
        if (args == null || args.length < 2)
        {
            throw new IllegalArgumentException();
        }
        
//        if (null == args[0] || null == args[1])
//        {
//            throw new IllegalArgumentException();
//        }
        
        if (!(args[0] instanceof CommonParameterHolder))
        {
            throw new IllegalArgumentException();
        }
        
//        if (!(args[1] instanceof BaseHolder))
//        {
//            throw new IllegalArgumentException();
//        }
        
        CommonParameterHolder cp = (CommonParameterHolder)args[0];
        
        
        AuditTrailHolder auditTrail = new AuditTrailHolder();
        auditTrail.setAuditTrailOid(oidService.getOid());
        auditTrail.setActionDate(new Date());
        auditTrail.setActorOid(cp.getCurrentUserOid());
        auditTrail.setActor(cp.getLoginId());
        auditTrail.setClientIp(cp.getClientIp());
        auditTrail.setSessionOid(cp.getSessionOid());
        
        String methodName = method.getName();
        
        if ("auditInsert".equals(methodName))
        {
            BaseHolder base = (BaseHolder)args[1];
            Class<?> c = this.getMainClass(base.getClass());
            auditTrail.setRecordType(c.getSimpleName().substring(0, c.getSimpleName().length() - 6));
            
            auditTrail.setRecordKey(base.getLogicalKey());
            auditTrail.setActionType(DbActionType.CREATE);
            auditTrail.setActorAction(ActorAction.CREATE);
            auditTrail.setXmlContent(this.getXml(base, DbActionType.CREATE, auditTrail.getActionDate()));
        }
        else if (methodName.startsWith("auditUpdate"))
        {
            if (!(args[2] instanceof BaseHolder))
            {
                throw new IllegalArgumentException();
            }
            
            BaseHolder base = (BaseHolder)args[1];
            Class<?> c = this.getMainClass(base.getClass());
            auditTrail.setRecordType(c.getSimpleName().substring(0, c.getSimpleName().length() - 6));
            
            BaseHolder base2 = (BaseHolder)args[2];
            auditTrail.setRecordKey(base2.getLogicalKey());
            auditTrail.setActionType(DbActionType.UPDATE);
            auditTrail.setActorAction(ActorAction.UPDATE);
            auditTrail.setXmlContent(this.getXml(base, base2, DbActionType.UPDATE, auditTrail.getActionDate()));
        }
        else if ("auditDelete".equals(methodName))
        {
            BaseHolder base = (BaseHolder)args[1];
            Class<?> c = this.getMainClass(base.getClass());
            auditTrail.setRecordType(c.getSimpleName().substring(0, c.getSimpleName().length() - 6));
            
            auditTrail.setRecordKey(base.getLogicalKey());
            auditTrail.setActionType(DbActionType.DELETE);
            auditTrail.setActorAction(ActorAction.DELETE);
            auditTrail.setXmlContent(this.getXml(base, DbActionType.DELETE, auditTrail.getActionDate()));
        }
        else if ("mkCreate".equals(methodName))
        {
            BaseHolder base = (BaseHolder)args[1];
            Class<?> c = this.getMainClass(base.getClass());
            auditTrail.setRecordType(c.getSimpleName().substring(0, c.getSimpleName().length() - 6));
            
            auditTrail.setRecordKey(base.getLogicalKey());
            auditTrail.setActionType(DbActionType.CREATE);
            auditTrail.setActorAction(ActorAction.CREATE);
            auditTrail.setRecordStatus(MkCtrlStatus.PENDING);
            auditTrail.setXmlContent(this.getXml(base, DbActionType.CREATE, auditTrail.getActionDate()));
        }
        else if ("mkUpdate".equals(methodName))
        {
            BaseHolder base = (BaseHolder)args[1];
            Class<?> c = this.getMainClass(base.getClass());
            auditTrail.setRecordType(c.getSimpleName().substring(0, c.getSimpleName().length() - 6));
            
            if (!(args[2] instanceof BaseHolder))
            {
                throw new IllegalArgumentException();
            }
            
            BaseHolder base2 = (BaseHolder)args[2];
            
            auditTrail.setRecordKey(base2.getLogicalKey());
            auditTrail.setActionType(DbActionType.UPDATE);
            auditTrail.setActorAction(ActorAction.UPDATE);
            auditTrail.setRecordStatus(MkCtrlStatus.PENDING);
            auditTrail.setXmlContent(this.getXml(base, base2, DbActionType.UPDATE, auditTrail.getActionDate()));
        }
        else if ("mkDelete".equals(methodName))
        {
            BaseHolder base = (BaseHolder)args[1];
            Class<?> c = this.getMainClass(base.getClass());
            auditTrail.setRecordType(c.getSimpleName().substring(0, c.getSimpleName().length() - 6));
            
            auditTrail.setRecordKey(base.getLogicalKey());
            auditTrail.setActionType(DbActionType.DELETE);
            auditTrail.setActorAction(ActorAction.DELETE);
            auditTrail.setRecordStatus(MkCtrlStatus.PENDING);
            auditTrail.setXmlContent(this.getXml(base, DbActionType.DELETE, auditTrail.getActionDate()));
        }
        else if ("mkApprove".equals(methodName))
        {
            if (!(args[2] instanceof BaseHolder))
            {
                throw new IllegalArgumentException();
            }
            
            BaseHolder base2 = (BaseHolder)args[2];
            Class<?> c = this.getMainClass(base2.getClass());
            auditTrail.setRecordType(c.getSimpleName().substring(0, c.getSimpleName().length() - 6));
            
            auditTrail.setRecordKey(base2.getLogicalKey());
            auditTrail.setActorAction(ActorAction.APPROVE);
            auditTrail.setRecordStatus(MkCtrlStatus.APPROVED);
            
            Class<?> tmpClass = this.getTmpClass(base2.getClass());
            Method m = tmpClass.getMethod("getActionType", new Class[]{});
            
            if (!(m.getReturnType().equals(DbActionType.class)))
            {
                throw new IllegalArgumentException();
            }
            
            auditTrail.setActionType((DbActionType)m.invoke(base2, new Object[0]));
            
            if (DbActionType.UPDATE.equals(auditTrail.getActionType()))
            {
                BaseHolder base = (BaseHolder)args[1];
                auditTrail.setXmlContent(this.getXml(base, base2, auditTrail.getActionType(), auditTrail.getActionDate()));
            }
            else
            {
                auditTrail.setXmlContent(this.getXml(base2, auditTrail.getActionType(), auditTrail.getActionDate()));
            }
            
        }
        else if ("mkReject".equals(methodName))
        {
            if (!(args[2] instanceof BaseHolder))
            {
                throw new IllegalArgumentException();
            }
            
            BaseHolder base2 = (BaseHolder)args[2];
            Class<?> c = this.getMainClass(base2.getClass());
            auditTrail.setRecordType(c.getSimpleName().substring(0, c.getSimpleName().length() - 6));
            
            auditTrail.setRecordKey(base2.getLogicalKey());
            auditTrail.setActorAction(ActorAction.REJECT);
            auditTrail.setRecordStatus(MkCtrlStatus.REJECTED);
            
            Class<?> tmpClass = this.getTmpClass(base2.getClass());
            Method m = tmpClass.getMethod("getActionType", new Class[]{});
            
            if (!(m.getReturnType().equals(DbActionType.class)))
            {
                throw new IllegalArgumentException();
            }
            
            auditTrail.setActionType((DbActionType)m.invoke(base2, new Object[0]));
            
            if (DbActionType.UPDATE.equals(auditTrail.getActionType()))
            {
                BaseHolder base = (BaseHolder)args[1];
                auditTrail.setXmlContent(this.getXml(base, base2, auditTrail.getActionType(), auditTrail.getActionDate()));
            }
            else
            {
                auditTrail.setXmlContent(this.getXml(base2, auditTrail.getActionType(), auditTrail.getActionDate()));
            }
        }
        else if ("mkWithdraw".equals(methodName))
        {
            if (!(args[2] instanceof BaseHolder))
            {
                throw new IllegalArgumentException();
            }
            
            BaseHolder base2 = (BaseHolder)args[2];
            Class<?> c = this.getMainClass(base2.getClass());
            auditTrail.setRecordType(c.getSimpleName().substring(0, c.getSimpleName().length() - 6));
            
            auditTrail.setRecordKey(base2.getLogicalKey());
            auditTrail.setActorAction(ActorAction.WITHDRAW);
            auditTrail.setRecordStatus(MkCtrlStatus.WITHDRAWN);
            
            Class<?> tmpClass = this.getTmpClass(base2.getClass());
            Method m = tmpClass.getMethod("getActionType", new Class[]{});
            
            if (!(m.getReturnType().equals(DbActionType.class)))
            {
                throw new IllegalArgumentException();
            }
            
            auditTrail.setActionType((DbActionType)m.invoke(base2, new Object[0]));
            
            if (DbActionType.UPDATE.equals(auditTrail.getActionType()))
            {
                BaseHolder base = (BaseHolder)args[1];
                auditTrail.setXmlContent(this.getXml(base, base2, auditTrail.getActionType(), auditTrail.getActionDate()));
            }
            else
            {
                auditTrail.setXmlContent(this.getXml(base2, auditTrail.getActionType(), auditTrail.getActionDate()));
            }
        }
        
        
        mapper.insert(auditTrail);
    }
    
    
    private String outputDoc(Document doc) throws IOException
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try
        {
            (new XMLOutputter(Format.getPrettyFormat())).output(doc, os);

            return new String(os.toByteArray(), CommonConstants.ENCODING_UTF8);
        }
        finally
        {
            os.close();
            os = null;
        }

    }
    
    
    private String getXml(BaseHolder base, DbActionType actionType,
        Date actionDate) throws IllegalArgumentException,
        IllegalAccessException, InvocationTargetException, IOException
    {
        Document doc = new Document();
        Element root = new Element(ELEMENT_AUDIT_SNAPSHOT);
        root.setAttribute(ATTR_ACTION_TYPE, actionType.name());
        root.setAttribute(ATTR_ACTION_DATE, formatDate(actionDate));
        doc.setRootElement(root);

        root.addContent(computeRecord(base, null));

        return outputDoc(doc);
    }
    
    
    private String getXml(BaseHolder oldBase, BaseHolder newBase,
        DbActionType actionType, Date actionDate)
        throws IllegalArgumentException, IllegalAccessException,
        InvocationTargetException, IOException
    {
        Document doc = new Document();
        Element root = new Element(ELEMENT_AUDIT_SNAPSHOT);
        root.setAttribute(ATTR_ACTION_TYPE, actionType.name());
        root.setAttribute(ATTR_ACTION_DATE, formatDate(actionDate));
        doc.setRootElement(root);

        root.addContent(computeRecord(oldBase, newBase, null));

        return outputDoc(doc);
    }
    
    
    private Element computeRecord(BaseHolder base, DbActionType actionType)
        throws IllegalArgumentException, IllegalAccessException,
        InvocationTargetException
    {
        Class<?> c = getMainClass(base.getClass());
        
        Element rlt = new Element(ELEMENT_RECORD);
        
        rlt.setAttribute(ATTR_RECORD_TYPE,
            c.getSimpleName().substring(0, c.getSimpleName().length() - 6));
        
        if (null != actionType)
        {
            rlt.setAttribute(ATTR_ACTION_TYPE, actionType.name());
        }
        
        
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods)
        {
            if (method.getReturnType().equals(Void.TYPE))
            {
                continue;
            }
            
            if (method.getName().equals(CUSTOM_IDENTIFICATION_METHOD))
            {
                continue;
            }
            
            if (!method.getName().startsWith("get"))
            {
                continue;
            }
            
            Object fieldValue = method.invoke(base, new Object[] {});
            
            if (null == fieldValue)
            {
                continue;
            }
            
            if (method.getReturnType().equals(Date.class))
            {
                fieldValue = formatDate((Date)fieldValue);
            }
            
            String fieldname = method.getName().substring(3);
            fieldname = fieldname.substring(0, 1).toLowerCase()
                + fieldname.substring(1);
            
            Element field = new Element(ELEMENT_FIELD);
            field.setAttribute(ATTR_NAME, fieldname);
            
            if (method.getReturnType().equals(List.class))
            {
                @SuppressWarnings("unchecked")
                Iterator<BaseHolder> it = ((List<BaseHolder>)fieldValue).iterator();
                
                Element list = new Element(ELEMENT_LIST);
                while (it.hasNext())
                {
                    list.addContent(computeRecord(it.next(), null));
                }
                
                field.addContent(list);
            }
            else
            {
                field.addContent(fieldValue.toString());
            }
            
            rlt.addContent(field);
        }
        
        return rlt;
    }
    
    
    private Element computeRecord(BaseHolder oldBase, BaseHolder newBase, DbActionType actionType)
        throws IllegalArgumentException, IllegalAccessException,
        InvocationTargetException
    {
        Class<?> c = getMainClass(oldBase.getClass());
        
        Element rlt = new Element(ELEMENT_RECORD);
        
        rlt.setAttribute(ATTR_RECORD_TYPE,
            c.getSimpleName().substring(0, c.getSimpleName().length() - 6));
        
        if (null != actionType)
        {
            rlt.setAttribute(ATTR_ACTION_TYPE, actionType.name());
        }
        
        
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods)
        {
            if (method.getReturnType().equals(Void.TYPE))
            {
                continue;
            }
            
            if (method.getName().equals(CUSTOM_IDENTIFICATION_METHOD))
            {
                continue;
            }
            
            if (!method.getName().startsWith("get"))
            {
                continue;
            }
            
            Object oldFieldValue = method.invoke(oldBase, new Object[] {});
            Object newFieldValue = method.invoke(newBase, new Object[] {});
            
            if (null == oldFieldValue && null == newFieldValue)
            {
                continue;
            }
            
            if (method.getReturnType().equals(Date.class))
            {
                if (null != oldFieldValue)
                {
                    oldFieldValue = formatDate((Date)oldFieldValue);
                }
                
                if (null != newFieldValue)
                {
                    newFieldValue = formatDate((Date)newFieldValue);
                }
            }
            
            String fieldname = method.getName().substring(3);
            fieldname = fieldname.substring(0, 1).toLowerCase()
                + fieldname.substring(1);
            
            Element field = new Element(ELEMENT_FIELD);
            field.setAttribute(ATTR_NAME, fieldname);
            
            if (method.getReturnType().equals(List.class))
            {
                Map<String, BaseHolder> oMap = new HashMap<String, BaseHolder>();
                Map<String, BaseHolder> nMap = new HashMap<String, BaseHolder>();
                
                if (oldFieldValue != null)
                {
                    @SuppressWarnings("unchecked")
                    Iterator<BaseHolder> it = ((List<BaseHolder>)oldFieldValue).iterator();
                    while (it.hasNext())
                    {
                        BaseHolder base_ = it.next();
                        oMap.put(base_.getCustomIdentification(), base_);
                    }
                }
                
                if (newFieldValue != null)
                {
                    @SuppressWarnings("unchecked")
                    Iterator<BaseHolder> it2 = ((List<BaseHolder>)newFieldValue).iterator();
                    while (it2.hasNext())
                    {
                        BaseHolder base_ = it2.next();
                        nMap.put(base_.getCustomIdentification(), base_);
                    }
                }
                
                
                
                Element list = new Element(ELEMENT_LIST);
                for (Map.Entry<String, BaseHolder> entry : oMap.entrySet())
                {
                    if (nMap.containsKey(entry.getKey()))
                    {
                        list.addContent(computeRecord(entry.getValue(), nMap.get(entry.getKey()), DbActionType.UPDATE));
                        
                        nMap.remove(entry.getKey());
                    }
                    else
                    {
                        list.addContent(computeRecord(entry.getValue(), DbActionType.DELETE));
                    }
                }
                
                
                for (Map.Entry<String, BaseHolder> entry : nMap.entrySet())
                {
                    list.addContent(computeRecord(entry.getValue(), DbActionType.CREATE));
                }
                
                field.addContent(list);
            }
            else
            {
                if(null == oldFieldValue || null == newFieldValue
                    || !oldFieldValue.equals(newFieldValue))
                {
                    if (null == oldFieldValue)
                    {
                        oldFieldValue = "";
                    }
                    
                    if (null == newFieldValue)
                    {
                        newFieldValue = "";
                    }
                    
                    field.setAttribute("isModified", "true");

                    Element old_ = new Element("oldValue");
                    Element new_ = new Element("newValue");

                    old_.addContent(oldFieldValue.toString());
                    new_.addContent(newFieldValue.toString());

                    field.addContent(old_);
                    field.addContent(new_);
                }
                else
                {
                    field.addContent(oldFieldValue.toString());
                }
                
            }
            
            rlt.addContent(field);
        }
        
        return rlt;
    }
    
    
    public Class<?> getMainClass(Class<?> c)
    {
        if(c.getName().endsWith("TmpExHolder"))
        {
            return c.getSuperclass().getSuperclass();
        }

        if(c.getName().endsWith("ExHolder"))
        {
            return c.getSuperclass();
        }

        if(c.getName().endsWith("TmpHolder"))
        {
            return c.getSuperclass();
        }

        return c;
    }
    
    
    public Class<?> getTmpClass(Class<?> c)
    {
        if(c.getName().endsWith("TmpExHolder"))
        {
            return c.getSuperclass();
        }

        return c;
    }
    
    
    private String formatDate(Date value)
    {
        return DateUtil.getInstance().convertDateToString(value, "yyyy-MM-dd")
            + "T"
            + DateUtil.getInstance().convertDateToString(value, "HH:mm:ss");
    }
    
    
    /*public static void main(String[] args) throws IllegalArgumentException,
        IllegalAccessException, InvocationTargetException, IOException
    {
        BaseHolder oldOne = new RoleTmpExHolder();
        BaseHolder newOne = new RoleTmpExHolder();

        ((RoleTmpExHolder)oldOne).setRoleOid(BigDecimal.TEN);
        ((RoleTmpExHolder)oldOne).setRoleId("role ID .......");
        ((RoleTmpExHolder)oldOne).setRoleName("role name");
        ((RoleTmpExHolder)oldOne).setRoleType(RoleType.ADMIN);
        ((RoleTmpExHolder)oldOne).setUserTypeOid(BigDecimal.ONE);
        ((RoleTmpExHolder)oldOne).setCreateDate(new Date());
        ((RoleTmpExHolder)oldOne).setCreateBy("OuYang Liang");
        ((RoleTmpExHolder)oldOne).setBuyerOid(BigDecimal.valueOf(25));
        ((RoleTmpExHolder)oldOne).setSupplierOid(BigDecimal.valueOf(5));

        List<RoleOperationHolder> roList = new ArrayList<RoleOperationHolder>();
        RoleOperationHolder ro = new RoleOperationHolder();
        ro.setRoleOid(BigDecimal.TEN);
        ro.setOpnId("100000");
        roList.add(ro);
        ro = new RoleOperationHolder();
        ro.setRoleOid(BigDecimal.TEN);
        ro.setOpnId("100001");
        roList.add(ro);
        ro = new RoleOperationHolder();
        ro.setRoleOid(BigDecimal.TEN);
        ro.setOpnId("100002");
        roList.add(ro);
        ro = new RoleOperationHolder();
        ro.setRoleOid(BigDecimal.TEN);
        ro.setOpnId("100003");
        roList.add(ro);
        ((RoleTmpExHolder)oldOne).setRoleOperations(roList);

        ((RoleTmpExHolder)newOne).setRoleOid(BigDecimal.TEN);
        ((RoleTmpExHolder)newOne).setRoleId("role ID");
        ((RoleTmpExHolder)newOne).setRoleName("role name");
        ((RoleTmpExHolder)newOne).setRoleType(RoleType.BUYER);
        ((RoleTmpExHolder)newOne).setUserTypeOid(BigDecimal.ONE);
        ((RoleTmpExHolder)newOne).setCreateDate(new Date());
        ((RoleTmpExHolder)newOne).setCreateBy("OuYang");
        ((RoleTmpExHolder)newOne).setBuyerOid(BigDecimal.valueOf(25));
        ((RoleTmpExHolder)newOne).setSupplierOid(BigDecimal.valueOf(5));

        roList = new ArrayList<RoleOperationHolder>();
        ro = new RoleOperationHolder();
        ro = new RoleOperationHolder();
        ro.setRoleOid(BigDecimal.TEN);
        ro.setOpnId("100001");
        roList.add(ro);
        ro = new RoleOperationHolder();
        ro.setRoleOid(BigDecimal.TEN);
        ro.setOpnId("100009");
        roList.add(ro);
        ro = new RoleOperationHolder();
        ro.setRoleOid(BigDecimal.TEN);
        ro.setOpnId("100008");
        roList.add(ro);
        ((RoleTmpExHolder)newOne).setRoleOperations(roList);

        AuditTrailAdvice a = new AuditTrailAdvice();

        System.out.println(a.getXml(oldOne, DbActionType.DELETE, new Date()));

        System.out.println(a.getXml(oldOne, newOne, DbActionType.UPDATE,
            new Date()));
    }*/
    
}
