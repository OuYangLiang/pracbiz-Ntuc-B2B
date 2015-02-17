<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dijit/registry",
                "dojo/on",
                "dijit/form/Select",
                "dojo/parser",
                "dojo/_base/xhr",
                "custom/InformationDialog",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    Select,
                    parser,
                    xhr,
                    InformationDialog
                    )
                {
                    parser.parse();
                
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');

                    
                    if (dom.byId("exportExcelBtn"))
                    {
                    	on(registry.byId("exportExcelBtn"),'click',
    						function()
                            {
                    			(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
                    		    xhr.get({
                                    url: '<s:url value="/readStatusReport/checkExcelData.action" />',
                                    form: dom.byId("searchForm"),
                                    load: function(data)
                                    {
                                        if (data=='"empty"')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2603" />'})).show();
                                        }
                                        else if (data=='"generating"') 
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2604" />'})).show();
                                        }
                                        else if (data=='"beforeHourNull"')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2602" />'})).show();
                                        }
                                        else if (data=='"beforeHourWrong"')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2601" />'})).show();
                                        }
                                        else
                                        {
                                        	submitForm("searchForm", "<s:url value="/readStatusReport/printExcel.action" />");
                                        }
                                    }
                                });
                            });
                    }

                });

    </script>
</head>
<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
	        <button data-dojo-type="dijit.form.Button" id="exportExcelBtn" ><s:text name="button.exportExcel" /></button>
        </td></tr></tbody></table>
    </div>

	<div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="report.search.headerLabel" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
             <tr id="buyer">
             		<td></td>
					<td>&nbsp;&nbsp;<s:text name="report.search.buyer" /></td>
					<td>:</td>
					<td>
						<s:select data-dojo-type="dijit/form/FilteringSelect" name="param.buyerOid" 
							list="buyers" listKey="buyerOid" listValue="buyerName" 
							headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
					</td>
			 </tr>
	         <tr>
                    <td></td>
                	<td>&nbsp;&nbsp;<s:text name="report.search.supplier" /></td>
                    <td>:</td>
                    <td>
                       <s:textfield name="param.supplierCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple"/>
                    </td>
             </tr>
             <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="report.search.doctype" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.msgType" list="docType" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
             </tr>
             <tr>
                 <td><span class="required">*</span></td>
                 <td>&nbsp;&nbsp;<s:text name="report.search.beforehour" /></td>
                 <td>:</td>
                 <td>
                     <s:textfield id="beforeHour" name="param.beforeHour" data-dojo-props="required: true" data-dojo-type="dijit/form/ValidationTextBox" 
                            maxlength="4" theme="simple"/>(hours)
                 </td>
             </tr>
            </tbody>
        </table>
        </form>
    </div>
    
    
</body>
</html>
