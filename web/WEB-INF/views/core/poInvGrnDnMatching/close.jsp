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

                    
                    on(registry.byId("saveCloseBtn"), 'click',
                        function()
                        {
                    		initRemarks();
                        }
                    )

                });

        function initRemarks()
        {
        	var registry = require("dijit/registry");
        	document.getElementById("closeRemarksText").value="";
        	registry.byId("closeRemarks").show();
        }

		function saveClose()
		{
			var ConfirmDialog = require("custom/ConfirmDialog");
        	var InformationDialog = require("custom/InformationDialog");
            var remarks = dijit.byId("closeRemarksText").value;
            if (remarks.length == 0 || remarks.length > 255)
            {
            	var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2106" />'});
                infoDialog.show();
                return;
            }
            clearRemarks();
            var csrfToken = document.getElementById("csrfToken").value;
            changeToURL('<s:url value="/poInvGrnDnMatching/saveClose.action" />?csrfToken='+csrfToken+'&param.closedRemarks='+remarks);
		}
        
        
        function clearRemarks()
        {
            var registry = require("dijit/registry");
            registry.byId("closeRemarksText").set("value","");
        }

    </script>
    <div dojoType="dijit.Dialog" id="closeRemarks" title="Remarks" style="width:50%">
    	<form id="remarksForm" name="searchForm" method="post" >
        <table class="commtable">
            <tr>
                <td style="vertical-align:text-top; line-height:23px"><span class="required">*</span></td>
                <td style="vertical-align:text-top; line-height:23px"><s:text name="admin.msgManagerment.summary.msgContent" /></td>
                <td style="vertical-align:text-top; line-height:23x">:</td>
                <td style="line-height:23px; font:Arial, Helvetica, sans-serif"><textarea name="param.closedRemarks" cols="70%" rows="16" id="closeRemarksText" maxlength="255" data-dojo-type="dijit/form/Textarea"></textarea></td>
            </tr>
            <tr class="space"><td></td></tr>
            <tr>
                <td colspan="4" style="text-align:left">
                   <button data-dojo-type="dijit.form.Button"  onclick="saveClose();"><s:text name="button.ok"/></button>
                   <button data-dojo-type="dijit.form.Button" onclick="clearRemarks();"><s:text name="button.clear"/></button>
               </td>
           </tr>
        </table>
        </form>
    </div>     
    
