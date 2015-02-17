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
                "dojox/data/QueryReadStore",
                "dojox/grid/EnhancedGrid",
                "dojox/grid/enhanced/plugins/Pagination",
                "dojox/grid/enhanced/plugins/IndirectSelection",
                "dijit/form/Select",
                "dojo/parser",
                "dojo/_base/xhr",
                "custom/InformationDialog",
                "custom/ConfirmDialog",
                "custom/SelectAllConfirmDialog",
                "dojo/string",
                "dojo/_base/array",
                "dijit/Tooltip",
                "dojo/number",
                "dojo/has",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    domStyle,
                    registry,
                    on,
                    QueryReadStore,
                    EnhancedGrid,
                    Pagination,
                    IndirectSelection,
                    Select,
                    parser,
                    xhr,
                    InformationDialog,
                    ConfirmDialog,
                    SelectAllConfirmDialog,
                    string,
                    array,
                    Tooltip,
                    number,
                    has
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
                    	<s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveClose.action')" >
	         				<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
	         					if(dom.byId('param.pendingForClosing').checked)
	         					{
	         						dom.byId('param.revised').disabled=true;
	         		        		dom.byId('param.buyerStatus').disabled=true;
	         		        		dom.byId('param.supplierStatus').disabled=true;
	         		        	}
	         				</s:if>
         				</s:if>
                    }
                    
                    var store = new QueryReadStore({url: '<s:url value="/poInvGrnDnMatching/data.action" />'});
                    var ie = has('ie');
                    var chrome = has('chrome');
					var structure;
					
					if (chrome!=null)
					{
						structure = [
									<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
									[
										{ name: "<s:text name='pigd.summary.grid.buyerCode' />", field: "buyerCode", rowSpan : 2, styles: "vertical-align:middle;", width: "5%"},
										{ name: "<s:text name='pigd.summary.grid.supplierCode' />", field: "supplierCode", rowSpan : 2, styles: "vertical-align:middle;", width: "5%"},
										{ name: "<s:text name='pigd.summary.grid.store' />", field: "poStoreCode", rowSpan : 2, styles: "vertical-align:middle;", width: "4%"},
										{ name: "<s:text name='pigd.summary.grid.poNo' />", field: "poNo", width: "12%", formatter:
			                                function(field, index, cell){
				                            	if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                            	<s:if test="#session.permitUrl.contains('/po/print.action')" >
				                            		return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
				                            	   + cell.grid.store.getValue(cell.grid.getItem(index), 'poOid') + "&docType=PO&matchingOid="
				                            	   + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + "\" target=\"_blank\">"
				                            	   + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'poDate') + ")</a>";
				                            	</s:if>
			                            	   return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'poDate') + ")";
			                            }},
										{ name: "<s:text name='pigd.summary.grid.invNo' />", field: "invNo", width: "12%", formatter:
			                                function(field, index, cell){
				                            	if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                            	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7" >
													return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")";
				                            	</s:if>
				                            	<s:if test="#session.permitUrl.contains('/inv/print.action')" >
					                            	return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
					                                + cell.grid.store.getValue(cell.grid.getItem(index), 'invOid') + "&docType=INV\" target=\"_blank\">" 
					                                + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")</a>";
				                            	</s:if>
				                            	return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")";
			                            }},
										{ name: "<s:text name='pigd.summary.grid.grnNo' />", field: "grnNo", width: "12%", formatter:
			                                function(field, index, cell){
				                                if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                                var grnOids = cell.grid.store.getValue(cell.grid.getItem(index), 'grnOid').split(",");
				                                var grnNos = field.split(",");
				                                var grnDates = cell.grid.store.getValue(cell.grid.getItem(index), 'grnDate').split(",");
				                                
				                                var result = "";
				                                for (var i=0; i<grnNos.length; i++)
				                                {
				                                	<s:if test="#session.permitUrl.contains('/grn/print.action')" >
				                                    	result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/printPdf.action'/>?docOid="+grnOids[i]+"&docType=GRN\" target=\"_blank\">" + grnNos[i] + "(" +grnDates[i] + ")</a><br/>";
				                                    </s:if>
				                                    <s:else>
				                                    result = result + grnNos[i] + "(" +grnDates[i] + ")<br/>"
				                                    </s:else>
				                                }
				                                return result;
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.matchingStatus' />", field: "matchingStatus", width: "11%", formatter:
			                                function(field, index, cell){
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue');
				                            }
				                        },
				                        
				                        { name: "<s:text name='pigd.summary.grid.buyerStatus' />", field: "buyerStatus", colSpan : 2, styles: "text-align: center;", width: "12%", formatter:
					                        function(field, index, cell){
					                        	if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
							                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| ("OUTDATED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
									                        	&& "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'buyerStatusValue'))
									                    || (("QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
											                    && "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')))
							                    {
													return "";
							                    }
												return cell.grid.store.getValue(cell.grid.getItem(index), 'buyerStatusValue');
					                    }},
			                            { name: "<s:text name='pigd.summary.grid.supplierStatus' />", field: "supplierStatus", rowSpan : 2, styles: "vertical-align:middle;", width: "8%", formatter:
			                                function(field, index, cell){
				                            	if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
				                            			|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised'))
							                    {
													return "";
							                    }
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue');
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.closedStatus' />", field: "closed", rowSpan : 2, styles: "vertical-align:middle;", width: "5%", formatter:
			                            	function(field, index, cell){
			                            	if (field == true)
						                    {
			                            		
			                            		var closedBy = cell.grid.store.getValue(cell.grid.getItem(index), 'closedBy');
			                            		var closedDate = cell.grid.store.getValue(cell.grid.getItem(index), 'closedDate');
			                            		var closedRemarks = cell.grid.store.getValue(cell.grid.getItem(index), 'closedRemarks');
												return "<label onMouseOver=\"loadClosedTooltip(this, '" + closedBy + "', '"+closedDate+"','" + closedRemarks+"')\" onMouseOut=\"closeClosedTooltip(this)\">YES</label>";
						                    }
						                    if (field == false)
						                    {
												return "NO";
						                    }
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.invStatus' />", field: "invStatus", rowSpan : 2, styles: "vertical-align:middle;", width: "11%", formatter:
			                                function(field, index, cell){
			                                var result = "";
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveClose.action')" >
			                                	if (true == cell.grid.store.getValue(cell.grid.getItem(index), 'closed'))
			                                	{
			                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initClose.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +">View Close</a>" + "<br/>";
			                                	}
			                                	else if (false == cell.grid.store.getValue(cell.grid.getItem(index), 'closed')
					                                	&& (("ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
					                                		|| ("REJECTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
							                                	&& "PENDING" != cell.grid.store.getValue(cell.grid.getItem(index), 'buyerStatusValue')))
							                                	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')))
			                                	{
			                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initClose.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +"><s:text name='button.close'/></a>" + "<br/>";
			                                	}
			                                </s:if>
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/initAuditPrice.action')" >
			                                	if ("PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
					                                	|| "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
			                                	{
				                                	if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
								                        	|| "QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "OUTDATED"  == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
									                        || "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
									                        || "PENDING" != cell.grid.store.getValue(cell.grid.getItem(index), 'priceStatus'))
				                                	{
				                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initAuditPrice.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +">View Price Approval</a>" + "<br/>";
				                                	}
				                                	else
				                                	{
				                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initAuditPrice.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +"><s:text name='button.audit.price'/></a>" + "<br/>";
				                                	}
			                                	}
			                                </s:if>
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/initAuditQty.action')" >
				                                if ("QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
					                                	|| "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
			                                	{
					                                if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
								                        	|| "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "OUTDATED"  == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
									                        || "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
									                        || "PENDING" != cell.grid.store.getValue(cell.grid.getItem(index), 'qtyStatus'))
				                                	{
					                                	result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initAuditQty.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +">View Qty Approval</a>" + "<br/>";
				                                	}
					                                else
					                                {
					                                	result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initAuditQty.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +"><s:text name='button.audit.qty'/></a>" + "<br/>";
					                                }
			                                	}
			                                </s:if>
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveAcceptOrReject.action')" >
			                                if ("MATCHED" != cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
						                        	&& "MATCHED BY DN" != cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
						                        	&& "PENDING" != cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
						                        	&& "INSUFFICIENT INV" != cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
						                        	&& !cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
						                        	&& "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue'))
		                                	{
			                                	if (cell.grid.store.getValue(cell.grid.getItem(index), 'acceptFlag'))
			                                	{
			                                		result = result + "<a href='javascript:checkAccept(" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') 
					                                	+", \"" + cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue') 
					                                	+"\", " + cell.grid.store.getValue(cell.grid.getItem(index), 'closed') 
					                                	+", \"" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue') 
					                                	+"\", " + cell.grid.store.getValue(cell.grid.getItem(index), 'revised') 
					                                	+")'><s:text name='button.accept'/></a>" + "<br/>";
			                                	}
				                                result = result + "<a href='javascript:checkReject(" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') 
				                                	+", \"" + cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue') 
				                                	+"\", " + cell.grid.store.getValue(cell.grid.getItem(index), 'closed') 
				                                	+", \"" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue') 
				                                	+"\", " + cell.grid.store.getValue(cell.grid.getItem(index), 'revised') 
				                                	+")'><s:text name='button.reject'/></a>" + "<br/>";
		                                	}
			                                </s:if>
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveAcceptOrReject.action')" >
			                                result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/viewDetails.action'/>?param.matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +">View Details</a>";
			                                </s:if>
			                                return result;
			                            }}
									],
									[
										{ name: "<s:text name='pigd.summary.grid.poAmount' />", field: "poAmt", formatter:
			                                function(field, index, cell){
											<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7" >
			                                    return "";
			                                </s:if>
			                                if(field==null)
			                                {
												return "";
			                                }
			                                var num = number.format(field, {
			                                    pattern: "#,###.00"
			                                  });
			                                return num;
		                            	}},
										{ name: "<s:text name='pigd.summary.grid.invAmount' />", field: "invAmt", formatter:
			                                function(field, index, cell){
											<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7" >
			                                    return "";
			                                </s:if>
			                                if(field==null)
			                                {
												return "";
			                                }
			                                var num = number.format(field, {
			                                    pattern: "#,###.00"
			                                  });
			                                return num;
		                            	}},
										{ name: "<s:text name='pigd.summary.grid.grnAmount' />", field: "", formatter:
			                                function(field, index, cell){
											<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7" >
			                                    return "";
			                                </s:if>
			                                return field;
		                            	}},
										{ name: "<s:text name='pigd.summary.grid.matchingDate' />", field: "matchingDate" },
										{ name: "<s:text name='pigd.summary.grid.priceStatus' />", field: "priceStatus", formatter:
			                                function(field, index, cell){
												if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
							                        	|| "QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| ("OUTDATED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
									                        	&& "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'priceStatusValue'))
									                    || (("QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
											                    && "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')))
							                    {
													return "";
							                    }
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'priceStatusValue');
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.qtyStatus' />", field: "qtyStatus", formatter:
			                                function(field, index, cell){
				                            	if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
							                        	|| "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| ("OUTDATED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
									                        	&& "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'qtyStatusValue'))
									                    || (("QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
											                    && "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')))
							                    {
													return "";
							                    }
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'qtyStatusValue');
			                            }}
									]
									</s:if>
									<s:else>
									[
			                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 3 || #session.SESSION_CURRENT_USER_PROFILE.userType == 5" >
				                            { name: "<s:text name='pigd.summary.grid.buyerCode' />", field: "buyerCode", rowSpan : 2, styles: "vertical-align:middle;", width: "6%"},
				                            { name: "<s:text name='pigd.summary.grid.buyerName' />", field: "buyerName", rowSpan : 2, styles: "vertical-align:middle;", width: "8%"},
			                            </s:if>
			                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 2 || #session.SESSION_CURRENT_USER_PROFILE.userType == 4" >
				                            { name: "<s:text name='pigd.summary.grid.supplierCode' />", field: "supplierCode", rowSpan : 2, styles: "vertical-align:middle;", width: "6%"},
				                            { name: "<s:text name='pigd.summary.grid.supplierName' />", field: "supplierName", rowSpan : 2, styles: "vertical-align:middle;", width: "8%"},
			                            </s:if>
			                            { name: "<s:text name='pigd.summary.grid.store' />", field: "poStoreCode", rowSpan : 2, styles: "vertical-align:middle;", width: "6%"},
			                            { name: "<s:text name='pigd.summary.grid.poNo' />", field: "poNo", width: "14%", formatter:
			                                function(field, index, cell){
				                            	if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                            	<s:if test="#session.permitUrl.contains('/po/print.action')" >
					                            	return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
					                            	   + cell.grid.store.getValue(cell.grid.getItem(index), 'poOid') + "&docType=PO&matchingOid="
					                            	   + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + "\" target=\"_blank\">"
					                            	   + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'poDate') + ")</a>";
				                            	</s:if>
			                            	   return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'poDate') + ")";
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.invNo' />", field: "invNo", width: "14%", formatter:
			                                function(field, index, cell){
				                            	if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                            	<s:if test="#session.permitUrl.contains('/inv/print.action')" >
					                            	return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
						                                + cell.grid.store.getValue(cell.grid.getItem(index), 'invOid') + "&docType=INV\" target=\"_blank\">" 
						                                + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")</a>";
				                            	</s:if>
				                                return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")";
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.grnNo' />", field: "grnNo", width: "14%", formatter:
			                                function(field, index, cell){
			                                if(field == null || field.trim=="")
			                                {
			                                    return "";
			                                }
			                                var grnOids = cell.grid.store.getValue(cell.grid.getItem(index), 'grnOid').split(",");
			                                var grnNos = field.split(",");
			                                var grnDates = cell.grid.store.getValue(cell.grid.getItem(index), 'grnDate').split(",");
			                                var result = "";
			                                for (var i=0; i<grnNos.length; i++)
			                                {
			                                	<s:if test="#session.permitUrl.contains('/grn/print.action')" >
			                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/printPdf.action'/>?docOid="+grnOids[i]+"&docType=GRN\" target=\"_blank\">" + grnNos[i] + "(" +grnDates[i] + ")</a><br/>";
			                                	</s:if>
			                                	<s:else>
			                                		result = result +  grnNos[i] + "(" +grnDates[i] + ")<br/>";
			                                	</s:else>
			                                }
			                                return result;
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.dnNo' />", field: "dnNo", width: "14%", formatter:
			                                function(field, index, cell){
			                                    if(field == null || field.trim=="")
			                                    {
			                                        return "";
			                                    }
			                                    <s:if test="#session.permitUrl.contains('/dn/print.action')" >
				                                    return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
					                                + cell.grid.store.getValue(cell.grid.getItem(index), 'dnOid') + "&docType=DN\" target=\"_blank\">"
					                                + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'dnDate') + ")</a>";
			                                    </s:if>
				                                return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'dnDate') + ")";
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.matchingStatus' />", field: "matchingStatus", rowSpan : 2, styles: "vertical-align:middle;", width: "10%", formatter:
			                                function(field, index, cell){
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue');
				                            }
				                        },
				                        { name: "<s:text name='pigd.summary.grid.closedStatus' />", field: "closed", rowSpan : 2, styles: "vertical-align:middle;", width: "6%", formatter:
                                            function(field, index, cell){
                                            if (field == true)
                                            {
                                                var closedBy = cell.grid.store.getValue(cell.grid.getItem(index), 'closedBy');
                                                var closedDate = cell.grid.store.getValue(cell.grid.getItem(index), 'closedDate');
                                                var closedRemarks = cell.grid.store.getValue(cell.grid.getItem(index), 'closedRemarks');
                                                return "<label onMouseOver=\"loadClosedTooltip(this, '" + closedBy + "', '"+closedDate+"','" + closedRemarks+"')\" onMouseOut=\"closeClosedTooltip(this)\">YES</label>";
                                            }
                                            if (field == false)
                                            {
                                                return "NO";
                                            }
                                        }},
			                            { name: "<s:text name='pigd.summary.grid.actionStatus' />", field: "invStatus", rowSpan : 2, styles: "vertical-align:middle;", width: "10%", formatter:
			                                function(field, index, cell){
			                                   var result = "";
			                                   if (true == cell.grid.store.getValue(cell.grid.getItem(index), 'closed')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "OUTDATED"  == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
			                                   {
					                               result = "";
			                                   }
			                                   else
			                                   {
			                                	   <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveClose.action')" >
					                                   result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initClose.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +"><s:text name='button.close'/></a>" + "<br/>";
					                               </s:if>
			                                   }
			                                
			                            	   var matchingOid = cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid');
			                            	   var invNo = cell.grid.store.getValue(cell.grid.getItem(index), 'invNo');
			                            	   var actionStatus = cell.grid.store.getValue(cell.grid.getItem(index), 'invStatusValue');
			                            	   var actionRemarks = cell.grid.store.getValue(cell.grid.getItem(index), 'invStatusActionRemarks');
			                            	
			                                    if(field == "PENDING")
			                                    {
			                                        var pendingFlag = true;
			                                        if(invNo == null 
			                                        	|| string.trim(invNo)=="")
			                                        {
			                                        	pendingFlag = false;
			                                        }
			                                        result = result + "<span id='actionStatus_"+ index +"'><a href=\"javascript:approve(" + 
				                                        matchingOid + ", " +
				                                        pendingFlag+","+index+");\">Approve</a></span>";
			                                    }
			                                    else
			                                    {
			                                        result = result + actionStatus;
			                                    }

			                                    return result;
			                                }}
			                            ],
			                            [
			                            { name: "<s:text name='pigd.summary.grid.poAmount' />", field: "poAmt" },
			                            { name: "<s:text name='pigd.summary.grid.invAmount' />", field: "invAmt" },
			                            { name: "<s:text name='pigd.summary.grid.grnAmount' />", field: "grnAmt" },
			                            { name: "<s:text name='pigd.summary.grid.dnAmount' />", field: "dnAmt" }
			                            ]
									</s:else>
									]	
					}

					else if (ie==null)
					{
						structure = [
									<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
									[
										{ name: "<s:text name='pigd.summary.grid.buyerCode' />", field: "buyerCode", rowSpan : 2, styles: "vertical-align:middle;", width: "5%"},
										{ name: "<s:text name='pigd.summary.grid.supplierCode' />", field: "supplierCode", rowSpan : 2, styles: "vertical-align:middle;", width: "5%"},
										{ name: "<s:text name='pigd.summary.grid.store' />", field: "poStoreCode", rowSpan : 2, styles: "vertical-align:middle;", width: "3%"},
										{ name: "<s:text name='pigd.summary.grid.poNo' />", field: "poNo", width: "9%", formatter:
											function(field, index, cell){
				                            	if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                            	<s:if test="#session.permitUrl.contains('/po/print.action')" >
				                            		return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
				                            	   + cell.grid.store.getValue(cell.grid.getItem(index), 'poOid') + "&docType=PO&matchingOid="
				                            	   + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + "\" target=\"_blank\">"
				                            	   + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'poDate') + ")</a>";
				                            	</s:if>
			                            	   return field+ "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'poDate') + ")";
			                            }},
										{ name: "<s:text name='pigd.summary.grid.invNo' />", field: "invNo", width: "10%", formatter:
											function(field, index, cell){
				                            	if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                            	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7" >
													return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")";
				                            	</s:if>
				                            	<s:if test="#session.permitUrl.contains('/inv/print.action')" >
					                            	return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
					                                + cell.grid.store.getValue(cell.grid.getItem(index), 'invOid') + "&docType=INV\" target=\"_blank\">" 
					                                + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")</a>";
				                            	</s:if>
				                                return field+ "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")";
			                            }},
										{ name: "<s:text name='pigd.summary.grid.grnNo' />", field: "grnNo", width: "9%", formatter:
											function(field, index, cell){
				                                if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                                var grnOids = cell.grid.store.getValue(cell.grid.getItem(index), 'grnOid').split(",");
				                                var grnNos = field.split(",");
				                                var grnDates = cell.grid.store.getValue(cell.grid.getItem(index), 'grnDate').split(",");
				                                var result = "";
				                                for (var i=0; i<grnNos.length; i++)
				                                {
				                                	<s:if test="#session.permitUrl.contains('/grn/print.action')" >
				                                    	result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/printPdf.action'/>?docOid="+grnOids[i]+"&docType=GRN\" target=\"_blank\">" + grnNos[i] + "(" +grnDates[i] + ")</a><br/>";
				                                    </s:if>
				                                    <s:else>
				                                    result = result + grnNos[i] + "(" +grnDates[i] + ")<br/>"
				                                    </s:else>
				                                }
				                                return result;
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.matchingStatus' />", field: "matchingStatus", width: "10%", formatter:
			                                function(field, index, cell){
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue');
				                            }
				                        },
				                        
				                        { name: "<s:text name='pigd.summary.grid.buyerStatus' />", field: "buyerStatus", colSpan : 2, styles: "text-align: center;", width: "12%", formatter:
					                        function(field, index, cell){
					                        	if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
					                        			|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
							                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| ("OUTDATED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
									                        	&& "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'buyerStatusValue'))
									                    || (("QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
											                    && "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')))
							                    {
													return "";
							                    }
												return cell.grid.store.getValue(cell.grid.getItem(index), 'buyerStatusValue');
					                    }},
			                            { name: "<s:text name='pigd.summary.grid.supplierStatus' />", field: "supplierStatus", rowSpan : 2, styles: "vertical-align:middle;", width: "8%", formatter:
			                                function(field, index, cell){
				                            	if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
				                            			|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised'))
							                    {
													return "";
							                    }
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue');
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.closedStatus' />", field: "closed", rowSpan : 2, styles: "vertical-align:middle;", width: "4%", formatter:
			                            	function(field, index, cell){
			                            	if (field == true)
						                    {
			                            		var closedBy = cell.grid.store.getValue(cell.grid.getItem(index), 'closedBy');
                                                var closedDate = cell.grid.store.getValue(cell.grid.getItem(index), 'closedDate');
                                                var closedRemarks = cell.grid.store.getValue(cell.grid.getItem(index), 'closedRemarks');
                                                return "<label onMouseOver=\"loadClosedTooltip(this, '" + closedBy + "', '"+closedDate+"','" + closedRemarks+"')\" onMouseOut=\"closeClosedTooltip(this)\">YES</label>";
						                    }
						                    if (field == false)
						                    {
												return "NO";
						                    }
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.invStatus' />", field: "invStatus", rowSpan : 2, styles: "vertical-align:middle;", width: "10%", formatter:
			                                function(field, index, cell){
			                                var result = "";
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveClose.action')" >
				                                if (true == cell.grid.store.getValue(cell.grid.getItem(index), 'closed'))
			                                	{
			                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initClose.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +">View Close</a>" + "<br/>";
			                                	}
				                                else if (false == cell.grid.store.getValue(cell.grid.getItem(index), 'closed')
					                                	&& (("ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
					                                		|| ("REJECTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
							                                	&& "PENDING" != cell.grid.store.getValue(cell.grid.getItem(index), 'buyerStatusValue')))
							                                	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')))
			                                	{
			                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initClose.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +"><s:text name='button.close'/></a>" + "<br/>";
			                                	}
			                                </s:if>
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/initAuditPrice.action')" >
				                                if ("PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
					                                	|| "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
			                                	{
			                                		if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
								                        	|| "QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "OUTDATED"  == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
									                        || "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
									                        || "PENDING" != cell.grid.store.getValue(cell.grid.getItem(index), 'priceStatus'))
				                                	{
				                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initAuditPrice.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +">View Price Approval</a>" + "<br/>";
				                                	}
				                                	else
				                                	{
				                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initAuditPrice.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +"><s:text name='button.audit.price'/></a>" + "<br/>";
				                                	}
			                                	}
			                                </s:if>
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/initAuditQty.action')" >
				                                if ("QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
					                                	|| "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
			                                	{
				                                	if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
								                        	|| "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "OUTDATED"  == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
									                        || "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
									                        || "PENDING" != cell.grid.store.getValue(cell.grid.getItem(index), 'qtyStatus'))
				                                	{
					                                	result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initAuditQty.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +">View Qty Approval</a>" + "<br/>";
				                                	}
					                                else
					                                {
					                                	result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initAuditQty.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +"><s:text name='button.audit.qty'/></a>" + "<br/>";
					                                }
			                                	}
			                                </s:if>
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveAcceptOrReject.action')" >
			                                if ("MATCHED" != cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
						                        	&& "MATCHED BY DN" != cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
						                        	&& "PENDING" != cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
						                        	&& "INSUFFICIENT INV" != cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
						                        	&& !cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
						                        	&& "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue'))
		                                	{
			                                	if (cell.grid.store.getValue(cell.grid.getItem(index), 'acceptFlag'))
			                                	{
			                                		result = result + "<a href='javascript:checkAccept(" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') 
					                                	+", \"" + cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue') 
					                                	+"\", " + cell.grid.store.getValue(cell.grid.getItem(index), 'closed') 
					                                	+", \"" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue') 
					                                	+"\", " + cell.grid.store.getValue(cell.grid.getItem(index), 'revised') 
					                                	+")'><s:text name='button.accept'/></a>" + "<br/>";
			                                	}
				                                result = result + "<a href='javascript:checkReject(" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') 
				                                	+", \"" + cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue') 
				                                	+"\", " + cell.grid.store.getValue(cell.grid.getItem(index), 'closed') 
				                                	+", \"" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue') 
				                                	+"\", " + cell.grid.store.getValue(cell.grid.getItem(index), 'revised') 
				                                	+")'><s:text name='button.reject'/></a>" + "<br/>";
		                                	}
			                                </s:if>
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveAcceptOrReject.action')" >
			                                result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/viewDetails.action'/>?param.matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +">View Details</a>";
			                                </s:if>
			                                return result;
			                            }}
									],
									[
										{ name: "<s:text name='pigd.summary.grid.poAmount' />", field: "poAmt", formatter:
			                                function(field, index, cell){
											<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7" >
			                                    return "";
			                                </s:if>
			                                if(field==null)
			                                {
												return "";
			                                }
			                                var num = number.format(field, {
			                                    pattern: "#,###.00"
			                                  });
			                                return num;
		                            	}},
										{ name: "<s:text name='pigd.summary.grid.invAmount' />", field: "invAmt", formatter:
			                                function(field, index, cell){
											<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7" >
			                                    return "";
			                                </s:if>
			                                if(field==null)
			                                {
												return "";
			                                }
			                                var num = number.format(field, {
			                                    pattern: "#,###.00"
			                                  });
			                                return num;
		                            	}},
										{ name: "<s:text name='pigd.summary.grid.grnAmount' />", field: "", formatter:
			                                function(field, index, cell){
											<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7" >
			                                    return "";
			                                </s:if>
			                                return field;
		                            	}},
										{ name: "<s:text name='pigd.summary.grid.matchingDate' />", field: "matchingDate" },
										{ name: "<s:text name='pigd.summary.grid.priceStatus' />", field: "priceStatus", formatter:
			                                function(field, index, cell){
												if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
														|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
							                        	|| "QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| ("OUTDATED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
									                        	&& "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'priceStatusValue'))
									                    || (("QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
											                    && "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')))
							                    {
													return "";
							                    }
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'priceStatusValue');
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.qtyStatus' />", field: "qtyStatus", formatter:
			                                function(field, index, cell){
				                            	if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
				                            			|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
							                        	|| "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| ("OUTDATED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
									                        	&& "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'qtyStatusValue'))
									                    || (("QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
											                    && "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')))
							                    {
													return "";
							                    }
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'qtyStatusValue');
			                            }}
									]
									</s:if>
									<s:else>
									[
			                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 3 || #session.SESSION_CURRENT_USER_PROFILE.userType == 5" >
				                            { name: "<s:text name='pigd.summary.grid.buyerCode' />", field: "buyerCode", rowSpan : 2, styles: "vertical-align:middle;", width: "6%"},
				                            { name: "<s:text name='pigd.summary.grid.buyerName' />", field: "buyerName", rowSpan : 2, styles: "vertical-align:middle;", width: "8%"},
			                            </s:if>
			                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 2 || #session.SESSION_CURRENT_USER_PROFILE.userType == 4" >
				                            { name: "<s:text name='pigd.summary.grid.supplierCode' />", field: "supplierCode", rowSpan : 2, styles: "vertical-align:middle;", width: "6%"},
				                            { name: "<s:text name='pigd.summary.grid.supplierName' />", field: "supplierName", rowSpan : 2, styles: "vertical-align:middle;", width: "8%"},
			                            </s:if>
			                            { name: "<s:text name='pigd.summary.grid.store' />", field: "poStoreCode", rowSpan : 2, styles: "vertical-align:middle;", width: "6%"},
			                            { name: "<s:text name='pigd.summary.grid.poNo' />", field: "poNo", width: "14%", formatter:
			                                function(field, index, cell){
				                            	if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                            	<s:if test="#session.permitUrl.contains('/po/print.action')" >
				                            	return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
				                            	   + cell.grid.store.getValue(cell.grid.getItem(index), 'poOid') + "&docType=PO&matchingOid="
				                            	   + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + "\" target=\"_blank\">"
				                            	   + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'poDate') + ")</a>";
				                            	</s:if>
			                            	   return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'poDate') + ")";
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.invNo' />", field: "invNo", width: "14%", formatter:
			                                function(field, index, cell){
				                            	if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                            	<s:if test="#session.permitUrl.contains('/inv/print.action')" >
				                            	return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
					                                + cell.grid.store.getValue(cell.grid.getItem(index), 'invOid') + "&docType=INV\" target=\"_blank\">" 
					                                + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")</a>";
				                            	</s:if>
				                                return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")";
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.grnNo' />", field: "grnNo", width: "14%", formatter:
			                                function(field, index, cell){
			                                if(field == null || field.trim=="")
			                                {
			                                    return "";
			                                }
			                                var grnOids = cell.grid.store.getValue(cell.grid.getItem(index), 'grnOid').split(",");
			                                var grnNos = field.split(",");
			                                var grnDates = cell.grid.store.getValue(cell.grid.getItem(index), 'grnDate').split(",");
			                                var result = "";
			                                for (var i=0; i<grnNos.length; i++)
			                                {
			                                	<s:if test="#session.permitUrl.contains('/grn/print.action')" >
			                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/printPdf.action'/>?docOid="+grnOids[i]+"&docType=GRN\" target=\"_blank\">" + grnNos[i] + "(" +grnDates[i] + ")</a><br/>";
			                                	</s:if>
			                                	<s:else>
			                                		result = result +  grnNos[i] + "(" +grnDates[i] + ")<br/>";
			                                	</s:else>
			                                }
			                                return result;
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.dnNo' />", field: "dnNo", width: "14%", formatter:
			                                function(field, index, cell){
			                                    if(field == null || field.trim=="")
			                                    {
			                                        return "";
			                                    }
			                                    <s:if test="#session.permitUrl.contains('/dn/print.action')" >
				                                    return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
					                                + cell.grid.store.getValue(cell.grid.getItem(index), 'dnOid') + "&docType=DN\" target=\"_blank\">"
					                                + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'dnDate') + ")</a>";
			                                    </s:if>
			                                return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'dnDate') + ")";
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.matchingStatus' />", field: "matchingStatus", rowSpan : 2, styles: "vertical-align:middle;", width: "10%", formatter:
			                                function(field, index, cell){
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue');
				                            }
				                        },
				                        { name: "<s:text name='pigd.summary.grid.closedStatus' />", field: "closed", rowSpan : 2, styles: "vertical-align:middle;", width: "4%", formatter:
                                            function(field, index, cell){
                                            if (field == true)
                                            {
                                                var closedBy = cell.grid.store.getValue(cell.grid.getItem(index), 'closedBy');
                                                var closedDate = cell.grid.store.getValue(cell.grid.getItem(index), 'closedDate');
                                                var closedRemarks = cell.grid.store.getValue(cell.grid.getItem(index), 'closedRemarks');
                                                return "<label onMouseOver=\"loadClosedTooltip(this, '" + closedBy + "', '"+closedDate+"','" + closedRemarks+"')\" onMouseOut=\"closeClosedTooltip(this)\">YES</label>";
                                            }
                                            if (field == false)
                                            {
                                                return "NO";
                                            }
                                        }},
			                            { name: "<s:text name='pigd.summary.grid.actionStatus' />", field: "invStatus", rowSpan : 2, styles: "vertical-align:middle;", width: "10%", formatter:
			                                function(field, index, cell){
			                            	   var result = "";
			                                   if (true == cell.grid.store.getValue(cell.grid.getItem(index), 'closed')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "OUTDATED"  == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
			                                   {
					                               result = "";
			                                   }
			                                   else
			                                   {
			                                	   <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveClose.action')" >
					                                   result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initClose.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +"><s:text name='button.close'/></a>" + "<br/>";
					                               </s:if>
			                                   }
			                                
			                            	   var matchingOid = cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid');
			                            	   var invNo = cell.grid.store.getValue(cell.grid.getItem(index), 'invNo');
			                            	   var actionStatus = cell.grid.store.getValue(cell.grid.getItem(index), 'invStatusValue');
			                            	   var actionRemarks = cell.grid.store.getValue(cell.grid.getItem(index), 'invStatusActionRemarks');
			                            	
			                                    if(field == "PENDING")
			                                    {
			                                        var pendingFlag = true;
			                                        if(invNo == null 
			                                        	|| string.trim(invNo)=="")
			                                        {
			                                        	pendingFlag = false;
			                                        }
			                                        result = result + "<span id='actionStatus_"+ index +"'><a href=\"javascript:approve(" + 
				                                        matchingOid + ", " +
				                                        pendingFlag+","+index+");\">Approve</a></span>";
			                                    }
			                                    else
			                                    {
			                                        result = result + actionStatus;
			                                    }

			                                    return result;
			                                }}
			                            ],
			                            [
			                            { name: "<s:text name='pigd.summary.grid.poAmount' />", field: "poAmt" },
			                            { name: "<s:text name='pigd.summary.grid.invAmount' />", field: "invAmt" },
			                            { name: "<s:text name='pigd.summary.grid.grnAmount' />", field: "grnAmt" },
			                            { name: "<s:text name='pigd.summary.grid.dnAmount' />", field: "dnAmt" }
			                            ]
									</s:else>
									]	
					}
					else
					{
						structure = [
									<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
									[
										{ name: "<s:text name='pigd.summary.grid.buyerCode' />", field: "buyerCode", rowSpan : 2, styles: "vertical-align:middle;", width: "5%"},
										{ name: "<s:text name='pigd.summary.grid.supplierCode' />", field: "supplierCode", rowSpan : 2, styles: "vertical-align:middle;", width: "5%"},
										{ name: "<s:text name='pigd.summary.grid.store' />", field: "poStoreCode", rowSpan : 2, styles: "vertical-align:middle;", width: "4%"},
										{ name: "<s:text name='pigd.summary.grid.poNo' />", field: "poNo", width: "11%", formatter:
											function(field, index, cell){
				                            	if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                            	<s:if test="#session.permitUrl.contains('/po/print.action')" >
				                            		return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
				                            	   + cell.grid.store.getValue(cell.grid.getItem(index), 'poOid') + "&docType=PO&matchingOid="
				                            	   + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + "\" target=\"_blank\">"
				                            	   + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'poDate') + ")</a>";
				                            	</s:if>
		                            	   return field+ "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'poDate') + ")";
			                            }},
										{ name: "<s:text name='pigd.summary.grid.invNo' />", field: "invNo", width: "12%", formatter:
											function(field, index, cell){
				                            	if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                            	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7" >
													return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")";
				                            	</s:if>
				                            	<s:if test="#session.permitUrl.contains('/inv/print.action')" >
					                            	return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
					                                + cell.grid.store.getValue(cell.grid.getItem(index), 'invOid') + "&docType=INV\" target=\"_blank\">" 
					                                + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")</a>";
				                            	</s:if>
				                                return field+ "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")";
			                            }},
										{ name: "<s:text name='pigd.summary.grid.grnNo' />", field: "grnNo", width: "12%", formatter:
											function(field, index, cell){
				                                if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                                var grnOids = cell.grid.store.getValue(cell.grid.getItem(index), 'grnOid').split(",");
				                                var grnNos = field.split(",");
				                                var grnDates = cell.grid.store.getValue(cell.grid.getItem(index), 'grnDate').split(",");
				                                var result = "";
				                                for (var i=0; i<grnNos.length; i++)
				                                {
				                                	<s:if test="#session.permitUrl.contains('/grn/print.action')" >
				                                    	result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/printPdf.action'/>?docOid="+grnOids[i]+"&docType=GRN\" target=\"_blank\">" + grnNos[i] + "(" +grnDates[i] + ")</a><br/>";
				                                    </s:if>
				                                    <s:else>
				                                    result = result + grnNos[i] + "(" +grnDates[i] + ")<br/>"
				                                    </s:else>
				                                }
				                                return result;
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.matchingStatus' />", field: "matchingStatus", width: "10%", formatter:
			                                function(field, index, cell){
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue');
				                            }
				                        },
				                        
				                        { name: "<s:text name='pigd.summary.grid.buyerStatus' />", field: "buyerStatus", colSpan : 2, styles: "text-align: center;", width: "12%", formatter:
					                        function(field, index, cell){
					                        	if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
					                        			|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
							                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| ("OUTDATED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
									                        	&& "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'buyerStatusValue'))
									                    || (("QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
											                    && "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')))
							                    {
													return "";
							                    }
												return cell.grid.store.getValue(cell.grid.getItem(index), 'buyerStatusValue');
					                    }},
			                            { name: "<s:text name='pigd.summary.grid.supplierStatus' />", field: "supplierStatus", rowSpan : 2, styles: "vertical-align:middle;", width: "9%", formatter:
			                                function(field, index, cell){
				                            	if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
				                            			|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised'))
							                    {
													return "";
							                    }
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue');
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.closedStatus' />", field: "closed", rowSpan : 2, styles: "vertical-align:middle;", width: "5%", formatter:
			                            	function(field, index, cell){
			                            	if (field == true)
						                    {
			                            		var closedBy = cell.grid.store.getValue(cell.grid.getItem(index), 'closedBy');
                                                var closedDate = cell.grid.store.getValue(cell.grid.getItem(index), 'closedDate');
                                                var closedRemarks = cell.grid.store.getValue(cell.grid.getItem(index), 'closedRemarks');
                                                return "<label onMouseOver=\"loadClosedTooltip(this, '" + closedBy + "', '"+closedDate+"','" + closedRemarks+"')\" onMouseOut=\"closeClosedTooltip(this)\">YES</label>";
						                    }
						                    if (field == false)
						                    {
												return "NO";
						                    }
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.invStatus' />", field: "invStatus", rowSpan : 2, styles: "vertical-align:middle;", width: "11%", formatter:
			                                function(field, index, cell){
			                            	var result = "";
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveClose.action')" >
				                                if (true == cell.grid.store.getValue(cell.grid.getItem(index), 'closed'))
			                                	{
			                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initClose.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +">View Close</a>" + "<br/>";
			                                	}
				                                else if (false == cell.grid.store.getValue(cell.grid.getItem(index), 'closed')
					                                	&& (("ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
					                                		|| ("REJECTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
							                                	&& "PENDING" != cell.grid.store.getValue(cell.grid.getItem(index), 'buyerStatusValue')))
							                                	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')))
			                                	{
			                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initClose.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +"><s:text name='button.close'/></a>" + "<br/>";
			                                	}
			                                </s:if>
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/initAuditPrice.action')" >
				                                if ("PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
					                                	|| "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
			                                	{
			                                		if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
								                        	|| "QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "OUTDATED"  == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
									                        || "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
									                        || "PENDING" != cell.grid.store.getValue(cell.grid.getItem(index), 'priceStatus'))
				                                	{
				                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initAuditPrice.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +">View Price Approval</a>" + "<br/>";
				                                	}
				                                	else
				                                	{
				                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initAuditPrice.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +"><s:text name='button.audit.price'/></a>" + "<br/>";
				                                	}
			                                	}
			                                </s:if>
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/initAuditQty.action')" >
				                                if ("QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
					                                	|| "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
			                                	{
				                                	if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
								                        	|| "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "OUTDATED"  == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
								                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
									                        || "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
									                        || "PENDING" != cell.grid.store.getValue(cell.grid.getItem(index), 'qtyStatus'))
				                                	{
					                                	result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initAuditQty.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +">View Qty Approval</a>" + "<br/>";
				                                	}
					                                else
					                                {
					                                	result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initAuditQty.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +"><s:text name='button.audit.qty'/></a>" + "<br/>";
					                                }
			                                	}
			                                </s:if>
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveAcceptOrReject.action')" >
			                                if ("MATCHED" != cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
						                        	&& "MATCHED BY DN" != cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
						                        	&& "PENDING" != cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
						                        	&& "INSUFFICIENT INV" != cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
						                        	&& !cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
						                        	&& "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue'))
		                                	{
			                                	if (cell.grid.store.getValue(cell.grid.getItem(index), 'acceptFlag'))
			                                	{
			                                		result = result + "<a href='javascript:checkAccept(" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') 
					                                	+", \"" + cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue') 
					                                	+"\", " + cell.grid.store.getValue(cell.grid.getItem(index), 'closed') 
					                                	+", \"" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue') 
					                                	+"\", " + cell.grid.store.getValue(cell.grid.getItem(index), 'revised') 
					                                	+")'><s:text name='button.accept'/></a>" + "<br/>";
			                                	}
				                                result = result + "<a href='javascript:checkReject(" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') 
				                                	+", \"" + cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue') 
				                                	+"\", " + cell.grid.store.getValue(cell.grid.getItem(index), 'closed') 
				                                	+", \"" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue') 
				                                	+"\", " + cell.grid.store.getValue(cell.grid.getItem(index), 'revised') 
				                                	+")'><s:text name='button.reject'/></a>" + "<br/>";
		                                	}
			                                </s:if>
			                                <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveAcceptOrReject.action')" >
			                                result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/viewDetails.action'/>?param.matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +">View Details</a>";
			                                </s:if>	
			                                return result;
			                            }}
									],
									[
										{ name: "<s:text name='pigd.summary.grid.poAmount' />", field: "poAmt", formatter:
			                                function(field, index, cell){
											<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7" >
			                                    return "";
			                                </s:if>
			                                if(field==null)
			                                {
												return "";
			                                }
			                                var num = number.format(field, {
			                                    pattern: "#,###.00"
			                                  });
			                                return num;
		                            	}},
										{ name: "<s:text name='pigd.summary.grid.invAmount' />", field: "invAmt", formatter:
			                                function(field, index, cell){
											<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7" >
			                                    return "";
			                                </s:if>
			                                if(field==null)
			                                {
												return "";
			                                }
			                                var num = number.format(field, {
			                                    pattern: "#,###.00"
			                                  });
			                                return num;
		                            	}},
										{ name: "<s:text name='pigd.summary.grid.grnAmount' />", field: "", formatter:
			                                function(field, index, cell){
											<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7" >
			                                    return "";
			                                </s:if>
			                                return field;
		                            	}},
										{ name: "<s:text name='pigd.summary.grid.matchingDate' />", field: "matchingDate" },
										{ name: "<s:text name='pigd.summary.grid.priceStatus' />", field: "priceStatus", formatter:
			                                function(field, index, cell){
												if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
														|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
							                        	|| "QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| ("OUTDATED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
									                        	&& "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'priceStatusValue'))
									                    || (("QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
											                    && "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')))
							                    {
													return "";
							                    }
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'priceStatusValue');
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.qtyStatus' />", field: "qtyStatus", formatter:
			                                function(field, index, cell){
				                            	if ("MATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
				                            			|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "MATCHED BY DN" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "ACCEPTED" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')
							                        	|| "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| true == cell.grid.store.getValue(cell.grid.getItem(index), 'revised')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| ("OUTDATED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
									                        	&& "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'qtyStatusValue'))
									                    || (("QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
											                    || "PRICE&QTY UNMATCHED" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
											                    && "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'supplierStatusValue')))
							                    {
													return "";
							                    }
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'qtyStatusValue');
			                            }}
									]
									</s:if>
									<s:else>
									[
			                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 3 || #session.SESSION_CURRENT_USER_PROFILE.userType == 5" >
				                            { name: "<s:text name='pigd.summary.grid.buyerCode' />", field: "buyerCode", rowSpan : 2, styles: "vertical-align:middle;", width: "6%"},
				                            { name: "<s:text name='pigd.summary.grid.buyerName' />", field: "buyerName", rowSpan : 2, styles: "vertical-align:middle;", width: "8%"},
			                            </s:if>
			                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 2 || #session.SESSION_CURRENT_USER_PROFILE.userType == 4" >
				                            { name: "<s:text name='pigd.summary.grid.supplierCode' />", field: "supplierCode", rowSpan : 2, styles: "vertical-align:middle;", width: "6%"},
				                            { name: "<s:text name='pigd.summary.grid.supplierName' />", field: "supplierName", rowSpan : 2, styles: "vertical-align:middle;", width: "8%"},
			                            </s:if>
			                            { name: "<s:text name='pigd.summary.grid.store' />", field: "poStoreCode", rowSpan : 2, styles: "vertical-align:middle;", width: "5%"},
			                            { name: "<s:text name='pigd.summary.grid.poNo' />", field: "poNo", width: "14%", formatter:
			                                function(field, index, cell){
				                            	if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                            	<s:if test="#session.permitUrl.contains('/po/print.action')" >
				                            	return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
				                            	   + cell.grid.store.getValue(cell.grid.getItem(index), 'poOid') + "&docType=PO&matchingOid="
				                            	   + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + "\" target=\"_blank\">"
				                            	   + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'poDate') + ")</a>";
				                            	</s:if>
			                            	   return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'poDate') + ")";
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.invNo' />", field: "invNo", width: "14%", formatter:
			                                function(field, index, cell){
				                            	if(field == null || field.trim=="")
				                                {
				                                    return "";
				                                }
				                            	<s:if test="#session.permitUrl.contains('/inv/print.action')" >
				                            	return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
					                                + cell.grid.store.getValue(cell.grid.getItem(index), 'invOid') + "&docType=INV\" target=\"_blank\">" 
					                                + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")</a>";
				                            	</s:if>
				                                return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'invDate') + ")";
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.grnNo' />", field: "grnNo", width: "14%", formatter:
			                                function(field, index, cell){
			                                if(field == null || field.trim=="")
			                                {
			                                    return "";
			                                }
			                                var grnOids = cell.grid.store.getValue(cell.grid.getItem(index), 'grnOid').split(",");
			                                var grnNos = field.split(",");
			                                var grnDates = cell.grid.store.getValue(cell.grid.getItem(index), 'grnDate').split(",");
			                                var result = "";
			                                for (var i=0; i<grnNos.length; i++)
			                                {
			                                	<s:if test="#session.permitUrl.contains('/grn/print.action')" >
			                                		result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/printPdf.action'/>?docOid="+grnOids[i]+"&docType=GRN\" target=\"_blank\">" + grnNos[i] + "(" +grnDates[i] + ")</a><br/>";
			                                	</s:if>
			                                	<s:else>
			                                		result = result +  grnNos[i] + "(" +grnDates[i] + ")<br/>";
			                                	</s:else>
			                                }
			                                return result;
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.dnNo' />", field: "dnNo", width: "14%", formatter:
			                                function(field, index, cell){
			                                    if(field == null || field.trim=="")
			                                    {
			                                        return "";
			                                    }
			                                    <s:if test="#session.permitUrl.contains('/dn/print.action')" >
				                                    return "<a href=\"<s:url value="/poInvGrnDnMatching/printPdf.action"/>?docOid=" 
					                                + cell.grid.store.getValue(cell.grid.getItem(index), 'dnOid') + "&docType=DN\" target=\"_blank\">"
					                                + field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'dnDate') + ")</a>";
			                                    </s:if>
				                                return field + "(" + cell.grid.store.getValue(cell.grid.getItem(index), 'dnDate') + ")";
			                            }},
			                            { name: "<s:text name='pigd.summary.grid.matchingStatus' />", field: "matchingStatus", rowSpan : 2, styles: "vertical-align:middle;", width: "10%", formatter:
			                                function(field, index, cell){
				                                return cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue');
				                            }
				                        },
				                        { name: "<s:text name='pigd.summary.grid.closedStatus' />", field: "closed", rowSpan : 2, styles: "vertical-align:middle;", width: "5%", formatter:
                                            function(field, index, cell){
                                            if (field == true)
                                            {
                                                var closedBy = cell.grid.store.getValue(cell.grid.getItem(index), 'closedBy');
                                                var closedDate = cell.grid.store.getValue(cell.grid.getItem(index), 'closedDate');
                                                var closedRemarks = cell.grid.store.getValue(cell.grid.getItem(index), 'closedRemarks');
                                                return "<label onMouseOver=\"loadClosedTooltip(this, '" + closedBy + "', '"+closedDate+"','" + closedRemarks+"')\" onMouseOut=\"closeClosedTooltip(this)\">YES</label>";
                                            }
                                            if (field == false)
                                            {
                                                return "NO";
                                            }
                                        }},
			                            { name: "<s:text name='pigd.summary.grid.actionStatus' />", field: "invStatus", rowSpan : 2, styles: "vertical-align:middle;", width: "10%", formatter:
			                                function(field, index, cell){
			                            	   var result = "";
			                                   if (true == cell.grid.store.getValue(cell.grid.getItem(index), 'closed')
							                        	|| "INSUFFICIENT INV" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "OUTDATED"  == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue')
							                        	|| "PENDING" == cell.grid.store.getValue(cell.grid.getItem(index), 'matchingStatusValue'))
			                                   {
					                               result = "";
			                                   }
			                                   else
			                                   {
			                                	   <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveClose.action')" >
					                                   result = result + "<a href=\"<s:url value='/poInvGrnDnMatching/initClose.action'/>?matchingOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid') + '"' +"><s:text name='button.close'/></a>" + "<br/>";
					                               </s:if>
			                                   }
			                                
			                            	   var matchingOid = cell.grid.store.getValue(cell.grid.getItem(index), 'matchingOid');
			                            	   var invNo = cell.grid.store.getValue(cell.grid.getItem(index), 'invNo');
			                            	   var actionStatus = cell.grid.store.getValue(cell.grid.getItem(index), 'invStatusValue');
			                            	   var actionRemarks = cell.grid.store.getValue(cell.grid.getItem(index), 'invStatusActionRemarks');
			                            	
			                                    if(field == "PENDING")
			                                    {
			                                        var pendingFlag = true;
			                                        if(invNo == null 
			                                        	|| string.trim(invNo)=="")
			                                        {
			                                        	pendingFlag = false;
			                                        }
			                                        result = result + "<span id='actionStatus_"+ index +"'><a href=\"javascript:approve(" + 
				                                        matchingOid + ", " +
				                                        pendingFlag+","+index+");\">Approve</a></span>";
			                                    }
			                                    else
			                                    {
			                                        result = result + actionStatus;
			                                    }

			                                    return result;
			                                }}
			                            ],
			                            [
			                            { name: "<s:text name='pigd.summary.grid.poAmount' />", field: "poAmt" },
			                            { name: "<s:text name='pigd.summary.grid.invAmount' />", field: "invAmt" },
			                            { name: "<s:text name='pigd.summary.grid.grnAmount' />", field: "grnAmt" },
			                            { name: "<s:text name='pigd.summary.grid.dnAmount' />", field: "dnAmt" }
			                        ]
									</s:else>
									]
					}
                    
                    
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: structure,
                        
                        plugins: {
                            indirectSelection: {headerSelector:true, width:'2%', styles:'text-align: center;'},
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

                    <s:if test="#session.SUPPLIER_DISPUTE != 'yes'" >
                    	grid.canSort=function(col){if(col==9 || col==15)return false;else return true;};
                    </s:if>
                    grid.startup();
                    
                    // init tooltips
                    
                    
                    loadTooltip = function()
                    {
	                    dojo.connect(grid, "onCellMouseOver", function(event)
	                    {
	                    	<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
		                    	if (event.cellIndex != 1 && event.cellIndex != 2 && event.cellIndex != 9&& event.cellIndex != 10)
		                    	{
		                    		return;
		                    	}
		                    	var value="";
		                    	var tooltipField = "";
		                        var tooltipLabel = "";
		                        
		                       var rowItem = grid.getItem(event.rowIndex);
		                        if (rowItem == null) return;
		                    	
		                    	if (event.cellIndex == 1)
		                    	{
		                    		var tooltipField = "buyerName";
			                        var tooltipLabel = "Buyer Name";
		                    	}

								if (event.cellIndex == 2)
								{
									var tooltipField = "supplierName";
			                        var tooltipLabel = "Supplier Name";
								}
		                    	
								if (event.cellIndex == 9)
								{
									var tooltipField = "supplierStatusActionRemarks";
			                        var tooltipLabel = "Supplier Status Remarks";
								}

		                        
		                        value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
		                        value +=tooltipLabel +'</label><br/>';
		                        var rowItem = grid.getItem(event.rowIndex);
		                        if (rowItem == null) return;
		                        
		                        var rowItemValue = grid.store.getValue(rowItem,tooltipField);
		                        if (rowItemValue != null)
		                        {
			                        var content = "";
		                        	var length = 0;
		                        	for (var i=0;i < rowItemValue.length ; i++)
	                        		{
		                        		length ++;
		                        		if (rowItemValue.charAt(i)=='\n')
		                        		{
		                        			content += '<br/>';
		                        			length = 0;
		                        		}
		                        		else
		                        		{
		                        			content += rowItemValue.charAt(i);
		                        			
		                        			if (length > 50)
		                        			{
		                        				content += '<br/>';
		                        				length = 0;
		                        			}
		                        		}
	                        		}
		                        	value += content;
		                        	if(event.cellIndex==11)
		                        	{
		                        		Tooltip.defaultPosition=["before-centered","after"];
		                        	}
		                        	else
		                        	{
		                        		Tooltip.defaultPosition=["after-centered","after"];
		                        	}
			                        
			                        Tooltip.show(value, event.cellNode);
		                        }
	                    	</s:if>
	                    	<s:else>
		                    	if (event.cellIndex != 10 && event.cellIndex != 9)
		                    	{
		                    		return;
		                    	}
		                    	var value="";
		                        var tooltipField = "invStatusActionRemarks";
		                        var tooltipLabel = "Remarks";

								if (event.cellIndex == 10)
								{
									tooltipField = "invStatusActionRemarks";
			                        tooltipLabel = "Remarks";
								}
		                        
		                        value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
		                        value +=tooltipLabel +'</label><br/>';
		                        var rowItem = grid.getItem(event.rowIndex);
		                        if (rowItem == null) return;
		                        var rowItemValue = grid.store.getValue(rowItem,tooltipField);
		                        
		                        if (rowItemValue != null)
		                        {
			                        var content = "";
		                        	var length = 0;
		                        	for (var i=0;i < rowItemValue.length ; i++)
	                        		{
		                        		length ++;
		                        		if (rowItemValue.charAt(i)=='\n')
		                        		{
		                        			content += '<br/>';
		                        			length = 0;
		                        		}
		                        		else
		                        		{
		                        			content += rowItemValue.charAt(i);
		                        			
		                        			if (length > 50)
		                        			{
		                        				content += '<br/>';
		                        				length = 0;
		                        			}
		                        		}
	                        		}
		                        	value += content;
		                        	if (event.cellIndex == 10)
									{
		                        		Tooltip.defaultPosition=["before-centered","after"];
									}
		                        	else
									{
										Tooltip.defaultPosition=["after-centered","after"];
									}
			                        
			                        Tooltip.show(value, event.cellNode);
		                        }
	                        </s:else>
	                        return;
	                    });
                    }
                    loadTooltip();
                    // remove tooltip
                    dojo.connect(grid, "onCellMouseOut", function(event){
                    	<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
	                    	if (event.cellIndex != 1 && event.cellIndex != 2 && event.cellIndex != 9 && event.cellIndex != 10)
	                        {
	                            return;
	                        }
                    	</s:if>
                    	<s:else>
	                    	if (event.cellIndex != 10 && event.cellIndex != 9)
	                        {
	                            return;
	                        }
                    	</s:else>
                        Tooltip.hide(event.cellNode);
                    });
                    

                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    var anyRecordSelected = function()
                    {
                        return grid.selection.getSelected().length != 0;
                    }

                    var fn = function(url)
                    {
                        if (!anyRecordSelected())
                        {
                            infoDialog.show();
                            return;
                        }
                    
                        var oids = "";
                        
                        array.forEach(grid.selection.getSelected(), function(item){
                            try
                            {
                                var oid = store.getValue(item, 'matchingOid');
                                oids = oids + oid + '-';
                            }
                            catch (e)
                            {
                                // there may be a bug in dojo's selection plugin if select multiple records from different pages.
                                //alert(e);
                            }
                        });
                        
                        oids = oids.substring(0, oids.length-1);
                        xhr.get({
                                url: '<s:url value="/poInvGrnDnMatching/putParamIntoSession.action" />',
                                content: {selectedOids: oids},
                                load: function(data)
                                {
                                	if (data!='x')
                                    {
                                        (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                                    }
                                    else
                                    {
	                                    changeToURL(url);
                                    }
                                }
                            });
                    }

                    if (dom.byId("searchBtn"))
                    {
                        on(registry.byId("searchBtn"), 'click', function(){

                        	(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
                            
							if(!grid._isLoaded)
							{
							 	var infoDialog = new InformationDialog({message: "<s:text name='alert.summary.search.not.finished'/>"});
                               	infoDialog.show();
								return;
							}
                            
                        	var poDateFrom = dijit.byId("param.poDateFrom").getValue();
                            var poDateTo = dijit.byId("param.poDateTo").getValue();
                            if(null != poDateTo && new Date(poDateFrom) > new Date(poDateTo))
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2105' />"});
                                infoDialog.show();
                                return;
                            }
                        	var invDateFrom = dijit.byId("param.invDateFrom").getValue();
                            var invDateTo = dijit.byId("param.invDateTo").getValue();
                            if(null != invDateTo && new Date(invDateFrom) > new Date(invDateTo))
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2105' />"});
                                infoDialog.show();
                                return;
                            }
                        	var grnDateFrom = dijit.byId("param.grnDateFrom").getValue();
                            var grnDateTo = dijit.byId("param.grnDateTo").getValue();
                            if(null != grnDateTo && new Date(grnDateFrom) > new Date(grnDateTo))
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2105' />"});
                                infoDialog.show();
                                return;
                            }
                            var matchingDateFrom = dijit.byId("param.matchingDateFrom").getValue();
                            var matchingDateTo = dijit.byId("param.matchingDateTo").getValue();
                            if(null != matchingDateTo && new Date(matchingDateFrom) > new Date(matchingDateTo))
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2105' />"});
                                infoDialog.show();
                                return;
                            }
                            <s:if test="#session.SUPPLIER_DISPUTE != 'yes'" >
                        	var dnDateFrom = dijit.byId("param.dnDateFrom").getValue();
                            var dnDateTo = dijit.byId("param.dnDateTo").getValue();
                            if(null != dnDateTo && new Date(dnDateFrom) > new Date(dnDateTo))
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2105' />"});
                                infoDialog.show();
                                return;
                            }
                            </s:if>
                            xhr.post({
                                url: '<s:url value="/poInvGrnDnMatching/search.action" />',
                                form: dom.byId("searchForm"),
                                load: function(rlt)
                                {
                                    grid.setQuery();
                                }
                            });
                        });
                    }

                    var selectType = new SelectAllConfirmDialog({
                        message: '<s:text name="B2BPC2104" />',
                        yesBtnPressed: function(){
                        	if(!grid._isLoaded)
							{
								return;
							}
                            
                        	var poDateFrom = dijit.byId("param.poDateFrom").getValue();
                            var poDateTo = dijit.byId("param.poDateTo").getValue();
                            if(null != poDateTo && new Date(poDateFrom) > new Date(poDateTo))
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2105' />"});
                                infoDialog.show();
                                return;
                            }
                        	var invDateFrom = dijit.byId("param.invDateFrom").getValue();
                            var invDateTo = dijit.byId("param.invDateTo").getValue();
                            if(null != invDateTo && new Date(invDateFrom) > new Date(invDateTo))
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2105' />"});
                                infoDialog.show();
                                return;
                            }
                        	var grnDateFrom = dijit.byId("param.grnDateFrom").getValue();
                            var grnDateTo = dijit.byId("param.grnDateTo").getValue();
                            if(null != grnDateTo && new Date(grnDateFrom) > new Date(grnDateTo))
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2105' />"});
                                infoDialog.show();
                                return;
                            }
                            var matchingDateFrom = dijit.byId("param.matchingDateFrom").getValue();
                            var matchingDateTo = dijit.byId("param.matchingDateTo").getValue();
                            if(null != matchingDateTo && new Date(matchingDateFrom) > new Date(matchingDateTo))
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2105' />"});
                                infoDialog.show();
                                return;
                            }
                            <s:if test="#session.SUPPLIER_DISPUTE != 'yes'" >
                        	var dnDateFrom = dijit.byId("param.dnDateFrom").getValue();
                            var dnDateTo = dijit.byId("param.dnDateTo").getValue();
                            if(null != dnDateTo && new Date(dnDateFrom) > new Date(dnDateTo))
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2105' />"});
                                infoDialog.show();
                                return;
                            }
                            </s:if>

                            xhr.post({
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=excel&msgType=MATCHING',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
			                            submitForm('searchForm', '<s:url value="/poInvGrnDnMatching/exportExcel.action" />?selectAll=true');
                                	}
                                	else
                                	{
                                		new InformationDialog({message: printDocMsg}).show();
                                	}
                                }
                            });
                        },
                        noBtnPressed: function(){
                        	if (!anyRecordSelected())
                            {
                                infoDialog.show();
                                return;
                            }
                        
                            var oids = "";
                            
                            array.forEach(grid.selection.getSelected(), function(item){
                                try
                                {
                                    var oid = store.getValue(item, 'matchingOid');
                                    oids = oids + oid + '-';
                                }
                                catch (e)
                                {
                                    // there may be a bug in dojo's selection plugin if select multiple records from different pages.
                                    //alert(e);
                                }
                            });
                            
                            oids = oids.substring(0, oids.length-1);
                            xhr.get({
                               url: '<s:url value="/poInvGrnDnMatching/putParamIntoSession.action" />',
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
                                           url: '<s:url value="/ajax/checkDocCount.action" />?printType=excel&msgType=MATCHING',
                                           handleAs: "json",
                                           form: dom.byId("searchForm"),
                                           load: function(printDocMsg)
                                           {
                                           	if (printDocMsg == 'success')
                                           	{
			                                    changeToURL('<s:url value="/poInvGrnDnMatching/exportExcel.action" />');
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
                    if (dom.byId("exportExcelBtn"))
                    {
                        on(registry.byId("exportExcelBtn"), 'click', 
                            function()
                            {
	                        	(new B2BPortalBase()).resetTimeout(
	            	                    '<s:property value="#session.commonParam.timeout" />',
	            	                    '<s:url value="/logout.action" />');
                        	   if (grid.selection.getSelectedCount() > 0 && grid.selection.getSelectedCount() == grid.rowCount)
                        	   {
                        		   selectType.show();
                        	   }
                        	   else
                        	   {
                        		   if (!anyRecordSelected())
                                   {
                                       infoDialog.show();
                                       return;
                                   }
                               
                                   var oids = "";
                                   
                                   array.forEach(grid.selection.getSelected(), function(item){
                                       try
                                       {
                                           var oid = store.getValue(item, 'matchingOid');
                                           oids = oids + oid + '-';
                                       }
                                       catch (e)
                                       {
                                           // there may be a bug in dojo's selection plugin if select multiple records from different pages.
                                           //alert(e);
                                       }
                                   });
                                   
                                   oids = oids.substring(0, oids.length-1);
                                   xhr.get({
                                      url: '<s:url value="/poInvGrnDnMatching/putParamIntoSession.action" />',
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
                                                  url: '<s:url value="/ajax/checkDocCount.action" />?printType=excel&msgType=MATCHING',
                                                  handleAs: "json",
                                                  form: dom.byId("searchForm"),
                                                  load: function(printDocMsg)
                                                  {
                                                  	if (printDocMsg == 'success')
                                                  	{
			      	                                    changeToURL('<s:url value="/poInvGrnDnMatching/exportExcel.action" />');
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
                            }
                        );
                    }
                     
                    checkAccept = function(oids, supplierStatus, closeStatus, matchStatus, revised)
	                            {
			                    	(new B2BPortalBase()).resetTimeout(
			        	                    '<s:property value="#session.commonParam.timeout" />',
			        	                    '<s:url value="/logout.action" />');
	        	                    
	                               if (closeStatus == true)
	                               {
	                            	   (new InformationDialog({message: '<s:text name="B2BPC2108" />'})).show();
	                        		   return;
	                               }
	                               if (matchStatus=="Pending")
								   {
	                            	   (new InformationDialog({message: '<s:text name="B2BPC2134" />'})).show();
	                        		   return;
								   }
	                               if (matchStatus=="Matched")
								   {
	                            	   (new InformationDialog({message: '<s:text name="B2BPC2135" />'})).show();
	                        		   return;
								   }
	                               if (matchStatus=="Insufficient Inv")
	                               {
	                            	   (new InformationDialog({message: '<s:text name="B2BPC2113" />'})).show();
	                        		   return;
	                               }
	                               if (matchStatus=="Outdated")
	                               {
	                            	   (new InformationDialog({message: '<s:text name="B2BPC2114" />'})).show();
	                        		   return;
	                               }
	                               if (revised==true)
	                               {
	                            	   (new InformationDialog({message: '<s:text name="B2BPC2148" />'})).show();
	                        		   return;
	                               }
	                               if (supplierStatus=="ACCEPTED")
	                               {
	                            	   (new InformationDialog({message: '<s:text name="B2BPC2110" />'})).show();
	                        		   return;
	                               }
	                               if (supplierStatus=="REJECTED")
	                               {
	                            	   (new InformationDialog({message: '<s:text name="B2BPC2109" />'})).show();
	                        		   return;
	                               }
	
	                               accept(oids, "accept");
	                            }
                        
                    checkReject = function(oids, supplierStatus, closeStatus, matchStatus, revised)
                                {
			                    	(new B2BPortalBase()).resetTimeout(
			        	                    '<s:property value="#session.commonParam.timeout" />',
			        	                    '<s:url value="/logout.action" />');
                                   if (closeStatus == true)
                                   {
                                	   (new InformationDialog({message: '<s:text name="B2BPC2108" />'})).show();
                            		   return;
                                   }
								   if (matchStatus=="Pending")
								   {
									   (new InformationDialog({message: '<s:text name="B2BPC2134" />'})).show();
	                        		   return;
								   }
								   if (matchStatus=="Matched")
								   {
									   (new InformationDialog({message: '<s:text name="B2BPC2135" />'})).show();
	                        		   return;
								   }
                                   if (matchStatus=="Insufficient Inv")
                                   {
                                	   (new InformationDialog({message: '<s:text name="B2BPC2113" />'})).show();
                            		   return;
                                   }
                                   if (matchStatus=="Outdated")
                                   {
                                	   (new InformationDialog({message: '<s:text name="B2BPC2114" />'})).show();
                            		   return;
                                   }
                                   if (revised==true)
                                   {
                                	   (new InformationDialog({message: '<s:text name="B2BPC2148" />'})).show();
	                        		   return;
                                   }
                                   if (supplierStatus=="ACCEPTED")
                                   {
                                	   (new InformationDialog({message: '<s:text name="B2BPC2110" />'})).show();
                            		   return;
                                   }
                                   if (supplierStatus=="REJECTED")
                                   {
                                	   (new InformationDialog({message: '<s:text name="B2BPC2109" />'})).show();
                            		   return;
                                   }

                                   reject(oids, "reject");
                                }

                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.close.records" />',
                        yesBtnPressed: function(){
                        	var csrfToken = document.getElementById("csrfToken").value;
                            fn('<s:url value="/poInvGrnDnMatching/saveClose.action" />?csrfToken='+csrfToken);
                        }
                    });

                    if (dom.byId("saveCloseBtn"))
                    {
                    	on(registry.byId("saveCloseBtn"), 'click', 
                            function()
                            {
                    			(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
	                    		if (!anyRecordSelected())
	                            {
	                                infoDialog.show();
	                                return;
	                            }
	                            confirmDialog.show();
                            }
                            );
                    }
                		
                    if (dom.byId("initAuditPriceBtn"))
                    {
                    	on(registry.byId("initAuditPriceBtn"), 'click', 
                            function()
                            {
                    			(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
	                    		if (!anyRecordSelected())
	                            {
	                                infoDialog.show();
	                                return;
	                            }
	                   			else if (grid.selection.getSelectedCount() != 1)
		                 	    {
		                 		    (new InformationDialog({message: '<s:text name="alert.select.one" />'})).show();
		                 	    	return;
		                 	    }
	                   			else
	                   			{
	                   				fn('<s:url value="/poInvGrnDnMatching/initAuditPrice.action" />');
	                   			}
                            }
                        );
                    }

                    if (dom.byId("initAuditQtyBtn"))
                    {
                    	on(registry.byId("initAuditQtyBtn"), 'click', 
                            function()
                            {
                    			(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
	                    		if (!anyRecordSelected())
	                            {
	                                infoDialog.show();
	                                return;
	                            }
	                   			else if (grid.selection.getSelectedCount() != 1)
		                 	    {
		                 		    (new InformationDialog({message: '<s:text name="alert.select.one" />'})).show();
		                 	    	return;
		                 	    }
	                   			else
	                   			{
	                   				fn('<s:url value="/poInvGrnDnMatching/initAuditQty.action" />');
	                   			}
                            }
                        );
                    }

                    if (dom.byId("saveApproveBtn"))
                    {
                        on(registry.byId("saveApproveBtn"), 'click', 
                            function()
                            {
                        		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
	                        	if (grid.selection.getSelectedCount() != 1)
		                 	    {
		                 		    (new InformationDialog({message: '<s:text name="alert.select.one" />'})).show();
		                 	    	return;
		                 	    }

		                 	    var oid = "";
		                 	    var supplierStatus = "";
		                 	    var matchStatus = "";
		                 	    var buyerStatus = "";
		                 	    var invStatus = "";
		                 	    var supplierStatus = "";
		                 	    var revised;
	                        	array.forEach(grid.selection.getSelected(), function(item){
                                    try
                                    {
                                 		oid = store.getValue(item, 'matchingOid');
                                        matchStatus = store.getValue(item, 'matchingStatusValue');
                                        buyerStatus = store.getValue(item, 'buyerStatusValue');
                                        invStatus = store.getValue(item, 'invStatusValue');
                                        supplierStatus = store.getValue(item, 'supplierStatusValue');
                                        revised = store.getValue(item, 'revised');
                                    }
                                    catch (e)
                                    {
                                        // there may be a bug in dojo's selection plugin if select multiple records from different pages.
                                        //alert(e);
                                    }
                                    
                                });
	                        	if (matchStatus=="Pending")
							    {
	                        		(new InformationDialog({message: '<s:text name="B2BPC2134" />'})).show();
	                        		   return;
							    }
	                        	if (matchStatus=="Matched")
							    {
	                        		(new InformationDialog({message: '<s:text name="B2BPC2135" />'})).show();
	                        		   return;
							    }
	                        	if (matchStatus=="Insufficient Inv")
                                {
                            		(new InformationDialog({message: '<s:text name="B2BPC2113" />'})).show();
                        			return;
                                }
	                        	if (matchStatus=="Outdated")
                                {
                             		(new InformationDialog({message: '<s:text name="B2BPC2114" />'})).show();
                         			return;
                                }
								if (invStatus=="System Approved" || invStatus=="Approved")
								{
									(new InformationDialog({message: '<s:text name="B2BPC2130" />'})).show();
	                         	    return;
								}
								if (!revised)
                                {
									if (supplierStatus=="PENDING")
									{
										(new InformationDialog({message: '<s:text name="B2BPC2136" />'})).show();
		                         	    return;
									}
									if (supplierStatus=="ACCEPTED" && buyerStatus=="PENDING")
									{
										(new InformationDialog({message: '<s:text name="B2BPC2137" />'})).show();
		                         	    return;
									}
									if (supplierStatus=="REJECTED" && buyerStatus=="PENDING")
									{
										(new InformationDialog({message: '<s:text name="B2BPC2131" />'})).show();
		                         	    return;
									}
									if (buyerStatus=="REJECTED")
									{
										(new InformationDialog({message: '<s:text name="B2BPC2133" />'})).show();
		                         	    return;
									}
                                }

								approveInvRemarks(oid);
                            }
                        );
                    }
                    
                    hideDiv = function(src)
                    {
                        registry.byId(src).hide();
                    };
                    
                    refreshGrid = function()
                    {
                    	grid.setQuery();
                    }
                    
                    loadClosedTooltip = function(src, actionBy, actionDate, actionRemarks)
                    {
                            
                        var value="";
                        var actionBy = actionBy == 'null' ? '' : actionBy;
                        var actionByLabel = "Closed By";

                        var actionDate = actionDate;
                        var actionDateLabel = "Closed Date";

                        var actionRemarks = actionRemarks == 'null' ? '' : actionRemarks;
                        var actionRemarksLable = "Closed Remarks";
        
        
                        if (actionBy != null && actionBy != "")
                        {
                            value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
                            value +=actionByLabel +'</label><br/>';
                            var content = "";
                            var length = 0;
                            for (var i=0;i < actionBy.length ; i++)
                            {
                                length ++;
                                if (actionBy.charAt(i)=='\n')
                                {
                                    content += '<br/>';
                                    length = 0;
                                }
                                else
                                {
                                    content += actionBy.charAt(i);
                                    
                                    if (length > 50)
                                    {
                                        content += '<br/>';
                                        length = 0;
                                    }
                                }
                            }
                            value += content + '<br/>';
                        }

                        if (actionDate != null && actionDate != "")
                        {
                            value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
                            value +=actionDateLabel +'</label><br/>';
                            var content = "";
                            var length = 0;
                            for (var i=0;i < actionDate.length ; i++)
                            {
                                length ++;
                                if (actionDate.charAt(i)=='\n')
                                {
                                    content += '<br/>';
                                    length = 0;
                                }
                                else
                                {
                                    content += actionDate.charAt(i);
                                    
                                    if (length > 50)
                                    {
                                        content += '<br/>';
                                        length = 0;
                                    }
                                }
                            }
                            value += content + '<br/>';
                        }

                        if (actionRemarks != null && actionRemarks != "")
                        {
                            value += '<label style="font-weight:bold;text-decoration:underline;line-height:20px;">';
                            value +=actionRemarksLable +'</label><br/>';
                            var content = "";
                            var length = 0;
                            for (var i=0;i < actionRemarks.length ; i++)
                            {
                                length ++;
                                if (actionRemarks.charAt(i)=='\n')
                                {
                                    content += '<br/>';
                                    length = 0;
                                }
                                else
                                {
                                    content += actionRemarks.charAt(i);
                                    
                                    if (length > 50)
                                    {
                                        content += '<br/>';
                                        length = 0;
                                    }
                                }
                            }
                            value += content + '<br/>';
                        }


                        Tooltip.defaultPosition=["before-centered","after"];
                        if (value != null && value != "")
                        {
                            Tooltip.show(value,src);
                        }
                        return;
                    }
                    // remove tooltip
                    closeClosedTooltip = function(src)
                    {
                        Tooltip.hide(src);
                    }

                });

        var matchingOid;
        var index;
        var supplierStatusValue;

        function approveInvRemarks(OperationMatchingOid)
        {
        	matchingOid = OperationMatchingOid;
        	var ConfirmDialog = require("custom/ConfirmDialog");
            var xhr = require("dojo/_base/xhr");
            var registry = require("dijit/registry");
        	var confirmDialog = new ConfirmDialog({
                message: '<s:text name="B2BPC2101" />',
                yesBtnPressed: function(){
                	document.getElementById("actionRemarks").value="";
                	registry.byId("invRemarks").show();
                	
                }
            });
        	confirmDialog.show();
        }

        
        function approve(OperationMatchingOid, pendingFlag, operationIndex)
        {
        	matchingOid = OperationMatchingOid;
        	index = operationIndex;
            var InformationDialog = require("custom/InformationDialog");
            if(!pendingFlag)
            {
            	var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2103" />'});
            	infoDialog.show();
            	return;
            }
            var ConfirmDialog = require("custom/ConfirmDialog");
            var xhr = require("dojo/_base/xhr");
            var registry = require("dijit/registry");
        	var confirmDialog = new ConfirmDialog({
                message: '<s:text name="B2BPC2101" />',
                yesBtnPressed: function(){
                	document.getElementById("actionRemarks").value="";
                	registry.byId("invRemarks").show();
                	
                }
            });
        	confirmDialog.show();
        }

        function accept(OperationMatchingOid, supplierStatus)
        {
        	matchingOid = OperationMatchingOid;
        	supplierStatusValue = supplierStatus;
            var ConfirmDialog = require("custom/ConfirmDialog");
            var xhr = require("dojo/_base/xhr");
            var registry = require("dijit/registry");
        	var confirmDialog = new ConfirmDialog({
                message: '<s:text name="B2BPC2111" />',
                yesBtnPressed: function(){
                	document.getElementById("supplierRemarksText").value="";
                	registry.byId("supplierRemarks").show();
                	
                }
            });
        	confirmDialog.show();
        }

        function reject(OperationMatchingOid, supplierStatus)
        {
        	matchingOid = OperationMatchingOid;
        	supplierStatusValue = supplierStatus;
            var ConfirmDialog = require("custom/ConfirmDialog");
            var xhr = require("dojo/_base/xhr");
            var registry = require("dijit/registry");
        	var confirmDialog = new ConfirmDialog({
                message: '<s:text name="B2BPC2112" />',
                yesBtnPressed: function(){
                	document.getElementById("supplierRemarksText").value="";
                	registry.byId("supplierRemarks").show();
                	
                }
            });
        	confirmDialog.show();
        }

        function saveSupplierStatus()
        {
        	var ConfirmDialog = require("custom/ConfirmDialog");
        	var InformationDialog = require("custom/InformationDialog");
            var xhr = require("dojo/_base/xhr");
            var remarks = dijit.byId("supplierRemarksText").value;
            if (remarks.length == 0 || remarks.length > 255)
            {
            	var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2106" />'});
                infoDialog.show();
                return;
            }
            var csrfToken = document.getElementById("csrfToken").value;
        	xhr.post({
                url: '<s:url value="/poInvGrnDnMatching/saveAcceptOrReject.action" />',
                content: {"param.matchingOid": matchingOid, "param.supplierStatusActionRemarks":remarks, "param.supplierStatusValue":supplierStatusValue, "csrfToken":csrfToken},
                load: function(result)
                {
                    if (result=='"revised"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2148" />'})).show();
                    }
                    else if (result=='"pending"')
				    {
                	    (new InformationDialog({message: '<s:text name="B2BPC2134" />'})).show();
				    }
                	else if (result=='"matched"')
				    {
                	    (new InformationDialog({message: '<s:text name="B2BPC2135" />'})).show();
				    }
                	else if (result == '"closed"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2128" />'})).show();
                    }
                    else if(result == '"insufficientInv"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2113" />'})).show();
                    }
                    else if(result == '"outdated"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2114" />'})).show();
                    }
                    else if(result == '"accepted"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2110" />'})).show();
                    }
                    else if(result == '"rejected"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2109" />'})).show();
                    }
                    else if (result == '"remarks"')
                 	{
                 		(new InformationDialog({message: '<s:text name="B2BPC2106" />'})).show();
                 	}
                    else if(result == '"1"')
                    {
                    	hideDiv("supplierRemarks");
                    	refreshGrid();
                    	loadTooltip();
                    	clearRemarks();
                    }
                    else if(result == '"rejectsuccess"')
                    {
                    	(new InformationDialog({message: '<s:text name="B2BPC2150" />'})).show();
                    	hideDiv("supplierRemarks");
                    	refreshGrid();
                    	loadTooltip();
                    	clearRemarks();
                    }
                    else
                    {
                        var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2102" />'});
                        infoDialog.show();
                    }
                }
            });
        }

        function clearDate(src)
        {
            var registry = require("dijit/registry");
            registry.byId(src).attr("aria-valuenow", null);
            registry.byId(src).attr("value", null);
        }

        function clearRemarks()
        {
            var registry = require("dijit/registry");
            registry.byId("actionRemarks").set("value","");
            registry.byId("supplierRemarksText").set("value","");
        }
        
        function saveApprove()
        {
        	var ConfirmDialog = require("custom/ConfirmDialog");
        	var InformationDialog = require("custom/InformationDialog");
            var xhr = require("dojo/_base/xhr");
            var remarks = dijit.byId("actionRemarks").value;
            if (remarks.length == 0 || remarks.length > 255)
            {
            	var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2106" />'});
                infoDialog.show();
                return;
            }
            hideDiv("invRemarks");
            clearRemarks();
            var csrfToken = document.getElementById("csrfToken").value;
        	xhr.post({
                url: '<s:url value="/poInvGrnDnMatching/approve.action" />',
                content: {"param.matchingOid": matchingOid, "param.invStatusActionRemarks":remarks, "csrfToken":csrfToken},
                load: function(result)
                {
                	if (result=='"pending"')
				    {
                	    (new InformationDialog({message: '<s:text name="B2BPC2134" />'})).show();
				    }
                	else if (result=='"matched"')
				    {
                	    (new InformationDialog({message: '<s:text name="B2BPC2135" />'})).show();
				    }
                	else if (result == '"insufficientInv"' )
                 	{
                 		(new InformationDialog({message: '<s:text name="B2BPC2113" />'})).show();
                 	}
                 	else if (result == '"outdated"' )
                 	{
                 		(new InformationDialog({message: '<s:text name="B2BPC2114" />'})).show();
                 	}
                 	else if (result == '"approved"' )
                 	{
                 		(new InformationDialog({message: '<s:text name="B2BPC2130" />'})).show();
                 	}
                 	else if (result == '"supplierpending"')
                 	{
                 		(new InformationDialog({message: '<s:text name="B2BPC2136" />'})).show();
                 	}
                 	else if (result == '"supplieraccept"')
                 	{
						(new InformationDialog({message: '<s:text name="B2BPC2137" />'})).show();
                 	}
                 	else if (result == '"buyerpending"' )
                 	{
                 		(new InformationDialog({message: '<s:text name="B2BPC2131" />'})).show();
                 	}
                 	else if (result == '"rejected"' )
                 	{
                 		(new InformationDialog({message: '<s:text name="B2BPC2132" />'})).show();
                 	}
                 	else if (result == '"remarks"')
                 	{
                 		(new InformationDialog({message: '<s:text name="B2BPC2106" />'})).show();
                 	}
                 	else if(result == '"1"')
                    {
                    	hideDiv("invRemarks");
                    	refreshGrid();
                    	loadTooltip();
                    	clearRemarks();
                    }
                    else
                    {
                        var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2102" />'});
                        infoDialog.show();
                    }
                }
            });
        }

        function clearButton(src)
        {
            var registry = require("dijit/registry");
            registry.byId(src).attr("aria-valuenow", null);
    		registry.byId(src).attr("value", null);
        }


        function clickPendingForClosing(src)
        {
        	var registry = require("dijit/registry");
        	if (registry.byId(src).checked)
        	{
        		registry.byId('param.revised').attr("disabled", true);
        		registry.byId('param.buyerStatus').attr("disabled", true);
        		registry.byId('param.supplierStatus').attr("disabled", true);
        	}
        	else
        	{
        		registry.byId('param.revised').attr("disabled", false);
        		registry.byId('param.buyerStatus').attr("disabled", false);
        		registry.byId('param.supplierStatus').attr("disabled", false);
        	}
        }
    </script>
    <div dojoType="dijit.Dialog" id="invRemarks" title="Remarks" style="width:50%">
        <table class="commtable">
            <tr>
                <td style="vertical-align:text-top; line-height:23px"><span class="required">*</span></td>
                <td style="vertical-align:text-top; line-height:23px"><s:text name="admin.msgManagerment.summary.msgContent" /></td>
                <td style="vertical-align:text-top; line-height:23x">:</td>
                <td style="line-height:23px; font:Arial, Helvetica, sans-serif"><textarea cols="70%" rows="16" id="actionRemarks" maxlength="255" data-dojo-type="dijit/form/Textarea"></textarea></td>
            </tr>
            <tr class="space"><td></td></tr>
            <tr>
                <td colspan="4" style="text-align:left">
                   <button data-dojo-type="dijit.form.Button"  onclick="saveApprove();"><s:text name="button.ok"/></button>
                   <button data-dojo-type="dijit.form.Button" onclick="clearRemarks();"><s:text name="button.clear"/></button>
               </td>
           </tr>
        </table>
    </div>     
    <div dojoType="dijit.Dialog" id="supplierRemarks" title="Remarks" style="width:50%">
        <table class="commtable">
            <tr>
                <td style="vertical-align:text-top; line-height:23px"><span class="required">*</span></td>
                <td style="vertical-align:text-top; line-height:23px"><s:text name="admin.msgManagerment.summary.msgContent" /></td>
                <td style="vertical-align:text-top; line-height:23x">:</td>
                <td style="line-height:23px; font:Arial, Helvetica, sans-serif"><textarea cols="70%" rows="16" id="supplierRemarksText" maxlength="255" data-dojo-type="dijit/form/Textarea"></textarea></td>
            </tr>
            <tr class="space"><td></td></tr>
            <tr>
                <td colspan="4" style="text-align:left">
                   <button data-dojo-type="dijit.form.Button"  onclick="saveSupplierStatus();"><s:text name="button.ok"/></button>
                   <button data-dojo-type="dijit.form.Button" onclick="clearRemarks();"><s:text name="button.clear"/></button>
               </td>
           </tr>
        </table>
    </div>  
</head>
<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/search.action')" >
	           	<button data-dojo-type="dijit.form.Button" id="searchBtn" ><s:text name="button.search" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/exportExcel.action')" >
	           	<button data-dojo-type="dijit.form.Button" id="exportExcelBtn" ><s:text name="button.exportExcel" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/approve.action') && #session.SUPPLIER_DISPUTE == 'yes'" >
            	<button data-dojo-type="dijit.form.Button" id="saveApproveBtn" ><s:text name="button.approve.invoice" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveClose.action')" >
            	<button data-dojo-type="dijit.form.Button" id="saveCloseBtn" ><s:text name="button.close" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>

    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="pigd.summary.searcheara.headerLabel" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
		<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
        	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 2 || #session.SESSION_CURRENT_USER_PROFILE.userType == 4">
        	<table class="commtable">
        	<tbody>
        		<tr>
	                <td width="100"><s:text name="pigd.summary.searcheara.buyer" /></td>
	                <td width="10">:</td>
	                <td>
	                    <s:select data-dojo-type="dijit/form/FilteringSelect" name="param.buyerOid" list="param.buyerList" 
	                        listKey="buyerOid" listValue="buyerName" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
	                </td>
	                <td width="100"><s:text name="pigd.summary.searcheara.poStoreCode" /></td>
	                <td>:</td>
	                <td><s:textfield id="param.poStoreCode" name="param.poStoreCode" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
	                	<button  data-dojo-type="dijit.form.Button" id="clearPoStoreCode" type="button" onclick="clearButton('param.poStoreCode');"><s:text name='button.clear' /></button>
	                </td>
            	</tr>
            	<tr>
                    <td><s:text name="pigd.summary.searcheara.supplierCode" /></td>
                    <td>:</td>
                    <td>
                       <s:textfield id="param.supplierCode" name="param.supplierCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple"/>
                       <button  data-dojo-type="dijit.form.Button" id="clearSupplierCode" type="button" onclick="clearButton('param.supplierCode');"><s:text name='button.clear' /></button>
                    </td>
                    <td><s:text name="pigd.summary.searcheara.supplierName" /></td>
	                <td>:</td>
	                <td>
	                    <s:textfield name="param.supplierName" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.supplierName"/>
	                	<button  data-dojo-type="dijit.form.Button" id="clearSupplierName" type="button" onclick="clearButton('param.supplierName');"><s:text name='button.clear' /></button>
	                </td>
                </tr>
	            <tr>
	                <td><s:text name="pigd.summary.searcheara.poNo" /></td>
	                <td>:</td>
	                <td><s:textfield id="param.poNo" name="param.poNo" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
	                	<button  data-dojo-type="dijit.form.Button" id="clearPoNo" type="button" onclick="clearButton('param.poNo');"><s:text name='button.clear' /></button>
	                </td>
	                <td width="100"><s:text name="pigd.summary.searcheara.poDate" /></td>
	                 <td>:</td>
	                 <td>
	                     <label for="Dfrom1"><s:text name="Value.from" /></label>
	                     <input type="text" id="param.poDateFrom" name="param.poDateFrom" 
	                         onkeydown="javascript:document.getElementById('param.poDateFrom').blur();" 
	                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                         value='<s:date name="param.poDateFrom" format="yyyy-MM-dd" />'/>
	                     <button  data-dojo-type="dijit.form.Button" id="clearPoDateFrom" type="button" onclick="clearDate('param.poDateFrom');"><s:text name='button.clear' /></button>
	                     <label for="Dto1"><s:text name="Value.to" /></label>
	                     <input type="text" id="param.poDateTo" name="param.poDateTo" 
	                     onkeydown="javascript:document.getElementById('param.poDateTo').blur();" 
	                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                     value='<s:date name="param.poDateTo" format="yyyy-MM-dd"/>'/>
	                     <button  data-dojo-type="dijit.form.Button" id="clearPoDateTo" type="button" onclick="clearDate('param.poDateTo');"><s:text name='button.clear' /></button>
	                 </td>
	             </tr>
	             <tr>
	                <td><s:text name="pigd.summary.searcheara.invNo" /></td>
	                <td>:</td>
	                <td><s:textfield id="param.invNo" name="param.invNo" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
	                	<button  data-dojo-type="dijit.form.Button" id="clearInvNo" type="button" onclick="clearButton('param.invNo');"><s:text name='button.clear' /></button>
	                </td>
	                <td><s:text name="pigd.summary.searcheara.invDate" /></td>
	                 <td>:</td>
	                 <td>
	                     <label for="Dfrom1"><s:text name="Value.from" /></label>
	                     <input type="text" id="param.invDateFrom" name="param.invDateFrom" 
	                         onkeydown="javascript:document.getElementById('param.invDateFrom').blur();" 
	                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                         value='<s:date name="param.invDateFrom" format="yyyy-MM-dd" />'/>
	                     <button  data-dojo-type="dijit.form.Button" id="clearinvDateFrom" type="button" onclick="clearDate('param.invDateFrom');"><s:text name='button.clear' /></button>
	                     <label for="Dto1"><s:text name="Value.to" /></label>
	                     <input type="text" id="param.invDateTo" name="param.invDateTo" 
	                     onkeydown="javascript:document.getElementById('param.invDateTo').blur();" 
	                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                     value='<s:date name="param.invDateTo" format="yyyy-MM-dd"/>'/>
	                     <button  data-dojo-type="dijit.form.Button" id="clearinvDateTo" type="button" onclick="clearDate('param.invDateTo');"><s:text name='button.clear' /></button>
	                 </td>
	             </tr>
	             <tr>
	             	<td><s:text name="pigd.summary.searcheara.grnNo" /></td>
	                <td>:</td>
	                <td><s:textfield id="param.grnNo" name="param.grnNo" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
	                	<button  data-dojo-type="dijit.form.Button" id="clearGrnNo" type="button" onclick="clearButton('param.grnNo');"><s:text name='button.clear' /></button>
	                </td>
	                <td><s:text name="pigd.summary.searcheara.grnDate" /></td>
	                 <td>:</td>
	                 <td>
	                     <label for="Dfrom1"><s:text name="Value.from" /></label>
	                     <input type="text" id="param.grnDateFrom" name="param.grnDateFrom" 
	                         onkeydown="javascript:document.getElementById('param.grnDateFrom').blur();" 
	                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                         value='<s:date name="param.grnDateFrom" format="yyyy-MM-dd" />'/>
	                     <button  data-dojo-type="dijit.form.Button" id="cleargrnDateFrom" type="button" onclick="clearDate('param.grnDateFrom');"><s:text name='button.clear' /></button>
	                     <label for="Dto1"><s:text name="Value.to" /></label>
	                     <input type="text" id="param.grnDateTo" name="param.grnDateTo" 
	                     onkeydown="javascript:document.getElementById('param.grnDateTo').blur();" 
	                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                     value='<s:date name="param.grnDateTo" format="yyyy-MM-dd"/>'/>
	                     <button  data-dojo-type="dijit.form.Button" id="cleargrnDateTo" type="button" onclick="clearDate('param.grnDateTo');"><s:text name='button.clear' /></button>
	                 </td>
	             </tr>
	             <s:if test="#session.SUPPLIER_DISPUTE != 'yes'" >
	             <tr>
	                	<td><s:text name="pigd.summary.searcheara.dnNo" /></td>
		                <td>:</td>
		                <td><s:textfield id="param.dnNo" name="param.dnNo" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
		                	<button  data-dojo-type="dijit.form.Button" id="clearDnNo" type="button" onclick="clearButton('param.DnNo');"><s:text name='button.clear' /></button>
		                </td>
		                <td><s:text name="pigd.summary.searcheara.dnDate" /></td>
		                 <td>:</td>
		                 <td>
		                     <label for="Dfrom1"><s:text name="Value.from" /></label>
		                     <input type="text" id="param.dnDateFrom" name="param.dnDateFrom" 
		                         onkeydown="javascript:document.getElementById('param.dnDateFrom').blur();" 
		                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                         value='<s:date name="param.dnDateFrom" format="yyyy-MM-dd" />'/>
		                     <button  data-dojo-type="dijit.form.Button" id="cleardnDateFrom" type="button" onclick="clearDate('param.dnDateFrom');"><s:text name='button.clear' /></button>
		                     <label for="Dto1"><s:text name="Value.to" /></label>
		                     <input type="text" id="param.dnDateTo" name="param.dnDateTo" 
		                     onkeydown="javascript:document.getElementById('param.dnDateTo').blur();" 
		                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                     value='<s:date name="param.dnDateTo" format="yyyy-MM-dd"/>'/>
		                     <button  data-dojo-type="dijit.form.Button" id="cleardnDateTo" type="button" onclick="clearDate('param.dnDateTo');"><s:text name='button.clear' /></button>
		                 </td>
	             </tr>
	             </s:if>
	             </tbody>
             </table>
             <table class="commtable">
             <tbody>
             <tr>
                 <td width="100"><s:text name="pigd.summary.searcheara.matchingDate" /></td>
                 <td>:</td>
                 <td>
                     <label for="Dfrom1"><s:text name="Value.from" /></label>
                     <input type="text" id="param.matchingDateFrom" name="param.matchingDateFrom" 
                         onkeydown="javascript:document.getElementById('param.matchingDateFrom').blur();" 
                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                         value='<s:date name="param.matchingDateFrom" format="yyyy-MM-dd" />'/>
                     <button  data-dojo-type="dijit.form.Button" id="clearmatchingDateFrom" type="button" onclick="clearDate('param.matchingDateFrom');"><s:text name='button.clear' /></button>
                     <label for="Dto1"><s:text name="Value.to" /></label>
                     <input type="text" id="param.matchingDateTo" name="param.matchingDateTo" 
                     onkeydown="javascript:document.getElementById('param.matchingDateTo').blur();" 
                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                     value='<s:date name="param.matchingDateTo" format="yyyy-MM-dd"/>'/>
                     <button  data-dojo-type="dijit.form.Button" id="clearmatchingDateTo" type="button" onclick="clearDate('param.matchingDateTo');"><s:text name='button.clear' /></button>
                 </td>
             </tr>
             </tbody>
             </table>
             <table class="commtable">
             	<tbody>
             		<tr>
             			<td width="100" rowSpan=2><s:text name="pigd.summary.searcheara.matchingStatus" /></td>
             			<td width="10" rowSpan=2>:</td>
           				<td><s:checkbox name="param.statusPending" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.pending" /></td>
           				<td><s:checkbox name="param.statusUnmatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.unmatched" /></td>
           				<td><s:checkbox name="param.statusAmountUnmatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.amount_unmatched" /></td>
           				<td><s:checkbox name="param.statusMatchedByDn" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.matchedByDn" /></td>
           				<td></td>
           				<td rowspan="2" width="100"></td>
             		</tr>
             		<tr>
             			<td><s:checkbox name="param.statusPriceUnmatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.price_unmatched" /></td>
           				<td><s:checkbox name="param.statusQtyUnmatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.qty_unmatched" /></td>
           				<td><s:checkbox name="param.statusInsInv" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.insufficient_inv" /></td>
           				<td><s:checkbox name="param.statusOutdated" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.outdated" /></td>
             			<td><s:checkbox name="param.statusMatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.matched" /></td>
             		</tr>
             		<s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/saveClose.action')" >
	             		<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
	             			<tr>
	             				<td><s:text name="pigd.summary.searcheara.pendingForClosing" /></td>
             					<td>:</td>
             					<td><s:checkbox id="param.pendingForClosing" name="param.pendingForClosing" data-dojo-type="dijit.form.CheckBox" onclick="clickPendingForClosing('param.pendingForClosing')" theme="simple"/></td>
             				</tr>
	             		</s:if>
	             		<s:else>
	             			<tr>
		             			<td><s:text name="pigd.summary.searcheara.pendingForClosing" /></td>
	             				<td>:</td>
	             				<td><s:checkbox id="param.pendingForClosing" name="param.pendingForClosing" data-dojo-type="dijit.form.CheckBox" theme="simple"/></td>
							</tr>
	             		</s:else>
             		</s:if>
             		<s:if test="#session.permitUrl.contains('/poInvGrnDnMatching/initAuditPrice.action')" >
             			<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
             				<tr>
	             				<td><s:text name="pigd.summary.searcheara.priceApprove" /></td>
             					<td>:</td>
             					<td><s:checkbox id="param.priceApprove" name="param.priceApprove" data-dojo-type="dijit.form.CheckBox" theme="simple"/></td>
             				</tr>
             			</s:if>
             		</s:if>
             	</tbody>
             </table>
             <table class="commtable">
             <tbody>
             <tr>
                <td width="100">
                	<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
                		<s:text name="pigd.summary.searcheara.invStatus" />
                	</s:if>
                	<s:else>
                		<s:text name="pigd.summary.searcheara.actionStatus"/>
                	</s:else>
                </td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.invStatus" list="invStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
                <s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
                	<td><s:text name="pigd.summary.searcheara.supplierStatus" /></td>
	                <td>:</td>
	                <td>
	                    <s:select id="param.supplierStatus" data-dojo-type="dijit.form.Select" name="param.supplierStatus" list="supplierStatus" 
	                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
	                </td>
	                <td><s:text name="pigd.summary.searcheara.buyerStatus" /></td>
	                <td>:</td>
	                <td>
	                    <s:select id="param.buyerStatus" data-dojo-type="dijit.form.Select" name="param.buyerStatus" list="buyerStatus" 
	                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
	                </td>
	                <td><s:text name="pigd.summary.searcheara.revised" /></td>
	                <td>:</td>
	                <td>
	                    <s:select id="param.revised" data-dojo-type="dijit.form.Select" name="param.revised" list="revisedStatus" 
	                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
	                </td>
	            </s:if>
	            <s:if test="#session.SUPPLIER_DISPUTE != 'yes'">
	            	<td width="100"><s:text name="pigd.summary.searcheara.closed" /></td>
	                <td>:</td>
	                <td>
	                    <s:select data-dojo-type="dijit.form.Select" name="param.closed" list="closedStatus" 
	                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
	                </td>
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
             <tr>
             	<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
                <td><s:text name="pigd.summary.searcheara.priceStatus" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.priceStatus" list="priceStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
                <td><s:text name="pigd.summary.searcheara.qtyStatus" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.qtyStatus" list="qtyStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
                <td><s:text name="pigd.summary.searcheara.closed" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.closed" list="closedStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
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
        	</s:if>
        	
        	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 3 || #session.SESSION_CURRENT_USER_PROFILE.userType == 5">
        	<table class="commtable">
        	<tbody>
        		<tr>
	                <td width="100"><s:text name="pigd.summary.searcheara.buyer" /></td>
	                <td width="10">:</td>
	                <td>
	                    <s:select data-dojo-type="dijit/form/FilteringSelect" name="param.buyerOid" list="param.buyerList" 
	                        listKey="buyerOid" listValue="buyerName" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
	                </td>
	                <td width="100"><s:text name="pigd.summary.searcheara.poStoreCode" /></td>
	                <td>:</td>
	                <td><s:textfield id="param.poStoreCode" name="param.poStoreCode" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
	                	<button  data-dojo-type="dijit.form.Button" id="clearPoStoreCode" type="button" onclick="clearButton('param.poStoreCode');"><s:text name='button.clear' /></button>
	                </td>
            	</tr>
            	<s:if test="param.supplierList != null && param.supplierList.size() > 0">
            	<tr>
                	<td><s:text name="pigd.summary.searcheara.supplier" /></td>
                	<td>:</td>
                	<td>
                	   <s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.supplierOid" list="param.supplierList" 
                        	listKey="supplierOid" listValue="supplierName + ' (' + supplierCode + ')'" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
             	</tr>
            	</s:if>
	            <tr>
	                <td><s:text name="pigd.summary.searcheara.poNo" /></td>
	                <td>:</td>
	                <td><s:textfield id="param.poNo" name="param.poNo" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
	                	<button  data-dojo-type="dijit.form.Button" id="clearPoNo" type="button" onclick="clearButton('param.poNo');"><s:text name='button.clear' /></button>
	                </td>
	                <td width="100"><s:text name="pigd.summary.searcheara.poDate" /></td>
	                 <td>:</td>
	                 <td>
	                     <label for="Dfrom1"><s:text name="Value.from" /></label>
	                     <input type="text" id="param.poDateFrom" name="param.poDateFrom" 
	                         onkeydown="javascript:document.getElementById('param.poDateFrom').blur();" 
	                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                         value='<s:date name="param.poDateFrom" format="yyyy-MM-dd" />'/>
	                     <button  data-dojo-type="dijit.form.Button" id="clearPoDateFrom" type="button" onclick="clearDate('param.poDateFrom');"><s:text name='button.clear' /></button>
	                     <label for="Dto1"><s:text name="Value.to" /></label>
	                     <input type="text" id="param.poDateTo" name="param.poDateTo" 
	                     onkeydown="javascript:document.getElementById('param.poDateTo').blur();" 
	                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                     value='<s:date name="param.poDateTo" format="yyyy-MM-dd"/>'/>
	                     <button  data-dojo-type="dijit.form.Button" id="clearPoDateTo" type="button" onclick="clearDate('param.poDateTo');"><s:text name='button.clear' /></button>
	                 </td>
	            </tr>
	            <tr>
	                <td><s:text name="pigd.summary.searcheara.invNo" /></td>
	                <td>:</td>
	                <td><s:textfield id="param.invNo" name="param.invNo" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
	                	<button  data-dojo-type="dijit.form.Button" id="clearInvNo" type="button" onclick="clearButton('param.invNo');"><s:text name='button.clear' /></button>
	                </td>
	                <td><s:text name="pigd.summary.searcheara.invDate" /></td>
	                 <td>:</td>
	                 <td>
	                     <label for="Dfrom1"><s:text name="Value.from" /></label>
	                     <input type="text" id="param.invDateFrom" name="param.invDateFrom" 
	                         onkeydown="javascript:document.getElementById('param.invDateFrom').blur();" 
	                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                         value='<s:date name="param.invDateFrom" format="yyyy-MM-dd" />'/>
	                     <button  data-dojo-type="dijit.form.Button" id="clearinvDateFrom" type="button" onclick="clearDate('param.invDateFrom');"><s:text name='button.clear' /></button>
	                     <label for="Dto1"><s:text name="Value.to" /></label>
	                     <input type="text" id="param.invDateTo" name="param.invDateTo" 
	                     onkeydown="javascript:document.getElementById('param.invDateTo').blur();" 
	                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                     value='<s:date name="param.invDateTo" format="yyyy-MM-dd"/>'/>
	                     <button  data-dojo-type="dijit.form.Button" id="clearinvDateTo" type="button" onclick="clearDate('param.invDateTo');"><s:text name='button.clear' /></button>
	                 </td>
	             </tr>
	             <tr>
	             	<td><s:text name="pigd.summary.searcheara.grnNo" /></td>
	                <td>:</td>
	                <td><s:textfield id="param.grnNo" name="param.grnNo" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
	                	<button  data-dojo-type="dijit.form.Button" id="clearGrnNo" type="button" onclick="clearButton('param.grnNo');"><s:text name='button.clear' /></button>
	                </td>
	                <td><s:text name="pigd.summary.searcheara.grnDate" /></td>
	                 <td>:</td>
	                 <td>
	                     <label for="Dfrom1"><s:text name="Value.from" /></label>
	                     <input type="text" id="param.grnDateFrom" name="param.grnDateFrom" 
	                         onkeydown="javascript:document.getElementById('param.grnDateFrom').blur();" 
	                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                         value='<s:date name="param.grnDateFrom" format="yyyy-MM-dd" />'/>
	                     <button  data-dojo-type="dijit.form.Button" id="cleargrnDateFrom" type="button" onclick="clearDate('param.grnDateFrom');"><s:text name='button.clear' /></button>
	                     <label for="Dto1"><s:text name="Value.to" /></label>
	                     <input type="text" id="param.grnDateTo" name="param.grnDateTo" 
	                     onkeydown="javascript:document.getElementById('param.grnDateTo').blur();" 
	                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                     value='<s:date name="param.grnDateTo" format="yyyy-MM-dd"/>'/>
	                     <button  data-dojo-type="dijit.form.Button" id="cleargrnDateTo" type="button" onclick="clearDate('param.grnDateTo');"><s:text name='button.clear' /></button>
	                 </td>
	             </tr>
                <s:if test="#session.SUPPLIER_DISPUTE != 'yes'" >
	             <tr>
	                	<td><s:text name="pigd.summary.searcheara.dnNo" /></td>
		                <td>:</td>
		                <td><s:textfield id="param.dnNo" name="param.dnNo" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
		                	<button  data-dojo-type="dijit.form.Button" id="clearDnNo" type="button" onclick="clearButton('param.DnNo');"><s:text name='button.clear' /></button>
		                </td>
		                <td><s:text name="pigd.summary.searcheara.dnDate" /></td>
		                 <td>:</td>
		                 <td>
		                     <label for="Dfrom1"><s:text name="Value.from" /></label>
		                     <input type="text" id="param.dnDateFrom" name="param.dnDateFrom" 
		                         onkeydown="javascript:document.getElementById('param.dnDateFrom').blur();" 
		                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                         value='<s:date name="param.dnDateFrom" format="yyyy-MM-dd" />'/>
		                     <button  data-dojo-type="dijit.form.Button" id="cleardnDateFrom" type="button" onclick="clearDate('param.dnDateFrom');"><s:text name='button.clear' /></button>
		                     <label for="Dto1"><s:text name="Value.to" /></label>
		                     <input type="text" id="param.dnDateTo" name="param.dnDateTo" 
		                     onkeydown="javascript:document.getElementById('param.dnDateTo').blur();" 
		                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                     value='<s:date name="param.dnDateTo" format="yyyy-MM-dd"/>'/>
		                     <button  data-dojo-type="dijit.form.Button" id="cleardnDateTo" type="button" onclick="clearDate('param.dnDateTo');"><s:text name='button.clear' /></button>
		                 </td>
	             </tr>
                </s:if>
	             </tbody>
             </table>
             <table class="commtable">
             <tbody>
             <tr>
                 <td width="100"><s:text name="pigd.summary.searcheara.matchingDate" /></td>
                 <td>:</td>
                 <td>
                     <label for="Dfrom1"><s:text name="Value.from" /></label>
                     <input type="text" id="param.matchingDateFrom" name="param.matchingDateFrom" 
                         onkeydown="javascript:document.getElementById('param.matchingDateFrom').blur();" 
                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                         value='<s:date name="param.matchingDateFrom" format="yyyy-MM-dd" />'/>
                     <button  data-dojo-type="dijit.form.Button" id="clearmatchingDateFrom" type="button" onclick="clearDate('param.matchingDateFrom');"><s:text name='button.clear' /></button>
                     <label for="Dto1"><s:text name="Value.to" /></label>
                     <input type="text" id="param.matchingDateTo" name="param.matchingDateTo" 
                     onkeydown="javascript:document.getElementById('param.matchingDateTo').blur();" 
                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                     value='<s:date name="param.matchingDateTo" format="yyyy-MM-dd"/>'/>
                     <button  data-dojo-type="dijit.form.Button" id="clearmatchingDateTo" type="button" onclick="clearDate('param.matchingDateTo');"><s:text name='button.clear' /></button>
                 </td>
             </tr>
             </tbody>
             </table>
             <table class="commtable">
             	<tbody>
             		<tr>
             			<td width="100" rowSpan=2><s:text name="pigd.summary.searcheara.matchingStatus" /></td>
             			<td width="10" rowSpan=2>:</td>
           				<td><s:checkbox name="param.statusPending" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.pending" /></td>
           				<td><s:checkbox name="param.statusUnmatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.unmatched" /></td>
           				<td><s:checkbox name="param.statusAmountUnmatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.amount_unmatched" /></td>
           				<td><s:checkbox name="param.statusMatchedByDn" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.matchedByDn" /></td>
           				<td></td>
           				<td rowspan="2" width="100"></td>
             		</tr>
             		<tr>
             			<td><s:checkbox name="param.statusPriceUnmatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.price_unmatched" /></td>
           				<td><s:checkbox name="param.statusQtyUnmatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.qty_unmatched" /></td>
           				<td><s:checkbox name="param.statusInsInv" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.insufficient_inv" /></td>
           				<td><s:checkbox name="param.statusOutdated" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.outdated" /></td>
             			<td><s:checkbox name="param.statusMatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.matched" /></td>
             		</tr>
             	</tbody>
             </table>
             <table class="commtable">
             <tbody>
             <tr>
                <td width="100">
                	<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
                		<s:text name="pigd.summary.searcheara.invStatus" />
                	</s:if>
                	<s:else>
                		<s:text name="pigd.summary.searcheara.actionStatus"/>
                	</s:else>
                </td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.invStatus" list="invStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
                <s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
                	<td><s:text name="pigd.summary.searcheara.supplierStatus" /></td>
	                <td>:</td>
	                <td>
	                    <s:select data-dojo-type="dijit.form.Select" name="param.supplierStatus" list="supplierStatus" 
	                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
	                </td>
	                <td><s:text name="pigd.summary.searcheara.buyerStatus" /></td>
	                <td>:</td>
	                <td>
	                    <s:select data-dojo-type="dijit.form.Select" name="param.buyerStatus" list="buyerStatus" 
	                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
	                </td>
	                <td><s:text name="pigd.summary.searcheara.revised" /></td>
	                <td>:</td>
	                <td>
	                    <s:select id="param.revised" data-dojo-type="dijit.form.Select" name="param.revised" list="revisedStatus" 
	                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
	                </td>
	            </s:if>
	            
             </tr>
             <tr>
             	<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
                <td><s:text name="pigd.summary.searcheara.priceStatus" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.priceStatus" list="priceStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
                <td><s:text name="pigd.summary.searcheara.qtyStatus" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.qtyStatus" list="qtyStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
                </s:if>
                <td><s:text name="pigd.summary.searcheara.closed" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.closed" list="closedStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
             </tr>
            </tbody>
        	</table>
        	</s:if>
        	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 6 || #session.SESSION_CURRENT_USER_PROFILE.userType == 7">
             	<table class="commtable">
	        	<tbody>
	        		<tr>
		                <td width="100"><s:text name="pigd.summary.searcheara.buyer" /></td>
		                <td width="10">:</td>
		                <td>
		                    <s:select data-dojo-type="dijit/form/FilteringSelect" name="param.buyerOid" list="param.buyerList" 
		                        listKey="buyerOid" listValue="buyerName" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
		                </td>
		                <td width="100"><s:text name="pigd.summary.searcheara.poStoreCode" /></td>
		                <td>:</td>
		                <td><s:textfield id="param.poStoreCode" name="param.poStoreCode" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
		                	<button  data-dojo-type="dijit.form.Button" id="clearPoStoreCode" type="button" onclick="clearButton('param.poStoreCode');"><s:text name='button.clear' /></button>
		                </td>
	            	</tr>
	            	<tr>
		                <td><s:text name="pigd.summary.searcheara.supplierName" /></td>
		                <td>:</td>
		                <td>
		                    <s:textfield name="param.supplierName" data-dojo-type="dijit/form/TextBox" 
	                            maxlength="100" theme="simple" id="param.supplierName"/>
		                	<button  data-dojo-type="dijit.form.Button" id="clearSupplierName" type="button" onclick="clearButton('param.supplierName');"><s:text name='button.clear' /></button>
		                </td>
	                    <td><s:text name="pigd.summary.searcheara.supplierCode" /></td>
	                    <td>:</td>
	                    <td>
	                       <s:textfield id="param.supplierCode" name="param.supplierCode" data-dojo-type="dijit/form/TextBox" 
	                            maxlength="20" theme="simple"/>
	                       <button  data-dojo-type="dijit.form.Button" id="clearSupplierCode" type="button" onclick="clearButton('param.supplierCode');"><s:text name='button.clear' /></button>
	                    </td>
	                </tr>
		            <tr>
	                <td><s:text name="pigd.summary.searcheara.poNo" /></td>
	                <td>:</td>
	                <td><s:textfield id="param.poNo" name="param.poNo" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
	                	<button  data-dojo-type="dijit.form.Button" id="clearPoNo" type="button" onclick="clearButton('param.poNo');"><s:text name='button.clear' /></button>
	                </td>
	                <td width="100"><s:text name="pigd.summary.searcheara.poDate" /></td>
	                 <td>:</td>
	                 <td>
	                     <label for="Dfrom1"><s:text name="Value.from" /></label>
	                     <input type="text" id="param.poDateFrom" name="param.poDateFrom" 
	                         onkeydown="javascript:document.getElementById('param.poDateFrom').blur();" 
	                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                         value='<s:date name="param.poDateFrom" format="yyyy-MM-dd" />'/>
	                     <button  data-dojo-type="dijit.form.Button" id="clearPoDateFrom" type="button" onclick="clearDate('param.poDateFrom');"><s:text name='button.clear' /></button>
	                     <label for="Dto1"><s:text name="Value.to" /></label>
	                     <input type="text" id="param.poDateTo" name="param.poDateTo" 
	                     onkeydown="javascript:document.getElementById('param.poDateTo').blur();" 
	                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                     value='<s:date name="param.poDateTo" format="yyyy-MM-dd"/>'/>
	                     <button  data-dojo-type="dijit.form.Button" id="clearPoDateTo" type="button" onclick="clearDate('param.poDateTo');"><s:text name='button.clear' /></button>
	                 </td>
	            </tr>
	            <tr>
	                <td><s:text name="pigd.summary.searcheara.invNo" /></td>
	                <td>:</td>
	                <td><s:textfield id="param.invNo" name="param.invNo" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
	                	<button  data-dojo-type="dijit.form.Button" id="clearInvNo" type="button" onclick="clearButton('param.invNo');"><s:text name='button.clear' /></button>
	                </td>
	                <td><s:text name="pigd.summary.searcheara.invDate" /></td>
	                 <td>:</td>
	                 <td>
	                     <label for="Dfrom1"><s:text name="Value.from" /></label>
	                     <input type="text" id="param.invDateFrom" name="param.invDateFrom" 
	                         onkeydown="javascript:document.getElementById('param.invDateFrom').blur();" 
	                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                         value='<s:date name="param.invDateFrom" format="yyyy-MM-dd" />'/>
	                     <button  data-dojo-type="dijit.form.Button" id="clearinvDateFrom" type="button" onclick="clearDate('param.invDateFrom');"><s:text name='button.clear' /></button>
	                     <label for="Dto1"><s:text name="Value.to" /></label>
	                     <input type="text" id="param.invDateTo" name="param.invDateTo" 
	                     onkeydown="javascript:document.getElementById('param.invDateTo').blur();" 
	                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                     value='<s:date name="param.invDateTo" format="yyyy-MM-dd"/>'/>
	                     <button  data-dojo-type="dijit.form.Button" id="clearinvDateTo" type="button" onclick="clearDate('param.invDateTo');"><s:text name='button.clear' /></button>
	                 </td>
	             </tr>
	             <tr>
	             	<td><s:text name="pigd.summary.searcheara.grnNo" /></td>
	                <td>:</td>
	                <td><s:textfield id="param.grnNo" name="param.grnNo" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
	                	<button  data-dojo-type="dijit.form.Button" id="clearGrnNo" type="button" onclick="clearButton('param.grnNo');"><s:text name='button.clear' /></button>
	                </td>
	                <td><s:text name="pigd.summary.searcheara.grnDate" /></td>
	                 <td>:</td>
	                 <td>
	                     <label for="Dfrom1"><s:text name="Value.from" /></label>
	                     <input type="text" id="param.grnDateFrom" name="param.grnDateFrom" 
	                         onkeydown="javascript:document.getElementById('param.grnDateFrom').blur();" 
	                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                         value='<s:date name="param.grnDateFrom" format="yyyy-MM-dd" />'/>
	                     <button  data-dojo-type="dijit.form.Button" id="cleargrnDateFrom" type="button" onclick="clearDate('param.grnDateFrom');"><s:text name='button.clear' /></button>
	                     <label for="Dto1"><s:text name="Value.to" /></label>
	                     <input type="text" id="param.grnDateTo" name="param.grnDateTo" 
	                     onkeydown="javascript:document.getElementById('param.grnDateTo').blur();" 
	                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
	                     value='<s:date name="param.grnDateTo" format="yyyy-MM-dd"/>'/>
	                     <button  data-dojo-type="dijit.form.Button" id="cleargrnDateTo" type="button" onclick="clearDate('param.grnDateTo');"><s:text name='button.clear' /></button>
	                 </td>
	             </tr>
                <s:if test="#session.SUPPLIER_DISPUTE != 'yes'" >
	             <tr>
	                	<td><s:text name="pigd.summary.searcheara.dnNo" /></td>
		                <td>:</td>
		                <td><s:textfield id="param.dnNo" name="param.dnNo" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
		                	<button  data-dojo-type="dijit.form.Button" id="clearDnNo" type="button" onclick="clearButton('param.DnNo');"><s:text name='button.clear' /></button>
		                </td>
		                <td><s:text name="pigd.summary.searcheara.dnDate" /></td>
		                 <td>:</td>
		                 <td>
		                     <label for="Dfrom1"><s:text name="Value.from" /></label>
		                     <input type="text" id="param.dnDateFrom" name="param.dnDateFrom" 
		                         onkeydown="javascript:document.getElementById('param.dnDateFrom').blur();" 
		                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                         value='<s:date name="param.dnDateFrom" format="yyyy-MM-dd" />'/>
		                     <button  data-dojo-type="dijit.form.Button" id="cleardnDateFrom" type="button" onclick="clearDate('param.dnDateFrom');"><s:text name='button.clear' /></button>
		                     <label for="Dto1"><s:text name="Value.to" /></label>
		                     <input type="text" id="param.dnDateTo" name="param.dnDateTo" 
		                     onkeydown="javascript:document.getElementById('param.dnDateTo').blur();" 
		                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                     value='<s:date name="param.dnDateTo" format="yyyy-MM-dd"/>'/>
		                     <button  data-dojo-type="dijit.form.Button" id="cleardnDateTo" type="button" onclick="clearDate('param.dnDateTo');"><s:text name='button.clear' /></button>
		                 </td>
	             </tr>
                </s:if>
	             </tbody>
             </table>
             <table class="commtable">
             <tbody>
             <tr>
                 <td width="100"><s:text name="pigd.summary.searcheara.matchingDate" /></td>
                 <td>:</td>
                 <td>
                     <label for="Dfrom1"><s:text name="Value.from" /></label>
                     <input type="text" id="param.matchingDateFrom" name="param.matchingDateFrom" 
                         onkeydown="javascript:document.getElementById('param.matchingDateFrom').blur();" 
                         data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                         value='<s:date name="param.matchingDateFrom" format="yyyy-MM-dd" />'/>
                     <button  data-dojo-type="dijit.form.Button" id="clearmatchingDateFrom" type="button" onclick="clearDate('param.matchingDateFrom');"><s:text name='button.clear' /></button>
                     <label for="Dto1"><s:text name="Value.to" /></label>
                     <input type="text" id="param.matchingDateTo" name="param.matchingDateTo" 
                     onkeydown="javascript:document.getElementById('param.matchingDateTo').blur();" 
                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                     value='<s:date name="param.matchingDateTo" format="yyyy-MM-dd"/>'/>
                     <button  data-dojo-type="dijit.form.Button" id="clearmatchingDateTo" type="button" onclick="clearDate('param.matchingDateTo');"><s:text name='button.clear' /></button>
                 </td>
             </tr>
             </tbody>
             </table>
             <table class="commtable">
             	<tbody>
             		<tr>
             			<td width="100" rowSpan=2><s:text name="pigd.summary.searcheara.matchingStatus" /></td>
             			<td width="10" rowSpan=2>:</td>
           				<td><s:checkbox name="param.statusPending" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.pending" /></td>
           				<td><s:checkbox name="param.statusUnmatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.unmatched" /></td>
           				<td><s:checkbox name="param.statusAmountUnmatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.amount_unmatched" /></td>
           				<td><s:checkbox name="param.statusMatchedByDn" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.matchedByDn" /></td>
           				<td></td>
           				<td rowspan="2" width="100"></td>
             		</tr>
             		<tr>
             			<td><s:checkbox name="param.statusPriceUnmatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.price_unmatched" /></td>
           				<td><s:checkbox name="param.statusQtyUnmatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.qty_unmatched" /></td>
           				<td><s:checkbox name="param.statusInsInv" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.insufficient_inv" /></td>
           				<td><s:checkbox name="param.statusOutdated" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.outdated" /></td>
             			<td><s:checkbox name="param.statusMatched" data-dojo-type="dijit.form.CheckBox" theme="simple"/><s:text name="MatchingStatus.matched" /></td>
             		</tr>
             	</tbody>
             </table>
             <table class="commtable">
             <tbody>
             <tr>
                <td width="100">
                	<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
                		<s:text name="pigd.summary.searcheara.invStatus" />
                	</s:if>
                	<s:else>
                		<s:text name="pigd.summary.searcheara.actionStatus"/>
                	</s:else>
                </td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.invStatus" list="invStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
                <s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
                	<td><s:text name="pigd.summary.searcheara.supplierStatus" /></td>
	                <td>:</td>
	                <td>
	                    <s:select data-dojo-type="dijit.form.Select" name="param.supplierStatus" list="supplierStatus" 
	                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
	                </td>
	                <td><s:text name="pigd.summary.searcheara.buyerStatus" /></td>
	                <td>:</td>
	                <td>
	                    <s:select data-dojo-type="dijit.form.Select" name="param.buyerStatus" list="buyerStatus" 
	                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
	                </td>
	                <td><s:text name="pigd.summary.searcheara.revised" /></td>
	                <td>:</td>
	                <td>
	                    <s:select id="param.revised" data-dojo-type="dijit.form.Select" name="param.revised" list="revisedStatus" 
	                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
	                </td>
	            </s:if>
	            <s:if test="#session.SUPPLIER_DISPUTE != 'yes'" >
	            <td width="100"><s:text name="pigd.summary.searcheara.closed" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.closed" list="closedStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
	            </s:if>
             </tr>
             <tr>
             	<s:if test="#session.SUPPLIER_DISPUTE == 'yes'" >
                <td><s:text name="pigd.summary.searcheara.priceStatus" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.priceStatus" list="priceStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
                <td><s:text name="pigd.summary.searcheara.qtyStatus" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.qtyStatus" list="qtyStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
                <td><s:text name="pigd.summary.searcheara.closed" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.closed" list="closedStatus" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
                </s:if>
             </tr>
            </tbody>
        	</table>
             </s:if>
        </form>
    </div>
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name="pigd.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" class="grid"></div>
</body>
</html>
