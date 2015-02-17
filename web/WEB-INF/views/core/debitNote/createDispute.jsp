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
                    	
	                    if (objName == "disputeQty")
	                    {
	                        if (!validateNumber(objValue) || objValue < 0)  
	                        {
	                        	var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2235"/>'});
	                            dojo.byId("disputeQty" + lineSeqNo).value=parseFloat(oldValue).toFixed(2);
	                            infoDialog.show();
	                            return;
	                        }

	                        jsonStore.setValue(item, objName, objValue);
	                        dojo.byId("disputeQty" + lineSeqNo).value=parseFloat(objValue).toFixed(2);
	                        return;
	                    }
	                    else if (objName == "disputeQtyRemarks")
	                    {
                            jsonStore.setValue(item, objName, objValue);
                            return;
	                    }
	                    else if (objName == "disputePrice")
	                    {
	                    	if (!validateNumber(objValue) || objValue < 0 )  
                            {
                                var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2237"/>'});
                                dojo.byId("disputePrice" + lineSeqNo).value=parseFloat(oldValue).toFixed(2);
                                infoDialog.show();
                                return;
                            }

                            jsonStore.setValue(item, objName, objValue);
                            dojo.byId("disputePrice" + lineSeqNo).value=parseFloat(objValue).toFixed(2);
                            return;
	                    }
	                    else if (objName == "disputePriceRemarks")
	                    {
                            jsonStore.setValue(item, objName, objValue);
                            return;
	                    }
                    }});
			    } 
			    
                if(dom.byId("saveBtn"))
			    {
				    on(registry.byId("saveBtn"), 'click', function()
				    {	
						var headerJson = JSON.stringify(header);
                        var detailJson = JSON.stringify(getDetailJsonData(jsonStore));
                        var csrfToken = dom.byId("csrfToken").value;
                        var url = '<s:url value="/dn/saveDispute.action" />';
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
                     
				    });
			    }

                if (dom.byId("restBtn"))
                {
                	  on(registry.byId("restBtn"), 'click', function()
                      {
                          changeToURL('<s:url value="/dn/initDispute.action" />?docOid=<s:property value="dn.dnHeader.dnOid"/>&actionType=<s:property value="actionType"/>&displayType=<s:property value="displayType"/>');
                      }); 
                }

                if (dom.byId("cancelBtn"))
                {
                    on(registry.byId("cancelBtn"), 'click', function()
                    {
                        changeToURL('<s:url value="/dn/init.action" />?keepSp=Y');
                    }); 
                }
                
                // init tooltips
                loadTooltip = function(src, actionBy, actionDate, actionRemarks)
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
                closeTooltip = function(src)
                {
                    Tooltip.hide(src);
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
    </script>
</head>

<body>
	<!-- Content Part -->
	<div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
       	<!-- action button part -->
       	<div class="summaryBtn">
       		<div style="float: right;">
       		    <s:if test="dn.dnHeader.dnStatus.name() != 'OUTDATED' && dn.dnHeader.dispute != true && dn.dnHeader.closed != true">
		    		<button data-dojo-type="dijit/form/Button" id="saveBtn" ><s:text name="button.save"/></button>
		    		<button data-dojo-type="dijit/form/Button" id="restBtn" ><s:text name="button.reset"/></button>
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
       		data-dojo-props="title:'<s:text name="dn.dispute"/>', width:275" 
       		open="true" style="width:100%;">
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
							<th style="text-align: center;" width="4%"><s:text name="dn.edit.detail.lineSeqNo"/></th>
							<th style="text-align: center;" width="8%"><s:text name="dn.edit.detail.buyerItemCode"/></th>
							<th style="text-align: center;" width="8%"><s:text name="dn.edit.detail.itemDesc"/></th>
							<th style="text-align: center;" width="9%"><s:text name="dn.edit.detail.barcode"/></th>
							<th style="text-align: center;" width="5%"><s:text name="dn.edit.detail.debitQty"/></th>
							<th style="text-align: center;" width="7%"><s:text name="dn.edit.detail.disputeQty"/></th>
							<th style="text-align: center;" ><s:text name="dn.edit.detail.qtyRemark"/></th>
							<s:if test="dn.dnHeader.dispute">
							<th style="text-align: center;" width="7%"><s:text name="dn.edit.detail.qtyStatus"/></th>
							</s:if>
							<th style="text-align: center;" width="5%"><s:text name="dn.edit.detail.price"/></th>
							<th style="text-align: center;" width="7%"><s:text name="dn.edit.detail.disputePrice"/></th>
							<th style="text-align: center;" ><s:text name="dn.edit.detail.priceRemark"/></th>
							<s:if test="dn.dnHeader.dispute">
							<th style="text-align: center;" width="7%"><s:text name="dn.edit.detail.priceStatus"/></th>
							</s:if>
						</tr>
					</thead>
					<tbody>
						<s:iterator value="dn.dnDetail" id="detail" status="stat">
						<tr id="<s:property value="#stat.index+1"/>" height="60">
							<td style="text-align: center;word-wrap:   break-word"><s:property value="#stat.index+1"/></td>
							<td style="text-align: center;word-wrap:   break-word"><s:property value="#detail.buyerItemCode"/></td>
							<td style="text-align: center;word-wrap:   break-word"><s:property value="#detail.itemDesc"/></td>
							<td style="text-align: center;word-wrap:   break-word"><s:property value="#detail.barcode"/></td>
							<s:if test="dn.dnHeader.dnStatus.name() == 'OUTDATED'  || dn.dnHeader.dispute == true || dn.dnHeader.closed == true">
								<td style="text-align: center;" id="debitQty<s:property value="#detail.lineSeqNo"/>" ><s:property value="#detail.debitQty"/></td>
								<td style="text-align: center;color: <s:if test='#detail.isDisputeQtyChanged()'>red</s:if>">
									<s:property value="#detail.disputeQty"/>
								</td>
								<td style="word-wrap:   break-word">
								     <s:property value="#detail.disputeQtyRemarks"/>
								</td>
								<s:if test="dn.dnHeader.dispute">
                                <td style="text-align: center;color: red" onMouseOver="loadTooltip(this, '<s:property value="#detail.qtyStatusActionByName"/>', 
                                    '<s:property value="#detail.qtyStatusActionDate"/>', '<s:property value="#detail.qtyStatusActionRemarks"/>')" onMouseOut="closeTooltip(this)">
                                    <s:if test='#detail.isDisputeQtyChanged()'>
	                                    <a href="#" onclick="return;"><s:property value="#detail.qtyStatus"/></a>
                                    </s:if>
                                </td>
                                </s:if>
								<td style="text-align: center;" id="unitCost<s:property value="#detail.lineSeqNo"/>" ><s:property value="#detail.unitCost"/></td>
								<td style="text-align: center;color: <s:if test='#detail.isDisputePriceChanged()'>red</s:if>">
									<s:property value="#detail.disputePrice"/>
								</td>
								<td style="word-wrap:   break-word">
								     <s:property value="#detail.disputePriceRemarks"/>
								</td>
								<s:if test="dn.dnHeader.dispute">
								<td style="text-align: center;color: red" onMouseOver="loadTooltip(this, '<s:property value="#detail.priceStatusActionByName"/>', 
								    '<s:property value="#detail.priceStatusActionDate"/>', '<s:property value="#detail.priceStatusActionRemarks"/>')" onMouseOut="closeTooltip(this)">
									<s:if test='#detail.isDisputePriceChanged()'>
		                                <a href="#" onclick="return;"><s:property value="#detail.priceStatus"/></a>
									</s:if>
	                            </td>
	                            </s:if>
							</s:if>
							<s:else>
							    <td style="text-align: center;" id="debitQty<s:property value="#detail.lineSeqNo"/>" ><s:property value="#detail.debitQty"/></td>
								<td>
									<input id="disputeQty<s:property value="#detail.lineSeqNo"/>" 
										type="text" name="disputeQty"
										data-dojo-type="dijit/form/NumberTextBox" data-dojo-props="maxLength:6,constraints:{pattern: '###0.00'},style:{width: 80}" 
										onblur="changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');" 
										value="<s:property value="#detail.disputeQty"/>" />
								</td>
								<td>
								     <textarea id="disputeQtyRemarks<s:property value="#detail.lineSeqNo"/>" name="disputeQtyRemarks" 
								         data-dojo-type="dijit/form/Textarea" onblur="changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');" 
								         maxlength="255" wrap="hard"><s:property value="#detail.disputeQtyRemarks"/></textarea>
								</td>
								<td style="text-align: center;" id="unitCost<s:property value="#detail.lineSeqNo"/>" ><s:property value="#detail.unitCost"/></td>
								<td>
									<input id="disputePrice<s:property value="#detail.lineSeqNo"/>" 
										type="text" name="disputePrice"
										data-dojo-type="dijit/form/NumberTextBox" data-dojo-props="maxLength:11,constraints:{pattern: '###0.00'},style:{width: 80}" 
										onblur="changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');" 
										value="<s:property value="#detail.disputePrice"/>"/>
								</td>
								<td>
								     <textarea id="disputePriceRemarks<s:property value="#detail.lineSeqNo"/>" name="disputePriceRemarks" 
								         data-dojo-type="dijit/form/Textarea" onblur="changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');" 
								         maxlength="255" wrap="hard"><s:property value="#detail.disputePriceRemarks"/></textarea>
								</td>
							</s:else>
						</tr>	
						</s:iterator>
					</tbody>
				</table>
       		</div>
       	</div>
	</div>
</body>
</html>
