<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <style type="text/css">
    @import "<s:url value='/js/dojo-root/dojox/grid/resources/Grid.css' />";
    @import "<s:url value='/js/dojo-root/dojox/grid/resources/claroGrid.css' />";
    @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/%{#session.layoutTheme}/EnhancedGrid.css' />";
    @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css' />";
    </style>
        
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dijit/registry",
                "dijit/form/ValidationTextBox",
                "dijit/form/CheckBox",
                "dojo/on",
                "dojox/data/QueryReadStore",
                "dojox/grid/EnhancedGrid",
                "dojox/grid/enhanced/plugins/Pagination",
                "dojox/grid/enhanced/plugins/IndirectSelection",
                "dijit/form/Select",
                "dojo/parser",
                "dojo/_base/xhr",
                "custom/InformationDialog",
                "custom/ConfirmDialog",
                "custom/SelectAllConfirmDialog",
                "dojo/string",
                "dojo/_base/array",
                "dijit/Tooltip",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    ValidationTextBox,
                    CheckBox,
                    on,
                    QueryReadStore,
                    EnhancedGrid,
                    Pagination,
                    IndirectSelection,
                    Select,
                    parser,
                    xhr,
                    InformationDialog,
                    ConfirmDialog,
                    SelectAllConfirmDialog,
                    string,
                    array,
                    Tooltip
                    )
                {
                    parser.parse();
                
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');

                    
                    // init tooltips
                    loadPriceTooltip = function(src, actionBy, actionDate, actionRemarks)
		            {
		            		
		              	var value="";
		              	var actionBy = actionBy;
		                var actionByLabel = "Action By";

		                var actionDate = actionDate;
		                var actionDateLabel = "Action Date";

		                var actionRemarks = actionRemarks;
		                var actionRemarksLable = "Action Remarks";
		
		
			            if (actionBy != null && actionBy != "")
			            {
							value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
				            value +=actionByLabel +'</label><br/>';
			                var content = "";
			            	var length = 0;
			            	for (var i=0;i < actionBy.length ; i++)
			          		{
			            		length ++;
			            		if (actionBy.charAt(i)=='\n')
			            		{
			            			content += '<br/>';
			            			length = 0;
			            		}
			            		else
			            		{
			            			content += actionBy.charAt(i);
			            			
			            			if (length > 50)
			            			{
			            				content += '<br/>';
			            				length = 0;
			            			}
			            		}
			          		}
			            	value += content + '<br/>';
			            }

			            if (actionDate != null && actionDate != "")
			            {
				            value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
				            value +=actionDateLabel +'</label><br/>';
			                var content = "";
			            	var length = 0;
			            	for (var i=0;i < actionDate.length ; i++)
			          		{
			            		length ++;
			            		if (actionDate.charAt(i)=='\n')
			            		{
			            			content += '<br/>';
			            			length = 0;
			            		}
			            		else
			            		{
			            			content += actionDate.charAt(i);
			            			
			            			if (length > 50)
			            			{
			            				content += '<br/>';
			            				length = 0;
			            			}
			            		}
			          		}
			            	value += content + '<br/>';
			            }

			            if (actionRemarks != null && actionRemarks != "")
			            {
				            value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
				            value +=actionRemarksLable +'</label><br/>';
			                var content = "";
			            	var length = 0;
			            	for (var i=0;i < actionRemarks.length ; i++)
			          		{
			            		length ++;
			            		if (actionRemarks.charAt(i)=='\n')
			            		{
			            			content += '<br/>';
			            			length = 0;
			            		}
			            		else
			            		{
			            			content += actionRemarks.charAt(i);
			            			
			            			if (length > 50)
			            			{
			            				content += '<br/>';
			            				length = 0;
			            			}
			            		}
			          		}
			            	value += content + '<br/>';
			            }


			            Tooltip.defaultPosition=["after-centered","after"];
			            if (value != null && value != "")
			            {
			            	Tooltip.show(value,src);
			            }
		                return;
		            }
		            // remove tooltip
		            closePriceTooltip = function(src)
		            {
		            	Tooltip.hide(src);
		            }

		         	// init tooltips
                    loadQtyTooltip = function(src, actionBy, actionDate, actionRemarks)
		            {
		            		
		              	var value="";
		              	var actionBy = actionBy;
		                var actionByLabel = "Action By";

		                var actionDate = actionDate;
		                var actionDateLabel = "Action Date";

		                var actionRemarks = actionRemarks;
		                var actionRemarksLable = "Action Remarks";
		
		
			            if (actionBy != null && actionBy != "")
			            {
							value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
				            value +=actionByLabel +'</label><br/>';
			                var content = "";
			            	var length = 0;
			            	for (var i=0;i < actionBy.length ; i++)
			          		{
			            		length ++;
			            		if (actionBy.charAt(i)=='\n')
			            		{
			            			content += '<br/>';
			            			length = 0;
			            		}
			            		else
			            		{
			            			content += actionBy.charAt(i);
			            			
			            			if (length > 50)
			            			{
			            				content += '<br/>';
			            				length = 0;
			            			}
			            		}
			          		}
			            	value += content + '<br/>';
			            }

			            if (actionDate != null && actionDate != "")
			            {
				            value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
				            value +=actionDateLabel +'</label><br/>';
			                var content = "";
			            	var length = 0;
			            	for (var i=0;i < actionDate.length ; i++)
			          		{
			            		length ++;
			            		if (actionDate.charAt(i)=='\n')
			            		{
			            			content += '<br/>';
			            			length = 0;
			            		}
			            		else
			            		{
			            			content += actionDate.charAt(i);
			            			
			            			if (length > 50)
			            			{
			            				content += '<br/>';
			            				length = 0;
			            			}
			            		}
			          		}
			            	value += content + '<br/>';
			            }

			            if (actionRemarks != null && actionRemarks != "")
			            {
				            value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
				            value +=actionRemarksLable +'</label><br/>';
			                var content = "";
			            	var length = 0;
			            	for (var i=0;i < actionRemarks.length ; i++)
			          		{
			            		length ++;
			            		if (actionRemarks.charAt(i)=='\n')
			            		{
			            			content += '<br/>';
			            			length = 0;
			            		}
			            		else
			            		{
			            			content += actionRemarks.charAt(i);
			            			
			            			if (length > 50)
			            			{
			            				content += '<br/>';
			            				length = 0;
			            			}
			            		}
			          		}
			            	value += content + '<br/>';
			            }


			            Tooltip.defaultPosition=["before-centered","after"];
			            if (value != null && value != "")
			            {
			            	Tooltip.show(value,src);
			            }
		                return;
		            }
		            // remove tooltip
		            closeQtyTooltip = function(src)
		            {
		            	Tooltip.hide(src);
		            }
                    
                    on(registry.byId("backBtn"), 'click', 
                        function()
                        {
                    		changeToURL('<s:url value="/poInvGrnDnMatching/init.action?keepSp=Y" />');
                        }
                    );

                    if (dom.byId("acceptButton"))
                    {
                    	on(registry.byId("acceptButton"), 'click', 
                            function()
                            {
                            	var oid = <s:property value="param.matchingOid"/>;
                    			var csrfToken = document.getElementById("csrfToken").value;
	                    		xhr.get({
	                                url: '<s:url value="/poInvGrnDnMatching/checkAcceptOrReject.action" />',
	                                content: {"param.matchingOid": oid, "csrfToken":csrfToken},
	                                load: function(result)
	                                {
	                                	if (result=='"revised"')
	                                    {
	                                    	(new InformationDialog({message: '<s:text name="B2BPC2148" />'})).show();
	                                    }
	                                    else if (result=='"pending"')
	                				    {
	                                	    (new InformationDialog({message: '<s:text name="B2BPC2134" />'})).show();
	                				    }
	                                	else if (result=='"matched"')
	                				    {
	                                	    (new InformationDialog({message: '<s:text name="B2BPC2135" />'})).show();
	                				    }
	                                	else if (result == '"closed"')
	                                    {
	                                    	(new InformationDialog({message: '<s:text name="B2BPC2128" />'})).show();
	                                    }
	                                    else if(result == '"insufficientInv"')
	                                    {
	                                    	(new InformationDialog({message: '<s:text name="B2BPC2113" />'})).show();
	                                    }
	                                    else if(result == '"outdated"')
	                                    {
	                                    	(new InformationDialog({message: '<s:text name="B2BPC2114" />'})).show();
	                                    }
	                                    else if(result == '"accepted"')
	                                    {
	                                    	(new InformationDialog({message: '<s:text name="B2BPC2110" />'})).show();
	                                    }
	                                    else if(result == '"rejected"')
	                                    {
	                                    	(new InformationDialog({message: '<s:text name="B2BPC2109" />'})).show();
	                                    }
	                                    else if(result == '"1"')
	                                    {
	                                    	accept(oid, "accept");
	                                    }
	                                }
	                            });
                            }
                        );
                    }

                    if (dom.byId("rejectButton"))
                    {
                    	on(registry.byId("rejectButton"), 'click', 
                            function()
                            {
                    			var oid = <s:property value="param.matchingOid"/>;
	                    		var csrfToken = document.getElementById("csrfToken").value;
	                    		xhr.get({
	                                url: '<s:url value="/poInvGrnDnMatching/checkAcceptOrReject.action" />',
	                                content: {"param.matchingOid":oid, "csrfToken":csrfToken},
	                                load: function(result)
	                                {
	                                	if (result=='"revised"')
	                                    {
	                                    	(new InformationDialog({message: '<s:text name="B2BPC2148" />'})).show();
	                                    }
	                                    else if (result=='"pending"')
	                				    {
	                                	    (new InformationDialog({message: '<s:text name="B2BPC2134" />'})).show();
	                				    }
	                                	else if (result=='"matched"')
	                				    {
	                                	    (new InformationDialog({message: '<s:text name="B2BPC2135" />'})).show();
	                				    }
	                                	else if (result == '"closed"')
	                                    {
	                                    	(new InformationDialog({message: '<s:text name="B2BPC2128" />'})).show();
	                                    }
	                                    else if(result == '"insufficientInv"')
	                                    {
	                                    	(new InformationDialog({message: '<s:text name="B2BPC2113" />'})).show();
	                                    }
	                                    else if(result == '"outdated"')
	                                    {
	                                    	(new InformationDialog({message: '<s:text name="B2BPC2114" />'})).show();
	                                    }
	                                    else if(result == '"accepted"')
	                                    {
	                                    	(new InformationDialog({message: '<s:text name="B2BPC2110" />'})).show();
	                                    }
	                                    else if(result == '"rejected"')
	                                    {
	                                    	(new InformationDialog({message: '<s:text name="B2BPC2109" />'})).show();
	                                    }
	                                    else if(result == '"1"')
	                                    {
	                                    	reject(oid, "reject");
	                                    }
	                                }
	                            });
                            }
                        );
                    }
                    hideDiv = function(src)
                    {
                        registry.byId(src).hide();
                    };
                });

        function accept(OperationMatchingOid, supplierStatus)
        {
        	matchingOid = OperationMatchingOid;
        	supplierStatusValue = supplierStatus;
            var ConfirmDialog = require("custom/ConfirmDialog");
            var xhr = require("dojo/_base/xhr");
            var registry = require("dijit/registry");
        	var confirmDialog = new ConfirmDialog({
                message: '<s:text name="B2BPC2111" />',
                yesBtnPressed: function(){
                	document.getElementById("supplierRemarksText").value="";
                	registry.byId("supplierRemarks").show();
                	
                }
            });
        	confirmDialog.show();
        }

        function reject(OperationMatchingOid, supplierStatus)
        {
        	matchingOid = OperationMatchingOid;
        	supplierStatusValue = supplierStatus;
            var ConfirmDialog = require("custom/ConfirmDialog");
            var xhr = require("dojo/_base/xhr");
            var registry = require("dijit/registry");
        	var confirmDialog = new ConfirmDialog({
                message: '<s:text name="B2BPC2112" />',
                yesBtnPressed: function(){
                	document.getElementById("supplierRemarksText").value="";
                	registry.byId("supplierRemarks").show();
                	
                }
            });
        	confirmDialog.show();
        }

        function saveSupplierStatus()
        {
        	var ConfirmDialog = require("custom/ConfirmDialog");
        	var InformationDialog = require("custom/InformationDialog");
            var xhr = require("dojo/_base/xhr");
            var remarks = dijit.byId("supplierRemarksText").value;
            if (remarks.length == 0 || remarks.length > 255)
            {
            	var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2106" />'});
                infoDialog.show();
                return;
            }
            var csrfToken = document.getElementById("csrfToken").value;
        	xhr.post({
                url: '<s:url value="/poInvGrnDnMatching/saveAcceptOrReject.action" />',
                content: {"param.matchingOid": matchingOid, "param.supplierStatusActionRemarks":remarks, "param.supplierStatusValue":supplierStatusValue, "csrfToken":csrfToken},
                load: function(result)
                {
                    if (result=='"revised"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2148" />'})).show();
                    }
                    else if (result=='"pending"')
				    {
                	    (new InformationDialog({message: '<s:text name="B2BPC2134" />'})).show();
				    }
                	else if (result=='"matched"')
				    {
                	    (new InformationDialog({message: '<s:text name="B2BPC2135" />'})).show();
				    }
                	else if (result == '"closed"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2128" />'})).show();
                    }
                    else if(result == '"insufficientInv"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2113" />'})).show();
                    }
                    else if(result == '"outdated"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2114" />'})).show();
                    }
                    else if(result == '"accepted"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2110" />'})).show();
                    }
                    else if(result == '"rejected"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2109" />'})).show();
                    }
                    else if (result == '"remarks"')
                 	{
                 		(new InformationDialog({message: '<s:text name="B2BPC2106" />'})).show();
                 	}
                    else if(result == '"1"')
                    {
                    	hideDiv("supplierRemarks");
                    	changeToURL('<s:url value="/poInvGrnDnMatching/init.action?keepSp=Y" />');
                    	clearRemarks();
                    }
                    else if(result == '"rejectsuccess"')
                    {
                    	//changeToURL('<s:url value="/poInvGrnDnMatching/init.action?keepSp=Y" />');
                    	hideDiv("supplierRemarks");
                    	clearRemarks();
                    	var informationDialog = new InformationDialog({message: '<s:text name="B2BPC2150" />'});
                    	var on = require("dojo/on");
                    	on(informationDialog, "hide", function(){ changeToURL('<s:url value="/poInvGrnDnMatching/init.action?keepSp=Y" />');});
                    	on(informationDialog, "click", function(){ changeToURL('<s:url value="/poInvGrnDnMatching/init.action?keepSp=Y" />');});
                    	informationDialog.show();
                    }
                    else
                    {
                        var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2102" />'});
                        infoDialog.show();
                    }
                }
            });
        }

        function clearRemarks()
        {
            var registry = require("dijit/registry");
            registry.byId("supplierRemarksText").set("value","");
        }
        

    </script>
    <div dojoType="dijit.Dialog" id="supplierRemarks" title="Remarks" style="width:50%">
        <table class="commtable">
            <tr>
                <td style="vertical-align:text-top; line-height:23px"><span class="required">*</span></td>
                <td style="vertical-align:text-top; line-height:23px"><s:text name="admin.msgManagerment.summary.msgContent" /></td>
                <td style="vertical-align:text-top; line-height:23x">:</td>
                <td style="line-height:23px; font:Arial, Helvetica, sans-serif"><textarea cols="70%" rows="16" id="supplierRemarksText" maxlength="255" data-dojo-type="dijit/form/Textarea"></textarea></td>
            </tr>
            <tr class="space"><td></td></tr>
            <tr>
                <td colspan="4" style="text-align:left">
                   <button data-dojo-type="dijit.form.Button"  onclick="saveSupplierStatus();"><s:text name="button.ok"/></button>
                   <button data-dojo-type="dijit.form.Button" onclick="clearRemarks();"><s:text name="button.clear"/></button>
               </td>
           </tr>
        </table>
    </div>  
