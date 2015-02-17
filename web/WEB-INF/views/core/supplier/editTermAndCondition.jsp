<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <style type="text/css">
    
    </style>
        
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dijit/registry",
                "dojo/on",
                "dojo/parser",
                "dijit/form/ValidationTextBox",
                "dijit/form/RadioButton",
                "custom/ConfirmDialog",
                "dijit/form/CheckBox",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    parser,
                    ValidationTextBox,
                    RadioButton,
                    ConfirmDialog,
                    CheckBox
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                    
                    
                    on(registry.byId("saveBtn"), 'click', 
                        function()
                        {
                            submitForm('mainForm', '<s:url value="/supplier/saveEditTermAndCondition.action" />'+'?supplier.supplierOid=<s:property value="supplier.supplierOid"/>');
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/supplier/initEditTermAndCondition.action" />'+'?supplier.supplierOid=<s:property value="supplier.supplierOid"/>');
                        }
                    );
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                        	changeToURL('<s:url value="/supplier/init.action?keepSp=Y" />');
                        }
                    });
                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                    		confirmDialog.show();
                        }
                    );
                    
                    on(registry.byId("addBtn"), 'click', 
                        function()
                        {
                    		add(true);
                        }
                    );
                    
                });

        var index = <s:property value='termConditions.size' />;
		function add(flag)
		{
			var sourceTable = document.getElementById("sourceTable");
			var allTbody = sourceTable.getElementsByTagName("tbody");
            var div = document.getElementById("targetDiv");
            var div1 = document.createElement("div");
            div1.id="itemDiv"+index;
			var s = '<table width="100%"><tbody id="tbody'+index+'">'
						   +'<tr>'
								+'<td width="2px"><span class="required">*</span> </td>'
					  				+'<td width="30%">&nbsp;&nbsp;<s:text name="supplier.edit.termAndCondition.code"/></td>'
							    +'<td>:</td>'
								+'<td><input name="termConditions['+index+'].termConditionCode" data-dojo-type="dijit.form.ValidationTextBox"  maxlength="20"/>'
								+'&nbsp;&nbsp;<button onclick="remove('+index+');" data-dojo-type="dijit.form.Button" name="termConditions['+index+'].button" ><s:text name="button.remove" /></button></td>'
							+'</tr>'
							+'<tr>'
								+'<td width="2px"><span class="required">*</span> </td>'
						       				+'<td>&nbsp;&nbsp;<s:text name="supplier.edit.termAndCondition.desc"/></td>'
							    +'<td>:</td>'
								+'<td><input name="termConditions['+index+'].termCondition1" data-dojo-type="dijit.form.ValidationTextBox" maxlength="100"/></td>'
							+'</tr>'
							+'<tr>'
								+'<td colspan="3"></td>'
								+'<td><input name="termConditions['+index+'].termCondition2" data-dojo-type="dijit.form.ValidationTextBox" maxlength="100"/></td>'
							+'</tr>'
							+'<tr>'
								+'<td colspan="3"></td>'
								+'<td><input name="termConditions['+index+'].termCondition3" data-dojo-type="dijit.form.ValidationTextBox" maxlength="100"/></td>'
							+'</tr>'
							+'<tr>'
								+'<td colspan="3"></td>'
								+'<td><input name="termConditions['+index+'].termCondition4" data-dojo-type="dijit.form.ValidationTextBox" maxlength="100"/></td>'
							+'</tr>'
							+'<tr>'
								+'<td width="2px"><span class="required">*</span> </td>'
						       				+'<td>&nbsp;&nbsp;<s:text name="supplier.edit.termAndCondition.default"/></td>'
							    +'<td>:</td>'
								+'<td><input name="defaultSelected" type="radio" value="'+index+'"/></td>'
							+'</tr>'
						+'</tbody></table>';
			div1.innerHTML=s;
            div.appendChild(div1);
            if (flag)
            {
                var parser = require("dojo/parser");
                parser.parse(sourceTable);
            }
            setDefaultSelected();
            index++;
		}

		
        function remove(index)
        {
            var xhr = require("dojo/_base/xhr");
            var ConfirmDialog = require("custom/ConfirmDialog");

            var confirmDialog = new ConfirmDialog({
                message: '<s:text name="alert.delete.records" />',
                yesBtnPressed: function(){
                	var targetDiv = document.getElementById("targetDiv");
        			var allTbody = sourceTable.getElementsByTagName("tbody");
        			if(allTbody.length==1)
        			{
        				alert('<s:text name="B2BPC0339"/>');
        				return;
        			}

        			var csrfToken = document.getElementById("csrfToken").value;
                    xhr.get({
                            url: '<s:url value="/supplier/removeTermAndCondition.action" />',
                            content: {index: index, csrfToken: csrfToken},
                            load: function(data)
                            {
                                console.log(data);
                                if(data == '0')
                                {
        							alert("<s:text name='B2BPC0334' />")
                                }
                                else if(data == '-1')
                                {
        							alert("<s:text name='B2BPC0335' />");
                                }
                                else if(data =='1')
                                {
        				            var itemDiv = document.getElementById('itemDiv'+index);
        				            targetDiv.removeChild(itemDiv);
        				            setDefaultSelected();
                                }
                            }
                    });
                }
            });
            
            confirmDialog.show();
        	
        }


        function setDefaultSelected()
        {
        	var defaultSelected = document.getElementsByName("defaultSelected");
            var flag = false;
            for(var i=0;i<defaultSelected.length;i++)
            {
                if(defaultSelected[i].checked==true)
                {
					flag = true;
                }
            }
            if(!flag)
            {
            	defaultSelected[0].checked=true;
            }
        }
                
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button data-dojo-type="dijit.form.Button" id="addBtn" ><s:text name="button.add" /></button>
            <button data-dojo-type="dijit.form.Button" id="saveBtn" ><s:text name="button.save" /></button>
            <button data-dojo-type="dijit.form.Button" id="resetBtn" ><s:text name="button.reset" /></button>
            <button data-dojo-type="dijit.form.Button" id="cancelBtn" ><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    
    <div class="pageBar">
        <div class="title"><s:text name="supplier.edit.termAndCondition"/></div>
    </div>
    
    <div class="required">
        <s:actionerror />
    </div>
    <div id="haha"></div>
    
    <s:form id="mainForm" name="mainForm"   theme="simple"  method="post" enctype="multipart/form-data" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token></s:token>
	<table class="commtable" style="border:#C5DBEC 1px solid;"  width="99%" cellpadding="1px" id="sourceTable">
	   <tr>
	       <td>
	           <div id="targetDiv">
			    	<s:iterator value="termConditions" status="status" id="item">
			    	    <div id="itemDiv<s:property value="#status.index" />">
			    	    <table width="100%">
						<tbody id="tbody<s:property value="#status.index" />">
							<tr>
								<td width="2px"><span class="required">*</span> </td>
				      				<td width="30%">&nbsp;&nbsp;<s:text name="supplier.edit.termAndCondition.code"/></td>
							    <td>:</td>
								<td><input name="termConditions[<s:property value="#status.index" />].termConditionCode" value="<s:property value="#item.termConditionCode"/>"  data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" maxlength="20"/>
									&nbsp;&nbsp;<button onclick="remove(<s:property value="#status.index" />);" data-dojo-type="dijit.form.Button" name="termConditions._<s:property value="#status.index" />.button"><s:text name="button.remove" /></button>
								</td>
							</tr>
							<tr>
								<td width="2px"><span class="required">*</span> </td>
						       				<td>&nbsp;&nbsp;<s:text name="supplier.edit.termAndCondition.desc"/></td>
							    <td>:</td>
								<td><input name="termConditions[<s:property value="#status.index" />].termCondition1" value="<s:property value="#item.termCondition1"/>"  data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" maxlength="100"/></td>
							</tr>
							<tr>
								<td colspan="3"></td>
								<td><input name="termConditions[<s:property value="#status.index" />].termCondition2" value="<s:property value="#item.termCondition2"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="100"/></td>
							</tr>
							<tr>
								<td colspan="3"></td>
								<td><input name="termConditions[<s:property value="#status.index" />].termCondition3" value="<s:property value="#item.termCondition3"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="100"/></td>
							</tr>
							<tr>
								<td colspan="3"></td>
								<td><input name="termConditions[<s:property value="#status.index" />].termCondition4" value="<s:property value="#item.termCondition4"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="100"/></td>
							</tr>
							<tr>
								<td width="2px"><span class="required">*</span> </td>
						       				<td>&nbsp;&nbsp;<s:text name="supplier.edit.termAndCondition.default"/></td>
							    <td>:</td>
								<td>
								<s:if test="#item.defaultSelected==true">
									<input name="defaultSelected" checked="checked" type="radio" value="<s:property value="#status.index" />"/>
								</s:if>
								<s:else>
									<input name="defaultSelected" type="radio" value="<s:property value="#status.index" />"/>
								</s:else>
								</td>
							</tr>
						</tbody>
						</table>
						</div>
					</s:iterator>
				</div>
	       </td>
	   </tr>
	</table>
	<div id="div1"></div>
    
	
    </s:form>
	<script type="text/javascript">
		if("<s:property value='termConditions' />" == null || "<s:property value='termConditions' />" == "[]")
		{
		    add(false);
		}
	</script>
</body>
</html>
