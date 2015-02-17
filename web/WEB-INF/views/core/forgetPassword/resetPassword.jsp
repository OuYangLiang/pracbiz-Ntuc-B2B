<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- TemplateBeginEditable name="doctitle" -->
    <title><s:text name="ntuc.web.portal"/></title>

    <script type="text/javascript" src="<s:url value='/js/common.js' includeParams='none'/>"></script>

    <script type="text/javascript">
        require(["custom/B2BPortalBase","dojo/parser","dojo/on","dojo/dom","dojo/string","dijit/Tooltip","dojo/touch","dojo/domReady!"],
            function(B2BPortalBase,parser,on,dom,string,Tooltip,touch)
            {
                parser.parse();
                
                (new B2BPortalBase()).initLoginTemplate(
                    '<c:out value="${session.helpExHolder.helpNo}"/>',
                    '<c:out value="${session.helpExHolder.helpEmail}"/>');
                    
                on(dom.byId("submitBtn"), "click", function(){
                    if(string.trim(dom.byId("newPwd").value) == "" )
                    {
                        alert('<s:text name="B2BPC0830"/>');
                        return;
                    }
                    
                    if(string.trim(dom.byId("confirmPwd").value) == "" )
                    {
                        alert('<s:text name="B2BPC0831"/>');
                        return;
                    }
                    
                    if(string.trim(dom.byId("newPwd").value) != string.trim(dom.byId("confirmPwd").value))
                    {
                        alert('<s:text name="B2BPC0820"/>');
                        return;
                    }
                    
                    submitForm("mainForm", '<s:url value="/forgetPassword/saveNewPassword.action" />');
                });
                
                on(dom.byId("backBtn"), "click", function(){
                    changeToURL('<s:url value="/home.action"/>');
                });

                loadTooltip = function(src)
		        {
		            Tooltip.defaultPosition=["after-centered","after"];
		            Tooltip.show("Caps Lock is on.",src);
		            return;
		        }
		        // remove tooltip
		        closeTooltip = function(src)
		        {
		            Tooltip.hide(src);
		        }

   		        on(dojo.byId("newPwd"), 'keypress', function(e)
 	    		{
 		        	detectCapsLock(e);
                });

 		        on(dojo.byId("newPwd"), touch.out, function(e)
 	    		{
 		        	var o = e.target||e.srcElement;
 		        	closeTooltip(o);
                });

   	   		    on(dojo.byId("confirmPwd"), 'keypress', function(e)
   	    		{
   		        	detectCapsLock(e);
                });

   		        on(dojo.byId("confirmPwd"), touch.out, function(e)
   	    		{
   		        	var o = e.target||e.srcElement;
   		        	closeTooltip(o);
                });
            });
        
        function changePassword()
        {
            var url = '<s:url value="/changePassword.action" />?loginId=<c:out value="${ loginId }" />';
            submitForm('mainForm', url);
        }

        function  detectCapsLock(event)
        {
        	var e = event?event:(window.event?window.event:null);
            if (e)
            {
	            var o = e.target||e.srcElement;
	            var keyCode  =  e.keyCode||e.which; 
	            var isShift  =  e.shiftKey ||(keyCode  ==   16 ) || false ;
	            if (((keyCode >=   65   &&  keyCode  <=   90 )  &&   !isShift)
	            || ((keyCode >=   97   &&  keyCode  <=   122 )  &&  isShift))
	            {
	            	loadTooltip(o);
	            }
	            else
	            {
	            	closeTooltip(o);
	            }
            }
            else
            {
            	closeTooltip(o);
            }
        }
    </script>
</head>


<body class="<s:property value='%{#session.layoutTheme}' />">
 	<form method="post" id="mainForm" name="mainForm" onsubmit="javascript: return false;">
 	<s:token></s:token>
 	<div class="log_content" style="background:url(<s:url value='/css/%{#session.layoutTheme}/log_img/forgot_password_content_1.png' />) no-repeat center; ">
 	    <div class="log_text"><p><s:text name="ntuc.web.portal"/></p></div>
 		<table class="log_tab" cellpadding="0" cellspacing="0" border="0" >
     		<tr>
     			<td>
    				<table class="tb3" >
      					<tr>
        					<th width="97"><s:text name="changePwd.content.newPwd"/></th>
        					<td width="212"><input type="password" id="newPwd" name="newPassword" size="30" theme="simple" ></td>
      					</tr>
     				</table>
     			</td>
     		</tr>
     		<tr>
     			<td>
    				<table class="tb3" >
      					<tr>
        					<th width="97"><s:text name="changePwd.content.cfmNewPwd" /></th>
        					<td width="212"><input type="password" id="confirmPwd" name="confirmPassword" size="30" ></td>
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
