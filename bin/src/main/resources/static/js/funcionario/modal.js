$(document).ready(function() {
	var url = "http://localhost:8080/funcionario/excluir/" + $("#id").val();
	$('#ok_confirm').click(function() {
		document.location.href = url;
	}); 
});