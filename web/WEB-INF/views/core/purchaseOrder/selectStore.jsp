<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        require(
        		[
                 "custom/B2BPortalBase",
                 "dojo/dom",
                 "dijit/registry",
                 "dojo/on",
                 "dojo/parser",
                 "dojo/_base/xhr",
                 "dijit/form/Select",
                 "custom/InformationDialog",
                 "custom/ConfirmDialog",
                 "dijit/form/RadioButton",
                 "dojo/store/Memory",
                 "dojo/domReady!"
                 ], 
                 function(
                     B2BPortalBase,
                     dom,
                     registry,
                     on,
                     parser,
                     xhr,
                     Select,
                     InformationDialog,
                     ConfirmDialog,
                     RadioButton,
                     Memory
                     )
                 {
        			parser.parse();

        			(new B2BPortalBase()).init(
     	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
     	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
     	                    '<s:property value="#session.commonParam.timeout" />',
     	                    '<s:url value="/logout.action" />');

        			 var confirmDialog = new ConfirmDialog({
                         message: '<s:text name="B2BPU0107" />',
                         yesBtnPressed: function(){
                             changeToURL('<s:url value="/po/init.action?keepSp=Y" />');
                         }
                     });
                     
                     on(registry.byId("generateInvBtn"),'click',
                         function()
                         {
                         	var docOid = '<s:property value="selectedPo.poOid"/>';
                         	var storeCount = '<s:property value="storeCount"/>';
                         	var locations = '<s:property value="locations"/>';
                         	var poExpiredMsg = '<s:property value="poExpiredMsg"/>';
                            var form = dom.byId("mainForm");
                            form.action='<s:url value="/po/generateInvForSor.action?" />?docOid=' + docOid + "&storeCount=" + storeCount + "&poExpiredMsg=" + poExpiredMsg;
                            form.submit();
                         }
                     );

                     on(registry.byId("cancelBtn"),'click',
                         function()
                         {
                            confirmDialog.show();
                         }
                     );
                });
        
    </script>
</head>

<body>
	<!-- Content Part -->
	<div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
       	<!-- action button part -->
       	<div class="summaryBtn">
       		<div style="float: right;">
	    		<input id="generateInvBtn" type="button" data-dojo-type="dijit.form.Button"
	    			intermediateChanges="false" label="<s:text name="button.generate.einvoice" />" 
	    			iconClass="dijitNoIcon">
	    		</input>
	    		<input id="cancelBtn" type="button" data-dojo-type="dijit.form.Button"
	    			intermediateChanges="false" label="<s:text name="button.cancel" />" 
	    			iconClass="dijitNoIcon">
	    		</input>
	    	</div>
       	</div>
       	
       	<!-- here is message area -->
       	<s:if test="errorMsg != null" >   
    		<div id="errorMsg" class="error">
    			<s:property value="errorMsg"/>
			</div>
		</s:if>
		
       	<!-- search Part -->
       	<div data-dojo-type="dijit.TitlePane" 
       		data-dojo-props="title:'<s:text name="inv.selecte.store"/>', width:275" 
       		open="true" style="width:100%;">
       		<div class="bigMessage">
				<s:text name="inv.selecte.store.info">
					<s:param><s:property value="selectedPo.poNo"/></s:param>
				</s:text>
			</div>
       		<form name="mainForm" id="mainForm" action="" method="post"> 
			<table width="100%">
				<tbody>
					<tr>
						<td class="data">
                             <table class="innerTable">
                                 <tbody>
                                 	<s:iterator value="locations" id="poLocation" status="stat">
									 <tr>
										 <td id="<s:property value="#stat.index+1"/>">
										  <input type="radio" data-dojo-type="dijit.form.RadioButton" class="radioButton" name="store" value="<c:out value="${poLocation.locationCode}"/>" 
										  <s:if test="(store == null && #stat.index==0) || (store == #poLocation.locationCode)">
										  checked="checked" 
										  </s:if>
										  /> <c:out value="${poLocation.locationName}"/> (<c:out value="${poLocation.locationCode}"/>)
										 </td>
									 </tr>
									</s:iterator>
                                   </tbody>
							 </table>
						</td>
					</tr>
				</tbody>
			</table>
			</form>
       	</div>
	</div>
</body>
</html>
