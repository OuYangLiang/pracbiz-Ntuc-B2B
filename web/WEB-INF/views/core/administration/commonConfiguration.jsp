<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <style type="text/css">
        @import "<s:url value='/js/dojo-root/dojox/grid/resources/Grid.css' />";
        @import "<s:url value='/js/dojo-root/dojox/grid/resources/claroGrid.css' />";
        @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/%{#session.layoutTheme}/EnhancedGrid.css' />";
        @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css' />";
        @import "<s:url value='/js/dojo-root/dijit/themes/claro/claro.css' />";
        @import "<s:url value='/js/dojo-root/dojo/resources/dojo.css' />";
       
        @import "<s:url value='/css/claro/overlay.css' />";
        @import "<s:url value='/css/claro/common.css' />";
        @import "<s:url value='/js/dojo-root/dojox/form/resources/CheckedMultiSelect.css' />";
        
    </style>
     <script type="text/javascript" src="<s:url value='/js/dojo-root/dojo/dojo.js' />"
        data-dojo-config="parseOnLoad: false, async: true, isDebug: true"></script>
     <script type="text/javascript">
     require(
             [
             "dojo/dom",
             "dijit/registry",
             "dojo/string",
             "dijit/layout/ContentPane", 
             "dojo/parser",
             "dojo/_base/xhr",
             "custom/InformationDialog",
             "dijit/TitlePane",
             "dijit/form/CheckBox",
             "dijit/form/TextBox",
             "dojo/domReady!"
             ], 
             function(
                 dom,
                 registry,
                 string,
                 ContentPane,
                 parser,
                 xhr,
                 InformationDialog,
                 TitlePane,
                 CheckBox,
                 TextBox
                 )
             {
            parser.parse();
            
            
            save = function(){
                var pwdExpPeri = string.trim(dom.byId("pwdExpPeri.numValue").value);
                var emailExpPeri = string.trim(dom.byId("emailExpPeri.numValue").value);
                var maxAttLogin = string.trim(dom.byId("maxAttLogin.numValue").value);
                var helpDeskNo = string.trim(dom.byId("helpDeskNo.stringValue").value);
                var helpDeskEmail = string.trim(dom.byId("helpDeskEmail.stringValue").value);
                var reqInterval = string.trim(dom.byId("reqInterval.numValue").value);
                var autoLoginOut = string.trim(dom.byId("autoLoginOut.numValue").value);
                var logFileName = string.trim(dom.byId("logFileName.stringValue").value);
                var logFilePath = string.trim(dom.byId("logFilePath.stringValue").value);
                var repeatedLogin = dom.byId("repeatedLogin.valid").checked;
                var makerChecker = dom.byId("makerChecker.valid").checked;
                var maxUserAmountForSupplier = string.trim(dom.byId("maxUserAmountForSupplier.stringValue").value);
                var defaultPageSize = string.trim(dom.byId("defaultPageSize.stringValue").value);
                var pageSizes = string.trim(dom.byId("pageSizes.stringValue").value);
                var regDeskNo = new RegExp('^[(]?(\\d+)([/\\s-()]?(\\d+)[)]?)*$');
                var regEmail = new RegExp('^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$');
                var documentWindowForBuyer = string.trim(dom.byId("documentWindowForBuyer.numValue").value);
                var documentWindowForSupplier = string.trim(dom.byId("documentWindowForSupplier.numValue").value);
                var maxPdfCount = string.trim(dom.byId("maxPdfCount.numValue").value);
                var maxExcelCount = string.trim(dom.byId("maxExcelCount.numValue").value);
                var maxSummaryExcelCount = string.trim(dom.byId("maxSummaryExcelCount.numValue").value);
                var maxDayOfReport = string.trim(dom.byId("maxDayOfReport.numValue").value);
                var currentGst = string.trim(dom.byId("currentGst.stringValue").value);
                var newGst = string.trim(dom.byId("newGst.stringValue").value);
                var newGstFromDate = string.trim(dom.byId("newGstFromDate.dateValue").value);
                var fileSizeLimit = string.trim(dom.byId("fileSizeLimit.numValue").value);
                

                var positiveInt = new RegExp("^[0-9]*[1-9][0-9]*$");
                
                if(pwdExpPeri.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0101'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(pwdExpPeri) || parseFloat(pwdExpPeri)<1 || parseFloat(pwdExpPeri)>9999 || !positiveInt.test(string.trim(pwdExpPeri)))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0102'/>"});
                    infoDialog.show();
                    return;
                }
                if(emailExpPeri.length ==0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0103'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(emailExpPeri) || parseFloat(emailExpPeri)<1 || parseFloat(emailExpPeri)>9999 || !positiveInt.test(string.trim(emailExpPeri)))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0104'/>"});
                    infoDialog.show();
                    return;
                }
                if(maxAttLogin.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0105'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(maxAttLogin) || parseFloat(maxAttLogin)<3 || parseFloat(maxAttLogin)>9999 || !positiveInt.test(string.trim(maxAttLogin)))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0106'/>"});
                    infoDialog.show();
                    return;
                }
                if(helpDeskNo.length > 0 && !regDeskNo.test(helpDeskNo)) 
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0107'/>"});
                    infoDialog.show();
                    return;
                }
                if(helpDeskEmail.length > 0 && !regEmail.test(helpDeskEmail)) 
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0108'/>"});
                    infoDialog.show();
                    return;
                }
                if(reqInterval.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0135'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(reqInterval) || parseFloat(reqInterval)<1  || parseFloat(reqInterval)>60 || !positiveInt.test(string.trim(reqInterval)))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0136'/>"});
                    infoDialog.show();
                    return;
                }
                if(autoLoginOut.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0137'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(autoLoginOut) || parseFloat(autoLoginOut)<1 || parseFloat(autoLoginOut)>9999 || !positiveInt.test(string.trim(autoLoginOut)))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0138'/>"});
                    infoDialog.show();
                    return;
                }
                if(logFileName.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0153'/>"});
                    infoDialog.show();
                    return;
                }
                var logFileNames = logFileName.split(",");
                var fileReg = new RegExp("^(\\S)+[\.](\\S)+$");
                var resultLogFileName  = "";
                for(var i = 0; i < logFileNames.length; i++)
                {
                    if(!fileReg.test(string.trim(logFileNames[i])))
                    {
                        var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0155'/>"});
                        infoDialog.show();
                        return;
                    }
                    resultLogFileName += string.trim(logFileNames[i]);
                    if (i < logFileNames.length-1)
                    {
                    	resultLogFileName += ",";
                    }
                }
                if (string.trim(maxUserAmountForSupplier).length > 0 && !positiveInt.test(string.trim(maxUserAmountForSupplier)))
                {
                	var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0172'/>"});
                    infoDialog.show();
                    return;
                }
                if (string.trim(pageSizes).length == 0)
                {
                	var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0170'/>"});
                    infoDialog.show();
                    return;
                }
                var pageSizeArray = pageSizes.split(",");
                var includeFlag = false;// judge if the defaultPageSize is one of the pageSizes.
                for (var i = 0;i < pageSizeArray.length ; i++)
                {
                	if(string.trim(pageSizeArray[i]).length == 0 || !positiveInt.test(string.trim(pageSizeArray[i])))
                	{
                		var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0171'/>"});
                        infoDialog.show();
                        return;
                	}
                	if (string.trim(pageSizeArray[i]) == string.trim(defaultPageSize))
                	{
                    	includeFlag = true;
                	}
                }
                if (string.trim(defaultPageSize).length == 0 || !includeFlag)
                {
                	var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0173'/>"});
                    infoDialog.show();
                    return;
                }
                if(documentWindowForBuyer.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0174'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(documentWindowForBuyer) || parseFloat(documentWindowForBuyer)<1 || parseFloat(documentWindowForBuyer)>7 || !positiveInt.test(string.trim(documentWindowForBuyer)))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0175'/>"});
                    infoDialog.show();
                    return;
                }
                if(maxPdfCount.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0193'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(maxPdfCount) || parseFloat(maxPdfCount)<1 || parseFloat(maxPdfCount)>10000 || !positiveInt.test(string.trim(maxPdfCount)))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0194'/>"});
                    infoDialog.show();
                    return;
                }
                if(maxExcelCount.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0195'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(maxExcelCount) || parseFloat(maxExcelCount)<1 || parseFloat(maxExcelCount)>10000 || !positiveInt.test(string.trim(maxExcelCount)))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0196'/>"});
                    infoDialog.show();
                    return;
                }
                if(maxSummaryExcelCount.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0197'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(maxSummaryExcelCount) || parseFloat(maxSummaryExcelCount)<1 || parseFloat(maxSummaryExcelCount)>10000 || !positiveInt.test(string.trim(maxSummaryExcelCount)))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0198'/>"});
                    infoDialog.show();
                    return;
                }
                if(maxDayOfReport.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0199'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(maxDayOfReport) || parseFloat(maxDayOfReport)<1 || parseFloat(maxDayOfReport)>9999 || !positiveInt.test(string.trim(maxDayOfReport)))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0200'/>"});
                    infoDialog.show();
                    return;
                }
                if(currentGst.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0190'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(currentGst) || parseFloat(currentGst)<0 || parseFloat(currentGst)>100)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0191'/>"});
                    infoDialog.show();
                    return;
                }
                if(string.trim(newGst).length > 0 && (isNaN(newGst) || parseFloat(newGst)<0 || parseFloat(newGst)>100))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0192'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(fileSizeLimit) || parseFloat(fileSizeLimit)<0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0201'/>"});
                    infoDialog.show();
                    return;
                }
                
                var csrfToken = dom.byId("csrfToken").value;
                xhr.get({
                      url: '<s:url value="/admin/saveCommonFig.action" />',
                      content: {"pwdExpPeri.numValue" : pwdExpPeri,
                            "emailExpPeri.numValue" : emailExpPeri,
                            "maxAttLogin.numValue" : maxAttLogin,
                            "helpDeskNo.stringValue" : helpDeskNo,
                            "helpDeskEmail.stringValue" : helpDeskEmail,
                            "reqInterval.numValue" : reqInterval,
                            "autoLoginOut.numValue" : autoLoginOut,
                            "logFileName.stringValue" : resultLogFileName,
                            "logFilePath.stringValue" : logFilePath,
                            "repeatedLogin.valid" : repeatedLogin,
                            "makerChecker.valid" : makerChecker,
                            "maxUserAmountForSupplier.stringValue" : maxUserAmountForSupplier,
                            "defaultPageSize.stringValue" : defaultPageSize,
                            "pageSizes.stringValue" : pageSizes,
                            "documentWindowForBuyer.numValue" : documentWindowForBuyer,
                            "documentWindowForSupplier.numValue" : documentWindowForSupplier,
                            "maxPdfCount.numValue" : maxPdfCount,
                            "maxExcelCount.numValue" : maxExcelCount,
                            "maxSummaryExcelCount.numValue" : maxSummaryExcelCount,
                            "maxDayOfReport.numValue" : maxDayOfReport,
                            "currentGst.stringValue" : currentGst,
                            "newGst.stringValue" : newGst,
                            "newGstFromDate.dateValue" : newGstFromDate,
                            "fileSizeLimit.numValue" : fileSizeLimit,
                            "csrfToken" : csrfToken
                            },
                      handleAs: "json",
                      load: function(jsonData)
                      {
                            var infoDialog = new InformationDialog({message: jsonData});
                            infoDialog.show();
                            reset();
                      }
                  });
            };
	        reset = function()
            {
                xhr.get({
                    url: '<s:url value="/admin/resetCommonFig.action" />',
                    handleAs: "json",
                    load: function(jsonData)
                    {
                    	registry.byId("pwdExpPeri.numValue").set('value',jsonData.pwdExpPeri.numValue);
                    	registry.byId("emailExpPeri.numValue").set('value',jsonData.emailExpPeri.numValue);
                    	registry.byId("maxAttLogin.numValue").set('value',jsonData.maxAttLogin.numValue);
                    	registry.byId("helpDeskNo.stringValue").set('value',jsonData.helpDeskNo.stringValue);
                    	registry.byId("helpDeskEmail.stringValue").set('value',jsonData.helpDeskEmail.stringValue);
                    	registry.byId("reqInterval.numValue").set('value',jsonData.reqInterval.numValue);
                    	registry.byId("autoLoginOut.numValue").set('value',jsonData.autoLoginOut.numValue);
                    	registry.byId("logFileName.stringValue").set('value',jsonData.logFileName.stringValue);
                    	registry.byId("logFilePath.stringValue").set('value',jsonData.logFilePath.stringValue);
                        registry.byId("repeatedLogin.valid").set('checked',jsonData.repeatedLogin.valid);
                        registry.byId("makerChecker.valid").set('checked',jsonData.makerChecker.valid);
                    	registry.byId("maxUserAmountForSupplier.stringValue").set('value',jsonData.maxUserAmountForSupplier.stringValue);
                    	registry.byId("defaultPageSize.stringValue").set('value',jsonData.defaultPageSize.stringValue);
                    	registry.byId("pageSizes.stringValue").set('value',jsonData.pageSizes.stringValue);
                    	registry.byId("documentWindowForBuyer.numValue").set('value',jsonData.documentWindowForBuyer.numValue);
                    	registry.byId("documentWindowForSupplier.numValue").set('value',jsonData.documentWindowForSupplier.numValue);
                    	registry.byId("currentGst.stringValue").set('value',jsonData.currentGst.stringValue);
                    	registry.byId("newGst.stringValue").set('value',jsonData.newGst.stringValue);
                    	registry.byId("newGstFromDate.dateValue").set('value',jsonData.newGstFromDate.dateValue);
                    	registry.byId("maxPdfCount.numValue").set('value',jsonData.maxPdfCount.numValue);
                    	registry.byId("maxExcelCount.numValue").set('value',jsonData.maxExcelCount.numValue);
                    	registry.byId("maxSummaryExcelCount.numValue").set('value',jsonData.maxSummaryExcelCount.numValue);
                    	registry.byId("maxDayOfReport.numValue").set('value',jsonData.maxDayOfReport.numValue);
                    	registry.byId("fileSizeLimit.numValue").set('value',jsonData.fileSizeLimit.numValue);
                    }
                });
            };
            
            clearButton = function(src)
            {
                registry.byId(src).attr("aria-valuenow", null);
                registry.byId(src).attr("value", null);
            }
       }); 


     </script>

