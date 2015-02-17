<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>
<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
   <script type="text/javascript">
        require(["dojo/parser", "custom/B2BPortalBase", "dojo/domReady!"], 
            function(parser,B2BPortalBase)
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
    <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
<%--         <s:text name="welcome.content"/>  --%>
		<s:if test="messages != null && messages.size() > 0">
			<table width="100%" style="table-layout: fixed;">
	        	<thead>
	        		<tr align="center"><th colspan="2"  style="text-align: center;vertical-align: middle; font-size: 25px;font-weight: bold; ">Announcement</th></tr>
	        		<tr><th colspan="2">&nbsp;</th></tr>
	        		<tr><th colspan="2"><hr/></th></tr>
	        	</thead>
	        	<tbody>
	       			<c:forEach items="${messages}" var="msg" varStatus="status">
						<tr>
							<td colspan="2" align="left" style="font-size: 15px; font-weight: bold; "><p></p><c:out value="${msg.title}"/></td>
						</tr>
						<tr>
							<td colspan="2" align="left" style="font-size: 15px; word-wrap : break-word;"><c:out value="${msg.content}"/></td>
						</tr>
					</c:forEach>
	        	</tbody>
        	</table>
		</s:if>
    </div>
</body>
</html>
