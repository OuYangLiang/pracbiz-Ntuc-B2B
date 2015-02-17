<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>
<div class="space"></div>
       
<div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.retailer.privilege.retailerPrivilege" />', width:275" style="width:99%">
    <table class="commtable">
        <tr>
            <td width="350px">
                <table>
                    <tr>
                        <td>
                            <input class="s1_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px" />
                            <button type="button" onclick="filterPrivilege('s1_filter','s1');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                         <s:select cssClass="s1" list="buyerOperations" listKey="opnId" listValue="opnDesc" multiple="multiple" theme="simple"
                                style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit.form.MultiSelect" />
                        </td>
                        <td style="display: none;">
                         <s:select cssClass="s1_hide" list="#{}" multiple="multiple" theme="simple"
                                style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit.form.MultiSelect" />
                        </td>
                    </tr>
                </table>
            </td>
            
            <td width="100px">
                <p><button type="button" onclick="moveOperation('s1','s2');" data-dojo-type="dijit.form.Button">&gt;</button></p>
                <p><button type="button" onclick="moveOperation('s2','s1');" data-dojo-type="dijit.form.Button">&lt;</button></p>
            </td>
            
            <td width="350px">
                <table>
                    <tr>
                        <td>
                            <input class="s2_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px" />
                            <button type="button" onclick="filterPrivilege('s2_filter','s2');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                           <s:select cssClass="s2" list="buyerSelectedOperations" listKey="opnId" listValue="opnDesc" multiple="multiple" theme="simple"
                                style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit.form.MultiSelect" />
                        </td>
                        <td style="display: none;">
                         <s:select cssClass="s2_hide" list="#{}" multiple="multiple" theme="simple"
                                style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit.form.MultiSelect" />
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>
   
<div class="space"></div>
       
<div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.retailer.buyerGivenSupplierPrivilege" />', width:275" style="width:99%">
    <table class="commtable">
        <tr>
            <td width="350px">
                <table>
                    <tr>
                        <td>
                            <input class="s3_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px" />
                            <button type="button" onclick="filterPrivilege('s3_filter','s3');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                         <s:select cssClass="s3" list="supplierOperations" listKey="opnId" listValue="opnDesc" multiple="multiple" theme="simple"
                                style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit.form.MultiSelect" />
                        </td>
                    </tr>
                </table>
            </td>
            
            <td width="100px">
                <p><button type="button" onclick="moveOperation('s3','s4');" data-dojo-type="dijit.form.Button">&gt;</button></p>
                <p><button type="button" onclick="moveOperation('s4','s3');" data-dojo-type="dijit.form.Button">&lt;</button></p>
            </td>
            
            <td width="350px">
                <table>
                    <tr>
                        <td>
                            <input class="s4_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px" />
                            <button type="button" onclick="filterPrivilege('s4_filter','s4');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                           <s:select cssClass="s4" list="supplierSelectedOperations" listKey="opnId" listValue="opnDesc" multiple="multiple" theme="simple"
                                style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit.form.MultiSelect" />
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>
