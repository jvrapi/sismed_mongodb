$(document).ready(function() {
	$( function() {
		$("#txtBusca").autocomplete({
			source: "http://localhost:8080/registroclinico/buscar/2",
			minLength: 3,
			select: function (event, ui) {
				url = "http://localhost:8080/registroclinico/cadastrar/" + ui.item.value2 + "/null";
				document.location.href = url;
				return false;
			}
		});
	});
});
function muda() {
	$("#dropdownMenu2").text("Prontuário");
	$("#dropdownMenu2").val("1");
	$( function() {
		$("#txtBusca").autocomplete({
			source: "http://localhost:8080/registroclinico/buscar/1",
			minLength: 1,
			select: function (event, ui) {
				url = "http://localhost:8080/registroclinico/cadastrar/" + ui.item.value2 + "/null";
				document.location.href = url;
				return false;
			}
		});
	});
}
function muda2() {
	$("#dropdownMenu2").text("Nome");
	$("#dropdownMenu2").val("2");
	$( function() {
		$("#txtBusca").autocomplete({
			source: "http://localhost:8080/registroclinico/buscar/2",
			minLength: 3,
			select: function (event, ui) {
				url = "http://localhost:8080/registroclinico/cadastrar/" + ui.item.value2 + "/null";
				document.location.href = url;
				return false;
			}
		});
	});
}