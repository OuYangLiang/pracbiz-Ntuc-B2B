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
                 "dojo/parser",
                 "dojo/_base/xhr",
                 "dijit/Tooltip",
                 "dijit/form/TextBox",
                 "dijit/form/SimpleTextarea",
                 "dijit/form/DateTextBox",
                 "dijit/form/NumberTextBox",
                 "dojo/_base/lang",
                 "dojo/data/ItemFileWriteStore",
                 "custom/CustomDateTextBox",
                 "dojo/query",
                 "dojo/json",
                 "custom/ConfirmDialog",
                 "dojo/_base/event",
                 "dojo/domReady!"
                 ], 
                 function(
                     B2BPortalBase,
                     dom,
                     domStyle,
                     registry,
                     on,
                     parser,
                     xhr,
                     Tooltip,
                     TextBox,
                     Textarea,
                     DateTextBox,
                     NumberTextBox,
                     lang,
                     ItemFileWriteStore,
                     CustomDateTextBox,
                     query,
                     JSON,
                     ConfirmDialog,
                     event
                     )
                 {
        			parser.parse();
        			function stopRefresh()
        			{
        				dojo.connect(dojo.doc, 'onkeydown',function(e)
                		{
        					if ((e.keyCode == dojo.keys.F5 && !e.ctrlKey) 
                					|| (e.keyCode == 82 && e.ctrlKey) ) 
            				{
              			        dojo.stopEvent(e);
              			        return false;
              			    }
        				});
        			};
        			stopRefresh();

        			(new B2BPortalBase()).init(
     	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
     	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
     	                    '<s:property value="#session.commonParam.timeout" />',
     	                    '<s:url value="/logout.action" />');
	                
        			var header = <s:property value='invHeaderJson'/>;
        			var deliveryDate = '<s:property value="invoice.header.deliveryDate"/>'
       				if (deliveryDate)
             		{
       					header["deliveryDate"]=deliveryDate  + ' 00:00:00';
             		}
        			
        			//edit invHeader data
					changeInvHeader = function(invHeader)
					{
						var id = invHeader.id;
						var value = invHeader.value;
						var isString = lang.isString(value);
						if (isString)
						{
							value = dojo.string.trim(value);
						}
						header[id] = value;
					}

					changeInvHeaderDate = function(invHeader)
                    {
                        var id = invHeader.id;
                        var value = invHeader.value;
                        header[id] = value.getDate()+'/'+(value.getMonth() + 1)+'/'+value.getFullYear() + ' 00:00:00';
                    }

					changeInvHeaderForTermAndCondition = function(id)
                    {
                        var value = dom.byId(id).value;
                        var isString = lang.isString(value);
                        if (isString)
                        {
                            value = dojo.string.trim(value);
                        }
                        header[id] = value;
                    }
					
					//init Business Rules
					var rules = <s:property value="businessRulesJson"/>;
					initBusinessRules(rules,domStyle);
					
        			//edit invDetail data
					var details = <s:property value='invDetailJson'/>;
					//var invDetailObject = getJsonObject(details);
					var data = {identifier: 'lineSeqNo', label: 'lineSeqNo',items:  details}; 
					var jsonStore = new ItemFileWriteStore({data: data});
                    
					changeInvDetails = function(object,lineSeqNo)
                    {
	                    if (object  == null )
	                    {
		                    console.log("object is null");
		                    return;
		                }
		                
                        var objValue = object.value;
                        var objName = object.name;
                        console.log(objName,objValue);
                        jsonStore.fetchItemByIdentity({identity:lineSeqNo,onItem:function(item)
                        {
                        	var value = jsonStore.getValue(item,objName);
                        	var discountPercent,discountAmount;
                        	var newConTotalCost;
							var oldConTotalCost = jsonStore.getValue(item, "conTotalCost");
							var itemGrossCost, retailDiscAmt, discountAmount, netAmount, itemSharedCost;
							
							if (objName == "discountAmount")
							{
								if (!validateNumber(objValue))
								{
									alert('<s:text name="B2BPC1662" />');
									dojo.byId("discountAmount" + lineSeqNo).value=parseFloat(value).toFixed(2);
									return;
								}
								
								itemGrossCost = jsonStore.getValue(item, "itemGrossAmount");
								retailDiscAmt = jsonStore.getValue(item, "totalCustomerDisc");
								discountAmount = parseFloat(objValue).toFixed(2);
								netAmount = (parseFloat(itemGrossCost) - parseFloat(retailDiscAmt) - discountAmount).toFixed(2);
								itemSharedCost = jsonStore.getValue(item, "itemSharedCost");
								var conTotalCost = parseFloat(netAmount) + parseFloat(itemSharedCost);
								
								dojo.byId("discountAmount" + lineSeqNo).value=parseFloat(objValue).toFixed(2);
								dojo.byId("netAmount" + lineSeqNo).value=parseFloat(netAmount).toFixed(2);
								dojo.byId("conTotalCost" + lineSeqNo).innerHTML=parseFloat(conTotalCost).toFixed(2);

								jsonStore.setValue(item, "netAmount", netAmount);
								jsonStore.setValue(item, "discountAmount", discountAmount);
								jsonStore.setValue(item, "conTotalCost", conTotalCost);
								jsonStore.setValue(item, "itemGrossAmount", itemGrossCost);

								newConTotalCost = conTotalCost;
							}

							if (objName == "netAmount")
							{
								if (!validateNumber(objValue))
								{
									alert('<s:text name="B2BPC1654" />');
									dojo.byId("netAmount" + lineSeqNo).value=parseFloat(value).toFixed(2);
									return;
								}

								retailDiscAmt = jsonStore.getValue(item, "totalCustomerDisc");
								discountAmount = jsonStore.getValue(item, "discountAmount");
								netAmount = parseFloat(objValue).toFixed(2);
								itemSharedCost = jsonStore.getValue(item, "itemSharedCost");
								itemGrossCost = parseFloat(parseFloat(retailDiscAmt) + parseFloat(discountAmount) + parseFloat(netAmount)).toFixed(2);
								
								var conTotalCost = parseFloat(netAmount) + parseFloat(itemSharedCost);
							
								dojo.byId("conTotalCost" + lineSeqNo).innerHTML=parseFloat(conTotalCost).toFixed(2); 
								dojo.byId("itemGrossAmount" + lineSeqNo).innerHTML=parseFloat(itemGrossCost).toFixed(2); 
								dojo.byId("netAmount" + lineSeqNo).innerHTML=parseFloat(objValue).toFixed(2); 

								jsonStore.setValue(item, "itemGrossAmount", itemGrossCost);
								jsonStore.setValue(item, "netAmount", parseFloat(objValue).toFixed(2));
								jsonStore.setValue(item, "conTotalCost", parseFloat(conTotalCost).toFixed(2))

								newConTotalCost = conTotalCost;
							}

							if (objName == "itemSharedCost")
							{
								if (!validateNumber(objValue) || objValue < 0)
								{
									alert('<s:text name="B2BPC1655" />');
									dojo.byId("itemSharedCost" + lineSeqNo).innerHTML=parseFloat(value).toFixed(2);
									return;
								}

								netAmount = jsonStore.getValue(item, "netAmount");
								conTotalCost = (parseFloat(netAmount) + parseFloat(objValue)).toFixed(2); 
								dojo.byId("conTotalCost" + lineSeqNo).innerHTML=conTotalCost;
								newConTotalCost = conTotalCost;

								jsonStore.setValue(item, "itemSharedCost", parseFloat(objValue).toFixed(2));
								jsonStore.setValue(item, "conTotalCost", conTotalCost);
							}

							var change = (newConTotalCost - oldConTotalCost).toFixed(2);
							//jsonStore.setValue(item, objName, parseFloat(objValue).toFixed(2));
							console.log("change : " + change);
							var invTotalBeforeAdditional = (parseFloat(header.invTotalBeforeAdditional) + parseFloat(change)).toFixed(2);
							header.invTotalBeforeAdditional = invTotalBeforeAdditional;
							dojo.byId("totalBeforeTrade").innerHTML=invTotalBeforeAdditional;
							header.invTotalBeforeAdditional = invTotalBeforeAdditional;

							var additionalDiscountAmount = parseFloat(header.additionalDiscountAmount);
							var invAmountNoVat = (parseFloat(invTotalBeforeAdditional) - additionalDiscountAmount).toFixed(2);
							dojo.byId("invAmountNoVat").innerHTML=invAmountNoVat;
							header.invAmountNoVat = invAmountNoVat;

							var vatAmount = (invAmountNoVat * (header.vatRate/100)).toFixed(2);
							header.vatAmount = vatAmount;
							dojo.byId("vatAmount").innerHTML=vatAmount;

							var invAmountWithVat = (parseFloat(invAmountNoVat) + parseFloat(vatAmount)).toFixed(2);
							header.invAmountWithVat = invAmountWithVat;
							dojo.byId("invAmountWithVat").innerHTML=invAmountWithVat;

							var totalPay = (parseFloat(invAmountWithVat) - parseFloat(header.cashDiscountAmount)).toFixed(2);
							header.totalPay = totalPay;
							dojo.byId("totalPay").innerHTML=totalPay;
                        }});
                    } 

					changeAdditionalAmount = function()
					{
						 var totalBeforeTrade = dojo.byId("totalBeforeTrade").innerHTML;
						 if (totalBeforeTrade == null || parseFloat(totalBeforeTrade) == 0)
						 {
							 return;
						 }
						 var objValue = dojo.byId("additionalDiscountAmount").value;
	                     var objName = dojo.byId("additionalDiscountAmount").name;
	                     if ("" == objValue.trim() || !validateNumber(objValue) || parseFloat(objValue) < 0)
						 {
							 alert('<s:text name="B2BPC1639" />');
							 dojo.byId("additionalDiscountAmount").value=parseFloat(header.additionalDiscountAmount).toFixed(2);
							 return;
						 }

	                     if (parseFloat(objValue) >= parseFloat(header.invTotalBeforeAdditional))
						 {
							 alert('<s:text name="B2BPC1644" />');
							 dojo.byId("additionalDiscountAmount").value=parseFloat(header.additionalDiscountAmount).toFixed(2);
							 return;
						 }

						 if (parseFloat(objValue).toFixed(2) == parseFloat(header.additionalDiscountAmount).toFixed(2))
						 {
							 return;
						 }
						 
	                     header.invAmountNoVat = (parseFloat(header.invTotalBeforeAdditional) - parseFloat(objValue)).toFixed(2);
	                     header.additionalDiscountAmount = parseFloat(objValue).toFixed(2);
	                     dojo.byId("invAmountNoVat").innerHTML=header.invAmountNoVat;

	                     var addtionalPercent = parseFloat(objValue)/(parseFloat(header.invTotalBeforeAdditional))*100;
						 header.additionalDiscountPercent = parseFloat(addtionalPercent).toFixed(2)
	                     dojo.byId("additionalDiscountPercent").value=header.additionalDiscountPercent;

	                     var vatAmount = (header.invAmountNoVat * (header.vatRate/100)).toFixed(2);
						 header.vatAmount = vatAmount;
						 dojo.byId("vatAmount").innerHTML=vatAmount;

						 var invAmountWithVat = (parseFloat(header.invAmountNoVat) + parseFloat(header.vatAmount)).toFixed(2);
						 header.invAmountWithVat = invAmountWithVat;
						 dojo.byId("invAmountWithVat").innerHTML=invAmountWithVat;

						 var totalPay = (parseFloat(header.invAmountWithVat) - parseFloat(header.cashDiscountAmount)).toFixed(2);
						 header.totalPay = totalPay;
						 dojo.byId("totalPay").innerHTML=totalPay;
					}

					changeAdditionalPercent = function()
					{
						 var totalBeforeTrade = dojo.byId("totalBeforeTrade").innerHTML;
						 if (totalBeforeTrade == null || parseFloat(totalBeforeTrade) == 0)
						 {
							 return;
						 }
						 var objValue = dojo.byId("additionalDiscountPercent").value;
	                     var objName = dojo.byId("additionalDiscountPercent").name;
	                     if ("" == objValue.trim() || !validateNumber(objValue) || parseFloat(objValue) < 0 || parseFloat(objValue) >= 100)
						 {
							 alert('<s:text name="B2BPC1640" />');
							 dojo.byId("additionalDiscountPercent").value=parseFloat(header.additionalDiscountPercent).toFixed(2);
							 return;
						 }

	                     if (parseFloat(objValue).toFixed(2) == parseFloat(header.additionalDiscountPercent).toFixed(2))
						 {
							 return;
						 }
	                     header.additionalDiscountAmount = parseFloat(totalBeforeTrade) * parseFloat(parseFloat(objValue)/100);
	                     header.additionalDiscountPercent = parseFloat(objValue).toFixed(2);
	                     header.invAmountNoVat = (parseFloat(totalBeforeTrade) - parseFloat(header.additionalDiscountAmount)).toFixed(2);
	                     header.additionalDiscountAmount = parseFloat(header.additionalDiscountAmount).toFixed(2);
						 dojo.byId("additionalDiscountAmount").value = header.additionalDiscountAmount;
	                     dojo.byId("invAmountNoVat").innerHTML=header.invAmountNoVat;

	                     var vatAmount = (header.invAmountNoVat * (header.vatRate/100)).toFixed(2);
						 header.vatAmount = vatAmount;
						 dojo.byId("vatAmount").innerHTML=vatAmount;

						 var invAmountWithVat = (parseFloat(header.invAmountNoVat) + parseFloat(header.vatAmount)).toFixed(2);
						 header.invAmountWithVat = invAmountWithVat;
						 dojo.byId("invAmountWithVat").innerHTML=invAmountWithVat;

						 var totalPay = (parseFloat(header.invAmountWithVat) - parseFloat(header.cashDiscountAmount)).toFixed(2);
						 header.totalPay = totalPay;
						 dojo.byId("totalPay").innerHTML=totalPay;
					}

					var lastCash = header.cashDiscountAmount;
					changeCashAmount = function()
					{
						 var totalBeforeTrade = dojo.byId("totalBeforeTrade").innerHTML;
						 if (totalBeforeTrade == null || parseFloat(totalBeforeTrade) == 0)
						 {
							 return;
						 }
						 var objValue = dojo.byId("cashDiscountAmount").value;
	                     var objName = dojo.byId("cashDiscountAmount").name;
	                     if ("" == objValue.trim() || !validateNumber(objValue) || parseFloat(objValue) < 0)
						 {
							 alert('<s:text name="B2BPC1641" />');
							 dojo.byId("cashDiscountAmount").value=parseFloat(header.cashDiscountAmount).toFixed(2);
							 return;
						 }

	                     if (parseFloat(objValue) >= parseFloat(header.invAmountWithVat))
						 {
							 alert('<s:text name="B2BPC1645" />');
							 dojo.byId("cashDiscountAmount").value=parseFloat(header.cashDiscountAmount).toFixed(2);
							 return;
						 }
						 
	                     if (parseFloat(objValue).toFixed(2) == parseFloat(header.cashDiscountAmount).toFixed(2))
						 {
							 return;
						 }
						 var change = parseFloat(lastCash) - parseFloat(objValue);
						 lastCash = parseFloat(objValue);
	                     header.totalPay = (parseFloat(header.totalPay) + parseFloat(change)).toFixed(2);
	                     header.cashDiscountAmount = parseFloat(objValue).toFixed(2);
	                     dojo.byId("totalPay").innerHTML=header.totalPay;

	                     var cashPercent = parseFloat(objValue)/(parseFloat(header.totalPay) + parseFloat(header.cashDiscountAmount))*100;
						 header.cashDiscountPercent = parseFloat(cashPercent).toFixed(2)
	                     dojo.byId("cashDiscountPercent").value=header.cashDiscountPercent;

						 var totalPay = (parseFloat(header.invAmountWithVat) - parseFloat(header.cashDiscountAmount)).toFixed(2);
						 header.totalPay = totalPay;
						 dojo.byId("totalPay").innerHTML=totalPay;
					}

					changeCashPercent = function()
					{
						 var totalBeforeTrade = dojo.byId("totalBeforeTrade").innerHTML;
						 if (totalBeforeTrade == null || parseFloat(totalBeforeTrade) == 0)
						 {
							 return;
						 }
						 var objValue = dojo.byId("cashDiscountPercent").value;
	                     var objName = dojo.byId("cashDiscountPercent").name;
	                     if ("" == objValue.trim() || !validateNumber(objValue) || parseFloat(objValue) < 0 || parseFloat(objValue) >= 100)
						 {
							 alert('<s:text name="B2BPC1642" />');
							 dojo.byId("cashDiscountPercent").value=parseFloat(header.cashDiscountPercent).toFixed(2);
							 return;
						 }
	                     if (parseFloat(objValue).toFixed(2) == parseFloat(header.cashDiscountPercent).toFixed(2))
						 {
							 return;
						 }
						 var totalPay =parseFloat(header.totalPay) / (1 - parseFloat(header.cashDiscountPercent)/100)
	                     header.cashDiscountAmount = parseFloat(totalPay) * parseFloat(parseFloat(objValue)/100);
	                     header.cashDiscountPercent = parseFloat(objValue).toFixed(2);
	                     header.totalPay = (parseFloat(totalPay) - parseFloat(header.cashDiscountAmount)).toFixed(2);
	                     header.cashDiscountAmount = parseFloat(header.cashDiscountAmount).toFixed(2);
						 dojo.byId("cashDiscountAmount").value = header.cashDiscountAmount;
	                     dojo.byId("totalPay").innerHTML=header.totalPay;

						 var totalPay = (parseFloat(header.invAmountWithVat) - parseFloat(header.cashDiscountAmount)).toFixed(2);
						 header.totalPay = totalPay;
						 dojo.byId("totalPay").innerHTML=totalPay;
					}

					
        			if (dom.byId("saveBtn"))
                    {
                    	on(registry.byId("saveBtn"), 'click', function()
                        {
                            save('<s:url value="/inv/save.action"/>');
                        });
                    }

                    if (dom.byId("saveAndSentBtn"))
                    {
                    	on(registry.byId("saveAndSentBtn"), 'click', function()
                        {
                    		var confirmDialog = new ConfirmDialog({
                                message: '<s:text name="B2BPC1646" />',
                                yesBtnPressed: function(){
                                	save('<s:url value="/inv/saveAndSent.action"/>');
                                }
                            });
                        	confirmDialog.show();
                        });
                    }

                    function save(url)
                    {
                    	var headerJson = JSON.stringify(header);
                    	var detailJson = JSON.stringify(getDetailJsonData(jsonStore));
                    	var csrfToken = dom.byId("csrfToken").value;
                    	
                		xhr.post({
                        	url: url,
                            handleAs: "json",
                            content: {invDetailJson: detailJson,invHeaderJson: headerJson, csrfToken: csrfToken},
                            load:function(jsonData)
                            {
                                if (jsonData.success === "success")
                                {
                                	changeToURL('<s:url value="/inv/init.action" />?keepSp=Y');
                                	return;
                                }
                                
                                var errorMsg = jsonData.errorMsg;
                                if (errorMsg!=null)
                                {
                                	var obj = dojo.byId("errorMsg");
                                	obj.innerHTML=errorMsg;
                                	dojo.addClass(obj,"error");
                                }
                            }
                		});
                    }

                    if (dom.byId("restBtn"))
                    {
                    	on(registry.byId("restBtn"), 'click', function()
                    	{
                    		changeToURL('<s:url value="/inv/initEdtCnInv.action" />?param.invOid=<s:property value="invoice.header.invOid"/>');
                		}); 
                    }

                    if (dom.byId("cancelBtn"))
                    {
                    	on(registry.byId("cancelBtn"), 'click', function()
                    	{
                    		changeToURL('<s:url value="/inv/init.action" />?keepSp=Y');
                		}); 
                    }

                    changeInvHeaderForTermAndCondition = function(id)
                    {
                        var value = dom.byId(id).value;
                        var isString = lang.isString(value);
                        if (isString)
                        {
                            value = dojo.string.trim(value);
                        }
                        header[id] = value;
                    }

                    on(registry.byId("selectTermsConditions"),'click',
                        function()
                        {
                            var url = '<s:url value="/popup/popupViewTermConditionForInv.action" />?tradingPartner.supplierOid=<s:property value="invoice.header.supplierOid"/>';
                            window.open (url,'','width=600,height=400,scrollbars=1');
                        }
                    );
                });

        function initBusinessRules(rules,domStyle)
        {
        	if (!rules)
				return;
			var rule1 = getRuleBuyerRuleId(rules,"ItemDiscountEditable");
			if (rule1)
			{
				refreshDetail(".discountAmount",domStyle)
			}

			var rule2 = getRuleBuyerRuleId(rules,"ItemAmountEditable");
			if (rule2)
			{
				refreshDetail(".netAmount",domStyle)
			}
			var rule3 = getRuleBuyerRuleId(rules,"ItemSharedCostEditable");
			if (rule3)
			{
				refreshDetail(".itemSharedCost",domStyle);
			}
			var rule4 = getRuleBuyerRuleId(rules,"TradeDiscountEditable");
			if (rule4)
			{
				refreshDetail(".discount",domStyle);
			}
			var rule5 = getRuleBuyerRuleId(rules,"CashDiscountEditable");
			if (rule5)
			{
				refreshDetail(".discountCash",domStyle);
			}
			var rule6 = <s:property value="ggDisableInvoicePaymentInstructions" />
			if (rule6)
			{
				refreshHeader("selectTermsConditions", "style", "display:none;");
			}
        }

        function getRuleBuyerRuleId(rules,ruleId)
        {
        	for (var i=0; i < rules.length; i++)
        	{
				if (ruleId == rules[i].ruleId)
				{
					return rules[i];
				}
            }
        }

        function refreshHeader(id, attribute, content)
        {
        	dijit.registry.byId(id).set(attribute, content);
        }
        
        function refreshDetail(className,domStyle)
        {
        	dojo.query(className).forEach(function(node, index, nodeList)
			{
				var box = dijit.registry.byNode(node);
				box.setAttribute("readOnly",false);
				node.style.border = "thin solid";
			});
        }

        function getDetailJsonData(jsonStore)
        {
        	var details = []
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

        /* function getJsonObject(value)
        {
        	var value2string = JSON.stringify(value.replace(/&quot;/ig,'"').replace(/&amp;/ig,"&")); 
			var value2Json = JSON.parse(value2string);
			var value2Object = eval('('+value2Json+')');
			return value2Object;
        } */
        
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
    </script>
</head>

<body>
	<!-- Content Part -->
	<div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
       	<!-- action button part -->
       	<div class="summaryBtn">
       		<div style="float: right;">
	    		<button data-dojo-type="dijit/form/Button" id="saveBtn" ><s:text name="button.save"/></button>
	    		<button data-dojo-type="dijit/form/Button" id="saveAndSentBtn" ><s:text name="button.saveAndSent"/></button>
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
       	<!-- search Part -->
       	<div data-dojo-type="dijit.TitlePane" 
       		data-dojo-props="title:'<s:text name="inv.header"/>', width:275" 
       		open="true" style="width:100%;">
       		<div style="width:100%">
       			<table class="tablestyle2">
					<tbody>
						<tr class="odd">
							<td class="tdLabel"><s:text name="inv.header.supplierCode"/></td>
							<td class="tdData"><s:property value="invoice.header.supplierCode"/></td>
							<td class="tdLabel"><s:text name="inv.header.buyerCode"/></td>
							<td class="tdData"><s:property value="invoice.header.buyerCode"/></td>
						</tr>
						<tr class="even">
							<td class="tdLabel"><s:text name="inv.header.supplierName"/></td>
							<td class="tdData"><s:property value="invoice.header.supplierName"/></td>
							<td class="tdLabel"><s:text name="inv.header.buyerName"/></td>
							<td class="tdData"><s:property value="invoice.header.buyerName"/></td>
						</tr>
						<tr class="odd">
							<td class="tdLabel"><s:text name="inv.header.supplierAddress"/></td>
							<td class="tdData">
								<s:if test='invoice.header.supplierAddr1 != "" '>
									<s:property value="invoice.header.supplierAddr1"/><br />
								</s:if>
								<s:if test='invoice.header.supplierAddr2 != "" '>
									<s:property value="invoice.header.supplierAddr2"/><br />
								</s:if>
								<s:if test='invoice.header.supplierAddr3 != "" '>
									<s:property value="invoice.header.supplierAddr3"/><br />
								</s:if>
								<s:property value="invoice.header.supplierAddr4"/>
							</td>
							<td class="tdLabel"><s:text name="inv.header.store"/></td>
							<td class="tdData">
								<s:property value="invoice.header.shipToCode"/>
								<s:if test='invoice.header.shipToName != "" '>
									/ <s:property value="invoice.header.shipToName"/>
								</s:if>
							</td>
						</tr>
						<tr class="even">
							<td class="tdLabel"><s:text name="inv.header.suppCompanyRegNo"/></td>
							<td class="tdData"><s:property value="invoice.header.supplierBizRegNo"/></td>
							<td class="tdLabel"><s:text name="inv.header.poType"/></td>
							<td class="tdData"><s:property value="invoice.header.poType"/></td>
						</tr>
						<tr class="odd">
							<td class="tdLabel"><s:text name="inv.header.suppGstRegNo"/></td>
							<td class="tdData"><s:property value="invoice.header.supplierVatRegNo"/></td>
							<td class="tdLabel"><s:text name="inv.header.poNo"/></td>
							<td class="tdData"><s:property value="invoice.header.poNo"/></td>
						</tr>
						<tr class="even">
							<td class="tdLabel"><s:text name="inv.header.deliveryOrderNo"/></td>
							<td class="tdData">
								<input id="deliveryNo" type="text" 
										name="invoice.header.deliveryNo"
										data-dojo-type="dijit/form/TextBox" 
										onchange="changeInvHeader(this)" 
										value="<s:property value="invoice.header.deliveryNo"/>" 
										maxlength="20"/>
							</td>
							<td class="tdLabel"><s:text name="inv.header.taxInvNo"/></td>
							<td class="tdData">
								<s:if test="invoice.header.autoInvNumber">
									<s:property value="invoice.header.invNo"/>
								</s:if>
								<s:else>
									<input id="invNo" type="text" 
										name="invoice.header.invNo"
										data-dojo-type="dijit/form/TextBox" 
										onchange="changeInvHeader(this)" 
										value="<s:property value="invoice.header.invNo"/>" 
										maxlength="20"/>
									<span class="required">&nbsp;*</span>
								</s:else>
							</td>
						</tr>
						<tr class="odd">
							<td class="tdLabel"><s:text name="inv.header.deliveryDate"/></td>
							<td class="tdData">
								<s:property value="invoice.header.deliveryDate"/>
							</td>
							<td class="tdLabel"><s:text name="inv.header.invDate"/></td>
							<td class="tdData"><s:property value="invoice.header.invDate"/></td>
						</tr>
						<tr class="even">
							<td class="tdLabel"><s:text name="inv.header.credit"/></td>
							<td class="tdData"><s:property value="invoice.header.payTermCode"/></td>
							<td class="tdLabel"><s:text name="inv.header.promptPaymentDisc"/></td>
							<td class="tdData"><s:property value="invoice.header.payTermDesc"/></td>
						</tr>
						<tr class="odd">
							<td class="tdLabel" rowspan="4">
								<s:text name="inv.header.paymentInstructions"/>
								<button id="selectTermsConditions" data-dojo-type="dijit.form.Button" ><s:text name='button.select' /></button>
							</td>
							<td class="tdData">
								<input id="footerLine1" type="text" style="width:100%" 
									name="invoice.header.footerLine1"
									onchange="changeInvHeader(this)" 
									data-dojo-type="dijit/form/TextBox" readonly="readonly"
									value="<s:property value="invoice.header.footerLine1"/>" />
							</td>
							<td class="tdLabel" rowspan="4"><s:text name="inv.header.remarks"/></td>
							<td class="tdData" rowspan="4">
								<textarea id="invRemarks"  rows="4" cols="50" 
									name="invoice.header.invRemarks"
									onchange="changeInvHeader(this)" 
									data-dojo-type="dijit/form/SimpleTextarea" ><s:property value="invoice.header.invRemarks"/></textarea>
							</td>
						</tr>
						<tr class="odd">
							<td class="tdData" >
								<input id="footerLine2" type="text" style="width:100%; height: 100%;"
									name="invoice.header.term2"
									onchange="changeInvHeader(this)" 
									data-dojo-type="dijit/form/TextBox" readonly="readonly"
									value="<s:property value="invoice.header.footerLine2"/>" />
							</td>
							
						</tr>
						<tr  class="odd">
							<td class="tdData" >
								<input id="footerLine3" type="text" style="width:100%"
									name="invoice.header.term3"
									onchange="changeInvHeader(this)" 
									data-dojo-type="dijit/form/TextBox" readonly="readonly"
									value="<s:property value="invoice.header.footerLine3"/>" />
							</td>
							
						</tr>
						<tr class="odd">
							<td class="tdData">
								<input id="footerLine4" type="text" style="width:100%"
									data-dojo-type="dijit/form/TextBox" 
									name="invoice.header.term4"
									onchange="changeInvHeader(this)" readonly="readonly"
									value="<s:property value="invoice.header.footerLine4"/>" />
							</td>						
						</tr>
					</tbody>
				</table>
       		</div>
        	
       	</div>
       	
       	<div class="space"></div>
       	<div data-dojo-type="dijit.TitlePane" 
       		data-dojo-props="title:'<s:text name="inv.detail"/>', width:275" 
       		open="true" style="width:100%;">
       		<div style="width:100%">
       			<table class="tablestyle1">
					<thead>
						<tr>
							<th width="2%"><s:text name="inv.detail.SEQ"/></th>
							<th width="6%"><s:text name="inv.detail.SKUNO"/></th>
							<th width="8%"><s:text name="inv.detail.BARCODE"/></th>
							<th><s:text name="inv.detail.DESCRIPTION"/></th>
							<th width="8%"><s:text name="inv.detail.ARTICLENO"/></th>
							<th width="6%"><s:text name="inv.detail.QTY"/></th>
							<th width="8%"><s:text name="inv.detail.UNITCOST"/></th>
							<th width="3%"><s:text name="inv.detail.UOM"/></th>
							<th width="8%"><s:text name="inv.detail.TOTALPOSELL"/></th>
							<th width="8%"><s:text name="inv.detail.TOTALCUSTOMERDISCOUNT"/></th>
							<th width="6%"><s:text name="inv.detail.TRADEDISCOUNT"/></th>
							<th width="7%"><s:text name="inv.detail.AMOUNT"/></th>
							<th width="8%"><s:text name="inv.detail.SHAREDCOSTONDISCOUNT"/></th>
							<th width="6%"><s:text name="inv.detail.TOTAL"/></th>						
						</tr>
					</thead>
					<tbody>
						<s:iterator value="invoice.details" id="detail" status="stat">
						<tr id="<s:property value="#stat.index+1"/>">
							<td><s:property value="#stat.index+1"/></td>
							<td><s:property value="#detail.buyerItemCode"/></td>
							<td><s:property value="#detail.barcode"/></td>
							<td><s:property value="#detail.itemDesc"/></td>
							<td><s:property value="#detail.supplierItemCode"/></td>
							<td class="amountAlign">
								<s:if test="!(#detail.unitPrice == 0 and #detail.invQty == 0)">
									<s:property value="#detail.invQty"/>
								</s:if>
							</td>
							<td class="amountAlign" id="unitPrice<s:property value="#detail.lineSeqNo"/>">
								<s:if test="!(#detail.unitPrice == 0 and #detail.invQty == 0)">
									<s:property value="#detail.unitPrice"/>
								</s:if>
							</td>
							<td><s:property value="#detail.invUom"/></td>
							<td class="amountAlign"  id="itemGrossAmount<s:property value="#detail.lineSeqNo"/>" ><s:property value="#detail.itemGrossAmount"/></td>
							<td class="amountAlign"  id="totalCustomerDisc<s:property value="#detail.lineSeqNo"/>" ><s:property value="#detail.totalCustomerDisc"/></td>
							<td class="amountAlign">
								<input  id="discountAmount<s:property value="#detail.lineSeqNo"/>" 
										type="text" style="width:100%;border:1px;"
										name="discountAmount" <s:if test="#detail.conTotalCost != 0">class="discountAmount"</s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:11,constraints : {pattern: '###0.00'}"
										onblur="changeInvDetails(this,'<s:property value="#detail.lineSeqNo"/>');" 
										value="<s:property value="#detail.discountAmount"/>" />
							</td>
							<td class="amountAlign">
								<input  id='netAmount<s:property value="#detail.lineSeqNo"/>'
										type="text" style="width:100%;border:1px;"
										name="netAmount" <s:if test="#detail.conTotalCost  != 0">class="netAmount"</s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:11,constraints : {pattern: '###0.00'}"
										onblur="changeInvDetails(this,'<s:property value="#detail.lineSeqNo"/>');" 
										value="<s:property value="#detail.netAmount"/>" />
							</td>
							<td class="amountAlign">
								<input  id='itemSharedCost<s:property value="#detail.lineSeqNo"/>'
										type="text" style="width:100%;border:1px;"
										name="itemSharedCost" <s:if test="#detail.conTotalCost != 0">class="itemSharedCost"</s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:11,constraints : {pattern: '###0.00'}"
										onblur="changeInvDetails(this,'<s:property value="#detail.lineSeqNo"/>');" 
										value="<s:property value="#detail.itemSharedCost"/>" />
							</td>
							<td class="amountAlign"  id="conTotalCost<s:property value="#detail.lineSeqNo"/>" ><s:property value="#detail.conTotalCost"/></td>
						</tr>	
						</s:iterator>
						<tr>
							<td colspan="7" rowspan="7">&nbsp;</td>
							<td colspan="6" class="amountAlign"><s:text name="inv.detail.TOTAL.BEFORE.TRADE"/></td>
							<td class="amountAlign" id="totalBeforeTrade"><s:property value="invoice.header.invTotalBeforeAdditional"/></td>
						</tr>
						<tr>
							<td colspan="3" class="amountAlign"><s:text name="inv.detail.ADDITIONAL"/></td>
							<td class="amountAlign"><s:text name="inv.detail.PERCENT"/></td>
							<td>
								<input  id="additionalDiscountPercent" 
										type="text" style="width:100%;border:1px;"
										name="additionalDiscountPercent" <s:if test="invoice.header.invTotalBeforeAdditional != 0">class="discount"</s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:5,constraints : {pattern: '###0.00'}"
										onblur="changeAdditionalPercent();" 
										value="<s:property value="invoice.header.additionalDiscountPercent"/>" />
							</td>
							<td class="amountAlign"><s:text name="inv.detail.AMT"/></td>
							<td>
								<input  id="additionalDiscountAmount" 
										type="text" style="width:100%;border:1px;"
										name="additionalDiscountAmount" <s:if test="invoice.header.invTotalBeforeAdditional != 0">class="discount"</s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:11,constraints : {pattern: '###0.00'}"
										onblur="changeAdditionalAmount();" 
										value="<s:property value="invoice.header.additionalDiscountAmount"/>" />
							</td>
						</tr>
						<tr>
							<td colspan="6" class="amountAlign"><s:text name="inv.detail.TOTAL.BEFORE.GST"/></td>
							<td class="amountAlign" id="invAmountNoVat"><s:property value="invoice.header.invAmountNoVat"/></td>
						</tr>
						<tr>
							<td colspan="5" class="amountAlign"><s:text name="inv.detail.GST"/></td>
							<td class="amountAlign" id="vatRate" ><s:property value="invoice.header.vatRate"/>%</td>
							<td class="amountAlign" id="vatAmount" ><s:property value="invoice.header.vatAmount"/></td>
						</tr>
						<tr>
							<td colspan="6" class="amountAlign"><s:text name="inv.detail.TOTAL.BEFORE.CASH"/></td>
							<td class="amountAlign" id="invAmountWithVat"><s:property value="invoice.header.invAmountWithVat"/></td>
						</tr>
						<tr>
							<td colspan="3" class="amountAlign"><s:text name="inv.detail.CASH"/></td>
							<td class="amountAlign"><s:text name="inv.detail.PERCENT"/></td>
							<td>
								<input  id="cashDiscountPercent" 
										type="text" style="width:100%;border:1px;"
										name="cashDiscountPercent" <s:if test="invoice.header.invTotalBeforeAdditional != 0">class="discountCash"</s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:5,constraints : {pattern: '###0.00'}"
										onblur="changeCashPercent();" 
										value="<s:property value="invoice.header.cashDiscountPercent"/>" />
							</td>
							<td class="amountAlign"><s:text name="inv.detail.AMT"/></td>
							<td>
								<input  id="cashDiscountAmount" 
										type="text" style="width:100%;border:1px;"
										name="cashDiscountAmount" <s:if test="invoice.header.invTotalBeforeAdditional != 0">class="discountCash"</s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:11,constraints : {pattern: '###0.00'}"
										onblur="changeCashAmount();" 
										value="<s:property value="invoice.header.cashDiscountAmount"/>" />
							</td>
						</tr>
						<tr>
							<td colspan="6" class="amountAlign"><s:text name="inv.detail.PLEASE.PAY"/></td>
							<td class="amountAlign" id="totalPay"><s:property value="invoice.header.totalPay"/></td>
						</tr>
					</tbody>
				</table>

       		</div>
       	</div>
	</div>
</body>
</html>
