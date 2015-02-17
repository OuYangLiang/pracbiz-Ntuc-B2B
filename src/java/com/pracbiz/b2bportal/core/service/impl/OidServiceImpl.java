package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.mapper.OidGenericMapper;
import com.pracbiz.b2bportal.core.service.OidService;

public class OidServiceImpl implements OidService
{
    @Autowired OidGenericMapper mapper;

    @Override
    public BigDecimal getOid() throws Exception
    {
        return mapper.getOid();
    }

}
