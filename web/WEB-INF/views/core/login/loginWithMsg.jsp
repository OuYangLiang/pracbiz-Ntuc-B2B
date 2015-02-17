<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- TemplateBeginEditable name="doctitle" -->
	<title><s:text name="ntuc.web.portal"/></title>
    
	<script type="text/javascript">
		var code = null;
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
	<div class="log_content" style="background:url(<s:url value='/css/%{#session.layoutTheme}/log_img/login_content_2.png' />) no-repeat center; ">
	    <div class="log_text_msg"><p><s:text name="ntuc.web.portal"/></p></div>
    	<table style="width:1024px; margin:0 auto; padding-top:0;margin-top:0px; ">
    		<tr>
    			<td width="50%">
    				<span class="title" style="float: right; margin-left: 10px; margin-top: 0px;width: 90%">
    					<table width="91%"  border="0" cellpadding="0" cellspacing="0">
							<tr height="4%"><td>&nbsp; </td></tr>
            				<tr>
              					<td height="138" valign="top">
			   						<table width="95%" border="0" cellpadding="0" cellspacing="0">
                						<tr>
                  							<td height="198" align="right" valign="top">
				  								<table width="100%" border="0" cellpadding="10" cellspacing="10">
                    								<tr>
                    									<td>
					   										<div style="height: 350px; overflow-y: scroll; border: 1px solid rgb(255, 255, 255);">
					   											<c:forEach items="${messages}" var="msg" varStatus="status">
					   												<li><c:out value="${msg.title}"/></li>
					   												<p><c:out value="${msg.content}"/></p>
					   											</c:forEach>
                      										</div>
                   										</td>
                   									</tr>
                  								</table>
				  							</td>
                						</tr>
              						</table>
              					</td>
            				</tr>
            			</table>
    				</span>
    			</td>
    			<td>
    				<span class="title" style="float: right; margin-right: 115px;">
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
    				</span>
    			</td>
    		</tr>
    	</table>
  	</div>
	</form>
	<c:if test="${ not empty ERROR_LOGIN_FAILED }">
	<script language="javascript">
		var msg = "";
		    		    		
    	msg = "<c:out value='${ERROR_LOGIN_FAILED}' />";        	
    								
		alert(msg.replace("&#039;","\'"));
	</script>		
	</c:if>
</body>
</html>
