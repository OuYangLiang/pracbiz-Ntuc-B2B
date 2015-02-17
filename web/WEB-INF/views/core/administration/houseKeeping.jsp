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
                var root = document.getElementById("houseKeepingDiv");
                var inputList = root.getElementsByTagName("input");
                var params = "";
                for(var i = 0; i< inputList.length; i++)
                {
                    if(string.trim(inputList[i].value).length==0)
                    {
                        var infoDialog = new InformationDialog({message: inputList[i].id+"<s:text name='B2BPA0116' />"});
                        infoDialog.show();
                        return;
                    }
                    if(isNaN(string.trim(inputList[i].value)) || parseFloat(string.trim(inputList[i].value))<30)
                    {
                        var infoDialog = new InformationDialog({message: inputList[i].id+"<s:text name='B2BPA0117' />"});
                        infoDialog.show();
                        return;
                    }
                    params += inputList[i].name + "=" + inputList[i].value;
                    if(i < inputList.length - 1)
                    {
                        params += "&";
                    }
                }
                var csrfToken = document.getElementById("csrfToken").value;
                xhr.get({
                      url: '<s:url value="/admin/saveHsekeep.action" />?'+params + "&csrfToken=" + csrfToken,
                      handleAs: "json",
                      load: function(jsonData)
                      {
                          var infoDialog = new InformationDialog({message: jsonData});
                          infoDialog.show();
                          reset();
                      }
                  });
            }

            reset = function()
            {
                xhr.get({
                    url: '<s:url value="/admin/resetHsekeep.action" />',
                    handleAs: "json",
                    load: function(jsonData)
                    {
                        var outbounds = jsonData.outbounds;
                        var inbounds = jsonData.inbounds;
                        if(typeof outbounds != undefined)
                        {
                            for(var i = 0; i < outbounds.length; i++)
                            {
                                dom.byId(outbounds[i].paramId).value = outbounds[i].numValue;
                            }
                        }
                        if(typeof inbounds != undefined)
                        {
                            for(var i = 0; i < inbounds.length; i++)
                            {
                                dom.byId(inbounds[i].paramId).value = inbounds[i].numValue;
                            }
                        }
                    }
                });
            } 
       }); 


     </script>

</head>
<body class="claro">
    <table class="btnContainer" style="width:97%" >
         <tbody><tr><td>
              <s:if test="#session.permitUrl.contains('/admin/saveHsekeep.action')" >
	              <button data-dojo-type="dijit.form.Button" onclick="save();"><s:text name="button.save" /></button>
	              <button data-dojo-type="dijit.form.Button" onclick="reset();"><s:text name="button.reset" /></button>
              </s:if>
          </td></tr></tbody>
    </table>
    <div >
		<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
	</div>
    <div  data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'" id="houseKeepingDiv">
        <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.hKeeping.summaryOut" />', width:275" style="width:99%">
            <table class="commtable">
                <s:iterator value="outbounds" status="status" id="item">
	                <tr>
	                   <td><span class="required">*</span></td>
	                   <td width="30%"><s:property value="#item.paramId"/></td>
	                   <td>:</td>
	                   <td><input id="<s:property value="#item.paramId"/>" name="outbounds[<s:property value="#status.index" />].numValue" value="<s:property value="#item.numValue" />" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="4"/>(Days)</td>
	                </tr>
                </s:iterator>
            </table>
        </div>
        <div class="space"></div>
        <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.hKeeping.summaryIn" />', width:275" style="width:99%">
            <table class="commtable">
                <s:iterator value="inbounds" status="status" id="item">
	                <tr>
	                   <td><span class="required">*</span></td>
	                   <td width="30%"><s:property value="#item.paramId"/></td>
	                   <td>:</td>
	                   <td><input id="<s:property value="#item.paramId"/>" name="inbounds[<s:property value="#status.index" />].numValue" value="<s:property value="#item.numValue" />" data-dojo-type="dijit.form.TextBox" data-dojo-props="required: true" maxlength="4"/>(Days)</td>
	                </tr>
                </s:iterator>
            </table>
        </div>
    </div>
</body>
</html>