</head>
<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveClose.action') && #session.canCloseRecord != 'no'" >
            	<button data-dojo-type="dijit.form.Button" id="saveCloseBtn" ><s:text name="button.confirm" /></button>
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
    <div id="details">
    	
    		<s:if test="#session.isRevised == 'unit'">
    		<table class="tablestyle3" id="sourceTable">
    			<thead>
	            <tr>
	            	<th width="5%"><s:text name="pigd.detail.summary.No"/></th>
	                <th width="10%"><s:text name="pigd.detail.summary.itemCode"/></th>
	                <th width="20%"><s:text name="pigd.detail.summary.itemDesc"/></th>
	                <th width="7%"><s:text name="pigd.detail.summary.barCode"/></th>
	                <th width="7%"><s:text name="pigd.detail.summary.poPrice"/></th>
		            <th width="7%"><s:text name="pigd.detail.summary.invPrice"/></th>
		            <th width="7%"><s:text name="pigd.detail.summary.poQty"/></th>
		            <th width="7%"><s:text name="pigd.detail.summary.invQty"/></th>
		            <th width="7%"><s:text name="pigd.detail.summary.grnQty"/></th>
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
        			</tr>
        		</s:iterator>
        	</tbody>
        	</table>
    		</s:if>
    		<s:elseif test="#session.isRevised == 'yes'">
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
    		<table class="tablestyle3" id="sourceTable">
    		<thead>
	            <tr>
	            	<th width="3%" rowSpan=2><s:text name="pigd.detail.summary.No"/></th>
	                <th width="7%" rowSpan=2><s:text name="pigd.detail.summary.itemCode"/></th>
	                <th width="13%" rowSpan=2><s:text name="pigd.detail.summary.itemDesc"/></th>
	                <th width="7%" rowSpan=2><s:text name="pigd.detail.summary.barCode"/></th>
	                <th width="5%" rowSpan=2><s:text name="pigd.detail.summary.poPrice"/></th>
		            <th width="9%"><s:text name="pigd.detail.summary.invPrice"/></th>
		            <th width="7%" rowSpan=2><s:text name="pigd.detail.summary.priceStatus"/></th>
		            <th width="5%" rowSpan=2><s:text name="pigd.detail.summary.poQty"/></th>
		            <th width="8%"><s:text name="pigd.detail.summary.invQty"/></th>
		            <th width="5%" rowSpan=2><s:text name="pigd.detail.summary.grnQty"/></th>
		            <th width="8%" rowSpan=2><s:text name="pigd.detail.summary.qtyStatus"/></th>
	            </tr>
	            <tr>
	            	<th><s:text name="pigd.detail.summary.oldInvPrice"/></th>
	            	<th><s:text name="pigd.detail.summary.oldInvQty"/></th>
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
        				<td rowSpan=2><s:property value="#item.seq" /></td>
        				<td rowSpan=2><s:property value="#item.buyerItemCode" /></td>
        				<td rowSpan=2><s:property value="#item.itemDesc" /></td>
        				<td rowSpan=2><s:property value="#item.barcode" /></td>
       					<td class="amountAlign"  rowSpan=2><s:property value="#item.poPrice" /></td>
        				<s:if test="#item.priceMatched == false" >
	        				<td class="amountAlign"><font color="red"><s:property value="#item.invPrice" /></font></td>
        				</s:if>
        				<s:else>
	        				<td class="amountAlign"><s:property value="#item.invPrice" /></td>
        				</s:else>
        				<td onMouseOver="loadPriceTooltip(this, '<s:property value="#item.oldDetail.priceStatusActionByName" />', '<s:property value="#item.oldDetail.priceStatusActionDate" />', '<s:property value="#item.oldDetail.priceStatusActionRemarks" />')" onMouseOut="closePriceTooltip(this)" rowSpan=2>
        					<s:if test="#item.oldDetail.priceStatusValue == 'PENDING'">
        					</s:if>
        					<s:else>
        						<a href="javascript:void(0)"><s:property value="#item.oldDetail.priceStatusValue" /></a>
        					</s:else>
        				</td>
        				<td class="amountAlign" rowSpan=2><s:property value="#item.poQty" /></td>
       					<s:if test="#item.qtyMatched == false" >
	        				<td class="amountAlign"><font color="red"><s:property value="#item.invQty" /></font></td>
       					</s:if>
       					<s:else>
	        				<td class="amountAlign"><s:property value="#item.invQty" /></td>
       					</s:else>
	        			<td class="amountAlign" rowSpan=2><s:property value="#item.grnQty" /></td>
        				<td onMouseOver="loadQtyTooltip(this, '<s:property value="#item.oldDetail.qtyStatusActionByName" />', '<s:property value="#item.oldDetail.qtyStatusActionDate" />', '<s:property value="#item.oldDetail.qtyStatusActionRemarks" />')" onMouseOut="closeQtyTooltip(this)" rowSpan=2>
        					<s:if test="#item.oldDetail.qtyStatusValue == 'PENDING'">
        					</s:if>
        					<s:else>
        						<a href="javascript:void(0)"><s:property value="#item.oldDetail.qtyStatusValue" /></a>
        					</s:else>
        				</td>
        			</tr>
        			<s:if test="#status.index%2==0">
        				<tr class="even" id="show<s:property value="#status.index" />">
        			</s:if>
        			<s:else>
        				<tr class="odd" id="show<s:property value="#status.index" />">
        			</s:else>
        				<s:if test="#item.priceMatched == false" >
	        				<td class="amountAlign"><font color="red"><s:property value="#item.oldDetail.invPrice" /></font></td>
        				</s:if>
        				<s:else>
	        				<td class="amountAlign"><s:property value="#item.oldDetail.invPrice" /></td>
        				</s:else>
        				
        				<s:if test="#item.qtyMatched == false" >
	        				<td class="amountAlign"><font color="red"><s:property value="#item.oldDetail.invQty" /></font></td>
       					</s:if>
       					<s:else>
	        				<td class="amountAlign"><s:property value="#item.oldDetail.invQty" /></td>
       					</s:else>
        			</tr>
        		</s:iterator>
        	</tbody>
        	</table>
    		</s:elseif>
    		<s:else>
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
    		<table class="tablestyle3" id="sourceTable">
    		<thead>
	            <tr>
	            	<th width="3%"><s:text name="pigd.detail.summary.No"/></th>
	                <th width="7%"><s:text name="pigd.detail.summary.itemCode"/></th>
	                <th width="15%"><s:text name="pigd.detail.summary.itemDesc"/></th>
	                <th width="7%"><s:text name="pigd.detail.summary.barCode"/></th>
	                <th width="5%"><s:text name="pigd.detail.summary.poPrice"/></th>
		            <th width="5%"><s:text name="pigd.detail.summary.invPrice"/></th>
		            <th width="10%"><s:text name="pigd.detail.summary.priceStatus"/></th>
		            <th width="5%"><s:text name="pigd.detail.summary.poQty"/></th>
		            <th width="5%"><s:text name="pigd.detail.summary.invQty"/></th>
		            <th width="5%"><s:text name="pigd.detail.summary.grnQty"/></th>
		            <th width="10%"><s:text name="pigd.detail.summary.qtyStatus"/></th>
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
        					   <s:if test="(param.matchingStatus.name() != 'MATCHED' && param.matchingStatus.name() != 'MATCHED_BY_DN') && (param.matchingStatus.name() == 'UNMATCHED' || param.matchingStatus.name() == 'PRICE_UNMATCHED') ">
        						<s:property value="#item.priceStatusValue" />
        					   </s:if>
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
        					   <s:if test="(param.matchingStatus.name() != 'MATCHED' && param.matchingStatus.name() != 'MATCHED_BY_DN') && (param.matchingStatus.name() == 'UNMATCHED' || param.matchingStatus.name() == 'QTY_UNMATCHED') ">
        						<s:property value="#item.qtyStatusValue" />
        					   </s:if>
        					</s:if>
        					<s:else>
        						<a href="javascript:void(0)"><s:property value="#item.qtyStatusValue" /></a>
        					</s:else>
        				</td>
        			</tr>
        		</s:iterator>
        	</tbody>
        	</table>
    		</s:else>
    </div>
</body>
</html>
