function executar(botao) {
	$(botao).remove();
	$(".classid").remove();
	$("#div_botao").append("<button class='btn btn-outline-primary float-right' id='btn_submit' type='submit'>Salvar</button>");
	$("#div_botao").append("<button class='btn btn-outline-secondary mr-2 float-right' id='btn_cancelar' type='button' onclick='voltar(this)'>Cancelar</button>");
	$("input").prop("disabled", false);
	$("select").prop("disabled", false);
	$("textarea").prop("disabled", false);
	$("#convenio").prop("disabled", true);
	$("#tipo").prop("disabled", true);
	$('#form').find(':input').each(function(i, elem) {
	      $(this).data("previous-value", $(this).val());
	});
}

function voltar(botao) {
	$(botao).remove();
	$("#btn_submit").remove();
	$("#div_botao").append("<button type='button' class='btn btn-outline-danger classid float-right' data-toggle='modal' data-target='#myModal'>Excluir</button>");
	$("#div_botao").append("<button class='btn btn-outline-primary mr-2 float-right' id='btn_editar' type='button' onclick='executar(this)'>Editar</button>");
	$("input").prop("disabled", true);
	$("select").prop("disabled", true);
	$("textarea").prop("disabled", true);
	$('#form').find(':input').each(function(i, elem) {
        $(this).val($(this).data("previous-value"));
    });
}