<s:if test="!updateList.isEmpty()">
    <table style="border: solid 15px #FF6633" width="99%">
        <tbody>
            <s:iterator var="item" value="updateList" status="sts">
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
                                       <s:property value="#item.oldVersion.groupId" />
                                   </td>
                               </tr>
                               
                               <s:if test="#item.groupId != #item.oldVersion.groupId">
				               <tr height="25px">
				                   <td width="2px"></td>
				                   <td width="30%"></td>
				                   <td width="2%"></td>
				                
				                   <td class="required">
				                       <s:property value="#item.groupId" />
				                   </td>
				               </tr>
				               </s:if>
                               
                               <tr height="25px">
                                   <td width="2px"></td>
                                   <td width="30%"><s:text name='group.confirm.profileInfo.groupDesc' /></td>
                                   <td width="2%">:</td>
                                   <td>
                                        <s:property value="#item.oldVersion.groupName" />
                                   </td>
                               </tr>
                               <s:if test="#item.groupName != #item.oldVersion.groupName">
                               <tr height="25px">
                                   <td width="2px"></td>
                                   <td width="30%"></td>
                                   <td width="2%"></td>
                                
                                   <td class="required">
                                       <s:property value="#item.groupName" />
                                   </td>
                               </tr>
                               </s:if>
                               
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
                                   <td> <s:property value="#item.oldVersion.company" /></td>
                               </tr>
                               <s:if test="#item.company != #item.oldVersion.company">
                               <tr height="25px">    
                                   <td width="2px"></td>
                                   <td width="30%"></td>
                                   <td width="2%"></td>
                                
                                   <td class="required">
                                       <s:property value="#item.company" />
                                   </td>
                               </tr>
                               </s:if>
                           </tbody>
                       </table>
                    </div>
                    <div class="space"></div>
                    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.confirm.role" />', width:275">
                        <table class="commtable">
                            <tr class="thead">
                                <td width="50%"><s:text name="group.confirm.previous" /></td>
                                <td><s:text name="group.confirm.current" /></td>
                            </tr>
                            <tr>
                                <td style="vertical-align:top">
                                    <table class="access">
                                        <tr><td><ul>
                                            <s:iterator value="#item.oldVersion.roles" var="innerItem" >
                                                <li><s:property value="#innerItem.roleName + '(' + #innerItem.roleId + ')'" /></li>
                                            </s:iterator>
                                        </ul></td></tr>
                                    </table>
                                </td>
                                <td  style="vertical-align:top">
                                    <table class="access">
                                        <tr><td><ul>
                                            <s:iterator value="#item.roles" var="innerItem" >
                                                <li><s:property value="#innerItem.roleName + '(' + #innerItem.roleId + ')'" /></li>
                                            </s:iterator>
                                        </ul></td></tr>
                                    </table>
                                </td>
                            </tr>
                       </table>
                    </div>
                    <div class="space"></div>
                    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.confirm.user" />', width:275">
                        <table class="commtable">
                            <tr class="thead">
                                <td width="50%"><s:text name="group.confirm.previous" /></td>
                                <td><s:text name="group.confirm.current" /></td>
                            </tr>
                            <tr>
                                <td style="vertical-align:top">
                                    <table class="access">
                                        <tr><td><ul>
                                            <s:iterator value="#item.oldVersion.users" var="innerItem" >
                                                <s:if test="#innerItem.locked">
                                                <li class="required"><s:property value="#innerItem.userName" /><s:text name="LastUpdateFrom.User"/></li>
                                                </s:if>
                                                <s:else>
                                                <li><s:property value="#innerItem.userName" /></li>
                                                </s:else>
                                            </s:iterator>
                                        </ul></td></tr>
                                    </table>
                                </td>
                                <td  style="vertical-align:top">
                                    <table class="access">
                                        <tr><td><ul>
                                           <s:iterator value="#item.users" var="innerItem" >
                                                <s:if test="#innerItem.locked">
                                                <li class="required"><s:property value="#innerItem.userName" /><s:text name="LastUpdateFrom.User"/></li>
                                                </s:if>
                                                <s:else>
                                                <li><s:property value="#innerItem.userName" /></li>
                                                </s:else>
                                           </s:iterator>
                                        </ul></td></tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="space"></div>
                    <s:if test="#item.userTypeOid == 2 || #item.userTypeOid == 4">
                    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.confirm.supplier" />', width:275">
                         <table class="commtable">
                            <tr class="thead">
                                <td width="50%"><s:text name="group.confirm.previous" /></td>
                                <td><s:text name="group.confirm.current" /></td>
                            </tr>
                            <tr>
                                <td style="vertical-align:top">
                                    <table class="access">
                                        <tr><td><ul>
                                            <s:iterator value="#item.oldVersion.suppliers" var="innerItem" >
                                                <li><s:property value="#innerItem.supplierName" /></li>
                                            </s:iterator>
                                        </ul></td></tr>
                                    </table>
                                </td>
                                <td  style="vertical-align:top">
                                    <table class="access">
                                        <tr><td><ul>
                                            <s:iterator value="#item.suppliers" var="innerItem" >
                                                <li><s:property value="#innerItem.supplierName" /></li>
                                            </s:iterator>
                                        </ul></td></tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="space"></div>
                    </s:if>
                       
                    <s:if test="#item.userTypeOid == 3 || #item.userTypeOid == 5">
                       <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.confirm.tp" />', width:275">
                           <table class="commtable">
                            <tr class="thead">
                                <td width="50%"><s:text name="group.confirm.previous" /></td>
                                <td><s:text name="group.confirm.current" /></td>
                            </tr>
                            <tr>
                                <td style="vertical-align:top">
                                    <table class="access">
                                        <tr><td><ul>
                                            <s:iterator value="#item.oldVersion.tradingPartners" var="innerItem" >
                                                <li><s:property value="#innerItem.tradingPartnerDesc" /></li>
                                            </s:iterator>
                                        </ul></td></tr>
                                    </table>
                                </td>
                                <td  style="vertical-align:top">
                                    <table class="access">
                                        <tr><td><ul>
                                            <s:iterator value="#item.tradingPartners" var="innerItem" >
                                                <li><s:property value="#innerItem.tradingPartnerDesc" /></li>
                                            </s:iterator>
                                        </ul></td></tr>
                                    </table>
                                </td>
                            </tr>
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
                <tr><td style="background-color:#FF6633"> &nbsp;</td></tr>
            </s:if>
            </s:iterator>
        </tbody>
    </table>
</s:if>