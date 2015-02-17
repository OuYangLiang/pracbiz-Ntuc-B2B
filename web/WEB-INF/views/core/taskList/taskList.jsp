<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
	<title><s:text name="ntuc.web.portal"/></title>

	<style type="text/css">
		html, body {
            width: 100%;
            height: 100%;
            margin: 0;
            overflow:hidden;
        }
		#borderContainer {
            width: 100%;
            height: 100%;
        }
     
        
        .loadingMessage {
	        margin:0 auto;
	        width: 10px;
	        background: #fff url('<s:url value="/js/dojo-root/dijit/themes/claro/images/loadingAnimation.gif" />') no-repeat 10px 23px;
		    padding: 25px 40px;
		    color: #999;
		}
	</style>

  	<script type="text/javascript">
       require(["custom/B2BPortalBase",
		        "dijit/layout/ContentPane",
				"dijit/TitlePane",
				"dojo/parser",
				"dojo/_base/xhr",
				"dojo/dom",
				"dojo/dom-style",
				"dojo/domReady!"], 
    			function(
    				B2BPortalBase,
    				ContentPane, 
    				TitlePane,
    				parser,
    				xhr,
    				dom,
    				domStyle
					) {

				    	   parser.parse();
				           
				           (new B2BPortalBase()).init(
				               '<c:out value="${session.helpExHolder.helpNo}"/>',
				               '<c:out value="${session.helpExHolder.helpEmail}"/>',
				               '<s:property value="#session.commonParam.timeout" />',
				               '<s:url value="/logout.action" />');

						   loadingRole = function()
				           {
				        	   domStyle.set(dom.byId('loadingOverlay1'),'display','');
					           xhr.get({
	                               url: '<s:url value="/taskList/checkPendingRole.action" />',
	                               load: function(roleCount)
	                               {
	                                   if (roleCount==0)
	                                   {
	                                   		domStyle.set(dom.byId('loadingOverlay1'),'display','none');
	                                   }
	                                   else 
	                                   {
	                                	   domStyle.set(dom.byId('roleDiv'),'display','');
	                                	   dom.byId('div1').innerHTML='<ul><li><h3>You have <a href="<s:url value="/role/init.action?param.ctrlStatus=PENDING" />">' + roleCount + '</a> Role(s) to handle.</h3></li></ul>';
	                                	   domStyle.set(dom.byId('loadingOverlay1'),'display','none');
	                                	   haveRecord = true;
	                                   }
	                                   loadingUser();
	                               }
	                           });
					       }

						   loadingUser = function()
				           {
				        	   domStyle.set(dom.byId('loadingOverlay2'),'display','');
                               xhr.get({
                                   url: '<s:url value="/taskList/checkPendingUser.action" />',
                                   load: function(userCount)
                                   {
                                       if (userCount==0)
                                       {
                                    	    domStyle.set(dom.byId('loadingOverlay2'),'display','none');
                                       }
                                       else 
                                       {
                                       		domStyle.set(dom.byId('userDiv'),'display','');
                                    	    dom.byId('div2').innerHTML='<ul><li><h3>You have <a href="<s:url value="/user/init.action?userProfile.ctrlStatus=PENDING" />">' + userCount + '</a> User(s) to handle.</h3></li></ul>';
                                    	    domStyle.set(dom.byId('loadingOverlay2'),'display','none');
                                    	    haveRecord = true;
                                       }
                                       loadingGroup();
                                   }
                               });
					       }

						   loadingGroup = function()
				           {
				        	   domStyle.set(dom.byId('loadingOverlay3'),'display','');
                               xhr.get({
                                   url: '<s:url value="/taskList/checkPendingGroup.action" />',
                                   load: function(groupCount)
                                   {
                                       if (groupCount==0)
                                       {
                                    	   domStyle.set(dom.byId('loadingOverlay3'),'display','none');
                                       }
                                       else 
                                       {
                                    	   domStyle.set(dom.byId('groupDiv'),'display','');
                                    	   dom.byId('div3').innerHTML='<ul><li><h3>You have <a href="<s:url value="/group/init.action?param.ctrlStatus=PENDING" />">' + groupCount + '</a> Group(s) to handle.</h3></li></ul>';
                                    	   domStyle.set(dom.byId('loadingOverlay3'),'display','none');
                                    	   haveRecord = true;
                                       }
                                       if (!haveRecord)
   									   {
   											domStyle.set(dom.byId('noRecord'),'display','');
   									   }
                                   }
                               });
				           }

						   loadingBuyerUnreadInv = function()
				           {
				        	   domStyle.set(dom.byId('loadingOverlay4'),'display','');
				        	   xhr.get({
								   url: '<s:url value="/taskList/checkUnreadInv.action" />',
								   load: function(invCount)
								   {
									   if (invCount==0)
									   {
										   domStyle.set(dom.byId('loadingOverlay4'),'display','none');
									   }
									   else
									   {
										   domStyle.set(dom.byId('invDiv'),'display','');
                                    	   dom.byId('div4').innerHTML='<ul><li><h3>You have <a href="<s:url value="/inv/init.action?param.readStatus=UNREAD" />">' + invCount + '</a> E-Invoice(s) to handle.</h3></li></ul>';
                                    	   domStyle.set(dom.byId('loadingOverlay4'),'display','none');
                                    	   haveRecord = true;
									   }
									   loadingDn();
				           			}	
								});
					       }


						   loadingDn = function()
					       {
					    	   domStyle.set(dom.byId('loadingOverlay5'),'display','');
				        	   xhr.get({
								   url: '<s:url value="/taskList/checkPendingDn.action" />',
								   handleAs : "json",
								   load: function(holder)
								   {
									   if ((holder.dnSuppCount + holder.dnPriceCount + holder.dnCloseCount + holder.dnQtyCount)==0)
									   {
										   domStyle.set(dom.byId('loadingOverlay5'),'display','none');
									   }
									   else
									   {
										   domStyle.set(dom.byId('dnDiv'),'display','');
										   var html = '';
										   if (holder.dnPriceCount != 0)
										   {
											   html += '<ul><li><h3>You have <a href="<s:url value="/dn/init.action?param.dispute=true&param.closed=false&param.priceDisputed=true&param.priceStatus=PENDING&param.priceApprove=true" />">' + holder.dnPriceCount + '</a> Debit Note(s) to audit price.</h3></li></ul></br>';
										   }
										   if (holder.dnQtyCount != 0)
										   {
											   html += '<ul><li><h3>You have <a href="<s:url value="/dn/init.action?param.dispute=true&param.closed=false&param.qtyDisputed=true&param.qtyStatus=PENDING" />">' + holder.dnQtyCount + '</a> Debit Note(s) to audit qty.</h3></li></ul></br>';
										   }
										   if (holder.dnCloseCount != 0)
										   {
											   html += '<ul><li><h3>You have <a href="<s:url value="/dn/init.action?param.dispute=true&param.closed=false&param.pendingForClosing=true" />">' + holder.dnCloseCount + '</a> Debit Note(s) to close.</h3></li></ul></br>';
										   }
										   if (holder.dnSuppCount != 0)
										   {
											   html += '<ul><li><h3>You have <a href="<s:url value="/dn/init.action?param.close=true&param.dispute=false&param.dnType=STK_RTV" />">' + holder.dnSuppCount + '</a> Debit Note(s) to handle.</h3></li></ul>';
										   }
										   dom.byId('div5').innerHTML=html;
                                    	   domStyle.set(dom.byId('loadingOverlay5'),'display','none');
                                    	   haveRecord = true;
									   }
									   loadingPIGD();
						   		 	}		
				      		    });
						   }
						   
						   

                           loadingPIGD = function()
                           {
                               domStyle.set(dom.byId('loadingOverlay6'),'display','');
                               xhr.get({
                                   url: '<s:url value="/taskList/checkPendingPIGD.action" />',
                                   handleAs : "json",
                                   load: function(holder)
                                   {
                                       if ((holder.matchingCloseCount+holder.matchingAuditPriceCount+holder.matchingAuditQtyCount+holder.matchingApproveInvCount+holder.matchingSuppCount)==0)
                                       {
                                           domStyle.set(dom.byId('loadingOverlay6'),'display','none');
                                       }
                                       else
                                       {
                                           domStyle.set(dom.byId('pigdDiv'),'display','');
                                           var html = '';
                                           if (holder.matchingAuditPriceCount != 0)
                                           {
                                               html += '<ul><li><h3>You have <a href="<s:url value="/poInvGrnDnMatching/init.action?param.statusPriceUnmatched=true&param.statusUnmatched=true&param.supplierStatus=REJECTED&param.priceStatus=PENDING&param.priceApprove=true" />">' + holder.matchingAuditPriceCount + '</a> PO-Invoice-GRN-DN Matching(s) to audit price.</h3></li></ul></br>';
                                           }
                                           if (holder.matchingAuditQtyCount != 0)
                                           {
                                               html += '<ul><li><h3>You have <a href="<s:url value="/poInvGrnDnMatching/init.action?param.statusQtyUnmatched=true&param.statusUnmatched=true&param.supplierStatus=REJECTED&param.qtyStatus=PENDING" />">' + holder.matchingAuditQtyCount + '</a> PO-Invoice-GRN-DN Matching(s) to audit qty.</h3></li></ul></br>';
                                           }
                                           if (holder.matchingCloseCount != 0)
                                           {
                                               html += '<ul><li><h3>You have <a href="<s:url value="/poInvGrnDnMatching/init.action?param.pendingForClosing=true&param.statusUnmatched=true&param.statusPriceUnmatched=true&param.statusQtyUnmatched=true&param.statusAmountUnmatched=true" />">' + holder.matchingCloseCount + '</a> PO-Invoice-GRN-DN Matching(s) to close.</h3></li></ul></br>';
                                           }
                                           if (holder.matchingApproveInvCount != 0)
                                           {
                                               html += '<ul><li><h3>You have <a href="<s:url value="/poInvGrnDnMatching/init.action?param.pendingForApproving=true&param.statusUnmatched=true&param.statusPriceUnmatched=true&param.statusQtyUnmatched=true&param.statusAmountUnmatched=true" />">' + holder.matchingApproveInvCount + '</a> PO-Invoice-GRN-DN Matching(s) to approve.</h3></li></ul></br>';
                                           }
                                           if (holder.matchingSuppCount != 0)
                                           {
                                               html += '<ul><li><h3>You have <a href="<s:url value="/poInvGrnDnMatching/init.action?param.statusUnmatched=true&param.statusPriceUnmatched=true&param.statusQtyUnmatched=true&param.statusAmountUnmatched=true&param.supplierStatus=PENDING&param.revised=false" />">' + holder.matchingSuppCount + '</a> PO-Invoice-GRN-DN Matching(s) to handle.</h3></li></ul>';
                                           }
                                           dom.byId('div6').innerHTML=html;
                                           domStyle.set(dom.byId('loadingOverlay6'),'display','none');
                                           haveRecord = true;
                                       }
                                       if (!haveRecord)
                                       {
                                            domStyle.set(dom.byId('noRecord'),'display','');
                                       }
                                    }       
                                });
                           }

				           var curUserType = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.userType" />';
						   var haveRecord = false;
				           
				           if (curUserType==1)
				           {
				        	   loadingRole();
				           }
				           else if(curUserType==2 || curUserType==4)
				           {
				        	   loadingDn();
				           }
				           else if(curUserType==3 || curUserType==5)
				           {
				        	   loadingDn();
				           }
				           else if(curUserType==6 || curUserType==7)
				           {
				        	   loadingDn();
				           }
       					});
	</script>
				
