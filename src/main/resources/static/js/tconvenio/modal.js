$(document).ready(
		function() {
			var url = "http://localhost:8080/tconvenios/excluir/"+ $("#id").val() + "/"+ $("#convenio_id").val();

			$('#ok_confirm').click(function() {
				document.location.href = url;
			});
		});
