<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dijit/registry",
                "dojo/on",
                "dojo/parser",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
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
                    
    	        	   
                    on(registry.byId("cancelBtn"), "click", function(){
                        changeToURL('<s:url value="/buyer/init.action?keepSp=Y" />');
                    });
                });
                
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button data-dojo-type="dijit.form.Button" id="cancelBtn" ><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    
    <div class="pageBar">
        <div class="title"><s:text name="buyer.create"/></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <form id="mainForm" name="mainForm" method="post" >
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.create.panel.profile" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.buyerCode"/></td>
                <td width="20">:</td>
                <td><s:property value="param.buyerCode"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.buyerName"/></td>
                <td>:</td>
                <td><s:property value="param.buyerName"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.buyerAlias"/></td>
                <td>:</td>
                <td><s:property value="param.buyerAlias" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.regNo"/></td>
                <td>:</td>
                <td><s:property value="param.regNo" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.gstRegNo"/></td>
                <td>:</td>
                <td><s:property value="param.gstRegNo" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.gstPercent"/></td>
                <td>:</td>
                <td><s:property value="param.gstPercent" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.otherRegNo"/></td>
                <td>:</td>
                <td><s:property value="param.otherRegNo"  /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.branch"/></td>
                <td>:</td>
                <td><s:property value="param.branch" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.active"/></td>
                <td>:</td>
                <td><s:property value="param.active" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.logo"/></td>
                <td>:</td>
                <td>
	               	<s:if test="param.logo != null">
	               		<img src="<s:url value="/buyer/viewLogoImage.action"><s:param name="param.buyerOid" value="%{param.buyerOid}" /></s:url>"  height="45" width="115"/>
	               	</s:if>
				</td>
            </tr>
        </tbody>
        </table>
    </div>
    
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.create.panel.contact" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
            	<td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.address"/></td>
                <td width="20">:</td>
                <td><s:property value="param.address1" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:property value="param.address2" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:property value="param.address3" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:property value="param.address4" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.state"/></td>
                <td>:</td>
                <td><s:property value="param.state" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.postalCode"/></td>
                <td>:</td>
                <td><s:property value="param.postalCode" /></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.country"/></td>
                <td>:</td>
                <td>
					<s:property value="buyerEx.ctryDesc"/>
				</td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.currency"/></td>
                <td>:</td>
                <td>
					<s:property value="buyerEx.currDesc"/>
				</td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactName"/></td>
                <td>:</td>
                <td><s:property value="param.contactName" /></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactTel"/></td>
                <td>:</td>
                <td><s:property value="param.contactTel" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactMobile"/></td>
                <td>:</td>
                <td><s:property value="param.contactMobile" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactFax"/></td>
                <td>:</td>
                <td><s:property value="param.contactFax" /></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactEmail" /></td>
                <td>:</td>
                <td><s:property value="param.contactEmail" /></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.create.panel.communication" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="buyer.create.panel.communication.deploymentMode"/></td>
                <td width="20">:</td>
                <td><s:property value="param.deploymentMode.name()" /></td>
            </tr>
            <tr id="channelTr" style="<s:if test='param.deploymentMode.name() != "REMOTE"'>display: none</s:if>">
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="buyer.create.panel.communication.channel"/></td>
                <td width="20">:</td>
                <td><s:property value="param.channel" /></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.communication.mboxId"/></td>
                <td>:</td>
                <td><s:property value="param.mboxId" /></td>
            </tr>
        </tbody>
        </table>
    </div>
    </form>
</body>
</html>
