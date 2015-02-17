<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- TemplateBeginEditable name="doctitle" -->
    <title><s:text name="ntuc.web.portal"/></title>

    <script type="text/javascript">
        require(["dijit/registry","dojo/on","dojo/parser", "custom/B2BPortalBase","dijit/form/TextBox","dijit/Tooltip","dojo/touch","dojo/domReady!"],
             function(registry,on,parser,B2BPortalBase,TextBox,Tooltip,touch)
             {
             	parser.parse();

             	(new B2BPortalBase()).initLoginTemplate(
                        '<s:text name="login.help.helpNo"/>',
                        '<c:out value="${session.helpExHolder.helpNo}"/>',
                        '<s:text name="login.help.helpEmail"/>',
                        '<c:out value="${session.helpExHolder.helpEmail}"/>');
                
             	on(registry.byId("submitBtn"), 'click', 
             		function ()
             		{
             			submitForm('mainForm', '<s:url value="/user/savePassword.action" />');
             		});
             		
                on(registry.byId("backBtn"), 'click', 
                    function ()
                    {
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

   	   		    on(dojo.byId("cfmNewPwd"), 'keypress', function(e)
   	    		{
   		        	detectCapsLock(e);
                });

   		        on(dojo.byId("cfmNewPwd"), touch.out, function(e)
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
 	<form method="post" id="mainForm" name="mainForm" onsubmit="javascript: return false;">
 	<s:token />
 	<div class="log_content" style="background:url(<s:url value='/css/%{#session.layoutTheme}/log_img/SET_password_content.png' />) no-repeat center; ">
 	    <div class="log_text"><p><s:text name="ntuc.web.portal"/></p></div>
    	<table class="log_tab" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
				<table class="tb3">
					<tr>
     					<th width="97">
						<span> 
							<s:text name="user.setPwd.content.newPwd" /> &nbsp;&nbsp;
						</span>
						</th>
     					<td width="212"><input type="password" id="newPwd" name="newPwd" size="30" theme="simple"></td>
   					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
				<table class="tb3">
					<tr>
     					<th width="97">
						<span> 
							<s:text name="user.setPwd.content.cfmNewPwd" /> &nbsp;&nbsp;
						</span>
						</th>
     					<td width="212"><input type="password" id="cfmNewPwd" name="cfmNewPwd" size="30" theme="simple"></td>
   					</tr>
				</table>
				</td>
			</tr>
			</table>
    	<p class="sub">
    		<button data-dojo-type="dijit.form.Button" id="submitBtn" >
				<s:text name="button.submit"/>
			</button>
	        <button data-dojo-type="dijit.form.Button" id="backBtn">
	            <s:text name="button.cancel"/>
	        </button>
    	</p>
  	</div>
  	
</form>
<c:if test="${ not empty ERROR_SET_PASSWORD_FAILED }">
	<script language="javascript">
		var msg = "";
		    		    		
    	msg = '<c:out value="${ERROR_SET_PASSWORD_FAILED}" />';        	
    								
		alert(msg);

		<s:if test="#request.CLOSE_WINDOW">
			changeToURL('<s:url value="/home.action" />');
		</s:if>
	</script>		
</c:if>
<c:if test="${ not empty SET_PASSWORD_SUCCESSFULLY }">
	<script language="javascript">
		var msg = "";
		    		    		
    	msg = '<c:out value="${SET_PASSWORD_SUCCESSFULLY}" />';        	
    								
		alert(msg);
		changeToURL('<s:url value="/home.action" />');
	</script>		
</c:if>
</body>
</html>
