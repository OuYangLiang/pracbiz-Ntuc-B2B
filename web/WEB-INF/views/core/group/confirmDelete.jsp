<s:if test="!deleteList.isEmpty()">
<table style="border: solid 15px #CC0000" width="99%">
    <tbody>
        <s:iterator var="item" value="deleteList" status="sts">
        <tr>
            <td>
                <div data-dojo-type="dijit/TitlePane"
                    data-dojo-props="title:'<s:text name="group.confirm.profileInfo" />', width:275">
                    <table class="commtable">
                        <tbody> 
                           <tr height="25px">
                               <td width="2px"></td>
                               <td width="30%"><s:text name='group.confirm.profileInfo.groupId' /></td>
                               <td width="2%">:</td>
                               <td>
                                   <s:property value="#item.groupId" />
                               </td>
                           </tr>
                           
                           <tr height="25px">
                               <td width="2px"></td>
                               <td width="30%"><s:text name='group.confirm.profileInfo.groupDesc' /></td>
                               <td width="2%">:</td>
                               <td>
                                   <s:property value="#item.groupName" />
                               </td>
                           </tr>
                           
                           <tr height="25px">
                               <td width="2px"></td>
                               <td width="30%"><s:text name='group.confirm.profileInfo.userType' /></td>
                               <td width="2%">:</td>
                               <td>
                                   <s:property value="#item.userTypeId" />
                               </td>
                           </tr>
                           
                           <tr height="25px" id="supplier">
                               <td width="2px"></td>
                               <s:if test="#item.userTypeOid == 3 || #item.userTypeOid == 5">
                                  <td width="30%"><s:text name='group.confirm.profileInfo.supplier' /></td>
                               </s:if>
                               <s:if test="#item.userTypeOid == 2 || #item.userTypeOid == 4">
                                  <td width="30%"><s:text name='group.confirm.profileInfo.buyer' /></td>
                               </s:if>
                               <td width="2%">:</td>
                               <td><s:property value="#item.company" /></td>
                           </tr>
                       </tbody>
                   </table>
                </div>
                <div class="space"></div>
                <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.confirm.role" />', width:275">
                    <table class="commtable">
                       <s:iterator value="#item.roles" var="innerItem" >
                           <tr>
                               <td>
                                   <s:property value="#innerItem.roleName + '(' + #innerItem.roleId + ')'" />
                               </td>
                           </tr>
                       </s:iterator>
                   </table>
                </div>
                <div class="space"></div>
                <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.confirm.user" />', width:275">
                    <table class="commtable">
                        <s:iterator value="#item.users" var="innerItem" >
                           <tr>
                               <td>
                                   <s:property value="#innerItem.userName" />
                               </td>
                           </tr>
                       </s:iterator>
                    </table>
                </div>
                <div class="space"></div>
                <s:if test="#item.userTypeOid == 2 || #item.userTypeOid == 4">
                <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.confirm.supplier" />', width:275">
                     <table class="commtable">
                         <s:iterator value="#item.suppliers" var="innerItem" >
                           <tr>
                               <td>
                                   <s:property value="#innerItem.supplierName" />
                               </td>
                           </tr>
                        </s:iterator>
                     </table>
                </div>
                <div class="space"></div>
                </s:if>
                   
                <s:if test="#item.userTypeOid == 3 || #item.userTypeOid == 5">
                   <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.view.tp" />', width:275">
                       <table class="commtable">
                       <s:iterator value="#item.tradingPartners" var="innerItem" >
                           <tr>
                               <td>
                                   <s:property value="#innerItem.tradingPartnerDesc" />
                               </td>
                           </tr>
                       </s:iterator>
                       </table>
                   </div>
                   <div class="space"></div>
                </s:if>
                <div id="ftcontent" align="center">
                    <table id="control">
                        <tr><td><s:text name="group.confirm.ctrl.ctrlstatus" /></td><td>:</td><td><s:property value="#item.ctrlStatusValue" /></td></tr>
                        <tr><td><s:text name="group.confirm.ctrl.actionType" /></td><td>:</td><td ><s:property value="#item.actionTypeValue" /></td></tr>
                        <tr><td><s:text name="group.confirm.ctrl.actor" /></td><td>:</td><td><s:property value="#item.actor" /></td></tr>
                        <tr><td><s:text name="group.confirm.ctrl.actionDate" /></td><td>:</td><td><s:property value="#item.actionDate" /></td></tr>
                    </table>
                </div>
            </td>
        </tr>
        <s:if test="!#sts.isLast()" >
            <tr><td style="background-color:#CC0000"> &nbsp;</td></tr>
        </s:if>
        </s:iterator>
    </tbody>
</table>
</s:if>