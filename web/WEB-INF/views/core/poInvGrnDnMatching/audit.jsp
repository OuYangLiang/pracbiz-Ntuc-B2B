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
                "dijit/form/Textarea",
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
                    Textarea,
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
                    loadTooltip = function(src, actionBy, actionDate)
		            {
		            	
		              	var value="";
		              	var rowItemValue = actionBy;
		                var tooltipLabel = "Action By";
		                
						var dateItemValue = actionDate;
						var dateLable = "Action Date";
						
						if (actionBy == null || actionDate == null || actionBy == '' || actionDate == '')
						{
							return;
						}
		
		                if (rowItemValue != null && rowItemValue != "")
		                {
		                	value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
				            value +=tooltipLabel +'</label><br/>';
		                  	var content = "";
		                	var length = 0;
		                	for (var i=0;i < rowItemValue.length ; i++)
		              		{
		                		length ++;
		                		if (rowItemValue.charAt(i)=='\n')
		                		{
		                			content += '<br/>';
		                			length = 0;
		                		}
		                		else
		                		{
		                			content += rowItemValue.charAt(i);
		                			
		                			if (length > 50)
		                			{
		                				content += '<br/>';
		                				length = 0;
		                			}
		                		}
		              		}
		                	value += content + "<br/>";
		                }

		                if (dateItemValue != null && dateItemValue != "")
		                {
		                	value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
				            value +=dateLable +'</label><br/>';
		                  	var content = "";
		                	var length = 0;
		                	for (var i=0;i < dateItemValue.length ; i++)
		              		{
		                		length ++;
		                		if (dateItemValue.charAt(i)=='\n')
		                		{
		                			content += '<br/>';
		                			length = 0;
		                		}
		                		else
		                		{
		                			content += dateItemValue.charAt(i);
		                			
		                			if (length > 50)
		                			{
		                				content += '<br/>';
		                				length = 0;
		                			}
		                		}
		              		}
		                	value += content;
		                }
		                
		                Tooltip.defaultPosition=["after-centered","after"];
		                Tooltip.show(value,src);
		                return;
		            }
		            // remove tooltip
		            closeTooltip = function(src)
		            {
		            	Tooltip.hide(src);
		            }
                    
                    on(registry.byId("backBtn"), 'click', 
                        function()
                        {
                    		changeToURL('<s:url value="/poInvGrnDnMatching/init.action?keepSp=Y" />');
                        }
                    );

                    on(registry.byId("saveAuditBtn"), 'click',
                        function()
                        {
                        	var oids = "";
							var checkBoxs = document.getElementsByName("saveCheckBox");
							var msg = "";

							var flag = false;
							for (var i = 0; i < checkBoxs.length ; i ++)
							{
								var id = checkBoxs[i].id;
								if (checkBoxs[i].checked)
								{
									var parts = id.split("-");
									var remarks = dijit.byId("remarks-"+parts[2]).value;
									if (parts[0] == "reject")
									{
							            if (remarks.length == 0 || remarks.length > 255)
							            {
								            msg += parts[2] + ","
							            }
									}

									if (remarks == "" || remarks == null)
									{
										remarks = " ";
									}
									oids = oids + parts[0] + "," + parts[1] + "," + parts[2] + "," +remarks + "," + '<s:property value="#session.auditType" />' + "-" ;
									flag = true;
								}
								
							}

							if (!flag)
							{
								(new InformationDialog({message: '<s:text name="alert.select.any" />'})).show();
                     		    return;
							}

							if (msg != "")
							{
								msg = msg.substring(0, msg.length-1);
								var infoDialog = new InformationDialog({message: 'Length of Remarks must between 1 and 255 in No. ' + msg + '.'});
				                infoDialog.show();
				                return;
							}

							oids = oids.substring(0, oids.length-1);
							var csrfToken = dom.byId("csrfToken").value;
							xhr.get({
                                url: '<s:url value="/poInvGrnDnMatching/putParamIntoSession.action" />',
                                content: {selectedOids: oids},
                                load: function(data)
                                {
                                	if (data!='x')
                                    {
                                        (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                                    }
                                    else
                                    {
                                    	xhr.get({
                                            url: '<s:url value="/poInvGrnDnMatching/saveAudit.action" />?csrfToken='+csrfToken,
                                            load: function(result)
                                            {
                                                if(result == '"notExist"')
                                                {
                                                	var confirmDialog = new ConfirmDialog({
                                                        message: '<s:text name="B2BPC2132" />',
                                                        yesBtnPressed: function(){
                                                        	changeToURL('<s:url value="/poInvGrnDnMatching/init.action?keepSp=Y" />');
                                                        }
                                                    });
                                                	confirmDialog.show();
                                                }
                                                else if(result == '"allAudit"')
                                                {
                                                	var confirmDialog = new ConfirmDialog({
                                                        message: '<s:text name="B2BPC2151" />',
                                                        yesBtnPressed: function(){
                                                        	changeToURL('<s:url value="/poInvGrnDnMatching/init.action?keepSp=Y" />');
                                                        }
                                                    });
                                                	confirmDialog.show();
                                                }
                                                else if (result == '"remarks"')
                                             	{
                                             		(new InformationDialog({message: '<s:text name="B2BPC2106" />'})).show();
                                             	}
                                                else if(result == '"1"')
                                                {
                                                	changeToURL('<s:url value="/poInvGrnDnMatching/init.action?keepSp=Y" />');
                                                }
                                                else
                                                {
                                                    var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2102" />'});
                                                    infoDialog.show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    )
                });

        function selectOne(src)
        {
        	var registry = require("dijit/registry");
			var id1 = src.id;
			var id2 = "";

			var parts = id1.split("-");
			if (parts[0] == "accept")
			{
				id2 = "reject-" + parts[1] + "-" + parts[2];
			}
			else
			{
				id2 = "accept-" + parts[1] + "-" + parts[2];
			}

			if (src.checked)
			{
				registry.byId(id2).set("checked",false);
			}
			
        }

        function selectAtLeastOne(src)
        {
        	var registry = require("dijit/registry");
			var id1 = src.id;
			var id2 = "";

			if (src.checked)
			{
				registry.byId(id1).set("checked",true);
				var InformationDialog = require("custom/InformationDialog");
				(new InformationDialog({message: '<s:text name="B2BPC2152" />'})).show();
				return;
			}

			var parts = id1.split("-");
			if (parts[0] == "accept")
			{
				id2 = "reject-" + parts[1] + "-" + parts[2];
			}
			else
			{
				id2 = "accept-" + parts[1] + "-" + parts[2];
			}

			if (!src.checked)
			{
				registry.byId(id2).set("checked",false);
			}
			
        }

        function hideAndShow()
		{
			var value=document.getElementById("hideShow").value;
			var trs = document.getElementsByTagName("tr");
			if (value=="show")
		  	{
			  	document.getElementById("hideShow").value="hide";
			  	document.getElementById("hideShow").innerHTML='<s:text name="button.show.item" />';
			  	for (var i = 0; i < trs.length; i++)
			  	{
				  	if(startWith(trs[i].id, "hide"))
				  	{
				  		trs[i].style.display="none";
				  	}
			  	}
			
		    }
		    else
			{
				document.getElementById("hideShow").value="show";
				document.getElementById("hideShow").innerHTML='<s:text name="button.hide.item" />';
				for (var i = 0; i < trs.length; i++)
			  	{
				  	if(startWith(trs[i].id, "hide"))
				  	{
				  		trs[i].style.display="";
				  	}
			  	}
			}
		}


		function startWith(source, str)
		{
			if(str==null||str==""||source.length==0||str.length>source.length)
			{
			    return false;
			}
			if(source.substr(0,str.length)==str)
			{
			    return true;
			}
			else
			{
			    return false;
			}

		}
    </script>
    
</head>
<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<button data-dojo-type="dijit.form.Button" id="hideShow" onclick="hideAndShow();" value="hide"><s:text name="button.show.item" /></button>
            <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveAudit.action') && (#session.isAllAudited == 'no' || (param.supplierStatusValue == 'REJECTED' && param.buyerStatusValue == 'PENDING'))" >
            	<button data-dojo-type="dijit.form.Button" id="saveAuditBtn" ><s:text name="button.save" /></button>
            </s:if>
           	<button data-dojo-type="dijit.form.Button" id="backBtn" ><s:text name="button.back.to.list" /></button>
        </td></tr>
        </tbody></table>
    </div>
 	<div>
		<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
	</div>
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name="pigd.detail.summary.rltmsg" /></div>
    </div>
    <div id="details">
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
    	<table class="tablestyle3" id="sourceTable" style="table-layout: fixed;">
    		<thead>
	            <tr>
	            	<th width="3%"><s:text name="pigd.detail.summary.No"/></th>
	                <th width="7%"><s:text name="pigd.detail.summary.itemCode"/></th>
	                <th width="15%"><s:text name="pigd.detail.summary.itemDesc"/></th>
	                <th width="8%"><s:text name="pigd.detail.summary.barCode"/></th>
	                <s:if test="#session.auditType == 'price'" >
	                	<th width="7%"><s:text name="pigd.detail.summary.classCode"/></th>
		                <th width="9%"><s:text name="pigd.detail.summary.subclassCode"/></th>
	                	<th width="7%"><s:text name="pigd.detail.summary.poPrice"/></th>
		                <th width="9%"><s:text name="pigd.detail.summary.invPrice"/></th>
		                <th width="8%"><s:text name="pigd.detail.summary.priceStatus"/></th>
	                </s:if>
	                <s:if test="#session.auditType == 'qty'" >
		                <th width="10%"><s:text name="pigd.detail.summary.poQty"/></th>
		                <th width="10%"><s:text name="pigd.detail.summary.invQty"/></th>
		                <th width="10%"><s:text name="pigd.detail.summary.grnQty"/></th>
		                <th width="10%"><s:text name="pigd.detail.summary.qtyStatus"/></th>
	                </s:if>
	                <th width="5%"><s:text name="pigd.detail.summary.approve"/></th>
	                <th width="5%"><s:text name="pigd.detail.summary.reject"/></th>
	                <th width="15%"><s:text name="pigd.detail.summary.remarks"/></th>             
	            </tr>
        	</thead>
        	<tbody>
        		<s:iterator value="details" id="item" status="status">
        			<s:if test="#status.index%2==0">
        				<s:if test="(#item.priceMatched == false && #session.auditType == 'price' && (#item.privileged == true || #item.isJustShow == true)) 
        							|| (#item.qtyMatched == false && #session.auditType == 'qty' && (#item.privileged == true || #item.isJustShow == true)) ">
        					<tr class="even" id="show<s:property value="#status.index" />">
        				</s:if>
        				<s:else>
        					<tr class="even" id="hide<s:property value="#status.index" />" style="display: none">
        				</s:else>
        			</s:if>
        			<s:else>
        				<s:if test="(#item.priceMatched == false && #session.auditType == 'price' && (#item.privileged == true || #item.isJustShow == true)) 
        							|| (#item.qtyMatched == false && #session.auditType == 'qty' && (#item.privileged == true || #item.isJustShow == true)) ">
        					<tr class="odd" id="show<s:property value="#status.index" />">
        				</s:if>
        				<s:else>
        					<tr class="odd" id="hide<s:property value="#status.index" />" style="display: none">
        				</s:else>
        			</s:else>
        				<td style="word-wrap:   break-word"><s:property value="#item.seq" /></td>
        				<td style="word-wrap:   break-word"><s:property value="#item.buyerItemCode" /></td>
        				<td style="word-wrap:   break-word"><s:property value="#item.itemDesc" /></td>
        				<td style="word-wrap:   break-word"><s:property value="#item.barcode" /><s:property value="param.buyerStatusValue" /><s:property value="param.supplierStatusValue" /></td>
        				<s:if test="#session.auditType == 'price'" >
        					<td style="word-wrap:   break-word"><s:property value="#item.classCode" /></td>
        					<td style="word-wrap:   break-word"><s:property value="#item.subclassCode" /></td>
	        				<s:if test="#item.priceMatched == false" >
	        					<td class="amountAlign"><font color="red"><s:property value="#item.poPrice" /></font></td>
		        				<td class="amountAlign"><font color="red"><s:property value="#item.invPrice" /></font></td>
	        				</s:if>
	        				<s:else>
	        					<td class="amountAlign"><s:property value="#item.poPrice" /></td>
		        				<td class="amountAlign"><s:property value="#item.invPrice" /></td>
	        				</s:else>
	        				<td onMouseOver="loadTooltip(this, '<s:property value="#item.priceStatusActionByName" />', '<s:property value="#item.priceStatusActionDate" />')" onMouseOut="closeTooltip(this)">
	        					<s:if test="(#item.priceStatusValue == 'PENDING' && #item.priceMatched == true) || #item.isJustShow == true">
	        					</s:if>
	        					<s:elseif test="#item.priceStatusValue == 'PENDING'">
	        						<s:property value="#item.priceStatusValue" />
	        					</s:elseif>
	        					<s:else>
	        						<a href="javascript:void(0)"><s:property value="#item.priceStatusValue" /></a>
	        					</s:else>
	        				</td>
        				</s:if>
        				<s:if test="#session.auditType == 'qty'" >
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
	        				<td onMouseOver="loadTooltip(this, '<s:property value="#item.qtyStatusActionByName" />', '<s:property value="#item.qtyStatusActionDate" />')" onMouseOut="closeTooltip(this)">
	        					<s:if test="(#item.qtyStatusValue == 'PENDING' && #item.qtyMatched == true) || #item.isJustShow == true">
	        					</s:if>
	        					<s:elseif test="#item.qtyStatusValue == 'PENDING'">
	        						<s:property value="#item.qtyStatusValue" />
	        					</s:elseif>
	        					<s:else>
	        						<a href="javascript:void(0)"><s:property value="#item.qtyStatusValue" /></a>
	        					</s:else>
	        				</td>
        				</s:if>
        				<s:if test="param.priceStatusValue == 'PENDING' && param.supplierStatusValue == 'REJECTED' && #session.auditType == 'price' && #item.privileged == true" >
        					<s:if test="#session.auditType == 'price' && #item.priceMatched == false">
	        					<td align="center"><input type="checkbox" id="accept-<s:property value="#item.matchingOid" />-<s:property value="#item.seq" />" name="saveCheckBox" data-dojo-type="dijit.form.CheckBox" <s:if test="#item.priceStatusValue == 'ACCEPTED'">checked</s:if> <s:if test="#item.priceStatusValue == 'ACCEPTED' || #item.priceStatusValue == 'REJECTED'">onMouseDown="selectAtLeastOne(this);"</s:if><s:else>onclick="selectOne(this);"</s:else> /></td>
        						<td align="center"><input type="checkbox" id="reject-<s:property value="#item.matchingOid" />-<s:property value="#item.seq" />" name="saveCheckBox" data-dojo-type="dijit.form.CheckBox" <s:if test="#item.priceStatusValue == 'REJECTED'">checked</s:if> <s:if test="#item.priceStatusValue == 'REJECTED' || #item.priceStatusValue == 'ACCEPTED'">onMouseDown="selectAtLeastOne(this);"</s:if><s:else>onclick="selectOne(this);"</s:else> /></td>
        						<td><textarea id="remarks-<s:property value="#item.seq" />" data-dojo-type="dijit/form/Textarea" maxlength="255" wrap="hard"><s:property value="#item.priceStatusActionRemarks" /></textarea></td>
        					</s:if>
        					<s:else>
        						<td align="center"><input type="checkbox" style="display:none" id="accept-<s:property value="#item.matchingOid" />-<s:property value="#item.seq" />" name="saveCheckBox" data-dojo-type="dijit.form.CheckBox" onclick="selectOne(this);" checked/></td>
        						<td align="center"><input type="checkbox" style="display:none" id="reject-<s:property value="#item.matchingOid" />-<s:property value="#item.seq" />" name="saveCheckBox" data-dojo-type="dijit.form.CheckBox" onclick="selectOne(this);"/></td>
        						<td><textarea style="display:none" id="remarks-<s:property value="#item.seq" />" data-dojo-type="dijit/form/Textarea" maxlength="255" wrap="hard"></textarea></td>
        					</s:else>
        				</s:if>
        				<s:elseif test="param.qtyStatusValue == 'PENDING' && param.supplierStatusValue == 'REJECTED' && #session.auditType == 'qty' && #item.privileged == true">
        					<s:if test="#session.auditType == 'qty' && #item.qtyMatched == false">
        						<td align="center"><input type="checkbox" id="accept-<s:property value="#item.matchingOid" />-<s:property value="#item.seq" />" name="saveCheckBox" data-dojo-type="dijit.form.CheckBox" <s:if test="#item.qtyStatusValue == 'ACCEPTED'">checked</s:if> <s:if test="#item.qtyStatusValue == 'ACCEPTED' || #item.qtyStatusValue == 'REJECTED'">onMouseDown="selectAtLeastOne(this);"</s:if><s:else>onclick="selectOne(this);"</s:else> /></td>
        						<td align="center"><input type="checkbox" id="reject-<s:property value="#item.matchingOid" />-<s:property value="#item.seq" />" name="saveCheckBox" data-dojo-type="dijit.form.CheckBox" <s:if test="#item.qtyStatusValue == 'REJECTED'">checked</s:if> <s:if test="#item.qtyStatusValue == 'ACCEPTED' || #item.qtyStatusValue == 'REJECTED'">onMouseDown="selectAtLeastOne(this);"</s:if><s:else>onclick="selectOne(this);"</s:else> /></td>
        						<td><textarea id="remarks-<s:property value="#item.seq" />" data-dojo-type="dijit/form/Textarea" maxlength="255" wrap="hard"><s:property value="#item.qtyStatusActionRemarks" /></textarea></td>
        					</s:if>
        					<s:else>
        						<td align="center"><input type="checkbox" style="display:none" id="accept-<s:property value="#item.matchingOid" />-<s:property value="#item.seq" />" name="saveCheckBox" data-dojo-type="dijit.form.CheckBox" onclick="selectOne(this);" checked/></td>
        						<td align="center"><input type="checkbox" style="display:none" id="reject-<s:property value="#item.matchingOid" />-<s:property value="#item.seq" />" name="saveCheckBox" data-dojo-type="dijit.form.CheckBox" onclick="selectOne(this);"/></td>
        						<td><textarea style="display:none" id="remarks-<s:property value="#item.seq" />" data-dojo-type="dijit/form/Textarea" maxlength="255" wrap="hard"></textarea></td>
        					</s:else>
        				</s:elseif>
        				<s:else>
        					<td></td>
        					<td></td>
        					<s:if test="#session.auditType == 'price'" >
        						<td style="word-wrap:   break-word"><s:property value="#item.priceStatusActionRemarks" /></td>
        					</s:if>
        					<s:else>
        						<td style="word-wrap:   break-word"><s:property value="#item.qtyStatusActionRemarks" /></td>
        					</s:else>
        				</s:else>
        			</tr>
        		</s:iterator>
        	</tbody>
    	</table>
    </div>
</body>
</html>
