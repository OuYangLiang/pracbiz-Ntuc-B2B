<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <style type="text/css">
	    @import "<s:url value='/js/dojo-root/dojox/grid/resources/Grid.css' />";
	    @import "<s:url value='/js/dojo-root/dojox/grid/resources/claroGrid.css' />";
	    @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/%{#session.layoutTheme}/EnhancedGrid.css' />";
	    @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css' />";
	    @import "<s:url value='/js/dojo-root/dijit/themes/claro/claro.css' />";
	    @import "<s:url value='/js/dojo-root/dojo/resources/dojo.css' />";
	   
	    @import "<s:url value='/css/claro/overlay.css' />";
	    @import "<s:url value='/css/claro/common.css' />";
	    @import "<s:url value='/js/dojo-root/dojox/form/resources/CheckedMultiSelect.css' />";
    </style>
        
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dijit/registry",
                "dojo/on",
                "dijit/layout/TabContainer",
                "dijit/layout/ContentPane", 
                "dojo/parser",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    TabContainer, 
                    ContentPane,
                    parser
                    )
                {
                    parser.parse();
                     
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                    
				    var tab = registry.byId("tab");
				    
				    if(dom.byId("commonConfig"))
				    {
	                    on(dom.byId("commonConfig"), 'click', 
	                        function()
	                        {
	                    		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
	                    		var commonConfig = document.getElementById("commonConfigTab");
	   					   		if(commonConfig == null)
	   					   		{
	   					   	 		var commonConfig = new  dijit.layout.ContentPane({
	   						    		content:"<iframe style='width:100%; height:90%;' frameborder='0' src='<s:url value='/admin/initCommonFig.action' />' ></iframe>",
	            						title: "<s:text name='admin.config.commonConfig' />",
	   									closable: true,
	   									id: "commonConfigTab"
	            					});	
		   							tab.addChild(commonConfig);
		                            tab.selectChild(commonConfig);
		   						}
		   						else
		   						{
		   							tab.selectChild(dijit.byId("commonConfigTab"), true);
		   						}
							}
						);
				    }

                    if(dom.byId("houseKeeping"))
                    {
	                    on(dom.byId("houseKeeping"), 'click', 
	                   		function()
	                        {
	                    		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
	                       		var houseKeeping = document.getElementById("houseKeepingTab");
	      					   		if(houseKeeping == null)
	      					   		{
	      					   	 		var houseKeeping = new  dijit.layout.ContentPane({
	      						    		content:"<iframe style='width:100%; height:90%;' frameborder='0' src='<s:url value='/admin/initHsekeep.action' />' ></iframe>",
	               							title: "<s:text name='admin.config.hKeeping' />",
	      									closable: true,
	      									id: "houseKeepingTab"
	               					});	
	   	   							tab.addChild(houseKeeping);
	   	                            tab.selectChild(houseKeeping);
	   	   						}
	   	   						else
	   	   						{
	   	   							tab.selectChild(dijit.byId("houseKeepingTab"), true);
	   	   						}
	   						}
	                    );
                    }

                    if(dom.byId("passwordRule"))
                    {
	                    on(dom.byId("passwordRule"), 'click', 
	                   		function()
	                        {
	                    		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
	                       		var passwordRule = document.getElementById("passwordRuleTab");
	      					   		if(passwordRule == null)
	      					   		{
	      					   	 		var passwordRule = new  dijit.layout.ContentPane({
	      						    		content:"<iframe style='width:100%; height:90%;' frameborder='0' src='<s:url value='/admin/initPwdRule.action' />' ></iframe>",
	               							title: "<s:text name='admin.config.pwdRule' />",
	      									closable: true,
	      									id: "passwordRuleTab"
	               					});	
	   	   							tab.addChild(passwordRule);
	   	                            tab.selectChild(passwordRule);
	   	   						}
	   	   						else
	   	   						{
	   	   							tab.selectChild(dijit.byId("passwordRuleTab"), true);
	   	   						}
	   						}
	                    );
                    }

                    if(dom.byId("messageManagement"))
                    {
	                    on(dom.byId("messageManagement"), 'click', 
	                   		function()
	                        {
	                    		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
	                       		var messageManagement = document.getElementById("messageManagementTab");
	      					   		if(messageManagement == null)
	      					   		{
	      					   	 		var messageManagement = new  dijit.layout.ContentPane({
	      						    		content:"<iframe style='width:100%; height:90%;' frameborder='0' src='<s:url value='/admin/initMsg.action' />' ></iframe>",
	               							title: "<s:text name='admin.config.msgManagerment' />",
	      									closable: true,
	      									id: "messageManagementTab"
	               					});	
	   	   							tab.addChild(messageManagement);
	   	                            tab.selectChild(messageManagement);
	   	   						}
	   	   						else
	   	   						{
	   	   							tab.selectChild(dijit.byId("messageManagementTab"), true);
	   	   						}
	   						}
	                    );
                    }

                    if(dom.byId("messageSummaryPageSetting"))
                    {
	                    on(dom.byId("messageSummaryPageSetting"), 'click', 
	                   		function()
	                        {
	                    		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
	                       		var messageSummaryPageSetting = document.getElementById("messageSummaryPageSettingTab");
	  					   		if(messageSummaryPageSetting == null)
	  					   		{
	  					   	 		var messageSummaryPageSetting = new  dijit.layout.ContentPane({
	  						    		content:"<iframe style='width:100%; height:90%;' frameborder='0' src='<s:url value='/admin/initMsgSummaryPageSetting.action' />' ></iframe>",
	           							title: "<s:text name='admin.config.msgSummary.page.setting' />",
	  									closable: true,
	  									id: "messageSummaryPageSettingTab"
	               					});	
	   	   							tab.addChild(messageSummaryPageSetting);
	   	                            tab.selectChild(messageSummaryPageSetting);
	   	   						}
	   	   						else
	   	   						{
	   	   							tab.selectChild(dijit.byId("messageSummaryPageSettingTab"), true);
	   	   						}
	   						}
	                    );
                    }

                    if(dom.byId("retailerPrivilege"))
                    {
	                    on(dom.byId("retailerPrivilege"), 'click', 
	                   		function()
	                        {
	                    		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
	                       		var retailerPrivilege = document.getElementById("retailerPrivilegeTab");
	  					   		if(retailerPrivilege == null)
	  					   		{
	  					   	 		var retailerPrivilege = new  dijit.layout.ContentPane({
	  						    		content:"<iframe style='width:100%; height:90%;' frameborder='0' src='<s:url value='/admin/initEditRetailerPrivilege.action' />' ></iframe>",
	           							title: "<s:text name='admin.config.retailerPrivilege' />",
	  									closable: true,
	  									id: "retailerPrivilegeTab"
	               					});	
	   	   							tab.addChild(retailerPrivilege);
	   	                            tab.selectChild(retailerPrivilege);
	   	   						}
	   	   						else
	   	   						{
	   	   							tab.selectChild(dijit.byId("retailerPrivilegeTab"), true);
	   	   						}
	   						}
	                    );
                    }

                    if(dom.byId("emailConfig"))
				    {
	                    on(dom.byId("emailConfig"), 'click', 
	                        function()
	                        {
	                    		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
		                    	var emailConfig = document.getElementById("emailConfigTab");
	   					   		if(emailConfig == null)
	   					   		{
	   					   	 		var emailConfig = new  dijit.layout.ContentPane({
	   						    		content:"<iframe style='width:100%; height:90%;' frameborder='0' src='<s:url value='/admin/initEmailConfig.action' />' ></iframe>",
	            						title: "<s:text name='admin.config.emailConfig' />",
	   									closable: true,
	   									id: "emailConfigTab"
	            					});	
		   							tab.addChild(emailConfig);
		                            tab.selectChild(emailConfig);
		   						}
		   						else
		   						{
		   							tab.selectChild(dijit.byId("emailConfigTab"), true);
		   						}
							}
						);
				    }

                });
    </script>
