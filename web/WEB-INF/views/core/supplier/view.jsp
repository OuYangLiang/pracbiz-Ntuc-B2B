<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        require(["custom/B2BPortalBase","dojo/dom","dijit/registry",
                "dojo/on", "dijit/form/Button","dojo/parser","dojo/domReady!"], 
                function(B2BPortalBase,dom,registry,on,Button,parser)
               {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                   
                    on(registry.byId("cancelBtn"), 'click', function(){
                        changeToURL('<s:url value="/supplier/init.action?keepSp=Y" />');
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
        <div class="title"><s:text name="button.view"/></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <form id="mainForm" name="mainForm" method="post" >
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.create.panel.profile" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.supplierCode"/></td>
                <td width="20">:</td>
                <td><s:property value="supplier.supplierCode"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.supplierName"/></td>
                <td>:</td>
                <td><s:property value="supplier.supplierName"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.supplierAlias"/></td>
                <td>:</td>
                <td><s:property value="supplier.supplierAlias" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.regNo"/></td>
                <td>:</td>
                <td><s:property value="supplier.regNo" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.otherRegNo"/></td>
                <td>:</td>
                <td><s:property value="supplier.otherRegNo" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.source"/></td>
                <td>:</td>
                <td><s:property value="supplier.supplierSource" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.gst"/></td>
                <td>:</td>
                <td>
                	<s:if test='isGst == "Y"'>
                	<label for = "on">Turn On</label>
                	</s:if>
                	<s:else>
                	<label for = "off">Turn Off</label>
                	</s:else>
                </td>
            </tr>
            <s:if test='isGst == "N"'>
            <tr id="gstRegNo" style="display: none;">
            </s:if>
            <s:else>
            <tr id="gstRegNo">
            </s:else>
             <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.gstRegNo"/></td>
                <td>:</td>
                <td><s:property value="supplier.gstRegNo"/></td>
            </tr>
            <s:if test='isGst == "N"'>
            <tr id="gstPercent" style="display: none;">
            </s:if>
            <s:else>
            <tr id="gstPercent">
            </s:else>
            <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.gstPercent"/></td>
                <td>:</td>
                <td><s:property value="supplier.gstPercentStr" /></td>
            </tr>
            
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.autoInvNum"/></td>
                <td>:</td>
                <td>
                <s:if test='isStartNum == "Y"'>
                <label for="yes">Turn On</label>
                </s:if>
                <s:else>
                <label for="no">Turn off</label>
                </s:else>
                </td>
            </tr>
            
            <s:if test='isStartNum == "N"'>
             <tr style="display: none;" id="startNumber">
            </s:if>
           	<s:else>
            <tr id="startNumber">
            </s:else>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.startNum"/></td>
                <td>:</td>
                <td><s:property value="supplier.startNumberStr" /></td>
            </tr>
            
            <tr <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 1" >style="display: none;"</s:if>>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.active"/></td>
                <td>:</td>
                <td><s:property value="supplier.active" /></td>
            </tr>
            
            <tr <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 1" >style="display: none;"</s:if>>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.block"/></td>
                <td>:</td>
                <td><s:property value="supplier.blocked" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.logo"/></td>
                <td>:</td>
                <td>
                    <s:if test="supplier.logo != null">
                        <img src="<s:url value="/supplier/viewLogoImage.action"><s:param name="supplier.supplierOid" value="%{supplier.supplierOid}" /></s:url>"  height="45" width="115"/>
                    </s:if>
                </td>
            </tr>
             <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.dateCreated"/></td>
                <td>:</td>
                <td><s:property value="supplier.updateDate" /></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.create.panel.contact" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.address"/></td>
                <td width="20">:</td>
                <td><s:property value="supplier.address1" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:property value="supplier.address2" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:property value="supplier.address3" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:property value="supplier.address4" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.state"/></td>
                <td>:</td>
                <td><s:property value="supplier.state" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.postalCode"/></td>
                <td>:</td>
                <td><s:property value="supplier.postalCode" /></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.country"/></td>
                <td>:</td>
                <td>
                    <s:property value="supplier.ctryDesc"/>
                </td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.currency"/></td>
                <td>:</td>
                <td>
                    <s:property value="supplier.currDesc"/>
                </td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.contactName"/></td>
                <td>:</td>
                <td><s:property value="supplier.contactName" /></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.contactTel"/></td>
                <td>:</td>
                <td><s:property value="supplier.contactTel" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.contactMobile"/></td>
                <td>:</td>
                <td><s:property value="supplier.contactMobile" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.contactFax"/></td>
                <td>:</td>
                <td><s:property value="supplier.contactFax" /></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.contactEmail" /></td>
                <td>:</td>
                <td><s:property value="supplier.contactEmail" /></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.create.panel.communication" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.communication.deploymentMode"/></td>
                <td width="20">:</td>
                <td><s:property value="supplier.deploymentMode.name()" /></td>
            </tr>
            <tr id="channelTr" style="<s:if test='supplier.deploymentMode.name() != "REMOTE"'>display: none</s:if>">
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.communication.channel"/></td>
                <td width="20">:</td>
                <td><s:property value="supplier.channel" /></td>
            </tr>
            <tr>
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.communication.mboxId"/></td>
                <td width="20">:</td>
                <td><s:property value="supplier.mboxId" /></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.create.panel.client.document.setup" />', width:275" style="width:99%;
        <s:if test='supplier == null || supplier.deploymentMode == null || supplier.deploymentMode.name() != "LOCAL"'>display: none</s:if>">
        <table class="commtable">
        <tbody>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.clientEnabled"/></td>
                <td>:</td>
                <td><s:property value="supplier.clientEnabled" /></td>
            </tr>
            
            <tr>
                <td width="20"><span class="required"></span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.client.document.setup.inbound"/></td>
                <td width="20">:</td>
                <td>
                    <s:if test="supplier.requireTranslationIn">
                        <s:text name="supplier.create.panel.client.document.setup.translated"></s:text>
                    </s:if>
                    <s:else>
                        <s:text name="supplier.create.panel.client.document.setup.standard"></s:text>
                    </s:else>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    <s:checkbox name="supplier.requireReport" disabled="true"  data-dojo-type="dijit.form.CheckBox" theme="simple" />
                    <s:text name="supplier.create.panel.client.document.setup.pdf"></s:text>
                </td>
            </tr>
            <tr>
                <td width="20"><span class="required"></span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.client.document.setup.outbound"/></td>
                <td width="20">:</td>
                <td><s:checkbox name="supplier.requireTranslationOut" disabled="true" data-dojo-type="dijit.form.CheckBox" theme="simple" />
                <s:text name="supplier.create.panel.client.document.setup.require.translation"/></td>
            </tr>
        </tbody>
        </table>
    </div>   
    </form>
</body>
</html>
