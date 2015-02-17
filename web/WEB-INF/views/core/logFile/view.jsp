<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        require(
                [
                "custom/B2BPortalBase",
				"dojo/dom",
				"dijit/registry",
				"dojo/on",
				"dijit/form/Textarea",
				"dijit/form/NumberSpinner",
                "dojo/_base/xhr",
				"dojo/parser",
				"dojo/domReady!"
				], 
				function(
				    B2BPortalBase,
				    dom,
				    registry,
				    on,
				    Textarea,
				    NumberSpinner,
                    xhr,
				    parser
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');

                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                    		changeToURL('<s:url value="/log/init.action?keepSp=Y" />');
                        }
                    );

                    if (dom.byId("downloadBtn"))
                    {
	                    on(registry.byId("downloadBtn"), 'click', 
                    		function()
                            {
	                    		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
                                var oids = '<s:property value="logFile.hashCode" />';
                                
                                xhr.get({
                                        url: '<s:url value="/log/putParamIntoSession.action" />',
                                        content: {selectedFiles: oids},
                                        load: function(data)
                                        {
                                           	changeToURL('<s:url value="/log/saveDownload.action" />');
                                        }
                                    });
                            }
	                    );
                    }
                    
                    if (dom.byId("refreshBtn"))
                    {
	                    on(registry.byId("refreshBtn"), 'click', 
                    		function()
                            {
	                    		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
                            	var sl = dom.byId('startLine').value;
                            	var el = dom.byId('endLine').value;
                            	if (sl.indexOf(',') != -1)
                            	{
                                	sl = sl.replace(',','');
                            	}
                            	if (el.indexOf(',') != -1)
                            	{
                                	el = el.replace(',','');
                            	}
		                    	xhr.get({
	                                url: '<s:url value="/log/ajaxView.action" />?logFile.hashCode=' + '<s:property value="logFile.hashCode" />',
	                                handleAs: "json",
	                                content: {startLine: sl, endLine: el},
	                                load: function(data)
	                                {
		                                dom.byId('stringContent').value=data.stringContent;
		                                dom.byId('fileSize').innerHTML=data.fileSize + " (kb)";
		                                dom.byId('lastUpdate').innerHTML=data.lastModifiedTime;
		                                dom.byId('totalLine').innerHTML=data.totalLine;
	                                }
	                            });
                            }
	                    );
                    }
                    
                });
    </script>
</head>

<body class="claro">
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/log/view.action')" >
            <button id="refreshBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.refresh' /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/log/saveDownload.action')" >
            <button id="downloadBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.download' /></button>
            </s:if>
            <button id="cancelBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.cancel' /></button>
        </td></tr></tbody></table>
    </div>
	<!-- here is message area -->
	 
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <div class="pageBar">
        <div class="title"><s:text name='log.view.pageBar' /></div>
    </div>
    <form id="mainForm" name="mainForm" method="post" >
    	<table class="commtable">
            <tbody> 
                <tr>
                	<td></td>
                    <td width="20%"><s:text name='log.view.fileName' /></td>
                    <td>:</td>
                    <td>
                    	<s:property value="logFile.fileName" />
                    </td>
                </tr>
                <tr>
                	<td></td>
                    <td><s:text name='log.view.fileSize' /></td>
                    <td>:</td>
                    <td>
                    	<div id="fileSize"><s:property value="logFile.fileSize" /> (kb)</div>
                    </td>
                </tr>
                <tr>
                	<td></td>
                    <td><s:text name='log.view.lastUpdate' /></td>
                    <td>:</td>
                    <td>
                    	<div id="lastUpdate"><s:date name="logFile.lastModifiedTime" format="yyyy-MM-dd HH:mm:ss" /></div>
                    </td>
                </tr>
                <tr>
                	<td></td>
                    <td><s:text name='log.view.totalLine' /></td>
                    <td>:</td>
                    <td>
                    	<div id="totalLine"><s:property value="logFile.totalLine" /></div>
                    </td>
                </tr>
                <tr>
                	<td></td>
                    <td><s:text name='log.view.fileLine' /></td>
                    <td>:</td>
                    <td>
                    	<label for="startLine"><s:text name='log.view.searcharea.fromLine' />:</label>
                    	<s:textfield name="startLine" id="startLine" data-dojo-type="dijit.form.NumberSpinner" constraints="{min:1, places:0}"  theme="simple" />
                    	<label for="endLine"><s:text name='log.view.searcharea.toLine' />:</label>
                    	<s:textfield name="endLine" id="endLine" data-dojo-type="dijit.form.NumberSpinner" constraints="{min:1}"  theme="simple" />
                    </td>
                </tr>
                <tr>
                	<td></td>
                    <td style="vertical-align:text-top; line-height:23px;">
                    	<s:text name='log.view.fileContent' /><br/>
                    	<span class="required"><s:text name='log.view.fileContent.default' /></span>
                    </td>
                    <td></td>
                    <td style="font:Arial, Helvetica, sans-serif; ">
                    	<s:textarea name="logFile.stringContent" id="stringContent" cols="120" rows="30" data-dojo-type="dijit.form.Textarea"></s:textarea>
                    </td>
                </tr>
            </tbody>
        </table>
    </form>
</body>
</html>