</head>

<body>
	<div  data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
		<div id="tab" data-dojo-type="dijit.layout.TabContainer" style="width: 100%;" doLayout="false">
		   <!--site configuration-->
			<div data-dojo-type="dijit.layout.ContentPane" title="<s:text name="admin.config" />" selected="true">
			   <table class="commtable">
					<tr><td>
						<h2 class="tabHeader" ><s:text name="admin.config.param" /></h2>
						<ul>
						    <s:if test="#session.permitUrl.contains('/admin/initCommonFig.action')" >
							     <li><a href="#" id="commonConfig"><s:text name="admin.config.commonConfig" /></a></li>
                            </s:if>
						    <s:if test="#session.permitUrl.contains('/admin/initHsekeep.action')" >
								<li><a href="#" id="houseKeeping"><s:text name="admin.config.hKeeping" /></a></li>
                            </s:if>
						    <s:if test="#session.permitUrl.contains('/admin/initPwdRule.action')" >
								<li><a href="#" id="passwordRule"><s:text name="admin.config.pwdRule" /></a></li>
                            </s:if>
						    <s:if test="#session.permitUrl.contains('/admin/initMsg.action')" >
								<li><a href="#" id="messageManagement"><s:text name="admin.config.msgManagerment" /></a></li>
                            </s:if>
						    <s:if test="#session.permitUrl.contains('/admin/initMsgSummaryPageSetting.action')" >
								<li><a href="#" id="messageSummaryPageSetting"><s:text name="admin.config.msgSummary.page.setting" /></a></li>
                            </s:if>
						    <s:if test="#session.permitUrl.contains('/admin/initEditRetailerPrivilege.action')" >
								<li><a href="#" id="retailerPrivilege"><s:text name="admin.config.retailerPrivilege" /></a></li>
                            </s:if>
                            <s:if test="#session.permitUrl.contains('/admin/initEmailConfig.action')" >
								<li><a href="#" id="emailConfig"><s:text name="admin.config.emailConfig" /></a></li>
                            </s:if>
						</ul>
					</td>
					</tr>
			   </table>
			</div>
		</div>
	</div>
</body>
</html>
