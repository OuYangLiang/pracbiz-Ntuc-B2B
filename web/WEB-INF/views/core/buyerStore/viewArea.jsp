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
        <div class="title"><s:text name="store.view.area.title" /></div>
    </div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="store.view.area.panel.title" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="300">&nbsp;&nbsp;<s:text name="store.view.area.areaCode"/></td>
                <td width="20">:</td>
                <td><s:property value="param.areaCode"/></td>
            </tr>
            <tr>
                <td>&nbsp;&nbsp;<s:text name="store.view.area.areaName"/></td>
                <td>:</td>
                <td><s:property value="param.areaName" /></td>
            </tr>
            <tr>
                <td valign="top">&nbsp;&nbsp;<s:text name="store.view.area.store"/></td>
                <td valign="top">:</td>
                <td>
					<table>
                		<s:if test="storeList != null">
                			<c:forEach items="${storeList}" var="store" varStatus="targetIndex">
				                   <tr><td valign="top">
				                   <c:out value="${store.storeCode}" />&nbsp;[<c:out value="${store.storeName}" />]
				                   </td></tr>
				            </c:forEach>
                		</s:if>
                	</table>
				</td>
            </tr>
            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.supplierOid == null" >  
            <tr>
                <td  valign="top">&nbsp;&nbsp;<s:text name="store.view.area.assignedUser"/></td>
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
