package com.pracbiz.b2bportal.core.report.excel;

import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public class MatchingResult
{
    private int countOfUnmatchedUnitPrice;
    private int countOfUnmatchedQty;
    private int countOfUnmatchedAmt;
    private PoInvGrnDnMatchingStatus matchingStatus;


    public int getCountOfUnmatchedUnitPrice()
    {
        return countOfUnmatchedUnitPrice;
    }


    public void setCountOfUnmatchedUnitPrice(int countOfUnmatchedUnitPrice)
    {
        this.countOfUnmatchedUnitPrice = countOfUnmatchedUnitPrice;
    }


    public int getCountOfUnmatchedQty()
    {
        return countOfUnmatchedQty;
    }


    public void setCountOfUnmatchedQty(int countOfUnmatchedQty)
    {
        this.countOfUnmatchedQty = countOfUnmatchedQty;
    }


    public int getCountOfUnmatchedAmt()
    {
        return countOfUnmatchedAmt;
    }


    public void setCountOfUnmatchedAmt(int countOfUnmatchedAmt)
    {
        this.countOfUnmatchedAmt = countOfUnmatchedAmt;
    }


    public PoInvGrnDnMatchingStatus getMatchingStatus()
    {
        return matchingStatus;
    }


    public void setMatchingStatus(PoInvGrnDnMatchingStatus matchingStatus)
    {
        this.matchingStatus = matchingStatus;
    }

}
