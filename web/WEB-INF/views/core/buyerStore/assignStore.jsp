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
                "dojo/_base/xhr",
                "dojo/parser",
                "dijit/form/ValidationTextBox",
                "dijit/form/MultiSelect",
                "custom/ConfirmDialog",
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
                    xhr,
                    parser,
                    ValidationTextBox,
                    MultiSelect,
                    ConfirmDialog,
                    fx
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');

                    disableUser();

                    function disableUser()
                    {
                    	var user = dijit.byId("user1");
                        <s:iterator value="availableUsers" var="innerItem" >
                            var lock = '<s:property value="#innerItem.locked"/>';
                            var userOid = '<s:property value="#innerItem.userOid"/>';
                            if (lock == 'true')
                            {
                            	dojo.forEach(user.domNode.options,function(item) 
                                {
                                       if (item != null)
                                       {
                                           var value = item.value;
                                           
                                           if (userOid == value)
                                           {
                                                item.disabled = true;
                                                var text = item.text;
                                                var message='<s:text name="LastUpdateFrom.User"/>';
                                                item.text = text + message;
                                                item.style.setProperty('color','red','');
                                                item.selected=false;
                                           }
                                       }
                                });
                           }
                        </s:iterator>
                        var user = dijit.byId("user2");
                        <s:iterator value="assignedUsers" var="innerItem" >
                            var lock = '<s:property value="#innerItem.locked"/>';
                            var userOid = '<s:property value="#innerItem.userOid"/>';
                            if (lock == 'true')
                            {
                            	dojo.forEach(user.domNode.options,function(item) 
                                {
                                       if (item != null)
                                       {
                                           var value = item.value;
                                           
                                           if (userOid == value)
                                           {
                                                item.disabled = true;
                                                var text = item.text;
                                                var message='<s:text name="LastUpdateFrom.User"/>';
                                                item.text = text + message;
                                                item.style.setProperty('color','red','');
                                                item.selected=false;
                                           }
                                       }
                                });
                           }
                        </s:iterator>
                    }
                    moveSelection = function(from, to)
                    {
                        var selectedOnes = registry.byId(from).getSelected();
                        registry.byId(to).addSelected(registry.byId(from));
                        registry.byId(to).getSelected().forEach(function(option){option.selected=false;});
                        
                        dojo.query(selectedOnes).animateProperty({
                                        properties:{
                                            backgroundColor: {start: "#ff6", end: "#fff" }
                                        },
                                        duration :1500
                                    }).play();
                    };

                    moveAll = function(from,to)
                    {
                        registry.byId(from).getSelected().forEach(function(option){option.selected=false;});
                        registry.byId(from).invertSelection();
                        moveSelection(from,to);
                    
                    };
                    
                    filterPrivilege = function(src,id)
                    {
                        var inputValue = dom.byId(src).value.toUpperCase();
                        
                        moveAll(id+"_hide", id);
                        registry.byId(id).getSelected().forEach(function(option){option.selected=false;});
                        registry.byId(id).invertSelection();
                        registry.byId(id).getSelected().forEach(function(option){
                            if (option.label.toUpperCase().indexOf(inputValue) >= 0 )
                            {
                                option.selected = false;
                            }
                        });
                        
                        moveSelection(id, id+"_hide");
                    };
                    
                    on(registry.byId("saveBtn"), 'click', 
                        function()
                        {
                            moveAll("user2_hide","user2");
                            registry.byId("user2").getSelected().forEach(function(option){option.selected=false;});
                            registry.byId("user2").invertSelection();
                        
                            submitForm('mainForm', '<s:url value="/buyerStore/saveAssignStore.action" />?param.storeOid='+<s:property value="param.storeOid" /> 
                            + '&param.storeCode=<s:property value="param.storeCode" />' );
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/buyerStore/assignStore.action" />?param.storeOid=' + '<s:property value="param.storeOid" />');
                        }
                    );
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                        	changeToURL('<s:url value="/buyerStore/init.action?keepSp=Y" />');
                        }
                    });
                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                    		confirmDialog.show();
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
        <div class="title"><s:text name="store.assign.store.title" /></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <form id="mainForm" name="mainForm" method="post" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token/>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="store.assign.store.panel.title" />', width:275">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="5"></td>
                <td width="300">&nbsp;&nbsp;<s:text name="store.assign.store.area"/></td>
                <td width="20">:</td>
                <td><s:property value="param.areaCode"/>&nbsp;<s:if test="param.areaName != null">[<s:property value="param.areaName"/>]</s:if></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="store.assign.store.storeCode"/></td>
                <td>:</td>
                <td><s:property value="param.storeCode"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="store.assign.store.storeName"/></td>
                <td>:</td>
                <td><s:property value="param.storeName" /></td>
            </tr>
            <tr>
                <td></td>
                <td valign="top">&nbsp;&nbsp;<s:text name="store.assign.store.addr"/></td>
                <td valign="top">:</td>
                <td><table>
                	<tbody>
                	<s:if test='param.storeAddr1 != null && param.storeAddr1 != ""'>
                	<tr><td valign="top"><s:property value="param.storeAddr1" /></td></tr>
                	</s:if>
                	<s:if test='param.storeAddr2 != null && param.storeAddr2 != ""'>
                	<tr><td valign="top"><s:property value="param.storeAddr2" /></td></tr>
                	</s:if>
                	<s:if test='param.storeAddr3 != null && param.storeAddr3 != ""'>
                	<tr><td valign="top"><s:property value="param.storeAddr3" /></td></tr>
                	</s:if>
                	<s:if test='param.storeAddr4 != null && param.storeAddr4 != ""'>
                	<tr><td valign="top"><s:property value="param.storeAddr4" /></td></tr>
                	</s:if>
                	</tbody>
                </table></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="store.assign.store.city"/></td>
                <td>:</td>
                <td><s:property value="param.storeCity" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="store.assign.store.state"/></td>
                <td>:</td>
                <td><s:property value="param.storeState" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="store.assign.store.ctry"/></td>
                <td>:</td>
                <td><s:property value="param.storeCtryName" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="store.assign.store.postalCode"/></td>
                <td>:</td>
                <td><s:property value="param.storePostalCode" /></td>
            </tr>
        </tbody>
        </table>
    </div>
    <div class="space"></div>
    <div id="availableUser" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="store.assign.store.availableUsers" />', width:275" >
        <table class="commtable">
            <tr>
                <td width="350px">
                    <table>
                        <tr><td>
                           <input id="user1_filter" data-dojo-type="dijit.form.ValidationTextBox" style="width:298px" 
                               onkeyup="filterPrivilege('user1_filter','user1');" >
                        </td></tr>
                        
                        <tr><td>
                            <s:select id="user1" list="availableUsers" listKey="userOid" listValue="userName+'['+userTypeDesc+']'" multiple="multiple"
                                theme="simple" style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit.form.MultiSelect" />
                            </td>
                            <td style="display:none;">
                                <s:select id="user1_hide" list="#{}" multiple="multiple" theme="simple"
                                    data-dojo-type="dijit.form.MultiSelect" />
                            </td>
                        </tr>
                    </table>
                </td>
                
                <td width="100px">
                    <p><button onclick="moveSelection('user1','user2');" data-dojo-type="dijit.form.Button">&gt;</button></p>
                    <p><button onclick="moveSelection('user2','user1');" data-dojo-type="dijit.form.Button">&lt;</button></p>
                </td>
                
                <td width="350px">
                    <table>
                        <tr><td>
                            <input id="user2_filter" data-dojo-type="dijit.form.ValidationTextBox" style="width:298px" 
                                onkeyup="filterPrivilege('user2_filter','user2');" >
                        </td></tr>
                        
                        <tr><td>
                            <s:select id="user2" name="assignedUsers" list="assignedUsers" listKey="userOid" listValue="userName+'['+userTypeDesc+']'"
                                multiple="multiple" theme="simple"
                                style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit.form.MultiSelect" />
                            </td>
                            <td style="display:none;">
                                <s:select id="user2_hide" list="#{}" multiple="multiple" theme="simple"
                                    data-dojo-type="dijit.form.MultiSelect" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    </form>
</body>
</html>
