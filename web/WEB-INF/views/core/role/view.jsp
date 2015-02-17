<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>
<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dojo/dom-style",
                "dijit/registry",
                "dojo/on",
                "dojo/parser",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    domStyle,
                    registry,
                    on,
                    parser
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                    
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
            <button data-dojo-type="dijit.form.Button" id="cancelBtn" ><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    <div class="pageBar">
        <div class="title"><s:text name="role.view.title" /></div>
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
				
				<s:if test="param.userTypeOid != 1" >
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
					<td><s:property value="param.roleId" /></td>
				</tr>
				
				<tr>
					<td><span class="required">*</span></td>
					<td>&nbsp;&nbsp;<s:text name="role.view.profile.roleDesc" /></td>
					<td>:</td>
					<td><s:property value="param.roleName" /></td>
				</tr>
			</tbody>
        </table>
    </div>
        
    <div class="space"></div>
       
	<div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="role.view.privilege" />', width:275" style="width:99%">
		<table class="commtable" style="text-indent:2em">
            <c:forEach items="${selectedOperations}" var="operation" varStatus="targetIndex">
                <tr><td><c:out value="${operation.opnDesc}" /></td></tr>
            </c:forEach>
		</table>
	</div>
</body>
</html>
