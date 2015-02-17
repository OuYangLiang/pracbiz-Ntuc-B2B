package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public class FavouriteListHolder extends BaseHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private BigDecimal listOid;
    private String listCode;
    private BigDecimal userOid;
    
    private List<SupplierHolder> availableSupplierList;
    private List<SupplierHolder> selectedSupplierList;
    


    public BigDecimal getListOid()
    {
        return listOid;
    }


    public void setListOid(BigDecimal listOid)
    {
        this.listOid = listOid;
    }


    public String getListCode()
    {
        return listCode;
    }


    public void setListCode(String listCode)
    {
        this.listCode = listCode;
    }


    public BigDecimal getUserOid()
    {
        return userOid;
    }


    public void setUserOid(BigDecimal userOid)
    {
        this.userOid = userOid;
    }


    public List<SupplierHolder> getAvailableSupplierList()
    {
        return availableSupplierList;
    }


    public void setAvailableSupplierList(List<SupplierHolder> availableSupplierList)
    {
        this.availableSupplierList = availableSupplierList;
    }


    public List<SupplierHolder> getSelectedSupplierList()
    {
        return selectedSupplierList;
    }


    public void setSelectedSupplierList(List<SupplierHolder> selectedSupplierList)
    {
        this.selectedSupplierList = selectedSupplierList;
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(listOid);
    }

}
