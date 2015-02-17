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
        			registry.byId("deliveryDate").set('displayedValue','<s:property value="invoice.header.deliveryDate"/>');
        			header["deliveryDate"]=deliveryDate;
        			
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
                        	if (objName == "focQty")
                        	{
                        		var poQty = jsonStore.getValue(item,"poFocQty");
                        		var focQtylessThanPO = getRuleBuyerRuleId(rules,"FocQtylessThanPO");
                        		console.log("focQtylessThanPO :", focQtylessThanPO);
                        		if (!validateNumber(objValue)) 
                        		{
                        			alert('<s:text name="B2BPC1634" />');
                        			dojo.byId("focQty" + lineSeqNo).value=parseFloat(value).toFixed(2);
                        			return;
                            	}
                        		if (!validateNumber(objValue) || objValue < 0 || (focQtylessThanPO&&(objValue-poQty) > 0)) 
                        		{
                        			alert('<s:text name="B2BPC1611" /> '+poQty+'.');
                        			dojo.byId("focQty" + lineSeqNo).value=parseFloat(value).toFixed(2);
                        			return;
                            	}
                            	
                        		jsonStore.setValue(item, objName, objValue);
                        		dojo.byId("focQty" + lineSeqNo).value=parseFloat(objValue).toFixed(2);
                            	return;
                            }
                            

                        	var qty,unitPrice;
                        	var discountPercent,discountAmount;
							if (objName == "invQty")
							{
								var poQty = jsonStore.getValue(item,"poQty");
								var qtylessThanPO = getRuleBuyerRuleId(rules,"QtylessThanPO");
								console.log("qtylessThanPO :", qtylessThanPO);
								if (!validateNumber(objValue))
								{
									alert('<s:text name="B2BPC1633" />');
									dojo.byId("invQty" + lineSeqNo).value=parseFloat(value).toFixed(2);
									return;
								}
								if (objValue < 0 || (qtylessThanPO && (objValue-poQty) > 0))
								{
									alert('<s:text name="B2BPC1610" /> '+poQty+'.');
									dojo.byId("invQty" + lineSeqNo).value=parseFloat(value).toFixed(2);
									return;
								}

								unitPrice = jsonStore.getValue(item,"unitPrice");
								qty = parseFloat(objValue).toFixed(2);
								var itemAmount = parseFloat(unitPrice) * parseFloat(qty)
								
								if (parseFloat(itemAmount) != 0)
								{
									discountAmount = jsonStore.getValue(item,"discountAmount");
									discountPercent = parseFloat(parseFloat(discountAmount) / itemAmount * 100).toFixed(2);
								}
								else
								{
									discountAmount = 0;
									discountPercent = 0;
								}
								dojo.byId("invQty" + lineSeqNo).value=parseFloat(objValue).toFixed(2);
							}

							if (objName == "unitPrice")
							{
								var poUnitPrice = jsonStore.getValue(item,"poUnitPrice");
								var unitPriceLessThanPO = getRuleBuyerRuleId(rules,"UnitPriceLessThanPO");
								console.log("unitPriceLessThanPO :", unitPriceLessThanPO);
								if (!validateNumber(objValue))
								{
									alert('<s:text name="B2BPC1635" />');
									dojo.byId("unitPrice" + lineSeqNo).value=parseFloat(value).toFixed(2);
									return;
								}
								if (objValue < 0 || (unitPriceLessThanPO&&(objValue-poUnitPrice) > 0))
								{
									alert('<s:text name="B2BPC1609" /> '+poUnitPrice+'.');
									dojo.byId("unitPrice" + lineSeqNo).value=parseFloat(value).toFixed(2);
									return;
								}

								qty = jsonStore.getValue(item,"invQty");
								unitPrice = parseFloat(objValue).toFixed(2);
								var itemAmount = parseFloat(unitPrice) * parseFloat(qty);
								
								if (parseFloat(itemAmount) != 0)
								{
									discountAmount = jsonStore.getValue(item,"discountAmount");
									discountPercent = parseFloat(parseFloat(discountAmount) / itemAmount * 100).toFixed(2);
								}
								else
								{
									discountAmount = 0;
									discountPercent = 0;
								}
								dojo.byId("unitPrice" + lineSeqNo).value=parseFloat(objValue).toFixed(2);
							}

							if (objName == "discountPercent")
							{
								if (jsonStore.getValue(item,"itemAmount") == null || jsonStore.getValue(item,"itemAmount") == 0)
								{
									return;
								}
								if (!validateNumber(objValue) || objValue < 0 || parseFloat(objValue) >= 100)
								{
									alert('<s:text name="B2BPC1638" />');
									dojo.byId("discountPercent" + lineSeqNo).value=parseFloat(value).toFixed(2);
									return;
								}

								qty = jsonStore.getValue(item,"invQty");
								unitPrice = jsonStore.getValue(item,"unitPrice");
								discountAmount = (jsonStore.getValue(item,"itemAmount") * parseFloat(objValue) / 100).toFixed(2);
								discountPercent = parseFloat(objValue).toFixed(2);
								dojo.byId("discountPercent" + lineSeqNo).value=parseFloat(objValue).toFixed(2);
							}


							if (objName == "discountAmount")
							{
								if (jsonStore.getValue(item,"itemAmount") == null || jsonStore.getValue(item,"itemAmount") == 0)
								{
									return;
								}
								
								if (!validateNumber(objValue) || objValue < 0)
								{
									alert('<s:text name="B2BPC1637" />');
									dojo.byId("discountAmount" + lineSeqNo).value=parseFloat(value).toFixed(2);
									return;
								}

								if (parseFloat(objValue) >= jsonStore.getValue(item,"itemAmount"))
								{
									alert('<s:text name="B2BPC1643" />');
									dojo.byId("discountAmount" + lineSeqNo).value=parseFloat(value).toFixed(2);
									return;
								}

								qty = jsonStore.getValue(item,"invQty");
								unitPrice = jsonStore.getValue(item,"unitPrice");
								discountPercent = (parseFloat(objValue) / (jsonStore.getValue(item,"itemAmount")) * 100).toFixed(2);
								discountAmount = parseFloat(objValue).toFixed(2);
								dojo.byId("discountAmount" + lineSeqNo).value=parseFloat(objValue).toFixed(2);
							}
							
							
							jsonStore.setValue(item, objName, parseFloat(objValue).toFixed(2));
							jsonStore.setValue(item, "discountAmount", discountAmount);
							jsonStore.setValue(item, "discountPercent", discountPercent);

							dojo.byId("discountPercent"+lineSeqNo).value=parseFloat(discountPercent).toFixed(2);
							dojo.byId("discountAmount"+lineSeqNo).value=parseFloat(discountAmount).toFixed(2);
							
							var oldAmount = jsonStore.getValue(item,"itemAmount");
                        	var oldNetAmount = jsonStore.getValue(item, "netAmount");
                        	//var discountAmout = jsonStore.getValue(item,"discountAmount");
                        	var oldInvAmountNoVat = header.invAmountNoVat;
                        	var vatRate = header.vatRate;
                        	console.log("unitPrice : " + unitPrice);
							console.log("invQty : " + qty);
							var amount = (unitPrice * qty).toFixed(2);
							var netAmount = (amount - discountAmount).toFixed(2);
							jsonStore.setValue(item, "itemAmount", amount);
							jsonStore.setValue(item, "netAmount", netAmount);

                        	dojo.byId("itemAmount"+lineSeqNo).innerHTML=amount;
							dojo.byId("netAmount"+lineSeqNo).innerHTML=netAmount;

							var change = (netAmount - oldNetAmount).toFixed(2);
							
							var invTotalBeforeAdditional = (parseFloat(header.invTotalBeforeAdditional) + parseFloat(change)).toFixed(2);
							header.invTotalBeforeAdditional = invTotalBeforeAdditional;
							dojo.byId("totalBeforeTrade").innerHTML=invTotalBeforeAdditional;

							var invAmountNoVat = (parseFloat(oldInvAmountNoVat) + parseFloat(change)).toFixed(2);
							header.invAmountNoVat = invAmountNoVat;
							dojo.byId("invAmountNoVat").innerHTML=invAmountNoVat;

							var vatAmount = (invAmountNoVat * (header.vatRate/100)).toFixed(2);
							header.vatAmount = vatAmount;
							dojo.byId("vatAmount").innerHTML=vatAmount;

							var invAmountWithVat = (parseFloat(header.invAmountNoVat) + parseFloat(vatAmount)).toFixed(2);
							header.invAmountWithVat = invAmountWithVat;
							dojo.byId("invAmountWithVat").innerHTML=invAmountWithVat;

							var totalPay = (parseFloat(header.invAmountWithVat) - parseFloat(header.cashDiscountAmount)).toFixed(2);
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
						 var invNoVatAmt =parseFloat(header.invAmountNoVat) / (1 - parseFloat(header.additionalDiscountPercent)/100)
	                     header.additionalDiscountAmount = parseFloat(invNoVatAmt) * parseFloat(parseFloat(objValue)/100);
	                     header.additionalDiscountPercent = parseFloat(objValue).toFixed(2);
	                     header.invAmountNoVat = (parseFloat(invNoVatAmt) - parseFloat(header.additionalDiscountAmount)).toFixed(2);
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
	                     header.totalPay = (parseFloat(header.invAmountWithVat) + parseFloat(objValue)).toFixed(2);
	                     header.cashDiscountAmount = parseFloat(objValue).toFixed(2);
	                     dojo.byId("totalPay").innerHTML=header.totalPay;

	                     var cashPercent = (parseFloat(objValue).toFixed(2))/(parseFloat(header.invAmountWithVat))*100;
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
	                     header.cashDiscountAmount = (parseFloat(header.invAmountWithVat) * parseFloat(parseFloat(objValue).toFixed(2)/100)).toFixed(2);
	                     header.cashDiscountPercent = parseFloat(objValue).toFixed(2);
	                     header.totalPay = (parseFloat(totalPay) - parseFloat(header.cashDiscountAmount)).toFixed(2);
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
                            save('<s:url value="/po/save.action"/>');
                        });
                    }

                    if (dom.byId("saveAndSentBtn"))
                    {
                    	on(registry.byId("saveAndSentBtn"), 'click', function()
                        {
                    		var confirmDialog = new ConfirmDialog({
                                message: '<s:text name="B2BPC1646" />',
                                yesBtnPressed: function(){
                                	save('<s:url value="/po/saveAndSent.action"/>');
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
                                	changeToURL('<s:url value="/po/init.action" />?keepSp=Y');
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
                        	var store = '<s:property value="store"/>';
                        	var docOid = '<s:property value="docOid"/>';
                        	var invNo='<s:property value="invNo"/>';
                        	var storeCount='<s:property value="storeCount"/>';
                        	var poExpiredMsg = '<s:property value="poExpiredMsg"/>';
                        	changeToURL('<s:url value="/po/generateInvForSor.action" />?docOid=' + docOid + "&store=" + store + "&invNo="+invNo +"&storeCount=" + storeCount + "&poExpiredMsg="+poExpiredMsg);
                		}); 
                    }

                    if (dom.byId("cancelBtn"))
                    {
                    	on(registry.byId("cancelBtn"), 'click', function()
                    	{
                        	var store = '<s:property value="store"/>';
                        	var docOid = '<s:property value="docOid"/>';
                        	var storeCount='<s:property value="storeCount"/>';
                        	var poExpiredMsg = '<s:property value="poExpiredMsg"/>';
                        	if (storeCount > 1)
                        	{
                        		changeToURL('<s:url value="/po/initGenerateInv.action" />?docOid=' + docOid + "&store=" + store + "&poExpiredMsg="+poExpiredMsg);
                        		return;
                            }

                        	changeToURL('<s:url value="/po/init.action" />?keepSp=Y');
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
			var rule1 = getRuleBuyerRuleId(rules,"QtyEditable");
			if (rule1)
			{
				refreshDetail(".invQty",domStyle)
			}

			var rule2 = getRuleBuyerRuleId(rules,"FocQtyEditable");
			if (rule2)
			{
				refreshDetail(".focQty",domStyle)
			}

			var rule3 = getRuleBuyerRuleId(rules,"UnitPriceEditable");
			if (rule3)
			{
				refreshDetail(".unitPrice",domStyle)
			}
			var rule4 = getRuleBuyerRuleId(rules,"DiscountEditable");
			if (rule4)
			{
				refreshDetail(".discount",domStyle);
			}
			var rule5 = getRuleBuyerRuleId(rules,"DiscountForDetailEditable");
			if (rule5)
			{
				refreshDetail(".discountDetail",domStyle);
			}
			var rule6 = getRuleBuyerRuleId(rules,"CashDiscountEditable");
			if (rule6)
			{
				refreshDetail(".discountCash",domStyle);
			}
			var rule7 = <s:property value="ggDisableInvoicePaymentInstructions" />
			if (rule7)
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
								<input id="deliveryDate" type="text" 
									name="invoice.header.deliveryDate"
									onchange="changeInvHeaderDate(this)" 
									data-dojo-type="custom/CustomDateTextBox" 
									data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
        							value='<s:property value="invoice.header.deliveryDate"/>'/>
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
							<th width="6%"><s:text name="inv.detail.FOCQTY"/></th>
							<th width="3%"><s:text name="inv.detail.UOM"/></th>
							<th width="8%"><s:text name="inv.detail.UNITCOST"/></th>
							<th width="7%"><s:text name="inv.detail.AMOUNT"/></th>
							<th width="7%"><s:text name="inv.detail.DISCOUNTPERCENT"/></th>
							<th width="6%"><s:text name="inv.detail.TRADEDISCOUNT"/></th>
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
								<input id="invQty<s:property value="#detail.lineSeqNo"/>" 
									type="text" style="width:100%;border:1px;"
									name="invQty" class="invQty"
									data-dojo-type="dijit/form/NumberTextBox" readOnly="readOnly" data-dojo-props="maxLength:6,constraints : {pattern: '###0.00'}"
									onblur="changeInvDetails(this,'<s:property value="#detail.lineSeqNo"/>');" 
									value="<s:property value="#detail.invQty"/>" />
							</td>
							<td class="amountAlign">
								<input id="focQty<s:property value="#stat.index+1"/>" 
									type="text" style="width:100%;border:1px;" 
									name="focQty" <s:if test="#detail.itemAmount != 0">class="focQty"</s:if>
									data-dojo-type="dijit/form/NumberTextBox" readOnly="readOnly" data-dojo-props="maxLength:6,constraints : {pattern: '###0.00'}"
									onblur="changeInvDetails(this,'<s:property value="#detail.lineSeqNo"/>')" 
									value="<s:property value="#detail.focQty"/>" />
							</td>
							<td><s:property value="#detail.invUom"/></td>
							<td class="amountAlign">
								<input id="unitPrice<s:property value="#detail.lineSeqNo"/>" 
									type="text" style="width:100%;border:1px;"
									name="unitPrice" <s:if test="#detail.itemAmount != 0">class="unitPrice" </s:if>
									data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:11,constraints : {pattern: '###0.00'}"
									onblur="changeInvDetails(this,'<s:property value="#detail.lineSeqNo"/>');" 
									value="<s:property value="#detail.unitPrice"/>" />
							</td>
							<td class="amountAlign"  id="itemAmount<s:property value="#detail.lineSeqNo"/>" ><s:property value="#detail.itemAmount"/></td>
							<td>
								<input id="discountPercent<s:property value="#detail.lineSeqNo"/>" 
										type="text" style="width:100%;border:1px;"
										name="discountPercent" <s:if test="#detail.itemAmount != 0">class="discountDetail" </s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="constraints : {pattern: '###0.00'}"
										onblur="changeInvDetails(this,'<s:property value="#detail.lineSeqNo"/>');" 
										value="<s:property value="#detail.discountPercent"/>" />
							</td>
							<td class="amountAlign">
								<input id="discountAmount<s:property value="#detail.lineSeqNo"/>" 
										type="text" style="width:100%;border:1px;"
										name="discountAmount" <s:if test="#detail.itemAmount != 0">class="discountDetail"</s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:11,constraints : {pattern: '###0.00'}"
										onblur="changeInvDetails(this,'<s:property value="#detail.lineSeqNo"/>');" 
										value="<s:property value="#detail.discountAmount"/>" />
							</td>
							<td class="amountAlign"  id="netAmount<s:property value="#detail.lineSeqNo"/>" ><s:property value="#detail.netAmount"/></td>
						</tr>	
						</s:iterator>
						<tr>
							<td colspan="7" rowspan="7">&nbsp;</td>
							<td colspan="5" class="amountAlign"><s:text name="inv.detail.TOTAL.BEFORE.TRADE"/></td>
							<td class="amountAlign" id="totalBeforeTrade"><s:property value="invoice.header.invTotalBeforeAdditional"/></td>
						</tr>
						<tr>
							<td colspan="2" class="amountAlign"><s:text name="inv.detail.ADDITIONAL"/></td>
							<td class="amountAlign"><s:text name="inv.detail.PERCENT"/></td>
							<td>
								<input id="additionalDiscountPercent" 
										type="text" style="width:100%;border:1px;"
										name="additionalDiscountPercent" <s:if test="invoice.header.invTotalBeforeAdditional != 0">class="discount"</s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:5,constraints : {pattern: '###0.00'}"
										onblur="changeAdditionalPercent();" 
										value="<s:property value="invoice.header.additionalDiscountPercent"/>" />
							</td>
							<td class="amountAlign"><s:text name="inv.detail.AMT"/></td>
							<td>
								<input id="additionalDiscountAmount" 
										type="text" style="width:100%;border:1px;"
										name="additionalDiscountAmount" <s:if test="invoice.header.invTotalBeforeAdditional != 0">class="discount"</s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:11,constraints : {pattern: '###0.00'}"
										onblur="changeAdditionalAmount();" 
										value="<s:property value="invoice.header.additionalDiscountAmount"/>" />
							</td>
						</tr>
						<tr>
							<td colspan="5" class="amountAlign"><s:text name="inv.detail.TOTAL.BEFORE.GST"/></td>
							<td class="amountAlign" id="invAmountNoVat"><s:property value="invoice.header.invAmountNoVat"/></td>
						</tr>
						<tr>
							<td colspan="4" class="amountAlign"><s:text name="inv.detail.GST"/></td>
							<td class="amountAlign" id="vatRate" ><s:property value="invoice.header.vatRate"/>%</td>
							<td class="amountAlign" id="vatAmount" ><s:property value="invoice.header.vatAmount"/></td>
						</tr>
						<tr>
							<td colspan="5" class="amountAlign"><s:text name="inv.detail.TOTAL.BEFORE.CASH"/></td>
							<td class="amountAlign" id="invAmountWithVat"><s:property value="invoice.header.invAmountWithVat"/></td>
						</tr>
						<tr>
							<td colspan="2" class="amountAlign"><s:text name="inv.detail.CASH"/></td>
							<td class="amountAlign"><s:text name="inv.detail.PERCENT"/></td>
							<td>
								<input id="cashDiscountPercent" 
										type="text" style="width:100%;border:1px;"
										name="cashDiscountPercent" <s:if test="invoice.header.invTotalBeforeAdditional != 0">class="discountCash"</s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:5,constraints : {pattern: '###0.00'}"
										onblur="changeCashPercent();" 
										value="<s:property value="invoice.header.cashDiscountPercent"/>" />
							</td>
							<td class="amountAlign"><s:text name="inv.detail.AMT"/></td>
							<td>
								<input id="cashDiscountAmount" 
										type="text" style="width:100%;border:1px;"
										name="cashDiscountAmount" <s:if test="invoice.header.invTotalBeforeAdditional != 0">class="discountCash"</s:if>
										data-dojo-type="dijit/form/NumberTextBox"  readOnly="readOnly" data-dojo-props="maxLength:11,constraints : {pattern: '###0.00'}"
										onblur="changeCashAmount();" 
										value="<s:property value="invoice.header.cashDiscountAmount"/>" />
							</td>
						</tr>
						<tr>
							<td colspan="5" class="amountAlign"><s:text name="inv.detail.PLEASE.PAY"/></td>
							<td class="amountAlign" id="totalPay"><s:property value="invoice.header.totalPay"/></td>
						</tr>
					</tbody>
				</table>

       		</div>
       	</div>
	</div>
</body>
</html>
