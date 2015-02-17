<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
        
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dojo/dom-style",
                "dijit/registry",
                "dojo/on",
                "dojo/parser",
                "dojo/_base/xhr",
                "dijit/form/Select",
                "dijit/form/ValidationTextBox",
                "custom/ConfirmDialog",
                "dijit/form/CheckBox",
                "dojox/form/Uploader",
                "dijit/form/RadioButton",
                "dojo/NodeList-dom",
                "dojo/NodeList-fx",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    domStyle,
                    registry,
                    on,
                    parser,
                    xhr,
                    Select,
                    ValidationTextBox,
                    ConfirmDialog,
                    CheckBox,
                    Uploader,
                    RadioButton,
                    fx
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                
                    
                    
                    
                    on(registry.byId("saveBtn"), 'click', function(){
                        submitForm('mainForm', '<s:url value="/itemIn/save.action" />');
                    });
                    
                    on(registry.byId("saveAndSendBtn"), 'click', function(){
                        submitForm('mainForm', '<s:url value="/itemIn/saveAndSend.action" />');
                    });
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                            changeToURL('<s:url value="/itemIn/init.action?keepSp=Y" />');
                        }
                    });
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/itemIn/initUpload.action" />');
                        }
                    );
                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                            confirmDialog.show();
                        }
                    );
                    
                    on(registry.byId("selectTp"),'click',
                        function()
                        {
                            var supplierOid = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.supplierOid" />';
                            var url = '<s:url value="/popup/popupViewTradingPartnerForItemIn.action" />?tradingPartner.supplierOid='+supplierOid;
                            window.open (url,'','width=800,height=600,scrollbars=1');
                        }
                    );
                    
                });
        
    </script>
</head>

<body class="claro">
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button data-dojo-type="dijit.form.Button" id="saveBtn" ><s:text name="button.save" /></button>
            <button data-dojo-type="dijit.form.Button" id="saveAndSendBtn" ><s:text name="button.saveAndSent" /></button>
            <button data-dojo-type="dijit.form.Button" id="resetBtn" ><s:text name="button.reset" /></button>
            <button data-dojo-type="dijit.form.Button" id="cancelBtn" ><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <form id="mainForm" name="mainForm" method="post"  enctype="multipart/form-data"  >
    <s:token/>
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="itemMaster.upload" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
               <td><span class="required">*</span></td>
               <td>&nbsp;&nbsp;<s:text name="itemMaster.upload.tradingPartner" /></td>
               <td>:</td>
               <td>
                   <s:textfield id="tradingPartner" readonly="true" name="param.tradingPartner" data-dojo-type="dijit/form/TextBox" maxlength="15" theme="simple"/>
                    <button data-dojo-type="dijit.form.Button"  type="button" id="selectTp"><s:text name="button.select"/></button>
               </td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="itemMaster.upload.itemMasterFile"/></td>
                <td>:</td>
                <td><s:file name="upload" data-dojo-props="showInput:'before'" data-dojo-type="dojox/form/Uploader" theme="simple"/></td>
            </tr>
        </tbody>
        </table>
    </div>
    </form>
</body>
</html>
