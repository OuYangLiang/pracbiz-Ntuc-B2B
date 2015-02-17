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
                "dojo/_base/xhr",
                "dojo/parser",
                "dijit/form/Select",
                "dijit/form/ValidationTextBox",
                "dijit/form/MultiSelect",
                "custom/ConfirmDialog",
                "dojo/_base/array",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    xhr,
                    parser,
                    Select,
                    ValidationTextBox,
                    MultiSelect,
                    ConfirmDialog,
                    array
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
	                
	                
	                on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/jobControl/initEdit.action" />?sourceOid=' + '<s:property value="param.jobOid" />');
                        }
                    );
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                            changeToURL('<s:url value="/jobControl/init.action?keepSp=Y" />');
                        }
                    });
                    
                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                            confirmDialog.show();
                        }
                    );
                    
                    
                    on(registry.byId("saveBtn"), 'click', 
                        function()
                        {
                            submitForm('mainForm', '<s:url value="/jobControl/saveEdit.action" />?sourceOid='+<s:property value="param.jobOid" />);
                        }
                    );
                });
                
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button data-dojo-type="dijit.form.Button" id="saveBtn" ><s:text name="button.save" /></button>
            <button data-dojo-type="dijit.form.Button" id="resetBtn" ><s:text name="button.reset" /></button>
            <button data-dojo-type="dijit.form.Button" id="cancelBtn" ><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    
    <div class="pageBar">
        <div class="title"><s:text name="job.edit.title" /></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="job.edit.profile" />', width:275, toggleable:false" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="2px"><span class="required"></span></td>
                <td width="30%">&nbsp;&nbsp;<s:text name="job.edit.profile.jobName" /></td>
                <td>:</td>
                <td><s:property value="param.jobName" /></td>
            </tr>
            
            <tr>
                <td><span class="required"></span></td>
                <td>&nbsp;&nbsp;<s:text name="job.edit.profile.jobDesc" /></td>
                <td>:</td>
                <td><s:property value="param.jobDescription" /></td>
            </tr>
            
            <tr>
                <td><span class="required"></span></td>
                <td>&nbsp;&nbsp;<s:text name="job.edit.profile.updateDate" /></td>
                <td>:</td>
                <td><s:date name="param.updateDate" format="dd/MM/yyyy HH:mm:ss" /></td>
            </tr>
            
            <tr>
                <td><span class="required"></span></td>
                <td>&nbsp;&nbsp;<s:text name="job.edit.profile.updateBy" /></td>
                <td>:</td>
                <td><s:property value="param.updateBy" /></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    <div class="space"></div>
    
    <form id="mainForm" name="mainForm" method="post" >
    <s:token/>
        
        <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="job.edit.setting" />', width:275, toggleable:false" style="width:99%">
        
            <div style="padding-left: 100px">If you don't know how to setup the following values, please refer to <a target="_blank" href="<s:url value='/jobControl/tutorial.action' />">here</a>.</div>
        
	        <table class="commtable">
	        <tbody>
	            <tr>
	                <td width="2px"><span class="required">*</span></td>
	                <td width="30%">&nbsp;&nbsp;<s:text name="job.edit.setting.second" /></td>
	                <td>:</td>
	                <td><s:textfield name="second" maxlength="25" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" /></td>
	            </tr>
	            
	            <tr>
	                <td><span class="required">*</span></td>
	                <td>&nbsp;&nbsp;<s:text name="job.edit.setting.minute" /></td>
	                <td>:</td>
	                <td><s:textfield name="minute" maxlength="25" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" /></td>
	            </tr>
	            
	            <tr>
	                <td><span class="required">*</span></td>
	                <td>&nbsp;&nbsp;<s:text name="job.edit.setting.hour" /></td>
	                <td>:</td>
	                <td><s:textfield name="hour" maxlength="25" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" /></td>
	            </tr>
	            
	            <tr>
                    <td><span class="required">*</span></td>
                    <td>&nbsp;&nbsp;<s:text name="job.edit.setting.dayOfMonth" /></td>
                    <td>:</td>
                    <td><s:textfield name="dayOfMonth" maxlength="25" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" /></td>
                </tr>
                
                <tr>
                    <td><span class="required">*</span></td>
                    <td>&nbsp;&nbsp;<s:text name="job.edit.setting.month" /></td>
                    <td>:</td>
                    <td><s:textfield name="month" maxlength="25" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" /></td>
                </tr>
                
                <tr>
                    <td><span class="required">*</span></td>
                    <td>&nbsp;&nbsp;<s:text name="job.edit.setting.dayOfWeek" /></td>
                    <td>:</td>
                    <td><s:textfield name="dayOfWeek" maxlength="25" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" /></td>
                </tr>
                
                <tr>
                    <td><span class="required"></span></td>
                    <td>&nbsp;&nbsp;<s:text name="job.edit.setting.year" /></td>
                    <td>:</td>
                    <td><s:textfield name="year" maxlength="9" data-dojo-props="required: true" data-dojo-type="dijit.form.TextBox" theme="simple" /></td>
                </tr>
                
	        </tbody>
	        </table>
	    </div>
    </form>
</body>
</html>
