package com.pracbiz.b2bportal.core.report.excel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingInvStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.extension.BusinessRuleExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class MatchingReportParameter
{
    private BigDecimal buyerOid;
    private String buyerCode;
    private String buyerName;
    private String supplierCode;
    private String supplierName;

    private String poNo;
    private String poType;
    private Date poDate;
    private String storeCode;
    private String storeName;
    private BigDecimal poAmt;
    private String poSubType2;

    private String invNo;
    private Date invDate;
    private BigDecimal invAmt;
    private BigDecimal expInvAmt;
    private BigDecimal invVat;
    private BigDecimal invAmtWithVat;
    private List<InvDetailHolder> invDetails;

    private String[] grnNos;
    private Date[] grnDates;
    private BigDecimal grnAmt;
    private Date firstGrnDate;

    private String dnNo;
    private Date dnDate;
    private BigDecimal dnAmt;
    private BigDecimal dnVat;
    private BigDecimal dnAmtWithVat;

    private PoInvGrnDnMatchingInvStatus actionStatus;
    private Date matchingDate;

    private MatchingResult matchingResult;
    private List<PoInvGrnDnMatchingDetailExHolder> matchingDetails;
    private List<BusinessRuleHolder> businessRuleList;
    
    private Integer daysSpaned;
    private String buyerStatus;
    private String priceStatus;
    private String qtyStatus;
    private Date approveInvDate;
    private Boolean closed;
    private String closedRemarks;
    private Boolean revised;
    

    public MatchingReportParameter()
    {
        
    }


    public MatchingReportParameter(PoInvGrnDnMatchingHolder poInvGrnDnMatching,
            PoHolder poHolder, InvHolder invHolder, List<GrnHolder> grnHolders,
            DnHolder dnHolder, List<BusinessRuleHolder> businessRules,
            String poType, boolean computeMatchingStatus)
    {
        setMatchingResult(new MatchingResult());

        businessRuleList = businessRules;

        initReport(poInvGrnDnMatching, poHolder, invHolder, grnHolders, dnHolder);

        setMatchingDetails(poInvGrnDnMatching.getDetailList());

        calculatePoAmtAndExpInvAmt();
        
        initCountOfUnmatched(this);
        
        if (computeMatchingStatus)
        {
            initMatchingStatus();
        }
        else
        {
            this.getMatchingResult().setMatchingStatus(poInvGrnDnMatching.getMatchingStatus());
        }
        
        boolean needPriceStatus = PoInvGrnDnMatchingStatus.PRICE_UNMATCHED.equals(this.matchingResult.getMatchingStatus()) ||
            PoInvGrnDnMatchingStatus.UNMATCHED.equals(this.matchingResult.getMatchingStatus());
        
        boolean needQtyStatus = PoInvGrnDnMatchingStatus.QTY_UNMATCHED.equals(this.matchingResult.getMatchingStatus()) ||
                PoInvGrnDnMatchingStatus.UNMATCHED.equals(this.matchingResult.getMatchingStatus());
        
        setPriceStatus(needPriceStatus ? poInvGrnDnMatching.getPriceStatus().name() : "NA");
        setQtyStatus(needQtyStatus ? poInvGrnDnMatching.getQtyStatus().name() : "NA");

        setPoType(poType);

    }


    public static String getGrnNosAndGrnDates(String[] grnNos, Date[] grnDates)
    {
        String result = "";
        if (grnNos == null || grnNos.length == 0)
        {
            return result;
        }
        for (int i = 0; i < grnNos.length; i++)
        {
            result = grnNos[i] + "("
                    + DateUtil.getInstance().convertDateToString(grnDates[i])
                    + ")" + "\r\n";
        }
        return result;
    }
    
    
    public static String getGrnNos(String[] grnNos)
    {
        String result = "";
        if (grnNos == null || grnNos.length == 0)
        {
            return result;
        }
        for (int i = 0; i < grnNos.length; i++)
        {
            result = grnNos[i] + "\r\n";
        }
        return result;
    }
    
    
    public static String getGrnDates(Date[] grnDates)
    {
        String result = "";
        if (grnDates == null || grnDates.length == 0)
        {
            return result;
        }
        for (int i = 0; i < grnDates.length; i++)
        {
            result = DateUtil.getInstance().convertDateToString(grnDates[i]) + "\r\n";
        }
        return result;
    }


    public static String getNoAndDate(String No, Date date)
    {
        String result = "";
        if (No == null || No.trim().isEmpty())
        {
            return result;
        }
        return StringUtil.getInstance().convertObjectToString(No) + "("
                + DateUtil.getInstance().convertDateToString(date) + ")";
    }
    
    
    public static String convertBigDecimalToDouble(BigDecimal input)
    {
        if (input == null)
        {
            return null;
        }
        return new DecimalFormat("#,##0.00").format(input);
    }


    private void initReport(PoInvGrnDnMatchingHolder poInvGrnDnMatching,
            PoHolder poHolder, InvHolder invHolder, List<GrnHolder> grnHolders, DnHolder dnHolder)
    {
        this.setBuyerOid(poInvGrnDnMatching.getBuyerOid());
        this.setBuyerCode(poInvGrnDnMatching.getBuyerCode());
        this.setBuyerName(poInvGrnDnMatching.getBuyerName());
        this.setSupplierCode(poInvGrnDnMatching.getSupplierCode());
        this.setSupplierName(poInvGrnDnMatching.getSupplierName());
        this.setPoNo(poInvGrnDnMatching.getPoNo());
        this.setPoDate(poInvGrnDnMatching.getPoDate());
        this.setStoreCode(poInvGrnDnMatching.getPoStoreCode());
        this.setStoreName(poInvGrnDnMatching.getPoStoreName());
        this.setActionStatus(poInvGrnDnMatching.getInvStatus());
        this.setMatchingDate(poInvGrnDnMatching.getMatchingDate());
        this.setDaysSpaned(DateUtil.getInstance().daysAfterDate(poInvGrnDnMatching.getSupplierStatusActionDate(), new Date()));
        this.setBuyerStatus(poInvGrnDnMatching.getBuyerStatus().name());
        this.setClosed(poInvGrnDnMatching.getClosed());
        this.setClosedRemarks(poInvGrnDnMatching.getClosedRemarks());
        this.setApproveInvDate(poInvGrnDnMatching.getInvStatusActionDate());
        this.setRevised(poInvGrnDnMatching.getRevised());
        
        if (poHolder != null)
        {
            this.setPoSubType2(poHolder.getPoHeader().getPoSubType2());
        }
        if (invHolder != null)
        {
            this.setInvNo(poInvGrnDnMatching.getInvNo());
            this.setInvAmt(poInvGrnDnMatching.getInvAmt());
            this.setInvDate(invHolder.getHeader().getInvDate());
            this.setInvVat(invHolder.getHeader().getVatAmount());
            this.setInvAmtWithVat(invHolder.getHeader().getInvAmountWithVat());
            
            if (invHolder.getDetails() != null && !invHolder.getDetails().isEmpty())
            {
                this.setInvDetails(invHolder.getDetails());
            }
        }
        if (dnHolder != null)
        {
            this.setDnNo(poInvGrnDnMatching.getDnNo());
            this.setDnAmt(poInvGrnDnMatching.getDnAmt());
            this.setDnDate(dnHolder.getDnHeader().getDnDate());
            this.setDnVat(dnHolder.getDnHeader().getTotalVat());
            this.setDnAmtWithVat(dnHolder.getDnHeader().getTotalCostWithVat());
        }
        if (grnHolders != null && !grnHolders.isEmpty())
        {
            String[] grnNos = new String[grnHolders.size()];
            Date[] grnDates = new Date[grnHolders.size()];
            for (int i = 0; i < grnHolders.size(); i++)
            {
                GrnHolder grnHolder = grnHolders.get(i);
                GrnHeaderHolder grnHeader = grnHolder.getHeader();
                grnNos[i] = grnHeader.getGrnNo();
                grnDates[i] = grnHeader.getGrnDate();
            }
            this.setGrnNos(grnNos);
            this.setGrnDates(grnDates);
            
            BigDecimal grnAmt = BigDecimal.ZERO;
            List<PoInvGrnDnMatchingGrnHolder> matchingGrns = poInvGrnDnMatching.getGrnList();
            for (PoInvGrnDnMatchingGrnHolder matchingGrn : matchingGrns)
            {
                grnAmt = grnAmt.add(matchingGrn.getGrnAmt());
            }
            this.setGrnAmt(grnAmt);
            
            //set firstGrnDate
            Collections.sort(grnHolders, new Comparator<GrnHolder>()
            {
                @Override
                public int compare(GrnHolder m1, GrnHolder m2)
                {
                    return m1.getHeader().getGrnDate().before(m2.getHeader().getGrnDate()) ? -1 : 1;
                }
            });
            
            this.setFirstGrnDate(grnHolders.get(0).getHeader().getGrnDate());
        }
    }
    
    
    private void calculatePoAmtAndExpInvAmt()
    {
        BigDecimal poAmt = BigDecimal.ZERO;
        BigDecimal expInvAmt = BigDecimal.ZERO;
        for (PoInvGrnDnMatchingDetailHolder detail : matchingDetails)
        {
            poAmt = poAmt.add(detail.getPoAmt()== null ? BigDecimal.ZERO : detail.getPoAmt());
            detail.setExpInvAmt((detail.getPoPrice() == null ? BigDecimal.ZERO
                    : detail.getPoPrice()).multiply(detail.getGrnQty() == null ? BigDecimal.ZERO
                    : detail.getGrnQty()));
            expInvAmt = expInvAmt.add(detail.getExpInvAmt() == null ? BigDecimal.ZERO : detail.getExpInvAmt());
        }
        this.setPoAmt(poAmt);
        this.setExpInvAmt(expInvAmt);
    }


    private void initMatchingStatus()
    {
        MatchingResult matchingResult = this.getMatchingResult();
        
        if (null == this.getInvNo() && null == this.getInvAmt())
        {
            matchingResult
                    .setMatchingStatus(PoInvGrnDnMatchingStatus.INSUFFICIENT_INV);
            return;
        }

        int countOfUnmatchedUnitPrice = matchingResult
                .getCountOfUnmatchedUnitPrice();
        int countOfUnmatchedQty = matchingResult.getCountOfUnmatchedQty();
        int countOfUnmatchedAmt = matchingResult.getCountOfUnmatchedAmt();

        if (countOfUnmatchedUnitPrice > 0 && countOfUnmatchedQty > 0)
        {
            matchingResult
                    .setMatchingStatus(PoInvGrnDnMatchingStatus.UNMATCHED);
        }
        else if (countOfUnmatchedUnitPrice > 0 && countOfUnmatchedQty == 0)
        {
            if (this.getDnNo() == null || this.getDnNo().isEmpty())
            {
                matchingResult
                        .setMatchingStatus(PoInvGrnDnMatchingStatus.PRICE_UNMATCHED);
            }
            else if (countOfUnmatchedAmt == 0)
            {
                matchingResult
                        .setMatchingStatus(PoInvGrnDnMatchingStatus.MATCHED_BY_DN);
            }
            else
            {
                matchingResult
                        .setMatchingStatus(PoInvGrnDnMatchingStatus.PRICE_UNMATCHED);
            }
        }
        else if (countOfUnmatchedUnitPrice == 0 && countOfUnmatchedQty > 0)
        {
            if (this.getDnNo() == null || this.getDnNo().isEmpty())
            {
                matchingResult
                        .setMatchingStatus(PoInvGrnDnMatchingStatus.QTY_UNMATCHED);
            }
            else if (countOfUnmatchedAmt == 0)
            {
                matchingResult
                        .setMatchingStatus(PoInvGrnDnMatchingStatus.MATCHED_BY_DN);
            }
            else
            {
                matchingResult
                        .setMatchingStatus(PoInvGrnDnMatchingStatus.QTY_UNMATCHED);
            }
        }
        else if (countOfUnmatchedUnitPrice == 0 && countOfUnmatchedQty == 0)
        {
            if (countOfUnmatchedAmt == 0)
            {
                matchingResult
                        .setMatchingStatus(PoInvGrnDnMatchingStatus.MATCHED);
            }
            else if (this.getDnNo() == null || this.getDnNo().isEmpty())
            {
                matchingResult
                        .setMatchingStatus(PoInvGrnDnMatchingStatus.MATCHED);
            }
            else
            {
                matchingResult
                        .setMatchingStatus(PoInvGrnDnMatchingStatus.AMOUNT_UNMATCHED);
            }
        }
    }


    private void initCountOfUnmatched(MatchingReportParameter report)
    {
        if (report.getMatchingDetails() == null
                || report.getMatchingDetails().isEmpty())
        {
            return;
        }
        for (PoInvGrnDnMatchingDetailHolder reportDetail : report.getMatchingDetails())
        {
            boolean onlyPoItem = (null == reportDetail.getInvPrice())
                    && (null == reportDetail.getInvQty())
                    && (null == reportDetail.getGrnQty());

            if (onlyPoItem) return;

            BigDecimal poPrice = reportDetail.getPoPrice() == null ? (reportDetail.getInvPrice() == null ? BigDecimal.ZERO : reportDetail.getInvPrice()) 
                    : reportDetail.getPoPrice();
            BigDecimal invPrice = reportDetail.getInvPrice() == null ? poPrice : reportDetail.getInvPrice();

            BusinessRuleHolder priceInvLessPoFlag = isTreatThisMatchingRule("PriceInvLessPo");
            
            if ((priceInvLessPoFlag == null && poPrice.compareTo(invPrice) != 0) 
                    || (priceInvLessPoFlag != null && poPrice.compareTo(invPrice) < 0))
            {
                report.getMatchingResult().setCountOfUnmatchedUnitPrice(
                        report.getMatchingResult()
                        .getCountOfUnmatchedUnitPrice() + 1);
            }

            BigDecimal poQty = reportDetail.getPoQty() == null ? BigDecimal.ZERO
                    : reportDetail.getPoQty();
            BigDecimal invQty = reportDetail.getInvQty() == null ? BigDecimal.ZERO
                    : reportDetail.getInvQty();
            BigDecimal grnQty = reportDetail.getGrnQty() == null ? BigDecimal.ZERO
                    : reportDetail.getGrnQty();

            BusinessRuleHolder qtyInvLessGrnFlag = isTreatThisMatchingRule(
                    /*report.getBuyerCode(), */"QtyInvLessGrn");
            if (qtyInvLessGrnFlag == null)
            {
                if (invQty.compareTo(grnQty) != 0)
                {
                    report.getMatchingResult()
                            .setCountOfUnmatchedQty(
                                    report.getMatchingResult()
                                            .getCountOfUnmatchedQty() + 1);
                }
                
            }
            else
            {
                BusinessRuleHolder qtyPoLessGrnFlag = isTreatThisMatchingRule("QtyPoLessGrn");
                
                if (invQty.compareTo(grnQty) > 0)
                {
                    report.getMatchingResult()
                            .setCountOfUnmatchedQty(
                                    report.getMatchingResult()
                                            .getCountOfUnmatchedQty() + 1);
                }
                else if (qtyPoLessGrnFlag == null && grnQty.compareTo(poQty) > 0)
                {
                    report.getMatchingResult()
                    .setCountOfUnmatchedQty(
                            report.getMatchingResult()
                            .getCountOfUnmatchedQty() + 1);
                }
            }

            BigDecimal expInvAmt = poPrice.multiply(grnQty);
            BigDecimal dnAmt = reportDetail.getDnAmt() == null ? BigDecimal.ZERO
                    : reportDetail.getDnAmt();
            BigDecimal invAmt = reportDetail.getInvAmt() == null ? BigDecimal.ZERO
                    : reportDetail.getInvAmt();

            BigDecimal matchAmt = expInvAmt.add(dnAmt);
            
            boolean canMoreThanInv = priceInvLessPoFlag != null || qtyInvLessGrnFlag != null;
            
            BusinessRuleExHolder useAmountTolerance = (BusinessRuleExHolder) isTreatThisMatchingRule("AmountTolerance");
            
            BigDecimal tolerance = useAmountTolerance == null || useAmountTolerance.getNumValue() == null ? BigDecimal.ZERO : useAmountTolerance.getNumValue();
            
            BigDecimal amtTolerance = matchAmt.subtract(invAmt);
            
            if (canMoreThanInv && amtTolerance.compareTo(BigDecimal.ZERO) < 0
                    || !canMoreThanInv && amtTolerance.compareTo(BigDecimal.ZERO) != 0)
            {
                if (tolerance.compareTo(amtTolerance.abs()) < 0)
                {
                    report.getMatchingResult().setCountOfUnmatchedAmt(report.getMatchingResult().getCountOfUnmatchedAmt() + 1);
                }
            }
        }

    }


    private BusinessRuleHolder isTreatThisMatchingRule(/*String buyerCode,*/
            String ruleId)
    {

        if (businessRuleList == null)
        {
            return null;
        }
        for (BusinessRuleHolder businessRule : businessRuleList)
        {
            if (ruleId.equalsIgnoreCase(businessRule.getRuleId()))
            {
                return businessRule;
            }
        }
        return null;
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode;
    }


    public String getBuyerName()
    {
        return buyerName;
    }


    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName;
    }


    public String getSupplierCode()
    {
        return supplierCode;
    }


    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode;
    }


    public String getSupplierName()
    {
        return supplierName;
    }


    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }


    public String getPoNo()
    {
        return poNo;
    }


    public void setPoNo(String poNo)
    {
        this.poNo = poNo;
    }


    public String getPoType()
    {
        return poType;
    }


    public void setPoType(String poType)
    {
        this.poType = poType;
    }


    public Date getPoDate()
    {
        return poDate == null ? null : (Date)poDate.clone();
    }


    public void setPoDate(Date poDate)
    {
        this.poDate = poDate == null ? null : (Date)poDate.clone();
    }


    public String getStoreCode()
    {
        return storeCode;
    }


    public void setStoreCode(String storeCode)
    {
        this.storeCode = storeCode;
    }


    public BigDecimal getPoAmt()
    {
        return poAmt;
    }


    public void setPoAmt(BigDecimal poAmt)
    {
        this.poAmt = poAmt;
    }


    public String getInvNo()
    {
        return invNo;
    }


    public void setInvNo(String invNo)
    {
        this.invNo = invNo;
    }


    public Date getInvDate()
    {
        return invDate == null ? null : (Date)invDate.clone();
    }


    public void setInvDate(Date invDate)
    {
        this.invDate = invDate == null ? null : (Date)invDate.clone();
    }


    public BigDecimal getInvAmt()
    {
        return invAmt;
    }


    public void setInvAmt(BigDecimal invAmt)
    {
        this.invAmt = invAmt;
    }


    public BigDecimal getExpInvAmt()
    {
        return expInvAmt;
    }


    public void setExpInvAmt(BigDecimal expInvAmt)
    {
        this.expInvAmt = expInvAmt;
    }


    public BigDecimal getInvVat()
    {
        return invVat;
    }


    public void setInvVat(BigDecimal invVat)
    {
        this.invVat = invVat;
    }


    public BigDecimal getInvAmtWithVat()
    {
        return invAmtWithVat;
    }


    public void setInvAmtWithVat(BigDecimal invAmtWithVat)
    {
        this.invAmtWithVat = invAmtWithVat;
    }


    public List<InvDetailHolder> getInvDetails()
    {
        return invDetails;
    }


    public void setInvDetails(List<InvDetailHolder> invDetails)
    {
        this.invDetails = invDetails;
    }


    public String[] getGrnNos()
    {
        return grnNos == null ? null : (String[])grnNos.clone();
    }


    public void setGrnNos(String[] grnNos)
    {
        this.grnNos = grnNos == null ? null : (String[])grnNos.clone();
    }


    public Date[] getGrnDates()
    {
        return grnDates == null ? null : (Date[])grnDates.clone();
    }


    public void setGrnDates(Date[] grnDates)
    {
        this.grnDates = grnDates == null ? null : (Date[])grnDates.clone();
    }


    public BigDecimal getGrnAmt()
    {
        return grnAmt;
    }


    public void setGrnAmt(BigDecimal grnAmt)
    {
        this.grnAmt = grnAmt;
    }


    public String getDnNo()
    {
        return dnNo;
    }


    public void setDnNo(String dnNo)
    {
        this.dnNo = dnNo;
    }


    public Date getDnDate()
    {
        return dnDate == null ? null : (Date)dnDate.clone();
    }


    public void setDnDate(Date dnDate)
    {
        this.dnDate = dnDate == null ? null : (Date)dnDate.clone();
    }


    public BigDecimal getDnAmt()
    {
        return dnAmt;
    }


    public void setDnAmt(BigDecimal dnAmt)
    {
        this.dnAmt = dnAmt;
    }


    public BigDecimal getDnVat()
    {
        return dnVat;
    }


    public void setDnVat(BigDecimal dnVat)
    {
        this.dnVat = dnVat;
    }


    public BigDecimal getDnAmtWithVat()
    {
        return dnAmtWithVat;
    }


    public void setDnAmtWithVat(BigDecimal dnAmtWithVat)
    {
        this.dnAmtWithVat = dnAmtWithVat;
    }


    public PoInvGrnDnMatchingInvStatus getActionStatus()
    {
        return actionStatus;
    }


    public void setActionStatus(PoInvGrnDnMatchingInvStatus actionStatus)
    {
        this.actionStatus = actionStatus;
    }


    public MatchingResult getMatchingResult()
    {
        return matchingResult;
    }


    public void setMatchingResult(MatchingResult matchingResult)
    {
        this.matchingResult = matchingResult;
    }


    public List<PoInvGrnDnMatchingDetailExHolder> getMatchingDetails()
    {
        return matchingDetails;
    }

    
    public void setMatchingDetails(
            List<PoInvGrnDnMatchingDetailExHolder> matchingDetails)
    {
        this.matchingDetails = matchingDetails;
    }

    public Integer getDaysSpaned()
    {
        return daysSpaned;
    }


    public void setDaysSpaned(Integer daysSpaned)
    {
        this.daysSpaned = daysSpaned;
    }

    public String getPriceStatus()
    {
        return priceStatus;
    }

    public void setPriceStatus(String priceStatus)
    {
        this.priceStatus = priceStatus;
    }

    public String getQtyStatus()
    {
        return qtyStatus;
    }

    public void setQtyStatus(String qtyStatus)
    {
        this.qtyStatus = qtyStatus;
    }

    public String getBuyerStatus()
    {
        return buyerStatus;
    }

    public void setBuyerStatus(String buyerStatus)
    {
        this.buyerStatus = buyerStatus;
    }

    public Date getApproveInvDate()
    {
        return approveInvDate == null ? null : (Date)approveInvDate.clone();
    }

    public void setApproveInvDate(Date approveInvDate)
    {
        this.approveInvDate = approveInvDate == null ? null : (Date)approveInvDate.clone();
    }


    public Boolean getClosed()
    {
        return closed;
    }


    public void setClosed(Boolean closed)
    {
        this.closed = closed;
    }


    public String getClosedRemarks()
    {
        return closedRemarks;
    }


    public void setClosedRemarks(String closedRemarks)
    {
        this.closedRemarks = closedRemarks;
    }


    public String getPoSubType2()
    {
        return poSubType2;
    }


    public void setPoSubType2(String poSubType2)
    {
        this.poSubType2 = poSubType2;
    }


    public String getStoreName()
    {
        return storeName;
    }


    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }


    public Date getMatchingDate()
    {
        return matchingDate == null ? null : (Date)matchingDate.clone();
    }


    public void setMatchingDate(Date matchingDate)
    {
        this.matchingDate = matchingDate == null ? null : (Date)matchingDate.clone();
    }


    public Date getFirstGrnDate()
    {
        return firstGrnDate == null ? null : (Date)firstGrnDate.clone();
    }


    public void setFirstGrnDate(Date firstGrnDate)
    {
        this.firstGrnDate = firstGrnDate == null ? null : (Date)firstGrnDate.clone();
    }


    public Boolean getRevised()
    {
        return revised;
    }


    public void setRevised(Boolean revised)
    {
        this.revised = revised;
    }

}
