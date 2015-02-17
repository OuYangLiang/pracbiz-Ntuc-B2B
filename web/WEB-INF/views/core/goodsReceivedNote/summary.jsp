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
             "dijit/registry",
             "dojo/on",
             "dojox/data/QueryReadStore",
             "dojox/grid/EnhancedGrid",
             "dojo/parser",
             "dojo/_base/xhr",
             "custom/InformationDialog",
             "custom/SelectAllConfirmDialog",
             "dijit/Tooltip",
             "custom/CustomSummaryGrid",
             "dojo/domReady!"
             ], 
             function(
                 B2BPortalBase,
                 dom,
                 registry,
                 on,
                 QueryReadStore,
                 EnhancedGrid,
                 parser,
                 xhr,
                 InformationDialog,
                 SelectAllConfirmDialog,
                 Tooltip,
                 CustomSummaryGrid
                 )
             {
                parser.parse();
            
                (new B2BPortalBase()).init(
                    '<c:out value="${session.helpExHolder.helpNo}"/>',
                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
                    '<s:property value="#session.commonParam.timeout" />',
                    '<s:url value="/logout.action" />');
            
                var customGrid = new CustomSummaryGrid();
                customGrid.setPageSize('<s:property value="#session.commonParam.pageSizes"/>');
                customGrid.setDefaultPageSize(<s:if test = 'param == null || param.pageSize == 0'>
								                '<s:property value="#session.commonParam.defaultPageSize"/>'
								            </s:if>
								            <s:else>
								                '<s:property value="param.pageSize" />'
								            </s:else>);
				customGrid.setDefaultPage(<s:if test = 'param != null'>
											'<s:property value="param.requestPage" />'
										</s:if>);
                customGrid.initGrid(
                		'<s:property value="pageId"/>',
                		'<s:url value="/grn/data.action" />',
                        '<s:url value="/ajax/findSummaryFieldAndTooltips.action"/>',
                        [{field: '<s:text name="grn.summary.grid.grnNo" />',method: formatHref},
                         {field: '<s:text name="grn.summary.grid.ctrlStatus" />',method: formatStatus},
                         {field: '<s:text name="grn.summary.grid.disputeStatus" />',method: formatDisputeStatus},
                         {field: '<s:text name="grn.summary.grid.poNo" />',method: formatHrefPo}]);

                var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                
                if (dom.byId("searchBtn"))
                {
                    on(registry.byId("searchBtn"), 'click', 
                        function()
                        {
                    		(new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />');
							if(!customGrid.grid._isLoaded)
							{
							 	var infoDialog = new InformationDialog({message: "<s:text name='alert.summary.search.not.finished'/>"});
                               	infoDialog.show();
								return;
							}
                        
	                    	var validGrnFrom = dijit.byId("param.grnDateFrom").getValue();
	                        var validGrnTo = dijit.byId("param.grnDateTo").getValue();
	                        if(null != validGrnTo && new Date(validGrnFrom) > new Date(validGrnTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1801' />"});
	                            infoDialog.show();
	                            return;
	                        }

	                        var buyOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.buyerOid}"/>';

		                    if(buyOid != null && buyOid != (""))
		                    {
	                    	 	var validSentFrom = dijit.byId("param.sentDateFrom").getValue();
		                        var validSentTo = dijit.byId("param.sentDateTo").getValue();
		                        if(null != validSentTo && new Date(validSentFrom) > new Date(validSentTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1801' />"});
		                            infoDialog.show();
		                            return;
		                        }
		                    }

		                    var supplierOid='<c:out value="${session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';
	                        
	                        if(supplierOid != null && supplierOid != (""))
			                {	
				                var validReceivedFrom = dijit.byId("param.receivedDateFrom").getValue();
		                        var validReceivedTo = dijit.byId("param.receivedDateTo").getValue();
		                        if(null != validReceivedTo && new Date(validReceivedFrom) > new Date(validReceivedTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1801' />"});
		                            infoDialog.show();
		                            return;
		                        }
			                }
                    	
                            xhr.post({
                                url: '<s:url value="/grn/search.action" />',
                                form: dom.byId("searchForm"),
                                load: function(rlt)
                                {
                                	customGrid.grid.setQuery();
                                }
                            });
                        }
                    );
                }

                var selectPDFType = new SelectAllConfirmDialog({
                    message: '<s:text name="B2BPC1665" />',
                    yesBtnPressed: function(){
                    	var validGrnFrom = dijit.byId("param.grnDateFrom").getValue();
                        var validGrnTo = dijit.byId("param.grnDateTo").getValue();
                        if(null != validGrnTo && new Date(validGrnFrom) > new Date(validGrnTo))
                        {
                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1801' />"});
                            infoDialog.show();
                            return;
                        }

                        var buyOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.buyerOid}"/>';

	                    if(buyOid != null && buyOid != (""))
	                    {
                    	 	var validSentFrom = dijit.byId("param.sentDateFrom").getValue();
	                        var validSentTo = dijit.byId("param.sentDateTo").getValue();
	                        if(null != validSentTo && new Date(validSentFrom) > new Date(validSentTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1801' />"});
	                            infoDialog.show();
	                            return;
	                        }
	                    }

	                    var supplierOid='<c:out value="${session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';
                        
                        if(supplierOid != null && supplierOid != (""))
		                {	
			                var validReceivedFrom = dijit.byId("param.receivedDateFrom").getValue();
	                        var validReceivedTo = dijit.byId("param.receivedDateTo").getValue();
	                        if(null != validReceivedTo && new Date(validReceivedFrom) > new Date(validReceivedTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1801' />"});
	                            infoDialog.show();
	                            return;
	                        }
		                }
                        xhr.post({
                            url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=pdf&msgType=GRN',
                            handleAs: "json",
                            form: dom.byId("searchForm"),
                            load: function(printDocMsg)
                            {
                            	if (printDocMsg == 'success')
                            	{
			                        submitFormInNewWindow('searchForm', '<s:url value="/grn/print.action" />?selectAll=true');
                            	}
                            	else
                            	{
                            		new InformationDialog({message: printDocMsg}).show();
                            	}
                            }
                        });
                    },
                    noBtnPressed: function(){
                    	var grnOids = customGrid.getSelectedItemDocOids();
						 var oids = "";
						 dojo.forEach(grnOids, function(item)
						 {
						 	oids = oids + item.grnOid + '-';
						 });
                      oids = oids.substring(0, oids.length-1);
                      xhr.get({
                              url: '<s:url value="/grn/putParamIntoSession.action" />',
                              sync: true,
                              content: {selectedOids: oids},
                              load: function(data)
                              {
                           	   if (data!='x')
                                  {
                                      (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                                  }
                                  else
                                  {
                                	  xhr.post({
                                          url: '<s:url value="/ajax/checkDocCount.action" />?printType=pdf&msgType=GRN',
                                          handleAs: "json",
                                          form: dom.byId("searchForm"),
                                          load: function(printDocMsg)
                                          {
                                          	if (printDocMsg == 'success')
                                          	{
			                               	   window.open('<s:url value="/grn/print.action" />');
                                          	}
                                          	else
                                          	{
                                          		new InformationDialog({message: printDocMsg}).show();
                                          	}
                                          }
                                      });
                                  }
                              }
                          });
                    }
                });

                if (dom.byId("printBtn"))
                {
                    on(registry.byId("printBtn"), 'click',
                       function()
                       {
                    	 (new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />');
                    	 var selected = customGrid.getCountOfSelected();
                    	 if (selected > 0 && selected == customGrid.getCountOfRow())
                      	 {
							 selectPDFType.show();
                      	 }
						 else
						 {
							 if(selected < 1)
							 {
								var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
	                  			infoDialog.show();
	                  			return; 
							 }
							 var grnOids = customGrid.getSelectedItemDocOids();
							 var oids = "";
							 dojo.forEach(grnOids, function(item)
							 {
							 	oids = oids + item.grnOid + '-';
							 });
	                       oids = oids.substring(0, oids.length-1);
	                       xhr.get({
	                               url: '<s:url value="/grn/putParamIntoSession.action" />',
	                               sync: true,
	                               content: {selectedOids: oids},
	                               load: function(data)
	                               {
	                            	   if (data!='x')
	                                   {
	                                       (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
	                                   }
	                                   else
	                                   {
	                                	   xhr.post({
	                                            url: '<s:url value="/ajax/checkDocCount.action" />?printType=pdf&msgType=GRN',
	                                            handleAs: "json",
	                                            form: dom.byId("searchForm"),
	                                            load: function(printDocMsg)
	                                            {
	                                            	if (printDocMsg == 'success')
	                                            	{
				                                	   window.open('<s:url value="/grn/print.action" />');
	                                            	}
	                                            	else
	                                            	{
	                                            		new InformationDialog({message: printDocMsg}).show();
	                                            	}
	                                            }
	                                        });
	                                   }
	                               }
	                           });
						 }
                    });
                }

                if (dom.byId("disputeBtn"))
                {
                	on(registry.byId("disputeBtn"),'click', function()
                    {
                		(new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />');
                		var selected = customGrid.getCountOfSelected();
                		if (selected < 1)
                   		{
                			var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                			infoDialog.show();
                			return;
                   		}
                		if (selected > 1)
                		{
                			var infoDialog = new InformationDialog({message: '<s:text name="alert.select.one" />'});
                			infoDialog.show();
                			return;
                    	}
                    	
                		var docOid = customGrid.getSelectedItemDocOids()[0].docOid;
                    	var url = '<s:url value="/grn/checkAccessDispute.action" />';
                    	var cfmUrl = '<s:url value="/grn/initDispute.action" />?docOid='+docOid;
                   	    xhr.post({
                             url: url,
                             handleAs: "json",
                             content: {docOid: docOid},
                             load: function(jsonData)
                             {
                                 
                                 var errorMsg = jsonData.errorMsg;
                                 if (errorMsg!=null)
                                 {
                                	 var infoDialog = new InformationDialog({message: errorMsg}); 
                                	infoDialog.show();
                                	
                                 	return;
                                 }
                                 changeToURL(cfmUrl);
                             }
                         });
                    });
                 }

                if (dom.byId("auditBtn"))
                {
                	on(registry.byId("auditBtn"),'click', function()
                    {
                		(new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />');
                		var selected = customGrid.getCountOfSelected();
                		if (selected < 1)
                		{
                			var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                			infoDialog.show();
                			return;
                    	}
                		if (selected > 1)
                		{
                			var infoDialog = new InformationDialog({message: '<s:text name="alert.select.one" />'});
                			infoDialog.show();
                			return;
                    	}
                    	
                		var docOid = customGrid.getSelectedItemDocOids()[0].docOid;
                    	var url = '<s:url value="/grn/checkAccessDispute.action" />';
                    	var cfmUrl = '<s:url value="/grn/initAudit.action" />?docOid='+docOid;
                   	    xhr.post({
                             url: url,
                             handleAs: "json",
                             content: {docOid: docOid},
                             load: function(jsonData)
                             {
                                 var errorMsg = jsonData.errorMsg;
                                 console.log(errorMsg);
                                 if (errorMsg!=null)
                                 {
                                	var infoDialog = new InformationDialog({message: errorMsg}); 
                                	infoDialog.show();
                                 	return;
                                 }
                                 changeToURL(cfmUrl);
                             }
                         });
                    });
                 }

                var selectType = new SelectAllConfirmDialog({
                    message: '<s:text name="B2BPC1665" />',
                    yesBtnPressed: function(){
                    	var validGrnFrom = dijit.byId("param.grnDateFrom").getValue();
                        var validGrnTo = dijit.byId("param.grnDateTo").getValue();
                        if(null != validGrnTo && new Date(validGrnFrom) > new Date(validGrnTo))
                        {
                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1801' />"});
                            infoDialog.show();
                            return;
                        }

                        var buyOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.buyerOid}"/>';

	                    if(buyOid != null && buyOid != (""))
	                    {
                    	 	var validSentFrom = dijit.byId("param.sentDateFrom").getValue();
	                        var validSentTo = dijit.byId("param.sentDateTo").getValue();
	                        if(null != validSentTo && new Date(validSentFrom) > new Date(validSentTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1801' />"});
	                            infoDialog.show();
	                            return;
	                        }
	                    }

	                    var supplierOid='<c:out value="${session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';
                        
                        if(supplierOid != null && supplierOid != (""))
		                {	
			                var validReceivedFrom = dijit.byId("param.receivedDateFrom").getValue();
	                        var validReceivedTo = dijit.byId("param.receivedDateTo").getValue();
	                        if(null != validReceivedTo && new Date(validReceivedFrom) > new Date(validReceivedTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1801' />"});
	                            infoDialog.show();
	                            return;
	                        }
		                }

                        xhr.post({
                            url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=excel&msgType=GRN',
                            handleAs: "json",
                            form: dom.byId("searchForm"),
                            load: function(printDocMsg)
                            {
                            	if (printDocMsg == 'success')
                            	{
			                        submitForm('searchForm', '<s:url value="/grn/exportExcel.action" />?selectAll=true');
                            	}
                            	else
                            	{
                            		new InformationDialog({message: printDocMsg}).show();
                            	}
                            }
                        });
                    },
                    noBtnPressed: function(){
                    	var grnOids = customGrid.getSelectedItemDocOids();
						var oids = "";
						dojo.forEach(grnOids, function(item)
						{
							oids = oids + item.grnOid + '-';
						});
                        oids = oids.substring(0, oids.length-1);
                        xhr.get({
                              url: '<s:url value="/grn/putParamIntoSession.action" />',
                              content: {selectedOids: oids},
                              load: function(data)
                              {
                              	if (data!='x')
                                     {
                                         (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                                     }
                                     else
                                     {
                                    	 xhr.post({
                                             url: '<s:url value="/ajax/checkDocCount.action" />?printType=excel&msgType=GRN',
                                             handleAs: "json",
                                             form: dom.byId("searchForm"),
                                             load: function(printDocMsg)
                                             {
                                             	if (printDocMsg == 'success')
                                             	{
			                                   		changeToURL('<s:url value="/grn/exportExcel.action" />');
                                             	}
                                             	else
                                             	{
                                             		new InformationDialog({message: printDocMsg}).show();
                                             	}
                                             }
                                         });
                                     }
                              }
                           });
                    }
                });
                
                 if (dom.byId('exportExcel'))
                 {
                 	on(registry.byId("exportExcel"), 'click', 
                 	   function()
                 	   {
                 			(new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />');
                 	   		var selected = customGrid.getCountOfSelected();
	                 	   	if (selected > 0 && selected == customGrid.getCountOfRow())
	                     	 {
	                     		 selectType.show();
	                     	 }
							 else
							 {
								 if(selected < 1)
						 	       {
										var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
			                  			infoDialog.show();
			                  			return; 
									}
			                 	    var grnOids = customGrid.getSelectedItemDocOids();
									var oids = "";
									dojo.forEach(grnOids, function(item)
									{
										oids = oids + item.grnOid + '-';
									});
			                        oids = oids.substring(0, oids.length-1);
			                        xhr.get({
			                              url: '<s:url value="/grn/putParamIntoSession.action" />',
			                              content: {selectedOids: oids},
			                              load: function(data)
			                              {
			                              	if (data!='x')
			                                     {
			                                         (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
			                                     }
			                                     else
			                                     {
			                                    	 xhr.post({
					                                        url: '<s:url value="/ajax/checkDocCount.action" />?printType=excel&msgType=GRN',
					                                        handleAs: "json",
					                                        form: dom.byId("searchForm"),
					                                        load: function(printDocMsg)
					                                        {
					                                        	if (printDocMsg == 'success')
					                                        	{
							                                   		changeToURL('<s:url value="/grn/exportExcel.action" />');
					                                        	}
					                                        	else
					                                        	{
					                                        		new InformationDialog({message: printDocMsg}).show();
					                                        	}
					                                        }
					                                    });
			                                     }
			                              }
			                           });
							 }
                        });
                  }
                    
                    
                 if (dom.byId("exportSummaryExcel"))
                 {
					on(registry.byId("exportSummaryExcel"),'click',
					function()
                       {
						(new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />');
                        if (customGrid.grid.store._items.length == 0)
                        {
                       	    var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1628' />"});
                            infoDialog.show();
                            return;
                        }
                        
                   	 	var validGrnFrom = dijit.byId("param.grnDateFrom").getValue();
                        var validGrnTo = dijit.byId("param.grnDateTo").getValue();
                   	    if(null != validGrnTo && new Date(validGrnFrom) > new Date(validGrnTo))
                        {
                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1701' />"});
                            infoDialog.show();
                            return;
                        }
                       
                        var buyOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.buyerOid}"/>';

	                    if(buyOid != null && buyOid != (""))
	                    {
	                        var validSentFrom = dijit.byId("param.sentDateFrom").getValue();
	                        var validSentTo = dijit.byId("param.sentDateTo").getValue();
                        	if(null != validSentTo && new Date(validSentFrom) > new Date(validSentTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1701' />"});
	                            infoDialog.show();
	                            return;
	                        }
	                       
	                    }

						var supplierOid='<c:out value="${session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';
                        if(supplierOid != null && supplierOid != (""))
		                {
	                        var validReceivedFrom = dijit.byId("param.receivedDateFrom").getValue();
	                        var validReceivedTo = dijit.byId("param.receivedDateTo").getValue();
                        	if(null != validReceivedTo && new Date(validReceivedFrom) > new Date(validReceivedTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1701' />"});
	                            infoDialog.show();
	                            return;
	                        } 
		                }

                        xhr.post({
                            url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=sExcel&msgType=GRN',
                            handleAs: "json",
                            form: dom.byId("searchForm"),
                            load: function(printDocMsg)
                            {
                            	if (printDocMsg == 'success')
                            	{
			                        submitForm('searchForm','<s:url value="/grn/exportSummaryExcel.action" />');
                            	}
                            	else
                            	{
                            		new InformationDialog({message: printDocMsg}).show();
                            	}
                            }
                        });
                    });
                }
               	
            });

    function clearButton(src)
    {
        var registry = require("dijit/registry");
        registry.byId(src).attr("aria-valuenow", null);
		registry.byId(src).attr("value", null);
    }

    //format field as hype link 
    var formatHref = function(field,index,cell)
    {
 	   var bool = '<s:property value="#session.permitUrl.contains(\'/grn/print.action\')" />';
 	   var duplicate = cell.grid.store.getValue(cell.grid.getItem(index), 'duplicate');
       if(bool == 'true')
       {
     	  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
     	  if (duplicate)
       	  {
     		 return "<a style=\"color: red\" href=\"javascript:doPrint("+docOid+", 'GRN');\">"+ field +"</a>"
       	  }
     	  else
       	  {
     		 return "<a href=\"javascript:doPrint("+docOid+", 'GRN');\">"+ field +"</a>"
       	  }
           
       }
       else
       {
    	   if (duplicate)
           {
             return "<span style=\"color: red\" >"+ field +"</span>";
           }
           else
           {
             return field;
           }
       }
    }

    var formatHrefPo = function(field,index,cell)
    {

       var printPo = '<s:property value="#session.permitUrl.contains(\'/po/print.action\')" />';
       var printGrn = '<s:property value="#session.permitUrl.contains(\'/grn/print.action\')" />';
   	   var duplicate = cell.grid.store.getValue(cell.grid.getItem(index), 'duplicate');
       if(printPo == 'true' && printGrn == 'true')
       {
     	  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
     	  if (duplicate)
       	  {
     		 return "<a style=\"color: red\" href=\"javascript:doPrint("+docOid+", 'PO');\">"+ field +"</a>"
       	  }
     	  else
       	  {
     		 return "<a href=\"javascript:doPrint("+docOid+", 'PO');\">"+ field +"</a>"
       	  }
           
       }
       else
       {
    	   if (duplicate)
           {
             return "<span style=\"color: red\" >"+ field +"</span>";
           }
           else
           {
             return field;
           }
       }
       
    }

    //format  status
    var formatStatus = function(field,index,cell)
    {
 	  var duplicate = cell.grid.store.getValue(cell.grid.getItem(index), 'duplicate');
    	  if (duplicate)
    	  {
            return "<span style=\"color: red\">"+ field +"</span>";
    	  }
    	  else
    	  {
    	    return field;
    	  }
    }

    function doPrint(oid, docType)
    {
    	var B2BPortalBase = require("custom/B2BPortalBase");
    	(new B2BPortalBase()).resetTimeout(
                '<s:property value="#session.commonParam.timeout" />',
                '<s:url value="/logout.action" />');
    	var xhr = require("dojo/_base/xhr");
    	var InformationDialog = require("custom/InformationDialog");
    	xhr.get({
    		url: '<s:url value="/grn/putParamIntoSession.action" />',
            content: {selectedOids: oid},
            sync: true,
            load: function(data)
            {
            	if (data!='x')
                {
                    (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                }
                else
                {
	                  xhr.post({
					    		url: '<s:url value="/grn/checkOtherPdfData.action" />?docType='+docType,
					            content: {selectedOids: oid},
					            sync : true,
					            load: function(data)
					            {
					            	if (data!='x')
					                {
					                    (new InformationDialog({message: data})).show();
					                }
					                else
					                {
						                window.open('<s:url value="/grn/print.action" />?docType='+docType);
					                }
					            }
					        });
                }
            }
        });
    }

    function viewDispute(docOid)
    {
        console.log(docOid);
        changeToURL('<s:url value="/grn/viewDispute.action"/>?docOid='+docOid);
    }

    //format dispute status
    var formatDisputeStatus = function(field,index,cell)
    {
		  var disputeStatus = cell.grid.store.getValue(cell.grid.getItem(index), 'disputeStatus');
		  var dispute = cell.grid.store.getValue(cell.grid.getItem(index), 'dispute');
		  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
		  var grnStauts = cell.grid.store.getValue(cell.grid.getItem(index), 'grnStatus');
		  if (dispute && grnStauts !="OUTDATED")
	  	  {
	          return "<a href=\"javascript:viewDispute("+docOid+");\">"+ field +"</a>";
	  	  }
	  	  else
	  	  {
	  	      return "";
	  	  }
    }
    
</script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/grn/search.action')" >
            	<button data-dojo-type="dijit/form/Button" id="searchBtn" ><s:text name="button.search" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/grn/initAudit.action')" >
            	<button data-dojo-type="dijit/form/Button" id="auditBtn" ><s:text name="button.audit" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/grn/initDispute.action')" >
            	<button data-dojo-type="dijit/form/Button" id="disputeBtn" ><s:text name="button.dispute" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/grn/print.action')" >
            	<button data-dojo-type="dijit/form/Button" id="printBtn" ><s:text name="button.print" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/grn/exportExcel.action')" >
            	<button data-dojo-type="dijit/form/Button" id="exportExcel" ><s:text name="button.exportExcel" /></button>
            </s:if>
             <s:if test="#session.permitUrl.contains('/grn/exportSummaryExcel.action')" >
            	<button data-dojo-type="dijit/form/Button" id="exportSummaryExcel" ><s:text name="button.exportSummaryExcel" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>
    <!-- here is message area -->
   	<div id="errorMsg">
	</div>
    <!-- Search Area -->
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="grn.summary.searcharea.headerLabel" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
				<tr>
                	<td width="100"><s:text name="grn.summary.searcharea.buyer" /></td>
                	<td>:</td>
                	<td>
                    	<s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.buyerOid" list="buyers" 
                        	listKey="buyerOid" listValue="buyerName" 
                        	headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                	<td width="100"><s:text name="grn.summary.searcharea.storeCode" /></td>
                    <td>:</td>
                    <td>
                        <s:textfield id="param.storeCode" name="param.storeCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearStoreCode" type="button" onclick="clearButton('param.storeCode');"><s:text name='button.clear' /></button>
                    </td>
             	</tr>
             	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.buyerOid != null" >   
				<tr>
                	<td><s:text name="grn.summary.searcharea.supplierName" /></td>
                	<td>:</td>
                	<td>
                    	<s:textfield name="param.supplierName" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.supplierName"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearSupplierName" type="button" onclick="clearButton('param.supplierName');"><s:text name='button.clear' /></button>
                	</td>
                	<td><s:text name="grn.summary.searcharea.supplierCode" /></td>
                    <td>:</td>
                    <td>
                       <s:textfield name="param.supplierCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple" id="param.supplierCode"/>
                       <button  data-dojo-type="dijit.form.Button" id="clearSupplirCode" type="button" onclick="clearButton('param.supplierCode');"><s:text name='button.clear' /></button>
                    </td>
             	</tr>
             	</s:if>
             	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.supplierOid != null && suppliers != null" >  
             	<tr>
                	<td><s:text name="grn.summary.searcharea.supplier" /></td>
                	<td>:</td>
                	<td>
                	   <s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.supplierOid" list="suppliers" 
                        	listKey="supplierOid" listValue="supplierName + ' (' + supplierCode + ')'" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
             	</tr>
             	</s:if>
             	<tr>
                	<td><s:text name="grn.summary.searcharea.grnNo" /></td>
                	<td>:</td>
                	<td>
                		<s:textfield name="param.grnNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.grnNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearGrnNo" type="button" onclick="clearButton('param.grnNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td><s:text name="grn.summary.searcharea.grnDate" /></td>
                	<td>:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.grnDateFrom" name="param.grnDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.grnDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                	value='<s:date name="param.grnDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearGrnDateFrom" type="button" onclick="clearButton('param.grnDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.grnDateTo" name="param.grnDateTo" 
                        onkeydown="javascript:document.getElementById('param.grnDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                        value='<s:date name="param.grnDateTo" format="yyyy-MM-dd" />'/>
                		<button  data-dojo-type="dijit.form.Button" id="clearGrnDateTo" type="button" onclick="clearButton('param.grnDateTo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
             </tbody>
             </table>
             <table class="commtable">
             <tbody>
                <tr>
                	<td width="100"><s:text name="grn.summary.searcharea.poNo" /></td>
                	<td>:</td>
                	<td>
                		<s:textfield name="param.poNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.poNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearPoNo" type="button" onclick="clearButton('param.poNo');"><s:text name='button.clear' /></button>
                	</td>
                	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.buyerOid != null" >  
                	<td width="100"><s:text name="grn.summary.searcharea.sentDate" /></td>
                	<td>:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.sentDateFrom" name="param.sentDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.sentDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                	value='<s:date name="param.sentDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearSentDateFrom" type="button" onclick="clearButton('param.sentDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.sentDateTo" name="param.sentDateTo" 
                        onkeydown="javascript:document.getElementById('param.sentDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                        value='<s:date name="param.sentDateTo" format="yyyy-MM-dd" />'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearSentDateTo" type="button" onclick="clearButton('param.sentDateTo');"><s:text name='button.clear' /></button>
		                	</td>
		             	</s:if>
		             	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.supplierOid != null" >  
		                	<td width="100"><s:text name="grn.summary.searcharea.receivedDate" /></td>
		                	<td>:</td>
		                	<td>
		                		<label for="Dfrom1"><s:text name="Value.from" /></label>
				                <input type="text" id="param.receivedDateFrom" name="param.receivedDateFrom" 
				                	onkeydown="javascript:document.getElementById('param.receivedDateFrom').blur();" 
				                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
				                	value='<s:date name="param.receivedDateFrom" format="yyyy-MM-dd" />'/>
				                <button  data-dojo-type="dijit.form.Button" id="clearReceivedDateFrom" type="button" onclick="clearButton('param.receivedDateFrom');"><s:text name='button.clear' /></button>
				                <label for="Dto1"><s:text name="Value.to" /></label>
				                <input type="text" id="param.receivedDateTo" name="param.receivedDateTo" 
		                        onkeydown="javascript:document.getElementById('param.receivedDateTo').blur();" 
		                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                        value='<s:date name="param.receivedDateTo" format="yyyy-MM-dd" />'/>
		                        <button  data-dojo-type="dijit.form.Button" id="clearReceivedDateTo" type="button" onclick="clearButton('param.receivedDateTo');"><s:text name='button.clear' /></button>
		                	</td>
		             	</s:if>
             	</tr>
             	</tbody>
            </table>
            <table class="commtable">
            <tbody>
             	<tr>
                    <td width="100"><s:text name="grn.summary.searcharea.readStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.readStatus" list="readStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td width="100"><s:text name="grn.summary.searcharea.grnStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.grnStatus" list="docStatuses" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 2 || #session.SESSION_CURRENT_USER_PROFILE.userType == 4" >   
                    <td width="100"><s:text name="po.summary.searcharea.favouriteList" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.listOid" list="favouriteLists" 
                            listKey="listOid" listValue="listCode" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                </s:if>
                </tr>
            </tbody>
        </table>
        </form>
    </div>
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name="grn.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" class="grid"></div>
</body>
</html>
