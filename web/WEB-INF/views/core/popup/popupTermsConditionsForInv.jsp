<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
<title><s:text name="ntuc.web.portal"/></title>

<script>
	var radioCode="";
	var radioId;
	require([ "dojo/dom", "dojo/dom-style", "dijit/registry", "dojo/on",
			"dijit/form/Button", "dijit/form/Select",
            "dijit/form/RadioButton","dojo/parser",
			"dojo/_base/window", "dojo/_base/array",
            "dojo/_base/xhr", "dojo/domReady!" ],
			function(dom, domStyle, registry, on, Button, Select, 
                    RadioButton,
					parser, win, array, xhr) {
				parser.parse();

				on(registry.byId("colseBtn"), 'click', function() {
					window.close();
				});
				
				on(registry.byId("selectBtn"), 'click', function() {
					if (radioId == null)
					{
						alert('<s:text name="B2BPC0532"/>');
					}
                    xhr.get({
                            url: '<s:url value="/tp/putParamIntoSession.action" />',
                            content: {termConditionCode: radioCode, supplierOid: '<s:property value="tradingPartner.supplierOid" />'},
                            load: function(data)
                            {
                            	var tc1 = opener.document.getElementById("footerLine1");
            					var tc2 = opener.document.getElementById("footerLine2");
            					var tc3 = opener.document.getElementById("footerLine3");
            					var tc4 = opener.document.getElementById("footerLine4");
            					
            					tc1.value = dom.byId('tc1_'+radioId).textContent || dom.byId('tc1_'+radioId).innerText;
            					tc2.value = dom.byId('tc2_'+radioId).textContent || dom.byId('tc2_'+radioId).innerText;
            					tc3.value = dom.byId('tc3_'+radioId).textContent || dom.byId('tc3_'+radioId).innerText;
            					tc4.value = dom.byId('tc4_'+radioId).textContent || dom.byId('tc4_'+radioId).innerText;

            					if (tc1.value == 'undefined')
               					{
            						tc1.value = '';
               					}

            					if (tc2.value == 'undefined')
               					{
            						tc2.value = '';
               					}

            					if (tc3.value == 'undefined')
               					{
            						tc3.value = '';
               					}

            					if (tc4.value == 'undefined')
               					{
            						tc4.value = '';
               					}
            					
            					opener.changeInvHeaderForTermAndCondition('footerLine1');
            					opener.changeInvHeaderForTermAndCondition('footerLine2');
            					opener.changeInvHeaderForTermAndCondition('footerLine3');
            					opener.changeInvHeaderForTermAndCondition('footerLine4');
            					window.close();
                            }
                        });
				});

				
			});
			function chooseTC(radio)
			{
				var codeId = "code_" + radio.id;
				radioId = radio.id;
				radioCode = document.getElementById(codeId).innerHTML;
			}
</script>
</head>

<body>
	<!-- Button Area -->
	<div>
		<table class="btnContainer">
			<tbody>
				<tr>
					<td>
						<button id="selectBtn" data-dojo-type="dijit.form.Button">
							<s:text name='button.select' />
						</button>
						<button id="colseBtn" data-dojo-type="dijit.form.Button">
							<s:text name='button.close' />
						</button>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="pageBar">
        <div class="title"><s:text name='tp.popuTermsConditions.pageBar' /></div>
    </div>
	<!-- Content Area -->
	<table id="table1"
		style="margin-left: 8px; border: #C5DBEC 1px solid; font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #5c4b16"
		cellpadding="2px" width="95%">
		<tbody>
			<c:forEach items="${termsConditionsList}" var="tc" varStatus="vs">
				<tr>
					<td width="5%">
						<c:choose>
							<c:when test="${tradingPartner.tradingPartnerOid != null }">
								<c:choose>
								<c:when test="${tradingPartner.termConditionOid == tc.termConditionOid }">
								<input type="radio" name="termsConditions" id="<c:out value='${vs.index}'/>" onclick="chooseTC(this)" data-dojo-type="dijit.form.RadioButton" checked="checked"/>
								</c:when>
								<c:otherwise>
								<input type="radio" name="termsConditions" id="<c:out value='${vs.index}'/>" onclick="chooseTC(this)" data-dojo-type="dijit.form.RadioButton"/>
								</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:choose>
								<c:when test="${tc.defaultSelected == true }">
								<input type="radio" name="termsConditions" id="<c:out value='${vs.index}'/>" onclick="chooseTC(this)" data-dojo-type="dijit.form.RadioButton" checked="checked"/>
								</c:when>
								<c:otherwise>
								<input type="radio" name="termsConditions" id="<c:out value='${vs.index}'/>" onclick="chooseTC(this)" data-dojo-type="dijit.form.RadioButton"/>
								</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
						
					</td>
					<td width="20%">&nbsp;&nbsp;<s:text name='tp.popuTermsConditions.code' /></td>
					<td width="1%">:</td>
					<td width="69%" id="code_<c:out value='${vs.index}'/>"><c:out value="${tc.termConditionCode}" /></td>
				</tr>
				<tr>
					<td></td>
					<td>&nbsp;&nbsp;<s:text name='tp.popuTermsConditions.termCondition' /></td>
					<td>:</td>
					<td id="tc1_<c:out value='${vs.index}'/>"><c:out value="${tc.termCondition1}" /></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td id="tc2_<c:out value='${vs.index}'/>"><c:out value="${tc.termCondition2}" /></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td id="tc3_<c:out value='${vs.index}'/>"><c:out value="${tc.termCondition3}" /></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td id="tc4_<c:out value='${vs.index}'/>"><c:out value="${tc.termCondition4}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
<c:if test="${ not empty ERROR_POPUP_TERM_CONDITION_FAILED }">
	<script language="javascript">
		var msg = "";
		    		    		
    	msg = '<c:out value="${ERROR_POPUP_TERM_CONDITION_FAILED}" />';        	
    								
		alert(msg);
		window.close();
	</script>		
</c:if>
<script type="text/javascript">
	initChooseTC();
	function initChooseTC()
	{
		var radios = document.getElementsByName("termsConditions");

		for (var i=0 ; i < radios.length ; i++)
		{
			var radio = radios[i];
			if (radio.checked)
			{
				var codeId = "code_" + radio.id;
				radioCode = document.getElementById(codeId).innerHTML;
				radioId = radio.id;
			}
		}
	}
</script>
</body>
</html>
