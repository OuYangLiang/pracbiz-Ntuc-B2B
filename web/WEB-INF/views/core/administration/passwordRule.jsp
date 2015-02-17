<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

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
                  TextBox
              )
             {
            parser.parse();
	        save = function()
            {
                var maxLength = string.trim(dom.byId("maxLength.numValue").value);
                var minLength = string.trim(dom.byId("minLength.numValue").value);
                var alphanumeric = dom.byId("alphanumeric.valid").checked;
                var noRepeat = dom.byId("noRepeat.valid").checked;
                var mixtureCase = dom.byId("mixtureCase.valid").checked;
                var specialCharacter = dom.byId("specialCharacter.valid").checked;
                var notDictWord = dom.byId("notDictWord.valid").checked;
                var notRepeatForPwdHis = dom.byId("notRepeatForPwdHis.valid").checked;
                var notEqualLoginId = dom.byId("notEqualLoginId.valid").checked;
                if(maxLength.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0110'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(maxLength) || parseFloat(maxLength) < 4 || parseFloat(maxLength) > 20)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0111'/>"});
                    infoDialog.show();
                    return;
                }
                if(minLength.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0113'/>"});
                    infoDialog.show();
                    return;
                }
                if(isNaN(minLength) || parseFloat(minLength) < 4 || parseFloat(minLength) > 20)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0114'/>"});
                    infoDialog.show();
                    return;
                }
                if(parseFloat(maxLength)<parseFloat(minLength))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0112'/>"});
                    infoDialog.show();
                    return;
                }
                var csrfToken = dom.byId("csrfToken").value;
                xhr.get({
                      url: '<s:url value="/admin/savePwdRule.action" />',
                      content: {"maxLength.numValue" : maxLength,
                            "minLength.numValue" : minLength,
                            "alphanumeric.valid" : alphanumeric,
                            "noRepeat.valid" : noRepeat,
                            "mixtureCase.valid" : mixtureCase,
                            "specialCharacter.valid" : specialCharacter,
                            "notDictWord.valid" : notDictWord,
                            "notRepeatForPwdHis.valid" : notRepeatForPwdHis,
                            "notEqualLoginId.valid" : notEqualLoginId,
                            "csrfToken" : csrfToken
                            },
                      handleAs: "json",
                      load: function(jsonData)
                      {
                          var infoDialog = new InformationDialog({message: jsonData});
                          infoDialog.show();
                          reset();
                      }
                  });
            };  
            
	        reset = function()
            {
                xhr.get({
                    url: '<s:url value="/admin/resetPwdRule.action" />',
                    handleAs: "json",
                    load: function(jsonData)
                    {
                    	registry.byId("maxLength.numValue").set('value', jsonData.maxLength.numValue);
                    	registry.byId("minLength.numValue").set('value', jsonData.minLength.numValue);
                        registry.byId("alphanumeric.valid").set('checked', jsonData.alphanumeric.valid);
                        registry.byId("noRepeat.valid").set('checked', jsonData.noRepeat.valid);
                        registry.byId("mixtureCase.valid").set('checked', jsonData.mixtureCase.valid);
                        registry.byId("specialCharacter.valid").set('checked', jsonData.specialCharacter.valid);
                        registry.byId("notDictWord.valid").set('checked', jsonData.notDictWord.valid);
                        registry.byId("notRepeatForPwdHis.valid").set('checked', jsonData.notRepeatForPwdHis.valid);
                        registry.byId("notEqualLoginId.valid").set('checked', jsonData.notEqualLoginId.valid);
                    }
                });
            }; 

       }); 


     </script>

</head>
<body class="claro">
<body>
    <table class="btnContainer" style="width:97%" >
         <tbody><tr><td>
              <s:if test="#session.permitUrl.contains('/admin/savePwdRule.action')" >
	              <button data-dojo-type="dijit.form.Button" onclick="save();"><s:text name="button.save" /></button>
	              <button data-dojo-type="dijit.form.Button" onclick="reset();"><s:text name="button.reset" /></button>
              </s:if>
          </td></tr></tbody>
    </table>
    <div >
		<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
	</div>
    <div  data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
        <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.pwdRule.summary" />', width:275" style="width:99%">
                
                                
            <table class="commtable">
                <tr>
                   <td><span class="required">*</span></td>
                   <td width="50%"><s:text name="admin.pwdRule.summary.maxLength" /></td>
                   <td>:</td>
                   <td><s:textfield id="maxLength.numValue" name="maxLength.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="4"/></td>
                </tr>
                <tr>
                    <td><span class="required">*</span></td>
                   <td><s:text name="admin.pwdRule.summary.minLength" /></td>
                   <td>:</td>
                   <td><s:textfield id="minLength.numValue" name="minLength.numValue" theme="simple" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="4"/></td>
                </tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.pwdRule.summary.alpanumeric" /></td>
                   <td>:</td>
                   <td><s:checkbox id="alphanumeric.valid" name="alphanumeric.valid" theme="simple" data-dojo-type="dijit.form.CheckBox"/></td>
                </tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.pwdRule.summary.doNotRepeated" /></td>
                   <td>:</td>
                   <td><s:checkbox id="noRepeat.valid" name="noRepeat.valid" theme="simple" data-dojo-type="dijit.form.CheckBox"/></td>
                </tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.pwdRule.summary.upperAndLowerAlphabets" /></td>
                   <td>:</td>
                   <td><s:checkbox id="mixtureCase.valid" name="mixtureCase.valid" theme="simple" data-dojo-type="dijit.form.CheckBox"/></td>
                </tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.pwdRule.summary.leastAllowable" /></td>
                   <td>:</td>
                   <td><s:checkbox id="specialCharacter.valid" name="specialCharacter.valid" theme="simple" data-dojo-type="dijit.form.CheckBox"/></td>
                </tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.pwdRule.summary.notDictionaryPwd" /></td>
                   <td>:</td>
                   <td><s:checkbox id="notDictWord.valid" name="notDictWord.valid" theme="simple" data-dojo-type="dijit.form.CheckBox"/></td>
                </tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.pwdRule.summary.notExist" /></td>
                   <td>:</td>
                   <td><s:checkbox id="notRepeatForPwdHis.valid" name="notRepeatForPwdHis.valid" theme="simple" data-dojo-type="dijit.form.CheckBox"/></td>
                </tr>
                <tr>
                   <td><span class="required"></span></td>
                   <td><s:text name="admin.pwdRule.summary.pwdNotEqualToUserId" /></td>
                   <td>:</td>
                   <td><s:checkbox id="notEqualLoginId.valid" name="notEqualLoginId.valid" theme="simple" data-dojo-type="dijit.form.CheckBox"/></td>
                </tr>
            </table>
        </div>
    </div>
</body>
</html>
