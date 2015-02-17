<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- TemplateBeginEditable name="doctitle" -->
    <title><s:text name="ntuc.web.portal"/></title>

    <script type="text/javascript">
        require(["custom/B2BPortalBase","dojo/parser","dojo/on","dojo/dom","dojo/string","dojo/domReady!"],
            function(B2BPortalBase,parser,on,dom,string)
            {
                parser.parse();
                
                (new B2BPortalBase()).initLoginTemplate(
                    '<c:out value="${session.helpExHolder.helpNo}"/>',
                    '<c:out value="${session.helpExHolder.helpEmail}"/>');
                    
                on(dom.byId("submitBtn"), "click", function(){
                    if(string.trim(dom.byId("loginId").value) == "" )
                    {
                        alert('<s:text name="B2BPC0724"/>');
                        return;
                    }
                    
                    submitForm("mainForm", '<s:url value="/forgetPassword/resetPassword.action"/>');
                });
                
                
                on(dom.byId("backBtn"), "click", function(){
                    changeToURL('<s:url value="/home.action"/>');
                });
            });
        
    </script>
</head>


<body class="<s:property value='%{#session.layoutTheme}' />">
 	<form method="post" id="mainForm" name="mainForm" onsubmit="javascript: return false;">
    <div class="log_content" style="background:url(<s:url value='/css/%{#session.layoutTheme}/log_img/forgot_password_content_1.png' />) no-repeat center; ">
        <div class="log_text"><p><s:text name="ntuc.web.portal"/></p></div>
    	<table class="log_tab" cellpadding="0" cellspacing="0" border="0">
    		<tr>
    			<td>
    				<table class="tb2" >
      					<tr>
        					<th width="97"><s:text name="login.content.loginId"/></th>
        					<td width="212"><input name="userProfile.loginId" id="loginId" size="30" ></td>
      					</tr>
     				</table>
     			</td>
     		</tr>
     	</table>
    	<p class="sub">
    		<button id="submitBtn" class="subbut">
    			<s:text name="button.submit"/>
	        </button>
	        <button id="backBtn" class="subbut"/>
	            <s:text name="button.back.to.login"/>
	        </button>
    	</p>
  	</div>
	</form>
	<c:if test="${ not empty MSG_ERROR_FORGET_PASSWORD }">
		<script language="javascript">
			var msg = "";
			    		    		
	    	msg = '<c:out value="${MSG_ERROR_FORGET_PASSWORD}" />';        	
	    								
			alert(msg.replace("&#039;","\'"));
		</script>		
	</c:if>
</body>
</html>
