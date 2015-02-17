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
            <c:forEach items="${msg.targets}" var="target" varStatus="targetIndex">
                <button onClick='javascript:submitForm("backForm<c:out value='${targetIndex.index}' />", "<c:url value='${target.targetURI}.action' />");' data-dojo-type="dijit.form.Button" ><c:out value="${target.targetBtnTitle}" /></button>
            </c:forEach>
        </td></tr></tbody>
    </table>

    <div id="titleEdit">
        <div class="pageBar">
            <div class="title"><c:out value="${msg.title}" /></div>
        </div>
        
        <c:if test="${msg.contents != null}">
            <h2 style="text-indent:2em">
            <c:forEach items="${msg.contents}" var="content" varStatus="rowCount">
                <div><c:out value="${content.MSG_CONTENT}" /></div>
            </c:forEach>
            </h2>
        </c:if>
        
        <c:forEach items="${msg.targets}" var="target" varStatus="targetIndex">
            <form id="backForm<c:out value="${targetIndex.index}" />" name="backForm<c:out value="${targetIndex.index}" />" action="" method="post">
            <c:if test="${target.parameters != null}">
            <c:forEach items="${target.parameters}" var="parameter" varStatus="rowCount">
                <input type="hidden" name='<c:out value="${parameter.paramKey}" />' value='<c:out value="${parameter.paramValue}" />'/>
            </c:forEach>
            </c:if>
            </form>
        </c:forEach>
    </div>
</body>
</html>
