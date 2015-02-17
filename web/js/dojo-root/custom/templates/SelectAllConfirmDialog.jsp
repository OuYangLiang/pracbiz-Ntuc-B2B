<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<div data-dojo-attach-point="contentNode">       
	<p>${message}</p>

	<div style="text-align:right">
		<button data-dojo-type="dijit/form/Button" data-dojo-attach-point="confirmDialogYesBtn">Select All</button>
		<button data-dojo-type="dijit/form/Button" data-dojo-attach-point="confirmDialogNoBtn">Select Current Page</button>
	</div>
</div>