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
				"dojo/parser",
				"dojo/domReady!"
				], 
				function(
				    B2BPortalBase,
				    dom,
				    registry,
				    on,
				    parser
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');

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
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.profilearea.title" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                	<td width="2px"></td>
                    <td width="30%"><s:text name='user.view.profilearea.userType' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="userProfile.userTypeDesc" />
					</td>
                </tr>
                
            </tbody>
        </table>
    </div>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.contactarea.title" />', width:275">
    	<table class="commtable">
            <tbody> 
            	<s:if test="userProfile.userType == 3 || userProfile.userType == 5">
                <tr height="25px" id="supplier">
                	<td width="2px"></td>
                    <td width="30%"><s:text name='user.view.contactarea.supplier' /></td>
                    <td width="2%">:</td>
                    <td><s:property value="userProfile.companyName" /></td>
                </tr>
                </s:if>
                <s:if test="userProfile.userType == 2 || userProfile.userType == 4 || userProfile.userType == 6 || userProfile.userType == 7">
                <tr height="25px" id="buyer">
                	<td width="2px"></td>
                    <td width="30%"><s:text name='user.view.contactarea.buyer' /></td>
                    <td width="2%">:</td>
                    <td><s:property value="userProfile.companyName" /></td>
                </tr>
                </s:if>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.loginId' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="userProfile.loginId" />
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.salutation' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="userProfile.salutation" />
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.sex' /></td>
                    <td width="2%">:</td>
                    <td>
                        <s:if test="userProfile.gender != null"><s:text name='Value.%{userProfile.gender }' /></s:if>
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.name' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="userProfile.userName" />
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.email' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="userProfile.email" />
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.telephone' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="userProfile.tel" />
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.fax' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="userProfile.fax" />
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.mobile' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="userProfile.mobile" />
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.active' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:if test="userProfile.active">
                    		<s:text name='Value.Yes' />
                    	</s:if>
                    	<s:else>
                    		<s:text name='Value.No' />
                    	</s:else>
                    </td>
                </tr>
                <tr height="25px" id="blocked">
                	<td></td>
                    <td width="30%"><s:text name='user.view.contactarea.block' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:if test="userProfile.blocked">
                    		<s:text name='Value.Yes' />
                    	</s:if>
                    	<s:else>
                    		<s:text name='Value.No' />
                    	</s:else>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <s:if test="userProfile.userType == 2 || userProfile.userType == 4 || userProfile.userType == 6 || userProfile.userType == 7">
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.assignedBuyer.title" />', width:275">
        <table class="commtable">
            <c:forEach items="${selectedBuyersList}" var="buyer" varStatus="targetIndex">
                   <tr><td><c:out value="${buyer.buyerName}" /></td></tr>
            </c:forEach>
        </table>
    </div>
    </s:if>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.rolearea.title" />', width:275">
  		<table class="commtable">
       		<c:forEach items="${selectedRolesList}" var="role" varStatus="targetIndex">
                   <tr><td><c:out value="${role.roleName}" />(<c:out value="${role.roleId}" />)</td></tr>
            </c:forEach>
        </table>
    </div>
    <s:if test="userProfile.userType == 2 || userProfile.userType == 4 || userProfile.userType == 6 || userProfile.userType == 7">
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.storearea.title" />', width:275">
  		<table class="commtable">
       		<c:forEach items="${selectedAreasList}" var="area" varStatus="targetIndex">
                   <tr><td><c:out value="${area.areaCode}" /></td></tr>
            </c:forEach>
        </table>
    </div>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.store.title" />', width:275">
  		<table class="commtable">
       		<c:forEach items="${selectedStoresList}" var="store" varStatus="targetIndex">
                   <tr><td><c:out value="${store.storeName}" /><c:out value="${store.storeCode}" /></td></tr>
            </c:forEach>
        </table>
    </div>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.warehouse.title" />', width:275">
  		<table class="commtable">
       		<c:forEach items="${selectedWareHouseList}" var="wareHouse" varStatus="targetIndex">
                   <tr><td><c:out value="${wareHouse.storeName}" /><c:out value="${wareHouse.storeCode}" /></td></tr>
            </c:forEach>
        </table>
    </div>
    </s:if>
    <s:if test="userProfile.userType == 2 || userProfile.userType == 4">
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.class.title" />', width:275">
  		<table class="commtable">
       		<c:forEach items="${selectedClassList}" var="class" varStatus="targetIndex">
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
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.subclass.title" />', width:275">
  		<table class="commtable">
       		<c:forEach items="${selectedSubclassList}" var="subclass" varStatus="targetIndex">
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
    <div class="space"></div>
    <s:if test="userProfile.userType == 2 || userProfile.userType == 3 || userProfile.userType == 4 || userProfile.userType == 5">
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.view.grouparea.title" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                    <td>
                    	<s:property value="userProfile.groupName" />
					</td>
                </tr>
            </tbody>
        </table>
    </div>
    </s:if>
    </form>
</body>
</html>
