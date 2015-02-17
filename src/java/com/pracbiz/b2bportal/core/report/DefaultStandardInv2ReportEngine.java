//*****************************************************************************
//
// File Name       :  DefaultStandardInvReportEngine.java
// Date Created    :  2012-12-24
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-24 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.report;

import com.pracbiz.b2bportal.core.holder.InvHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DefaultStandardInv2ReportEngine extends DefaultStandardInvReportEngine
{
    @Override
    protected String reportTemplate(InvHolder data, int flag)
    {
        return "reports/standard/inv/STANDARD_INV_2.jasper";
    }

}
