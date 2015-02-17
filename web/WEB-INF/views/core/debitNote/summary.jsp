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
                 "custom/ConfirmDialog",
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
                     ConfirmDialog,
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

                    initData();
                    function initData()
                    {
                    	<s:if test="#session.permitUrl.contains('/dn/saveClose.action')" >
	         					if(dom.byId('param.pendingForClosing').checked)
	         					{
	         		        		dom.byId('param.buyerStatus').disabled=true;
	         		        	}
         				</s:if>
                    }
                
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
                    		'<s:url value="/dn/data.action" />',
                            '<s:url value="/ajax/findSummaryFieldAndTooltips.action"/>',
                            [{field: '<s:text name="dn.summary.grid.dnNo" />',method: formatHref},
                             {field: '<s:text name="dn.summary.grid.rtvNo" />',method: formatHrefRtv},
                             {field: '<s:text name="dn.summary.grid.giNo" />',method: formatHrefGi},
                             {field: '<s:text name="dn.summary.grid.poNo" />',method: formatHrefPo},
                             {field: '<s:text name="dn.summary.grid.invNo" />',method: formatHrefInv},
                             {field: '<s:text name="dn.summary.grid.ctrlStatus" />',method: formatStatus},
                             {field: '<s:text name="dn.summary.grid.action" />',method: formatAction},
                             {field: '<s:text name="dn.summary.grid.dispute" />',method: formatBoolean},
                             {field: '<s:text name="dn.summary.grid.closed" />',method: formatBoolean},
                             {field: '<s:text name="dn.summary.grid.generatedOnPortal" />',method: formatBoolean},
                             {field: '<s:text name="dn.summary.grid.buyerStatus" />',method: formatAllStatus},
                             {field: '<s:text name="dn.summary.grid.priceStatus" />',method: formatPriceStatus},
                             {field: '<s:text name="dn.summary.grid.qtyStatus" />',method: formatQtyStatus},
                             {field: '<s:text name="dn.summary.grid.sentToSupplier" />',method: formatBoolean},
                             {field: '<s:text name="dn.summary.grid.markSentToSupplier" />',method: formatBoolean},
                             {field: '<s:text name="dn.summary.grid.disputeWindow" />',method: formatDisputeWindow}
                             ]);

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
                            	
	                        	var validDnFrom = dijit.byId("param.dnDateFrom").getValue();
		                        var validDnTo = dijit.byId("param.dnDateTo").getValue();
		                        if(null != validDnTo && new Date(validDnFrom) > new Date(validDnTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
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
			                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
			                            infoDialog.show();
			                            return;
			                        }
			                    }

		                    	var supplierOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';

		                        if(supplierOid != null && supplierOid != (""))
			                    {
			                        var validReceivedFrom = dijit.byId("param.receivedDateFrom").getValue();
			                        var validReceivedTo = dijit.byId("param.receivedDateTo").getValue();
			                        if(null != validReceivedTo && new Date(validReceivedFrom) > new Date(validReceivedTo))
			                        {
			                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
			                            infoDialog.show();
			                            return;
			                        }
			                    }
			                    
                                xhr.post({
                                    url: '<s:url value="/dn/search.action" />',
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
                        	var validDnFrom = dijit.byId("param.dnDateFrom").getValue();
	                        var validDnTo = dijit.byId("param.dnDateTo").getValue();
	                        if(null != validDnTo && new Date(validDnFrom) > new Date(validDnTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
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
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
		                            infoDialog.show();
		                            return;
		                        }
		                    }

	                    	var supplierOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';

	                        if(supplierOid != null && supplierOid != (""))
		                    {
		                        var validReceivedFrom = dijit.byId("param.receivedDateFrom").getValue();
		                        var validReceivedTo = dijit.byId("param.receivedDateTo").getValue();
		                        if(null != validReceivedTo && new Date(validReceivedFrom) > new Date(validReceivedTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
		                            infoDialog.show();
		                            return;
		                        }
		                    }

	                        xhr.post({
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=pdf&msgType=DN',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
				                        submitFormInNewWindow('searchForm', '<s:url value="/dn/print.action" />?selectAll=true');
                                	}
                                	else
                                	{
                                		new InformationDialog({message: printDocMsg}).show();
                                	}
                                }
                            });
                        },
                        noBtnPressed: function(){
                        	var dnOids = customGrid.getSelectedItemDocOids();
							 var oids = "";
							 dojo.forEach(dnOids, function(item)
							 {
							 	oids = oids + item.dnOid + '-';
							 });
	                        oids = oids.substring(0, oids.length-1);
	                        xhr.get({
	                                url: '<s:url value="/dn/putOidsParamIntoSession.action" />',
	                                content: {selectedOids: oids},
	                                sync : true,
	                                load: function(data)
	                                {
	                                	if (data!='x')
	                                    {
	                                        (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
	                                    }
	                                    else
	                                    {
	                                    	xhr.post({
	                                            url: '<s:url value="/ajax/checkDocCount.action" />?printType=pdf&msgType=DN',
	                                            handleAs: "json",
	                                            form: dom.byId("searchForm"),
	                                            load: function(printDocMsg)
	                                            {
	                                            	if (printDocMsg == 'success')
	                                            	{
					                                    window.open('<s:url value="/dn/print.action" />');
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
								 var dnOids = customGrid.getSelectedItemDocOids();
								 var oids = "";
								 dojo.forEach(dnOids, function(item)
								 {
								 	oids = oids + item.dnOid + '-';
								 });
		                        oids = oids.substring(0, oids.length-1);
		                        xhr.get({
		                                url: '<s:url value="/dn/putOidsParamIntoSession.action" />',
		                                content: {selectedOids: oids},
		                                sync : true,
		                                load: function(data)
		                                {
		                                	if (data!='x')
		                                    {
		                                        (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
		                                    }
		                                    else
		                                    {
		                                    	xhr.post({
		                                            url: '<s:url value="/ajax/checkDocCount.action" />?printType=pdf&msgType=DN',
		                                            handleAs: "json",
		                                            form: dom.byId("searchForm"),
		                                            load: function(printDocMsg)
		                                            {
		                                            	if (printDocMsg == 'success')
		                                            	{
						                                    window.open('<s:url value="/dn/print.action" />');
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

                    
                    if (dom.byId("addBtn"))
                    {
                        on(registry.byId("addBtn"), 'click',
                    		function()
                            {
                        	   changeToURL('<s:url value="/dn/initAdd.action" />');
                       	    }
                        );
                    }

                    
                    if (dom.byId("editBtn"))
                    {
                        on(registry.byId("editBtn"), 'click',
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
							 var dnOids = customGrid.getSelectedItemDocOids();
							 var oids = "";
							 dojo.forEach(dnOids, function(item)
							 {
							 	oids = oids + item.dnOid + '-';
							 });
	                        oids = oids.substring(0, oids.length-1);
	                        xhr.get({
                                url: '<s:url value="/dn/putOidsParamIntoSession.action" />',
                                content: {selectedOids: oids},
                                form: dom.byId("searchForm"),
                                load: function(data)
                                {
                                	if (data!='x')
                                    {
                                        (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                                    }
                                    else
                                    {
	                                    xhr.get({
	                                        url: '<s:url value="/dn/checkSentStatus.action" />',
	                                        handleAs: "json",
	                                        load: function(data)
	                                        {
	                                            if(data == 1)
	                                            {
	                                                var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2219" />'});
	                                                infoDialog.show();
	                                                return;
	                                            }
	                                            else if(data == 2)
	                                            {
	                                                var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2220" />'});
	                                                infoDialog.show();
	                                                return;
	                                            }
	                                            changeToURL('<s:url value="/dn/initEdit.action" />');
	                                        }
	                                    });
                                    }
                                }
                            });
                           });
                    }


                    if (dom.byId("deleteBtn"))
                    {
                        on(registry.byId("deleteBtn"), 'click', 
                            function()
                            {
	                        	var selected = customGrid.getCountOfSelected();
	                            if(selected < 1)
	                            {
	                               var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
	                               infoDialog.show();
	                               return; 
	                            }
	                            var confirmDialog = new ConfirmDialog({
	                                message: '<s:text name="alert.delete.records" />',
	                                yesBtnPressed: function(){
		                            var dnOids = customGrid.getSelectedItemDocOids();
		                            var oids = "";
		                            dojo.forEach(dnOids, function(item)
		                            {
		                               oids = oids + item.dnOid + '-';
		                            });
		                           oids = oids.substring(0, oids.length-1);
		                           var csrfToken = dom.byId("csrfToken").value;
		                           xhr.get({
		                                   url: '<s:url value="/dn/putOidsParamIntoSession.action" />',
		                                   content: {selectedOids: oids},
		                                   load: function(data)
		                                   {
		                                	   if (data!='x')
		                                       {
		                                           (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
		                                       }
		                                       else
		                                       {
			                                	   changeToURL('<s:url value="/dn/saveDelete.action" />?csrfToken='+csrfToken);
		                                       }
		                                   }
		                               });
		                            }
	                            });
	                            confirmDialog.show();
                            }
                        )
                    }
                    
                    
                    if (dom.byId("approveBtn"))
                    {
                        on(registry.byId("approveBtn"), 'click',
                           function()
                           {
                        	 (new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
                        	 var selected = customGrid.getCountOfSelected();
							 if(selected < 1)
							 {
								var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
	                   			infoDialog.show();
	                   			return; 
							 }
							 var dnOids = customGrid.getSelectedItemDocOids();
							 var oids = "";
							 dojo.forEach(dnOids, function(item)
							 {
							 	oids = oids + item.dnOid + '-';
							 });
	                        oids = oids.substring(0, oids.length-1);
	                        var csrfToken = dom.byId("csrfToken").value;
	                        xhr.get({
                                url: '<s:url value="/dn/putOidsParamIntoSession.action" />',
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
	                                        url: '<s:url value="/dn/checkSentStatus.action" />',
	                                        handleAs: "json",
	                                        load: function(data)
	                                        {
	                                        	if(data == 1)
	                                            {
	                                                var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2202" />'});
	                                                infoDialog.show();
	                                                return;
	                                            }
	                                        	changeToURL('<s:url value="/dn/approve.action" />?csrfToken='+csrfToken);
	                                        }
	                                    });
                                    }
                                }
                            });
                           });
                    }
                    
                    if (dom.byId("rejectBtn"))
                    {
                        on(registry.byId("rejectBtn"), 'click',
                           function()
                           {
                        	 (new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
                        	 var selected = customGrid.getCountOfSelected();
							 if(selected < 1)
							 {
								var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
	                   			infoDialog.show();
	                   			return; 
							 }
							 var dnOids = customGrid.getSelectedItemDocOids();
							 var oids = "";
							 dojo.forEach(dnOids, function(item)
							 {
							 	oids = oids + item.dnOid + '-';
							 });
	                        oids = oids.substring(0, oids.length-1);
	                        var csrfToken = dom.byId("csrfToken").value;
	                        xhr.get({
	                                url: '<s:url value="/dn/putOidsParamIntoSession.action" />',
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
		                                        url: '<s:url value="/dn/checkSentStatus.action" />',
		                                        handleAs: "json",
		                                        load: function(data)
		                                        {
			                                        if(data == 1)
			                                        {
			                                            var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2202" />'});
			                                            infoDialog.show();
			                                            return;
			                                        }
			                                        xhr.get({
		                                                url: '<s:url value="/dn/reject.action" />?csrfToken='+csrfToken,
		                                                load: function(data)
		                                                {
		                                                    customGrid.grid.setQuery();
		                                                }
		                                            });
		                                        }
		                                    });
	                                    }
	                                }
	                            });
                           });
                    }

                    var selectType = new SelectAllConfirmDialog({
                        message: '<s:text name="B2BPC1665" />',
                        yesBtnPressed: function(){
                        	var validDnFrom = dijit.byId("param.dnDateFrom").getValue();
	                        var validDnTo = dijit.byId("param.dnDateTo").getValue();
	                        if(null != validDnTo && new Date(validDnFrom) > new Date(validDnTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
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
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
		                            infoDialog.show();
		                            return;
		                        }
		                    }

	                    	var supplierOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';

	                        if(supplierOid != null && supplierOid != (""))
		                    {
		                        var validReceivedFrom = dijit.byId("param.receivedDateFrom").getValue();
		                        var validReceivedTo = dijit.byId("param.receivedDateTo").getValue();
		                        if(null != validReceivedTo && new Date(validReceivedFrom) > new Date(validReceivedTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
		                            infoDialog.show();
		                            return;
		                        }
		                    }

	                        xhr.post({
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=excel&msgType=DN',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
			                            submitForm('searchForm', '<s:url value="/dn/exportExcel.action" />?selectAll=true');
                                	}
                                	else
                                	{
                                		new InformationDialog({message: printDocMsg}).show();
                                	}
                                }
                            });
                        },
                        noBtnPressed: function(){
                        	var dnOids = customGrid.getSelectedItemDocOids();
							var oids = "";
							dojo.forEach(dnOids, function(item)
							{
								oids = oids + item.dnOid + '-';
							});
	                        oids = oids.substring(0, oids.length-1);
	                        xhr.get({
	                                url: '<s:url value="/dn/putOidsParamIntoSession.action" />',
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
                                                url: '<s:url value="/ajax/checkDocCount.action" />?printType=excel&msgType=DN',
                                                handleAs: "json",
                                                form: dom.byId("searchForm"),
                                                load: function(printDocMsg)
                                                {
                                                	if (printDocMsg == 'success')
                                                	{
					                                    changeToURL('<s:url value="/dn/exportExcel.action" />');
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
									var dnOids = customGrid.getSelectedItemDocOids();
									var oids = "";
									dojo.forEach(dnOids, function(item)
									{
										oids = oids + item.dnOid + '-';
									});
			                        oids = oids.substring(0, oids.length-1);
			                        xhr.get({
			                                url: '<s:url value="/dn/putOidsParamIntoSession.action" />',
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
				                                        url: '<s:url value="/ajax/checkDocCount.action" />?printType=excel&msgType=DN',
				                                        handleAs: "json",
				                                        form: dom.byId("searchForm"),
				                                        load: function(printDocMsg)
				                                        {
				                                        	if (printDocMsg == 'success')
				                                        	{
							                                    changeToURL('<s:url value="/dn/exportExcel.action" />');
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
                    	   		if(selected < 1)
							 	{
									var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
		                   			infoDialog.show();
		                   			return; 
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
	                        
                    	 	var validDnFrom = dijit.byId("param.dnDateFrom").getValue();
	                        var validDnTo = dijit.byId("param.dnDateTo").getValue();
                    	    if(null != validDnTo && new Date(validDnFrom) > new Date(validDnTo))
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
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=sExcel&msgType=DN',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
				                        submitForm('searchForm','<s:url value="/dn/exportSummaryExcel.action" />');
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

        //format dnNo as hype link 
        var formatHref = function(field,index,cell)
        {
     	   var bool = '<s:property value="#session.permitUrl.contains(\'/dn/print.action\')" />';
     	   var duplicate = cell.grid.store.getValue(cell.grid.getItem(index), 'duplicate');
           if(bool == 'true')
           {
         	  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
         	  if (duplicate)
           	  {
         		 return "<a style=\"color: red\" href=\"javascript:doPrint("+docOid+", 'DN');\">"+ field +"</a>";
           	  }
         	  else
           	  {
         		 return "<a href=\"javascript:doPrint("+docOid+", 'DN');\">"+ field +"</a>";
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
        
        //format rtvNo as hype link 
        var formatHrefRtv = function(field,index,cell)
        {
           var printDn = '<s:property value="#session.permitUrl.contains(\'/dn/print.action\')" />';
     	   var printRtv = '<s:property value="#session.permitUrl.contains(\'/rtv/print.action\')" />';
     	   var duplicate = cell.grid.store.getValue(cell.grid.getItem(index), 'duplicate');
           if(printDn == 'true' && printRtv == 'true')
           {
         	  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
         	  if (duplicate)
           	  {
         		 return "<a style=\"color: red\" href=\"javascript:doPrint("+docOid+", 'RTV');\">"+ (field == null?'':field) +"</a>";
           	  }
         	  else
           	  {
         		 return "<a href=\"javascript:doPrint("+docOid+", 'RTV');\">"+ (field == null?'':field) +"</a>";
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
                 return (field == null?'':field);
               }
           }
        }
        
        //format GiNo as hype link 
        var formatHrefGi = function(field,index,cell)
        {
           var printDn = '<s:property value="#session.permitUrl.contains(\'/dn/print.action\')" />';
     	   var printGi = '<s:property value="#session.permitUrl.contains(\'/gi/print.action\')" />';
     	   var duplicate = cell.grid.store.getValue(cell.grid.getItem(index), 'duplicate');
           if(printDn == 'true' && printGi == 'true')
           {
         	  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
         	  if (duplicate)
           	  {
         		 return "<a style=\"color: red\" href=\"javascript:doPrint("+docOid+", 'GI');\">"+ (field == null?'':field) +"</a>";
           	  }
         	  else
           	  {
         		 return "<a href=\"javascript:doPrint("+docOid+", 'GI');\">"+ (field == null?'':field) +"</a>";
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
                 return (field == null?'':field);
               }
           }
        }
        
        //format PoNo as hype link 
        var formatHrefPo = function(field,index,cell)
        {
           var printDn = '<s:property value="#session.permitUrl.contains(\'/dn/print.action\')" />';
     	   var printPo = '<s:property value="#session.permitUrl.contains(\'/po/print.action\')" />';
     	   var duplicate = cell.grid.store.getValue(cell.grid.getItem(index), 'duplicate');
           if(printDn == 'true' && printPo == 'true')
           {
         	  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
         	  if (duplicate)
           	  {
         		 return "<a style=\"color: red\" href=\"javascript:doPrint("+docOid+", 'PO');\">"+ (field == null?'':field) +"</a>";
           	  }
         	  else
           	  {
         		 return "<a href=\"javascript:doPrint("+docOid+", 'PO');\">"+ (field == null?'':field) +"</a>";
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
                 return (field == null?'':field);
               }
           }
        }
        
        //format InvNo as hype link 
        var formatHrefInv = function(field,index,cell)
        {
           var printDn = '<s:property value="#session.permitUrl.contains(\'/dn/print.action\')" />';
     	   var printInv = '<s:property value="#session.permitUrl.contains(\'/inv/print.action\')" />';
     	   var duplicate = cell.grid.store.getValue(cell.grid.getItem(index), 'duplicate');
           if(printDn == 'true' && printInv == 'true')
           {
         	  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
         	  if (duplicate)
           	  {
         		 return "<a style=\"color: red\" href=\"javascript:doPrint("+docOid+", 'INV');\">"+ (field == null?'':field) +"</a>";
           	  }
         	  else
           	  {
         		 return "<a href=\"javascript:doPrint("+docOid+", 'INV');\">"+ (field == null?'':field) +"</a>";
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
                 return (field == null?'':field);
               }
           }
        }

        //format status
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

        //format action
        var formatAction = function(field,index,cell)
        {
        	  var result = ""; 
        	  
     	  	  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'dnOid');
     	  	  var closed = cell.grid.store.getValue(cell.grid.getItem(index), 'closed');
     	  	  var buyerStatus = cell.grid.store.getValue(cell.grid.getItem(index), 'buyerStatus');
     	  	  var priceStatus = cell.grid.store.getValue(cell.grid.getItem(index), 'priceStatus');
     	  	  var qtyStatus = cell.grid.store.getValue(cell.grid.getItem(index), 'qtyStatus');
     	  	  var dispute = cell.grid.store.getValue(cell.grid.getItem(index), 'dispute');
     	  	  var priceDisputed = cell.grid.store.getValue(cell.grid.getItem(index), 'priceDisputed');
     	  	  var qtyDisputed = cell.grid.store.getValue(cell.grid.getItem(index), 'qtyDisputed');
     	  	  var allowMatchingDnDispute = cell.grid.store.getValue(cell.grid.getItem(index), 'allowMatchingDnDispute');
     	  	  var invNo = cell.grid.store.getValue(cell.grid.getItem(index), 'invNo');
     	  	  
     	  	  var hasClosePrivilege = <s:property value='#session.permitUrl.contains("/dn/initClose.action")'/> || <s:property value='#session.permitUrl.contains("/dn/initCloseWithoutEdit.action")'/>;
     	  	  var hasAuditPricePrivilege = <s:property value='#session.permitUrl.contains("/dn/initAuditPrice.action")'/>;
     	  	  var hasAuditQtyPrivilege = <s:property value='#session.permitUrl.contains("/dn/initAuditQty.action")'/>;
     	  	  var hasDisputePrivilege = <s:property value='#session.permitUrl.contains("/dn/initDispute.action")'/>;
     	  	  
        	  if (hasClosePrivilege)
        	  {
	        	  if (!closed && dispute && buyerStatus != 'PENDING')
	        	  {
	        		  result += "<a href='javascript:close("+docOid+",1)'>Close</a><br/>";
	        	  }
	        	  else if (closed && dispute && buyerStatus != 'PENDING')
	        	  {
	        		  result += "<a href='javascript:close("+docOid+",2)'>View Close</a><br/>";
	        	  }
        	  }
        	  
        	  if (hasAuditPricePrivilege)
        	  {
	        	  if (priceDisputed && priceStatus == 'PENDING' && !closed)
	              {
	        		  result += "<a href='javascript:auditPrice("+docOid+",1)'>Price Approval</a><br/>";
	              }
	        	  else if (priceDisputed)
	        	  {
	        		  result += "<a href='javascript:auditPrice("+docOid+",2)'>View Price Approval</a><br/>";
	        	  }
        	  }
        	  
        	  if (hasAuditQtyPrivilege)
        	  {
	        	  if (qtyDisputed && qtyStatus == 'PENDING' && !closed)
	              {
	        		  result += "<a href='javascript:auditQty("+docOid+",1)'>Qty Approval</a><br/>";
	              }
	        	  else if (qtyDisputed)
	              {
	        		  result += "<a href='javascript:auditQty("+docOid+",2)'>View Qty Approval</a><br/>";
	              }
        	  }
        	  
        	  
        	  if (hasDisputePrivilege)
        	  {
        		  if (invNo != null && invNo != '' && !allowMatchingDnDispute)
        		  {
        			  return result;
        		  }
        		  else if (!dispute && !closed)
	              {
	        		  result += "<a href='javascript:dispute("+docOid+",1)'>Dispute</a><br/>";
	              }
	        	  else if ((dispute && closed) || (dispute && !closed))
	              {
	        		  result += "<a href='javascript:dispute("+docOid+",2)'>View Dispute</a><br/>";
	              }
        	  }
        	  
        	  return result;
        	  
        }

        var formatDisputeWindow = function(field,index,cell)
        {
        	var alertWindow = cell.grid.store.getValue(cell.grid.getItem(index), 'alertWindow');
    	  	if ((field != null && field != '') && (alertWindow != null && alertWindow != ''))
       	  	{
    	  		if ((parseFloat(field) <= parseFloat(alertWindow)) && parseFloat(alertWindow) != 0)
       	  		{
           	  		return "<span style=\"color: red\">"+ field +"</span>";
       	  		}
    	  		else
       	  		{
    	  			return field == null ? '' : field;
       	  		}
       	  	}
    	  	else
       	  	{
           	  	return field == null ? '' : field;
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
                url: '<s:url value="/dn/putOidsParamIntoSession.action" />',
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
					    		url: '<s:url value="/dn/checkOtherPdfData.action" />?docType='+docType,
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
						                 window.open('<s:url value="/dn/print.action" />?docType='+docType);
					                }
					            }
					        });
                    }
                }
            });
        }

        
        function close(docOid, displayType)
        {
        	var closeWithEdit = <s:property value='#session.permitUrl.contains("/dn/initClose.action")'/>;
        	if (closeWithEdit)
        	{
        	    changeToURL('<s:url value="/dn/initClose.action" />?docOid='+docOid+'&actionType=close&displayType='+displayType);
        	}
        	else
        	{
        	    changeToURL('<s:url value="/dn/initCloseWithoutEdit.action" />?docOid='+docOid+'&actionType=close&displayType='+displayType);
        	}
        }
        
        
        function auditPrice(docOid, displayType)
        {
            changeToURL('<s:url value="/dn/initAuditPrice.action" />?docOid='+docOid+'&actionType=price&displayType='+displayType);
        }
        
        
        function auditQty(docOid, displayType)
        {
            changeToURL('<s:url value="/dn/initAuditQty.action" />?docOid='+docOid+'&actionType=qty&displayType='+displayType);
        }

        
        function dispute(docOid, displayType)
        {
        	changeToURL('<s:url value="/dn/initDispute.action" />?docOid='+docOid+'&actionType=dispute&displayType='+displayType);
        }
        
        var formatAllStatus = function(field,index,cell)
        {
        	var dispute = cell.grid.store.getValue(cell.grid.getItem(index), 'dispute');
        	if (dispute)
        	{
	        	return field;
        	}
       		return "";
        }
        
        var formatPriceStatus = function(field,index,cell)
        {
        	var dispute = cell.grid.store.getValue(cell.grid.getItem(index), 'dispute');
        	var priceDisputed = cell.grid.store.getValue(cell.grid.getItem(index), 'priceDisputed');
        	if (dispute && priceDisputed)
        	{
	        	return field;
        	}
       		return "";
        }
        
        var formatQtyStatus = function(field,index,cell)
        {
        	var dispute = cell.grid.store.getValue(cell.grid.getItem(index), 'dispute');
        	var qtyDisputed = cell.grid.store.getValue(cell.grid.getItem(index), 'qtyDisputed');
        	if (dispute && qtyDisputed)
        	{
	        	return field;
        	}
       		return "";
        }


        function clickPendingForClosing(src)
        {
        	var registry = require("dijit/registry");
        	if (registry.byId(src).checked)
        	{
        		registry.byId('param.buyerStatus').attr("disabled", true);
        	}
        	else
        	{
        		registry.byId('param.buyerStatus').attr("disabled", false);
        	}
        }
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/dn/search.action')" >
            	<button data-dojo-type="dijit/form/Button" id="searchBtn" ><s:text name="button.search" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/dn/initAdd.action')" >
            	<button data-dojo-type="dijit/form/Button" id="addBtn" ><s:text name="button.add" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/dn/initEdit.action')" >
            	<button data-dojo-type="dijit/form/Button" id="editBtn" ><s:text name="button.edit" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/dn/saveDelete.action')" >
            	<button data-dojo-type="dijit.form.Button" id="deleteBtn" ><s:text name="button.delete" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/dn/approve.action')" >
            	<button data-dojo-type="dijit/form/Button" id="approveBtn" ><s:text name="button.approve" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/dn/reject.action')" >
            	<button data-dojo-type="dijit/form/Button" id="rejectBtn" ><s:text name="button.reject" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/dn/print.action')" >
                <button data-dojo-type="dijit/form/Button" id="printBtn" ><s:text name="button.print" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/dn/exportExcel.action')" >
            	<button data-dojo-type="dijit/form/Button" id="exportExcel" ><s:text name="button.exportExcel" /></button>
            </s:if>
             <s:if test="#session.permitUrl.contains('/dn/exportSummaryExcel.action')" >
            	<button data-dojo-type="dijit/form/Button" id="exportSummaryExcel" ><s:text name="button.exportSummaryExcel" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>
    <!-- here is message area -->
	<div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    <!-- Search Area -->
    <div id="tooltip"></div>
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="dn.summary.searcharea.headerLabel" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
		<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
		<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 2 || #session.SESSION_CURRENT_USER_PROFILE.userType == 4">
			<table class="commtable">
            <tbody>
				<tr>
                	<td width="100"><s:text name="dn.summary.searcharea.buyer" /></td>
                	<td width="10">:</td>
                	<td>
                    	<s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.buyerOid" list="buyers" 
                        	listKey="buyerOid" listValue="buyerName" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                    <td width="100"><s:text name="dn.summary.searcharea.storeCode" /></td>
                    <td>:</td>
                    <td>
                        <s:textfield name="param.storeCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.storeCode"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearStoreCode" type="button" onclick="clearButton('param.storeCode');"><s:text name='button.clear' /></button>
                    </td>
                </tr>
                <tr>
                	<td width="100"><s:text name="dn.summary.searcharea.supplierName" /></td>
                	<td width="10">:</td>
                	<td>
                    	<s:textfield name="param.supplierName" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.supplierName"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearSupplierName" type="button" onclick="clearButton('param.supplierName');"><s:text name='button.clear' /></button>
                	</td>
                    <td><s:text name="dn.summary.searcharea.supplierCode" /></td>
                    <td>:</td>
                    <td>
                       <s:textfield name="param.supplierCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple" id="param.supplierCode"/>
                       <button  data-dojo-type="dijit.form.Button" id="clearSupplierCode" type="button" onclick="clearButton('param.supplierCode');"><s:text name='button.clear' /></button>
                    </td>
                </tr>
                <tr>
                	<td width="100"><s:text name="dn.summary.searcharea.dnNo" /></td>
                	<td width="10">:</td>
                	<td>
                		<s:textfield name="param.dnNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.dnNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearDnNo" type="button" onclick="clearButton('param.dnNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="dn.summary.searcharea.dnDate" /></td>
                	<td width="10">:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.dnDateFrom" name="param.dnDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.dnDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                	value='<s:date name="param.dnDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearDnDateFrom" type="button" onclick="clearButton('param.dnDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.dnDateTo" name="param.dnDateTo" 
                        onkeydown="javascript:document.getElementById('param.dnDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                        value='<s:date name="param.dnDateTo" format="yyyy-MM-dd" />'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearDnDateTo" type="button" onclick="clearButton('param.dnDateTo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
             	<tr>
             		<td></td>
             		<td></td>
             		<td></td>
             		<td><s:text name="dn.summary.searcharea.sentDate" /></td>
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
             		
             	</tr>
             	</tbody>
             </table>
             <table class="commtable">
             <tbody>
             	<tr>
             		<td width="100"><s:text name="dn.summary.searcharea.poNo" /></td>
                	<td width="10">:</td>
                	<td>
                		<s:textfield name="param.poNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.poNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearPoNo" type="button" onclick="clearButton('param.poNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="dn.summary.searcharea.rtvNo" /></td>
                	<td width="10">:</td>
                	<td>
                		<s:textfield name="param.rtvNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.rtvNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearRtvNo" type="button" onclick="clearButton('param.rtvNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="dn.summary.searcharea.invNo" /></td>
                	<td width="10">:</td>
                	<td>
                		<s:textfield name="param.invNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.invNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearInvNo" type="button" onclick="clearButton('param.invNo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
            </tbody>
         </table>
         <s:if test="#session.permitUrl.contains('/dn/saveClose.action') || #session.permitUrl.contains('/poInvGrnDnMatching/initAuditPrice.action')">
         <table class="commtable">
             <tbody>
             	<tr>
             	<s:if test="#session.permitUrl.contains('/dn/saveClose.action')">
             		<td width="103"><s:text name="dn.summary.searcharea.pendingForClosing" /></td>
                	<td>:</td>
                	<td><s:checkbox id="param.pendingForClosing" name="param.pendingForClosing" data-dojo-type="dijit.form.CheckBox" onclick="clickPendingForClosing('param.pendingForClosing')" theme="simple"/></td>
                </s:if>
                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/initAuditPrice.action')" >
    				<td width="100"><s:text name="dn.summary.searcheara.priceApprove" /></td>
   					<td>:</td>
   					<td><s:checkbox id="param.priceApprove" name="param.priceApprove" data-dojo-type="dijit.form.CheckBox" theme="simple"/></td>
   				</s:if>
                </tr>
                
              </tbody>
          </table>
         </s:if>
        <table class="commtable">
             <tbody>
                 <tr>
                    <td width="100"><s:text name="dn.summary.searcharea.dnType" /></td>
                    <td width="10">:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.dnType" list="dnTypes" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.dnStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.dnStatus" list="dnStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.readStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.readStatus" list="readStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
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
                <tr>
                <td>
                	<s:text name="dn.summary.searcharea.dispute" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.dispute" list="#{true:'YES', false:'NO'}" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                	<td><s:text name="dn.summary.searcharea.buyerStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" id="param.buyerStatus"
                            name="param.buyerStatus" list="buyerStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.priceStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.priceStatus" list="priceStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.qtyStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.qtyStatus" list="qtyStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.closed" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.closed" list="#{true:'YES', false:'NO'}" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                </tr>
                <tr>
                <td>
                	<s:text name="dn.summary.searcharea.priceDisputed" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.priceDisputed" list="#{true:'YES', false:'NO'}" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                	<td><s:text name="dn.summary.searcharea.qtyDisputed" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.qtyDisputed" list="#{true:'YES', false:'NO'}" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                </tr>
             </tbody>
        </table>
		
		</s:if>
		<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 3 || #session.SESSION_CURRENT_USER_PROFILE.userType == 5">
			<table class="commtable" style="padding: 0em;">
            <tbody>
				<tr>
                	<td width="100"><s:text name="dn.summary.searcharea.buyer" /></td>
                	<td width="10">:</td>
                	<td>
                    	<s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.buyerOid" list="buyers" 
                        	listKey="buyerOid" listValue="buyerName" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                    <td width="100"><s:text name="dn.summary.searcharea.storeCode" /></td>
                    <td width="10">:</td>
                    <td>
                        <s:textfield name="param.storeCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.storeCode"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearStoreCode" type="button" onclick="clearButton('param.storeCode');"><s:text name='button.clear' /></button>
                    </td>
                </tr>
                <s:if test="suppliers != null">
                <tr>
                	<td width="100"><s:text name="dn.summary.searcharea.supplier" /></td>
                	<td width="10">:</td>
                	<td>
                	    <s:select data-dojo-type="dijit/form/FilteringSelect" id="supplierCodeList"
                    		name="param.supplierOid" list="suppliers" 
                        	listKey="supplierOid" listValue="supplierName + ' (' + supplierCode + ')'" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
             	</tr>
             	</s:if>
             	<tr>
                	<td width="100"><s:text name="dn.summary.searcharea.dnNo" /></td>
                	<td width="10">:</td>
                	<td>
                		<s:textfield name="param.dnNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.dnNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearDnNo" type="button" onclick="clearButton('param.dnNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="dn.summary.searcharea.dnDate" /></td>
                	<td width="10">:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.dnDateFrom" name="param.dnDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.dnDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                	value='<s:date name="param.dnDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearDnDateFrom" type="button" onclick="clearButton('param.dnDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.dnDateTo" name="param.dnDateTo" 
                        onkeydown="javascript:document.getElementById('param.dnDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                        value='<s:date name="param.dnDateTo" format="yyyy-MM-dd" />'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearDnDateTo" type="button" onclick="clearButton('param.dnDateTo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
             	<tr>
             		<td></td>
             		<td></td>
             		<td></td>
             		<td><s:text name="dn.summary.searcharea.receivedDate" /></td>
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
             	</tr>
             	</tbody>
             </table>
             <table class="commtable" style="padding: 0em;">
             <tbody>
             	<tr>
             		<td width="100"><s:text name="dn.summary.searcharea.poNo" /></td>
                	<td>:</td>
                	<td>
                		<s:textfield name="param.poNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.poNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearPoNo" type="button" onclick="clearButton('param.poNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="dn.summary.searcharea.rtvNo" /></td>
                	<td>:</td>
                	<td>
                		<s:textfield name="param.rtvNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.rtvNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearRtvNo" type="button" onclick="clearButton('param.rtvNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="dn.summary.searcharea.invNo" /></td>
                	<td>:</td>
                	<td>
                		<s:textfield name="param.invNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.invNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearInvNo" type="button" onclick="clearButton('param.invNo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
            </tbody>
         </table>
         
        <table class="commtable" style="padding: 0em;">
             <tbody>
                 <tr>
                    <td width="100"><s:text name="dn.summary.searcharea.dnType" /></td>
                    <td width="10">:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.dnType" list="dnTypes" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.dnStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.dnStatus" list="dnStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.readStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.readStatus" list="readStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.closed" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.closed" list="#{true:'YES', false:'NO'}" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                </tr>
                <tr>
                	<td><s:text name="dn.summary.searcharea.dispute" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.dispute" list="#{true:'YES', false:'NO'}" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                	<td><s:text name="dn.summary.searcharea.buyerStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.buyerStatus" list="buyerStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.priceStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.priceStatus" list="priceStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.qtyStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.qtyStatus" list="qtyStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                </tr>
                <tr>
                 <td>
                	<s:text name="dn.summary.searcharea.priceDisputed" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.priceDispute" list="#{true:'YES', false:'NO'}" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                	<td><s:text name="dn.summary.searcharea.qtyDisputed" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.qtyDispute" list="#{true:'YES', false:'NO'}" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                </tr>
             </tbody>
        </table>
		</s:if>
		<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7">
			<table class="commtable">
            <tbody>
				<tr>
                	<td width="100"><s:text name="dn.summary.searcharea.buyer" /></td>
                	<td width="10">:</td>
                	<td>
                    	<s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.buyerOid" list="buyers" 
                        	listKey="buyerOid" listValue="buyerName" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                    <td><s:text name="dn.summary.searcharea.storeCode" /></td>
                    <td>:</td>
                    <td>
                        <s:textfield name="param.storeCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.storeCode"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearStoreCode" type="button" onclick="clearButton('param.storeCode');"><s:text name='button.clear' /></button>
                    </td>
                </tr>
                <tr>
                	<td width="100"><s:text name="dn.summary.searcharea.supplierName" /></td>
                	<td width="10">:</td>
                	<td>
                    	<s:textfield name="param.supplierName" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.supplierName"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearSupplierName" type="button" onclick="clearButton('param.supplierName');"><s:text name='button.clear' /></button>
                	</td>
                    <td><s:text name="dn.summary.searcharea.supplierCode" /></td>
                    <td>:</td>
                    <td>
                       <s:textfield name="param.supplierCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple" id="param.supplierCode"/>
                       <button  data-dojo-type="dijit.form.Button" id="clearSupplierCode" type="button" onclick="clearButton('param.supplierCode');"><s:text name='button.clear' /></button>
                    </td>
                </tr>
                <tr>
                	<td width="100"><s:text name="dn.summary.searcharea.dnNo" /></td>
                	<td width="10">:</td>
                	<td>
                		<s:textfield name="param.dnNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.dnNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearDnNo" type="button" onclick="clearButton('param.dnNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="dn.summary.searcharea.dnDate" /></td>
                	<td width="10">:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.dnDateFrom" name="param.dnDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.dnDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                	value='<s:date name="param.dnDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearDnDateFrom" type="button" onclick="clearButton('param.dnDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.dnDateTo" name="param.dnDateTo" 
                        onkeydown="javascript:document.getElementById('param.dnDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                        value='<s:date name="param.dnDateTo" format="yyyy-MM-dd" />'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearDnDateTo" type="button" onclick="clearButton('param.dnDateTo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
             	<tr>
             		<td></td>
             		<td></td>
             		<td></td>
             		<td><s:text name="dn.summary.searcharea.sentDate" /></td>
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
             		
             	</tr>
             </tbody>
             </table>
             <table class="commtable">
             <tbody>
             	<tr>
             		<td width="100"><s:text name="dn.summary.searcharea.poNo" /></td>
                	<td>:</td>
                	<td>
                		<s:textfield name="param.poNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.poNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearPoNo" type="button" onclick="clearButton('param.poNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="dn.summary.searcharea.rtvNo" /></td>
                	<td>:</td>
                	<td>
                		<s:textfield name="param.rtvNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.rtvNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearRtvNo" type="button" onclick="clearButton('param.rtvNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="dn.summary.searcharea.invNo" /></td>
                	<td>:</td>
                	<td>
                		<s:textfield name="param.invNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.invNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearInvNo" type="button" onclick="clearButton('param.invNo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
            </tbody>
         </table>
        <table class="commtable">
             <tbody>
                 <tr>
                    <td width="100"><s:text name="dn.summary.searcharea.dnType" /></td>
                    <td width="10">:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.dnType" list="dnTypes" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.dnStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.dnStatus" list="dnStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.readStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.readStatus" list="readStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.closed" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.closed" list="#{true:'YES', false:'NO'}" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                </tr>
                <tr>
                <td>
                	<s:text name="dn.summary.searcharea.dispute" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.dispute" list="#{true:'YES', false:'NO'}" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                	<td><s:text name="dn.summary.searcharea.buyerStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.buyerStatus" list="buyerStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.priceStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.priceStatus" list="priceStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td><s:text name="dn.summary.searcharea.qtyStatus" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.qtyStatus" list="qtyStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                </tr>
                <tr>
                 <td>
                	<s:text name="dn.summary.searcharea.priceDisputed" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.priceDispute" list="#{true:'YES', false:'NO'}" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                	<td><s:text name="dn.summary.searcharea.qtyDisputed" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.priceDispute" list="#{true:'YES', false:'NO'}" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                </tr>
             </tbody>
        </table>
		</s:if>
        </form>
    </div>
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name="dn.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" class="grid"></div>
</body>
</html>
