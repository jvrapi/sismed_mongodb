$(document).ready(function() {
	$('#ok_confirm').click(function() {
		document.location.href = "http://localhost:8080/registroclinico/excluir/" + $("#recipient-id").val() + "/" + $("#recipient-pacienteid").val();
	}); 
});
