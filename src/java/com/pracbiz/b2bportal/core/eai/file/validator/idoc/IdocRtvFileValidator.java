//*****************************************************************************
//
// File Name       :  IdocRtvFileValidator.java
// Date Created    :  Dec 9, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 9, 2013 12:01:05 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.validator.idoc;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.eai.file.validator.RtvFileValidator;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.RtvDocMsg;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class IdocRtvFileValidator extends RtvFileValidator implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(IdocRtvFileValidator.class);

    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        log.info("start init rtv date");
        RtvDocMsg rtvDocMsg = (RtvDocMsg)docMsg;
        InputStream inputStream = null;
        RtvHolder rtvHolder = null;
        RtvHeaderHolder rtvHeader = null;
        try
        {
            rtvHolder = new RtvHolder();
            rtvHeader = new RtvHeaderHolder();
            inputStream = new ByteArrayInputStream(input);
            Document document = FileParserUtil.getInstance().build(inputStream);
            List<Element> headerRoots = document.getRootElement().getChildren(
            "IDOC");
        
            Element headerRoot = headerRoots.get(0);
            List<Element> e1edk02 = headerRoot.getChildren("E1EDK02");
            
            Element dk02 = null;
            
            for (Element element : e1edk02)
            {
                String qualf = element.getChildText("QUALF");
                if (qualf.trim().isEmpty())
                {
                    continue;
                }
                
                if (qualf.trim().equals("001"))
                {
                    dk02 = element;
                    break;
                }
            }
            if (dk02 != null)
            {
                rtvHeader.setRtvNo(StringUtil.getInstance().convertDocNoWithNoLeading(dk02.getChildTextTrim("BELNR"), LEADING_ZERO));
            }
            
            rtvHolder.setRtvHeader(rtvHeader);
            rtvDocMsg.setData(rtvHolder);
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
    }

    @Override
    protected List<String> validateFile(DocMsg docMsg, byte[] input)
        throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}

