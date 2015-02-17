<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
    <style>
        @import "<s:url value='/js/dojo-root/dijit/themes/claro/claro.css'/>";
        @import "<s:url value='/js/dojo-root/dojo/resources/dojo.css'/>";
        @import "<s:url value='/js/dojo-root/dojo/resources/dnd.css'/>";
        @import "<s:url value='/css/claro/dnd.css' />";
        @import "<s:url value='/css/claro/overlay.css' />";
        @import "<s:url value='/css/claro/common.css' />";
        @import "<s:url value='/js/dojo-root/dojox/form/resources/CheckedMultiSelect.css' />";
    </style>
     <script type="text/javascript" src="<s:url value='/js/dojo-root/dojo/dojo.js' />"
        data-dojo-config="parseOnLoad: false, async: true, isDebug: true"></script>
     <script type="text/javascript">
        require([
                 "custom/B2BPortalBase",
                 "dojo/dom",
                 "dijit/registry",
                 "dojo/on",
                 "dojo/json",
                 "dojo/string",
                 "dojo/_base/xhr",
                 "dojo/parser",
                 "dijit/form/Select",
                 "dijit/layout/ContentPane", 
                 "dijit/form/ValidationTextBox",
                 "dijit/form/MultiSelect",
                 "dijit/TitlePane",
                 "custom/InformationDialog",
                 "custom/ConfirmDialog",
                 "dojo/domReady!"
                 ], 
                 function(
                     B2BPortalBase,
                     dom,
                     registry,
                     on,
                     JSON,
                     string,
                     xhr,
                     parser,
                     Select,
                     ContentPane,
                     ValidationTextBox,
                     MultiSelect,
                     TitlePane,
                     InformationDialog,
                     ConfirmDialog
                     )
        {
            parser.parse();
            
            var selectAll = function(options)
            {
                for (var i = 0; i < options.length; i++)
                {
                	options[i].selected = true;
                }
            };

            var sortSelect = function(src)
            {
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
            
            moveOperation = function(from, to)
            {
                var source;
                var target;
            	dojo.query("." + from).forEach(function(node, index, nodeList){
            		source = registry.byNode(node);
                });
            	dojo.query("." + to).forEach(function(node, index, nodeList){
            		target = registry.byNode(node);
                });
                target.addSelected(source);

                dojo.query("." + to).forEach(function(node, index, nodeList){
                	sortSelect(node);
                });
            }
            
            on(registry.byId("buyerOid"), "change" ,
                function(value)
                {
                    if(value == "")
                    {
                    	dom.byId("content").innerHTML="";
                    }
                    else
                    {
		             	xhr.get({
		                     url: '<s:url value="/admin/getPrivilegeDetail.action" />',
		                     content: {"buyerOid" : value},
		                     load: function(html)
		                     {
		                         dom.byId("content").innerHTML=html;
		                         parser.parse(dom.byId("content"));
		                     }
		                 });
                    }

                }
            );

            moveAll = function(from,to)
            {
                dojo.query("."+from).forEach(function(node, index, nodeList){
                    selectAll(node.options);
                    moveOperation(from,to);
                });
            };

            on(registry.byId("save"), 'click', 
                function()
                {
            	    var value = registry.byId("buyerOid").get("value");
            	    if(value == "")
            	    {
            	    	var infoDialog = new InformationDialog({message: '<s:text name="B2BPA0169"/>'});
                        infoDialog.show();
                        return;
            	    }
            	    var oldOptions ;
            	    var buyerSelectedOperationOids;
            	    var supplierSelectedOperationOids;
                    dojo.query(".s2").forEach(function(node, index, nodeList){
                        oldOptions = node;
                    });
                    for (var i = 0; i< oper2Store.length; i++)
                    {
                        oldOptions.add(new Option(oper2Store[i].text, oper2Store[i].value));
                    }
                    dojo.query(".s4").forEach(function(node, index, nodeList){
                        oldOptions = node;
                    });
                    for (var i = 0; i< oper4Store.length; i++)
                    {
                        oldOptions.add(new Option(oper4Store[i].text, oper4Store[i].value));
                    }
                    
                    
            	    dojo.query(".s2").forEach(function(node, index, nodeList){
                        source = registry.byNode(node);
                        source.getSelected().forEach(function(option){option.selected=false;});
                        source.invertSelection();
                        buyerSelectedOperationOids = source.value;
                    });
            	    dojo.query(".s4").forEach(function(node, index, nodeList){
                        source = registry.byNode(node);
                        source.getSelected().forEach(function(option){option.selected=false;});
                        source.invertSelection();
                        supplierSelectedOperationOids = source.value;
                    });

                    var csrfToken = dom.byId("csrfToken").value;
                     xhr.post({
                        url: '<s:url value="/admin/savePrivilege.action" />',
                        content: {"buyerOid" : value,
                                  "buyerSelectedOperationOids" : buyerSelectedOperationOids,
                                  "supplierSelectedOperationOids" : supplierSelectedOperationOids,
                                  "csrfToken" : csrfToken
                            },
                        load: function(result)
                        {
                            filterPrivilege("s2_filter","s2");
                            filterPrivilege("s4_filter","s4");
                           	var infoDialog = new InformationDialog({message: JSON.parse(result)});
                            infoDialog.show();
                        }
                    });
                }
            );

            on(registry.byId("reset"), 'click', 
                function(){
                    var value = registry.byId("buyerOid").get("value");
                    xhr.get({
                        url: '<s:url value="/admin/getPrivilegeDetail.action" />',
                        content: {"buyerOid" : value},
                        load: function(html)
                        {
                        	dom.byId("content").innerHTML=html;
                            parser.parse(dom.byId("content"));
                        }
                    });
                }
            );
            
            var oper1Store = new Array();
            var oper2Store = new Array();
            var oper3Store = new Array();
            var oper4Store = new Array();
            
            filterPrivilege = function(src,id)
            {
            	var store;
            	if (id=='s1')
            	{
            		store = oper1Store;
            	}
            	else if (id=='s2')
            	{
            		store = oper2Store;
            	}
            	else if (id=='s3')
            	{
            		store = oper3Store;
            	}
            	else if (id=='s4')
            	{
            		store = oper4Store;
            	}
                var inputValue;
                dojo.query("."+src).forEach(function(node, index, nodeList){
                	inputValue = string.trim(registry.byNode(node).get("value")).toUpperCase();
                });
                var oldOptions ;
                dojo.query("."+id).forEach(function(node, index, nodeList){
                	oldOptions = node;
                });
                
                for (var i = 0; i < oldOptions.length; i++)
                {
                    var option = oldOptions[i];
                    
                    if (option.text.toUpperCase().indexOf(inputValue) < 0)
                    {
                    	store.push({text: option.text, value: option.value});
                        oldOptions.remove(i);
                        i--;
                    }
                }
                
                for (var i = 0; i< store.length; i++)
                {
                    if (store[i].text.toUpperCase().indexOf(inputValue) >=0)
                    {
                        oldOptions.add(new Option(store[i].text, store[i].value));
                        store.splice(i,1);
                        i--;
                    }
                }

                dojo.query("."+id).forEach(function(node, index, nodeList){
                	sortSelect(node);
                });
            };
            
       }); 


     </script>

</head>
<body class="claro">
    <form id="mainForm" name="mainForm" method="post" >
    <div id="msg" data-dojo-type="dijit.layout.ContentPane" closable="true">
        <table class="btnContainer" style="width:97%">
            <tbody><tr><td>
                <s:if test="#session.permitUrl.contains('/admin/savePrivilege.action')" >
	                <button type="button" id="save" data-dojo-type="dijit.form.Button" ><s:text name="button.save" /></button>
	                <button type="button" id="reset" data-dojo-type="dijit.form.Button"><s:text name="button.reset" /></button>
                </s:if>
            </td></tr></tbody>
        </table>
        <div >
			<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
		</div>
        <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.retailer.privilege.profile" />', width:275" style="width:99%">
	        <table class="commtable">
	        <tbody>
	            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 1 " >
	                <tr>
	                    <td width="20">&nbsp;&nbsp;<s:text name="admin.retailer.privilege.profile.retailer" /></td>
	                    <td>:</td>
	                    <td>
	                        <s:select id="buyerOid" data-dojo-type="dijit.form.Select" name="buyerOid" list="buyers" 
	                            listKey="buyerOid" listValue="buyerName" headerKey="" headerValue="%{getText('select.select')}" theme="simple"/>
	                    </td>
	                </tr>
	            </s:if>
	            <s:else>
	                <tr>
	                    <td width="20">&nbsp;&nbsp;<s:text name="role.create.profile.buyer" /></td>
	                    <td>:</td>
	                    <td><s:property value="%{#session.SESSION_CURRENT_USER_PROFILE.userType}"/></td>
	                </tr>
	            </s:else>
	        </tbody>
	        </table>
	    </div>
        
	    <div id="content"></div>
    </div>
    </form>
</body>
</html>