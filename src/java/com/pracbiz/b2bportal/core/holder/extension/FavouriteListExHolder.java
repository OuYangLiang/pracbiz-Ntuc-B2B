package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.core.holder.FavouriteListHolder;

public class FavouriteListExHolder extends FavouriteListHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<BigDecimal> availableSupplierOids;
    private List<BigDecimal> selectedSupplierOids;
    public List<BigDecimal> getAvailableSupplierOids()
    {
        return availableSupplierOids;
    }
    public void setAvailableSupplierOids(List<BigDecimal> availableSupplierOids)
    {
        this.availableSupplierOids = availableSupplierOids;
    }
    public List<BigDecimal> getSelectedSupplierOids()
    {
        return selectedSupplierOids;
    }
    public void setSelectedSupplierOids(List<BigDecimal> selectedSupplierOids)
    {
        this.selectedSupplierOids = selectedSupplierOids;
    }
    
    

}
