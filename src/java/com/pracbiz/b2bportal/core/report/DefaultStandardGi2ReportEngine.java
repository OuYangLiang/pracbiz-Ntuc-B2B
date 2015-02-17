//*****************************************************************************
//
// File Name       :  DefaultStandardGiReportEngine.java
// Date Created    :  2013-11-14
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2013-11-14 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.report;

import com.pracbiz.b2bportal.core.holder.GiHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DefaultStandardGi2ReportEngine extends DefaultStandardGiReportEngine
    implements CoreCommonConstants
{
    @Override
    protected String reportTemplate(GiHolder data, int flag)
    {
        if (flag == 1)
        {
            return "reports/standard/gi/STANDARD_GI" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".jasper";
        }
        else
        {
            return "reports/standard/gi/STANDARD_GI2.jasper";
        }
    }

}
