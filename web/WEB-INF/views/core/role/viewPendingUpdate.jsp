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
                "dojo/_base/xhr",
                "dojo/parser",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    xhr,
                    parser
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                    
                    var fn = function(url)
                    {
                        var oids = '<s:property value="param.roleOid" />';
                        
                        xhr.get({
                                url: '<s:url value="/role/putParamIntoSession.action" />',
                                content: {selectedOids: oids},
                                load: function(data)
                                {
                                    changeToURL(url);
                                }
                            });
                    }
                    
                    if (dom.byId("approveBtn"))
                    {
                        on(registry.byId("approveBtn"), 'click', 
                            function()
                            {
                            	var csrfToken = dom.byId("csrfToken").value;
                                fn('<s:url value="/role/saveApprove.action?csrfToken=" />'+csrfToken);
                            }
                        );
                    }
                    
                    if (dom.byId("rejectBtn"))
                    {
                        on(registry.byId("rejectBtn"), 'click', 
                            function()
                            {
                        		var csrfToken = dom.byId("csrfToken").value;
                                fn('<s:url value="/role/saveReject.action?csrfToken=" />'+csrfToken);
                            }
                        );
                    }
                    
                    if (dom.byId("withdrawBtn"))
                    {
                        on(registry.byId("withdrawBtn"), 'click', 
                            function()
                            {
                        		var csrfToken = dom.byId("csrfToken").value;
                                fn('<s:url value="/role/saveWithdraw.action?csrfToken=" />'+csrfToken);
                            }
                        );
                    }
                    
                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/role/init.action?keepSp=Y" />');
                        }
                    );
                });
                
    </script>
        
    </script>
</head>
<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <s:if test="#session.commonParam.mkMode" >
            <s:if test="#session.permitUrl.contains('/role/saveApprove.action') && #session.SESSION_CURRENT_USER_PROFILE.loginId!=param.actor" >
                <button data-dojo-type="dijit.form.Button" id="approveBtn" ><s:text name="button.approve" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/role/saveReject.action') && #session.SESSION_CURRENT_USER_PROFILE.loginId!=param.actor" >
                <button data-dojo-type="dijit.form.Button" id="rejectBtn" ><s:text name="button.reject" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/role/saveWithdraw.action') && #session.SESSION_CURRENT_USER_PROFILE.loginId==param.actor" >
                <button data-dojo-type="dijit.form.Button" id="withdrawBtn" ><s:text name="button.withdraw" /></button>
            </s:if>
            </s:if>
            <button data-dojo-type="dijit.form.Button" id="cancelBtn" ><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    <div class="pageBar">
        <div class="title"><s:text name="role.view.title" /></div>
    </div>
    
    <div>
    	<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    </div>
    
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="role.view.profile" />', width:275" style="width:99%">
        <table class="commtable">
			<tbody>
				<tr>
					<td width="2px"><span class="required">*</span> </td>
					<td width="30%">&nbsp;&nbsp;<s:text name="role.view.profile.userType" /></td>
					<td>:</td>
					<td><s:property value="param.userTypeId" /></td>
				</tr>
				
				<tr>
					<td width="2px"><span class="required">*</span> </td>
					<td width="30%">&nbsp;&nbsp;<s:text name="role.view.profile.roleType" /></td>
					<td>:</td>
					<td><s:property value="param.roleTypeValue" /></td>
				</tr>
				
				<s:if test="param.userTypeOid != 1 && !companyChanged" >
				<tr>
					<td style="vertical-align:top"><span class="required">*</span> </td>
                    <td style="vertical-align:top">&nbsp;&nbsp;<s:text name="role.view.profile.company" /></td>
                    <td style="vertical-align:top">:</td>
                    <td style="vertical-align:top"><s:property escape="false" value="param.company" /></td>
				</tr>
				</s:if>
				
				<tr>
					<td><span class="required">*</span></td>
					<td>&nbsp;&nbsp;<s:text name="role.view.profile.roleId" /></td>
					<td>:</td>
					<td><s:property value="OldParam.roleId" /></td>
				</tr>
				
				<s:if test="param.roleId!=oldParam.roleId" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required"><s:property value="param.roleId" /></td>
                </tr>
                </s:if>
				
				<tr>
					<td><span class="required">*</span></td>
					<td>&nbsp;&nbsp;<s:text name="role.view.profile.roleDesc" /></td>
					<td>:</td>
					<td><s:property value="OldParam.roleName" /></td>
				</tr>
				
				<s:if test="param.roleName!=oldParam.roleName" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required"><s:property value="param.roleName" /></td>
                </tr>
                </s:if>
			</tbody>
        </table>
    </div>
    
    <s:if test="companyChanged" >
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="role.view.company.list" />', width:275" style="width:99%">
        <table class="commtable">
            <tr class="thead">
                <td width="50%">Previous</td>
                <td>Current</td>
            </tr>
            
            <tr>
                <td style="vertical-align:top">
                    <table class="access">
                        <tr><td><ul>
                            <c:forEach items="${oldSelectedSuppliers}" var="supplier" varStatus="targetIndex">
                                <li><c:out value="${supplier.supplierName}" /></li>
                            </c:forEach>
                        </ul></td></tr>
                    </table>
                </td>
                
                <td  style="vertical-align:top">
                    <table class="access">
                        <tr><td><ul>
                            <c:forEach items="${selectedSuppliers}" var="supplier" varStatus="targetIndex">
                                <li><c:out value="${supplier.supplierName}" /></li>
                            </c:forEach>
                        </ul></td></tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    </s:if>
        
    <div class="space"></div>
        
	<div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="role.view.privilege" />', width:275" style="width:99%">
		<table class="commtable">
			<tr class="thead">
				<td width="50%">Previous</td>
				<td>Current</td>
			</tr>
			
			<tr>
                <td style="vertical-align:top">
					<table class="access">
						<tr><td><ul>
                            <c:forEach items="${oldSelectedOperations}" var="operation" varStatus="targetIndex">
				                <li><c:out value="${operation.opnDesc}" /></li>
				            </c:forEach>
						</ul></td></tr>
					</table>
				</td>
				
				<td  style="vertical-align:top">
					<table class="access">
						<tr><td><ul>
							<c:forEach items="${selectedOperations}" var="operation" varStatus="targetIndex">
                                <li><c:out value="${operation.opnDesc}" /></li>
                            </c:forEach>
						</ul></td></tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	
    <div class="space"></div>
    
	<div id="ftcontent" align="center">
		<table id="control">
			<tr><td><s:text name="role.view.ctrl.ctrlstatus" /></td><td>:</td><td><s:property value="param.ctrlStatusValue" /></td></tr>
            <tr><td><s:text name="role.view.ctrl.actionType" /></td><td>:</td><td ><s:property value="param.actionTypeValue" /></td></tr>
            <tr><td><s:text name="role.view.ctrl.actor" /></td><td>:</td><td><s:property value="param.actor" /></td></tr>
            <tr><td><s:text name="role.view.ctrl.actionDate" /></td><td>:</td><td><s:property value="param.actionDate" /></td></tr>
		</table>
	</div>
</body>
</html>
