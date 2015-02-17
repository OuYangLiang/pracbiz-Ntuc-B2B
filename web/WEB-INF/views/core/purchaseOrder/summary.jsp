<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <style type="text/css">
    @import "<s:url value='/js/dojo-root/dojox/grid/resources/Grid.css' />";
    @import "<s:url value='/js/dojo-root/dojox/grid/resources/claroGrid.css' />";
    @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/%{#session.layoutTheme}/EnhancedGrid.css' />";
    @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css' />";
    
    .odd {
    	background-color: #EEEEEE;
    }   
    .event {
        background-color: #FFFFFF;
    }
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
                 "custom/ConfirmDialog",
                 "custom/SelectAllConfirmDialog",
                 "custom/PrintPdfConfirmDialog",
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
                     ConfirmDialog,
                     SelectAllConfirmDialog,
                     PrintPdfConfirmDialog,
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
                    customGrid.setIsPoPage(true);
                    customGrid.setFormatByItem(formatByItem);
                    customGrid.setFormatByStore(formatByStore);
                    customGrid.setCurrentUserType('<s:property value="#session.SESSION_CURRENT_USER_PROFILE.userType"/>');
                    customGrid.initGrid(
                    		'<s:property value="pageId"/>',
                    		'<s:url value="/po/data.action" />',
                            '<s:url value="/ajax/findSummaryFieldAndTooltips.action"/>',
                            [{field: '<s:text name="po.summary.grid.poNo" />',method: formatHref},
                             {field: '<s:text name="po.summary.searcharea.poStatus" />',method: formatStatus}, 
                             {field: '<s:text name="po.summary.searcharea.storeCode" />',method: formatStores}]);

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
                              	
                        	 	var validPoFrom = dijit.byId("param.poDateFrom").getValue();
		                        var validPoTo = dijit.byId("param.poDateTo").getValue();
                        	    if(null != validPoTo && new Date(validPoFrom) > new Date(validPoTo))
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
                                    url: '<s:url value="/po/search.action" />',
                                    form: dom.byId("searchForm"),
                                    load: function(rlt)
                                    {
                                    	customGrid.grid.setQuery();
                                    }
                                });
                            }
                        );
                    }

                    if (dom.byId("generateInvBtn"))
                    {
                    	on(registry.byId("generateInvBtn"), 'click', function()
                        {
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
                        	var url = '<s:url value="/po/initGenerateInv.action" />';
                        	var cfmUrl = '<s:url value="/po/initGenerateInv.action" />?docOid='+docOid;
							xhr.post({
                            	url: url,
                                handleAs: "json",
                                content: {docOid: docOid},
                                load:function(jsonData)
                                {
                                	var poExpiredMsg = jsonData.poExpiredMsg;
                                	if (poExpiredMsg != null)
                                	{
                                		cfmUrl = cfmUrl + '&poExpiredMsg=poExpiredMsg';
                                		var confirmDialog = new ConfirmDialog({
                                            message: poExpiredMsg,
                                            yesBtnPressed: function(){
                                                changeToURL(cfmUrl);
                                            }
                                        });
                                    	confirmDialog.show();
                                    	return;
                                	}
                                	
                                    var errorMsg = jsonData.errorMsg;
                                    if (errorMsg!=null)
                                    {
                                    	var obj = dojo.byId("errorMsg");
                                    	obj.innerHTML=errorMsg;
                                    	dojo.addClass(obj,"error");
                                    	return;
                                    }
                                    pass = true;
                                },
                                error: function(response, ioArgs)
                                {
                                	changeToURL(cfmUrl)
                                    return response; 
                                }
                    		});
                        });
                    }

                    var selectPDFType = new SelectAllConfirmDialog({
                        message: '<s:text name="B2BPC1665" />',
                        yesBtnPressed: function(){
                        	var validPoFrom = dijit.byId("param.poDateFrom").getValue();
	                        var validPoTo = dijit.byId("param.poDateTo").getValue();
                    	    if(null != validPoTo && new Date(validPoFrom) > new Date(validPoTo))
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
	                        selectPrintType.show();
                        },
                        noBtnPressed: function(){
                        	var poOids = customGrid.getSelectedItemDocOids();
							 var oids = "";
							 dojo.forEach(poOids, function(item)
							 {
								 var defaultPdfFlag = "I";
								 try
								 {
									 if (document.getElementsByName("pdfFlag_"+item.poOid)[0].checked)
									 {
										 defaultPdfFlag = document.getElementsByName("pdfFlag_"+item.poOid)[0].value;  
									 }
									 else
									 {
										 defaultPdfFlag = document.getElementsByName("pdfFlag_"+item.poOid)[1].value;  
									 }
								 }
								 catch(e)
								 {

								 }
								 oids += item.poOid + '/' + defaultPdfFlag + '-'; 
							 });
	                        oids = oids.substring(0, oids.length-1);
	                        xhr.get({
	                                url: '<s:url value="/po/putParamIntoSession.action" />',
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
	                                            url: '<s:url value="/ajax/checkDocCount.action" />?printType=pdf&msgType=PO',
	                                            handleAs: "json",
	                                            form: dom.byId("searchForm"),
	                                            load: function(printDocMsg)
	                                            {
	                                            	if (printDocMsg == 'success')
	                                            	{
	                                            		window.open('<s:url value="/po/print.action" />');
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

                    var selectPrintType = new PrintPdfConfirmDialog({
                        message: '<s:text name="B2BPC1666" />',
                        yesBtnPressed: function(){
                        	xhr.post({
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=pdf&msgType=PO',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
	                        			submitFormInNewWindow('searchForm', '<s:url value="/po/print.action" />?selectAll=true&typePrint=I');
                                	}
                                	else
                                	{
                                		new InformationDialog({message: printDocMsg}).show();
                                	}
                                }
                            });
                        },
                        noBtnPressed: function(){
                        	xhr.post({
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=pdf&msgType=PO',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
                        				submitFormInNewWindow('searchForm', '<s:url value="/po/print.action" />?selectAll=true&typePrint=S');
                                	}
                                	else
                                	{
                                		new InformationDialog({message: printDocMsg}).show();
                                	}
                                }
                            });
                        }
                    });

                    if (dom.byId("printBtn"))
                    {
                    	(new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />');
						on(registry.byId("printBtn"),'click',
							function()
                            {
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
								 var poOids = customGrid.getSelectedItemDocOids();
								 var oids = "";
								 dojo.forEach(poOids, function(item)
								 {
									 var defaultPdfFlag = "I";
									 try
									 {
										 if (document.getElementsByName("pdfFlag_"+item.poOid)[0].checked)
										 {
											 defaultPdfFlag = document.getElementsByName("pdfFlag_"+item.poOid)[0].value;  
										 }
										 else
										 {
											 defaultPdfFlag = document.getElementsByName("pdfFlag_"+item.poOid)[1].value;  
										 }
									 }
									 catch(e)
									 {

									 }
									 oids += item.poOid + '/' + defaultPdfFlag + '-'; 
								 });
		                        oids = oids.substring(0, oids.length-1);
		                        xhr.get({
		                                url: '<s:url value="/po/putParamIntoSession.action" />',
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
		                                            url: '<s:url value="/ajax/checkDocCount.action" />?printType=pdf&msgType=PO',
		                                            handleAs: "json",
		                                            form: dom.byId("searchForm"),
		                                            load: function(printDocMsg)
		                                            {
		                                            	if (printDocMsg == 'success')
		                                            	{
		                                            		window.open('<s:url value="/po/print.action" />');
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
                        	var validPoFrom = dijit.byId("param.poDateFrom").getValue();
	                        var validPoTo = dijit.byId("param.poDateTo").getValue();
                    	    if(null != validPoTo && new Date(validPoFrom) > new Date(validPoTo))
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
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=excel&msgType=PO',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
                            			submitForm('searchForm', '<s:url value="/po/exportExcel.action" />?selectAll=true');
                                	}
                                	else
                                	{
                                		new InformationDialog({message: printDocMsg}).show();
                                	}
                                }
                            });
                        },
                        noBtnPressed: function(){
                        	var poOids = customGrid.getSelectedItemDocOids();
                            var oids = "";
                            dojo.forEach(poOids, function(item)
                            {
                               oids = oids + item.poOid + '-';
                            });
                           oids = oids.substring(0, oids.length-1);
	                        xhr.get({
                                url: '<s:url value="/po/putParamIntoSession.action" />',
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
	                                            url: '<s:url value="/ajax/checkDocCount.action" />?printType=excel&msgType=PO',
	                                            handleAs: "json",
	                                            form: dom.byId("searchForm"),
	                                            load: function(printDocMsg)
	                                            {
	                                            	if (printDocMsg == 'success')
	                                            	{
                                          				changeToURL('<s:url value="/po/exportExcel.action" />');
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

                    
                    if (dom.byId("exportExcel"))
                    {
                    	(new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />'); 
						on(registry.byId("exportExcel"),'click',
							function()
                            {
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
									 var poOids = customGrid.getSelectedItemDocOids();
		                             var oids = "";
		                             dojo.forEach(poOids, function(item)
		                             {
		                                oids = oids + item.poOid + '-';
		                             });
		                             oids = oids.substring(0, oids.length-1);
			                         xhr.get({
		                                url: '<s:url value="/po/putParamIntoSession.action" />',
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
		                                            url: '<s:url value="/ajax/checkDocCount.action" />?printType=excel&msgType=PO',
		                                            handleAs: "json",
		                                            form: dom.byId("searchForm"),
		                                            load: function(printDocMsg)
		                                            {
		                                            	if (printDocMsg == 'success')
		                                            	{
				                                            changeToURL('<s:url value="/po/exportExcel.action" />');
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
                    	(new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />');
						on(registry.byId("exportSummaryExcel"),'click',
						function()
                        {
	                        if (customGrid.grid.store._items.length == 0)
	                        {
	                        	  var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1628' />"});
		                            infoDialog.show();
		                            return;
	                        }
	                        
                    	 	var validPoFrom = dijit.byId("param.poDateFrom").getValue();
	                        var validPoTo = dijit.byId("param.poDateTo").getValue();
                    	    if(null != validPoTo && new Date(validPoFrom) > new Date(validPoTo))
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
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=sExcel&msgType=PO',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
				                        submitForm('searchForm','<s:url value="/po/exportSummaryExcel.action" />');
                                	}
                                	else
                                	{
                                		new InformationDialog({message: printDocMsg}).show();
                                	}
                                }
                            });
                           
	                    });
                    }

                    if (dom.byId("editDeliveryDate"))
                    {
						on(registry.byId("editDeliveryDate"),'click',
							function()
                            {
							 var selected = customGrid.getCountOfSelected();
							 if(selected < 1)
							 {
								var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
	                   			infoDialog.show();
	                   			return; 
							 }
							 if(selected > 1)
							 {
								var infoDialog = new InformationDialog({message: '<s:text name="alert.select.one" />'});
	                   			infoDialog.show();
	                   			return; 
							 }
							 var poOids = customGrid.getSelectedItemDocOids();
                             var oids = "";
                             dojo.forEach(poOids, function(item)
                             {
                                oids = oids + item.poOid + '-';
                             });
                             oids = oids.substring(0, oids.length-1);
	                         xhr.get({
	                                url: '<s:url value="/po/putParamIntoSession.action" />',
	                                content: {selectedOids: oids},
	                                load: function(data)
	                                {
	                                	if (data!='x')
	                                    {
	                                        (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
	                                    }
	                                    else
	                                    {
	                                    	xhr.get({
	        	                                url: '<s:url value="/po/initEditDeliveryDate.action" />',
	        	                                handleAs: "json",
	        	                                load: function(data)
	        	                                {
		        	                                var errorMsg=data.errorMsg;
		        	                                var editPo=data.editPo;
	        	                                	if (errorMsg=="noPrivilege")
	                                                {
	                                                    (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
	                                                }
	        	                                	else if (errorMsg=="notExist")
	        	                                	{
	        	                                		(new InformationDialog({message: '<s:text name="B2BPC1702" />'})).show();
	        	                                	}
	        	                                	else if (errorMsg=="invoiced")
	        	                                	{
	        	                                		(new InformationDialog({message: '<s:text name="B2BPC1703" />'})).show();
	        	                                	}
	        	                                	else if (errorMsg=="cancelled")
	        	                                	{
	        	                                		(new InformationDialog({message: '<s:text name="B2BPC1704" />'})).show();
	        	                                	}
	        	                                	else if (errorMsg=="outDated")
	        	                                	{
	        	                                		(new InformationDialog({message: '<s:text name="B2BPC1706" />'})).show();
	        	                                	}
	        	                                	else if (errorMsg=="cnType")
	        	                                	{
	        	                                		(new InformationDialog({message: '<s:text name="B2BPC1710" />'})).show();
	        	                                	}
	                                                else
	                                                {
	                                                	initDeliveryDateDiv(editPo);
	                                                    registry.byId("editDeliveryDateDiv").show();
	                                                }
	        	                                }
	                                    	});
	                                    }
	                                }
	                            });
	                     });
                    }

                    hideDiv = function(src)
                    {
                        registry.byId(src).hide();
                    };
                    
                    refreshGrid = function()
                    {
                    	customGrid.grid.setQuery();
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
    	  var bool = '<s:property value="#session.permitUrl.contains(\'/po/print.action\')" />';
    	  var duplicate = cell.grid.store.getValue(cell.grid.getItem(index), 'duplicate');
    	  
          if(bool == 'true')
          {
        	  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
        	  
        	  if (duplicate)
        	  {
                return "<a style=\"color: red\" href=\"javascript:doPrint("+docOid+");\" >"+ field +"</a>";
        	  }
        	  else
        	  {
        	    return "<a href=\"javascript:doPrint("+docOid+");\" >"+ field +"</a>";
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
          
        	  return field;
          }
       }
       
       //format po status
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

       var formatByItem = function(field,index,cell)
       {
    	   var locationCount = cell.grid.store.getValue(cell.grid.getItem(index), 'locationCount');
           if (locationCount > 1)
           {
        	   var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
        	    return '<input data-dojo-type="dijit/form/RadioButton" type="radio" name="pdfFlag_'+docOid+'" value="I"/>';
           }
           return "";
       }

       var formatByStore = function(field,index,cell)
       {
           var locationCount = cell.grid.store.getValue(cell.grid.getItem(index), 'locationCount');
           if (locationCount > 1)
           {
        	   var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
        	    return '<input data-dojo-type="dijit/form/RadioButton" checked="checked" type="radio" name="pdfFlag_'+docOid+'" value="S"/>';
           }
           return "";
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

        function doPrint(oid)
        {
        	var B2BPortalBase = require("custom/B2BPortalBase");
        	(new B2BPortalBase()).resetTimeout(
                    '<s:property value="#session.commonParam.timeout" />',
                    '<s:url value="/logout.action" />');
        	var xhr = require("dojo/_base/xhr");
        	var InformationDialog = require("custom/InformationDialog");
        	var defaultPdfFlag = "I";
            try
            {
                if (document.getElementsByName("pdfFlag_"+oid)[0].checked)
                {
                    defaultPdfFlag = document.getElementsByName("pdfFlag_"+oid)[0].value;  
                }
                else
                {
                    defaultPdfFlag = document.getElementsByName("pdfFlag_"+oid)[1].value;  
                }
            }
            catch(e)
            {

            }
            oid += '/' + defaultPdfFlag; 
        	xhr.get({
                url: '<s:url value="/po/putParamIntoSession.action" />',
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
                        window.open('<s:url value="/po/print.action" />');
                    }
                }
            });

        	
        }

        function saveEdit()
        {
        	var B2BPortalBase = require("custom/B2BPortalBase");
        	(new B2BPortalBase()).resetTimeout(
                    '<s:property value="#session.commonParam.timeout" />',
                    '<s:url value="/logout.action" />');
        	var xhr = require("dojo/_base/xhr");
        	var registry = require("dijit/registry");
        	var InformationDialog = require("custom/InformationDialog");
        	var domStyle = require("dojo/dom-style");
        	var dom = require("dojo/dom");

        	var poOid = document.getElementById("poOid").value;
        	var deliveryDateFrom = document.getElementById("deliveryDateFrom").value;
			var deliveryDateTo = document.getElementById("deliveryDateTo").value;

        	//document.getElementById("loadingOverlay").style.display="";
        	var csrfToken = document.getElementById("csrfToken").value;
        	xhr.post({
                url: '<s:url value="/po/saveEditDeliveryDate.action" />',
                content: {"editPo.poOid": poOid, "editPo.deliveryDateFrom":deliveryDateFrom, "editPo.deliveryDateTo":deliveryDateTo, "csrfToken": csrfToken},
                handleAs: "json",
                load: function(data)
                {
                	var errorMsg=data.errorMsg;
                	if (errorMsg=="notExist")
                	{
                		registry.byId("editDeliveryDateDiv").show();
                		(new InformationDialog({message: '<s:text name="B2BPC1702" />'})).show();
                	}
                	else if (errorMsg=="dateFromToWrong")
                	{
                		(new InformationDialog({message: '<s:text name="B2BPC1701" />'})).show();
                	}
                	else if (errorMsg=="notChange")
                	{
                		registry.byId("editDeliveryDateDiv").show();
                		(new InformationDialog({message: '<s:text name="B2BPC1705" />'})).show();
                	}
                	else if (errorMsg=="dateError")
                	{
                		registry.byId("editDeliveryDateDiv").show();
                    	var dateError=data.dateError;
                		(new InformationDialog({message: dateError})).show();
                	}
                	else if (errorMsg=="isEditting")
                	{
						return;
                	}
                    else
                    {
                    	registry.byId("editDeliveryDateDiv").hide();
                    	refreshGrid();
                    	(new InformationDialog({message: '<s:text name="B2BPC1707" />'})).show();
                    }
                }
            });
        }

        function cancelledEdit()
        {
        	hideDiv("editDeliveryDateDiv");
        }

        function initDeliveryDateDiv(editPo)
        {
			document.getElementById("buyerCode").innerHTML=editPo.buyerCode;
			document.getElementById("buyerName").innerHTML=editPo.buyerName;
			document.getElementById("supplierCode").innerHTML=editPo.supplierCode;
			document.getElementById("supplierName").innerHTML=editPo.supplierName;
			document.getElementById("poNo").innerHTML=editPo.poNo;
			document.getElementById("poDate").innerHTML=editPo.poDate.substr(0,10);
			document.getElementById("poType").innerHTML=editPo.poType;
			document.getElementById("totalCost").innerHTML=editPo.totalCost;
			var deliveryDateFrom=editPo.deliveryDateFrom;
			if (deliveryDateFrom)
			{
				var year=deliveryDateFrom.substr(0,10).split("-")[0];
				var month=deliveryDateFrom.substr(0,10).split("-")[1];
				var day=deliveryDateFrom.substr(0,10).split("-")[2];
				document.getElementById("deliveryDateFrom").value=day+"/"+month+"/"+year;
			}
			var deliveryDateTo=editPo.deliveryDateTo;
			if (deliveryDateTo)
			{
				var year=deliveryDateTo.substr(0,10).split("-")[0];
				var month=deliveryDateTo.substr(0,10).split("-")[1];
				var day=deliveryDateTo.substr(0,10).split("-")[2];
				document.getElementById("deliveryDateTo").value=day+"/"+month+"/"+year;
			}
			document.getElementById("poOid").value=editPo.poOid;
        }

        
        
    </script>
    <div dojoType="dijit.Dialog" id="editDeliveryDateDiv" title="<B>Edit Delivery Date</B>" style="width:50%;font-weight: bolder;">
    <form id="editForm" name="editForm" method="post">
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
        <table class="commtable">
            <tr class="odd">
                <td  style="font-weight: bolder;"><s:text name="po.edit.delivery.date.buyerCode" /></td>
                <td><span id="buyerCode" ></span></td>
                <td  style="font-weight: bolder;"><s:text name="po.edit.delivery.date.buyerName" /></td>
                <td><span id="buyerName" ></span></td>
            </tr>
            <tr class="even">
                <td  style="font-weight: bolder;"><s:text name="po.edit.delivery.date.supplierCode" /></td>
                <td><span id="supplierCode" ></span></td>
                <td  style="font-weight: bolder;"><s:text name="po.edit.delivery.date.supplierName" /></td>
                <td><span id="supplierName" ></span></td>
            </tr>
            <tr class="odd">
                <td  style="font-weight: bolder;"><s:text name="po.edit.delivery.date.poNo" /></td>
                <td><span id="poNo" ></span></td>
                <td  style="font-weight: bolder;"><s:text name="po.edit.delivery.date.poDate" /></td>
                <td><span id="poDate" ></span></td>
            </tr>
            <tr class="even">
                <td  style="font-weight: bolder;"><s:text name="po.edit.delivery.date.poType" /></td>
                <td><span id="poType" ></span></td>
                <td  style="font-weight: bolder;"><s:text name="po.edit.delivery.date.totalCost" /></td>
                <td><span id="totalCost" ></span></td>
            </tr>
            <tr class="odd">
                <td  style="font-weight: bolder;"><s:text name="po.edit.delivery.date.deliveryDate" /></td>
                <td colspan="3">
                	<label for="Dfrom1"><s:text name="Value.from" /></label>
                	<input type="text" id="deliveryDateFrom" name="editPo.deliveryDateFrom" 
		            	onkeydown="javascript:document.getElementById('deliveryDateFrom').blur();" 
		            	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		            	value='<s:date name="editPo.deliveryDateFrom" format="yyyy-MM-dd" />'/>
		            <button  data-dojo-type="dijit.form.Button" id="clearEditDeliveryDateFrom" type="button" onclick="clearButton('deliveryDateFrom');"><s:text name='button.clear' /></button>
		            <label for="Dto1"><s:text name="Value.to" /></label>
		            <input type="text" id="deliveryDateTo" name="editPo.deliveryDateTo" 
		            	onkeydown="javascript:document.getElementById('deliveryDateTo').blur();" 
		            	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		            	value='<s:date name="editPo.deliveryDateTo" format="yyyy-MM-dd" />'/>
		            <button  data-dojo-type="dijit.form.Button" id="clearEditDeliveryDateTo" type="button" onclick="clearButton('deliveryDateTo');"><s:text name='button.clear' /></button>
		            <input id="poOid" name="editPo.poOid" style="display : none" />
		        </td>
            </tr>
            <tr class="space"><td></td></tr>
            <tr>
                <td colspan="4" style="text-align:left">
                   <button data-dojo-type="dijit.form.Button"  onclick="saveEdit();"><s:text name="button.ok"/></button>
                   <button data-dojo-type="dijit.form.Button" onclick="cancelledEdit();"><s:text name="button.cancel"/></button>
               </td>
           </tr>
        </table>
    </form>
    </div>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/po/search.action')" >
            	<button data-dojo-type="dijit/form/Button" id="searchBtn" ><s:text name="button.search" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/po/print.action')" >
            	<button data-dojo-type="dijit/form/Button" id="printBtn" ><s:text name="button.print" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/po/exportExcel.action')" >
            	<button data-dojo-type="dijit/form/Button" id="exportExcel" ><s:text name="button.exportExcel" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/po/exportSummaryExcel.action')" >
            	<button data-dojo-type="dijit/form/Button" id="exportSummaryExcel" ><s:text name="button.exportSummaryExcel" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/po/generateInv.action')" >
            	<button data-dojo-type="dijit/form/Button" id="generateInvBtn" ><s:text name="button.generate.einvoice" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/po/initEditDeliveryDate.action')" >
            	<button data-dojo-type="dijit/form/Button" id="editDeliveryDate" ><s:text name="button.editDeliveryDate" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>
    
    <!-- Search Area -->
   	<div id="errorMsg">
	</div>
    
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="po.summary.searcharea.headerLabel" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
				<tr>
                	<td width="85"><s:text name="po.summary.searcharea.buyer" /></td>
                	<td>:</td>
                	<td>
                    	<s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.buyerOid" list="buyers" 
                        	listKey="buyerOid" listValue="buyerName" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                	<td width="85"><s:text name="po.summary.searcharea.storeCode" /></td>
                    <td>:</td>
                    <td>
                        <s:textfield id="param.storeCode" name="param.storeCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearStoreCode" type="button" onclick="clearButton('param.storeCode');"><s:text name='button.clear' /></button>
                    </td>
             	</tr>
             	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.buyerOid != null" >   
				<tr>
                	<td><s:text name="po.summary.searcharea.supplierName" /></td>
                	<td>:</td>
                	<td>
                        <s:textfield name="param.supplierName" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.supplierName"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearSupplierName" type="button" onclick="clearButton('param.supplierName');"><s:text name='button.clear' /></button>
                	</td>
                	<td><s:text name="po.summary.searcharea.supplierCode" /></td>
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
                	<td><s:text name="po.summary.searcharea.supplier" /></td>
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
                	<td><s:text name="po.summary.searcharea.poNo" /></td>
                	<td>:</td>
                	<td>
                		<s:textfield name="param.poNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.poNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearPoNo" type="button" onclick="clearButton('param.poNo');"><s:text name='button.clear' /></button>	
                	</td>
                	<s:if test="">
                	
                	</s:if>
                	<td width="85"><s:text name="po.summary.searcharea.deliveryDate" /></td>
                	<td>:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.deliveryDateFrom" name="param.deliveryDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.deliveryDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
		                	value='<s:date name="param.deliveryDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="cleardeliveryDateFrom" type="button" onclick="clearButton('param.deliveryDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.deliveryDateTo" name="param.deliveryDateTo" 
                        onkeydown="javascript:document.getElementById('param.deliveryDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
                        value='<s:date name="param.deliveryDateTo" format="yyyy-MM-dd"/>'/>
                        <button  data-dojo-type="dijit.form.Button" id="cleardeliveryDateTo" type="button" onclick="clearButton('param.deliveryDateTo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
             	<tr>
                	<td width="85"><s:text name="po.summary.searcharea.poDate" /></td>
                	<td>:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.poDateFrom" name="param.poDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.poDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
		                	value='<s:date name="param.poDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearPoDateFrom" type="button" onclick="clearButton('param.poDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.poDateTo" name="param.poDateTo" 
                        onkeydown="javascript:document.getElementById('param.poDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
                        value='<s:date name="param.poDateTo" format="yyyy-MM-dd"/>'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearPoDateTo" type="button" onclick="clearButton('param.poDateTo');"><s:text name='button.clear' /></button>
                	</td>
                	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.buyerOid != null" >  
                	<td width="85"><s:text name="po.summary.searcharea.sentDate" /></td>
                	<td>:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.sentDateFrom" name="param.sentDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.sentDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
		                	value='<s:date name="param.sentDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearSentDateFrom" type="button" onclick="clearButton('param.sentDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.sentDateTo" name="param.sentDateTo" 
                        onkeydown="javascript:document.getElementById('param.sentDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
                        value='<s:date name="param.sentDateTo" format="yyyy-MM-dd" />'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearSentDateTo" type="button" onclick="clearButton('param.sentDateTo');"><s:text name='button.clear' /></button>
                	</td>
             		</s:if>
             		<s:if test="#session.SESSION_CURRENT_USER_PROFILE.supplierOid != null" >  
                	<td width="85"><s:text name="po.summary.searcharea.receivedDate" /></td>
                	<td>:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.receivedDateFrom" name="param.receivedDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.receivedDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
		                	value='<s:date name="param.receivedDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearReceivedDateFrom" type="button" onclick="clearButton('param.receivedDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.receivedDateTo" name="param.receivedDateTo" 
                        onkeydown="javascript:document.getElementById('param.receivedDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
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
                	<td width="85"><s:text name="po.summary.searcharea.poStatus" /></td>
                	<td>:</td>
                	<td>
		                <s:select data-dojo-type="dijit/form/Select" 
                    		name="param.poStatus" list="docStatuses" 
                        	listKey="key" listValue="value" 
                        	headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                	<td><s:text name="po.summary.searcharea.poType" /></td>
                	<td>:</td>
                	<td>
		                <s:select data-dojo-type="dijit/form/Select" 
                    		name="param.poType" list="docTypes" 
                        	listKey="key" listValue="value" 
                        	headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                	<td><s:text name="po.summary.searcharea.readStatus" /></td>
                	<td>:</td>
                	<td>
		                <s:select data-dojo-type="dijit/form/Select" 
                    		name="param.readStatus" list="readStatus" 
                        	listKey="key" listValue="value" 
                        	headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                	<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
             		<td><s:text name="po.summary.searcharea.poSubType2"/></td>
                	<td>:</td>
                	<td>
		                <s:select data-dojo-type="dijit/form/Select" 
                    		name="param.poSubType2" list="poSubTypes" 
                        	listKey="key" listValue="value" 
                        	headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
             	</s:if>
             	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 2 || #session.SESSION_CURRENT_USER_PROFILE.userType == 4" >   
                    <td><s:text name="po.summary.searcharea.favouriteList" /></td>
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
        <div class="title"><s:text name="po.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" class="grid"></div>
</body>
</html>
