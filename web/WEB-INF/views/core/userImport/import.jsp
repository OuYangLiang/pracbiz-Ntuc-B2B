<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    <style type="text/css">
    	@import "<s:url value='/js/dojo-root/dojox/grid/resources/Grid.css' />";
    	@import "<s:url value='/js/dojo-root/dojox/grid/resources/claroGrid.css' />";
    	@import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/%{#session.layoutTheme}/EnhancedGrid.css' />";
    	@import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css' />";
    </style>
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dojo/dom-style",
                "dijit/registry",
                "dojo/on",
                "dojo/_base/xhr",
                "dijit/form/Button",
                "dojo/parser",
                "dijit/Dialog",
                "dijit/form/Select",
                "dijit/form/CheckBox",
                "dijit/form/RadioButton",
                "dijit/form/ValidationTextBox",
                "dijit/form/MultiSelect",
                "custom/ConfirmDialog",
                "dojo/dom-construct",
                "dojox/grid/EnhancedGrid",
                "dojox/grid/enhanced/plugins/Pagination",
                "dojox/grid/_SelectionPreserver",
                "dojox/grid/enhanced/plugins/IndirectSelection",
                "dojox/grid/cells",
                "dijit/ProgressBar",
                "dojo/data/ItemFileReadStore",
                "custom/InformationDialog",
                "custom/ConfirmDialog",
                "dojo/_base/array",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    domStyle,
                    registry,
                    on,
                    xhr,
                    Button,
                    parser,
                    Dialog,
                    Select,
                    CheckBox,
                    RadioButton,
                    ValidationTextBox,
                    MultiSelect,
                    ConfirmDialog,
                    domConstruct,
                    EnhancedGrid,
                    Pagination,
                    _SelectionPreserver,
                    IndirectSelection,
                    cells,
                    ProgressBar,
                    ItemFileReadStore,
                    InformationDialog,
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
                    
                	domStyle.set('pageBar',"display","none");
                	
                	var jsonStore;
                	
                    if (dom.byId("accordingByOu"))
                    {
	                    on(registry.byId("accordingByOu"), 'click', 
	                    		
                    		function(value)
                            {
	                
	                    		dojo.byId("errorMsg").innerHTML="";
	                    		var checked = registry.byId("accordingByOu").checked;
	                    		if (!checked) return;
	                        	// The parameters to pass to xhrGet, the url, how to handle it, and the callbacks.
	                        	var xhrArgs = {
		                            url: '<s:url value="/userImport/initGroupData.action" />',
		                            handleAs: "json",
		                            form: "mainForm"
		                        };
	                        	
	                        	// Call the asynchronous xhrGet
	                        	var deferred = dojo.xhrGet(xhrArgs);
	                        	
	                        	// Now add the callbacks
	                        	deferred.then
	                        	(
		                       	      function(data)
		                       	      {
		                       	    	  if (data.succ)
		                       	    	  {
			                            		var data = {identifier: 'groupCode', label: 'groupCode',items:  data.groups}; 
			                					jsonStore = new ItemFileReadStore({data: data});
			                					initGroupData(jsonStore);
			                            		domStyle.set('pageBar',"display","");
			                            		return;
			                              }
			                            	
			                            	var obj = dojo.byId("errorMsg");
		                                   	obj.innerHTML=data.errorMsg;
		                                   	dojo.addClass(obj,"error");
		                                   	registry.byId("accordingByOu").checked = false;
		                                   
		                       	      }
	                        	);
	                    	}
	                    );
               	 	}
                    
                    var grid;
                    function initGroupData(store)
                    {
                        grid = new EnhancedGrid({
                            store: store,
                            autoHeight: true,
                            selectionMode: "multiple",
                            keepSelection: true,
                            structure: [
                                [
                                //{ name: "<s:text name='user.import.summary.grid.No' />", field: "dojoIndex", width: "5%"},
                                { name: "<s:text name='user.import.summary.grid.groupCode' />", field: "groupCode", width: "40%"},
                                { name: "<s:text name='user.import.summary.grid.groupDesc' />", field: "groupDesc", width: "50%"}
                                ]
                            ],
                            
                            plugins: {
                                indirectSelection: {headerSelector:true, width:'5%', styles:'text-align: center;'},
                                pagination: {  
                                    defaultPageSize: <s:property value="#session.commonParam.defaultPageSize"/>,
                                    pageSizes: [<s:property value="#session.commonParam.pageSizes"/>],
                                    position: "bottom",
                                    maxPageStep: 7,
                                    gotoButton: true,
                                    description: true,
                                    sizeSwitch: true,
                                    pageStepper: true
                                }
                            },
                            
                            onSelected: function(index){
                                var item = this.getItem(index);
                                var value = this.store.getValue(item, 'groupCode');
                                //alert(value);
                            }
                        }, "grid");
                       grid.startup();
                    }
                  
                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    if (dom.byId("importBtn"))
                    {
	                    on(registry.byId("importBtn"), 'click', 
	                        function()
	                        {
	                    		var checked = registry.byId("accordingByOu").checked;
	                    		if (checked)
	                    		{
	                    			if (!anyRecordSelected())
	                          	   	{
	                          		    infoDialog.show();
	                          		    return;
	                                }
	                                
	                    			fn('<s:url value="/userImport/import.action" />');
	                    			
	                    			return;
	                    		}
	                    		
	                    		var confirmDialog = new ConfirmDialog({
	                                message: '<s:text name="B2BPU0209" />',
	                                yesBtnPressed: function(){
	                                    changeToURL();
	                                    submitForm("mainForm", '<s:url value="/userImport/import.action" />?allUser=Y')
	                                }
	                            });
	                    		confirmDialog.show();
	                        }
	                    );
                    }
                    
                    var getGroups = function()
                    {
                        var groups = "";
                        
                        array.forEach(grid.selection.getSelected(), function(item){
                            try
                            {
                                var group = jsonStore.getValue(item, 'groupCode');
                                groups = groups + group + '-';
                            }
                            catch (e)
                            {
                                // there may be a bug in dojo's selection plugin if select multiple records from different pages.
                                //alert(e);
                            }
                        });
                        
                        groups = groups.substring(0, groups.length-1);
                        return groups;
                    };

                    var anyRecordSelected = function()
                    {
                        return getGroups() != "";
                    };
                    
                    var fn = function(url)
                    {
                    	var groups = getGroups();
                        xhr.get({
                            url: '<s:url value="/userImport/putParamIntoSession.action" />',
                            content: {selectedGroups: groups},
                            load: function(data)
                            {
                            	submitForm("mainForm", url)
                            }
                        });
                    };
                    
                    if (dom.byId("resetBtn"))
                    {
	                    on(registry.byId("resetBtn"), 'click', 
	                        function()
	                        {
	                            changeToURL('<s:url value="/userImport/init.action" />');
	                        }
	                    );
                    }
                    
                });
    </script>
