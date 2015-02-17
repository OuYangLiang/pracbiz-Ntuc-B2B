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
             "dojo/_base/lang",
             "dojo/data/ItemFileWriteStore",
             "dojo/query",
             "dojo/json",
             "dojox/data/QueryReadStore",
             "dojox/grid/EnhancedGrid",
             "dojox/grid/enhanced/plugins/Pagination",
             "dojox/grid/enhanced/plugins/IndirectSelection",
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
                 lang,
                 ItemFileWriteStore,
                 query,
                 JSON,
                 QueryReadStore,  
                 EnhancedGrid,
                 Pagination,
                 IndirectSelection
                 )
             {
                parser.parse();

                function stopRefresh()
    			{
    				dojo.connect(dojo.doc, 'onkeydown',function(e)
            		{
    					if ((e.keyCode == dojo.keys.F5 && !e.ctrlKey) 
            					|| (e.keyCode == 82 && e.ctrlKey) ) 
        				{
          			        dojo.stopEvent(e);
          			        return false;
          			    }
    				});
    			};
    			stopRefresh();
    			
                (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');

                if (dom.byId("cancelBtn"))
                {
                    on(registry.byId("cancelBtn"), 'click', function()
                    {
                        changeToURL('<s:url value="/quickPo/init.action" />?keepSp=Y');
                    }); 
                }

                if (dom.byId("saveBtn"))
                {
                    on(registry.byId("saveBtn"), 'click', function()
                    {
                        var poOids = document.getElementsByName("poOids");
                        var oids = "";
                        dojo.forEach(poOids, function(item)
                        {
                           oids = oids + item.value + '-';
                        });
                        var csrfToken = dom.byId("csrfToken").value;
                        xhr.get({
                            url: '<s:url value="/quickPo/putParamIntoSession.action" />',
                            content: {selectedOids: oids},
                            load: function(data)
                            {
                            	if (data!='x')
                                {
                                    (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                                }
                                else
                                {
                                	submitForm("tokenForm",'<s:url value="/quickPo/saveBatchGenerateInv.action" />?op=S&csrfToken=' + csrfToken);
                                }
                           }
                     	});
                    }); 
                }

                if (dom.byId("saveAndSentBtn"))
                {
                    on(registry.byId("saveAndSentBtn"), 'click', function()
                    {
                        var poOids = document.getElementsByName("poOids");
                        var oids = "";
                        dojo.forEach(poOids, function(item)
                        {
                           oids = oids + item.value + '-';
                        });

                        var csrfToken = dom.byId("csrfToken").value;
                        xhr.get({
                            url: '<s:url value="/quickPo/putParamIntoSession.action" />',
                            content: {selectedOids: oids},
                            load: function(data)
                            {
                            	if (data!='x')
                                {
                                    (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                                }
                                else
                                {
                                	 submitForm("tokenForm",'<s:url value="/quickPo/saveBatchGenerateInv.action" />?op=CS&csrfToken=' + csrfToken);
                                }
                           }
                     	});
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
       			<button data-dojo-type="dijit/form/Button" id="saveBtn" ><s:text name="button.save"/></button>
       			<button data-dojo-type="dijit/form/Button" id="saveAndSentBtn" ><s:text name="button.saveAndSent"/></button>
	    		<button data-dojo-type="dijit/form/Button" id="cancelBtn"><s:text name="button.cancel"/></button>
	    	</div>
       	</div>
       	
       	<div>
       		<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
       	</div>
       	<!-- dispute Part -->
       	<div data-dojo-type="dijit.TitlePane" 
       		data-dojo-props="title:'<s:text name="quick.po.generator.invoice"/>', width:275" >
       		<div style="width:100%">
       			<table class="tablestyle1" style="table-layout: fixed;word-wrap : break-word;">
       				<thead>
      					<tr>
      						<th class="tdLabel" width='40'><s:text name="quick.po.generator.invoice.seq"/></th>
      						<th class="tdLabel"><s:text name="quick.po.generator.invoice.supplierCode"/></th>
							<th class="tdLabel"><s:text name="quick.po.generator.invoice.buyerCode"/></th>
       						<th class="tdLabel"><s:text name="quick.po.generator.invoice.poNo"/></th>
							<th class="tdLabel"><s:text name="quick.po.generator.invoice.poDate"/></th>
							<th class="tdLabel"><s:text name="quick.po.generator.invoice.deliveryDate"/></th>
							<th class="tdLabel"><s:text name="quick.po.generator.invoice.storeCode"/></th>
							<th class="tdLabel"><s:text name="quick.po.generator.invoice.invNo"/></th>
							<th class="tdLabel"><s:text name="quick.po.generator.invoice.invDate"/></th>
							<th class="tdLabel"><s:text name="quick.po.generator.invoice.invAmountNoVat"/></th>
							<th class="tdLabel"><s:text name="quick.po.generator.invoice.vatAmount"/></th>
							<th class="tdLabel"><s:text name="quick.po.generator.invoice.totalPay"/></th>
      					</tr>
       				</thead>
					<tbody>
						<s:iterator value="invoiceList" id="inv" status="stat">
						<tr>
							<input type="hidden" name="poOids" value='<s:property value="#inv.header.poOid"/>'/>
							<td class="tdData">&nbsp;<s:property value="#stat.index+1"/></td>
							<td class="tdData">&nbsp;<s:property value="#inv.header.supplierCode"/></td>
							<td class="tdData">&nbsp;<s:property value="#inv.header.buyerCode"/></td>
							<td class="tdData">&nbsp;<s:property value="#inv.header.poNo"/></td>
							<td class="tdData">&nbsp;<s:property value="#inv.header.poDate"/></td>
							<td class="tdData">&nbsp;<s:property value="#inv.header.deliveryDate"/></td>
							<td class="tdData">&nbsp;<s:property value="#inv.header.shipToCode"/></td>
							<td class="tdData">&nbsp;<s:property value="#inv.header.invNo"/></td>
							<td class="tdData">&nbsp;<s:property value="#inv.header.invDate"/></td>
							<td class="tdData">&nbsp;<s:property value="#inv.header.invAmountNoVat"/></td>
							<td class="tdData">&nbsp;<s:property value="#inv.header.vatAmount"/></td>
							<td class="tdData">&nbsp;<s:property value="#inv.header.totalPay"/></td>
						</tr>
						</s:iterator>
					</tbody>
				</table>
       		</div>
       	</div>
	</div>
	
	<form id="tokenForm" action="" method="post">
		<s:token></s:token>
	</form>
</body>
</html>
