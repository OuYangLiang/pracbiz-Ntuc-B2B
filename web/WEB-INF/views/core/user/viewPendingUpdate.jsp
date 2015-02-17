<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        require(
                [
                "custom/B2BPortalBase",
				"dojo/dom",
				"dijit/registry",
				"dojo/on",
				"dijit/form/Button",
				"dojo/parser",
				"dojo/_base/xhr",
				"dojo/domReady!"
				], 
				function(
				    B2BPortalBase,
				    dom,
				    registry,
				    on,
				    Button,
				    parser,
				    xhr
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');

                    var fn = function(url)
                    {
                        var oids = '<s:property value="userProfile.userOid" />';
                        console.log(oids);
                        xhr.get({
                                url: '<s:url value="/user/putParamIntoSession.action" />',
                                content: {selectedOids: oids},
                                load: function(data)
                                {
                                    changeToURL(url);
                                }
                            });
                    }
                    
                    if (dom.byId("approveBtn"))
                    {
                        on(registry.byId("approveBtn"), 'click', 
                            function()
                            {
                            	var csrfToken = dom.byId("csrfToken").value;
                                fn('<s:url value="/user/saveApprove.action?csrfToken=" />'+csrfToken);
                            }
                        );
                    }
                    
                    if (dom.byId("rejectBtn"))
                    {
                        on(registry.byId("rejectBtn"), 'click', 
                            function()
                            {
                        		var csrfToken = dom.byId("csrfToken").value;
                                fn('<s:url value="/user/saveReject.action?csrfToken=" />'+csrfToken);
                            }
                        );
                    }

                });
        function cancel()
        {
            changeToURL('<s:url value="/user/init.action?keepSp=Y" />');
        } 
    </script>
</head>

