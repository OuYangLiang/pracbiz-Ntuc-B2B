<s:if test="!mkCreateUserProfileList.isEmpty()">
<table style="border: solid 15px #0099FF" width="99%">
	<tbody>
		<s:iterator id="createdUser" value="mkCreateUserProfileList" status="sts">
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
								<td><s:property value="#createdUser.userTypeDesc" /></td>
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
								test="#createdUser.userType == 3 || #createdUser.userType == 5">
								<tr height="25px" id="supplier">
									<td width="2px"></td>
									<td width="30%"><s:text
											name='user.confirm.contactarea.supplier' /></td>
									<td width="2%">:</td>
									<td><s:property value="#createdUser.companyName" /></td>
								</tr>
							</s:if>
							<s:if
								test="#createdUser.userType == 2 || #createdUser.userType == 4 
								|| #createdUser.userType == 6 || #createdUser.userType == 7">
								<tr height="25px" id="buyer">
									<td width="2px"></td>
									<td width="30%"><s:text
											name='user.confirm.contactarea.buyer' /></td>
									<td width="2%">:</td>
									<td><s:property value="#createdUser.companyName" /></td>
								</tr>
							</s:if>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.loginId' /></td>
								<td width="2%">:</td>
								<td><s:property value="#createdUser.loginId" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.salutation' /></td>
								<td width="2%">:</td>
								<td><s:property value="#createdUser.salutation" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.sex' /></td>
								<td width="2%">:</td>
								<td>
								    <s:if test="#createdUser.gender != null"><s:text name='Value.%{#createdUser.gender }' /></s:if>
								</td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.name' /></td>
								<td width="2%">:</td>
								<td><s:property value="#createdUser.userName" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.email' /></td>
								<td width="2%">:</td>
								<td><s:property value="#createdUser.email" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.telephone' /></td>
								<td width="2%">:</td>
								<td><s:property value="#createdUser.tel" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.fax' /></td>
								<td width="2%">:</td>
								<td><s:property value="#createdUser.fax" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.mobile' /></td>
								<td width="2%">:</td>
								<td><s:property value="#createdUser.mobile" /></td>
							</tr>
							<tr height="25px">
								<td></td>
								<td width="30%"><s:text
										name='user.confirm.contactarea.active' /></td>
								<td width="2%">:</td>
								<td><s:if test="#createdUser.active">
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
								<td><s:if test="#createdUser.blocked">
										<s:text name='Value.Yes' />
									</s:if> <s:else>
										<s:text name='Value.No' />
									</s:else></td>
							</tr>
						</tbody>
					</table>
				</div>
				<s:if test="#createdUser.userType == 2 || #createdUser.userType == 4
                 || #createdUser.userType == 6 || #createdUser.userType == 7">
				<div class="space"></div>
                <div data-dojo-type="dijit.TitlePane"
                    data-dojo-props="title:'<s:text name="user.confirm.allowbuyer.title" />', width:275">
                    <table class="commtable">
                        <c:forEach items="${createdUser.selectedBuyersList}" var="buyer"
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
						<c:forEach items="${createdUser.selectedRolesList}" var="role"
							varStatus="targetIndex">
							<tr>
								<td><c:out value="${role.roleName}" />(<c:out value="${role.roleId}" />)</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="space"></div>
				<s:if test="#createdUser.userType == 2 || #createdUser.userType == 4
				 || #createdUser.userType == 6 || #createdUser.userType == 7">
				<div data-dojo-type="dijit.TitlePane"
					data-dojo-props="title:'<s:text name="user.confirm.storearea.title" />', width:275">
					<table class="commtable">
						<c:forEach items="${createdUser.selectedAreasList}" var="area"
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
						<c:forEach items="${createdUser.selectedStoresList}" var="store"
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
						<c:forEach items="${createdUser.selectedWareHouseList}" var="wareHouse"
							varStatus="targetIndex">
							<tr>
								<td><c:out value="${wareHouse.storeName}" /><c:out value="${wareHouse.storeCode}" /></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				</s:if>
				<s:if test="#createdUser.userType == 2 || #createdUser.userType == 4">
				<div class="space"></div>
				<div data-dojo-type="dijit.TitlePane"
					data-dojo-props="title:'<s:text name="user.confirm.class.title" />', width:275">
					<table class="commtable">
						<c:forEach items="${createdUser.selectedClassList}" var="class"
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
						<c:forEach items="${createdUser.selectedSubclassList}" var="subclass"
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
				<div class="space"></div> <s:if test="#createdUser.userType != 1 && #createdUser.userType != 6 && #createdUser.userType != 7">
					<div data-dojo-type="dijit.TitlePane"
						data-dojo-props="title:'<s:text name="user.confirm.grouparea.title" />', width:275">
						<table class="commtable">
							<tbody>
								<tr height="25px">
									<td><s:property value="#createdUser.groupName" /></td>
								</tr>
							</tbody>
						</table>
					</div>
				</s:if>
				<div class="space"></div>
 
				<div id="ftcontent" align="center">
					<table id="control">
						<tr><td><s:text name="user.confirm.ctrl.ctrlstatus" /></td><td>:</td><td><s:property value="#createdUser.ctrlStatusValue" /></td></tr>
						<tr><td><s:text name="user.confirm.ctrl.actionType" /></td><td>:</td><td ><s:property value="#createdUser.actionTypeValue" /></td></tr>
						<tr><td><s:text name="user.confirm.ctrl.actor" /></td><td>:</td><td><s:property value="#createdUser.actor" /></td></tr>
						<tr><td><s:text name="user.confirm.ctrl.actionDate" /></td><td>:</td><td><s:property value="#createdUser.actionDate" /></td></tr>
					</table>
				</div>
			</td>
		</tr>
		<s:if test="!#sts.isLast()">
		<tr><td style="background-color:#0099FF"> &nbsp;</td></tr>
		</s:if>
		</s:iterator>
	</tbody>
</table>
</s:if>