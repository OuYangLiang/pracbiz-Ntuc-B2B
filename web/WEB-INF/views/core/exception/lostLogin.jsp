<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
	<script type="text/javascript">
		function exe()
		{
			alert('<s:text name="message.exception.lost.login" />');
			window.location.href = "<s:url value='/home.action'/>";
		}
		exe();
	</script> 
</head>
<body>
</body>
</html>