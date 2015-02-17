define([
    "dojo/_base/declare",
    "dojo/dom",
    "dojo/on",
    "dojo/dom-style",
    "dojo/_base/xhr",
    "dojo/date/locale",
    "custom/HelpDialog",
    "custom/TimeOutInformDialog",
    "dojo/text!./templates/HelpDialog.jsp",
    "dojo/text!./templates/TimeOutInformDialog.jsp",
    "dojo/_base/fx"
    
], function(declare, dom, on, domStyle, xhr, locale, HelpDialog, TimeOutInformDialog, template, timeoutTemplate, fx) {
	
	var timeId;
     
    return declare(null, {
    	
    	initTimeout: function(/*minute*/ duration, url)
        {
    		var index = url.indexOf("logout.action");
    		var url2 = url.substring(0, index) + "home.action";
    		
    		var count = duration * 60;
    		var informCount = 120;
    		var timeoutDialog;
    		var tick = function(){
    			count -= 1;
    			
    			
        		if (informCount == count)
        		{
        			timeoutDialog = new TimeOutInformDialog({
        				message: "You will auto logout within " + informCount/60 + " minute(s).",
       	             	yesBtnPressed: function(){
       	             		timeoutDialog.hide();
       	             		
	       	             	xhr.get({
	                            url: url2,
	                            load: function()
	                            {
	                                console.log("done");
	                            }
	                        });
	       	             	
	       	             	count = duration * 60;
                        }
       	         	});
       			 	timeoutDialog.show();
        		}
        		
    			if (count === 0)
    				window.location.href = url;
    		}
    		
    		timeId = window.setInterval(tick, 1000);
        },
    	
    	
    	initClock: function()
        {
            var currentTime = new Date();
            var tick = function(){
            
                currentTime.setSeconds(currentTime.getSeconds() + 1);
                dom.byId("clock").innerHTML = locale.format(currentTime, {selector: "time", timePattern: "dd/MM/yyyy HH:mm:ss a"});
            };
    
            window.setInterval(tick, 1000);
        },
        
        
        initHelpBox: function(helpNo, helpEmail)
        {
            on(dom.byId("helpBtn"), "click", function(){
                var helpDialog = new HelpDialog({
                    helpNo: helpNo,
                    helpEmail: helpEmail
                });
                helpDialog.show();
            });
        },
        
        
        initLoginTemplate: function(helpNo, helpEmail)
        {
            this.initHelpBox(helpNo, helpEmail);
        },
        
        
        hideOverlay: function()
        {
			setTimeout(function(){
				var loader = dom.byId("loadingOverlay");
				fx.fadeOut({ node: loader, duration: 800, onEnd: function(){ loader.style.display = "none"; }}).play();
			}, 1);

        },
        
    	
        init: function(helpNo, helpEmail, timeout, timeoutUrl)
        {
        	this.initTimeout(timeout, timeoutUrl);
        	
            this.initClock();
        
        	this.initHelpBox(helpNo, helpEmail);
        	
        	this.hideOverlay();
        },
        
        resetTimeout : function(timeout, timeoutUrl)
        {
        	window.clearInterval(timeId);
        	this.initTimeout(timeout, timeoutUrl);
        }
        
    });
});