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
        require(["custom/B2BPortalBase", "dojo/dom", "dijit/registry", "dojo/on", "dojox/data/QueryReadStore", "dojox/grid/EnhancedGrid",
                 "dojox/grid/enhanced/plugins/Pagination", "dojox/grid/enhanced/plugins/IndirectSelection", "dojo/parser", "dojo/_base/xhr",
                 "custom/InformationDialog", "dijit/form/Select", "custom/ConfirmDialog","dijit/form/CheckBox", "dojo/domReady!"], 
                function(B2BPortalBase,dom, registry,on,QueryReadStore, EnhancedGrid, Pagination, IndirectSelection, parser, xhr, InformationDialog, Select, ConfirmDialog, CheckBox)
                {
                    parser.parse();
                
                    (new B2BPortalBase()).init(
                        '<c:out value="${session.helpExHolder.helpNo}"/>',
                        '<c:out value="${session.helpExHolder.helpEmail}"/>',
                        '<s:property value="#session.commonParam.timeout" />',
                        '<s:url value="/logout.action" />');
                    
                    
                    var store = new QueryReadStore({url: '<s:url value="/jobControl/data.action" />'});
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        rowHeight: 50,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            
                            { name: "<s:text name='job.summary.grid.No' />", field: "dojoIndex", width: "3%"},
                            { name: "<s:text name='job.summary.grid.jobName' />", field: "jobName", width: "24%", 
                                formatter: function(field, index, cell){
                                    if (field == null || "" == field)
                                    {
                                        return '&nbsp;';
                                    }
                                    
                                    var jobOid = cell.grid.store.getValue(cell.grid.getItem(index), 'jobOid');
                                    var jobName= cell.grid.store.getValue(cell.grid.getItem(index), 'jobName');
                                    
                                    return "<a href=\"javascript:changeToURL('<s:url value="/jobControl/initEdit.action"/>?sourceOid=" + jobOid + "');\">" + jobName + "</a>";
                                }
                            
                            },
                            { name: "<s:text name='job.summary.grid.jobDesc' />", field: "jobDescription", width: "38%"},
                            { name: "<s:text name='job.summary.grid.jobCronExp' />", field: "cronExpression", width: "10%"},
                            { name: "<s:text name='job.summary.grid.updateDate' />", field: "updateDate", width: "12%"},
                            { name: "<s:text name='job.summary.grid.updateBy' />", field: "updateBy", width: "7%"},
                            { name: "<s:text name='job.summary.grid.isEnabled' />", field: "enabled", width: "6%",
                            	formatter: function(field, index, cell){
                                    var jobOid = cell.grid.store.getValue(cell.grid.getItem(index), 'jobOid');
                                    var enabled = cell.grid.store.getValue(cell.grid.getItem(index), 'enabled');
                                    var gridCheckBox;
                                    if (enabled)
                                    {
	                                    gridCheckBox = new CheckBox({
	                                    	style: "dijit.form.CheckBox",
	                                    	checked: true,
	                                    	onMouseDown: function(){
	                                    		enable(this, jobOid);
	                                		}
	                                    });
                                    }
                                    else
                                    {
                                    	gridCheckBox = new CheckBox({
	                                    	style: "dijit.form.CheckBox",
	                                    	checked: false,
	                                    	onMouseDown: function(){
	                                    		enable(this, jobOid);
	                                		}
	                                    });
                                    }
                                    return gridCheckBox;
                                }
                            }
                            ]
                        ],
                        
                        plugins: {
                            pagination: {  
                                <s:if test = 'param != null'>
                                    defaultPage: <s:property value="param.requestPage" />,
                                </s:if>
                                defaultPageSize:<s:if test = 'param == null || param.pageSize == 0'>
                                                    <s:property value="#session.commonParam.defaultPageSize"/>
                                                </s:if>
                                                <s:else><s:property value="param.pageSize" /></s:else>,
                                pageSizes: [<s:property value="#session.commonParam.pageSizes"/>],
                                position: "bottom",
                                maxPageStep: 7,
                                gotoButton: true,
                                description: true,
                                sizeSwitch: true,
                                pageStepper: true
                            }
                        }
                    }, "grid");
                    
                    grid.canSort=function(col){if(col==1 || col==7)return false;else return true;};
                    grid.startup();

                });

		        var enable = function(src, sourceOid)
		        {
			        var ConfirmDialog = require("custom/ConfirmDialog");
			        var xhr = require("dojo/_base/xhr");
			        var registry = require("dijit/registry");
		        	var confirmDialog;

					if (!src.checked)
					{
						var isChecked = true;
						confirmDialog = new ConfirmDialog({
	                        message: '<s:text name="alert.job.enable" />',
	                        yesBtnPressed: function()
	                        {
	                        	xhr.get({
		                               url: '<s:url value="/jobControl/saveEditEnabled.action"/>?sourceOid=' + sourceOid + '&enabled=' + isChecked,
		                               handleAs: "json",
		                               load: function(success)
		                               {
		                                   if (success==0)
		                                   {
		                                	   src.set("checked",true);
		                                   }
		                               }
		                           });
	                        },
	                        noBtnPressed: function()
	                        {
	                        	return;
	                        }
	                    });
					}
					else
					{
						var isChecked = false;
						confirmDialog = new ConfirmDialog({
	                        message: '<s:text name="alert.job.disable" />',
	                        yesBtnPressed: function()
	                        {
	                        	xhr.get({
		                               url: '<s:url value="/jobControl/saveEditEnabled.action"/>?sourceOid=' + sourceOid + '&enabled=' + isChecked,
		                               handleAs: "json",
		                               load: function(success)
		                               {
		                                   if (success==0)
		                                   {
		                                	   src.set("checked",false);
		                                   }
		                               }
		                           });
	                        },
	                        noBtnPressed: function()
	                        {
	                        	return;
	                        }
	                    });
							
					}
		        	
		        	confirmDialog.show();
		        }

    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        </td></tr></tbody></table>
    </div>
    
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name="job.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" ></div>
</body>
</html>
