define([
    "dojo/_base/declare",
    "dojo/on",
    "dijit/_WidgetBase",
    "dijit/_TemplatedMixin",
    "dijit/Dialog",
    "dijit/_WidgetsInTemplateMixin",
    "dijit/form/Button",
    "dojo/text!./templates/SelectAllConfirmDialog.jsp"

], function(declare, on, _WidgetBase, _TemplatedMixin, Dialog, _WidgetsInTemplateMixin, Button, template) {
     
    return declare([Dialog], {
        title: "Information",
        style: "width:250px;",
        message: "Are you Sure?",
        
        yesBtnPressed: function()
        {
        
        },
        
        noBtnPressed: function()
        {
        	
        },

        constructor: function(/*Object*/ args) {
        	declare.safeMixin(this,args);
        	var message = this.message;
        	
        	var contentWidget = new (declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
            	templateString: template, //get template via dojo loader or so
            	message: message
        	}));
        	contentWidget.startup();
        	this.content = contentWidget;
        	
        	var me = this;
        	
        	on(this.content.confirmDialogYesBtn, "click", function(){ me.hide();me.yesBtnPressed();});
        	on(this.content.confirmDialogNoBtn, "click", function(){ me.hide();me.noBtnPressed();});
        }
    });
});