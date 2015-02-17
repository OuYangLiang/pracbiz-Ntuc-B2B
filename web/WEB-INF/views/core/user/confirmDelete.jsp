<s:if test="!mkDeleteUserProfileList.isEmpty()">
<table style="border: solid 15px #CC0000" width="99%">
	<tbody>
		<s:iterator id="deletedUser" value="mkDeleteUserProfileList" status="sts">
		<tr>
			<td>
				<div data-dojo-type="dijit.TitlePane"
					data-dojo-props="title:'<s:text name="user.confirm.profilearea.title" />', width:275">
					<table class="commtable">
						<tbody>
							<tr height="25px">
								<td width="2px"></td>
								<td width="30%"><s:text
										name='user.confirm.profilearea.userType' /></td>
								<td width="2%">:</td>
								<td><s:property value="#deletedUser.userTypeDesc" /></td>
							</tr>

						</tbody>
					</table>
				</div>
				<div class="space"></div>
				<div data-dojo-type="dijit.TitlePane"
					data-dojo-props="title:'<s:text name="user.confirm.contactarea.title" />', width:275">
					<table class="commtable">
						<tbody>
							<s:if
								test="#deletedUser.userType == 3 || #deletedUser.userType == 5">
								<tr height="25px" id="supplier">
									<td width="2px"></td>
									<td width="30%"><s:text
											name='user.confirm.contactarea.supplier' /></td>
									<td width="2%">:</td>
									<td><s:property value="#deletedUser.companyName" /></td>
								</tr>
							</s:if>
							<s:if
								test="#deletedUser.userType == 2 || #deletedUser.userType == 4
								 || #deletedUser.userType == 6 || #deletedUser.userType == 7">
								<tr height="25px" id="buyer">
									<td width="2px"></td>
									<td width="30%"><s:text
											name='user.confirm.contactarea.buyer' /></td>
									<td width="2%">:</td>
									<td><s:property value="#deletedUser.companyName" /></td>
								</tr>
							</s:if>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.loginId' /></td>
								<td width="2%">:</td>
								<td><s:property value="#deletedUser.loginId" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.salutation' /></td>
								<td width="2%">:</td>
								<td><s:property value="#deletedUser.salutation" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.sex' /></td>
								<td width="2%">:</td>
								<td><s:if test="#deletedUser.gender != null"><s:text name='Value.%{#deletedUser.gender }' /></s:if></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.name' /></td>
								<td width="2%">:</td>
								<td><s:property value="#deletedUser.userName" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.email' /></td>
								<td width="2%">:</td>
								<td><s:property value="#deletedUser.email" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.telephone' /></td>
								<td width="2%">:</td>
								<td><s:property value="#deletedUser.tel" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.fax' /></td>
								<td width="2%">:</td>
								<td><s:property value="#deletedUser.fax" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.mobile' /></td>
								<td width="2%">:</td>
								<td><s:property value="#deletedUser.mobile" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.active' /></td>
								<td width="2%">:</td>
								<td><s:if test="#deletedUser.active">
										<s:text name='Value.Yes' />
									</s:if> <s:else>
										<s:text name='Value.No' />
									</s:else></td>
							</tr>
							<tr height="25px" id="blocked">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.block' /></td>
								<td width="2%">:</td>
								<td><s:if test="#deletedUser.blocked">
										<s:text name='Value.Yes' />
									</s:if> <s:else>
										<s:text name='Value.No' />
									</s:else></td>
							</tr>
						</tbody>
					</table>
				</div>
				<s:if test="#deletedUser.userType == 2 || #deletedUser.userType == 4
                 || #deletedUser.userType == 6 || #deletedUser.userType == 7">
                <div class="space"></div>
                <div data-dojo-type="dijit.TitlePane"
                    data-dojo-props="title:'<s:text name="user.confirm.allowbuyer.title" />', width:275">
                    <table class="commtable">
                        <c:forEach items="${deletedUser.selectedBuyersList}" var="buyer"
                            varStatus="targetIndex">
                            <tr>
                                <td><c:out value="${buyer.buyerName}" /></td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
                </s:if>
				<div class="space"></div>
				<div data-dojo-type="dijit.TitlePane"
					data-dojo-props="title:'<s:text name="user.confirm.rolearea.title" />', width:275">
					<table class="commtable">
						<c:forEach items="${deletedUser.selectedRolesList}" var="role"
							varStatus="targetIndex">
							<tr>
								<td><c:out value="${role.roleName}" />(<c:out value="${role.roleId}" />)</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<s:if test="#deletedUser.userType == 2 || #deletedUser.userType == 4
				 || #deletedUser.userType == 6 || #deletedUser.userType == 7">
				<div class="space"></div>
				<div data-dojo-type="dijit.TitlePane"
					data-dojo-props="title:'<s:text name="user.confirm.storearea.title" />', width:275">
					<table class="commtable">
						<c:forEach items="${deletedUser.selectedAreasList}" var="area"
							varStatus="targetIndex">
							<tr>
								<td><c:out value="${area.areaCode}" /></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="space"></div>
				<div data-dojo-type="dijit.TitlePane"
					data-dojo-props="title:'<s:text name="user.confirm.store.title" />', width:275">
					<table class="commtable">
						<c:forEach items="${deletedUser.selectedStoresList}" var="store"
							varStatus="targetIndex">
							<tr>
								<td><c:out value="${store.storeName}" /><c:out value="${store.storeCode}" /></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="space"></div>
				<div data-dojo-type="dijit.TitlePane"
					data-dojo-props="title:'<s:text name="user.confirm.warehouse.title" />', width:275">
					<table class="commtable">
						<c:forEach items="${deletedUser.selectedWareHouseList}" var="wareHouse"
							varStatus="targetIndex">
							<tr>
								<td><c:out value="${wareHouse.storeName}" /><c:out value="${wareHouse.storeCode}" /></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				</s:if>
				<s:if test="#deletedUser.userType == 2 || #deletedUser.userType == 4">
				<div class="space"></div>
				<div data-dojo-type="dijit.TitlePane"
					data-dojo-props="title:'<s:text name="user.confirm.class.title" />', width:275">
					<table class="commtable">
						<c:forEach items="${deletedUser.selectedClassList}" var="class"
							varStatus="targetIndex">
							<tr><td>
                                <c:out value="${class.classCode}" />
                                <c:if test="${class.classOid != -1}">
                                [<c:out value="${class.buyerCode}" />]
                                </c:if>
                            </td></tr>
						</c:forEach>
					</table>
				</div>
				<div class="space"></div>
				<div data-dojo-type="dijit.TitlePane"
					data-dojo-props="title:'<s:text name="user.confirm.subclass.title" />', width:275">
					<table class="commtable">
						<c:forEach items="${deletedUser.selectedSubclassList}" var="subclass"
							varStatus="targetIndex">
							<tr><td><c:out value="${subclass.subclassCode}" />
                               <c:if test="${subclass.subclassOid != -1}">
                                  (<c:out value="${subclass.classCode}" />)
                                  [<c:out value="${subclass.buyerCode}" />]
                               </c:if>
                            </td></tr>
						</c:forEach>
					</table>
				</div>
				</s:if>
				<s:if test="#deletedUser.userType != 1 && #deletedUser.userType != 6 && #deletedUser.userType != 7">
				<div class="space"></div> 
				<div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.confirm.grouparea.title" />', width:275">
					<table class="commtable">
						<tbody>
							<tr height="25px">
								<td><s:property value="#deletedUser.groupName" /></td>
							</tr>
						</tbody>
					</table>
				</div>
				</s:if>
				<div class="space"></div>
 
				<div id="ftcontent" align="center">
					<table id="control">
						<tr><td><s:text name="user.confirm.ctrl.ctrlstatus" /></td><td>:</td><td><s:property value="#deletedUser.ctrlStatusValue" /></td></tr>
						<tr><td><s:text name="user.confirm.ctrl.actionType" /></td><td>:</td><td ><s:property value="#deletedUser.actionTypeValue" /></td></tr>
						<tr><td><s:text name="user.confirm.ctrl.actor" /></td><td>:</td><td><s:property value="#deletedUser.actor" /></td></tr>
						<tr><td><s:text name="user.confirm.ctrl.actionDate" /></td><td>:</td><td><s:property value="#deletedUser.actionDate" /></td></tr>
					</table>
				</div>
			</td>
		</tr>
		<s:if test="!#sts.isLast()">
		<tr><td style="background-color:#CC0000"> &nbsp;</td></tr>
		</s:if>
		</s:iterator>
	</tbody>
</table>
</s:if>