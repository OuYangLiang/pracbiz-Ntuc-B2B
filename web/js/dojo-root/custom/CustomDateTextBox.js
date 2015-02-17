define(["dojo/ready", "dojo/_base/declare", "dijit/form/_DateTimeTextBox", "dijit/form/DateTextBox", "dojo/date/locale"], 
function(ready, declare, _DateTimeTextBox, DateTextBox, locale){
	return declare([_DateTimeTextBox, DateTextBox], {
		customFormat: {selector: 'date', datePattern: 'dd/MM/yyyy', locale: 'en-us'},
		hasDownArrow: false,
        
        serialize: function(dateObject, options){
            return locale.format(dateObject, this.customFormat).toUpperCase();
        }
	});
});