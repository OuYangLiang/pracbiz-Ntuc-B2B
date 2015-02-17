<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
<title><s:text name="ntuc.web.portal"/></title>

<script>
	require(
			[ "custom/B2BPortalBase", "dojo/dom", "dijit/registry", "dojo/on", "dijit/layout/TabContainer",
 			  "dijit/layout/ContentPane", "dojox/layout/ExpandoPane", "dijit/TitlePane",
					"dijit/form/Button", "dojo/parser", "dojo/domReady!" ],
			function(B2BPortalBase, dom, registry, on, TabContainer, ContentPane, ExpandoPane, TitlePane, Button, parser)
			{

				parser.parse();
				disableUser();
				
				function disableUser()
                {
                    <s:iterator value="selectedGroupUsers" var="innerItem" >
                        var lastUpdateFrom = '<s:property value="#innerItem.lastUpdateFrom"/>';
                        var approved = '<s:property value="#innerItem.approved"/>';
                        var userOid = '<s:property value="#innerItem.userOid"/>';
                        var actionType= '<s:property value="#innerItem.actionType"/>';
                       
                        if (lastUpdateFrom == 'USER' && approved == 'false')
                        {
                            var message='<s:text name="LastUpdateFrom.User"/>';
                        }
                        else if (lastUpdateFrom == 'GROUP' && actionType == 'DELETE')
                        {
                            var user dom.byId("user"+userOid);
                        }
                               
                    </s:iterator>
                }

				(new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                
				if (dom.byId("cancelBtn")) 
				{
					on(registry.byId("cancelBtn"),'click',
						function() {
							changeToURL('<s:url value="/group/init.action?keepSp=Y" />');
						});
				}

                if (dom.byId("approveBtn"))
                {
                    on(registry.byId("approveBtn"), 'click', function(){
                        var csrfToken = dom.byId("csrfToken").value;
                    	changeToURL('<s:url value="/group/saveApprove.action" />?csrfToken='+csrfToken);
                    });
                }

                if (dom.byId("rejectBtn"))
                {
                    on(registry.byId("rejectBtn"), 'click', function(){
                    	var csrfToken = dom.byId("csrfToken").value;
                    	changeToURL('<s:url value="/group/saveReject.action" />?csrfToken='+csrfToken);
                    });
                }

                if (dom.byId("withdrawBtn"))
                {      	
                    on(registry.byId("withdrawBtn"), 'click', function(){
						var csrfToken = dom.byId("csrfToken").value;
                    	changeToURL('<s:url value="/group/saveWithdraw.action" />?csrfToken='+csrfToken);
                    });
                }
			});
</script>
</head>

<body class="claro">
	<!-- Button Area -->
	<div>
		<table class="btnContainer">
			<tbody>
				<tr>
					<td>
						<s:if test='#session.permitUrl.contains("/group/saveApprove.action") && confirmType=="A"' >
			            	<button id="approveBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.approve' /></button>
			            </s:if>
			            <s:if test='#session.permitUrl.contains("/group/saveReject.action") && confirmType=="R"' >
			            	<button id="rejectBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.reject' /></button>
			            </s:if>
			            <s:if test='#session.permitUrl.contains("/group/saveWithdraw.action") && confirmType=="W"' >
			            	<button id="withdrawBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.withdraw' /></button>
			            </s:if>
						<button id="cancelBtn" data-dojo-type="dijit.form.Button"><s:text name='button.cancel' /></button>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- here is message area -->

	<div class="required">
		<s:actionerror />
		<s:fielderror />
	</div>

	<div class="pageBar">
		<div class="title">
			<s:text name='group.confirm.pageBar' />
		</div>
	</div>
	<div>
		<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
	</div>
						
	<div data-dojo-type="dijit/layout/TabContainer" style="width: 100%;"
		doLayout="false">
		
		<!--CREATE SECTION-->
		<s:if test="!createList.isEmpty()">
        <div data-dojo-type="dijit/layout/ContentPane" title="CREATE">
            <%@ include file="/WEB-INF/views/core/group/confirmCreate.jsp"%>
        </div>
        </s:if>
        <!--UPDATE SECTION-->
        <s:if test="!updateList.isEmpty()">
        <div data-dojo-type="dijit/layout/ContentPane" title="UPDATE">
            <%@ include file="/WEB-INF/views/core/group/confirmUpdate.jsp"%>
        </div>
        </s:if>
        <!--DELETE SECTION-->
        <s:if test="!deleteList.isEmpty()">
        <div data-dojo-type="dijit/layout/ContentPane" title="DELETE">
            <%@ include file="/WEB-INF/views/core/group/confirmDelete.jsp"%>
        </div>
        </s:if>
        
        <!--ALL SECTION-->
        <s:if test="!createList.isEmpty() || !updateList.isEmpty() || !deleteList.isEmpty()">
        <div data-dojo-type="dijit/layout/ContentPane" title="ALL">
            <!-- Create part -->
            <%@ include file="/WEB-INF/views/core/group/confirmCreate.jsp"%>
            <!-- Delete part -->    
            <%@ include file="/WEB-INF/views/core/group/confirmUpdate.jsp"%>
            <!-- Update part -->    
            <%@ include file="/WEB-INF/views/core/group/confirmDelete.jsp"%>
        </div>
        </s:if>
	</div>
</body>
</html>
