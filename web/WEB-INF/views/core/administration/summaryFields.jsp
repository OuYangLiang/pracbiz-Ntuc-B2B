<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>
<div data-dojo-type="dijit.TitlePane" 
            data-dojo-props="title:'<s:text name="admin.msgSummaryPageSetting.buyer" />', width:275" style="width:99%">
    <div class="fieldListContainer">
        <table class="tablestyle3">
            <thead>
                <tr>
                    <th  rowspan="2" style="width:6%; text-align: center;">
                        <input type="checkbox" onclick="selectAll(this, 'buyer_available');" data-dojo-type="dijit.form.CheckBox"/>
                    </th>
                    <th rowspan="2"  width="20%"><s:text name="admin.msgSummaryPageSetting.fieldName" /></th>
                    <th rowspan="2" width="13%"><s:text name="admin.msgSummaryPageSetting.width" /></th>
                    <th rowspan="2" style="width:6%; text-align: center;">
                        <s:text name="admin.msgSummaryPageSetting.sortEnable" /><br/>
                        <input type="checkbox" onclick="selectAll(this, 'buyer_sortable');" data-dojo-type="dijit.form.CheckBox"/>
                    </th>
                    <th colspan="2" style=" text-align: center;">
                        <s:text name="admin.msgSummaryPageSetting.tooltip" />
                    </th>
                </tr>
                <tr >
                    <th style="width:27%; text-align: center;"><s:text name="admin.msgSummaryPageSetting.added" /></th>
                    <th style="text-align: center;"><s:text name="admin.msgSummaryPageSetting.all" /></th>
                </tr>
            </thead>
        </table>
    
        <div  data-dojo-type="dojo.dnd.Source"
            data-dojo-props="accept: [ 'field_buyer' ]" withHandles="true" 
            id="fieldListNode" class="fieldContainer">
            <s:iterator value="fields.B" id="item">
                <div class="dojoDndItem field_buyer" dndType="field">
                    <table class="tablestyle3">
                        <tr>
                            <td style="width:6%; text-align: center;">
                                <s:if test="#item.available">
	                                <input name="buyer_available" class="buyer_available" accesskey='<s:property value="#item.fieldOid"/>' type="checkbox" checked="checked" data-dojo-type="dijit.form.CheckBox"/>
                                </s:if>
                                <s:else>
	                                <input name="buyer_available" class="buyer_available" accesskey='<s:property value="#item.fieldOid"/>' type="checkbox" data-dojo-type="dijit.form.CheckBox"/>
                                </s:else>
                            </td>
                            <td class="dojoDndHandle" style="width: 20%;"><s:property value="#item.fieldLabel"/></td>
                            <td style="width:13%;">
                                <input type="text" name="buyer_fieldWidth"
                                    data-dojo-type="dijit.form.TextBox" style="width:40px" maxlength="4" value='<s:property value="#item.fieldWidth"/>'/>
                                <label>%</label>
                            </td>
                            <td style="width:6%; text-align: center;">
                                <s:if test="#item.sortable">
                                    <input name="buyer_sortable" class="buyer_sortable" type="checkbox" checked="checked" data-dojo-type="dijit.form.CheckBox"/>
                                </s:if>
                                <s:else>
                                    <input name="buyer_sortable" class="buyer_sortable" type="checkbox" data-dojo-type="dijit.form.CheckBox"/>
                                </s:else>
                            </td>
                            <td style="width:27%;">
                                <div class="toolTipListContainer">
                                    <div data-dojo-type="dojo.dnd.Source" 
                                        withHandles="true" data-dojo-props="accept: [ 'toolTip_<s:property value="#item.fieldOid"/>' ]"
                                        id="toolTipListNode_<s:property value="#item.fieldOid"/>" class="toolTipContainer">
                                        <s:iterator value="#item.selectedToolTips" id="toolTip">
                                            <div class="dojoDndItem toolTip_<s:property value="#item.fieldOid"/>" dndType="toolTip_<s:property value="#item.fieldOid"/>">
                                                <input type="checkbox" onclick="change(this)"
                                                    data-dojo-type="dijit.form.CheckBox"/>
                                                <label accesskey="<s:property value="#toolTip.tooltipFieldOid"/>"><s:property value="getText(#toolTip.tooltipFieldLabel)"/></label>
                                            </div>
                                        </s:iterator>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="toolTipListContainer">
                                    <div data-dojo-type="dojo.dnd.Source" 
                                        withHandles="true" data-dojo-props="accept: [ 'toolTip_<s:property value="#item.fieldOid"/>' ]"
                                        id="toolTipListNode" class="toolTipContainer">
                                        <s:iterator value="#item.otherToolTips" id="toolTip">
                                            <div class="dojoDndItem toolTip_<s:property value="#item.fieldOid"/>" dndType="toolTip_<s:property value="#item.fieldOid"/>">
                                                <input type="checkbox" onclick="change(this)"
                                                    data-dojo-type="dijit.form.CheckBox"/>
                                                <label accesskey="<s:property value="#toolTip.tooltipFieldOid"/>" ><s:property value="#toolTip.tooltipFieldLabel"/></label>
                                            </div>
                                        </s:iterator>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                 </div>
             </s:iterator>
        </div>
    </div>
