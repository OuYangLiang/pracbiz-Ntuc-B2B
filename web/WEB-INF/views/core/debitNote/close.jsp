<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script>
    require(
            [
             "custom/B2BPortalBase",
             "dojo/dom",
             "dojo/dom-style",
             "dijit/registry",
             "dojo/on",
             "dojo/date",
             "dojo/parser",
             "dojo/_base/xhr",
             "dijit/Tooltip",
             "dijit/form/NumberTextBox",
             "dijit/form/CheckBox",
             "dojo/_base/lang",
             "dojo/data/ItemFileWriteStore",
             "custom/CustomDateTextBox",
             "dojo/query",
             "dojo/json",
             "custom/InformationDialog",
             "dojo/domReady!"
             ], 
             function(
                 B2BPortalBase,
                 dom,
                 domStyle,
                 registry,
                 on,
                 date,
                 parser,
                 xhr,
                 Tooltip,
                 NumberTextBox,
                 CheckBox,
                 lang,
                 ItemFileWriteStore,
                 CustomDateTextBox,
                 query,
                 JSON,
                 InformationDialog
                 )
             {
                parser.parse();
                
                (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');

                var header = <s:property value='dnHeaderJson'/>;
                var details = <s:property value='dnDetailJson'/>;
                var data = {identifier: 'lineSeqNo', label: 'lineSeqNo',items:  details}; 
                var jsonStore = new ItemFileWriteStore({data : data});
                
                changeDetail = function(object,lineSeqNo)
			    {
			        var objValue = object.value;
                    var objName = object.name;
                    
                    jsonStore.fetchItemByIdentity({identity:lineSeqNo,onItem:function(item)
                    {
                    	var oldValue = jsonStore.getValue(item,objName);
                    	
                    	if (objName == "confirmQty")
                        {
                            if (!validateNumber(objValue) || objValue < 0)  
                            {
                                var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2242"/>'});
                                dojo.byId("confirmQty" + lineSeqNo).value=parseFloat(oldValue).toFixed(2);
                                infoDialog.show();
                                return;
                            }

                            jsonStore.setValue(item, objName, objValue);
                            dojo.byId("confirmQty" + lineSeqNo).value=parseFloat(objValue).toFixed(2);
                            return;
                        }
                        else if (objName == "confirmPrice")
                        {
                            if (!validateNumber(objValue) || objValue < 0 )  
                            {
                                var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2243"/>'});
                                dojo.byId("confirmPrice" + lineSeqNo).value=parseFloat(oldValue).toFixed(2);
                                infoDialog.show();
                                return;
                            }

                            jsonStore.setValue(item, objName, objValue);
                            dojo.byId("confirmPrice" + lineSeqNo).value=parseFloat(objValue).toFixed(2);
                            return;
                        }
                    }});
			    } 
			    
                if(dom.byId("saveBtn"))
			    {
				    on(registry.byId("saveBtn"), 'click', function()
				    {	
				    	initRemarks();
				    });
			    }

                if (dom.byId("restBtn"))
                {
                	  on(registry.byId("restBtn"), 'click', function()
                      {
                          changeToURL('<s:url value="/dn/initClose.action" />?docOid=<s:property value="dn.dnHeader.dnOid"/>&actionType=close');
                      }); 
                }

                if (dom.byId("cancelBtn"))
                {
                    on(registry.byId("cancelBtn"), 'click', function()
                    {
                        changeToURL('<s:url value="/dn/init.action" />?keepSp=Y');
                    }); 
                }
                
                selectOne = function(src)
                {
                    var registry = require("dijit/registry");
                    var id1 = src.id;
                    var id2 = "";

                    var parts = id1.split("-");
                    if (parts[0] == "accept")
                    {
                        id2 = "reject-" + parts[1];
                    }
                    else
                    {
                        id2 = "accept-" + parts[1];
                    }

                    if (src.checked)
                    {
                        registry.byId(id2).set("checked",false);
                    }
                    
                }
                
                // init tooltips
                loadTooltip = function(src, actionBy)
                {
                        
                    var value="";
                    var rowItemValue = actionBy;
                    var tooltipLabel = "Remarks";
    
    
                    value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
                    value +=tooltipLabel +'</label><br/>';
                    if (rowItemValue != null && rowItemValue != "")
                    {
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
                        value += content;
                      Tooltip.defaultPosition=["before-centered","after"];
                      Tooltip.show(value,src);
                    }
                    return;
                }
                // remove tooltip
                closeTooltip = function(src)
                {
                    Tooltip.hide(src);
                }
                
                saveClose = function()
                {
                    var remarks = dijit.byId("closeRemarksText").value;
                    if (remarks.length == 0 || remarks.length > 255)
                    {
                        var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2106" />'});
                        infoDialog.show();
                        return;
                    }
                    header['closedRemarks'] = remarks;
                    clearRemarks();
                    
                    var headerJson = JSON.stringify(header);
                    var detailJson = JSON.stringify(getDetailJsonData(jsonStore));
                    var csrfToken = dom.byId("csrfToken").value;
                    var url = '<s:url value="/dn/saveClose.action" />';
                    xhr.post({
                        url: url,
                        handleAs: "json",
                        content: {dnDetailJson: detailJson,dnHeaderJson: headerJson, csrfToken: csrfToken},
                        load:function(jsonData)
                        {
                            if (jsonData.errorMsg == null)
                            {
                                changeToURL('<s:url value="/dn/init.action" />?keepSp=Y');
                            }
                            else if (jsonData.errorMsg == 'exception')
                            {
                                var infoDialog = new InformationDialog({message: 'Save failed, exception occurs!'});
                                infoDialog.show();
                            }
                            else
                            {
                                var infoDialog = new InformationDialog({message: jsonData.errorMsg});
                                infoDialog.show();
                            }
                        }
                    });
                }
             });

		    function getDetailJsonData(jsonStore)
		    {
		        var details = [];
		        var gotItems = function(items, request)
		        {
		            for (var i = 0; i < items.length; i++)
		            {
		                jsonData = itemToJSON(jsonStore,items[i]);
		                details.push(jsonData);
		            }
		        };
		        jsonStore.fetch({onComplete: gotItems});
		        return details;
		    }

		    function itemToJSON(store, item)
	        {
	            // summary: Function to convert an item into a JSON format.
	            // store:
	            //    The datastore the item came from.
	            // item:
	            //    The item in question.
	            var json = {};
	            if(item && store)
	            {
	                // Determine the attributes we need to process.
	                var attributes = store.getAttributes(item);
	                if(attributes && attributes.length > 0)
	                {
	                    var i;
	                    for(i = 0; i < attributes.length; i++)
	                    {
	                        var values = store.getValues(item, attributes[i]);
	                        if(values)
	                        {
	                            // Handle multivalued and single-valued attributes.
	                            if(values.length > 1 )
	                            {
	                                var j;
	                                json[attributes[i]] = [];
	                                for(j = 0; j < values.length; j++ )
	                                {
	                                    var value = values[j];
	                                    // Check that the value isn't another item. If it is, process it as an item.
	                                    if(store.isItem(value))
	                                    {
	                                        json[attributes[i]].push(dojo.fromJson(itemToJSON(store, value)));
	                                    }else{
	                                        json[attributes[i]].push(value);
	                                    }
	                                }
	                            }else{
	                                if(store.isItem(values[0]))
	                                {
	                                    json[attributes[i]] = dojo.fromJson(itemToJSON(store, values[0]));
	                                }else{
	                                    json[attributes[i]] = values[0];
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	            return json;
	        }

		    function validateNumber(x)
	        {
	            var f_x = parseFloat(x);
	            var f_x = Math.round(x*100)/100;
	            var s_x = f_x.toString();
	            
	            if (isNaN(f_x))
	            {
	                return false;
	            }
	            return true;
	        }
		    
		    function initRemarks()
	        {
	            var registry = require("dijit/registry");
	            document.getElementById("closeRemarksText").value="";
	            registry.byId("closeRemarks").show();
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
                <td style="line-height:23px; font:Arial, Helvetica, sans-serif"><textarea name="dn.dnHeader.closedRemarks" cols="70%" rows="16" id="closeRemarksText" maxlength="255" data-dojo-type="dijit/form/Textarea"></textarea></td>
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
	<!-- Content Part -->
	<div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
       	<!-- action button part -->
       	<div class="summaryBtn">
       		<div style="float: right;">
       		    <s:if test="dn.calculateCountOfPrivilege() > 0">
	    		    <button data-dojo-type="dijit/form/Button" id="saveBtn" ><s:text name="button.confirm"/></button>
       		    </s:if>
	    		<button data-dojo-type="dijit/form/Button" id="cancelBtn" ><s:text name="button.cancel"/></button>
	    	</div>
       	</div>
       	<div>
    		<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
   		</div>
       	<!-- here is message area -->
    	<div id="errorMsg">
		</div>
       	<!-- dispute Part -->
       	<div data-dojo-type="dijit.TitlePane" 
       		data-dojo-props="title:'<s:text name="dn.dispute"/>', width:275"  style="width:100%;">
       		<div style="width:100%">
       			<table class="tablestyle2">
					<tbody>
						<tr class="odd">
							<td class="tdLabel"><s:text name="dn.edit.header.dnNo"/></td>
							<td class="tdData"><s:property value="dn.dnHeader.dnNo"/></td>
							<td class="tdLabel"><s:text name="dn.edit.header.dnDate"/></td>
							<td class="tdData"><s:property value="dn.dnHeader.dnDate"/></td>
						</tr>
						<tr class="even">
							<td class="tdLabel"><s:text name="dn.edit.header.rtvNo"/></td>
							<td class="tdData"><s:property value="dn.dnHeader.rtvNo"/></td>
							<td class="tdLabel"><s:text name="dn.edit.header.GiNo"/></td>
							<td class="tdData"><s:property value="dn.dnHeader.GiNo"/></td>
						</tr>
						<tr class="odd">
							<td class="tdLabel"><s:text name="dn.edit.header.buyerCode"/></td>
							<td class="tdData"><s:property value="dn.dnHeader.buyerCode"/></td>
							<td class="tdLabel"><s:text name="dn.edit.header.buyerName"/></td>
							<td class="tdData"><s:property value="dn.dnHeader.buyerName"/></td>
						</tr>
						<tr class="even">
							<td class="tdLabel"><s:text name="dn.edit.header.supplierCode"/></td>
                            <td class="tdData"><s:property value="dn.dnHeader.supplierCode"/></td>
                            <td class="tdLabel"><s:text name="dn.edit.header.supplierName"/></td>
                            <td class="tdData"><s:property value="dn.dnHeader.supplierName"/></td>
						</tr>
					</tbody>
				</table>
       		</div>
        	
       	</div>
       	
       	<div class="space"></div>
       	<div data-dojo-type="dijit.TitlePane" 
       		data-dojo-props="title:'<s:text name="dn.dispute.detail"/>', width:275" style="width:100%;">
       		<div style="width:100%">
       			<table class="tablestyle1" style="table-layout: fixed;">
					<thead>
						<tr>
							<th width="3%"><s:text name="dn.edit.detail.lineSeqNo"/></th>
							<th width="5%"><s:text name="dn.edit.detail.supplierItemCode"/></th>
							<th width="7%"><s:text name="dn.edit.detail.itemDesc"/></th>
							<th width="7%"><s:text name="dn.edit.detail.barcode"/></th>
							<th width="5%"><s:text name="dn.edit.detail.price"/></th>
							<th width="5%"><s:text name="dn.edit.detail.disputePrice"/></th>
							<th width="5%"><s:text name="dn.edit.detail.confirmPrice"/></th>
							<th width="6%"><s:text name="dn.edit.detail.priceStatus"/></th>
							<th width="9%"><s:text name="dn.edit.detail.priceRemark"/></th>
							<th width="4%"><s:text name="dn.edit.detail.qty"/></th>
							<th width="5%"><s:text name="dn.edit.detail.disputeQty"/></th>
							<th width="5%"><s:text name="dn.edit.detail.confirmQty"/></th>
							<th width="6%"><s:text name="dn.edit.detail.qtyStatus"/></th>
							<th width="9%"><s:text name="dn.edit.detail.qtyRemark"/></th>
						</tr>
					</thead>
					<tbody>
					    <s:iterator value="dn.dnDetail" id="detail" status="stat">
						<tr>
							<td><s:property value="#stat.index+1"/></td>
							<td style="word-wrap:   break-word"><s:property value="#detail.buyerItemCode"/></td>
							<td style="word-wrap:   break-word"><s:property value="#detail.itemDesc"/></td>
							<td style="word-wrap:   break-word"><s:property value="#detail.barcode"/></td>
							<td><s:property value="#detail.unitCost"/></td>
							<td style="color:red" onMouseOver="loadTooltip(this, '<s:property value="#detail.disputePriceRemarks"/>')" onMouseOut="closeTooltip(this)">
							    <s:if test="dn.dnHeader.dispute && #detail.isDisputePriceChanged()">
								<a href="#" style="color: red;" onclick="return;"><s:property value="#detail.disputePrice"/></a>
							    </s:if>
							</td>
							<s:if test="#detail.getPrivileged()">
								<td style="word-wrap:   break-word">
								     <s:if test="#session.permitUrl.contains('/dn/initClose.action')" >
									     <input id="confirmPrice<s:property value="#detail.lineSeqNo"/>" type="text" name="confirmPrice"
		                                    data-dojo-type="dijit/form/NumberTextBox" data-dojo-props="maxLength:11,constraints:{pattern: '###0.00'},style:{width: 67}" 
		                                    onblur="changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');" 
		                                    value="<s:property value="#detail.confirmPrice"/>" />
								     </s:if>
								     <s:else>
									     <input readonly="readonly" id="confirmPrice<s:property value="#detail.lineSeqNo"/>" type="text" name="confirmPrice"
		                                    data-dojo-type="dijit/form/NumberTextBox" data-dojo-props="maxLength:11,constraints:{pattern: '###0.00'},style:{width: 67}" 
		                                    onblur="changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');" 
		                                    value="<s:property value="#detail.confirmPrice"/>" />
								     </s:else>
	                            </td>
							</s:if>
							<s:else>
								<td style="color: <s:if test='#detail.isConfirmPriceChanged()'>red</s:if>">
							         <s:if test='dn.dnHeader.closed'>
								        <s:property value="#detail.confirmPrice"/>
                                     </s:if>
	                            </td>
							</s:else>
							<td style="word-wrap:   break-word">
							    <s:if test="dn.dnHeader.dispute && #detail.isDisputePriceChanged()">
								    <s:property value="#detail.priceStatus"/>
									<s:if test="#detail.priceStatusActionByName != null && #detail.priceStatusActionByName != ''">
									<br/>(<s:property value="#detail.priceStatusActionByName"/>)
									</s:if>
									<s:if test="#detail.priceStatusActionDate != null">
									<br/><s:property value="#detail.priceStatusActionDate"/>
									</s:if>
							    </s:if>
							</td>
                            <td style="word-wrap:   break-word">
                                <s:property value="#detail.priceStatusActionRemarks"/>
                            </td>
							
							<td><s:property value="#detail.debitQty"/></td>
							<td style="color: red" onMouseOver="loadTooltip(this, '<s:property value="#detail.disputeQtyRemarks"/>')" onMouseOut="closeTooltip(this)">
								<s:if test='dn.dnHeader.dispute && #detail.isDisputeQtyChanged()'>
								<a href="#" style="color: red;" onclick="return;"><s:property value="#detail.disputeQty"/></a>
							    </s:if>
							</td>
							<s:if test="#detail.getPrivileged()">
								<td style="word-wrap:   break-word">
								     <s:if test="#session.permitUrl.contains('/dn/initClose.action')" >
		                                 <input id="confirmQty<s:property value="#detail.lineSeqNo"/>" type="text" name="confirmQty"
		                                    data-dojo-type="dijit/form/NumberTextBox" data-dojo-props="maxLength:6,constraints:{pattern: '###0.00'},style:{width: 67}" 
		                                    onblur="changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');" 
		                                    value="<s:property value="#detail.confirmQty"/>" />
								     </s:if>
								     <s:else>
		                                 <input readonly="readonly" id="confirmQty<s:property value="#detail.lineSeqNo"/>" type="text" name="confirmQty"
		                                    data-dojo-type="dijit/form/NumberTextBox" data-dojo-props="maxLength:6,constraints:{pattern: '###0.00'},style:{width: 67}" 
		                                    onblur="changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');" 
		                                    value="<s:property value="#detail.confirmQty"/>" />
								     </s:else>
	                            </td>
							</s:if>
							<s:else>
								<td style="color: <s:if test='#detail.isConfirmQtyChanged()'>red</s:if>">
							         <s:if test='dn.dnHeader.closed'>
	                                     <s:property value="#detail.confirmQty"/>
							         </s:if>
	                            </td>
							</s:else>
							<td style="word-wrap:   break-word">
							    <s:if test='dn.dnHeader.dispute && #detail.isDisputeQtyChanged()'>
								<s:property value="#detail.qtyStatus"/>
								    <s:if test='#detail.qtyStatusActionByName != null && #detail.qtyStatusActionByName != ""'>
									<br/>(<s:property value="#detail.qtyStatusActionByName"/>)
								    </s:if>
								    <s:if test='#detail.qtyStatusActionDate != null'>
									<br/><s:property value="#detail.qtyStatusActionDate"/>
								    </s:if>
							    </s:if>
							</td>
                            <td style="word-wrap:   break-word">
                                <s:property value="#detail.qtyStatusActionRemarks"/>
                            </td>
						</tr>	
						</s:iterator>
					</tbody>
				</table>
       		</div>
       	</div>
	</div>
</body>
</html>
