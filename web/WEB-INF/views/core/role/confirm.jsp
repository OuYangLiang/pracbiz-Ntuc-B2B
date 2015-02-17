<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>
<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        require(["custom/B2BPortalBase", "dojo/dom", "dijit/registry", "dojo/on",
                "dijit/form/Button", "dojo/parser", "dijit/layout/TabContainer", "dojo/domReady!"], 
                function(B2BPortalBase, dom, registry, on, Button, parser, TabContainer )
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
                    
                    if (dom.byId("approveBtn"))
                    {
                        on(registry.byId("approveBtn"), 'click', function(){
                        	var csrfToken = dom.byId("csrfToken").value;
                            changeToURL('<s:url value="/role/saveApprove.action" />?csrfToken='+csrfToken);
                        });
                    }
                    
                    
                    if (dom.byId("rejectBtn"))
                    {
                        on(registry.byId("rejectBtn"), 'click', function(){
                        	var csrfToken = dom.byId("csrfToken").value;
                            changeToURL('<s:url value="/role/saveReject.action" />?csrfToken='+csrfToken);
                        });
                    }
                    
                    
                    if (dom.byId("withdrawBtn"))
                    {
                        on(registry.byId("withdrawBtn"), 'click', function(){
                        	var csrfToken = dom.byId("csrfToken").value;
                            changeToURL('<s:url value="/role/saveWithdraw.action" />?csrfToken='+csrfToken);
                        });
                    }
                    
                });
    </script>
</head>
<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <s:if test='#session.permitUrl.contains("/role/saveApprove.action") && confirmType=="A"' >
	            <button data-dojo-type="dijit/form/Button" id="approveBtn" ><s:text name="button.approve" /></button>
	        </s:if>
	        <s:if test='#session.permitUrl.contains("/role/saveReject.action") && confirmType=="R"' >
	            <button data-dojo-type="dijit/form/Button" id="rejectBtn" ><s:text name="button.reject" /></button>
	        </s:if>
	        <s:if test='#session.permitUrl.contains("/role/saveWithdraw.action") && confirmType=="W"' >
	            <button data-dojo-type="dijit/form/Button" id="withdrawBtn" ><s:text name="button.withdraw" /></button>
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
    <div data-dojo-type="dijit/layout/TabContainer" style="width: 100%;" doLayout="false">
        <!--CREATE SECTION-->
        <s:if test="!createList.isEmpty()">
        <div data-dojo-type="dijit/layout/ContentPane" title="CREATE">
            <%@ include file="/WEB-INF/views/core/role/confirmCreate.jsp"%>
        </div>
        </s:if>
        <!--UPDATE SECTION-->
        <s:if test="!updateList.isEmpty()">
        <div data-dojo-type="dijit/layout/ContentPane" title="UPDATE">
            <%@ include file="/WEB-INF/views/core/role/confirmUpdate.jsp"%>
        </div>
        </s:if>
        <!--DELETE SECTION-->
        <s:if test="!deleteList.isEmpty()">
        <div data-dojo-type="dijit/layout/ContentPane" title="DELETE">
            <%@ include file="/WEB-INF/views/core/role/confirmDelete.jsp"%>
        </div>
        </s:if>
        
        <!--ALL SECTION-->
        <s:if test="!createList.isEmpty() || !updateList.isEmpty() || !deleteList.isEmpty()">
        <div data-dojo-type="dijit/layout/ContentPane" title="ALL">
            <!-- Create part -->
            <%@ include file="/WEB-INF/views/core/role/confirmCreate.jsp"%>
            <!-- Delete part -->    
            <%@ include file="/WEB-INF/views/core/role/confirmUpdate.jsp"%>
            <!-- Update part -->    
            <%@ include file="/WEB-INF/views/core/role/confirmDelete.jsp"%>
        </div>
        </s:if>
    </div>
</body>
</html>
