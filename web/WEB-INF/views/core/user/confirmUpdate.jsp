<s:if test="!mkUpdateUserProfileList.isEmpty()">
<table style="border: solid 15px #FF6633" width="99%">
	<tbody>
		<s:iterator id="updatedUser" value="mkUpdateUserProfileList" status="sts">
		<tr>
			<td>
				<div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.confirm.profilearea.title" />', width:275">
			    	<table class="commtable">
			            <tbody> 
			                <tr height="25px">
			                	<td width="2px"></td>
			                    <td width="30%"><s:text name='user.confirm.profilearea.userType' /></td>
			                    <td width="2%">:</td>
			                    <td>
			                    	<s:property value="#updatedUser.oldVersion.userTypeDesc" />
								</td>
			                </tr>
			                <s:if test="#updatedUser.userType!=#updatedUser.oldVersion.userType" >
							<tr>
			                    <td></td>
			                    <td></td>
			                    <td></td>
			                    <td class="required"><s:property value="#updatedUser.userTypeDesc" /></td>
			                </tr>
			                </s:if>
			            </tbody>
			        </table>
			    </div>
			    <div class="space"></div>
			    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.confirm.contactarea.title" />', width:275">
			    	<table class="commtable">
			            <tbody> 
			            	<s:if test="#updatedUser.oldVersion.userType == 3 || #updatedUser.oldVersion.userType == 5">
			                <tr height="25px" id="supplier">
			                	<td width="2px"></td>
			                    <td width="30%"><s:text name='user.confirm.contactarea.supplier' /></td>
			                    <td width="2%">:</td>
			                    <td><s:property value="#updatedUser.oldVersion.companyName" /></td>
			                </tr>
			                </s:if>
			                <s:if test="#updatedUser.oldVersion.userType == 2 || #updatedUser.oldVersion.userType == 4
			                 || #updatedUser.oldVersion.userType == 6 || #updatedUser.oldVersion.userType == 7">
			                <tr height="25px" id="buyer">
			                	<td width="2px"></td>
			                    <td width="30%"><s:text name='user.confirm.contactarea.buyer' /></td>
			                    <td width="2%">:</td>
			                    <td><s:property value="#updatedUser.oldVersion.companyName" /></td>
			                </tr>
			                </s:if>
			                <s:if test="#updatedUser.companyName!=#updatedUser.oldVersion.companyName">
			                	<s:if test="#updatedUser.userType == 3 || #updatedUser.userType == 5">
				                <tr height="25px" id="supplier">
				                	<td width="2px"></td>
				                    <td class="required" width="30%"><s:text name='user.confirm.contactarea.supplier' /></td>
				                    <td width="2%">:</td>
				                    <td class="required"><s:property value="#updatedUser.companyName" /></td>
				                </tr>
				                </s:if>
				                <s:if test="#updatedUser.userType == 2 || #updatedUser.userType == 4
				                 || #updatedUser.userType == 6 || #updatedUser.userType == 7">
				                <tr height="25px" id="buyer">
				                	<td width="2px"></td>
				                    <td class="required" width="30%"><s:text name='user.confirm.contactarea.buyer' /></td>
				                    <td width="2%">:</td>
				                    <td class="required"><s:property value="#updatedUser.companyName" /></td>
				                </tr>
				                </s:if>
			            	</s:if>
			                <tr height="25px">
			                	<td></td>
			                    <td width="30%"><s:text name='user.confirm.contactarea.loginId' /></td>
			                    <td width="2%">:</td>
			                    <td>
			                    	<s:property value="#updatedUser.oldVersion.loginId" />
			                    </td>
			                </tr>
			                <s:if test="#updatedUser.loginId!=#updatedUser.oldVersion.loginId" >
							<tr>
			                    <td></td>
			                    <td></td>
			                    <td></td>
			                    <td class="required"><s:property value="#updatedUser.loginId" /></td>
			                </tr>
			                </s:if>
			                <tr height="25px">
			                	<td></td>
			                    <td width="30%"><s:text name='user.confirm.contactarea.salutation' /></td>
			                    <td width="2%">:</td>
			                    <td>
			                    	<s:property value="#updatedUser.oldVersion.salutation" />
			                    </td>
			                </tr>
			                <s:if test="#updatedUser.salutation!=#updatedUser.oldVersion.salutation" >
							<tr>
			                    <td></td>
			                    <td></td>
			                    <td></td>
			                    <td class="required"><s:property value="#updatedUser.salutation" /></td>
			                </tr>
			                </s:if>
			                <tr height="25px">
			                	<td></td>
			                    <td width="30%"><s:text name='user.confirm.contactarea.sex' /></td>
			                    <td width="2%">:</td>
			                    <td>
			                        <s:if test="#updatedUser.oldVersion.gender != null"><s:text name='Value.%{#updatedUser.oldVersion.gender }' /></s:if>
			                    </td>
			                </tr>
			                <s:if test="#updatedUser.gender!=#updatedUser.oldVersion.gender" >
							<tr>
			                    <td></td>
			                    <td></td>
			                    <td></td>
			                    <td class="required"><s:if test="#updatedUser.gender != null"><s:text name='Value.%{#updatedUser.gender }' /></s:if></td>
			                </tr>
			                </s:if>
			                <tr height="25px">
			                	<td></td>
			                    <td width="30%"><s:text name='user.confirm.contactarea.name' /></td>
			                    <td width="2%">:</td>
			                    <td>
			                    	<s:property value="#updatedUser.oldVersion.userName" />
			                    </td>
			                </tr>
			                <s:if test="#updatedUser.userName!=#updatedUser.oldVersion.userName" >
							<tr>
			                    <td></td>
			                    <td></td>
			                    <td></td>
			                    <td class="required"><s:property value="#updatedUser.userName" /></td>
			                </tr>
			                </s:if>
			                <tr height="25px">
			                	<td></td>
			                    <td width="30%"><s:text name='user.confirm.contactarea.email' /></td>
			                    <td width="2%">:</td>
			                    <td>
			                    	<s:property value="#updatedUser.oldVersion.email" />
			                    </td>
			                </tr>
			                <s:if test="#updatedUser.email!=#updatedUser.oldVersion.email" >
							<tr>
			                    <td></td>
			                    <td></td>
			                    <td></td>
			                    <td class="required"><s:property value="#updatedUser.email" /></td>
			                </tr>
			                </s:if>
			                <tr height="25px">
			                	<td></td>
			                    <td width="30%"><s:text name='user.confirm.contactarea.telephone' /></td>
			                    <td width="2%">:</td>
			                    <td>
			                    	<s:property value="#updatedUser.oldVersion.tel" />
			                    </td>
			                </tr>
			                <s:if test="#updatedUser.tel!=#updatedUser.oldVersion.tel" >
							<tr>
			                    <td></td>
			                    <td></td>
			                    <td></td>
			                    <td class="required"><s:property value="#updatedUser.tel" /></td>
			                </tr>
			                </s:if>
			                <tr height="25px">
			                	<td></td>
			                    <td width="30%"><s:text name='user.confirm.contactarea.fax' /></td>
			                    <td width="2%">:</td>
			                    <td>
			                    	<s:property value="#updatedUser.oldVersion.fax" />
			                    </td>
			                </tr>
			                <s:if test="#updatedUser.fax!=#updatedUser.oldVersion.fax" >
							<tr>
			                    <td></td>
			                    <td></td>
			                    <td></td>
			                    <td class="required"><s:property value="#updatedUser.fax" /></td>
			                </tr>
			                </s:if>
			                <tr height="25px">
			                	<td></td>
			                    <td width="30%"><s:text name='user.confirm.contactarea.mobile' /></td>
			                    <td width="2%">:</td>
			                    <td>
			                    	<s:property value="#updatedUser.oldVersion.mobile" />
			                    </td>
			                </tr>
			                <s:if test="#updatedUser.mobile!=#updatedUser.oldVersion.mobile" >
							<tr>
			                    <td></td>
			                    <td></td>
			                    <td></td>
			                    <td class="required"><s:property value="#updatedUser.mobile" /></td>
			                </tr>
			                </s:if>
			                <tr height="25px">
			                	<td></td>
			                    <td width="30%"><s:text name='user.confirm.contactarea.active' /></td>
			                    <td width="2%">:</td>
			                    <td>
			                    	<s:if test="#updatedUser.oldVersion.active">
			                    		<s:text name='Value.Yes' />
			                    	</s:if>
			                    	<s:else>
			                    		<s:text name='Value.No' />
			                    	</s:else>
			                    </td>
			                </tr>
			                <s:if test="#updatedUser.active!=#updatedUser.oldVersion.active" >
							<tr>
			                    <td></td>
			                    <td></td>
			                    <td></td>
			                    <td class="required">
			                    	<s:if test="#updatedUser.active">
			                    		<s:text name='Value.Yes' />
			                    	</s:if>
			                    	<s:else>
			                    		<s:text name='Value.No' />
			                    	</s:else>
			                    </td>
			                </tr>
			                </s:if>
			                <tr height="25px" id="blocked">
			                	<td></td>
			                    <td width="30%"><s:text name='user.confirm.contactarea.block' /></td>
			                    <td width="2%">:</td>
			                    <td>
			                    	<s:if test="#updatedUser.oldVersion.blocked">
			                    		<s:text name='Value.Yes' />
			                    	</s:if>
			                    	<s:else>
			                    		<s:text name='Value.No' />
			                    	</s:else>
			                    </td>
			                </tr>
			                <s:if test="#updatedUser.blocked!=#updatedUser.oldVersion.blocked" >
							<tr>
			                    <td></td>
			                    <td></td>
			                    <td></td>
			                    <td class="required">
			                    	<s:if test="#updatedUser.blocked">
			                    		<s:text name='Value.Yes' />
			                    	</s:if>
			                    	<s:else>
			                    		<s:text name='Value.No' />
			                    	</s:else>
			                    </td>
			                </tr>
			                </s:if>
			            </tbody>
			        </table>
			    </div>
			    <s:if test="#updatedUser.userType == 2 || #updatedUser.userType == 4 || #updatedUser.userType == 6 || #updatedUser.userType == 7">
			    <div class="space"></div>
                <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.confirm.allowbuyer.title" />', width:275">
                    <table class="commtable">
                        <tr class="thead">
                            <td width="50%">Previous</td>
                            <td>Current</td>
                        </tr>
                        <tr>
                            <td style="vertical-align:top">
                                <table class="access">
                                    <tr><td><ul>
                                        <c:forEach items="${updatedUser.oldVersion.oldSelectedBuyersList}" var="buyer" varStatus="targetIndex">
                                            <li><c:out value="${buyer.buyerName}" /></li>
                                        </c:forEach>
                                    </ul></td></tr>
                                </table>
                            </td>
                            
                            <td  style="vertical-align:top">
                                <table class="access">
                                    <tr><td><ul>
                                        <c:forEach items="${updatedUser.selectedBuyersList}" var="buyer" varStatus="targetIndex">
                                            <li><c:out value="${buyer.buyerName}" /></li>
                                        </c:forEach>
                                    </ul></td></tr>
                                </table>
                            </td>
                        </tr>
                        
                    </table>
                </div>
                </s:if>
			    <div class="space"></div>
			    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.confirm.rolearea.title" />', width:275">
			  		<table class="commtable">
			  			<tr class="thead">
							<td width="50%">Previous</td>
							<td>Current</td>
						</tr>
						<tr>
			                <td style="vertical-align:top">
								<table class="access">
									<tr><td><ul>
			                            <c:forEach items="${updatedUser.oldVersion.oldSelectedRolesList}" var="role" varStatus="targetIndex">
			                   				<li><c:out value="${role.roleName}" />(<c:out value="${role.roleId}" />)</li>
			            				</c:forEach>
									</ul></td></tr>
								</table>
							</td>
							
							<td  style="vertical-align:top">
								<table class="access">
									<tr><td><ul>
										<c:forEach items="${updatedUser.selectedRolesList}" var="role" varStatus="targetIndex">
			                   				<li><c:out value="${role.roleName}" />(<c:out value="${role.roleId}" />)</li>
			            				</c:forEach>
									</ul></td></tr>
								</table>
							</td>
						</tr>
			       		
			        </table>
			    </div>
			    <div class="space"></div>
			    <s:if test="#updatedUser.userType == 2 || #updatedUser.userType == 4 || #updatedUser.userType == 6 || #updatedUser.userType == 7">
			    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.confirm.storearea.title" />', width:275">
			  		<table class="commtable">
			  			<tr class="thead">
							<td width="50%">Previous</td>
							<td>Current</td>
						</tr>
						<tr>
			                <td style="vertical-align:top">
								<table class="access">
									<tr><td><ul>
			                            <c:forEach items="${updatedUser.oldVersion.oldSelectedAreasList}" var="area" varStatus="targetIndex">
			                   				<li><c:out value="${area.areaCode}" /></li>
			            				</c:forEach>
									</ul></td></tr>
								</table>
							</td>
							
							<td  style="vertical-align:top">
								<table class="access">
									<tr><td><ul>
										<c:forEach items="${updatedUser.selectedAreasList}" var="area" varStatus="targetIndex">
			                   				<li><c:out value="${area.areaCode}" /></li>
			            				</c:forEach>
									</ul></td></tr>
								</table>
							</td>
						</tr>
			       		
			        </table>
			    </div>
			    <div class="space"></div>
			    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.confirm.store.title" />', width:275">
			  		<table class="commtable">
			  			<tr class="thead">
							<td width="50%">Previous</td>
							<td>Current</td>
						</tr>
						<tr>
			                <td style="vertical-align:top">
								<table class="access">
									<tr><td><ul>
			                            <c:forEach items="${updatedUser.oldVersion.oldSelectedStoresList}" var="store" varStatus="targetIndex">
			                   				<li><c:out value="${store.storeName}" /><c:out value="${store.storeCode}" /></li>
			            				</c:forEach>
									</ul></td></tr>
								</table>
							</td>
							
							<td  style="vertical-align:top">
								<table class="access">
									<tr><td><ul>
										<c:forEach items="${updatedUser.selectedStoresList}" var="store" varStatus="targetIndex">
			                   				<li><c:out value="${store.storeName}" /><c:out value="${store.storeCode}" /></li>
			            				</c:forEach>
									</ul></td></tr>
								</table>
							</td>
						</tr>
			       		
			        </table>
			    </div>
			    <div class="space"></div>
			    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.confirm.warehouse.title" />', width:275">
			  		<table class="commtable">
			  			<tr class="thead">
							<td width="50%">Previous</td>
							<td>Current</td>
						</tr>
						<tr>
			                <td style="vertical-align:top">
								<table class="access">
									<tr><td><ul>
			                            <c:forEach items="${updatedUser.oldVersion.oldSelectedWareHouseList}" var="wareHouse" varStatus="targetIndex">
			                   				<li><c:out value="${wareHouse.storeName}" /><c:out value="${wareHouse.storeCode}" /></li>
			            				</c:forEach>
									</ul></td></tr>
								</table>
							</td>
							
							<td  style="vertical-align:top">
								<table class="access">
									<tr><td><ul>
										<c:forEach items="${updatedUser.selectedWareHouseList}" var="wareHouse" varStatus="targetIndex">
			                   				<li><c:out value="${wareHouse.storeName}" /><c:out value="${wareHouse.storeCode}" /></li>
			            				</c:forEach>
									</ul></td></tr>
								</table>
							</td>
						</tr>
			       		
			        </table>
			    </div>
			    </s:if>
			    <s:if test="#updatedUser.userType == 2 || #updatedUser.userType == 4">
			    <div class="space"></div>
			    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.confirm.class.title" />', width:275">
			  		<table class="commtable">
			  			<tr class="thead">
							<td width="50%">Previous</td>
							<td>Current</td>
						</tr>
						<tr>
			                <td style="vertical-align:top">
								<table class="access">
									<tr><td><ul>
			                            <c:forEach items="${updatedUser.oldVersion.oldSelectedClassList}" var="class" varStatus="targetIndex">
			                   				<li>
			                                    <c:out value="${class.classCode}" />
			                                    <c:if test="${class.classOid != -1}">
			                                    [<c:out value="${class.buyerCode}" />]
			                                    </c:if>
			                                </li>
			            				</c:forEach>
									</ul></td></tr>
								</table>
							</td>
							
							<td  style="vertical-align:top">
								<table class="access">
									<tr><td><ul>
										<c:forEach items="${updatedUser.selectedClassList}" var="class" varStatus="targetIndex">
			                   				<li>
                                                <c:out value="${class.classCode}" />
                                                <c:if test="${class.classOid != -1}">
                                                [<c:out value="${class.buyerCode}" />]
                                                </c:if>
                                            </li>
			            				</c:forEach>
									</ul></td></tr>
								</table>
							</td>
						</tr>
			       		
			        </table>
			    </div>
			    <div class="space"></div>
			    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.confirm.subclass.title" />', width:275">
			  		<table class="commtable">
			  			<tr class="thead">
							<td width="50%">Previous</td>
							<td>Current</td>
						</tr>
						<tr>
			                <td style="vertical-align:top">
								<table class="access">
									<tr><td><ul>
			                            <c:forEach items="${updatedUser.oldVersion.oldSelectedSubclassList}" var="subclass" varStatus="targetIndex">
			                   				<li><c:out value="${subclass.subclassCode}" />
			                                <c:if test="${subclass.subclassOid != -1}">
			                                (<c:out value="${subclass.classCode}" />)
			                                [<c:out value="${subclass.buyerCode}" />]
			                                </c:if>
			                                </li>
			            				</c:forEach>
									</ul></td></tr>
								</table>
							</td>
							
							<td  style="vertical-align:top">
								<table class="access">
									<tr><td><ul>
										<c:forEach items="${updatedUser.selectedSubclassList}" var="subclass" varStatus="targetIndex">
			                   				<li><c:out value="${subclass.subclassCode}" />
                                            <c:if test="${subclass.subclassOid != -1}">
                                            (<c:out value="${subclass.classCode}" />)
                                            [<c:out value="${subclass.buyerCode}" />]
                                            </c:if>
                                            </li>
			            				</c:forEach>
									</ul></td></tr>
								</table>
							</td>
						</tr>
			       		
			        </table>
			    </div>
			    </s:if>
			    <div class="space"></div>
			    <s:if test="#updatedUser.userType != 1 && #updatedUser.userType != 6 && #updatedUser.userType != 7">
			    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.confirm.grouparea.title" />', width:275">
			    	<table class="commtable">
			            <tbody> 
			                <tr height="25px">
			                	<td width="30%">Previous</td>
             							<td width="2%">:</td>
			                    <td>
			                    	<s:property value="#updatedUser.oldVersion.groupName" />
								</td>
			                </tr>
							<tr>
								<td>Current</td>
								<td width="2%">:</td>
			                    <td class="required"><s:property value="#updatedUser.groupName" /></td>
			                </tr>
			            </tbody>
			        </table>
			    </div>
			    </s:if>
			    
			    <div class="space"></div>
			    
				<div id="ftcontent" align="center">
					<table id="control">
						<tr><td><s:text name="user.confirm.ctrl.ctrlstatus" /></td><td>:</td><td><s:property value="#updatedUser.ctrlStatusValue" /></td></tr>
						<tr><td><s:text name="user.confirm.ctrl.actionType" /></td><td>:</td><td ><s:property value="#updatedUser.actionTypeValue" /></td></tr>
						<tr><td><s:text name="user.confirm.ctrl.actor" /></td><td>:</td><td><s:property value="#updatedUser.actor" /></td></tr>
						<tr><td><s:text name="user.confirm.ctrl.actionDate" /></td><td>:</td><td><s:property value="#updatedUser.actionDate" /></td></tr>
					</table>
				</div>
			</td>
		</tr>
		<s:if test="!#sts.isLast()">
		<tr><td style="background-color:#FF6633"> &nbsp;</td></tr>
		</s:if>
		</s:iterator>
	</tbody>
</table>
</s:if>