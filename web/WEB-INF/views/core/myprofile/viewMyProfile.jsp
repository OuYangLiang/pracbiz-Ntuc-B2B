<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
        
    <script>

	    var sortSelect = function(src)
	    {
	        var dom = require("dojo/dom");
	        var tmpAry = new Array();
	        var oldOptions = dom.byId(src).options;
	        
	        for (var i = 0; i < oldOptions.length; i++)
	        {
	            tmpAry[i] = new Array();
	            tmpAry[i][0] = oldOptions[i].text;
	            tmpAry[i][1] = oldOptions[i].value;
	        }
	        
	        dom.byId(src).options.length = 0;
	        
	        tmpAry.sort(function(a, b){
	            if (a[0].toUpperCase() < b[0].toUpperCase())
	                return -1;
	            else if (a[0].toUpperCase() > b[0].toUpperCase())
	                return 1;
	                
	            return 0;
	        });
	        
	        for (var i=0;i<tmpAry.length;i++) {
	            var option = new Option(tmpAry[i][0], tmpAry[i][1]);
	            
	            dom.byId(src).options.add(option);
	        }
	    };
    
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dojo/dom-style",
                "dijit/registry",
                "dojo/on",
                "dojo/_base/xhr",
                "dijit/form/Button",
                "dojo/parser",
				"dijit/form/TextBox",
				"dijit/form/RadioButton",
				"dijit/form/ValidationTextBox",
				"dijit/Tooltip",
				"dojo/touch",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    domStyle,
                    registry,
                    on,
                    xhr,
                    Button,
                    parser,
                    TextBox,
                    RadioButton,
                    ValidationTextBox,
                    Tooltip,
                    touch
                    )
                {
    				
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                
                    on(dom.byId("yes"), 'click', function(){
                        if (dom.byId('yes').checked)
                        {
							domStyle.set('currentPwd', 'display', '');
							domStyle.set('newPwd', 'display', '');
							domStyle.set('confirmPwd', 'display', '');
                        }
                        else
                        {
							domStyle.set('currentPwd', 'display', 'none');
							domStyle.set('newPwd', 'display', 'none');
							domStyle.set('confirmPwd', 'display', 'none');
                        }
                    });
                	 
                    on(dom.byId("no"), 'click', function(){
                        if (dom.byId('no').checked)
                        {
							domStyle.set('currentPwd', 'display', 'none');
							domStyle.set('newPwd', 'display', 'none');
							domStyle.set('confirmPwd', 'display', 'none');
                        }
                        else
                        {
							domStyle.set('currentPwd', 'display', '');
							domStyle.set('newPwd', 'display', '');
							domStyle.set('confirmPwd', 'display', '');
                        }
                    });
                	
                    var selectAll = function(src)
                    {
                        var oldOptions = dom.byId(src).options;
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            oldOptions[i].selected = true;
                        }
                    };
                    
                    
                    setLoginIdSuffix = function()
                    {
                        var userType = "<s:property value='userProfile.userType'/>";
                        if (userType==3 || userType==5)
                        {
                            var companyOid = "<s:property value='userProfile.supplierOid'/>";
                            
                            xhr.get({
                                url: '<s:url value="/ajax/findSupplierByKey.action" />',
                                handleAs: "json",
                                content: {companyOid: companyOid},
                                load: function(data)
                                {
                                    if (data == null)
                                    {
                                        return;
                                    }
                                    else
                                    {
                                        dom.byId("loginIdSuffix").innerHTML='@'+data.supplierCode;
                                        dom.byId("userProfile.loginId").maxLength = (49-data.supplierCode.length);
                                    }
                                }
                            });
                        }
                        else
                        {
                            dom.byId("userProfile.loginId").maxLength = 50;
                            dom.byId("loginIdSuffix").innerHTML='';
                        }
                    };
                    
                    
                    on(registry.byId("save"), 'click', function(){
                    	var curUserType = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.userType" />';
                    	if (curUserType == '2' || curUserType == '4')
                    	{
                    		for (var i=0;i<index;i++)
                    		{
	                    		try
	                    		{
	                    			 selectAll("selectedSuppliers"+i);
	                    		}
	                    		catch (e)
	                    		{
	                    			
	                    		}
                    			
                    		}
                    	}
                        submitForm('searchForm', '<s:url value="/myprofile/saveEdit.action" />');
                    });
                    
                    
                    on(registry.byId("reset"), 'click', function(){
                        changeToURL('<s:url value="/myprofile/initEdit.action" />');
                    });
                    
                    
                    moveSelection = function(from, to)
                    {
                    	
                        var oldOptions = registry.byId(from).getSelected();
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            var option = oldOptions[i];
                            dom.byId(from).options.remove(option.index);
                            var item = new Option(option.text, option.value);
                            dom.byId(to).options.add(item);
                            item.selected = false;
                        }
                        sortSelect(to);
                    };
                    setLoginIdSuffix();

                    var sup1Store = new Array();
                    var sup2Store = new Array();

                    filterSup1 = function(inputId, selectId)
                    {
                        var inputValue = dom.byId(inputId).value.toUpperCase();
                        oldOptions = dom.byId(selectId).options;
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            var option = oldOptions[i];
                            
                            if (option.text.toUpperCase().indexOf(inputValue) < 0)
                            {
                                sup1Store.push({supplierName: option.text, supplierOid: option.value});
                                oldOptions.remove(i);
                                i--;
                            }
                        }
                        
                        for (var i = 0; i< sup1Store.length; i++)
                        {
                            if (sup1Store[i].supplierName.toUpperCase().indexOf(inputValue) >=0)
                            {
                                oldOptions.add(new Option(sup1Store[i].supplierName, sup1Store[i].supplierOid));
                                sup1Store.splice(i,1);
                                i--;
                            }
                        }
                        
                        sortSelect(selectId);
                    };
                    
                    filterSup2 = function(inputId, selectId)
                    {
                        var inputValue = dom.byId(inputId).value.toUpperCase();
                        oldOptions = dom.byId(selectId).options;
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            var option = oldOptions[i];
                            
                            if (option.text.toUpperCase().indexOf(inputValue) < 0)
                            {
                                sup2Store.push({supplierName: option.text, supplierOid: option.value});
                                oldOptions.remove(i);
                                i--;
                            }
                        }
                        
                        for (var i = 0; i< sup2Store.length; i++)
                        {
                            if (sup2Store[i].supplierName.toUpperCase().indexOf(inputValue) >=0)
                            {
                                oldOptions.add(new Option(sup2Store[i].supplierName, sup2Store[i].supplierOid));
                                sup2Store.splice(i,1);
                                i--;
                            }
                        }
                        
                        sortSelect(selectId);
                    };

                    loadTooltip = function(src)
    		        {
    		            Tooltip.defaultPosition=["after-centered","after"];
    		            Tooltip.show("Caps Lock is on.",src);
    		            return;
    		        };
    		        // remove tooltip
    		        closeTooltip = function(src)
    		        {
    		            Tooltip.hide(src);
    		        }

    		        on(registry.byId("currentPwd"), 'keypress', function(e)
    	    		{
    		        	detectCapsLock(e);
                    });

    		        on(registry.byId("currentPwd"), touch.out, function(e)
    	    		{
    		        	var o = e.target||e.srcElement;
    		        	closeTooltip(o);
                    });

    		        on(registry.byId("newPwd"), 'keypress', function(e)
    	    		{
    		        	detectCapsLock(e);
                    });

    		        on(registry.byId("newPwd"), touch.out, function(e)
    	    		{
    		        	var o = e.target||e.srcElement;
    		        	closeTooltip(o);
                    });

    		        on(registry.byId("confirmPwd"), 'keypress', function(e)
    	    		{
    		        	detectCapsLock(e);
                    });

    		        on(registry.byId("confirmPwd"), touch.out, function(e)
    	    		{
    		        	var o = e.target||e.srcElement;
    		        	closeTooltip(o);
                    });
                });
        
        var index = <s:property value='userProfile.favouriteLists.size' />;
        function add(flag)
        {
	        var availableSupplierList = eval(<s:property value='availableSupplierList' />);
            var sourceTable = document.getElementById("sourceTable");
            var allTbody = sourceTable.getElementsByTagName("tbody");
            var div = document.getElementById("targetDiv");
            var div1 = document.createElement("div");
            div1.id="itemDiv"+index;
                var s = '<div class="space"></div>'
                	    +'<table style="width:100%">'
		                +'<tbody id="tbody'+index+' />">'
		                +'<tr>'
		                    +'<td>'
		                    +'<table>'
		                        +'<tr>'
		                         +'<td width="2px"><span class="required">*</span> </td>'
		                         +'<td width="30%">&nbsp;&nbsp;<s:text name="myprofile.favouriteList.listCode"/></td>'
		                         +'<td>:</td>'
		                         +'<td><input name="userProfile.favouriteLists['+index+'].listCode" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" maxlength="20"/>'
		                         +'</td>'
		                        +'</tr>'
		                    +'</table>'
		                    +'</td>'
		                    +'<td>'
		                        +'<button onclick="remove('+index+');" data-dojo-type="dijit.form.Button" name="termConditions._'+index+'.button"><s:text name="button.remove" /></button>'
		                    +'</td>'
		                +'</tr>'
		                +'<tr>'
		                     +'<td width="350px">'
		                     +'<table>'
		                         +'<tr><td style="font-weight:bold"><s:text name="myprofile.favouriteList.availableList"/></td></tr>'
								 +'<tr><td><input id="sup1_filter'+index+'" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px" data-dojo-type="dijit/form/ValidationTextBox"/>'
								 +'<button onclick="filterSup1(sup1_filter'+index+', availableSuppliers'+index+');" data-dojo-type="dijit/form/Button">Search</button>'
		                         +'</td></tr>'
		                         +'<tr>'
		                             +'<td>'
		                             +'<select id="availableSuppliers'+index+'" name="userProfile.favouriteLists['+index+'].availableSupplierOids" style="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " data-dojo-type="dijit/form/MultiSelect" multiple="true">';
		                             for (var i=0; i<availableSupplierList.length; i++ )
		                             {
		                            	 s += '<option value="'+availableSupplierList[i]['supplierOid']+'">'+availableSupplierList[i]['supplierName']+'('+availableSupplierList[i]['supplierCode']+')'+'</option>'
		                             }
		                             s += '</select>'
		                             +'</td>'
		                         +'</tr>'
		                     +'</table>'
		                     +'</td>'
		                     +'<td width="100px">'
		                       +'<p><button type="button" onclick="moveSelection(\'availableSuppliers'+index+'\',\'selectedSuppliers'+index+'\');" data-dojo-type="dijit.form.Button">&gt;</button></p>'
		                       +'<p><button type="button" onclick="moveSelection(\'selectedSuppliers'+index+'\',\'availableSuppliers'+index+'\');" data-dojo-type="dijit.form.Button">&lt;</button></p>'
		                     +'</td>'
		                     +'<td width="350px">'
		                     +'<table>'
		                         +'<tr><td style="font-weight:bold"><s:text name="myprofile.favouriteList.selectedList"/></td></tr>'
		                         +'<tr><td><input id="sup2_filter'+index+'" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px" data-dojo-type="dijit/form/ValidationTextBox"/>'
								 +'<button onclick="filterSup2(sup2_filter'+index+', selectedSuppliers'+index+');" data-dojo-type="dijit/form/Button">Search</button>'
		                         +'</td></tr>'
		                         +'<tr>'
		                             +'<td>'
		                             +'<select id="selectedSuppliers'+index+'" name="userProfile.favouriteLists['+index+'].selectedSupplierOids" style="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " data-dojo-type="dijit/form/MultiSelect" multiple="true">'
                                     +'</select>'
		                             +'</td>'
		                         +'</tr>'
		                     +'</table>'
		                     +'</td>'
		                +'</tr>'
		            +'</tbody>'
		            +'</table>';
            div1.innerHTML=s;
            div.appendChild(div1);
            sortSelect("availableSuppliers"+index);
            sortSelect("selectedSuppliers"+index);
            if (flag)
            {
                var parser = require("dojo/parser");
                parser.parse(sourceTable);
            }
            index++;
        }
        
        function remove(index)
        {
        	var itemDiv = document.getElementById('itemDiv'+index);
            targetDiv.removeChild(itemDiv);
        }

        function  detectCapsLock(event)
        {
        	var e = event?event:(window.event?window.event:null);
            if (e)
            {
	            var o = e.target||e.srcElement;
	            var keyCode  =  e.keyCode||e.which; 
	            var isShift  =  e.shiftKey ||(keyCode  ==   16 ) || false ;
	            if (((keyCode >=   65   &&  keyCode  <=   90 )  &&   !isShift)
	            || ((keyCode >=   97   &&  keyCode  <=   122 )  &&  isShift))
	            {
	            	loadTooltip(o);
	            }
	            else
	            {
	            	closeTooltip(o);
	            }
            }
            else
            {
            	closeTooltip(o);
            }
        }

                
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button id="save" data-dojo-type="dijit/form/Button" ><s:text name="button.save" /></button>
            <button id="reset" data-dojo-type="dijit/form/Button" ><s:text name="button.reset" /></button>
        </td></tr></tbody></table>
    </div>

    <!--Add Error -->
	<div class="required">
		<s:actionerror />
		<s:fielderror />
	</div>
	
    <form id="searchForm" name="searchForm" method="post" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
	<!-- Content Area -->
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="myprofile.edit" />', width:275">
        <table class="commtable">
				<tbody>
					<tr>
						<td width="2px"><span class="required">*</span></td>
						<td width="30%">&nbsp;&nbsp;<s:text name="myprofile.edit.loginId" /></td>
						<td>:</td>
						<td><s:textfield id="userProfile.loginId" name="userProfile.loginId" data-dojo-props="required: true"
							data-dojo-type="dijit/form/ValidationTextBox" theme="simple" maxlength="50"/>
							<span id="loginIdSuffix"></span></td>
					</tr>
					<tr>
						<td></td>
						<td width="30%">&nbsp;&nbsp;<s:text name="myprofile.edit.changePassword" /></td>
						<td>:</td>
						<td>
							<input type="radio" name="isChangePwd" id="yes" value="Y" data-dojo-type="dijit.form.RadioButton"/>
							<label for="yes">Yes</label> 
							<input type="radio" id="no" checked="checked" name="isChangePwd" value="N" data-dojo-type="dijit.form.RadioButton"/>
							<label for="no" >No</label>
						</td>
					</tr>
					<tr id="currentPwd" style="display:none">
						<td><span class="required">*</span></td>
						<td width="30%">&nbsp;&nbsp;<s:text name="myprofile.edit.currentPwd" /></td>
						<td>:</td>
						<td><s:password name="currentPwd" id="currentPwd" onblur="closeTooltip(this);" data-dojo-props="required: true"
							data-dojo-type="dijit/form/ValidationTextBox" theme="simple" maxlength="150"/>
						</td>
					</tr>
					<tr id="newPwd" style="display:none">
						<td><span class="required">*</span></td>
						<td width="30%">&nbsp;&nbsp;<s:text name="myprofile.edit.newPwd" /></td>
						<td>:</td>
						<td><s:password name="newPwd" id="newPwd" data-dojo-props="required: true"
							data-dojo-type="dijit/form/ValidationTextBox" theme="simple" maxlength="150"/></td>
					</tr>
					<tr id="confirmPwd" style="display:none">
						<td><span class="required">*</span></td>
						<td width="30%">&nbsp;&nbsp;<s:text name="myprofile.edit.confirmPwd" /></td>
						<td>:</td>
						<td><s:password name="confirmPwd" id="confirmPwd" data-dojo-props="required: true"
							data-dojo-type="dijit/form/ValidationTextBox" theme="simple" maxlength="150"/></td>
					</tr>
					<tr>
						<td></td>
						<td width="30%">&nbsp;&nbsp;<s:text name="myprofile.edit.salutation" /></td>
						<td>:</td>
						<td><s:textfield name="userProfile.salutation" theme="simple" data-dojo-type="dijit/form/TextBox" maxlength="20"/></td>
					</tr>
					<tr>
						<td><span class="required">*</span></td>
						<td width="30%">&nbsp;&nbsp;<s:text name="myprofile.edit.userName" /></td>
						<td>:</td>
						<td><s:textfield data-dojo-type="dijit/form/ValidationTextBox"
							name="userProfile.userName" data-dojo-props="required: true" theme="simple" maxlength="50"/></td>
					</tr>
					<tr>
						<td><span class="required">*</span></td>
						<td width="30%">&nbsp;&nbsp;<s:text name="myprofile.edit.telephone" /></td>
						<td>:</td>
						<td><s:textfield name="userProfile.tel"
							data-dojo-type="dijit/form/ValidationTextBox" data-dojo-props="required: true" theme="simple" maxlength="30"/></td>
					</tr>
					<tr>
						<td></td>
						<td width="30%">&nbsp;&nbsp;<s:text name="myprofile.edit.mobile" /></td>
						<td>:</td>
						<td><s:textfield name="userProfile.mobile" 
						    data-dojo-type="dijit/form/TextBox"
							 theme="simple" maxlength="30"/></td>
					</tr>
					<tr>
						<td><span class="required">*</span></td>
						<td width="30%">&nbsp;&nbsp;<s:text name="myprofile.edit.email" /></td>
						<td>:</td>
						<td><s:textfield name="userProfile.email"
							data-dojo-type="dijit/form/ValidationTextBox" data-dojo-props="required: true" theme="simple" maxlength="100"/></td>
					</tr>
				</tbody>
			</table>
    </div>
    <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 2 || #session.SESSION_CURRENT_USER_PROFILE.userType == 4">
	    <div class="space"></div>
	    <div data-dojo-type="dijit/TitlePane" id="favoritesList" data-dojo-props="title:'<s:text name='myprofile.favouriteList.title'/>', width:275">
	        <table class="commtable" id="sourceTable">
	            <tr>
	               <td>
	                   <div>
					        <table class="btnContainer"><tbody><tr><td>
					            <button id="addBtn" onclick="add(true);" data-dojo-type="dijit/form/Button"><s:text name="button.add" /></button>
					        </td></tr></tbody></table>
					    </div>
	               </td>
	            </tr>
	            <tr>
	                <td>
	                <div id="targetDiv">
	                   <s:iterator value="userProfile.favouriteLists" status="status" id="item">
	                       <div id="itemDiv<s:property value="#status.index" />">
	                       <div class="space"></div>
	                       <table style="width:100%">
	                       <tbody id="tbody<s:property value="#status.index" />">
	                           <tr>
	                               <td>
	                               <table>
	                                   <tr>
		                                <td width="2px"><span class="required">*</span> </td>
	                                    <td width="30%">&nbsp;&nbsp;<s:text name="myprofile.favouriteList.listCode"/></td>
		                                <td>:</td>
		                                <td><input name="userProfile.favouriteLists[<s:property value="#status.index" />].listCode" value="<s:property value="#item.listCode"/>"  data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" maxlength="20"/>
		                                </td>
	                                   </tr>
	                               </table>
	                               </td>
	                               <td>
	                                   <button onclick="remove(<s:property value="#status.index" />);" data-dojo-type="dijit.form.Button" name="termConditions._<s:property value="#status.index" />.button"><s:text name="button.remove" /></button>
	                               </td>
	                           </tr>
	                           <tr>
					                <td width="350px">
					                <table>
					                    <tr><td style="font-weight:bold"><s:text name="myprofile.favouriteList.availableList"/></td></tr>
					                    <tr><td>
				                           <input id="sup1_filter<s:property value="#status.index"/>" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px" data-dojo-type="dijit/form/ValidationTextBox"/>
				                           <button onclick="filterSup1('sup1_filter<s:property value="#status.index"/>', 'availableSuppliers<s:property value="#status.index"/>');" data-dojo-type="dijit/form/Button">Search</button>
				                        </td></tr>
					                    <tr>
					                        <td>
					                        <s:select id="availableSuppliers%{#status.index}" name="userProfile.favouriteLists[%{#status.index}].availableSupplierOids" list="#item.availableSupplierList" multiple="true"
					                                listKey="supplierOid" listValue="supplierName + '(' + supplierCode + ')'"
					                                data-dojo-type="dijit/form/MultiSelect"
					                                cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
					                        </td>
					                    </tr>
					                </table>
					                </td>
					                <td width="100px">
					                  <p><button type="button" onclick="moveSelection('availableSuppliers<s:property value="#status.index" />','selectedSuppliers<s:property value="#status.index" />');" data-dojo-type="dijit.form.Button">&gt;</button></p>
					                  <p><button type="button" onclick="moveSelection('selectedSuppliers<s:property value="#status.index" />','availableSuppliers<s:property value="#status.index" />');" data-dojo-type="dijit.form.Button">&lt;</button></p>
					                </td>
					                <td width="350px">
					                <table>
					                    <tr><td style="font-weight:bold"><s:text name="myprofile.favouriteList.selectedList"/></td></tr>
					                    <tr><td>
				                            <input id="sup2_filter<s:property value="#status.index"/>" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px" data-dojo-type="dijit/form/ValidationTextBox"/>
				                            <button onclick="filterSup2('sup2_filter<s:property value="#status.index"/>', 'selectedSuppliers<s:property value="#status.index"/>');" data-dojo-type="dijit/form/Button">Search</button>
				                        </td></tr>
					                    <tr>
					                        <td>
					                        <s:select id="selectedSuppliers%{#status.index}" name="userProfile.favouriteLists[%{#status.index}].selectedSupplierOids" list="#item.selectedSupplierList" multiple="true"
					                                listKey="supplierOid" listValue="supplierName + '(' + supplierCode + ')'"
					                                data-dojo-type="dijit/form/MultiSelect"
					                                cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
					                        </td>
					                    </tr>
					                </table>
					                </td>
	                           </tr>
	                       </tbody>
	                       </table>
	                       </div>
	                   </s:iterator>
	                </div>
	                </td>
	              </tr>
	        </table>
	    </div>
    </s:if>
    </form>
    
    <div class="space"></div>
    
</body>
</html>
 