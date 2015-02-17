<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script>
    require(
            [
             "custom/B2BPortalBase",
             "dojo/dom",
             "dojo/dom-style",
             "dijit/registry",
             "dojo/on",
             "dojo/date",
             "dojo/parser",
             "dojo/_base/xhr",
             "dijit/Tooltip",
             "dijit/form/NumberTextBox",
             "dojo/_base/lang",
             "dojo/data/ItemFileWriteStore",
             "custom/CustomDateTextBox",
             "dojo/query",
             "dojo/json",
             "dojo/domReady!"
             ], 
             function(
                 B2BPortalBase,
                 dom,
                 domStyle,
                 registry,
                 on,
                 date,
                 parser,
                 xhr,
                 Tooltip,
                 NumberTextBox,
                 lang,
                 ItemFileWriteStore,
                 CustomDateTextBox,
                 query,
                 JSON
                 )
             {
                parser.parse();
                
                (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');

                if (dom.byId("cancelBtn"))
                {
                    on(registry.byId("cancelBtn"), 'click', function()
                    {
                        changeToURL('<s:url value="/grn/init.action" />?keepSp=Y');
                    }); 
                }
             });

    </script>
</head>

<body>
	<!-- Content Part -->
	<div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
       	<!-- action button part -->
       	<div class="summaryBtn">
       		<div style="float: right;">
	    		<button data-dojo-type="dijit/form/Button" id="cancelBtn" ><s:text name="button.cancel"/></button>
	    	</div>
       	</div>
       	<!-- here is message area -->
    	<div id="errorMsg">
		</div>
       	<!-- dispute Part -->
       	<div data-dojo-type="dijit.TitlePane" 
       		data-dojo-props="title:'<s:text name="grn.dispute.header"/>', width:275" 
       		open="true" style="width:100%;">
       		<div style="width:100%">
       			<table class="tablestyle2" style="table-layout: fixed;">
					<tbody>
						<tr class="odd">
							<td class="tdLabel"><s:text name="grn.header.grnNo"/></td>
							<td class="tdData"><s:property value="grn.header.grnNo"/></td>
							<td class="tdLabel"><s:text name="grn.header.grnDate"/></td>
							<td class="tdData"><s:property value="grn.header.grnDate"/></td>
						</tr>
						<tr class="even">
							<td class="tdLabel"><s:text name="grn.header.poNo"/></td>
							<td class="tdData"><s:property value="grn.header.poNo"/></td>
							<td class="tdLabel"><s:text name="grn.header.poDate"/></td>
							<td class="tdData"><s:property value="grn.header.poDate"/></td>
						</tr>
						<tr class="odd">
							<td class="tdLabel"><s:text name="grn.header.buyerCode"/></td>
							<td class="tdData"><s:property value="grn.header.buyerCode"/></td>
							<td class="tdLabel"><s:text name="grn.header.buyerName"/></td>
							<td class="tdData"><s:property value="grn.header.buyerName"/></td>
						</tr>
						<tr class="even">
							<td class="tdLabel"><s:text name="grn.header.supplierCode"/></td>
							<td class="tdData"><s:property value="grn.header.supplierCode"/></td>
							<td class="tdLabel"><s:text name="grn.header.supplierName"/></td>
							<td class="tdData"><s:property value="grn.header.supplierName"/></td>
						</tr>
						<tr class="odd">
							<td class="tdLabel"><s:text name="grn.header.disputeSupplierBy"/></td>
							<td class="tdData" ><s:property value="grn.header.disputeSupplierBy" /></td>
							<td class="tdLabel"><s:text name="grn.header.disputeSupplierDate"/></td>
							<td class="tdData" ><s:property value="grn.header.disputeSupplierDate" /></td>
						</tr>
						<tr class="even">
							<td class="tdLabel"><s:text name="grn.header.supplierRemarks"/></td>
							<td class="tdData" colspan=3 style="WORD-WRAP: break-word">
							<div><s:property value="grn.header.disputeSupplierRemarks" /></div>
							</td>
						</tr>
						<tr class="odd">
							<td class="tdLabel"><s:text name="grn.header.disputeBuyerBy"/></td>
							<td class="tdData" ><s:property value="grn.header.disputeBuyerBy" /></td>
							<td class="tdLabel"><s:text name="grn.header.disputeBuyerDate"/></td>
							<td class="tdData" ><s:property value="grn.header.disputeBuyerDate" /></td>
						</tr>
						<tr class="even">
							<td class="tdLabel"><s:text name="grn.header.buyerRemarks"/></td>
							<td class="tdData" colspan=3 style="WORD-WRAP: break-word">
								<div><s:property value="grn.header.disputeBuyerRemarks" /></div>
							</td>
						</tr>
					</tbody>
				</table>
       		</div>
        	
       	</div>
       	
       	<div class="space"></div>
       	<div data-dojo-type="dijit.TitlePane" 
       		data-dojo-props="title:'<s:text name="grn.dispute.detail"/>', width:275" 
       		open="true" style="width:100%;">
       		<div style="width:100%">
       			<table class="tablestyle1">
					<thead>
						<tr>
							<th width="2%"><s:text name="grn.detail.seq"/></th>
							<th width="6%"><s:text name="grn.detail.itemNo"/></th>
							<th width="8%"><s:text name="grn.detail.itemDesc"/></th>
							<th width="9%"><s:text name="grn.detail.barcode"/></th>
							<th width="8%"><s:text name="grn.detail.receivedQty"/></th>
							<th width="9%"><s:text name="grn.detail.deliveryQty"/></th>
							
						</tr>
					</thead>
					<tbody>
						<s:iterator value="grn.details" id="detail" status="stat">
						<tr id="<s:property value="#stat.index+1"/>" >
							<td><s:property value="#stat.index+1"/></td>
							<td><s:property value="#detail.buyerItemCode"/></td>
							<td><s:property value="#detail.itemDesc"/></td>
							<td><s:property value="#detail.barcode"/></td>
							<s:if test="(#detail.deliveryQty) == null">
								<td><s:property value="#detail.receiveQty"/></td>
								<td><s:property value="#detail.receiveQty"/></td>
							</s:if>
							<s:else>
								<s:if test="(#detail.deliveryQty) == (#detail.receiveQty)">
									<td><s:property value="#detail.receiveQty"/></td>
									<td><s:property value="#detail.deliveryQty"/></td>
								</s:if>
								<s:else>
									<td style="color:red"><s:property value="#detail.receiveQty"/></td>
									<td style="color:red"><s:property value="#detail.deliveryQty"/></td>
								</s:else>
							</s:else>
						</tr>	
						</s:iterator>
					</tbody>
				</table>
       		</div>
       	</div>
	</div>
</body>
</html>
