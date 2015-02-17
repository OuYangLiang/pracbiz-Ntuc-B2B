<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- TemplateBeginEditable name="doctitle" -->
    <title><s:text name="ntuc.web.portal"/></title>

    <script type="text/javascript" src="<s:url value='/js/common.js' includeParams='none'/>"></script>

    <script type="text/javascript">
    	require(["custom/B2BPortalBase","dojo/parser","dojo/on","dijit/form/Button", "dijit/Dialog","dijit/form/TextBox","dijit/Tooltip","dojo/touch","dojo/domReady!"],
            function(B2BPortalBase,parser,on,Button,Dialog,TextBox,Tooltip,touch){
                parser.parse();
                
                (new B2BPortalBase()).initLoginTemplate(
                    '<c:out value="${session.helpExHolder.helpNo}"/>',
                    '<c:out value="${session.helpExHolder.helpEmail}"/>');
                
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

		        on(dojo.byId("currPwd"), 'keypress', function(e)
   	    		{
   		        	detectCapsLock(e);
                });

   		        on(dojo.byId("currPwd"), touch.out, function(e)
   	    		{
   		        	var o = e.target||e.srcElement;
   		        	closeTooltip(o);
                });

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
 	<form method="POST" id="mainForm" >
 	<div class="log_content" style="background:url(<s:url value='/css/%{#session.layoutTheme}/log_img/change_password_content_3.png' />) no-repeat center; ">
 	    <div class="log_text"><p><s:text name="ntuc.web.portal"/></p></div>
 		<table class="log_tab" cellpadding="0" cellspacing="0" border="0" >
 			<tr>
    			<td>
    				<table style=" color:red;">
      					<tr>
        					<td><h2><s:text name="changePwd.content.ack"/></h2><td>
      					</tr>
     				</table>
     			</td>
     		</tr>
    		<tr>
    			<td>
    				<table class="tb3" >
      					<tr>
        					<th width="97"><s:text name="changePwd.content.currPwd"/></th>
        					<td width="212"><input type="password" name="currPwd" id="currPwd" size="30" theme="simple"  /></td>
      					</tr>
     				</table>
     			</td>
     		</tr>
     		<tr>
     			<td>
    				<table class="tb3" >
      					<tr>
        					<th width="97"><s:text name="changePwd.content.newPwd"/></th>
        					<td width="212"><input  type="password" name="newPwd" id="newPwd"  size="30" theme="simple" ></td>
      					</tr>
     				</table>
     			</td>
     		</tr>
     		<tr>
     			<td>
    				<table class="tb3" >
      					<tr>
        					<th width="97"><s:text name="changePwd.content.cfmNewPwd" /></th>
        					<td width="212"><input  type="password"  name="cfmNewPwd" id="cfmNewPwd"  size="30" ></td>
      					</tr>
     				</table>
     			</td>
     		</tr>
     	</table>
    	<p class="sub">
    		<button class="subbut" onClick="javascript:changePassword();" >
    			<s:text name="button.submit"/>
	        </button>
	        <button class="subbut" onClick="javascript:submitForm('mainForm','<c:url value="/home.action"/>');">
	            <s:text name="button.cancel"/>
	        </button>
    	</p>
 	</div>
	</form>
	<c:if test="${ not empty ERROR_CHANGE_PASSWORD_FAILED }">
	<script language="javascript">
		var msg = "";
		    		    		
    	msg = "<c:out value='${ERROR_CHANGE_PASSWORD_FAILED}' />";     
				
		alert(msg.replace("&#039;","\'"));
	</script>		
	</c:if>
</body>
</html>
