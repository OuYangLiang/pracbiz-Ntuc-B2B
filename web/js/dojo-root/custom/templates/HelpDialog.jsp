<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<div>       
	<table>
	    <tr>
	        <td class="login_txt"><b><s:text name="login.help.helpNo"/></b></td>
	        <td>:</td>
	        <td class="login_txt">${helpNo}</td>
	    </tr>
	            
	    <tr>
	        <td class="login_txt"><b><s:text name="login.help.helpEmail"/></b></td>
	        <td>:</td>
	        <td class="login_txt">${helpEmail}</td>
	    </tr>
	</table>
	<div style="text-align:right">
	    <button data-dojo-type="dijit/form/Button" data-dojo-attach-point="helpDialogOkBtn">OK</button>
	</div>
</div>