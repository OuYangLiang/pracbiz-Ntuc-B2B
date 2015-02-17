define([
    "dojo/_base/declare",
    "dojo/on",
    "dojox/grid/EnhancedGrid",
    "dojox/grid/enhanced/plugins/Pagination",
    "dojox/grid/_SelectionPreserver",
    "dojox/grid/enhanced/plugins/IndirectSelection",
    "dojox/grid/cells",
    "dojo/_base/xhr",
    "dijit/Tooltip",
    "dojox/data/QueryReadStore",
    "dojo/_base/array"
], function(declare, on,EnhancedGrid, Pagination, _SelectionPreserver, IndirectSelection,cells,xhr,Tooltip,QueryReadStore,array) {
     
    return declare(null, {
        grid:{},
        store:{},
        defaultPageSize:{},
        pageSizes:{},
        defaultPage:{},
        isPoPage:{},
        currentUserType:{},
        formatByItem:{},
        formatByStore:{},
        initGrid: function(pageId,initDataAction,findSummaryFieldAndTooltipsAction,arr)
        {
        	var me = this;
        	store = new QueryReadStore({url: initDataAction});
        	xhr.get({
            	url: findSummaryFieldAndTooltipsAction,
                handleAs: "json",
                content: {pageId: pageId},
                load:function(jsonData)
                {
                	// init grid layout
                	var layout=[];
    	            if (me.isPoPage == true)
                    {
	                    layout.push({field: "flag1", name: "View By Item", width: '5%', formatter: me.formatByItem});
	                    layout.push({field: "flag2", name: "View By Store", width: '5%', formatter: me.formatByStore});
                    }            
                    dojo.forEach(jsonData.gridLayoutList,function(item) 
            		{
            			if (item != null)
            			{	
            				if(arr != null && arr.length >= 0)
            				{	
            					for(var i = 0;i < arr.length; i++)
            					{	
            						if(item.fieldLabel == arr[i].field)
            						{
            							layout.push({field: item.fieldId, name: item.fieldLabel, width: item.fieldWidth  + '%', formatter: arr[i].method});
            							return;
            						}
            					}
            					layout.push({field: item.fieldId, name: item.fieldLabel, width: item.fieldWidth + '%'});
            				}
            				else
            				{
            					layout.push({field: item.fieldId, name: item.fieldLabel, width: item.fieldWidth + '%'});
            				}
            			}
            		});
                	// create grid
                	me.grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: layout,
                        
                        plugins: {
                            indirectSelection: {headerSelector:true, width: '3%', styles:'text-align: center;'},
                			pagination: me.initPagination()
                        }
                        
                    }, "grid");
                	
                	// init sort field
                	me.grid.canSort=function(cellIndex)
                	{
                		if (me.grid.getCell(cellIndex-1) === undefined)
                			return true;
                		
                		var fieldId = me.grid.getCell(cellIndex-1).field;

                		var sortMap = jsonData.sortMap;
                		for (var key in sortMap)
                		{
                			if (key == fieldId) return true;
                		}
                		
                		return false;
                	};
                	
                	me.grid.startup();
                	
                	// init tooltips
                	dojo.connect(me.grid, "onCellMouseOver", function(event)
                	{
                		var fieldId = me.grid.getCell(event.cellIndex).field;
                		if (fieldId == null)
                            return;
                		
                		var tooltipMap = jsonData.tooltips
                		
                		for (var key in tooltipMap)
                		{
                			if (key == fieldId)
                			{
                				var value="";
                				var showFlag = false;
                				dojo.forEach(tooltipMap[key],function(item)
                				{
            		    	       	if (item != null)
            		    	    	{
            		    	       		var tooltipField = item.tooltipFieldId;
            		    	       		var tooltipLabel = item.tooltipFieldLabel;
            		    	       		value += '<label style="font-weight:bold;">';
            		    	       		value +=tooltipLabel +' :</label> ';
            		    	       		var rowItem = me.grid.getItem(event.rowIndex);
            		    	       		if (rowItem == null) return;
            		    	       		var rowItemValue = me.grid.store.getValue(rowItem,tooltipField);
            		    	       		if (rowItemValue != null && rowItemValue != '')
            		    	       		{
            		    	       			showFlag = true;
            		    	       			var content = "";
            		    	       			var length = 0;
            		    	       			
            		                        for (var i=0;i < rowItemValue.length ; i++)
            		                        {
            		                        
            		                            length ++;
            		                            rowItemValue = rowItemValue.replace(new RegExp("new-line", 'g'), ",")
            		                            if (rowItemValue.charAt(i)=='\n')
            		                            {
            		                                content += '<br/>';
            		                                length = 0;
            		                            }
            		                            else
            		                            {
            		                                content += rowItemValue.charAt(i);
            		                                
            		                                if (length > 50)
            		                                {
            		                                    content += '<br/>';
            		                                    length = 0;
            		                                }
            		                            }
            		                        }
            		                        
            		                        value += content;
            		    	       		}
            		    	       		value += '<br/>';
            		    	    	}
            		    	    });
                				if (!showFlag)
                				{
                					return;
                				}
                				Tooltip.defaultPosition=["above","below"];
            			       	Tooltip.show(value, event.cellNode);
            			       	return;
                			}
                		}
                	});
                	
                	// remove tooltip
                	dojo.connect(me.grid, "onCellMouseOut", function(event){
                		Tooltip.hide(event.cellNode);
                	});
                	
                }
            });
        },
        
        getCountOfSelected: function()
        {
        	return this.grid.selection.getSelected().length;
        },
        
        getCountOfRow: function()
        {
        	return this.grid.rowCount;
        },
        
        getSelectedItemDocOids: function()
        {
        	var docOids = [];
        	array.forEach(this.grid.selection.getSelected(),function(item)
        	{
        		try
        		{
        			docOids.push(item.i);
        		}
        		catch(e)
        		{}
        	});
        	return docOids;
        },
        
        getPoSelectedItemDocOids: function()
        {
        	var docOids = [];
        	dojo.forEach(this.grid.selection.getSelected(),function(item)
			{
        		try
        		{
        			docOids.push(item.i);
        		}
        		catch(e)
        		{}
			});
        	return docOids;
        },
        
        setPageSize: function(pageSizes)
        {
        	this.pageSizes = pageSizes.split(",");
        },
        
        setDefaultPageSize: function(defaultPageSize)
        {
        	this.defaultPageSize = defaultPageSize;
        },
        
        setDefaultPage: function(defaultPage)
        {
        	this.defaultPage = defaultPage;
        },
        
        initPagination:function()
        {
        	var pagination = {
            		defaultPage:this.defaultPage,
            		defaultPageSize:this.defaultPageSize,
            		pageSizes:this.pageSizes,
            		position: "bottom",
                    maxPageStep: 7,
                    gotoButton: true,
                    description: true,
                    sizeSwitch: true,
                    pageStepper: true
            	};
        	return pagination;
        },
        
        setIsPoPage:function(isPoPage)
        {
        	this.isPoPage = isPoPage;
        },
        
        setFormatByItem: function(formatByItem)
        {
        	this.formatByItem = formatByItem;
        },
        
        setFormatByStore: function(formatByStore)
        {
        	this.formatByStore = formatByStore;
        },
        
        setCurrentUserType: function(currentUserType)
        {
        	this.currentUserType = currentUserType;
        }
    });
});
