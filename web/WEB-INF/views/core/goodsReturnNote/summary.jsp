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
                    		'<s:url value="/rtv/data.action" />',
                            '<s:url value="/ajax/findSummaryFieldAndTooltips.action"/>',
                            [{field: '<s:text name="rtv.summary.grid.rtvNo" />',method: formatHref},
                             {field: '<s:text name="rtv.summary.grid.invNo" />',method: formatHrefInv},
                             {field: '<s:text name="rtv.summary.grid.ctrlStatus" />',method: formatStatus},
                             {field: '<s:text name="rtv.summary.grid.storeCode" />', method: formatStores}]);

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
                               	
	                        	var validInvFrom = dijit.byId("param.rtvDateFrom").getValue();
		                        var validInvTo = dijit.byId("param.rtvDateTo").getValue();
		                        if(null != validInvTo && new Date(validInvFrom) > new Date(validInvTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1901' />"});
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
			                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1901' />"});
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
			                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1901' />"});
			                            infoDialog.show();
			                            return;
			                        }
				                }
				                
                                xhr.post({
                                    url: '<s:url value="/rtv/search.action" />',
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
                        	var validInvFrom = dijit.byId("param.rtvDateFrom").getValue();
	                        var validInvTo = dijit.byId("param.rtvDateTo").getValue();
	                        if(null != validInvTo && new Date(validInvFrom) > new Date(validInvTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1901' />"});
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
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1901' />"});
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
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1901' />"});
		                            infoDialog.show();
		                            return;
		                        }
			                }

	                        xhr.post({
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=pdf&msgType=RTV',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
				                        submitFormInNewWindow('searchForm', '<s:url value="/rtv/print.action" />?selectAll=true');
                                	}
                                	else
                                	{
                                		new InformationDialog({message: printDocMsg}).show();
                                	}
                                }
                            });
                        },
                        noBtnPressed: function(){
                        	var rtvOids = customGrid.getSelectedItemDocOids();
							 var oids = "";
							 dojo.forEach(rtvOids, function(item)
							 {
							 	oids = oids + item.rtvOid + '-';
							 });
	                         oids = oids.substring(0, oids.length-1);
	                         xhr.get({
                                url: '<s:url value="/rtv/putParamIntoSession.action" />',
                                content: {selectedOids: oids},
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
                                            url: '<s:url value="/ajax/checkDocCount.action" />?printType=pdf&msgType=RTV',
                                            handleAs: "json",
                                            form: dom.byId("searchForm"),
                                            load: function(printDocMsg)
                                            {
                                            	if (printDocMsg == 'success')
                                            	{
				                                    window.open('<s:url value="/rtv/print.action" />');
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
								 var rtvOids = customGrid.getSelectedItemDocOids();
								 var oids = "";
								 dojo.forEach(rtvOids, function(item)
								 {
								 	oids = oids + item.rtvOid + '-';
								 });
		                         oids = oids.substring(0, oids.length-1);
		                         xhr.get({
	                                url: '<s:url value="/rtv/putParamIntoSession.action" />',
	                                content: {selectedOids: oids},
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
	                                            url: '<s:url value="/ajax/checkDocCount.action" />?printType=pdf&msgType=RTV',
	                                            handleAs: "json",
	                                            form: dom.byId("searchForm"),
	                                            load: function(printDocMsg)
	                                            {
	                                            	if (printDocMsg == 'success')
	                                            	{
					                                    window.open('<s:url value="/rtv/print.action" />');
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


                    var selectType = new SelectAllConfirmDialog({
                        message: '<s:text name="B2BPC1665" />',
                        yesBtnPressed: function(){
                        	var validInvFrom = dijit.byId("param.rtvDateFrom").getValue();
	                        var validInvTo = dijit.byId("param.rtvDateTo").getValue();
	                        if(null != validInvTo && new Date(validInvFrom) > new Date(validInvTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1901' />"});
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
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1901' />"});
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
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1901' />"});
		                            infoDialog.show();
		                            return;
		                        }
			                }

	                        xhr.post({
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=excel&msgType=RTV',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
			                            submitForm('searchForm', '<s:url value="/rtv/exportExcel.action" />?selectAll=true');
                                	}
                                	else
                                	{
                                		new InformationDialog({message: printDocMsg}).show();
                                	}
                                }
                            });
                        },
                        noBtnPressed: function(){
                        	var rtvOids = customGrid.getSelectedItemDocOids();
							var oids = "";
							dojo.forEach(rtvOids, function(item)
							{
								oids = oids + item.rtvOid + '-';
							});
	                        oids = oids.substring(0, oids.length-1);
	                        xhr.get({
                              url: '<s:url value="/rtv/putParamIntoSession.action" />',
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
                                        url: '<s:url value="/ajax/checkDocCount.action" />?printType=excel&msgType=RTV',
                                        handleAs: "json",
                                        form: dom.byId("searchForm"),
                                        load: function(printDocMsg)
                                        {
                                        	if (printDocMsg == 'success')
                                        	{
			                              		changeToURL('<s:url value="/rtv/exportExcel.action" />');
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
			                 	    var rtvOids = customGrid.getSelectedItemDocOids();
									var oids = "";
									dojo.forEach(rtvOids, function(item)
									{
										oids = oids + item.rtvOid + '-';
									});
			                        oids = oids.substring(0, oids.length-1);
			                        xhr.get({
			                              url: '<s:url value="/rtv/putParamIntoSession.action" />',
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
			                                        url: '<s:url value="/ajax/checkDocCount.action" />?printType=excel&msgType=RTV',
			                                        handleAs: "json",
			                                        form: dom.byId("searchForm"),
			                                        load: function(printDocMsg)
			                                        {
			                                        	if (printDocMsg == 'success')
			                                        	{
						                              		changeToURL('<s:url value="/rtv/exportExcel.action" />');
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
	                        
	                   	 	var validRtvFrom = dijit.byId("param.rtvDateFrom").getValue();
	                        var validRtvTo = dijit.byId("param.rtvDateTo").getValue();
	                   	    if(null != validRtvTo && new Date(validRtvFrom) > new Date(validRtvTo))
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
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=sExcel&msgType=RTV',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
				                        submitForm('searchForm','<s:url value="/rtv/exportSummaryExcel.action" />');
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
     	   var bool = '<s:property value="#session.permitUrl.contains(\'/rtv/print.action\')" />';
     	   var duplicate = cell.grid.store.getValue(cell.grid.getItem(index), 'duplicate');
           if(bool == 'true')
           {
         	  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
         	  if (duplicate)
           	  {
         		  return "<a style=\"color: red\" href=\"javascript:doPrint("+docOid+", 'RTV');\" >"+ field +"</a>"
           	  }
         	  else
           	  {
         		  return "<a  href=\"javascript:doPrint("+docOid+", 'RTV');\" >"+ field +"</a>"
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
        
        var formatHrefInv = function(field,index,cell)
        {
     	   var printInv = '<s:property value="#session.permitUrl.contains(\'/inv/print.action\')" />';
     	   var printRtv = '<s:property value="#session.permitUrl.contains(\'/rtv/print.action\')" />';
     	   var duplicate = cell.grid.store.getValue(cell.grid.getItem(index), 'duplicate');
           if(printInv == 'true' && printRtv == 'true')
           {
         	  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
         	  if (duplicate)
           	  {
         		  return "<a style=\"color: red\" href=\"javascript:doPrint("+docOid+", 'INV');\">"+ (field == null?'':field) +"</a>"
           	  }
         	  else
           	  {
         		 return "<a  href=\"javascript:doPrint("+docOid+", 'INV');\">"+ (field == null?'':field) +"</a>"
           	  }
           }
           else
           {
        	   if (duplicate)
           	   {
        		   return "<span style=\"color: red\" >"+ (field == null?'':field) +"</span>";
           	   }
          	   else
           	   {
          		 return field == null?'':field;
           	   }
         	  
           }
        }

        var formatStores = function(field, index, cell)
        {
            if (field == null || "" == field)
            {
                return '&nbsp;';
            }
            
            var stores = field.split("new-line");
            var rlt = stores[0];
            
            for (var i=1; i<stores.length; i++)
            {
                rlt = rlt + "</br>" + stores[i];
            }
            
            return rlt;
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
                url: '<s:url value="/rtv/putParamIntoSession.action" />',
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
				                url: '<s:url value="/rtv/checkOtherPdfData.action" />?docType='+docType,
				                content: {selectedOids: oid},
				                sync : true,
				                load: function(data)
				                {
					                console.log(data);
				                	if (data!='x')
				                    {
				                        (new InformationDialog({message: data})).show();
				                    }
				                    else
				                    {
					                     window.open('<s:url value="/rtv/print.action" />?docType='+docType, "_blank")
				                    }
				                }
				            });
                    }
                }
            });
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
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/rtv/search.action')" >
            	<button data-dojo-type="dijit/form/Button" id="searchBtn" ><s:text name="button.search" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/rtv/print.action')" >
            	<button data-dojo-type="dijit/form/Button" id="printBtn" ><s:text name="button.print" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/rtv/generateInv.action')" >
            	<button data-dojo-type="dijit/form/Button" id="approveBtn" ><s:text name="button.generate.einvoice" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/rtv/exportExcel.action')" >
            	<button data-dojo-type="dijit/form/Button" id="exportExcel" ><s:text name="button.exportExcel" /></button>
            </s:if>
             <s:if test="#session.permitUrl.contains('/rtv/exportSummaryExcel.action')" >
            	<button data-dojo-type="dijit/form/Button" id="exportSummaryExcel" ><s:text name="button.exportSummaryExcel" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>
    <!-- Search Area -->
    <div id="tooltip"></div>
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="rtv.summary.searcharea.headerLabel" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
				<tr>
                	<td width="100"><s:text name="rtv.summary.searcharea.buyer" /></td>
                	<td>:</td>
                	<td>
                    	<s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.buyerOid" list="buyers" 
                        	listKey="buyerOid" listValue="buyerName" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                	<td width="100"><s:text name="rtv.summary.searcharea.storeCode" /></td>
                    <td>:</td>
                    <td>
                        <s:textfield id="param.storeCode" name="param.storeCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearStoreCode" type="button" onclick="clearButton('param.storeCode');"><s:text name='button.clear' /></button>
                    </td>
             	</tr>
             	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.buyerOid != null" >   
				<tr>
                	<td><s:text name="rtv.summary.searcharea.supplierName" /></td>
                	<td>:</td>
                	<td>
                    	<s:textfield name="param.supplierName" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.supplierName"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearSupplierName" type="button" onclick="clearButton('param.supplierName');"><s:text name='button.clear' /></button>
                	</td>
                	<td><s:text name="rtv.summary.searcharea.supplierCode" /></td>
                    <td>:</td>
                    <td>
                       <s:textfield name="param.supplierCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple" id="param.supplierCode"/>
                       <button  data-dojo-type="dijit.form.Button" id="clearSupplierCode" type="button" onclick="clearButton('param.supplierCode');"><s:text name='button.clear' /></button>
                    </td>
             	</tr>
             	</s:if>
             	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.supplierOid != null && suppliers != null" >  
             	<tr>
                	<td><s:text name="rtv.summary.searcharea.supplier" /></td>
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
                	<td><s:text name="rtv.summary.searcharea.rtvNo" /></td>
                	<td>:</td>
                	<td>
                		<s:textfield name="param.rtvNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.rtvNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearRtvNo" type="button" onclick="clearButton('param.rtvNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td><s:text name="rtv.summary.searcharea.rtvDate" /></td>
                	<td>:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.rtvDateFrom" name="param.rtvDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.rtvDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                	value='<s:date name="param.rtvDateFrom" format="yyyy-MM-dd" />'/>
		               	<button  data-dojo-type="dijit.form.Button" id="clearRtvDateFrom" type="button" onclick="clearButton('param.rtvDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.rtvDateTo" name="param.rtvDateTo" 
                        onkeydown="javascript:document.getElementById('param.rtvDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                        value='<s:date name="param.rtvDateTo" format="yyyy-MM-dd" />'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearRtvDateTo" type="button" onclick="clearButton('param.rtvDateTo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
             </tbody>
             </table>
             <table class="commtable">
             <tbody>
             	<tr>
                	<td width="100"><s:text name="rtv.summary.searcharea.invNo" /></td>
                	<td>:</td>
                	<td>
                		<s:textfield name="param.invNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.invNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearInvNo" type="button" onclick="clearButton('param.invNo');"><s:text name='button.clear' /></button>
                	</td>
                	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.buyerOid != null" >  
                	<td width="100"><s:text name="po.summary.searcharea.sentDate" /></td>
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
	                	<td width="100"><s:text name="po.summary.searcharea.receivedDate" /></td>
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
                    <td width="100"><s:text name="rtv.summary.searcharea.readStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.readStatus" list="readStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td width="100"><s:text name="rtv.summary.searcharea.rtvStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.rtvStatus" list="docStatuses" 
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
        <div class="title"><s:text name="rtv.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" class="grid"></div>
</body>
</html>
