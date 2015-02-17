<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dijit/registry",
                "dojo/on",
                "dojo/parser",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
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
                    
    	        	   
                    on(registry.byId("backBtn"), "click", function(){
                        changeToURL('<s:url value="/buyerStore/init.action?keepSp=Y" />');
                    });
                });
                
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button data-dojo-type="dijit.form.Button" id="backBtn" ><s:text name="button.back" /></button>
        </td></tr></tbody></table>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <form id="mainForm" name="mainForm" method="post" >
    <div class="pageBar">
        <div class="title"><s:text name="store.view.store.title" /></div>
    </div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="store.view.store.panel.title" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="5"></td>
                <td width="300">&nbsp;&nbsp;<s:text name="store.view.store.area"/></td>
                <td width="20">:</td>
                <td><s:property value="param.areaCode"/>&nbsp;<s:if test="param.areaName != null">[<s:property value="param.areaName"/>]</s:if></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="store.view.store.storeCode"/></td>
                <td>:</td>
                <td><s:property value="param.storeCode"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="store.view.store.storeName"/></td>
                <td>:</td>
                <td><s:property value="param.storeName" /></td>
            </tr>
            <tr>
                <td><span class="required"></span></td>
                <td>&nbsp;&nbsp;<s:text name="store.view.store.contactTel"/></td>
                <td>:</td>
                <td><s:property value="param.contactTel" /></td>
            </tr>
            <tr>
                <td></td>
                <td valign="top">&nbsp;&nbsp;<s:text name="store.view.store.addr"/></td>
                <td valign="top">:</td>
                <td><table>
                	<tbody>
                	<s:if test='param.storeAddr1 != null && param.storeAddr1 != ""'>
                	<tr><td valign="top"><s:property value="param.storeAddr1" /></td></tr>
                	</s:if>
                	<s:if test='param.storeAddr2 != null && param.storeAddr2 != ""'>
                	<tr><td valign="top"><s:property value="param.storeAddr2" /></td></tr>
                	</s:if>
                	<s:if test='param.storeAddr3 != null && param.storeAddr3 != ""'>
                	<tr><td valign="top"><s:property value="param.storeAddr3" /></td></tr>
                	</s:if>
                	<s:if test='param.storeAddr4 != null && param.storeAddr4 != ""'>
                	<tr><td valign="top"><s:property value="param.storeAddr4" /></td></tr>
                	</s:if>
                	<s:if test='param.storeAddr5 != null && param.storeAddr5 != ""'>
                	<tr><td valign="top"><s:property value="param.storeAddr5" /></td></tr>
                	</s:if>
                	</tbody>
                </table></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="store.view.store.city"/></td>
                <td>:</td>
                <td><s:property value="param.storeCity" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="store.view.store.state"/></td>
                <td>:</td>
                <td><s:property value="param.storeState" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="store.view.store.ctry"/></td>
                <td>:</td>
                <td><s:property value="param.storeCtryName" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="store.view.store.postalCode"/></td>
                <td>:</td>
                <td><s:property value="param.storePostalCode" /></td>
            </tr>
            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.supplierOid == null" >  
            <tr>
                <td></td>
                <td  valign="top">&nbsp;&nbsp;<s:text name="store.view.store.assignedUser"/></td>
                <td  valign="top">:</td>
                <td>
                	<table>
                		<s:if test="assignedUsers != null">
                			<c:forEach items="${assignedUsers}" var="user" varStatus="targetIndex">
				                   <tr><td  valign="top">
				                   <c:out value="${user.userName}" />&nbsp;[<c:out value="${user.userTypeDesc}" />]
				                   </td></tr>
				            </c:forEach>
                		</s:if>
                	</table>
				</td>
            </tr>
            </s:if>
        </tbody>
        </table>
    </div>
    </form>
</body>
</html>
