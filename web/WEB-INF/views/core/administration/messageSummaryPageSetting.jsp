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
        
        .loadingMessage {
	        margin:0 auto;
	        width: 10px;
	        background: #fff url('<s:url value="/js/dojo-root/dijit/themes/claro/images/loadingAnimation.gif" />') no-repeat 10px 23px;
		    padding: 25px 40px;
		    color: #999;
	</style>
	 <script type="text/javascript" src="<s:url value='/js/dojo-root/dojo/dojo.js' />"
    	data-dojo-config="parseOnLoad: false, async: true, isDebug: true"></script>
     <script type="text/javascript">
        require([
                 "dojo/parser",
                 "dijit/registry",
                 "dojo/on",
                 "dojo/dom",
                 "dojo/query",
                 "custom/InformationDialog",
                 "dojo/_base/xhr",
                 "dojo/dnd/Source",
                 "dijit/layout/ContentPane", 
                 "dijit/TitlePane",
                 "dijit/form/Select",
                 "dijit/form/CheckBox",
                 "dijit/form/TextBox",
                 "dojox/form/CheckedMultiSelect",
                 "dojo/dom-style"
                 ],
        function(parser,registry,on,dom,query,InformationDialog,xhr,Source,ContentPane,TitlePane,Select,CheckBox,TextBox,CheckedMultiSelect,domStyle)
		{
    		parser.parse();
    		change = function(checkbox){
    			var item = checkbox.domNode.parentNode,
                handle = dojo.query("label", item);
                handle[checkbox.checked ? "addClass" : "removeClass"]("dojoDndHandle");
    		};
    		
    		on(registry.byId("summaryPageSetting.pageId"), 'change', 
   	    		function(value){
    				domStyle.set(dom.byId('loadingOverlay1'),'display','');
    				domStyle.set(dom.byId('fieldDiv'),'display','none');
	    			xhr.get({
	                    url: '<s:url value="/admin/getSummaryFields.action" />',
	                    content: {"summaryPageSetting.pageId" : value},
	                    load: function(html)
	                    {
	                    	dom.byId("fieldDiv").innerHTML=html;
                            parser.parse(dom.byId("fieldDiv"));
                            domStyle.set(dom.byId('loadingOverlay1'),'display','none');
                            domStyle.set(dom.byId('fieldDiv'),'display','');
	                    }
	                });
    		    }
    		);

    		parseJson = function(type)
    		{
        		var result = "";
    		    var avaliable = document.getElementsByName(type+"_available"); 
    		    var fieldWidth = document.getElementsByName(type+"_fieldWidth");
                var sortable = document.getElementsByName(type+"_sortable");
                for(var i = 0; i < avaliable.length; i++)
                {
                    var field = ""
                    +'{'
                        +'"fieldOid" : "' + avaliable[i].accessKey + '",'
                        +'"available" : "' + avaliable[i].checked + '",'
                        +'"fieldWidth" : "' + fieldWidth[i].value + '",'
                        +'"sortable" : "' + sortable[i].checked + '",'
                        +'"showOrder" : "' + (i+1) + '",';
                    var toolTipListNode = dom.byId("toolTipListNode_"+avaliable[i].accessKey);
                    var tooltips = toolTipListNode.getElementsByTagName("input");
                    var labels = toolTipListNode.getElementsByTagName("label");
                    var selectedToolTips = 'toolTips : [' ;
                    for(var j=0;j<labels.length;j++)
                    {
                        selectedToolTips +='{'
                            +'"fieldOid" : "' + avaliable[i].accessKey + '",'
                            +'"tooltipFieldOid" : "' + labels[j].accessKey + '",'
                            +'"showOrder" : "' + (j+1) + '"'
                        +'}';
                        if(j+1 < labels.length)
                        {
                            selectedToolTips += ',';
                        }
                    }
                    selectedToolTips += ']';
                    field += selectedToolTips + '}';
                    if(i+1 < avaliable.length)
                    {
                    	field += ',';
                    }
                    result += field;
                } 
                return result;
    		}

    		checkWidth = function(type)
    		{
    			var avaliable = document.getElementsByName(type+"_available");
    			var fieldWidth = document.getElementsByName(type+"_fieldWidth");
    			var sum = 0;
    			for(var i = 0; i < avaliable.length; i++)
    			{
        			var width = new Number(fieldWidth[i].value);
    			    if(isNaN(width) || width <=0 || width >87)
    			    {
    			    	var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0164'/>"});
                        infoDialog.show();
                        return false;
    			    }
    			    if(avaliable[i].checked)
    			    {
    			        sum += width;
    			    }
    			}
    			if(sum != 87)
    			{
        			var str = "";
        			if(type=='buyer')
        			{
            			str = "<s:text name='B2BPA0162'/>";
        			}
        			else
        			{
            			str = "<s:text name='B2BPA0163'/>";
        			}
    				var infoDialog = new InformationDialog({message: str});
                    infoDialog.show();
                    return false;
    			}
    			return true;
    		}
    		
    		on(registry.byId("save"), 'click', 
   	    		function(){
    			    if(!checkWidth('buyer'))
    			    {
        			    return false;
    			    }
    			    if(!checkWidth('supplier'))
    			    {
        			    return false;
    			    }
    			    var csrfToken = dom.byId("csrfToken").value;
	    			var jsonStr = "[" + parseJson("buyer") + "," + parseJson("supplier") +"]" ;
	    			xhr.post({
                        url: '<s:url value="/admin/saveMsgSummaryPageSetting.action" />',
                        content: {"jsonStr" : jsonStr, "csrfToken" : csrfToken},
                        handleAs: "json",
                        load: function(jsonData)
                        {
                              var infoDialog = new InformationDialog({message: jsonData});
                              infoDialog.show();
                              //resetCommonConfiguration();
                        }
                    });
    		    }
    		);
    		
    		on(registry.byId("reset"), 'click', 
   				function(){
	    			domStyle.set(dom.byId('loadingOverlay1'),'display','');
					domStyle.set(dom.byId('fieldDiv'),'display','none');
   				    var value = registry.byId("summaryPageSetting.pageId").get("value");
	                xhr.get({
	                    url: '<s:url value="/admin/getSummaryFields.action" />',
	                    content: {"summaryPageSetting.pageId" : value},
	                    load: function(html)
	                    {
	                        dom.byId("fieldDiv").innerHTML=html;
	                        parser.parse(dom.byId("fieldDiv"));
	                        domStyle.set(dom.byId('loadingOverlay1'),'display','none');
                            domStyle.set(dom.byId('fieldDiv'),'display','');
	                    }
	                });
	            }
    		);

    		selectAll = function(src, type)
    		{
    		    if(src.checked)
    		    {
    		    	query("."+type).forEach(function(node, index, nodeList){
        		    	registry.byNode(node).setChecked(true);
    		    	});
    		    }
    		    else
    		    {
    		    	query("."+type).forEach(function(node, index, nodeList){
                        registry.byNode(node).setChecked(false);
                    });
    		    }
    		}

	   }); 


     </script>

</head>
<body class="claro">
	<div id="msg" data-dojo-type="dijit.layout.ContentPane" title="Message Page Setting" closable="true">
		<table class="btnContainer" style="width:97%">
			<tbody><tr><td>
			    <s:if test="#session.permitUrl.contains('/admin/saveMsgSummaryPageSetting.action')" >
					<button type="button" id="save" data-dojo-type="dijit.form.Button" ><s:text name="button.save" /></button>
				    <button type="button" id="reset" data-dojo-type="dijit.form.Button"><s:text name="button.reset" /></button>
			    </s:if>
		    </td></tr></tbody>
		</table>
		<div >
			<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
		</div>
		<div id="msgType" data-dojo-type="dijit.TitlePane" 
            data-dojo-props="title:'Message Type', width:275" style="width:99%">
            <div class="commtable" >
   				<span>
   					<s:text name="admin.msgSummaryPageSetting.msgType" /> :
   				</span>
   				<span>
				    <s:select id="summaryPageSetting.pageId" name="summaryPageSetting.pageId" list="pages" headerKey="" headerValue="%{getText('select.select')}" theme="simple" data-dojo-type="dijit.form.Select"></s:select>
   				 </span>
	   		 </div>
		</div>

		<div class="space"></div> 
		<div id="loadingOverlay1" style="display:none;">
			<div style="text-align:center">
				<div class="loadingMessage">Loading...</div>
			</div>
	     </div>
		<div id="fieldDiv">
		</div>
	</div>



</body>
</html>