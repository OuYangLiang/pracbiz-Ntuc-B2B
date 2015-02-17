<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>
<!DOCTYPE html>
<html>
<head>
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
     <script type="text/javascript" src="<s:url value='/js/dojo-root/dojo/dojo.js' />"
        data-dojo-config="parseOnLoad: false, async: true, isDebug: true"></script>
     <script type="text/javascript">
     require(
             [
             "dojo/dom",
             "dijit/registry",
             "dojo/string",
             "dijit/layout/ContentPane", 
             "dojo/parser",
             "dojo/_base/xhr",
             "custom/InformationDialog",
             "dijit/TitlePane",
             "dijit/form/CheckBox",
             "dijit/form/TextBox",
             "dijit/form/Select",
             "dojo/on",
             "dijit/form/RadioButton",
             "dojo/dom-style",
             "dijit/Tooltip",
             "dojo/touch",
             "custom/B2BPortalBase",
             "dojo/domReady!"
             ], 
             function(
                 dom,
                 registry,
                 string,
                 ContentPane,
                 parser,
                 xhr,
                 InformationDialog,
                 TitlePane,
                 CheckBox,
                 TextBox,
                 Select,
                 on,
                 RadioButton,
                 domStyle,
                 Tooltip,
                 touch,
                 B2BPortalBase
                 )
             {
            parser.parse();
            
	        reset = function()
            {
                xhr.post({
                    url: '<s:url value="/admin/resetEmailConfig.action" />',
                    handleAs: "json",
                    load: function(jsonData)
                    {
                    	registry.byId("smtpHost.stringValue").set('value',jsonData.smtpHost.stringValue);
                    	registry.byId("smtpUser.stringValue").set('value',jsonData.smtpUser.stringValue);
                    	registry.byId("smtpPassword.stringValue").set('value',jsonData.smtpPassword.stringValue);
                    	registry.byId("smtpProtocol.stringValue").set('value',jsonData.smtpProtocol.stringValue);
                    	registry.byId("needAuth.valid").set('value',jsonData.needAuth.valid);
                    	registry.byId("needEhlo.valid").set('checked',jsonData.needEhlo.valid);
                    	registry.byId("connectType.stringValue").set('value',jsonData.connectType.stringValue);
                    	registry.byId("socketFacClass.stringValue").set('value',jsonData.socketFacClass.stringValue);
                    	registry.byId("socketFacFallback.valid").set('checked',jsonData.socketFacFallback.valid);
                        registry.byId("senderName.stringValue").set('value',jsonData.senderName.stringValue);
                        registry.byId("senderAddr.stringValue").set('value',jsonData.senderAddr.stringValue);
                        registry.byId("replyToAddr.stringValue").set('value',jsonData.replyToAddr.stringValue);
                        registry.byId("adminAddr.stringValue").set('value',jsonData.adminAddr.stringValue);
                        registry.byId("authMechanisms.stringValue").set('value',jsonData.authMechanisms.stringValue);

                        on.once(registry.byId("resetBtn"), 'click', function()
                                {
                                	reset();
                                });
                    }
                });
            };


            action = function(url, test)
            {
            	var smtpHost = string.trim(dom.byId("smtpHost.stringValue").value);
                var smtpPort = string.trim(dom.byId("smtpPort.stringValue").value);
                var smtpUser = string.trim(dom.byId("smtpUser.stringValue").value);
                var smtpPassword = string.trim(dom.byId("smtpPassword.stringValue").value);
                var smtpProtocol = string.trim(dom.byId("smtpProtocol.stringValue").value);
                var needAuth = dom.byId("needAuth.valid").checked;
                var authMechanisms = null;
                registry.byId("authMechanisms.stringValue").options.forEach(function(item, index){
                    if(item.selected)
                    {
                    	authMechanisms = string.trim(item.value);
                    }
                });
                var needEhlo = dom.byId("needEhlo.valid").checked;
                var connectType = null;
                registry.byId("connectType.stringValue").options.forEach(function(item, index){
                    if(item.selected)
                    {
                    	connectType = string.trim(item.label);
                    }
                });
                var socketFacClass = string.trim(dom.byId("socketFacClass.stringValue").value);
                var socketFacFallback = dom.byId("socketFacFallback.valid").checked;
                var senderName = dom.byId("senderName.stringValue").value;
                var senderAddr = dom.byId("senderAddr.stringValue").value;
                var replyToAddr = string.trim(dom.byId("replyToAddr.stringValue").value);
                var adminAddr = string.trim(dom.byId("adminAddr.stringValue").value);
                var regEmail = new RegExp('^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$');
                var regPort = new RegExp('^[0-9]+$')
              
                
                if(smtpHost.length == 0 || smtpHost == '')
                {
                	 var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0176'/>"});
                     infoDialog.show();
                     return;
                }

				if(smtpPort.length == 0 || smtpPort == '')
				{
					 var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0177'/>"});
                     infoDialog.show();
                     return;
				}
	
				if(smtpUser.length == 0 || smtpUser == '')
				{
					 var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0178'/>"});
                     infoDialog.show();
                     return;
				}

				if(needAuth != '' && (smtpPassword.length == 0 || smtpPassword == ''))
				{
					var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0179'/>"});
                    infoDialog.show();
                    return;
				}

				if(smtpProtocol.length == 0 || smtpProtocol == '')
				{
					 var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0180'/>"});
                     infoDialog.show();
                     return;
				}
                
                if(smtpUser.length > 0 && !regEmail.test(smtpUser)) 
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0181'/>"});
                    infoDialog.show();
                    return;
                }

                if(senderAddr.length > 0 && !regEmail.test(senderAddr)) 
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0182'/>"});
                    infoDialog.show();
                    return;
                }

                if(replyToAddr.length > 0 && !regEmail.test(replyToAddr)) 
                {
                	var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0183'/>"});
                    infoDialog.show();
                    return;
                }
	
                if(adminAddr.length > 0) 
                {
                    var adminAddrs = adminAddr.split(",");
                    console.log(adminAddrs.length);
                    if(adminAddrs.length > 0)
                    {
                        for(var i = 0 ; i < adminAddrs.length; i++)
                        {
                            if(!regEmail.test(adminAddrs[i]))
                            {
                            	var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0184'/>"});
                                infoDialog.show();
                                return;
                            }
                        }
                    }
                }

                if(smtpPort.length > 0 && !regPort.test(smtpPort)) 
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0185'/>"});
                    infoDialog.show();
                    return;
                }

                if(connectType=="SSL" && socketFacClass=='')
                {
                	var infoDialog = new InformationDialog({message: "Using SSL needs Socket Factory Class."});//"<s:text name='B2BPA0186'/>"
                    infoDialog.show();
                    return;
                }
                var csrfToken = document.getElementById("csrfToken").value;
                xhr.post({
                    url: url,
                    content: {
                    	"smtpHost.stringValue" : smtpHost,
                    	"smtpPort.stringValue" : smtpPort,
                        "smtpUser.stringValue" : smtpUser,
                        "smtpPassword.stringValue" : smtpPassword,
                        "smtpProtocol.stringValue" : smtpProtocol,
                        "needAuth.valid" : needAuth,
                        "needEhlo.valid" : needEhlo,
                        "connectType.stringValue" : connectType,
                        "socketFacClass.stringValue" : socketFacClass,
                        "socketFacFallback.valid" : socketFacFallback,
                        "senderName.stringValue" : senderName,
                        "senderAddr.stringValue" : senderAddr,
                        "replyToAddr.stringValue" : replyToAddr,
                        "adminAddr.stringValue" : adminAddr,
                        "authMechanisms.stringValue" : authMechanisms,
                        "csrfToken" : csrfToken
                          },
                    handleAs: "json",
                    load: function(jsonData)
                    {
                         console.log(jsonData);
                         var infoDialog = new InformationDialog({message: jsonData});
                         infoDialog.show();

                         if (test)
                         {
                        	  on.once(registry.byId("testBtn"), 'click', function()
                                      {
                                     		testEmail('<s:url value="/admin/testEmailForConfig.action" />');
                                      });
                         }
                         else
                         {
                        	   on.once(registry.byId("saveBtn"), 'click', function()
                                       {
                                       		save('<s:url value="/admin/saveEmailConfig.action" />');
                                       });
                         }
                    }
                });
            }


            testEmail = function(url){
                action(url, true);
            };


            save = function(url){
                action(url, false);
            };

            on.once(registry.byId("testBtn"), 'click', function()
                {
            		(new B2BPortalBase()).resetTimeout(
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
               		testEmail('<s:url value="/admin/testEmailForConfig.action" />');
                });
            
            on.once(registry.byId("saveBtn"), 'click', function()
                {
            		(new B2BPortalBase()).resetTimeout(
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                	save('<s:url value="/admin/saveEmailConfig.action" />');
                });


            on.once(registry.byId("resetBtn"), 'click', function()
                {
               
                	reset();
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

			on(dojo.byId("smtpPassword.stringValue"), 'keypress', function(e)
	   		{
	        	detectCapsLock(e);
	        });
	
	        on(dojo.byId("smtpPassword.stringValue"), touch.out, function(e)
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
<body class="claro">
    <table class="btnContainer" style="width:97%" >
         <tbody><tr><td>
              <s:if test="#session.permitUrl.contains('/admin/saveEmailConfig.action')" >
              	  <button data-dojo-type="dijit.form.Button" id="testBtn" ><s:text name="button.sentTestMsg" /></button>
	              <button data-dojo-type="dijit.form.Button"  id="saveBtn"><s:text name="button.save" /></button>
	              <button data-dojo-type="dijit.form.Button" id="resetBtn"><s:text name="button.reset" /></button>
              </s:if>
          </td></tr></tbody>
    </table>
    <div>
    	<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    </div>
    <div  data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
		<div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.emailConfig.summary"/>', width:275" style="width:99%">
			<table class="commtable">
				<tr>
				   <td><span class="required">*</span></td>
                   <td width="50%"><s:text name="admin.emailConfig.summary.smtpHost"/></td>
                   <td>:</td>
                   <td><s:textfield id="smtpHost.stringValue" name="smtpHost.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="50"/></td>
                </tr>
                <tr>
				   <td><span class="required">*</span></td>
                   <td width="50%"><s:text name="admin.emailConfig.summary.smtpPort"/></td>
                   <td>:</td>
                   <td><s:textfield id="smtpPort.stringValue" name="smtpPort.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="10"/></td>
                </tr>
                <tr>
				   <td><span class="required"></span></td>
                   <td><s:text name="admin.emailConfig.summary.needAuth"/></td>
                   <td>:</td>
                   <td><s:checkbox  id="needAuth.valid" name="needAuth.valid" theme="simple" data-dojo-type="dijit.form.CheckBox"></s:checkbox></td>
                </tr>
                <tr>
				   <td><span class="required"></span></td>
                   <td><s:text name="admin.emailConfig.summary.AuthMechanisms"/></td>
                   <td>:</td>
                   <td><s:select  id="authMechanisms.stringValue" name="authMechanisms.stringValue" listKey="key" listValue="value" list="mechanisms" data-dojo-type="dijit.form.Select"  theme="simple"></s:select></td>
                </tr>
				<tr>
				   <td><span class="required">*</span></td>
                   <td><s:text name="admin.emailConfig.summary.smtpUser"/></td>
                   <td>:</td>
                   <td><s:textfield id="smtpUser.stringValue" name="smtpUser.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="50"/></td>
              	</tr>
				<tr>
				   <td><span class="required"></span></td>
                   <td><s:text name="admin.emailConfig.summary.smtpPassword"/></td>
                   <td>:</td>
                   <td><input type="password" id="smtpPassword.stringValue" name="smtpPassword.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: false" maxlength="50" value="<s:property value='smtpPassword.stringValue'/>" /></td>
              	</tr>
				<tr>
				   <td><span class="required">*</span></td>
                   <td><s:text name="admin.emailConfig.summary.smtpProtocol"/></td>
                   <td>:</td>
                   <td><s:textfield id="smtpProtocol.stringValue" name="smtpProtocol.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" maxlength="255"/></td>
                </tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.emailConfig.summary.needEhlo"/></td>
                   <td>:</td>
                   <td><s:checkbox id="needEhlo.valid" name="needEhlo.valid" theme="simple" data-dojo-type="dijit.form.CheckBox"/></td>
                </tr>
                <tr>
                   <td><span class="required">*</span></td>
                   <td><s:text name="admin.emailConfig.summary.connectType"/></td>
                   <td>:</td>
                   <td><s:select  id="connectType.stringValue" name="connectType.stringValue" listKey="key" listValue="value" list="connectTypes" data-dojo-type="dijit.form.Select" theme="simple"></s:select></td>
                </tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.emailConfig.summary.socketFacClass"/></td>
                   <td>:</td>
                   <td><s:textfield id="socketFacClass.stringValue" name="socketFacClass.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/></td>
                </tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.emailConfig.summary.socketFacFallback"/></td>
                   <td>:</td>
                   <td><s:checkbox id="socketFacFallback.valid" name="socketFacFallback.numValue" theme="simple" data-dojo-type="dijit.form.CheckBox" data-dojo-props="required: true" /></td>
                </tr>
				<tr>
				   <td><span class="required"></span></td>
                   <td><s:text name="admin.emailConfig.summary.senderName"/></td>
                   <td>:</td>
                   <td><s:textfield id="senderName.stringValue" name="senderName.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="50"/></td>
              	</tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.emailConfig.summary.senderAddr"/></td>
                   <td>:</td>
                   <td><s:textfield id="senderAddr.stringValue" name="senderAddr.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/></td>
                </tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.emailConfig.summary.replyToAddr"/></td>
                   <td>:</td>
                   <td><s:textfield id="replyToAddr.stringValue" name="replyToAddr.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/></td>
                </tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.emailConfig.summary.adminAddr"/></td>
                   <td>:</td>
                   <td><s:textfield id="adminAddr.stringValue" name="adminAddr.stringValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="255"/>(comma delimited)</td>
                </tr>
            </table>
		</div>
	</div>
</body>
</html>
