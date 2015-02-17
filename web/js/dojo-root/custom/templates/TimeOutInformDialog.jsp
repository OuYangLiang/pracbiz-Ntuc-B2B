<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<div data-dojo-attach-point="contentNode">       
	<p>${message}</p>

	<div style="text-align:right">
		<button data-dojo-type="dijit/form/Button" data-dojo-attach-point="timeoutDialogYesBtn">OK</button>
	</div>
</div>