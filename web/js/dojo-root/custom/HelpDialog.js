define([
    "dojo/_base/declare",
    "dojo/on",
    "dijit/_WidgetBase",
    "dijit/_TemplatedMixin",
    "dijit/Dialog",
    "dijit/_WidgetsInTemplateMixin",
    "dijit/form/Button",
    "dojo/text!./templates/HelpDialog.jsp"

], function(declare, on, _WidgetBase, _TemplatedMixin, Dialog, _WidgetsInTemplateMixin, Button, template) {
     
    return declare([Dialog], {
        title: "Information",
        style: "width:250px;",
        helpNo: "",
    	helpEmail: "",

        constructor: function(/*Object*/ args) {
        	declare.safeMixin(this,args);
        	var helpNo = this.helpNo;
        	var helpEmail = this.helpEmail;
        	
        	var contentWidget = new (declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
            	templateString: template, //get template via dojo loader or so
            	helpNo: helpNo,
            	helpEmail: helpEmail
        	}));
        	contentWidget.startup();
        	this.content = contentWidget;
        	
        	var me = this;

        	on(this.content.helpDialogOkBtn, "click", function(){ me.hide();});
        }
    });
});