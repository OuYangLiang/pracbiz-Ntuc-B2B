package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.LastUpdateFrom;

public class GroupUserTmpHolder extends GroupUserHolder
{
    private static final long serialVersionUID = -1515802118480579345L;

    private LastUpdateFrom lastUpdateFrom;
    private Boolean approved;
    private DbActionType actionType;

    /**
     * Getter of lastUpdateFrom.
     * 
     * @return Returns the lastUpdateFrom.
     */
    public LastUpdateFrom getLastUpdateFrom()
    {
        return lastUpdateFrom;
    }

    /**
     * Setter of lastUpdateFrom.
     * 
     * @param lastUpdateFrom The lastUpdateFrom to set.
     */
    public void setLastUpdateFrom(LastUpdateFrom lastUpdateFrom)
    {
        this.lastUpdateFrom = lastUpdateFrom;
    }

    /**
     * Getter of approved.
     * 
     * @return Returns the approved.
     */
    public Boolean getApproved()
    {
        return approved;
    }

    /**
     * Setter of approved.
     * 
     * @param approved The approved to set.
     */
    public void setApproved(Boolean approved)
    {
        this.approved = approved;
    }

    /**
     * Getter of actionType.
     * 
     * @return Returns the actionType.
     */
    public DbActionType getActionType()
    {
        return actionType;
    }

    /**
     * Setter of actionType.
     * 
     * @param actionType The actionType to set.
     */
    public void setActionType(DbActionType actionType)
    {
        this.actionType = actionType;
    }

}