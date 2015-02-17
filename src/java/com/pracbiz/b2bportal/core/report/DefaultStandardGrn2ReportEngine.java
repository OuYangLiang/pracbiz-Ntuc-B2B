//*****************************************************************************
//
// File Name       :  DefaultStandardGrnReportEngine.java
// Date Created    :  2012-12-18
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-18 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.report;

import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DefaultStandardGrn2ReportEngine extends DefaultStandardGrnReportEngine
{
    @Override
    protected String reportTemplate(GrnHolder data, int flag)
    {
        if (flag == 1)
        {
            return "reports/standard/grn/STANDARD_GRN" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".jasper";
        }
        else
        {
            return "reports/standard/grn/STANDARD_GRN_2.jasper";
        }
    }
}
