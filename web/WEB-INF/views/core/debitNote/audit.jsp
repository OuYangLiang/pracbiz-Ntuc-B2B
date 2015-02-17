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
	                    if (objName == "priceStatus")
	                    {
	                    	var accept = document.getElementById('accept-' + lineSeqNo);
	                    	var reject = document.getElementById('reject-' + lineSeqNo);
	                    	if (accept.checked)
	                    	{
	                    		jsonStore.setValue(item, objName, 'ACCEPTED');
	                    	}
	                    	else if (reject.checked)
	                    	{
	                    		jsonStore.setValue(item, objName, 'REJECTED');
	                    	}
	                    	else
	                    	{
	                    		jsonStore.setValue(item, objName, 'PENDING');
	                    	}
	                    }
	                    else if (objName == "priceStatusActionRemarks")
	                    {
	                    	jsonStore.setValue(item, objName, objValue);
	                    }
	                    else if (objName == "qtyStatus")
	                    {
	                    	var accept = document.getElementById('accept-' + lineSeqNo);
	                    	var reject = document.getElementById('reject-' + lineSeqNo);
	                    	if (accept.checked)
	                    	{
	                    		jsonStore.setValue(item, objName, 'ACCEPTED');
	                    	}
	                    	else if (reject.checked)
	                    	{
	                    		jsonStore.setValue(item, objName, 'REJECTED');
	                    	}
	                    	else
	                    	{
	                    		jsonStore.setValue(item, objName, 'PENDING');
	                    	}
	                    }
	                    else if (objName == "qtyStatusActionRemarks")
	                    {
	                    	jsonStore.setValue(item, objName, objValue);
	                    }
                    }});
			    } 
			    
                if(dom.byId("saveBtn"))
			    {
				    on(registry.byId("saveBtn"), 'click', function()
				    {	
						var headerJson = JSON.stringify(header);
                        var detailJson = JSON.stringify(getDetailJsonData(jsonStore));
                        var url = '';
                        var actionType = '<s:property value="#session.actionType"/>';
                        if (actionType == 'price')
                        {
                            url = '<s:url value="/dn/saveAuditPrice.action" />';
                        }
                        else if (actionType == 'qty')
                        {
                            url = '<s:url value="/dn/saveAuditQty.action" />';
                        }
                        var csrfToken = dom.byId("csrfToken").value;
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
                		  var url = '';
                          var actionType = '<s:property value="#session.actionType"/>';
                          if (actionType == 'price')
                          {
                              url = '<s:url value="/dn/initAuditPrice.action" />?actionType=price';
                          }
                          else if (actionType == 'qty')
                          {
                              url = '<s:url value="/dn/initAuditQty.action" />?actionType=qty';
                          }
                          changeToURL(url + '&docOid=<s:property value="dn.dnHeader.dnOid"/>');
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
                
                
                hideAndShow = function()
                {
                    var value=document.getElementById("hideShow").value;
                    var trs = document.getElementsByTagName("tr");
                    if (value=="hide")
                    {
                        document.getElementById("hideShow").value="show";
                        document.getElementById("hideShow").innerHTML='<s:text name="button.show.item"/>';
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
                        document.getElementById("hideShow").value="hide";
                        document.getElementById("hideShow").innerHTML='<s:text name="button.hideDispute.item"/>';
                        for (var i = 0; i < trs.length; i++)
                        {
                            if(startWith(trs[i].id, "hide"))
                            {
                                trs[i].style.display="";
                            }
                        }
                    }
                }
                
                
                startWith = function (source, str)
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
       		    <button data-dojo-type="dijit.form.Button" id="hideShow" onclick="hideAndShow();" value="show"><s:text name="button.show.item"/></button>
       		    <s:if test="dn.calculateCountOfPrivilege() > 0">
	    		   <button data-dojo-type="dijit/form/Button" id="saveBtn" ><s:text name="button.save"/></button>
		    	   <button data-dojo-type="dijit/form/Button" id="restBtn" ><s:text name="button.reset"/></button>
       		    </s:if>
	    		<button data-dojo-type="dijit/form/Button" id="cancelBtn" ><s:text name="button.cancel"/></button>
	    	</div>
       	</div>
       	<!-- here is message area -->
    	<div id="errorMsg">
		</div>
		<div>
    		<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
   		</div>
       	<!-- dispute Part -->
       	<div data-dojo-type="dijit.TitlePane" 
       		data-dojo-props="title:'<s:text name="dn.audit"/>', width:275"  style="width:100%;">
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
       		data-dojo-props="title:'<s:text name="dn.audit.detail"/>', width:275" style="width:100%;">
       		<div style="width:100%">
       			<table class="tablestyle1" style="table-layout: fixed;">
					<thead>
						<tr>
							<th style="text-align: center;" width="2%"><s:text name="dn.edit.detail.lineSeqNo"/></th>
							<th style="text-align: center;" width="6%"><s:text name="dn.edit.detail.buyerItemCode"/></th>
							<th style="text-align: center;" width="8%"><s:text name="dn.edit.detail.itemDesc"/></th>
							<th style="text-align: center;" width="9%"><s:text name="dn.edit.detail.barcode"/></th>
							<s:if test="#session.actionType == 'price'">
								<th style="text-align: center;" width="5%"><s:text name="dn.edit.detail.classCode"/></th>
								<th style="text-align: center;" width="5%"><s:text name="dn.edit.detail.subclassCode"/></th>
								<th style="text-align: center;" width="5%"><s:text name="dn.edit.detail.price"/></th>
								<th style="text-align: center;" width="5%"><s:text name="dn.edit.detail.disputePrice"/></th>
								<th style="text-align: center;" width="15%"><s:text name="dn.edit.detail.priceRemark"/></th>
							</s:if>
							<s:elseif test="#session.actionType == 'qty'">
								<th style="text-align: center;" width="5%"><s:text name="dn.edit.detail.qty"/></th>
								<th style="text-align: center;" width="5%"><s:text name="dn.edit.detail.disputeQty"/></th>
								<th style="text-align: center;" width="15%"><s:text name="dn.edit.detail.qtyRemark"/></th>
							</s:elseif>
							<s:if test="dn.calculateCountOfPrivilege() > 0">
								<th style="text-align: center;" width="7%"><s:text name="dn.edit.detail.accept"/></th>
	                            <th style="text-align: center;" width="7%"><s:text name="dn.edit.detail.reject"/></th>
							</s:if>
                            <th style="text-align: center;" width="16%"><s:text name="dn.edit.detail.remark"/></th>      
						</tr>
					</thead>
					<tbody>
						<s:iterator value="dn.dnDetail" id="detail" status="stat">
						<s:if test="#detail.getShowOnInit()">
						<tr id="show<s:property value="#status.index" />" height="60">
						</s:if>
						<s:else>
						<tr id="hide<s:property value="#status.index" />" style="display: none;" height="60">
						</s:else>
							<td style="text-align: center;"><s:property value="#stat.index+1"/></td>
							<td style="text-align: center;word-wrap:   break-word"><s:property value="#detail.buyerItemCode"/></td>
							<td style="word-wrap:   break-word"><s:property value="#detail.itemDesc"/></td>
							<td style="text-align: center;word-wrap:   break-word"><s:property value="#detail.barcode"/></td>
							
							<s:if test="#session.actionType == 'price'">
						       <td style="text-align: center;"><s:property value="#detail.classCode"/></td>
						       <td style="text-align: center;"><s:property value="#detail.subclassCode"/></td>
						       <s:if test='dn.dnHeader.dispute && #detail.isDisputePriceChanged()'>
								   <td style="text-align: center;color:red"><s:property value="#detail.unitCost"/></td>
								   <td style="text-align: center;color:red">
										<s:property value="#detail.disputePrice"/>
								   </td>
						       </s:if>
						       <s:else>
								   <td style="text-align: center;"><s:property value="#detail.unitCost"/></td>
								   <td style="text-align: center;">
								   </td>
						       </s:else>
							   <td style="word-wrap:   break-word">
								    <s:property value="#detail.disputePriceRemarks"/>
							   </td>
								<s:if test="#detail.getPrivileged()">
								    <s:if test="#detail.priceStatus.name() == 'ACCEPTED'">
										<td style="text-align: center"><input name="priceStatus" type="checkbox" id="accept-<s:property value="#detail.lineSeqNo" />" 
											name="saveCheckBox" data-dojo-type="dijit/form/CheckBox"  checked="checked"
											onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
			                            <td style="text-align: center"><input name="priceStatus" type="checkbox" id="reject-<s:property value="#detail.lineSeqNo" />" 
				                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
				                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
								    </s:if>
								    <s:elseif test="#detail.priceStatus.name() == 'REJECTED'">
										<td style="text-align: center"><input name="priceStatus" type="checkbox" id="accept-<s:property value="#detail.lineSeqNo" />" 
											name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
											onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
			                            <td style="text-align: center"><input name="priceStatus" type="checkbox" id="reject-<s:property value="#detail.lineSeqNo" />" 
				                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" checked="checked" 
				                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
								    </s:elseif>
								    <s:else>
								        <td style="text-align: center"><input name="priceStatus" type="checkbox" id="accept-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                        <td style="text-align: center"><input name="priceStatus" type="checkbox" id="reject-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
								    </s:else>
		                            <td style="text-align: center;word-wrap:   break-word"><textarea name="priceStatusActionRemarks" onblur="changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');" 
		                                id="remarks-<s:property value="#detail.lineSeqNo" />" data-dojo-type="dijit/form/Textarea" 
		                                maxlength="255"><s:property value="#detail.priceStatusActionRemarks"/></textarea></td>
								</s:if>
								<s:else>
								   <s:if test="dn.calculateCountOfPrivilege() > 0">
								    <s:if test="#detail.priceStatus.name() == 'ACCEPTED'">
                                        <td style="text-align: center"><input name="priceStatus" disabled="disabled" type="checkbox" id="accept-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox"  checked="checked"
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                        <td style="text-align: center"><input name="priceStatus" disabled="disabled" type="checkbox" id="reject-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                    </s:if>
                                    <s:elseif test="#detail.priceStatus.name() == 'REJECTED'">
                                        <td style="text-align: center"><input name="priceStatus" disabled="disabled" type="checkbox" id="accept-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                        <td style="text-align: center"><input name="priceStatus" disabled="disabled" type="checkbox" id="reject-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" checked="checked" 
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                    </s:elseif>
                                    <s:else>
                                        <td style="text-align: center"><input name="priceStatus" disabled="disabled" type="checkbox" id="accept-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                        <td style="text-align: center"><input name="priceStatus" disabled="disabled" type="checkbox" id="reject-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                    </s:else>
									</s:if>
								    <td style="word-wrap:   break-word"><s:property value="#detail.priceStatusActionRemarks"/></td>
								</s:else>
							</s:if>
							
							<s:if test="#session.actionType == 'qty'">
							    <s:if test='dn.dnHeader.dispute && #detail.isDisputeQtyChanged()'>
								    <td style="text-align: center;color:red"><s:property value="#detail.debitQty"/></td>
	                                <td style="text-align: center;color:red">
								        <s:property value="#detail.disputeQty"/>
	                                </td>
							    </s:if>
							    <s:else>
								    <td style="text-align: center;"><s:property value="#detail.debitQty"/></td>
	                                <td style="text-align: center;">
	                                </td>
							    </s:else>
                                <td style="word-wrap:   break-word">
                                    <s:property value="#detail.disputeQtyRemarks"/>
                                </td>
								<s:if test="#detail.getPrivileged()">
								    <s:if test="#detail.qtyStatus.name() == 'ACCEPTED'">
										<td style="text-align: center" ><input name="qtyStatus" type="checkbox" id="accept-<s:property value="#detail.lineSeqNo" />" 
	                                        name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" checked="checked"
	                                        onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
	                                    <td style="text-align: center"><input name="qtyStatus" type="checkbox" id="reject-<s:property value="#detail.lineSeqNo" />" 
	                                        name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
	                                        onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
								    </s:if>
								    <s:elseif test="#detail.qtyStatus.name() == 'REJECTED'">
										<td style="text-align: center" ><input name="qtyStatus" type="checkbox" id="accept-<s:property value="#detail.lineSeqNo" />" 
	                                        name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
	                                        onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
	                                    <td style="text-align: center"><input name="qtyStatus" type="checkbox" id="reject-<s:property value="#detail.lineSeqNo" />" 
	                                        name="saveCheckBox" data-dojo-type="dijit/form/CheckBox"  checked="checked"
	                                        onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
								    </s:elseif>
								    <s:else>
								        <td style="text-align: center" ><input name="qtyStatus" type="checkbox" id="accept-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                        <td style="text-align: center"><input name="qtyStatus" type="checkbox" id="reject-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
								    </s:else>
		                            <td style="text-align: center;word-wrap:   break-word"><textarea name="qtyStatusActionRemarks" onblur="changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');" 
		                                id="remarks-<s:property value="#detail.lineSeqNo" />" data-dojo-type="dijit/form/Textarea" 
		                                maxlength="255"><s:property value="#detail.qtyStatusActionRemarks"/></textarea></td>
								</s:if>
								<s:else>
								    <s:if test="dn.calculateCountOfPrivilege() > 0">
								    <s:if test="#detail.qtyStatus.name() == 'ACCEPTED'">
                                        <td style="text-align: center" ><input name="qtyStatus" disabled="disabled" type="checkbox" id="accept-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" checked="checked"
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                        <td style="text-align: center"><input name="qtyStatus" disabled="disabled" type="checkbox" id="reject-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox"  checked="<s:property value=''/>"
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                    </s:if>
                                    <s:elseif test="#detail.qtyStatus.name() == 'REJECTED'">
                                        <td style="text-align: center" ><input name="qtyStatus" disabled="disabled" type="checkbox" id="accept-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                        <td style="text-align: center"><input name="qtyStatus" disabled="disabled" type="checkbox" id="reject-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox"  checked="checked"
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                    </s:elseif>
                                    <s:else>
                                        <td style="text-align: center" ><input name="qtyStatus" disabled="disabled" type="checkbox" id="accept-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                        <td style="text-align: center"><input name="qtyStatus" disabled="disabled" type="checkbox" id="reject-<s:property value="#detail.lineSeqNo" />" 
                                            name="saveCheckBox" data-dojo-type="dijit/form/CheckBox" 
                                            onclick="selectOne(this);changeDetail(this,'<s:property value="#detail.lineSeqNo"/>');"/></td>
                                    </s:else>
                                    </s:if>
								    <td style="word-wrap:   break-word"><s:property value="#detail.qtyStatusActionRemarks"/></td>
								</s:else>
							</s:if>
						</tr>	
						</s:iterator>
					</tbody>
				</table>
       		</div>
       	</div>
	</div>
</body>
</html>
