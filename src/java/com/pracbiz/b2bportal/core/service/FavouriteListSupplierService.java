package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.FavouriteListSupplierHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public interface FavouriteListSupplierService extends
        BaseService<FavouriteListSupplierHolder>,
        DBActionService<FavouriteListSupplierHolder>
{
    public List<FavouriteListSupplierHolder> selectFavouriteListSupplierByListOid(
            BigDecimal listOid) throws Exception;
}
