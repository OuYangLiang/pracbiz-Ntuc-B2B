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
                                        url: '<s:url value="/missingDocsReport/checkExcelData.action" />',
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
                                            else if (data != null && data.substring(0,7)=='maxDay,')
                                            {
                                            	(new InformationDialog({message: 'The count of days you select to export excel cannot be more than ' + data.split(",")[1] + '.'})).show();
                                            }
                                            else
                                            {
                                            	submitForm("searchForm", '<s:url value="/missingDocsReport/printExcel.action" />');
                                            }
                                        }
                                    });
                            });
                    }

                });

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
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="missing.report.search.headerLabel" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
            <tr>
            	<td></td>
				<td><s:text name="matching.report.search.msgtype" /></td>
				<td>:</td>
				<td>
					<s:select name="param.msgType" data-dojo-type="dijit/form/FilteringSelect" list="msgTypes" 
              				listKey="key" listValue="value" theme="simple"/>
				</td>
			</tr>
             <tr id="buyer">
             		<td></td>
					<td><s:text name="report.search.buyer" /></td>
					<td>:</td>
					<td>
						<s:select data-dojo-type="dijit/form/FilteringSelect" name="buyerOid" 
							list="buyers" listKey="buyerOid" listValue="buyerName" 
							theme="simple"/>
					</td>
			 </tr>
			 <tr>
			 		<td></td>
                	<td><s:text name="report.search.supplier" /></td>
                    <td>:</td>
                    <td>
                       <s:textfield name="param.supplierCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple"/>
                    </td>
             </tr>
			 <tr id="date">
			 		<td></td>
					<td  id="dateText"><s:text name="missingGrn.report.search.po.export.date" /></td>
					<td >:</td>
					<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="begin" name="begin" 
		                	onkeydown="javascript:document.getElementById('begin').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                	value='<s:date name="begin" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearBegin" type="button" onclick="clearButton('begin');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="end" name="end" 
	                        onkeydown="javascript:document.getElementById('end').blur();" 
	                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                        value='<s:date name="end" format="yyyy-MM-dd"/>'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearEnd" type="button" onclick="clearButton('end');"><s:text name='button.clear' /></button>
                	</td>
			 </tr>
            </tbody>
        </table>
        </form>
    </div>
    
    
</body>
</html>
