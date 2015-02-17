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

                            	//alert(registry.byId("types").value);
                    		    xhr.get({
                                    url: '<s:url value="/discrepancyReport/checkExcelData.action" />',
                                    form: dom.byId("searchForm"),
                                    load: function(data)
                                    {
                                        if (data=='"empty"')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2910" />'})).show();
                                        }
                                        else if (data=='"generating"') 
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2909" />'})).show();
                                        }
                                        else if (data=='"selectbuyer"')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2902" />'})).show();
                                        }
                                        else if (data=='"buyernotexist"')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2903" />'})).show();
                                        }
                                        else if (data=='"suppliernotexist"')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2904" />'})).show();
                                        }
                                        else
                                        {
                                        	submitForm("searchForm", "<s:url value="/discrepancyReport/printExcel.action" />");
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
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="matching.discrepancy.report.search.headerLabel" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
             <tr>
				<td colspan="3">
					<table width = "100%">
						<tr id="buyer">
							<td width="20%"><s:text name="matching.report.search.buyer" /></td>
							<td width="3px">:</td>
							<td>
								<s:select data-dojo-type="dijit/form/FilteringSelect" name="param.buyerOid" 
									list="buyers" listKey="buyerOid" listValue="buyerName" theme="simple"/>
							</td>
						</tr>
						<tr id="supplier">
							<td width="20%"><s:text name="matching.report.search.supplier" /></td>
							<td width="3px">:</td>
							<td>
								<s:select data-dojo-type="dijit/form/FilteringSelect" name="param.supplierOid" list="suppliers" 
                        			listKey="supplierOid" listValue="supplierName" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
							</td>
						</tr>
						<tr id="discrepancy">
							<td width="20%"><s:text name="matching.report.search.matchingDiscrepancy" /></td>
							<td width="3px">:</td>
							<td>
								<s:select data-dojo-type="dijit/form/FilteringSelect" name="param.discrepancyType" list="matchingDiscrepancy" 
                        			listKey="key" listValue="value" headerKey="all" headerValue="%{getText('select.all')}" theme="simple"/>
							</td>
						</tr>
					</table>
				</td>
        	</tr>
            </tbody>
        </table>
        </form>
    </div>
    
    
</body>
</html>
