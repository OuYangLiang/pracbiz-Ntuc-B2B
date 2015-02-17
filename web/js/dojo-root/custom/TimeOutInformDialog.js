define([
    "dojo/_base/declare",
    "dojo/on",
    "dijit/_WidgetBase",
    "dijit/_TemplatedMixin",
    "dijit/Dialog",
    "dijit/_WidgetsInTemplateMixin",
    "dijit/form/Button",
    "dojo/text!./templates/TimeOutInformDialog.jsp"
], function(declare, on, _WidgetBase, _TemplatedMixin, Dialog, _WidgetsInTemplateMixin, Button, timeoutTemplate) {
     
    return declare([Dialog], {
        title: "Timeout Inform",
        style: "width:250px;",
        message: "You will auto logout",

        yesBtnPressed: function()
        {
        
        },

        constructor: function(/*Object*/ args) {
        	declare.safeMixin(this,args);
        	var message = this.message;
        	
        	var contentWidget = new (declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        		templateString: timeoutTemplate, //get template via dojo loader or so
        		message: message
        	}));
        	contentWidget.startup();
        	this.content = contentWidget;
        	
        	var me = this;

        	on(this.content.timeoutDialogYesBtn, "click", function(){ me.yesBtnPressed();});
        }
    });
});