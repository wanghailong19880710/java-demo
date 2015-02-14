<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript">
		$.ajax({
			url : "http://localhost:9000/customerservice/customers/123",
			dataType : "json"
		}).done(function(msg) {
			alert(msg);
		}).fail(function(jqXHR, textStatus) {
			alert(textStatus);
		});
	</script>
</body>
</html>