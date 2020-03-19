$(document).ready(function() {
	var url = "http://localhost:8080/registroclinico/find/" + $("#paciente_id").val();
	$("#resultsBlock").load(url);
	});

function mudaPágina(element) {
	var url = "http://localhost:8080/registroclinico/find/" + $("#paciente_id").val() + "?page=" + element.text;
	$("#resultsBlock").load(url);
}
function primeiraPagina() {
	var url = "http://localhost:8080/registroclinico/find/" + $("#paciente_id").val() + "?page=1";
	$("#resultsBlock").load(url);
}
function ultimaPagina() {
	var url = "http://localhost:8080/registroclinico/find/" + $("#paciente_id").val() + "?page=" + $("#ultimaPagina").val();
	$("#resultsBlock").load(url);
}