</div>

<div class="space"></div>
 
<div data-dojo-type="dijit.TitlePane" 
            data-dojo-props="title:'Supplier', width:275" style="width:99%">
    <div class="fieldListContainer">
        <table class="tablestyle3">
            <thead>
                <tr>
                    <th  rowspan="2" style="width:6%; text-align: center;">
                        <input type="checkbox" onclick="selectAll(this, 'supplier_available');" data-dojo-type="dijit.form.CheckBox"/>
                    </th>
                    <th rowspan="2"  width="20%"><s:text name="admin.msgSummaryPageSetting.fieldName" /></th>
                    <th rowspan="2" width="13%"><s:text name="admin.msgSummaryPageSetting.width" /></th>
                    <th rowspan="2" style="width:6%; text-align: center;">
                        <s:text name="admin.msgSummaryPageSetting.sortEnable" /><br/>
                        <input type="checkbox" onclick="selectAll(this, 'supplier_sortable');" data-dojo-type="dijit.form.CheckBox"/>
                    </th>
                    <th colspan="2" style=" text-align: center;">
                        <s:text name="admin.msgSummaryPageSetting.tooltip" />
                    </th>
                </tr>
                <tr >
                    <th style="width:27%; text-align: center;"><s:text name="admin.msgSummaryPageSetting.added" /></th>
                    <th style="text-align: center;"><s:text name="admin.msgSummaryPageSetting.all" /></th>
                </tr>
            </thead>
        </table>
    
        <div  data-dojo-type="dojo.dnd.Source"
            data-dojo-props="accept: [ 'field_supplier' ]" withHandles="true" 
            id="fieldListNode" class="fieldContainer">
            <s:iterator value="fields.S" id="item">
                <div class="dojoDndItem field_supplier" dndType="field">
                    <table class="tablestyle3">
                        <tr>
                            <td style="width:6%; text-align: center;">
                                <s:if test="#item.available">
                                    <input name="supplier_available" class="supplier_available" accesskey='<s:property value="#item.fieldOid"/>' type="checkbox" checked="checked" data-dojo-type="dijit.form.CheckBox"/>
                                </s:if>
                                <s:else>
                                    <input name="supplier_available" class="supplier_available" accesskey='<s:property value="#item.fieldOid"/>' type="checkbox" data-dojo-type="dijit.form.CheckBox"/>
                                </s:else>
                            </td>
                            <td class="dojoDndHandle" style="width: 20%;"><s:property value="#item.fieldLabel"/></td>
                            <td style="width:13%;">
                                <input type="text" name="supplier_fieldWidth"
                                    data-dojo-type="dijit.form.TextBox" style="width:40px" maxlength="4" value='<s:property value="#item.fieldWidth"/>'/>
                                <label>%</label>
                            </td>
                            <td style="width:6%; text-align: center;">
                                <s:if test="#item.sortable">
                                    <input name="supplier_sortable" class="supplier_sortable" type="checkbox" checked="checked" data-dojo-type="dijit.form.CheckBox"/>
                                </s:if>
                                <s:else>
                                    <input name="supplier_sortable" class="supplier_sortable" type="checkbox" data-dojo-type="dijit.form.CheckBox"/>
                                </s:else>
                            </td>
                            <td style="width:27%;">
                                <div class="toolTipListContainer">
                                    <div data-dojo-type="dojo.dnd.Source" 
                                        withHandles="true" data-dojo-props="accept: [ 'toolTip_<s:property value="#item.fieldOid"/>' ]"
                                        id="toolTipListNode_<s:property value="#item.fieldOid"/>" class="toolTipContainer">
                                        <s:iterator value="#item.selectedToolTips" id="toolTip">
                                            <div class="dojoDndItem toolTip_<s:property value="#item.fieldOid"/>" dndType="toolTip_<s:property value="#item.fieldOid"/>">
                                                <input type="checkbox" onclick="change(this)"
                                                    data-dojo-type="dijit.form.CheckBox"/>
                                                <label accesskey="<s:property value="#toolTip.tooltipFieldOid"/>"><s:property value="#toolTip.tooltipFieldLabel"/></label>
                                            </div>
                                        </s:iterator>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="toolTipListContainer">
                                    <div data-dojo-type="dojo.dnd.Source" 
                                        withHandles="true" data-dojo-props="accept: [ 'toolTip_<s:property value="#item.fieldOid"/>' ]"
                                        id="toolTipListNode" class="toolTipContainer">
                                        <s:iterator value="#item.otherToolTips" id="toolTip">
                                            <div class="dojoDndItem toolTip_<s:property value="#item.fieldOid"/>" dndType="toolTip_<s:property value="#item.fieldOid"/>">
                                                <input type="checkbox" onclick="change(this)"
                                                    data-dojo-type="dijit.form.CheckBox"/>
                                                <label accesskey="<s:property value="#toolTip.tooltipFieldOid"/>" ><s:property value="#toolTip.tooltipFieldLabel"/></label>
                                            </div>
                                        </s:iterator>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                 </div>
             </s:iterator>
        </div>
    </div>
</div>
