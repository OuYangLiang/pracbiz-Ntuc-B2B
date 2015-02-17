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
                "dijit/form/FilteringSelect",
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
                    FilteringSelect,
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
                                    url: '<s:url value="/trackingReport/checkExcelData.action" />',
                                    handleAs: "json",
                                    form: dom.byId("searchForm"),
                                    load: function(data)
                                    {
                                        if (data=='empty')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2910" />'})).show();
                                        }
                                        else if (data=='generating') 
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2909" />'})).show();
                                        }
                                        else if (data=='selecttype')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2901" />'})).show();
                                        }
                                        else if (data=='selectbuyer')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2902" />'})).show();
                                        }
                                        else if (data=='buyernotexist')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2903" />'})).show();
                                        }
                                        else if (data=='suppliernotexist')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2904" />'})).show();
                                        }
                                        else if (data=='datanull')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2905" />'})).show();
                                        }
                                        else if (data=='tobefore')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2906" />'})).show();
                                        }
                                        else if (data=='morethandaysnull')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2908" />'})).show();
                                        }
                                        else if (data=='morethandayswrong')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2907" />'})).show();
                                        }
                                        else if (data=='datanull')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2905" />'})).show();
                                        }
                                        else if (data=='tobefore')
                                        {
                                        	(new InformationDialog({message: '<s:text name="B2BPC2906" />'})).show();
                                        }
                                        else if (data != null && data.substring(0,7)=='maxDay,')
                                        {
                                        	(new InformationDialog({message: 'The count of days you select to export excel cannot be more than ' + data.split(",")[1] + '.'})).show();
                                        }
                                        else
                                        {
                                        	submitForm("searchForm", "<s:url value="/trackingReport/printExcel.action" />");
                                        }
                                    }
                                });
                            });
                    }

                    selectMsg = function(src)
            		{
            			var value = src.value;
            			hideAll();
            			if ("matching" == value)
            			{
            				dijit.byId('types').options.length = 0;
            				var types = dijit.byId('types');

            				var option = new Option("please Select", "");
            				types.addOption(option);

            				<s:if test="#session.permitUrl.contains('/trackingReport/matchingResolution.action')" >
            					var option1 = new Option("Resolution Report", "resolution");
            					types.addOption(option1);
            				</s:if>
            				<s:if test="#session.permitUrl.contains('/trackingReport/matchingOutstanding.action')" >
            					var option2 = new Option("Outstanding Resolution Report", "outstanding");
            					types.addOption(option2);
            				</s:if>
            				hideAll();
            			}
            			else if ("dn" == value)
            			{
            				dijit.byId("types").options.length=0;
            				var types = dijit.byId("types");

            				var option = new Option("please Select", "");
            				types.addOption(option);

            				<s:if test="#session.permitUrl.contains('/trackingReport/dnResolution.action')" >
            					var option1 = new Option("Resolution Report", "resolution");
            					types.addOption(option1);
            				</s:if>
            				<s:if test="#session.permitUrl.contains('/trackingReport/dnOutstanding.action')" >
            					var option2 = new Option("Outstanding Resolution Report", "outstanding");
            					types.addOption(option2);
            				</s:if>
            				hideAll();
            			}
            		}

                });
        
        function hideAll()
		{
			document.getElementById("buyer").style.display = 'none';
			document.getElementById("supplierCodeTr").style.display = 'none';
			document.getElementById("supplierNameTr").style.display = 'none';
			document.getElementById("days").style.display = 'none';
			document.getElementById("reportDateTr").style.display = 'none';
			document.getElementById("supplierCode").value = '';
			document.getElementById("supplierName").value = '';
			/* document.getElementById("param.reportDateFrom").value = '';
			document.getElementById("param.reportDateTo").value = ''; */
		}


		function selectReport(src)
		{
			var registry = require("dijit/registry");
			var value = src.value;
			hideAll();
			var msgType = registry.byId("msgTypes").value;
			if ("matching" == msgType)
			{
				if (value=="resolution")
				{
					document.getElementById("buyer").style.display = '';
					document.getElementById("supplierCodeTr").style.display = '';
					document.getElementById("supplierNameTr").style.display = '';
					document.getElementById("reportDateTr").style.display = '';
				}
				else if (value=="outstanding")
				{
					document.getElementById("buyer").style.display = '';
					document.getElementById("supplierCodeTr").style.display = '';
					document.getElementById("supplierNameTr").style.display = '';
					document.getElementById("days").style.display = '';
					document.getElementById("moreThanDays").value = '0';
				}
			}
			else if ("dn" == msgType)
			{
				if (value=="resolution")
				{
					document.getElementById("buyer").style.display = '';
					document.getElementById("supplierCodeTr").style.display = '';
					document.getElementById("supplierNameTr").style.display = '';
					document.getElementById("reportDateTr").style.display = '';
				}
				else if (value=="outstanding")
				{
					document.getElementById("buyer").style.display = '';
					document.getElementById("supplierCodeTr").style.display = '';
					document.getElementById("supplierNameTr").style.display = '';
					document.getElementById("days").style.display = '';
					document.getElementById("moreThanDays").value = '0';
				}
			}
			
			//document.getElementById(type).style.display = '';
		}

		function clearButton(src)
        {
            var registry = require("dijit/registry");
            registry.byId(src).attr("aria-valuenow", null);
    		registry.byId(src).attr("value", null);
        }

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
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="matching.tracking.report.search.headerLabel" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
            	<tr>
             		<td colspan="3">
						<table width = "100%">
							<tr>
								<td width="20%"><s:text name="matching.report.search.msgtype" /></td>
        						<td width="3px">:</td>
        						<td>
        							<s:select id="msgTypes" onchange="selectMsg(this);" data-dojo-type="dijit/form/Select" name="param.msgType" list="msgType" 
                        				listKey="key" listValue="value" theme="simple"/>
        						</td>
							</tr>
						</table>
					</td>
        		</tr>
            	<tr>
             		<td colspan="3">
						<table width = "100%">
							<tr>
								<td width="20%"><s:text name="matching.report.search.reporttype" /></td>
        						<td width="3px">:</td>
        						<td>
        							<s:select id="types" onchange="selectReport(this);" data-dojo-type="dijit/form/Select" name="param.type" list="reportType" 
                        				listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.select')}" theme="simple"/>
        						</td>
							</tr>
						</table>
					</td>
        		</tr>
             <tr>
				<td colspan="3">
					<table width = "100%">
						<tr id="buyer" style="display:none">
							<td width="20%"><s:text name="matching.report.search.buyer" /></td>
							<td width="3px">:</td>
							<td>
								<s:select data-dojo-type="dijit/form/FilteringSelect" name="param.buyerOid" 
									list="buyers" listKey="buyerOid" listValue="buyerName" theme="simple"/>
							</td>
						</tr>
						<tr id="supplierCodeTr" style="display:none">
							<td width="20%"><s:text name="matching.report.search.supplierCode" /></td>
							<td width="3px">:</td>
							<td>
								<s:textfield id="supplierCode" name="param.supplierCode"  data-dojo-type="dijit/form/ValidationTextBox" 
                           			theme="simple"/>
                           		<button  data-dojo-type="dijit.form.Button" id="clearSupplierCode" type="button" onclick="clearButton('supplierCode');"><s:text name='button.clear' /></button>
							</td>
						</tr>
						<tr id="supplierNameTr" style="display:none">
							<td width="20%"><s:text name="matching.report.search.supplierName" /></td>
							<td width="3px">:</td>
							<td>
								<s:textfield id="supplierName" name="param.supplierName" data-dojo-type="dijit/form/ValidationTextBox" 
                           			theme="simple"/>
                           		<button  data-dojo-type="dijit.form.Button" id="clearSupplierName" type="button" onclick="clearButton('supplierName');"><s:text name='button.clear' /></button>
							</td>
						</tr>
						<tr id="reportDateTr" style="display:none">
							<td width="20%"><s:text name="matching.report.search.days.reportDate" /></td>
							<td width="3px">:</td>
							<td>
								<label for="Dfrom1"><s:text name="Value.from" /></label>
								<input type="text" id="param.reportDateFrom" name="param.reportDateFrom" 
		                            onkeydown="javascript:document.getElementById('param.reportDateFrom').blur();" 
		                            data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                            value='<s:date name="param.reportDateFrom" format="yyyy-MM-dd" />'/>
		                        <label for="Dto1"><s:text name="Value.to" /></label>
		                        <input type="text" id="param.reportDateTo" name="param.reportDateTo" 
		                            onkeydown="javascript:document.getElementById('param.reportDateTo').blur();" 
		                            data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                            value='<s:date name="param.reportDateTo" format="yyyy-MM-dd" />'/>
							</td>
						</tr>
						<tr id="days" style="display:none">
							<td width="20%"><s:text name="matching.report.search.days.span" /></td>
							<td width="3px">:</td>
							<td>
								<label>More than</label>
								<s:textfield id="moreThanDays" name="param.moreThanDays" data-dojo-props="required: true" data-dojo-type="dijit/form/ValidationTextBox" 
                           			maxlength="4" theme="simple"/>(Days)
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
