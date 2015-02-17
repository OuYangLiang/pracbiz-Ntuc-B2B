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

                var header = <s:property value='grnHeaderJson'/>;
                var details = <s:property value='grnDetailJson'/>;
                var data = {identifier: 'lineSeqNo', label: 'lineSeqNo',items:  details}; 
                var jsonStore = new ItemFileWriteStore({data : data});

                changeGrnHeader = function(grnHeader)
    		    {
    		        var id = grnHeader.id;
    		        var value = grnHeader.value;
    		        value = dojo.string.trim(value);
    		        if (value.length > 255)
       		        {
    		        	var infoDialog = new InformationDialog({message: '<s:text name="B2BPC1811"/>'});
						grnHeader.value=value.substring(0,255);
    		        	infoDialog.show();
    		        	
           		        return;
       		        }
    		        header[id] = value;
    		    }
                
                changeDelQtyDetails = function(object,lineSeqNo)
			    {
			        if (object == null )
			        {
			            console.log("object is null");
			            return;
			        }
			       
			        var objValue = object.value;
                    var objName = object.name;
					
                    jsonStore.fetchItemByIdentity({identity:lineSeqNo,onItem:function(item)
                    {
                    	var value = jsonStore.getValue(item,objName);
	                    if (objName == "deliveryQty")
	                    {
	                        var receiveQty = jsonStore.getValue(item,"receiveQty");
	                        
	                        if (!validateNumber(objValue) || objValue < 0 ) 
	                        {
	                        	var infoDialog = new InformationDialog({message: '<s:text name="B2BPC1825"/>'});
	                            dojo.byId("deliveryQty" + lineSeqNo).value=parseFloat(value).toFixed(2);
	                            infoDialog.show();
	                            return;
	                        }

	                        jsonStore.setValue(item, objName, objValue);
	                        dojo.byId("deliveryQty" + lineSeqNo).value=parseFloat(objValue).toFixed(2);
	                        return;
	                    }
                    }});
			    } 
			    
                if(dom.byId("saveBtn"))
			    {
				    on(registry.byId("saveBtn"), 'click', function()
				    {	
						var receiveQtys = document.getElementsByName("receiveQty");
						var changeFlag = false;
						for (var i = 0 ; i < receiveQtys.length; i++)
						{
							var recQty = dom.byId(receiveQtys[i].id).innerHTML
							var delQty = dom.byId(receiveQtys[i].id.replace("receiveQty", "deliveryQty")).value;
							if (recQty != delQty)
							{
								changeFlag = true;
							}
						}

						if (dom.byId("disputeSupplierRemarks").value == null || dom.byId("disputeSupplierRemarks").value == '')
						{
							var infoDialog = new InformationDialog({message: '<s:text name="B2BPC1808" />'});
							infoDialog.show();
							return;
						}

						if (!changeFlag)
						{
							var infoDialog = new InformationDialog({message: '<s:text name="B2BPC1809" />'});
							infoDialog.show();
							return;
						}
						
						var headerJson = JSON.stringify(header);
                        var detailJson = JSON.stringify(getDetailJsonData(jsonStore));
                        var csrfToken = dom.byId("csrfToken").value;
                        var url = '<s:url value="/grn/saveDispute.action" />';
                       
                        xhr.post({
                            url: url,
                            handleAs: "json",
                            content: {grnDetailJson: detailJson,grnHeaderJson: headerJson, csrfToken: csrfToken},
                            load:function(jsonData)
                            {
                                if (jsonData.success === "success")
                                {
                                    changeToURL('<s:url value="/grn/init.action" />?keepSp=Y');
                                }
                            }
                        });
                     
				    });
			    }

                if (dom.byId("restBtn"))
                {
                	  on(registry.byId("restBtn"), 'click', function()
                      {
                          changeToURL('<s:url value="/grn/initDispute.action" />?docOid=<s:property value="grn.header.grnOid"/>');
                      }); 
                }

                if (dom.byId("cancelBtn"))
                {
                    on(registry.byId("cancelBtn"), 'click', function()
                    {
                        changeToURL('<s:url value="/grn/init.action" />?keepSp=Y');
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
    </script>
</head>

<body>
	<!-- Content Part -->
	<div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
       	<!-- action button part -->
       	<div class="summaryBtn">
       		<div style="float: right;">
	    		<button data-dojo-type="dijit/form/Button" id="saveBtn" ><s:text name="button.save"/></button>
	    		<button data-dojo-type="dijit/form/Button" id="restBtn" ><s:text name="button.reset"/></button>
	    		<button data-dojo-type="dijit/form/Button" id="cancelBtn" ><s:text name="button.cancel"/></button>
	    	</div>
       	</div>
       	<!-- here is message area -->
    	<div id="errorMsg">
		</div>
		<div >
			<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
		</div>
       	<!-- dispute Part -->
       	<div data-dojo-type="dijit.TitlePane" 
       		data-dojo-props="title:'<s:text name="grn.dispute.header"/>', width:275" 
       		open="true" style="width:100%;">
       		<div style="width:100%">
       			<table class="tablestyle2">
					<tbody>
						<tr class="odd">
							<td class="tdLabel"><s:text name="grn.header.grnNo"/></td>
							<td class="tdData"><s:property value="grn.header.grnNo"/></td>
							<td class="tdLabel"><s:text name="grn.header.grnDate"/></td>
							<td class="tdData"><s:property value="grn.header.grnDate"/></td>
						</tr>
						<tr class="even">
							<td class="tdLabel"><s:text name="grn.header.poNo"/></td>
							<td class="tdData"><s:property value="grn.header.poNo"/></td>
							<td class="tdLabel"><s:text name="grn.header.poDate"/></td>
							<td class="tdData"><s:property value="grn.header.poDate"/></td>
						</tr>
						<tr class="odd">
							<td class="tdLabel"><s:text name="grn.header.buyerCode"/></td>
							<td class="tdData"><s:property value="grn.header.buyerCode"/></td>
							<td class="tdLabel"><s:text name="grn.header.buyerName"/></td>
							<td class="tdData"><s:property value="grn.header.buyerName"/></td>
						</tr>
						<tr class="even">
							<td class="tdLabel"><s:text name="grn.header.supplierCode"/></td>
							<td class="tdData"><s:property value="grn.header.supplierCode"/></td>
							<td class="tdLabel"><s:text name="grn.header.supplierName"/></td>
							<td class="tdData"><s:property value="grn.header.supplierName"/></td>
						</tr>
						<tr class="odd">
							<td class="tdLabel"><s:text name="grn.header.supplierRemarks"/></td>
							<td class="tdData" colspan=3>
							<s:textarea id="disputeSupplierRemarks" cols="120" rows="5" onchange="changeGrnHeader(this);" name="grn.header.disputeSupplierRemarks"></s:textarea>
							</td>
						</tr>
					</tbody>
				</table>
       		</div>
        	
       	</div>
       	
       	<div class="space"></div>
       	<div data-dojo-type="dijit.TitlePane" 
       		data-dojo-props="title:'<s:text name="grn.dispute.detail"/>', width:275" 
       		open="true" style="width:100%;">
       		<div style="width:100%">
       			<table class="tablestyle1">
					<thead>
						<tr>
							<th width="2%"><s:text name="grn.detail.seq"/></th>
							<th width="6%"><s:text name="grn.detail.itemNo"/></th>
							<th width="8%"><s:text name="grn.detail.itemDesc"/></th>
							<th width="9%"><s:text name="grn.detail.barcode"/></th>
							<th width="8%"><s:text name="grn.detail.receivedQty"/></th>
							<th width="9%"><s:text name="grn.detail.deliveryQty"/></th>
							
						</tr>
					</thead>
					<tbody>
						<s:iterator value="grn.details" id="detail" status="stat">
						<tr id="<s:property value="#stat.index+1"/>" >
							<td><s:property value="#stat.index+1"/></td>
							<td><s:property value="#detail.buyerItemCode"/></td>
							<td><s:property value="#detail.itemDesc"/></td>
							<td><s:property value="#detail.barcode"/></td>
							<s:if test="(#detail.deliveryQty) == null">
								<td id="receiveQty<s:property value="#detail.lineSeqNo"/>" name="receiveQty"><s:property value="#detail.receiveQty"/></td>
								<td>
									<input id="deliveryQty<s:property value="#detail.lineSeqNo"/>" 
										type="text" name="deliveryQty" 
										data-dojo-type="dijit/form/NumberTextBox" data-dojo-props="maxLength:11,constraints:{pattern: '###0.00'}" 
										onblur="changeDelQtyDetails(this,'<s:property value="#detail.lineSeqNo"/>');" 
										value="<s:property value="#detail.deliveryQty"/>" />
								</td>
							</s:if>
							<s:else>
								<td id="receiveQty<s:property value="#detail.lineSeqNo"/>" name="receiveQty"><s:property value="#detail.receiveQty" /></td>
								<td>
									<input id="deliveryQty<s:property value="#detail.lineSeqNo"/>" 
										type="text" name="deliveryQty" 
										data-dojo-type="dijit/form/NumberTextBox" data-dojo-props="maxLength:11,constraints:{pattern: '###0.00'}" 
										onblur="changeDelQtyDetails(this,'<s:property value="#detail.lineSeqNo"/>');" 
										value="<s:property value="#detail.deliveryQty"/>" />
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
