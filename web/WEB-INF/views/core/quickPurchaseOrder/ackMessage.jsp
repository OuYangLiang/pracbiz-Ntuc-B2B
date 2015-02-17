<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>
<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        require(["custom/B2BPortalBase", "dijit/form/Button", "dojo/parser", "dojo/domReady!"], 
            function(B2BPortalBase,Button,parser)
            {
                parser.parse();
                
                (new B2BPortalBase()).init(
                    '<c:out value="${session.helpExHolder.helpNo}"/>',
                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
                    '<s:property value="#session.commonParam.timeout" />',
                    '<s:url value="/logout.action" />');
            });
        
    </script>
</head>
<body>
    <table class="btnContainer" style="width:99%">
        <tbody><tr><td>
            <button onClick="javascript:changeToURL('<s:url value="/quickPo/init.action?keepSp=Y" />');" data-dojo-type="dijit.form.Button" ><s:text name="button.back.to.list"/></button>
        </td></tr></tbody>
    </table>

    <div id="titleEdit">
        <div class="pageBar">
            <div class="title"><s:text name="message.information.general.title" /></div>
        </div>
        
        <c:if test="${faileds != null && not empty faileds}">
        	<h2 style="text-indent:2em;color:red;">
        		 Invoice 
        		 <c:choose>
        			<c:when test="${op == 'S'}">
        			generation
        			</c:when>
        			<c:otherwise>
        			submission
        			</c:otherwise>
        		</c:choose>
        		for the following POs have failed.
        		<c:forEach items="${faileds}" var="content" varStatus="rowCount">
	            	<div style="color: red;text-indent:5em"><c:out value="${rowCount.index + 1}" />.&nbsp;&nbsp;<c:out value="${content}" /></div>
	            </c:forEach>
            </h2>
        </c:if>
        
        <c:if test="${successes != null and not empty successes }">
        	<h2 style="text-indent:2em">
        		The following invoices have been 
        		<c:choose>
        			<c:when test="${op == 'S'}">
        			generated
        			</c:when>
        			<c:otherwise>
        			submitted
        			</c:otherwise>
        		</c:choose>
        		 successfully.
	        	<c:forEach items="${successes}" var="content" varStatus="rowCount">
	            	<div style="text-indent:5em"><c:out value="${rowCount.index + 1}" />.&nbsp;&nbsp;<c:out value="${content}" /></div>
	            </c:forEach>
            </h2>
        </c:if>
    </div>
</body>
</html>
