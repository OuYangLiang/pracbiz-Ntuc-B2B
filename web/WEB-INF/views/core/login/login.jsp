<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- TemplateBeginEditable name="doctitle" -->
    <title><s:text name="ntuc.web.portal"/></title>

    
    <script type="text/javascript">
        require(["custom/B2BPortalBase","dojo/parser","dojo/on","dojo/dom","dojo/string","dijit/Tooltip","dojo/touch","dojo/domReady!"],
            function(B2BPortalBase,parser,on,dom,string,Tooltip,touch)
            {
                parser.parse();
                
                (new B2BPortalBase()).initLoginTemplate(
                    '<c:out value="${session.helpExHolder.helpNo}"/>',
                    '<c:out value="${session.helpExHolder.helpEmail}"/>');
                    
                on(dom.byId("resetBtn"), "click", function(){
                    dom.byId("loginId").value="";
                    dom.byId("loginPwd").value="";
                });
                
                on(dom.byId("loginBtn"), "click", function(){
                    if(string.trim(dom.byId("loginId").value) == "" )
		            {
		                alert('<s:text name="B2BPC0724"/>');
		                return;
		            }
		            
		            if(string.trim(dom.byId("loginPwd").value) == "")
		            {
		                alert('<s:text name="B2BPC0725"/>');
		                return;
		            }
		            
		            submitForm("mainForm", '<s:url value="/login.action"/>');
                });
                
                dom.byId("loginId").focus();
                
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

		        on(dojo.byId("loginPwd"), 'keypress', function(e)
   	    		{
   		        	detectCapsLock(e);
                });

   		        on(dojo.byId("loginPwd"), touch.out, function(e)
   	    		{
   		        	var o = e.target||e.srcElement;
   		        	closeTooltip(o);
                });
            });
        
        
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
 	<form method="post" id="mainForm" name="mainForm" onsubmit="javascript: return false;" autocomplete="OFF">
    <div class="log_content" style="background:url(<s:url value='/css/%{#session.layoutTheme}/log_img/login_content_1.png' />) no-repeat center; ">
        <div class="log_text"><p><s:text name="ntuc.web.portal"/></p></div>
    	<table class="log_tab" cellpadding="0" cellspacing="0" border="0">
    		<tr>
    			<td>
    				<table class="tb2" >
      					<tr>
        					<th width="97"><s:text name="login.content.loginId"/></th>
        					<td width="212"><input id="loginId" name="loginId" size="30" ></td>
      					</tr>
     				</table>
     			</td>
     		</tr>
     		<tr>
     			<td>
    				<table class="tb3" >
      					<tr>
        					<th width="97"><s:text name="login.content.password"/></th>
        					<td width="212"><input type="password" id="loginPwd" name="password" size="30" ></td>
      					</tr>
     				</table>
     			</td>
     		</tr>
     	</table>
    	<p class="sub">
    		<button id="loginBtn" class="subbut">
    			<s:text name="button.login"/>
	        </button>
	        <button id="resetBtn" class="subbut">
	            <s:text name="button.login.reset"/>
	        </button>
    	</p>
    	<p class="passlink">
    		<a href="<s:url value='/forgetPassword/init.action'/>" ><s:text name="login.content.forgotPwd"/></a>
    	</p>
  	</div>
	</form>
	<c:if test="${ not empty ERROR_LOGIN_FAILED }">
	<script language="javascript">
		var msg = "";
		    		    		
    	msg = '<c:out value="${ERROR_LOGIN_FAILED}" />';        	
    								
		alert(msg.replace("&#039;","\'"));
	</script>		
</c:if>
</body>
</html>
