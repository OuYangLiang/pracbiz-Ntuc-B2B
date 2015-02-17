<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- TemplateBeginEditable name="doctitle" -->
    <title><decorator:title /></title>

    <style type="text/css">
    @import "<s:url value='/js/dojo-root/dojo/resources/dojo.css' />";
    @import "<s:url value='/js/dojo-root/dijit/themes/%{#session.layoutTheme}/%{#session.layoutTheme}.css' />";
    @import "<s:url value='/css/%{#session.layoutTheme}/login.css' />";
    @import "<s:url value='/css/%{#session.layoutTheme}/login_new.css' />";
    </style>
    
    <script type="text/javascript" src="<s:url value='/js/dojo-root/dojo/dojo.js' />"
        data-dojo-config="parseOnLoad: true, async: true, locale: '<s:property value='%{#session.dojoLocale}' />'">
    </script>
    
    <script type="text/javascript" src="<s:url value='/js/common.js' includeParams='none'/>"></script>
    <script type="text/javascript" src="<s:url value='/js/ga.js' />"></script>
    
    <script type="text/javascript" >
        function refreshWithParam(locale)
	    {
	        var originalUrl = window.location.href;
	        
	        if (originalUrl.indexOf('?') != -1)
	        {
	            originalUrl = originalUrl.substring(0, originalUrl.indexOf('?'));
	        }
	        
	        changeToURL(originalUrl + '?' + locale);
	    }
    </script>
     
    <decorator:head />
</head>


<body  class="<s:property value='%{#session.layoutTheme}' />" style="text-align:center;">
 	<div class="wrapper">
 		<!-- header -->
 		<div class="log_header">
 			<table style="width:1024px; margin:0 auto; padding-top:0;margin-top:0px; align:center; ">
 				<tr>
 					<td>
 						<img src="<s:url value='/css/%{#session.layoutTheme}/img/PB-Logo.png' />" width="200" height="60" style="margin-left:2px; margin-top: 25px;">
 					</td>
 					<td>
 						<span class="title" style="float: right; margin-right: 4px; margin-top: 35px;">
 							<img onmouseover="this.style.cursor='pointer'"  src="<s:url value='/css/%{#session.layoutTheme}/log_img/help_balloon.png' />" width="55" height="55" id="helpBtn" >
            			</span>
 					</td>
 				</tr>
 			</table>
 		</div>
 		
 		<!-- content -->
 		<decorator:body />
 		
 		<!-- footer -->
 		<div class="log_footer_line" >
		</div>
		
		<div class="log_footer">
			<table style="width:1024px; margin:0 auto; padding-top:0;margin-top:0px; ">
				<tr>
 					<td>
 						<!-- <span class="data"><a href="javascript:refreshWithParam('request_locale=en_US');" >&nbsp;<s:text name="changePwd.footer.english"/></a></span>
    					<span class="seperator">&nbsp;|&nbsp;</span><span class="data"><a href="javascript:refreshWithParam('request_locale=zh_CN');"><s:text name="changePwd.footer.chinese"/></a></span>
    					<span class="seperator">&nbsp;|&nbsp;</span><span class="data"><a href="javascript:refreshWithParam('request_locale=zh_CN');"><s:text name="changePwd.footer.malaysia"/></a></span>
 					     -->
 					</td>
 					<td>
 						<span class="title" style="float: left; margin-left: 4px;">
 						     <s:text name="changePwd.footer.recommendedBrowser"/>
 					    </span>
 						<span class="title" style="float: right; margin-right: 4px;">
 						     <s:text name="changePwd.footer.poweredByPracbiz"/>
 					    </span>
 					</td>
 				</tr>
 			</table>
		</div>
 	</div>
</body>
</html>
