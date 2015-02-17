<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>
<html>
    <head>
        <title><s:text name="ntuc.web.portal"/></title>
           <script>
            require(["custom/B2BPortalBase", "dojo/dom", "dijit/registry", "dojo/on", "dijit/form/Button", "dojo/parser", "dojo/domReady!"], 
             function(B2BPortalBase,dom,registry,on,Button,parser)
             {
                parser.parse();
                
                (new B2BPortalBase()).init(
                    '<c:out value="${session.helpExHolder.helpNo}"/>',
                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
                    '<s:property value="#session.commonParam.timeout" />',
                    '<s:url value="/logout.action" />');
                
                on(registry.byId("backBtn"), 'click', function(){
                    changeToURL('<s:url value="/main.action" />');
                });
             });
        
           </script>
    </head>
    <body>
    <table class="btnContainer" style="width:99%">
        <tbody><tr><td>
                <button data-dojo-type="dijit/form/Button" id="backBtn" ><s:text name="button.back.to.home" /></button>
        </td></tr></tbody>
    </table>

    <div id="titleEdit">
        <div class="pageBar">
            <div class="title"><s:text name="message.exception.general.title"/></div>
        </div>
        <h2 style="text-indent:2em">
            <div><s:text name="message.double.post"/></div>
        </h2>
    </div>
    </body>
</html>