</head>
<body class="claro">
    <table class="btnContainer" style="width:97%" >
         <tbody><tr><td>
              <s:if test="#session.permitUrl.contains('/admin/saveCommonFig.action')" >
	              <button data-dojo-type="dijit.form.Button" onclick="save();" ><s:text name="button.save" /></button>
	              <button data-dojo-type="dijit.form.Button" onclick="reset();" ><s:text name="button.reset" /></button>
              </s:if>
          </td></tr></tbody>
    </table>
    <div>
    	<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    </div>
    <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
        <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.commonConfig.summary.security"/>', width:275" style="width:99%">
            <table class="commtable">
                <tr>
                   <td><span class="required">*</span></td>
                   <td width="50%"><s:text name="admin.commonConfig.summary.pwdExpiredPeriod"/></td>
                   <td>:</td>
                   <td><s:textfield id="pwdExpPeri.numValue" name="pwdExpPeri.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="4"/>(Days)</td>
                </tr>
                
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.maxAttemptLogin"/></td>
                   <td>:</td>
                   <td><s:textfield id="maxAttLogin.numValue" name="maxAttLogin.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="4"/></td>
                </tr>
                
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.autoLoginOut"/></td>
                   <td>:</td>
                   <td><s:textfield id="autoLoginOut.numValue" name="autoLoginOut.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="4"/>(Minutes)</td>
                </tr>
                
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.commonConfig.summary.duplicateLogin"/></td>
                   <td>:</td>
                   <td><s:checkbox id="repeatedLogin.valid" name="repeatedLogin.valid" theme="simple" data-dojo-type="dijit.form.CheckBox"/></td>
                </tr>
            </table>
        </div>
        <div class="space"></div>
        <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.commonConfig.summary.operation"/>', width:275" style="width:99%">
            <table class="commtable">
                <tr>
                   <td><span class="required"></span></td>
                   <td width="50%"><s:text name="admin.commonConfig.summary.maxUserAmountForSupplier"/></td>
                   <td>:</td>
                   <td><s:textfield id="maxUserAmountForSupplier.stringValue" name="maxUserAmountForSupplier.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="5"/></td>
                </tr>
                
                <tr>
                    <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.emailExpiredPeriod"/></td>
                   <td>:</td>
                   <td><s:textfield id="emailExpPeri.numValue" name="emailExpPeri.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="4"/>(Days)</td>
                </tr>
                
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.reqInterval"/></td>
                   <td>:</td>
                   <td><s:textfield id="reqInterval.numValue" name="reqInterval.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="4"/>(Minutes)</td>
                </tr>
            </table>
        </div>
        <div class="space"></div>
        <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.commonConfig.summary.userInterface"/>', width:275" style="width:99%">
            <table class="commtable">
                <tr>
                   <td><span class="required">*</span></td>
                   <td width="50%"><s:text name="admin.commonConfig.summary.pageSizes"/></td>
                   <td>:</td>
                   <td><s:textfield id="pageSizes.stringValue" name="pageSizes.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/></td>
                </tr>
                
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.defaultPageSize"/></td>
                   <td>:</td>
                   <td><s:textfield id="defaultPageSize.stringValue" name="defaultPageSize.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/></td>
                </tr>
            </table>
        </div>
        <div class="space"></div>
		<div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.commonConfig.summary"/>', width:275" style="width:99%">
			<table class="commtable">
				<tr>
				   <td><span class="required"></span></td>
                   <td width="50%"><s:text name="admin.commonConfig.summary.helpTele"/></td>
                   <td>:</td>
                   <td><s:textfield id="helpDeskNo.stringValue" name="helpDeskNo.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" maxlength="255"/></td>
                </tr>
                
				<tr>
				   <td><span class="required"></span></td>
                   <td><s:text name="admin.commonConfig.summary.helpEmail"/></td>
                   <td>:</td>
                   <td><s:textfield id="helpDeskEmail.stringValue" name="helpDeskEmail.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" maxlength="255"/></td>
                </tr>
                
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.file.path"/></td>
                   <td>:</td>
                   <td><s:textfield id="logFilePath.stringValue" name="logFilePath.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/></td>
                </tr>
                
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.file.name"/></td>
                   <td>:</td>
                   <td><s:textfield id="logFileName.stringValue" name="logFileName.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/></td>
                </tr>
                
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.documentWindow.buyer"/></td>
                   <td>:</td>
                   <td><s:textfield id="documentWindowForBuyer.numValue" name="documentWindowForBuyer.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/>(Days)</td>
                </tr>
                
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.documentWindow.supplier"/></td>
                   <td>:</td>
                   <td><s:textfield id="documentWindowForSupplier.numValue" name="documentWindowForSupplier.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/>(Days)</td>
                </tr>
                
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.maxCount.pdf"/></td>
                   <td>:</td>
                   <td><s:textfield id="maxPdfCount.numValue" name="maxPdfCount.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/></td>
                </tr>
                
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.maxCount.excel"/></td>
                   <td>:</td>
                   <td><s:textfield id="maxExcelCount.numValue" name="maxExcelCount.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/></td>
                </tr>
                
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.maxCount.summary.excel"/></td>
                   <td>:</td>
                   <td><s:textfield id="maxSummaryExcelCount.numValue" name="maxSummaryExcelCount.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/></td>
                </tr>
                
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.commonConfig.summary.maxDay.report"/></td>
                   <td>:</td>
                   <td><s:textfield id="maxDayOfReport.numValue" name="maxDayOfReport.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="4"/>(Days)</td>
                </tr>
                
                <tr style="display: none;">
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.commonConfig.summary.maker.checker"/></td>
                   <td>:</td>
                   <td><s:checkbox id="makerChecker.valid" name="makerChecker.valid" theme="simple" data-dojo-type="dijit.form.CheckBox"/></td>
                </tr>
            </table>
		</div>
        <div class="space"></div>
		<div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.commonConfig.summary.gstSetting"/>', width:275" style="width:99%">
			<table class="commtable">
				<tr>
				   <td><span class="required">*</span></td>
                   <td width="50%"><s:text name="admin.commonConfig.summary.currentGst"/></td>
                   <td>:</td>
                   <td><s:textfield id="currentGst.stringValue" name="currentGst.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" maxlength="5"/>%</td>
                </tr>
				<tr>
				   <td><span class="required"></span></td>
                   <td width="50%"><s:text name="admin.commonConfig.summary.newGst"/></td>
                   <td>:</td>
                   <td><s:textfield id="newGst.stringValue" name="newGst.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" maxlength="5"/>%</td>
                </tr>
				<tr>
				   <td><span class="required"></span></td>
                   <td width="50%"><s:text name="admin.commonConfig.summary.newGstFromDate"/></td>
                   <td>:</td>
                   <td>
                       <input type="text" id="newGstFromDate.dateValue" name="newGstFromDate.dateValue" 
                            onkeydown="javascript:document.getElementById('newGstFromDate.dateValue').blur();" 
                            data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                            value='<s:date name="newGstFromDate.dateValue" format="yyyy-MM-dd" />'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearNewGstFromDate" type="button" onclick="clearButton('newGstFromDate.dateValue');"><s:text name='button.clear' /></button>
                   </td>
                </tr>
            </table>
		</div>
		<div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.commonConfig.summary.fileSizeLimit"/>', width:275" style="width:99%">
			<table class="commtable">
				<tr>
				   <td><span class="required">&nbsp;</span></td>
                   <td width="50%"><s:text name="admin.commonConfig.summary.fileSizeLimit"/></td>
                   <td>:</td>
                   <td><s:textfield id="fileSizeLimit.numValue" name="fileSizeLimit.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" maxlength="5"/> (M)</td>
                </tr>
            </table>
		</div>
	</div>
</body>
</html>
