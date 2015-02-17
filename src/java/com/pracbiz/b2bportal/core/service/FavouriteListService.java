package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.extension.FavouriteListExHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public interface FavouriteListService extends BaseService<FavouriteListExHolder>,
        DBActionService<FavouriteListExHolder>
{
    public List<FavouriteListExHolder> selectFavouriteListByUserOid(
            BigDecimal userOid) throws Exception;
    

    public void deleteFavouriteListByUserOid(BigDecimal userOid)
            throws Exception;
}
