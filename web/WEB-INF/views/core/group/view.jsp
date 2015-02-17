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
	           "dojo/_base/xhr",
	           "dijit/form/Button",
	           "dojo/parser",
	           "dojo/domReady!"
	           ], 
	           function(
	               B2BPortalBase,
	               dom,
	               registry,
	               on,
	               xhr,
	               Button,
	               parser
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
                        var oids = '<s:property value="param.groupOid" />';
                        
                        xhr.get({
                                url: '<s:url value="/group/putParamIntoSession.action" />',
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
                                fn('<s:url value="/group/saveApprove.action?csrfToken=" />'+csrfToken);
                            }
                        );
                    }
                    
                    if (dom.byId("rejectBtn"))
                    {
                        on(registry.byId("rejectBtn"), 'click', 
                            function()
                            {
                        		var csrfToken = dom.byId("csrfToken").value;
                                fn('<s:url value="/group/saveReject.action?csrfToken=" />'+csrfToken);
                            }
                        );
                    }
                    
                    if (dom.byId("withdrawBtn"))
                    {
                        on(registry.byId("withdrawBtn"), 'click', 
                            function()
                            {
                        		var csrfToken = dom.byId("csrfToken").value;
                                fn('<s:url value="/group/saveWithdraw.action?csrfToken=" />'+csrfToken);
                            }
                        );
                    }
                    
                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/group/init.action?keepSp=Y" />');
                        }
                    );
                });
                
    </script>
</head>

<body class="claro">
    <!-- Button Area -->
    <div>
        <table class="btnContainer">
        	<tbody>
        		<tr>
        			<td>
        				<s:if test="#session.commonParam.mkMode" >
            			<s:if test="#session.permitUrl.contains('/group/saveApprove.action') && #session.SESSION_CURRENT_USER_PROFILE.loginId!=groupProfile.actor" >
                			<button data-dojo-type="dijit/form/Button" id="approveBtn" ><s:text name="button.approve" /></button>
            			</s:if>
            			<s:if test="#session.permitUrl.contains('/group/saveReject.action') && #session.SESSION_CURRENT_USER_PROFILE.loginId!=groupProfile.actor" >
                			<button data-dojo-type="dijit/form/Button" id="rejectBtn" ><s:text name="button.reject" /></button>
            			</s:if>
           				<s:if test="#session.permitUrl.contains('/group/saveWithdraw.action') && #session.SESSION_CURRENT_USER_PROFILE.loginId==groupProfile.actor" >
                			<button data-dojo-type="dijit/form/Button" id="withdrawBtn" ><s:text name="button.withdraw" /></button>
            			</s:if>
            			</s:if>
            			<button data-dojo-type="dijit/form/Button" id="cancelBtn" ><s:text name="button.cancel" /></button>
        			</td>
        		</tr>
        	</tbody>
        </table>
    </div>
	<!-- here is message area -->
	 
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <div class="pageBar">
        <div class="title"><s:text name='group.view.pageBar' /></div>
    </div>
    <form id="mainForm" name="mainForm" method="post" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.view.profileInfo" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                	<td width="2px"></td>
                    <td width="30%"><s:text name='group.view.profileInfo.groupId' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="groupProfile.groupId" />
					</td>
                </tr>
                
                <tr height="25px">
                	<td width="2px"></td>
                    <td width="30%"><s:text name='group.view.profileInfo.groupDesc' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="groupProfile.groupName" />
					</td>
                </tr>
                
                <tr height="25px">
                	<td width="2px"></td>
                    <td width="30%"><s:text name='group.view.profileInfo.userType' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="groupProfile.userTypeId" />
					</td>
                </tr>
                <s:if test="groupProfile.userTypeOid == 3 || groupProfile.userTypeOid == 5">
                <tr height="25px" id="supplier">
                	<td width="2px"></td>
                    <td width="30%"><s:text name='group.view.profileInfo.supplier' /></td>
                    <td width="2%">:</td>
                    <td><s:property value="groupProfile.supplierName" /></td>
                </tr>
                </s:if>
                <s:if test="groupProfile.userTypeOid == 2 || groupProfile.userTypeOid == 4">
                <tr height="25px" id="buyer">
                	<td width="2px"></td>
                    <td width="30%"><s:text name='group.view.profileInfo.buyer' /></td>
                    <td width="2%">:</td>
                    <td><s:property value="groupProfile.buyerName" /></td>
                </tr>
                </s:if>
            </tbody>
        </table>
    </div>
    <div class="space"></div>
    
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.view.role" />', width:275">
    	<table class="commtable">
            <c:forEach items="${selectedRoles}" var="role" varStatus="targetIndex">
            	<tr>
                	<td>
                   		<c:out value="${role.roleName}" />(<c:out value="${role.roleId}" />)</td>
                   	</td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div class="space"></div>
    
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.view.user" />', width:275">
    	<table class="commtable">
    	   <s:iterator value="selectedUsers" var="innerItem" >
            	<tr>
                   <s:if test="#innerItem.locked">
                        <td class="required">
                            <s:property value="#innerItem.userName" /><s:text name="LastUpdateFrom.User"/>
                        </td>
                   </s:if>
                   <s:else>
                        <td>
                            <s:property value="#innerItem.userName" />
                        </td>
                   </s:else>
               </tr>
           </s:iterator>
        </table>
    </div>
    <div class="space"></div>
    
    <s:if test="groupProfile.userTypeOid == 2 || groupProfile.userTypeOid == 4">
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.view.supplier" />', width:275">
  		<table class="commtable">
       		<c:forEach items="${selectedSupps}" var="supplier" varStatus="targetIndex">
                <tr>
                	<td>
                   		<c:out value="${supplier.supplierName}" />
                   	</td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div class="space"></div>
    </s:if>
    
    <s:if test="groupProfile.userTypeOid == 3 || groupProfile.userTypeOid == 5">
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.view.tp" />', width:275">
  		<table class="commtable">
       		<c:forEach items="${selectedTps}" var="tradingPartner" varStatus="targetIndex">
                <tr>
                	<td>
                   		<c:out value="${tradingPartner.tradingPartnerDesc}" />
                   	</td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div class="space"></div>
    </s:if>
    
    </form>
</body>
</html>
