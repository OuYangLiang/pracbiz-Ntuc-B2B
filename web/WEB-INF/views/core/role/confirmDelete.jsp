<s:if test="!deleteList.isEmpty()" >
    <table style="border:solid 15px #CC0000" width="99%">
    <tbody>
    <s:iterator value="deleteList" var="item" status="itemIdx">
    
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
                    
                    <s:if test="#item.userTypeOid != 1" >
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
                        <td><s:property value="#item.roleId" /></td>
                    </tr>
                    
                    <tr>
                        <td><span class="required">*</span></td>
                        <td>&nbsp;&nbsp;<s:text name="role.view.profile.roleDesc" /></td>
                        <td>:</td>
                        <td><s:property value="#item.roleName" /></td>
                    </tr>
                </tbody>
            </table>
        </div>
            
        <div class="space"></div>
            
        <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="role.view.privilege" />', width:275" style="width:99%">
            <table class="commtable" style="text-indent:2em">
                <s:iterator value="#item.selectedOperations" var="innerItem" >
                    <tr><td><s:property value="#innerItem.opnDesc" /></td></tr>
                </s:iterator>
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
        <tr><td style="background-color:#CC0000"> &nbsp;</td></tr>
    </s:if>
    </s:iterator>
    </tbody>
    </table>
</s:if>