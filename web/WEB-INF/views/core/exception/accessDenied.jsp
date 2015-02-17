<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>
<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
   <script type="text/javascript">
        require(["custom/B2BPortalBase", "dojo/parser", "dojo/domReady!"], 
            function(B2BPortalBase,parser)
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
        <br />
        
		<h2 style="text-indent:2em">
            <div>
                <s:text name="message.exception.no.privilege" /><br />
            </div>
        </h2>
        
        
        <h2 style="text-indent:2em">
            <div>
                <s:text name="message.exception.no.privilege.helpinfo" /><br />
            </div>
        </h2>
    </div>
</body>
</html>
