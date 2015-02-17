//*****************************************************************************
//
// File Name       :  DefaultStandardRTVReportEngine.java
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

import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DefaultStandardRtv2ReportEngine extends DefaultStandardRtvReportEngine
{
    @Override
    protected String reportTemplate(RtvHolder data, int flag)
    {
        if (flag == 1)
        {
            return "reports/standard/rtv/STANDARD_RTV" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".jasper";
        }
        else
        {
            return "reports/standard/rtv/STANDARD_RTV_2.jasper";
        }
    }

}