</head>

<body class="claro" >
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button id="importBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.import' /></button>
            <button id="resetBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.reset' /></button>
        </td></tr></tbody></table>
    </div>
	<!-- here is message area -->
	 
    <div class="required">
        <s:actionerror />
        <s:fielderror />
        <div id="errorMsg"></div>
    </div>
    
    <form id="mainForm" name="mainForm" method="post" >
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.import.usertypearea.title" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='user.import.usertypearea.domain.name' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield id="domain" name="param.domain" maxlength="50" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
					</td>
                </tr>
                <tr height="25px">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='user.import.usertypearea.hostname' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield id="hostname" name="param.hostname" maxlength="50" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
					</td>
                </tr>
                <tr height="25px">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='user.import.usertypearea.port' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield id="port" name="param.port" maxlength="5" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
					</td>
                </tr>
                <tr height="25px">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='user.import.usertypearea.accountName' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield id="userName"  name="param.userName" maxlength="50" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
					</td>
                </tr>
                 <tr height="25px">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='user.import.usertypearea.password' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:password id="password"  name="param.password" maxlength="50" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
					</td>
                </tr>
                <tr height="25px">
                	<td width="2px"><span class="required"></span></td>
                    <td width="30%"><s:text name='user.import.usertypearea.accordingByOu' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:checkbox id="accordingByOu" name="accordingByOu" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
					</td>
                </tr>
            </tbody>
        </table>
    </div>
    
    <div class="space"></div>
    
    <div id="group">
    	<div class="pageBar" id="pageBar">
        	<div class="title"><s:text name='user.summary.grid.rltmsg' /></div>
    	</div>
    	<div id="grid" ></div>
    </div>
    </form>
</body>
</html>
