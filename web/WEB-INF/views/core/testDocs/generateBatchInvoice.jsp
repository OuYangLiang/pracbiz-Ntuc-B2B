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
    html, body {
        width: 100%;
        height: 100%;
        margin: 0;
        overflow:hidden;
    }
    #borderContainer {
        width: 100%;
        height: 100%;
    }
 
    
    .loadingMessage {
        margin:0 auto;
        width: 10px;
        background: #fff url('<s:url value="/js/dojo-root/dijit/themes/claro/images/loadingAnimation.gif" />') no-repeat 10px 23px;
        padding: 25px 40px;
        color: #999;
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
                 "dijit/Tooltip",
                 "dojo/dom-style",
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
                     Tooltip,
                     domStyle,
                     CustomSummaryGrid
                     )
                 {
                    parser.parse();
                
                    (new B2BPortalBase()).init(
                        '<c:out value="${session.helpExHolder.helpNo}"/>',
                        '<c:out value="${session.helpExHolder.helpEmail}"/>',
                        '<s:property value="#session.commonParam.timeout" />',
                        '<s:url value="/logout.action" />');
                
                    processGenerateBatchInvoice = function()
                    {
                    	dom.byId('ResultDiv').style.display='none';
                    	dom.byId('loadingOverlay1').style.display='';
                    	var csrfToken = document.getElementById("csrfToken").value;
                        xhr.post({
                            url: '<s:url value="/testDocs/saveGenerateBatchInvoice.action" />',
                            content: {"csrfToken": csrfToken},
                            handleAs: "json",
                            load: function(data)
                            {
                            	dom.byId('ResultDiv').style.display='';
                                dom.byId('div1').innerHTML='<ul><li><h3>'+data.message+'</h3></li></ul>';
                                dom.byId('loadingOverlay1').style.display='none';
                                on.once(registry.byId("generateBatchInvoiceBtn"), 'click', generateBatchInvoice);
                            }
                        });
                    }
                    
                    generateBatchInvoice = function()
                    {
                        (new B2BPortalBase()).resetTimeout(
                            '<s:property value="#session.commonParam.timeout" />',
                            '<s:url value="/logout.action" />');
                        
                        var csrfToken = document.getElementById("csrfToken").value;
                        var validPoFrom = dijit.byId("param.createDateFrom").getValue();
                        var validPoTo = dijit.byId("param.createDateTo").getValue();
                        if(null != validPoTo && new Date(validPoFrom) > new Date(validPoTo))
                        {
                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1701' />"});
                            infoDialog.show();
                            on.once(registry.byId("generateBatchInvoiceBtn"), 'click', generateBatchInvoice);
                            return;
                        }
                       
                        xhr.post({
                            url: '<s:url value="/testDocs/checkGenerateBatchInvoice.action" />',
                            form: dom.byId("searchForm"),
                            handleAs: "json",
                            load: function(data)
                            {
                                if (data.poCount == 0)
                                {
                                    (new InformationDialog({message: data.message})).show();
                                    on.once(registry.byId("generateBatchInvoiceBtn"), 'click', generateBatchInvoice);
                                }
                                else
                                {
                                    var confirmDialog = new ConfirmDialog({
                                        message: data.message,
                                        yesBtnPressed: processGenerateBatchInvoice,
                                        noBtnPressed: function()
                                        {
                                        	on.once(registry.byId("generateBatchInvoiceBtn"), 'click', generateBatchInvoice);
                                        }
                                    });
                                    confirmDialog.show();
                                }
                            }
                        });
                    }
                    
                    if (dom.byId("generateBatchInvoiceBtn"))
                    {
                        on.once(registry.byId("generateBatchInvoiceBtn"), 'click', generateBatchInvoice);
                    }
                    
                    clearButton = function(src)
                    {
                        var registry = require("dijit/registry");
                        registry.byId(src).attr("aria-valuenow", null);
                        registry.byId(src).attr("value", null);
                    }
                });

    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <s:if test="#session.permitUrl.contains('/testDocs/initGenerateBatchInvoice.action')" >
                <button data-dojo-type="dijit/form/Button" id="generateBatchInvoiceBtn" ><s:text name="button.generate.batch.einvoice" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>
    
    <!-- Search Area -->
    <div id="errorMsg">
    </div>
    
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="Generate Batch E-Invoice" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
        <table class="commtable">
            <tbody>
                <tr>
                    <td width="85">Create Date</td>
                    <td>:</td>
                    <td>
                        <label for="Dfrom1"><s:text name="Value.from" /></label>
                        <input type="text" id="param.createDateFrom" name="param.createDateFrom" 
                            onkeydown="javascript:document.getElementById('param.createDateFrom').blur();" 
                            data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
                            value='<s:date name="param.createDateFrom" format="dd/MM/yyyy" />'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearcreateDateFrom" type="button" onclick="clearButton('param.createDateFrom');"><s:text name='button.clear' /></button>
                        <label for="Dto1"><s:text name="Value.to" /></label>
                        <input type="text" id="param.createDateTo" name="param.createDateTo" 
                        onkeydown="javascript:document.getElementById('param.createDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
                        value='<s:date name="param.createDateTo" format="dd/MM/yyyy"/>'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearcreateDateTo" type="button" onclick="clearButton('param.createDateTo');"><s:text name='button.clear' /></button>
                    </td>
                </tr>
             </tbody>
        </table>
        </form>
    </div>
    
    <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
          <div id="loadingOverlay1" style="display:none;">
            <div style="text-align:center">
            <div class="loadingMessage">Processing...</div>
            </div>
          </div>
     </div>
     <div id="ResultDiv" style="display:none;">
          <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'Process Result', width:275">
               <div id="div1"></div>
         </div>
     </div>
</body>
</html>
