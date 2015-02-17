<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><decorator:title /></title>

    <style type="text/css">
        @import "<s:url value='/js/dojo-root/dijit/themes/%{#session.layoutTheme}/%{#session.layoutTheme}.css' />";
        @import "<s:url value='/js/dojo-root/dojo/resources/dojo.css' />";
        @import "<s:url value='/js/dojo-root/dojox/layout/resources/ExpandoPane.css' />";
        
        @import "<s:url value='/css/%{#session.layoutTheme}/overlay.css' />";
        @import "<s:url value='/css/%{#session.layoutTheme}/common.css' />";
        @import "<s:url value='/css/%{#session.layoutTheme}/header.css' />";

      html, body {
            width: 100%;
            height: 100%;
			min-width: 1050px !important;
			overflow:auto !important;
			_width:expression((document.documentElement.clientWidth||document.body.clientWidth)<1050?"1050px":"100%"); 
		 }

        #borderContainer {
            width: 100%;
            height: 100%;
        }
        
        .pageOverlay {
		    top: 0;
		    left: 0;
		    position: absolute;
		    height: 100%;
		    width: 100%;
		    z-index: 1001;
		    display: block;
		    background: white;
		}
        
        .loadingMessage {
            margin:0 auto;
            margin-top: 260px;
            width: 10px;
            background: #fff url('<s:url value="/js/dojo-root/dijit/themes/claro/images/loadingAnimation.gif" />') no-repeat 10px 23px;
		    padding: 25px 40px;
		    color: #999;
		}
    </style>

    <script type="text/javascript" src="<s:url value='/js/dojo-root/dojo/dojo.js' />"
        data-dojo-config="parseOnLoad: false, async: true, locale: '<s:property value='%{#session.dojoLocale}' />'"></script>
        
    <script type="text/javascript" src="<s:url value='/js/common.js' />"></script>
    
    <script type="text/javascript" src="<s:url value='/js/ga.js' />"></script>
        
    <script type="text/javascript">
        require(["dijit/layout/BorderContainer","dijit/layout/ContentPane","dojox/layout/ExpandoPane","dijit/DropDownMenu",
                "dijit/MenuItem","dijit/MenuSeparator","dijit/PopupMenuItem","dijit/TitlePane"]);
    </script>
    
    <decorator:head />
</head>

<body class="<s:property value='%{#session.layoutTheme}' />">

    <div id="loadingOverlay" class="pageOverlay">
        <div style="text-align:center">
        <div class="loadingMessage">Loading...</div>
        </div>
    </div>
    
    <div data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="gutters:true, liveSplitters:false" id="borderContainer">
        <!-- Header Part -->
        <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'top', splitter:false" style="height:70px; overflow:hidden; padding:1 !important;">
            
            <div id="header" border="false"  style="height:70px;">
                <img src="<s:url value='/css/%{#session.layoutTheme}/img/PB-Logo.png' />" class="logo" width="200" height="60" style="margin-left:2px;">
                <span class= "title">
                    <s:text name="ntuc.web.portal"/>
                </span> 
                <div id="headerContent">
                    <div class="group" style="white-space:nowrap">
                        <span class="label"><s:text name="welcome.header.loginId"/> &nbsp;</span><span class="seperator">:</span><span class="data"><s:property value="%{#session.SESSION_CURRENT_USER_PROFILE.loginId}"/>(
                        <c:forEach items="${roleList}" var="role" varStatus="status">
                            <c:out value="${role.roleName}" /><span class="seperator"></span>
                        </c:forEach>
                        )</span>
                       <!-- <span class="seperator" style="float: right;"> |</span>
                        <span class="data" style="float: right;"><a href="#" onClick="changeToURL('<c:url value="/logout.action" />');" ><s:text name="welcome.header.logout"/></a></span>
                        <span class="seperator" style="float: right;">|</span>
                        <span class="data" style="float: right;"><a href="#" onClick="changeToURL('<c:url value="/myprofile/initEdit.action" />');"><s:text name="welcome.header.myprofile"/></a></span>
                        <span class="seperator" style="float: right;">|</span>
                        <span class="data" style="float: right;"><a href="#" id="helpBtn"><s:text name="welcome.header.help"/></a></span>
                        <span class="seperator" style="float: right;">|</span>-->
                    </div>
                    <div class="group">
                        <span class="label" style="float:left;"><s:text name="welcome.header.previousLoginTime"/>&nbsp;</span><span class="seperator" style="float:left;">:</span><span class="data" style="float:left;"><fmt:formatDate value="${session.SESSION_CURRENT_USER_PROFILE.lastLoginDate}" pattern="dd/MM/yyyy HH:mm:ss" /></span>
                        <span class="seperator" style="float: right;"> |</span>
                        <span class="data" style="float: right;"><a href="#" onClick="changeToURL('<c:url value="/main.action" />');" ><s:text name="welcome.header.welcome.page"/></a></span>
                    </div>
                    <div class="group">
                        <span class="label" style="float:left;"><s:text name="welcome.header.currentTime"/></span><span class="seperator" style="float:left;">:</span><span class="data" style="float:left;" id="clock"></span>
                        <span class="seperator" style="float: right;"> |</span>
                        <span class="data" style="float: right;"><a href="#" onClick="changeToURL('<c:url value="/logout.action" />');" ><s:text name="welcome.header.logout"/></a></span>
                        <span class="seperator" style="float: right;">|</span>
                        <span class="data" style="float: right;"><a href="#" onClick="changeToURL('<c:url value="/myprofile/initEdit.action" />');"><s:text name="welcome.header.myprofile"/></a></span>
                        <span class="seperator" style="float: right;">|</span>
                        <span class="data" style="float: right;"><a href="#" id="helpBtn"><s:text name="welcome.header.help"/></a></span>
                        <span class="seperator" style="float: right;">|</span>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Menu Part -->
        <div data-dojo-type="dojox/layout/ExpandoPane" data-dojo-props="title: 'Menu', maxWidth:175, splitter:false, region:'left'" style="width: 175;">
            <div data-dojo-type="dijit/DropDownMenu" style="margin-left:25px;margin-top:25px;">
                <c:forEach items="${MENU}" var="parent" varStatus="status">
                    <c:if test="${parent.childModules == null}" >
                        <div data-dojo-type="dijit/MenuItem" data-dojo-props="onClick:function(){changeToURL('<c:url value="${parent.moduleLink}" />')}"><c:out value="${parent.moduleTitleAftKey}" /></div>
                    </c:if>
                    <c:if test="${parent.childModules != null}" >
                       <div data-dojo-type="dijit/PopupMenuItem">
                            <span><c:out value="${parent.moduleTitleAftKey}" /></span>
                            <div data-dojo-type="dijit/DropDownMenu">
                                <c:forEach items="${parent.childModules}" var="child" varStatus="s">
                                    <div data-dojo-type="dijit/MenuItem" data-dojo-props="onClick:function(){changeToURL('<c:url value="${child.moduleLink}" />')}"><c:out value="${child.moduleTitleAftKey}" /></div>
			                        <c:if test="${!s.last}">
				                        <div data-dojo-type="dijit/MenuSeparator"></div>
				                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${!status.last}">
                        <div data-dojo-type="dijit/MenuSeparator"></div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
        
        <!-- Content Part -->
        <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'center'" >
            <decorator:body />
        </div>
    </div>
</body>
</html>
