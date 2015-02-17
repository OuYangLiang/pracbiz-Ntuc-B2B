<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <style type="text/css">
    @import "<s:url value='/js/dojo-root/dojox/grid/resources/Grid.css' />";
    @import "<s:url value='/js/dojo-root/dojox/grid/resources/claroGrid.css' />";
    @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/%{#session.layoutTheme}/EnhancedGrid.css' />";
    @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css' />";
    .updatetable{
            border:solid 8px #FF6633;
            font-family:Arial, Helvetica, sans-serif;
            font-size:11px;
        }
        .createtable {
            border:solid 8px #0099FF;
            font-family:Arial, Helvetica, sans-serif;
            font-size:11px;
        }
        .deletetable {
            border:solid 8px #CC0000;
            font-family:Arial, Helvetica, sans-serif;
            font-size:11px;
        }
        .innertable{
            font-family:Arial, Helvetica, sans-serif;
            font-size:9px;
        }
        .odd {
            background-color: #EEEEEE;
        }   
        .event {
            background-color: #FFFFFF;
        }   
    </style>
        
    <script>

    function showtip(){
    	var xhr = require("dojo/_base/xhr");
        xhr.get({
            url: '<s:url value="/auditTrail/getXmlDocument.action" />',
            content:{'param.auditTrailOid': <s:property value="param.auditTrailOid"/>},
            load: function(text)
            {
                try//Internet Explorer
                {
                    xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                    xmlDoc.async="false";
                    xmlDoc.loadXML(text);
                }
                catch(e)
                {
                    try//Firefox, Mozilla, Opera, etc.
                    {
                        parser=new DOMParser();
                        xmlDoc=parser.parseFromString(text,"text/xml");
                    }
                    catch(e)
                    {
                        alert(e.message);
                    }
                }
                var root = xmlDoc.documentElement;
                var record;
                for (var i=0;i<root.childNodes.length;i++)
                {
                    if (root.childNodes[i].nodeName == 'record')
                    {
	                    record = root.childNodes[i];
                    }
                }
                var table = initList(record, root.getAttribute('actionType'));
                document.getElementById("xmlContent").innerHTML = table.outerHTML;
            }
        });
    }

    function initList(record, actionType)
    {
        var table = document.createElement("table");
        var tbody = document.createElement("tbody");
        var fields = record.childNodes;
        var n = 0;
        for(var i = 0; i < fields.length; i++)
        {
            var field = fields[i];
            if(field.nodeName=='field')
            {
                n++;
                var childs = field.childNodes;
                var content = "";
                var newValue = "";
                for(var j = 0; j < childs.length; j++)
                {
                    var child = childs[j];
                    if(child.nodeName=='list')
                    {
                        var records = child.childNodes;
                        for(var m = 0; m < records.length; m++)
                        {
                            var r = records[m];
                            if(r.nodeName == 'record')
                            {
                                var at = r.getAttribute('actionType');
                                content += initList(r, at).outerHTML;
                            }
                        }
                    }
                    if(child.nodeName=="oldValue")
                    {
                        content = getNodeValue(child);
                    }
                    if(child.nodeName=="newValue")
                    {
                        newValue = getNodeValue(child);
                    }
                }
                var tr = document.createElement("tr");
                var s = "";
                var td1 = document.createElement("td");
                var td2 = document.createElement("td");
                if(field.getAttribute('isModified')=='true')
                {
	                var td3 = document.createElement("td");
                    td1.innerHTML=field.getAttribute('name');
                    td2.innerHTML=newValue;
                    td3.innerHTML=content;
                    td2.style.color='red';
	                tr.appendChild(td1);
	                tr.appendChild(td2);
	                tr.appendChild(td3);
	                s = "<td>"+field.getAttribute('name')+"</td><td>"+ content +"</td><td style='color:red;'>"+newValue+"</td>";
                }
                else
                {
                	td1.innerHTML=field.getAttribute('name');
                    td2.innerHTML=((content == "") ? getNodeValue(field) : content);
                    td2.setAttribute("colspan","2");
	                tr.appendChild(td1);
	                tr.appendChild(td2);
	                s = "<td>"+field.getAttribute('name')+"</td><td colspan='2'>"+((content == "") ? getNodeValue(field) : content) +"</td>";
                }
                if(n%2 == 0)
                {
                    tr.className = 'odd';
                }
                else
                {
                    tr.className = 'even';
                }
                tbody.appendChild(tr);
            }
        }
        table.appendChild(tbody);
        if(actionType=="CREATE")
        {
            table.className = 'createtable';
        }
        else if(actionType=="UPDATE")
        {
            table.className = 'updatetable';
        }
        else if(actionType=="DELETE")
        {
            table.className = 'deletetable';
        }
        table.width = '100%';
        table.style.tableLayout = 'fixed'; 
        table.style. wordBreak = 'break-all';
        return table;
    }

    function getNodeValue(element)
    {
   	    if (window.ActiveXObject)
   	    {
   	   	    return element.text;
   	    }
   	    else if(document.implementation && document.implementation.createDocument)
   	    {
   	        return element.textContent;
   	    }
   	    else
   	    {
   	   	    return "";
   	    }
    }
    
    require(
    		[
             "dojo/dom",
             "dijit/registry",
             "dojo/on",
             "dijit/form/Button",
             "dojox/data/QueryReadStore",
             "dojox/grid/EnhancedGrid",
             "dojox/grid/enhanced/plugins/Pagination",
             "dojox/grid/_SelectionPreserver",
             "dojox/grid/enhanced/plugins/IndirectSelection",
             "dojox/grid/cells",
             "dojo/parser",
             "dojo/_base/xhr",
             "custom/InformationDialog",
             "custom/ConfirmDialog",
             "dijit/form/Select",
             "custom/CustomDateTextBox",
             "dojo/domReady!"
             ], 
             function(
                 dom,
                 registry,
                 on,
                 Button,
                 QueryReadStore,
                 EnhancedGrid,
                 Pagination,
                 _SelectionPreserver,
                 IndirectSelection,
                 cells,
                 parser,
                 xhr,
                 InformationDialog,
                 ConfirmDialog,
                 Select,
                 CustomDateTextBox
                 )
            {
                parser.parse();
            
                
                showtip();
            });

    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
           	<button data-dojo-type="dijit.form.Button" id="closeBtn" onclick="javascript:window.close();"><s:text name="button.close" /></button>
        </td></tr></tbody></table>
    </div>

    <!-- Search Area -->
    <div id="xmlContent" data-dojo-type="dijit.TitlePane" >
        
    </div>
    
    <div class="space"></div>
</body>
</html>