<body class="claro">
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test='#session.permitUrl.contains("/user/saveApprove.action") && #session.SESSION_CURRENT_USER_PROFILE.loginId!=userProfile.actor' >
            	<button id="approveBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.approve' /></button>
            </s:if>
            <s:if test='#session.permitUrl.contains("/user/saveReject.action") && #session.SESSION_CURRENT_USER_PROFILE.loginId!=userProfile.actor' >
            	<button id="rejectBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.reject' /></button>
            </s:if>
            <button id="cancelBtn" data-dojo-type="dijit.form.Button" onClick="cancel();" ><s:text name='button.cancel' /></button>
        </td></tr></tbody></table>
    </div>
	<!-- here is message area -->
	 
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <div class="pageBar">
        <div class="title"><s:text name='user.view.pageBar' /></div>
    </div>
    <form id="mainForm" name="mainForm" method="post" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.profilearea.title" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                	<td width="2px"></td>
                    <td width="30%"><s:text name='user.view.profilearea.userType' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="oldUserProfile.userTypeDesc" />
					</td>
                </tr>
                <s:if test="userProfile.userType!=oldUserProfile.userType" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required"><s:property value="userProfile.userTypeDesc" /></td>
                </tr>
                </s:if>
            </tbody>
        </table>
    </div>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.contactarea.title" />', width:275">
    	<table class="commtable">
            <tbody> 
            	<s:if test="oldUserProfile.userType == 3 || oldUserProfile.userType == 5">
                <tr height="25px" id="supplier">
                	<td width="2px"></td>
                    <td width="30%"><s:text name='user.view.contactarea.supplier' /></td>
                    <td width="2%">:</td>
                    <td><s:property value="oldUserProfile.companyName" /></td>
                </tr>
                </s:if>
                <s:if test="oldUserProfile.userType == 2 || oldUserProfile.userType == 4 || oldUserProfile.userType == 6 || oldUserProfile.userType == 7">
                <tr height="25px" id="buyer">
                	<td width="2px"></td>
                    <td width="30%"><s:text name='user.view.contactarea.buyer' /></td>
                    <td width="2%">:</td>
                    <td><s:property value="oldUserProfile.companyName" /></td>
                </tr>
                </s:if>
                <s:if test="userProfile.userType!=oldUserProfile.userType">
                	<s:if test="userProfile.userType == 3 || userProfile.userType == 5">
	                <tr height="25px" id="supplier">
	                	<td width="2px"></td>
	                    <td class="required" width="30%"><s:text name='user.view.contactarea.supplier' /></td>
	                    <td width="2%">:</td>
	                    <td class="required"><s:property value="userProfile.companyName" /></td>
	                </tr>
	                </s:if>
	                <s:if test="userProfile.userType == 2 || userProfile.userType == 4 || userProfile.userType == 6 || userProfile.userType == 7">
	                <tr height="25px" id="buyer">
	                	<td width="2px"></td>
	                    <td class="required" width="30%"><s:text name='user.view.contactarea.buyer' /></td>
	                    <td width="2%">:</td>
	                    <td class="required"><s:property value="userProfile.companyName" /></td>
	                </tr>
	                </s:if>
            	</s:if>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.loginId' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="oldUserProfile.loginId" />
                    </td>
                </tr>
                <s:if test="userProfile.loginId!=oldUserProfile.loginId" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required"><s:property value="userProfile.loginId" /></td>
                </tr>
                </s:if>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.salutation' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="oldUserProfile.salutation" />
                    </td>
                </tr>
                <s:if test="userProfile.salutation!=oldUserProfile.salutation" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required"><s:property value="userProfile.salutation" /></td>
                </tr>
                </s:if>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.sex' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:if test="oldUserProfile.gender != null"><s:text name='Value.%{oldUserProfile.gender }' /></s:if>
                    </td>
                </tr>
                <s:if test="userProfile.gender!=oldUserProfile.gender" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required">
                        <s:if test="userProfile.gender != null"><s:text name='Value.%{userProfile.gender }' /></s:if>
                    </td>
                </tr>
                </s:if>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.name' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="oldUserProfile.userName" />
                    </td>
                </tr>
                <s:if test="userProfile.userName!=oldUserProfile.userName" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required"><s:property value="userProfile.userName" /></td>
                </tr>
                </s:if>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.email' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="oldUserProfile.email" />
                    </td>
                </tr>
                <s:if test="userProfile.email!=oldUserProfile.email" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required"><s:property value="userProfile.email" /></td>
                </tr>
                </s:if>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.telephone' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="oldUserProfile.tel" />
                    </td>
                </tr>
                <s:if test="userProfile.tel!=oldUserProfile.tel" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required"><s:property value="userProfile.tel" /></td>
                </tr>
                </s:if>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.fax' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="oldUserProfile.fax" />
                    </td>
                </tr>
                <s:if test="userProfile.fax!=oldUserProfile.fax" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required"><s:property value="userProfile.fax" /></td>
                </tr>
                </s:if>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.mobile' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="oldUserProfile.mobile" />
                    </td>
                </tr>
                <s:if test="userProfile.mobile!=oldUserProfile.mobile" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required"><s:property value="userProfile.mobile" /></td>
                </tr>
                </s:if>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.active' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:if test="oldUserProfile.active">
                    		<s:text name='Value.Yes' />
                    	</s:if>
                    	<s:else>
                    		<s:text name='Value.No' />
                    	</s:else>
                    </td>
                </tr>
                <s:if test="userProfile.active!=oldUserProfile.active" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required">
                    	<s:if test="userProfile.active">
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
                    <td width="30%"><s:text name='user.view.contactarea.block' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:if test="oldUserProfile.blocked">
                    		<s:text name='Value.Yes' />
                    	</s:if>
                    	<s:else>
                    		<s:text name='Value.No' />
                    	</s:else>
                    </td>
                </tr>
                <s:if test="userProfile.blocked!=oldUserProfile.blocked" >
				<tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="required">
                    	<s:if test="userProfile.blocked">
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
    <s:if test="userProfile.userType == 2 || userProfile.userType == 4 || userProfile.userType == 6 || userProfile.userType == 7">
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.assignedBuyer.title" />', width:275">
        <table class="commtable">
            <tr class="thead">
                <td width="50%">Previous</td>
                <td>Current</td>
            </tr>
            <tr>
                <td style="vertical-align:top">
                    <table class="access">
                        <tr><td><ul>
                            <c:forEach items="${oldSelectedBuyersList}" var="buyer" varStatus="targetIndex">
                                   <tr><td><c:out value="${buyer.buyerName}" /></td></tr>
                            </c:forEach>
                        </ul></td></tr>
                    </table>
                </td>
                
                <td  style="vertical-align:top">
                    <table class="access">
                        <tr><td><ul>
                            <c:forEach items="${selectedBuyersList}" var="buyer" varStatus="targetIndex">
                                   <tr><td><c:out value="${buyer.buyerName}" /></td></tr>
                            </c:forEach>
                        </ul></td></tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    </s:if>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.rolearea.title" />', width:275">
  		<table class="commtable">
  			<tr class="thead">
				<td width="50%">Previous</td>
				<td>Current</td>
			</tr>
			<tr>
                <td style="vertical-align:top">
					<table class="access">
						<tr><td><ul>
                            <c:forEach items="${oldSelectedRolesList}" var="role" varStatus="targetIndex">
                   				<li><c:out value="${role.roleName}" />(<c:out value="${role.roleId}" />)</li>
            				</c:forEach>
						</ul></td></tr>
					</table>
				</td>
				
				<td  style="vertical-align:top">
					<table class="access">
						<tr><td><ul>
							<c:forEach items="${selectedRolesList}" var="role" varStatus="targetIndex">
                   				<li><c:out value="${role.roleName}" />(<c:out value="${role.roleId}" />)</li>
            				</c:forEach>
						</ul></td></tr>
					</table>
				</td>
			</tr>
       		
        </table>
    </div>
    <s:if test="userProfile.userType == 2 || userProfile.userType == 4 || userProfile.userType == 6 || userProfile.userType == 7">
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.storearea.title" />', width:275">
  		<table class="commtable">
  			<tr class="thead">
				<td width="50%">Previous</td>
				<td>Current</td>
			</tr>
			<tr>
                <td style="vertical-align:top">
					<table class="access">
						<tr><td><ul>
                            <c:forEach items="${oldSelectedAreasList}" var="area" varStatus="targetIndex">
                   				<li><c:out value="${area.areaCode}" /></li>
            				</c:forEach>
						</ul></td></tr>
					</table>
				</td>
				
				<td  style="vertical-align:top">
					<table class="access">
						<tr><td><ul>
							<c:forEach items="${selectedAreasList}" var="area" varStatus="targetIndex">
                   				<li><c:out value="${area.areaCode}" /></li>
            				</c:forEach>
						</ul></td></tr>
					</table>
				</td>
			</tr>
       		
        </table>
    </div>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.store.title" />', width:275">
  		<table class="commtable">
  			<tr class="thead">
				<td width="50%">Previous</td>
				<td>Current</td>
			</tr>
			<tr>
                <td style="vertical-align:top">
					<table class="access">
						<tr><td><ul>
                            <c:forEach items="${oldSelectedStoresList}" var="store" varStatus="targetIndex">
                   				<li><c:out value="${store.storeName}" /><c:out value="${store.storeCode}" /></li>
            				</c:forEach>
						</ul></td></tr>
					</table>
				</td>
				
				<td  style="vertical-align:top">
					<table class="access">
						<tr><td><ul>
							<c:forEach items="${selectedStoresList}" var="store" varStatus="targetIndex">
                   				<li><c:out value="${store.storeName}" /><c:out value="${store.storeCode}" /></li>
            				</c:forEach>
						</ul></td></tr>
					</table>
				</td>
			</tr>
       		
        </table>
    </div>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.warehouse.title" />', width:275">
  		<table class="commtable">
  			<tr class="thead">
				<td width="50%">Previous</td>
				<td>Current</td>
			</tr>
			<tr>
                <td style="vertical-align:top">
					<table class="access">
						<tr><td><ul>
                            <c:forEach items="${oldSelectedWareHouseList}" var="wareHouse" varStatus="targetIndex">
                   				<li><c:out value="${wareHouse.storeName}" /><c:out value="${wareHouse.storeCode}" /></li>
            				</c:forEach>
						</ul></td></tr>
					</table>
				</td>
				
				<td  style="vertical-align:top">
					<table class="access">
						<tr><td><ul>
							<c:forEach items="${selectedWareHouseList}" var="wareHouse" varStatus="targetIndex">
                   				<li><c:out value="${wareHouse.storeName}" /><c:out value="${wareHouse.storeCode}" /></li>
            				</c:forEach>
						</ul></td></tr>
					</table>
				</td>
			</tr>
       		
        </table>
    </div>
    </s:if>
    <s:if test="userProfile.userType == 2 || userProfile.userType == 4">
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.class.title" />', width:275">
  		<table class="commtable">
  			<tr class="thead">
				<td width="50%">Previous</td>
				<td>Current</td>
			</tr>
			<tr>
                <td style="vertical-align:top">
					<table class="access">
						<tr><td><ul>
                            <c:forEach items="${oldSelectedClassList}" var="class" varStatus="targetIndex">
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
							<c:forEach items="${selectedClassList}" var="class" varStatus="targetIndex">
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
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.subclass.title" />', width:275">
  		<table class="commtable">
  			<tr class="thead">
				<td width="50%">Previous</td>
				<td>Current</td>
			</tr>
			<tr>
                <td style="vertical-align:top">
					<table class="access">
						<tr><td><ul>
                            <c:forEach items="${oldSelectedSubclassList}" var="subclass" varStatus="targetIndex">
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
							<c:forEach items="${selectedSubclassList}" var="subclass" varStatus="targetIndex">
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
    <s:if test="userProfile.userType != 1 && userProfile.userType != 6 && userProfile.userType != 7">
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.grouparea.title" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                	<td width="30%">Previous</td>
                	<td width="2%">:</td>
                    <td>
                    	<s:property value="oldUserProfile.groupName" />
					</td>
                </tr>
				<tr>
					<td>Current</td>
					<td width="2%">:</td>
                    <td class="required"><s:property value="userProfile.groupName" /></td>
                </tr>
            </tbody>
        </table>
    </div>
    </s:if>
    
    <div class="space"></div>
    
	<div id="ftcontent" align="center">
		<table id="control">
			<tr><td><s:text name="user.view.ctrl.ctrlstatus" /></td><td>:</td><td><s:property value="userProfile.ctrlStatusValue" /></td></tr>
			<tr><td><s:text name="user.view.ctrl.actionType" /></td><td>:</td><td ><s:property value="userProfile.actionTypeValue" /></td></tr>
			<tr><td><s:text name="user.view.ctrl.actor" /></td><td>:</td><td><s:property value="userProfile.actor" /></td></tr>
			<tr><td><s:text name="user.view.ctrl.actionDate" /></td><td>:</td><td><s:property value="userProfile.actionDate" /></td></tr>
		</table>
	</div>
    </form>
</body>
</html>