</head>


<body class="claro">
      <!-- Content Part -->
      <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'center'">
	      <div id="loadingOverlay1" style="display:none;">
			<div style="text-align:center">
			<div class="loadingMessage">Loading...</div>
			</div>
	      </div>
		  <div id="roleDiv" style="display:none;">
			   <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="taskList.title.role" />', width:275">
					<div id="div1"></div>
			  </div>
		  </div>
		  <br/>
		  <div id="loadingOverlay2" style="display:none;">
				<div style="text-align:center">
				<div class="loadingMessage">Loading...</div>
				</div>
		   </div>
		   <div id="userDiv" style="display:none;">
			   <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="taskList.title.user" />', width:275">
					<div id="div2"></div>
			  </div>
		  </div>
		  <br/>
		  <div id="loadingOverlay3" style="display:none;">
			<div style="text-align:center">
				<div class="loadingMessage">Loading...</div>
			</div>
		  </div>
		  <div id="groupDiv" style="display:none;">
			   <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="taskList.title.group" />', width:275">
					<div id="div3"></div>
			  </div>
		  </div>
		  <div id="loadingOverlay4" style="display:none;">
			<div style="text-align:center">
				<div class="loadingMessage">Loading...</div>
			</div>
		  </div>
		  <div id="invDiv" style="display:none;">
			   <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="taskList.title.invoice" />', width:275">
					<div id="div4"></div>
			  </div>
		  </div>
		  <br/>
		  <div id="loadingOverlay5" style="display:none;">
			<div style="text-align:center">
				<div class="loadingMessage">Loading...</div>
			</div>
		  </div>
		  <div id="dnDiv" style="display:none;">
               <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="taskList.title.dn" />', width:275">
                    <div id="div5"></div>
              </div>
          </div>
		  <div id="loadingOverlay6" style="display:none;">
			<div style="text-align:center">
				<div class="loadingMessage">Loading...</div>
			</div>
		  </div>
		  <div id="pigdDiv" style="display:none;">
               <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="taskList.title.pigd" />', width:275">
                    <div id="div6"></div>
              </div>
          </div>
		  <div id="noRecord" style="display:none;">
		  		<ul>
		  			<li>
		  				<h3> There are no tasks for you to handle.</h3>
		  			</li>
		  		</ul>
		  </div>
			 
     </div>
</body>
</html>
