<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <style type="text/css">
    
    </style>
        
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dijit/registry",
                "dojo/on",
                "dojo/string",
                "dojo/_base/xhr",
                "dojo/parser",
                "dijit/form/ValidationTextBox",
                "dijit/form/MultiSelect",
                "custom/ConfirmDialog",
                "dijit/form/CheckBox",
                "custom/InformationDialog",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    string,
                    xhr,
                    parser,
                    ValidationTextBox,
                    MultiSelect,
                    ConfirmDialog,
                    CheckBox,
                    InformationDialog
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                    
                    
                    on(registry.byId("saveBtn"), 'click', 
                        function()
                        {
                            
                            submitForm('mainForm', '<s:url value="/dn/saveAdd.action" />'+'?dnHeaderEx.buyerOid=<s:property value="dnHeaderEx.buyerOid"/>');
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/dn/initAdd.action" />');
                        }
                    );

                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                            changeToURL('<s:url value="/dn/init.action?keepSp=Y" />');
                        }
                    });
                    on(registry.byId("cancelBtn"), 'click', 
                         function()
                         {
                             confirmDialog.show();
                         }
                     );
                });
        function clearDate(src)
        {
            var registry = require("dijit/registry");
            registry.byId(src).attr("aria-valuenow", null);
            registry.byId(src).attr("value", null);
        }

        function selectSuppBtn()
        {
        	var url = '<s:url value="/popup/popupViewSupplierForDn.action" />?supplier.buyerOid=<s:property value="dnHeaderEx.buyerOid"/>';
            window.open (url,'','width=800,height=600,scrollbars=1');
        }

        var index = 0;
        function add(flag)
        {
            var sourceTable = document.getElementById("sourceTable");
            var allTbody = sourceTable.getElementsByTagName("tbody");
            var div = document.getElementById("targetDiv");
            var div1 = document.createElement("div");
            div1.id="itemDiv"+index;
            var s = '<table width="100%"><tbody id="tbody'+index+'">'
					       +'<tr>'
					            +'<td rowspan="2" width="7%"><input style="width: 100%;" name="dnDetailExs['+index+'].lineSeqNoVal" id="dnDetailExs['+index+'].lineSeqNoVal" onblur="checkSeq('+index+');" data-dojo-type="dijit.form.ValidationTextBox" maxlength="11"/></td>'
					            +'<td width="14%"><input style="width: 100%;" name="dnDetailExs['+index+'].buyerItemCode" data-dojo-type="dijit.form.ValidationTextBox" maxlength="20"/></td>'
					            +'<td width="19%"><input style="width: 100%;" name="dnDetailExs['+index+'].supplierItemCode" data-dojo-type="dijit.form.ValidationTextBox" maxlength="20"/></td>'
					            +'<td width="19%"><input style="width: 100%;" name="dnDetailExs['+index+'].barcode" data-dojo-type="dijit.form.ValidationTextBox" maxlength="50"/></td>'
					            +'<td width="12%"><input style="width: 100%;" name="dnDetailExs['+index+'].itemDesc" data-dojo-type="dijit.form.ValidationTextBox" maxlength="50"/></td>'
					            +'<td width="10%"><input style="width: 100%;" name="dnDetailExs['+index+'].orderUom" data-dojo-type="dijit.form.ValidationTextBox" maxlength="20"/></td>'
					            +'<td width="13%"><input style="width: 100%;" name="dnDetailExs['+index+'].debitQtyVal" id="dnDetailExs['+index+'].debitQtyVal" onblur="checkDebitQty('+index+')" data-dojo-type="dijit.form.ValidationTextBox" maxlength="9"/></td>'
					            +'<td rowspan="2"><button  type="button" data-dojo-type="dijit/form/Button" onclick="remove('+index+');"><s:text name="button.delete" /></button></td>'
					        +'</tr>'
					        +'<tr>'
					            +'<td><input style="width: 100%;" name="dnDetailExs['+index+'].unitCostVal" id="dnDetailExs['+index+'].unitCostVal" onblur="checkUnitCost('+index+')" data-dojo-type="dijit.form.ValidationTextBox" maxlength="10"/></td>'
					            +'<td><input style="width: 100%;" name="dnDetailExs['+index+'].costDiscountAmountVal" data-dojo-type="dijit.form.ValidationTextBox" maxlength="10"/></td>'
					            +'<td><input style="width: 100%;" name="dnDetailExs['+index+'].costDiscountPercentVal" data-dojo-type="dijit.form.ValidationTextBox" maxlength="2"/></td>'
					            +'<td><input style="width: 100%;" name="dnDetailExs['+index+'].netUnitCostVal" data-dojo-type="dijit.form.ValidationTextBox" maxlength="10"/></td>'
					            +'<td><input style="width: 100%;" name="dnDetailExs['+index+'].itemCostVal" id="dnDetailExs['+index+'].itemCostVal" readonly="readonly" data-dojo-type="dijit.form.ValidationTextBox" maxlength="10"/></td>'
					            +'<td><input style="width: 100%;" name="dnDetailExs['+index+'].itemRemarks" data-dojo-type="dijit.form.ValidationTextBox" maxlength="100"/></td>'
					        +'</tr>'
                        +'</tbody></table>';
            div1.innerHTML=s;
            div.appendChild(div1);
            if (flag)
            {
	            var parser = require("dojo/parser");
	            parser.parse(sourceTable);
            }
            index ++;
        }

        
        function remove(index)
        {
            var xhr = require("dojo/_base/xhr");
            var ConfirmDialog = require("custom/ConfirmDialog");
            var InformationDialog = require("custom/InformationDialog");

            var confirmDialog = new ConfirmDialog({
                message: '<s:text name="alert.delete.records" />',
                yesBtnPressed: function(){
                	var targetDiv = document.getElementById("targetDiv");
                    var allTbody = sourceTable.getElementsByTagName("tbody");
                    if(allTbody.length==2)
                    {
                        var infoDialog = new InformationDialog({message: '<s:text name="B2BPC0339"/>'});
                        infoDialog.show();
                        return;
                    }
                    var itemDiv = document.getElementById('itemDiv'+index);
                    targetDiv.removeChild(itemDiv);
                }
            });
            
            confirmDialog.show();
            
        }

        function checkVatRate()
        {
        	var string = require("dojo/string");
        	var InformationDialog = require("custom/InformationDialog");
            var vatRateVal = string.trim(document.getElementById("dnHeaderEx.vatRateVal").value);
            var vatRate = parseFloat(new Number(vatRateVal));
            if (vatRateVal == '' || isNaN(vatRate) || vatRate < 0 || vatRate > 100)
            {
            	var infoDialog = new InformationDialog({message: "vatRate must be a digital number between 0 and 100."});
                infoDialog.show();
                document.getElementById("dnHeaderEx.vatRateVal").value = '0';
                vatRate = 0.00;
            }
            calTotalVat();
        }

        function checkSeq(index)
        {
        	var string = require("dojo/string");
        	var InformationDialog = require("custom/InformationDialog");
        	var seqVal = string.trim(document.getElementById("dnDetailExs["+index+"].lineSeqNoVal").value);
        	var seq = parseInt(new Number(seqVal));
        	if (seqVal == '' || isNaN(seq) || seq < 0)
            {
                var infoDialog = new InformationDialog({message: "seqNo must be a positive digital number"});
                infoDialog.show();
                document.getElementById("dnDetailExs["+index+"].lineSeqNoVal").value = '';
            }
        }

        function checkDebitQty(index)
        {
        	var string = require("dojo/string");
        	var InformationDialog = require("custom/InformationDialog");
            var debitQtyVal = string.trim(document.getElementById("dnDetailExs["+index+"].debitQtyVal").value);
            var debitQty = parseFloat(new Number(debitQtyVal));
            if (debitQtyVal == '' || isNaN(debitQty))
            {
                var infoDialog = new InformationDialog({message: "debitQty must be a digital number"});
                infoDialog.show();
                document.getElementById("dnDetailExs["+index+"].debitQtyVal").value = '0.00';
            }
            else if (!(debitQty >= parseFloat(new Number(0)) && debitQty <= parseFloat(new Number(999999.99))))
            {
                var infoDialog = new InformationDialog({message: "debitQty is invalid, exceed the range(0 - 999999.99)."});
                infoDialog.show();
        		document.getElementById("dnDetailExs["+index+"].debitQtyVal").value = '0.00';
            }
            else
            {
                document.getElementById("dnDetailExs["+index+"].debitQtyVal").value = parseFloat(new Number(debitQtyVal)).toFixed(2);
            }
            calItemCost(index);
        }

        function checkUnitCost(index)
        {
        	var string = require("dojo/string");
        	var InformationDialog = require("custom/InformationDialog");
            var unitCostVal = string.trim(document.getElementById("dnDetailExs["+index+"].unitCostVal").value);
            var unitCost = parseFloat(new Number(unitCostVal));
            if (unitCostVal == '' || isNaN(unitCost))
            {
                var infoDialog = new InformationDialog({message: "unitCost must be a decimal number"});
                infoDialog.show();
                document.getElementById("dnDetailExs["+index+"].unitCostVal").value = '0.00';
            }
            else if (!(unitCost >= parseFloat(new Number(0)) && unitCost <= parseFloat(new Number(999999.99))))
            {
                var infoDialog = new InformationDialog({message: "unitCost is invalid, exceed the range(0 - 99999999999.99)."});
                infoDialog.show();
                document.getElementById("dnDetailExs["+index+"].unitCostVal").value = '0.00';
            }
            else
            {
                document.getElementById("dnDetailExs["+index+"].unitCostVal").value = parseFloat(new Number(unitCost)).toFixed(2);
            }
            calItemCost(index);
        }

        function calTotalCost()
        {
            var sourceTable = document.getElementById("sourceTable");
            var allTbody = sourceTable.getElementsByTagName("tbody");
            var totalCost = 0;
            for (var i=0; i < allTbody.length; i++)
            {
            	if (allTbody[i].id == '')
            	{
            		continue;
            	}
                var itemCost = parseFloat(new Number(allTbody[i].getElementsByTagName("input")[24].value));
                if (allTbody[i].getElementsByTagName("input")[24].value.trim == '' || isNaN(itemCost))
                {
                    allTbody[i].getElementsByTagName("input")[24].value = '0.00';
                    itemCost = 0.00;
                }
                totalCost += itemCost;
            }
            document.getElementById("dnHeaderEx.totalCost").value = forDight(totalCost,2);
            calTotalVat();
            calTotalCostWithVat();
        }

        function calTotalVat()
        {
        	var string = require("dojo/string");
            var totalCostVal = string.trim(document.getElementById("dnHeaderEx.totalCost").value);
            var vatRateVal = string.trim(document.getElementById("dnHeaderEx.vatRateVal").value);
            var totalCost = parseFloat(new Number(totalCostVal));
            var vatRate = parseFloat(new Number(vatRateVal));
            if (totalCostVal == '' || isNaN(totalCost))
            {
                totalCost = 0.00;
            }
            if (vatRateVal == '' || isNaN(vatRate) || vatRate < 0 || vatRate > 100)
            {
                document.getElementById("dnHeaderEx.vatRateVal").value = '0';
                vatRate = 0.00;
            }
            document.getElementById("dnHeaderEx.totalVat").value = forDight(totalCost*vatRate/100,2);
            calTotalCostWithVat();
        }

        function calTotalCostWithVat()
        {
        	var string = require("dojo/string");
            var totalCostVal = string.trim(document.getElementById("dnHeaderEx.totalCost").value);
            var totalVatVal = string.trim(document.getElementById("dnHeaderEx.totalVat").value);
            var totalCost = parseFloat(new Number(totalCostVal));
            var totalVat = parseFloat(new Number(totalVatVal));
            if (totalCostVal == '' || isNaN(totalCost))
            {
                document.getElementById("dnHeaderEx.totalCost").value = '0.00';
                totalCost = 0.00;
            }
            if (totalVatVal == '' || isNaN(totalVat))
            {
                document.getElementById("dnHeaderEx.totalVat").value = '0.00';
                totalVat = 0.00;
            }
            document.getElementById("dnHeaderEx.totalCostWithVat").value = forDight(totalCost+totalVat,2);
        }

        function calItemCost(index)
        {
            var string = require("dojo/string");
            var debitQtyVal = string.trim(document.getElementById("dnDetailExs["+index+"].debitQtyVal").value);
            var unitCostVal = string.trim(document.getElementById("dnDetailExs["+index+"].unitCostVal").value);
            var debitQty = parseFloat(new Number(debitQtyVal));
            var unitCost = parseFloat(new Number(unitCostVal));
            if (debitQtyVal == '' || isNaN(debitQty))
            {
                document.getElementById("dnDetailExs["+index+"].debitQtyVal").value = '0.00';
                debitQty = 0;
            }
            if (unitCostVal == '' || isNaN(unitCost))
            {
                document.getElementById("dnDetailExs["+index+"].unitCostVal").value = '0.00';
                unitCost = 0.00;
            }
            document.getElementById("dnDetailExs["+index+"].itemCostVal").value = forDight(debitQty*unitCost,2);
            calTotalCost();
        }

        function forDight(Dight,How)    
        {    
        	Dight  =  parseFloat(Math.round(Dight*Math.pow(10,How))/Math.pow(10,How)).toFixed(How);    
           return  Dight;    
        } 
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button type="button" data-dojo-type="dijit.form.Button" id="saveBtn" ><s:text name="button.save" /></button>
            <button type="button" data-dojo-type="dijit.form.Button" id="resetBtn" ><s:text name="button.reset" /></button>
            <button type="button" data-dojo-type="dijit.form.Button" id="cancelBtn" ><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    
    <div class="pageBar">
        <div class="title"><s:text name="dn.create"/></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <s:form id="mainForm" name="mainForm"   theme="simple"  method="post" enctype="multipart/form-data" >
    <input type="hidden" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token></s:token>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="dn.edit.header.name" />', width:275" style="width:99%">
        <table class="tablestyle2">
        <tbody>
            <tr class="odd">
                 <td class="tdLabel"><s:text name="dn.edit.header.dnNo"/></td>
                 <td class="tdData"><s:textfield id="dnHeaderEx.dnNo" name="dnHeaderEx.dnNo" data-dojo-type="dijit/form/TextBox" maxlength="15" theme="simple"/></td>
                 <td class="tdLabel"><s:text name="dn.edit.header.dnType"/></td>
                 <td class="tdData"><s:select data-dojo-type="dijit/form/Select" name="dnHeaderEx.dnType" list="dnTypes" listKey="key" listValue="value" 
                      headerKey="" headerValue="%{getText('select.all')}" theme="simple"/></td>
             </tr>
             <tr class="even">
                 <td class="tdLabel"><s:text name="dn.edit.header.dnDate"/></td>
                 <td class="tdData">
                    <input type="text" id="dnHeaderEx.dnDate" name="dnHeaderEx.dnDate" 
                            onkeydown="javascript:document.getElementById('dnHeaderEx.dnDate').blur();" 
                            data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                            value='<s:date name="dnHeaderEx.dnDate" format="yyyy-MM-dd" />'/>
                    <button  data-dojo-type="dijit.form.Button" type="button" onclick="clearDate('dnHeaderEx.dnDate');"><s:text name='button.clear' /></button>
                 </td>
                 <td class="tdLabel"><s:text name="dn.edit.header.poNo"/></td>
                 <td class="tdData"><s:textfield name="dnHeaderEx.poNo" data-dojo-type="dijit/form/TextBox" maxlength="20" theme="simple"/></td>
             </tr>
            <tr class="odd">
                 <td class="tdLabel"><s:text name="dn.edit.header.poDate"/></td>
                 <td class="tdData">
                    <input type="text" id="dnHeaderEx.poDate" name="dnHeaderEx.poDate" 
                            onkeydown="javascript:document.getElementById('dnHeaderEx.poDate').blur();" 
                            data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                            value='<s:date name="dnHeaderEx.poDate" format="yyyy-MM-dd" />'/>
                    <button  data-dojo-type="dijit.form.Button" type="button" onclick="clearDate('dnHeaderEx.poDate');"><s:text name='button.clear' /></button>
                 </td>
                 <td class="tdLabel"><s:text name="dn.edit.header.invNo"/></td>
                 <td class="tdData"><s:textfield name="dnHeaderEx.invNo" data-dojo-type="dijit/form/TextBox" maxlength="20" theme="simple"/></td>
             </tr>
             <tr class="even">
                 <td class="tdLabel"><s:text name="dn.edit.header.invDate"/></td>
                 <td class="tdData">
                    <input type="text" id="dnHeaderEx.invDate" name="dnHeaderEx.invDate" 
                            onkeydown="javascript:document.getElementById('dnHeaderEx.invDate').blur();" 
                            data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                            value='<s:date name="dnHeaderEx.invDate" format="yyyy-MM-dd" />'/>
                    <button  data-dojo-type="dijit.form.Button" type="button" onclick="clearDate('dnHeaderEx.invDate');"><s:text name='button.clear' /></button>
                 </td>
                 <td class="tdLabel"><s:text name="dn.edit.header.supplierCode"/></td>
                 <td class="tdData">
                    <s:textfield id="supplierCode" readonly="true" name="dnHeaderEx.supplierCode" data-dojo-type="dijit/form/TextBox" maxlength="15" theme="simple"/>
                    <button data-dojo-type="dijit.form.Button"  type="button" onclick="selectSuppBtn();"><s:text name="button.select"/></button>
                 </td>
             </tr>
            <tr class="odd">
                 <td class="tdLabel"><s:text name="dn.edit.header.supplierName"/></td>
                 <td class="tdData"><span id="supplierName"><s:property value="dnHeaderEx.supplierName"/></span></td>
                 <td class="tdLabel"><s:text name="dn.edit.header.supplierAddr"/></td>
                 <td class="tdData"><span id="supplierAddr">
                    <s:property value="dnHeaderEx.supplierAddr1"/><br/>  
                    <s:property value="dnHeaderEx.supplierAddr2"/><br/>
                    <s:property value="dnHeaderEx.supplierAddr3"/><br/>  
                    <s:property value="dnHeaderEx.supplierAddr4"/>
                 </span></td>
             </tr>
             <tr class="even">
                 <td class="tdLabel"><s:text name="dn.edit.header.totalCost"/></td>
                 <td class="tdData"><s:textfield readonly="true" name="dnHeaderEx.totalCost" id="dnHeaderEx.totalCost" data-dojo-type="dijit/form/TextBox" maxlength="15" theme="simple"/></td>
                 <td class="tdLabel"><s:text name="dn.edit.header.totalVat"/></td>
                 <td class="tdData"><s:textfield readonly="true" name="dnHeaderEx.totalVat" id="dnHeaderEx.totalVat" data-dojo-type="dijit/form/TextBox" maxlength="15" theme="simple"/></td>
             </tr>
            <tr class="odd">
                 <td class="tdLabel"><s:text name="dn.edit.header.totalCostWithVat"/></td>
                 <td class="tdData"><s:textfield readonly="true" name="dnHeaderEx.totalCostWithVat" id="dnHeaderEx.totalCostWithVat" data-dojo-type="dijit/form/TextBox" maxlength="15" theme="simple"/></td>
                 <td class="tdLabel"><s:text name="dn.edit.header.vatRate"/></td>
                 <td class="tdData"><s:textfield name="dnHeaderEx.vatRateVal" id="dnHeaderEx.vatRateVal" onblur="checkVatRate();" data-dojo-type="dijit/form/TextBox" maxlength="3" theme="simple"/></td>
             </tr>
            <tr class="even">
                 <td class="tdLabel"><s:text name="dn.edit.header.dnRemarks"/></td>
                 <td class="tdData" colspan="3"><s:textfield name="dnHeaderEx.dnRemarks" data-dojo-type="dijit/form/TextBox" maxlength="500" theme="simple"/></td>
             </tr>
        </tbody>
        </table>
    </div>
    
    <div class="space"></div>
    
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="dn.edit.detail.name" />', width:275" style="width:99%">
        <table><tbody><tr><td>
            <button data-dojo-type="dijit.form.Button" type="button" id="addBtn" onclick="add(true);"><s:text name="button.add" /></button>
        </td></tr></tbody></table>
        <div class="space"></div>
        <table class="tablestyle1" id="sourceTable">
        <thead>
            <tr>
                <th rowspan="2" width="7%"><s:text name="dn.edit.detail.lineSeqNo"/></th>
                <th width="14%"><s:text name="dn.edit.detail.buyerItemCode"/></th>
                <th width="19%"><s:text name="dn.edit.detail.supplierItemCode"/></th>
                <th width="19%"><s:text name="dn.edit.detail.barcode"/></th>
                <th width="12%"><s:text name="dn.edit.detail.itemDesc"/></th>
                <th width="10%"><s:text name="dn.edit.detail.orderUom"/></th>
                <th width="13%"><s:text name="dn.edit.detail.debitQty"/></th>
                <th rowspan="2"><s:text name="button.delete"/></th>               
            </tr>
            <tr>
                <th ><s:text name="dn.edit.detail.unitCost"/></th>
                <th ><s:text name="dn.edit.detail.costDiscountAmount"/></th>               
                <th ><s:text name="dn.edit.detail.costDiscountPercent"/></th>               
                <th ><s:text name="dn.edit.detail.netUnitCost"/></th>               
                <th ><s:text name="dn.edit.detail.itemCost"/></th>               
                <th ><s:text name="dn.edit.detail.itemRemarks"/></th>               
            </tr>
        </thead>
        <tr>
            <td colspan="8">
                <div id="targetDiv">
			        <s:iterator value="dnDetailExs" id="item" status="status">
			            <div id="itemDiv<s:property value="#status.index" />">
			                <table width="100%">
					        <tbody id="tbody<s:property value="#status.index" />">
					            <tr>
					                <td rowspan="2" width="7%"><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].lineSeqNoVal" id="dnDetailExs[<s:property value="#status.index" />].lineSeqNoVal" onblur="checkSeq(<s:property value="#status.index" />);" value="<s:property value="#item.lineSeqNoVal"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="11"/></td>
					                <td width="14%"><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].buyerItemCode" value="<s:property value="#item.buyerItemCode"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="20"/></td>
					                <td width="19%"><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].supplierItemCode" value="<s:property value="#item.supplierItemCode"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="20"/></td>
					                <td width="19%"><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].barcode" value="<s:property value="#item.barcode"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="50"/></td>
					                <td width="12%"><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].itemDesc" value="<s:property value="#item.itemDesc"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="50"/></td>
					                <td width="10%"><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].orderUom" value="<s:property value="#item.orderUom"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="20"/></td>
					                <td width="13%"><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].debitQtyVal" id="dnDetailExs[<s:property value="#status.index" />].debitQtyVal" onblur='checkDebitQty(<s:property value="#status.index" />)' value="<s:property value="#item.debitQtyVal"/>" data-dojo-type="dijit.form.NumberTextBox"  data-dojo-props="maxLength:9,constraints : {pattern: '###0.00'}"/></td>
					                <td rowspan="2"><button  type="button" data-dojo-type="dijit/form/Button" onclick="remove(<s:property value="#status.index" />);"><s:text name="button.delete" /></button></td>   
					            </tr>
					            <tr>
					                <td><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].unitCostVal" id="dnDetailExs[<s:property value="#status.index" />].unitCostVal" onblur='checkUnitCost(<s:property value="#status.index" />)' value="<s:property value="#item.unitCostVal"/>"  data-dojo-type="dijit.form.NumberTextBox"  data-dojo-props="maxLength:10,constraints : {pattern: '###0.00'}"/></td>
					                <td><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].costDiscountAmountVal" value="<s:property value="#item.costDiscountAmountVal"/>" data-dojo-type="dijit.form.NumberTextBox"  data-dojo-props="maxLength:10,constraints : {pattern: '###0.00'}"/></td>
					                <td><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].costDiscountPercentVal" value="<s:property value="#item.costDiscountPercentVal"/>" data-dojo-type="dijit.form.NumberTextBox"  data-dojo-props="maxLength:2,constraints : {pattern: '###0.00'}"/></td>
					                <td><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].netUnitCostVal" value="<s:property value="#item.netUnitCostVal"/>"  data-dojo-type="dijit.form.NumberTextBox"  data-dojo-props="maxLength:10,constraints : {pattern: '###0.00'}"/></td>
					                <td><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].itemCostVal" id="dnDetailExs[<s:property value="#status.index" />].itemCostVal" readonly="readonly" value="<s:property value="#item.itemCostVal"/>"  data-dojo-type="dijit.form.NumberTextBox"  data-dojo-props="maxLength:10,constraints : {pattern: '###0.00'}"/></td>
					                <td><input style="width: 100%;" name="dnDetailExs[<s:property value="#status.index" />].itemRemarks" value="<s:property value="#item.itemRemarks"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="100"/></td>
					            </tr>
					        </tbody>
					        </table>
	                    </div>
			        </s:iterator>
                 </div>
            </td>
        </tr>
        </table>
    </div>
    
    </s:form>
    <script type="text/javascript">
        if("<s:property value='dnDetailExs' />" == null || "<s:property value='dnDetailExs' />" == "")
        {
            add(false);
        }
    </script>
</body>
</html>
