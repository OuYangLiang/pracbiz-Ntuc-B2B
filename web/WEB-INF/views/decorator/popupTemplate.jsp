<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><decorator:title /></title>
    <style type="text/css">
        @import "<s:url value='/js/dojo-root/dijit/themes/%{#session.layoutTheme}/%{#session.layoutTheme}.css' />";
        @import "<s:url value='/js/dojo-root/dojo/resources/dojo.css' />";
        @import "<s:url value='/js/dojo-root/dojox/layout/resources/ExpandoPane.css' />";
        @import "<s:url value='/css/%{#session.layoutTheme}/header.css' />";
        @import "<s:url value='/css/%{#session.layoutTheme}/overlay.css' />";
        @import "<s:url value='/css/%{#session.layoutTheme}/common.css' />";
        
        html, body {
            width: 100%;
            height: 100%;
            margin: 0;
            overflow:hidden;
        }

        #borderContainer {
            width: 100%;
            height: 100%;
        }
    </style>

    <script type="text/javascript" src="<s:url value='/js/dojo-root/dojo/dojo.js' />"
        data-dojo-config="parseOnLoad: false, async: true, locale: '<s:property value='%{#session.dojoLocale}' />'"></script>
        
    <script type="text/javascript" src="<s:url value='/js/common.js' />"></script>
    <script type="text/javascript" src="<s:url value='/js/ga.js' />"></script>
        
    <script type="text/javascript">
        require(["dijit/layout/BorderContainer","dijit/layout/ContentPane","dijit/TitlePane"]);
    </script>
    
    <decorator:head />
</head>

<body class="<s:property value='%{#session.layoutTheme}' />">
    
    <div data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="gutters:true, liveSplitters:false" id="borderContainer">
        <!-- Header Part -->
        <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'top', splitter:false" style="height:70px; overflow:hidden;">
			<div id="header" border="false"  style="height:70px;">
   		 		<img src="<s:url value='/css/%{#session.layoutTheme}/img/PB-Logo.png' />" class="logo">
   		 		<span class= "title"><s:text name="ntuc.web.portal"/></span>
   		 	</div>
        </div>

        
        <!-- Content Part -->
        <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'center'">
            <decorator:body />
        </div>
    </div>
</body>
</html>
