<s:if test="!updateList.isEmpty()" >
    <table style="border:solid 15px #FF6633" width="99%">
    <tbody>
    <s:iterator value="updateList" var="item" status="itemIdx">
    <tr><td>
        <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="role.view.profile" />', width:275" style="width:99%">
            <table class="commtable">
                <tbody>
                    <tr>
                        <td width="2px"><span class="required">*</span> </td>
                        <td width="30%">&nbsp;&nbsp;<s:text name="role.view.profile.userType" /></td>
                        <td>:</td>
                        <td><s:property value="#item.userTypeId" /></td>
                    </tr>
                    
                    <tr>
                        <td width="2px"><span class="required">*</span> </td>
                        <td width="30%">&nbsp;&nbsp;<s:text name="role.view.profile.roleType" /></td>
                        <td>:</td>
                        <td><s:property value="#item.roleTypeValue" /></td>
                    </tr>
                    
                    <s:if test="#item.userTypeOid != 1 && !#item.companyChanged" >
                    <tr>
                        <td><span class="required">*</span> </td>
                        <td>&nbsp;&nbsp;<s:text name="role.view.profile.company" /></td>
                        <td>:</td>
                        <td><s:property escape="false" value="#item.company" /></td>
                    </tr>
                    </s:if>
                    
                    <tr>
                        <td><span class="required">*</span></td>
                        <td>&nbsp;&nbsp;<s:text name="role.view.profile.roleId" /></td>
                        <td>:</td>
                        <td><s:property value="#item.oldVersion.roleId" /></td>
                    </tr>
                    
                    <s:if test="#item.roleId!=#item.oldVersion.roleId" >
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td class="required"><s:property value="#item.roleId" /></td>
                    </tr>
                    </s:if>
                    
                    <tr>
                        <td><span class="required">*</span></td>
                        <td>&nbsp;&nbsp;<s:text name="role.view.profile.roleDesc" /></td>
                        <td>:</td>
                        <td><s:property value="#item.oldVersion.roleName" /></td>
                    </tr>
                    
                    <s:if test="#item.roleName!=#item.oldVersion.roleName" >
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td class="required"><s:property value="#item.roleName" /></td>
                    </tr>
                    </s:if>
                </tbody>
            </table>
        </div>
        
        <s:if test="#item.companyChanged" >
        <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="role.view.company.list" />', width:275" style="width:99%">
            <table class="commtable">
                <tr class="thead">
                    <td width="50%">Previous</td>
                    <td>Current</td>
                </tr>
                
                <tr>
                    <td style="vertical-align:top">
                        <table class="access">
                            <tr><td><ul>
                                <s:iterator value="#item.oldVersion.selectedSuppliers" var="innerItem">
                                    <li><s:property value="#innerItem.supplierName" /></li>
                                </s:iterator>
                            </ul></td></tr>
                        </table>
                    </td>
                    
                    <td  style="vertical-align:top">
                        <table class="access">
                            <tr><td><ul>
                                <s:iterator value="#item.selectedSuppliers" var="innerItem">
                                    <li><s:property value="#innerItem.supplierName" /></li>
                                </s:iterator>
                            </ul></td></tr>
                        </table>
                    </td>
                </tr>
            </table>
        </div>
        </s:if>
            
        <div class="space"></div>
            
        <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="role.view.privilege" />', width:275" style="width:99%">
            <table class="commtable">
                <tr class="thead">
                    <td width="50%">Previous</td>
                    <td>Current</td>
                </tr>
                
                <tr>
                    <td style="vertical-align:top">
                        <table class="access">
                            <tr><td><ul>
                                <s:iterator value="#item.oldVersion.selectedOperations" var="innerItem">
                                    <li><s:property value="#innerItem.opnDesc" /></li>
                                </s:iterator>
                            </ul></td></tr>
                        </table>
                    </td>
                    
                    <td  style="vertical-align:top">
                        <table class="access">
                            <tr><td><ul>
                                <s:iterator value="#item.selectedOperations" var="innerItem">
                                    <li><s:property value="#innerItem.opnDesc" /></li>
                                </s:iterator>
                            </ul></td></tr>
                        </table>
                    </td>
                </tr>
            </table>
        </div>
        
        <div class="space"></div>
        
        <div id="ftcontent" align="center">
            <table id="control">
                <tr><td><s:text name="role.view.ctrl.ctrlstatus" /></td><td>:</td><td><s:property value="#item.ctrlStatusValue" /></td></tr>
                <tr><td><s:text name="role.view.ctrl.actionType" /></td><td>:</td><td ><s:property value="#item.actionTypeValue" /></td></tr>
                <tr><td><s:text name="role.view.ctrl.actor" /></td><td>:</td><td><s:property value="#item.actor" /></td></tr>
                <tr><td><s:text name="role.view.ctrl.actionDate" /></td><td>:</td><td><s:property value="#item.actionDate" /></td></tr>
            </table>
        </div>
    </td></tr>
    <s:if test="!#itemIdx.isLast()" >
        <tr><td style="background-color:#FF6633"> &nbsp;</td></tr>
    </s:if>
    </s:iterator>
    </tbody>
    </table>
</s:if>