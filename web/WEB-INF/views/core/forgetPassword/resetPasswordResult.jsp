<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- TemplateBeginEditable name="doctitle" -->
    <title><s:text name="ntuc.web.portal"/></title>

    <script type="text/javascript" src="<s:url value='/js/common.js' includeParams='none'/>"></script>

    <script type="text/javascript">
        require(["custom/B2BPortalBase","dojo/parser","dojo/on","dojo/dom","dojo/domReady!"],
            function(B2BPortalBase,parser,on,dom)
            {
                parser.parse();
                
                (new B2BPortalBase()).initLoginTemplate(
                    '<c:out value="${session.helpExHolder.helpNo}"/>',
                    '<c:out value="${session.helpExHolder.helpEmail}"/>');
                
                on(dom.byId("backBtn"), "click", function(){
                    changeToURL('<s:url value="/home.action"/>');
                });
            });
    </script>
</head>


<body class="<s:property value='%{#session.layoutTheme}' />">
 	<div class="log_content" style="background:url(<s:url value='/css/%{#session.layoutTheme}/log_img/forgot_password_content_1.png' />) no-repeat center; ">
 	    <div class="log_text"><p><s:text name="ntuc.web.portal"/></p></div>
 		<table class="log_tab" cellpadding="0" cellspacing="0" border="0" >
     		<tr>
    			<td>
    				<table style=" color:red;">
      					<tr>
        					<td><h2><s:actionerror /></h2><td>
      					</tr>
     				</table>
     			</td>
     		</tr>
     		<tr>
    			<td>
    				<table >
      					<tr>
        					<td><h2><s:actionmessage/></h2><td>
      					</tr>
     				</table>
     			</td>
     		</tr>
     	</table>
    	<p class="sub">
    		<button id="backBtn" class="subbut">
    			<s:text name="button.back.to.login"/>
	        </button>
    	</p>
 	</div>
</body>
</html>