</head>
<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveAcceptOrReject.action') && #session.ifAcceptReject == 'yes'" >
        		<button data-dojo-type="dijit.form.Button" id="acceptButton" ><s:text name="button.accept" /></button>
        		<button data-dojo-type="dijit.form.Button" id="rejectButton" ><s:text name="button.reject" /></button>
        	</s:if>
           	<button data-dojo-type="dijit.form.Button" id="backBtn" ><s:text name="button.back.to.list" /></button>
        </td></tr></tbody></table>
    </div>
	<div>
		<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
	</div>
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name="pigd.detail.summary.rltmsg" /></div>
    </div>
    <table class="commtable">
        	<tbody>
        		<tr>
	                <td width="130"><s:text name="pigd.detail.summary.supplierRemark" />:</td>
	                <td>
	                	<s:property value="param.supplierStatusActionRemarks" />
	                </td>
	            </tr>
	        </tbody>
	    </table>
    <div id="details">
    	<table class="tablestyle3" id="sourceTable">
    		<thead>
	            <tr>
	            	<th width="3%"><s:text name="pigd.detail.summary.No"/></th>
	                <th width="7%"><s:text name="pigd.detail.summary.itemCode"/></th>
	                <th width="15%"><s:text name="pigd.detail.summary.itemDesc"/></th>
	                <th width="7%"><s:text name="pigd.detail.summary.barCode"/></th>
	                <th width="5%"><s:text name="pigd.detail.summary.poPrice"/></th>
		            <th width="7%"><s:text name="pigd.detail.summary.invPrice"/></th>
		            <th width="8%"><s:text name="pigd.detail.summary.priceStatus"/></th>
		            <th width="5%"><s:text name="pigd.detail.summary.poQty"/></th>
		            <th width="7%"><s:text name="pigd.detail.summary.invQty"/></th>
		            <th width="5%"><s:text name="pigd.detail.summary.grnQty"/></th>
		            <th width="8%"><s:text name="pigd.detail.summary.qtyStatus"/></th>
	            </tr>
        	</thead>
        	<tbody>
        		<s:iterator value="details" id="item" status="status">
        			<s:if test="#status.index%2==0">
        				<tr class="even" id="show<s:property value="#status.index" />">
        			</s:if>
        			<s:else>
        				<tr class="odd" id="show<s:property value="#status.index" />">
        			</s:else>
        				<td><s:property value="#item.seq" /></td>
        				<td><s:property value="#item.buyerItemCode" /></td>
        				<td><s:property value="#item.itemDesc" /></td>
        				<td><s:property value="#item.barcode" /></td>
        				
        				<s:if test="#item.priceMatched == false" >
        					<td class="amountAlign"><font color="red"><s:property value="#item.poPrice" /></font></td>
	        				<td class="amountAlign"><font color="red"><s:property value="#item.invPrice" /></font></td>
        				</s:if>
        				<s:else>
        					<td class="amountAlign"><s:property value="#item.poPrice" /></td>
	        				<td class="amountAlign"><s:property value="#item.invPrice" /></td>
        				</s:else>
        				<td onMouseOver="loadPriceTooltip(this, '<s:property value="#item.priceStatusActionByName" />', '<s:property value="#item.priceStatusActionDate" />', '<s:property value="#item.priceStatusActionRemarks" />')" onMouseOut="closePriceTooltip(this)">
        					<s:if test="#item.priceStatusValue == 'PENDING'">
        					</s:if>
        					<s:else>
        						<a href="javascript:void(0)"><s:property value="#item.priceStatusValue" /></a>
        					</s:else>
        				</td>
       					<s:if test="#item.qtyMatched == false" >
        					<td class="amountAlign"><font color="red"><s:property value="#item.poQty" /></font></td>
	        				<td class="amountAlign"><font color="red"><s:property value="#item.invQty" /></font></td>
	        				<td class="amountAlign"><font color="red"><s:property value="#item.grnQty" /></font></td>
       					</s:if>
       					<s:else>
        					<td class="amountAlign"><s:property value="#item.poQty" /></td>
	        				<td class="amountAlign"><s:property value="#item.invQty" /></td>
	        				<td class="amountAlign"><s:property value="#item.grnQty" /></td>
       					</s:else>
        				<td onMouseOver="loadQtyTooltip(this, '<s:property value="#item.qtyStatusActionByName" />', '<s:property value="#item.qtyStatusActionDate" />', '<s:property value="#item.qtyStatusActionRemarks" />')" onMouseOut="closeQtyTooltip(this)">
        					<s:if test="#item.qtyStatusValue == 'PENDING'">
        					</s:if>
        					<s:else>
        						<a href="javascript:void(0)"><s:property value="#item.qtyStatusValue" /></a>
        					</s:else>
        				</td>
        			</tr>
        		</s:iterator>
        	</tbody>
    	</table>
    </div>
</body>
</html>
