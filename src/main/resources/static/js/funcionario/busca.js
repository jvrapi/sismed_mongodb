$(document).ready(function() {
				$( function() {
					$("#txtBusca").autocomplete({
						source: "buscar/2",
						minLength: 1,
						select: function (event, ui) {
							url = "http://localhost:8080/funcionario/editar/" + ui.item.value;
							document.location.href = url;
							return false;
						}
					});
				});
			});
			function muda() {
				$('#txtBusca').unmask();
				$('#txtBusca').val("");
				$("#dropdownMenu2").text("Matrícula");
				$("#dropdownMenu2").val("1");
				$( function() {
					$("#txtBusca").autocomplete({
						source: "http://localhost:8080/funcionario/buscar/1",
						minLength: 1,
						select: function (event, ui) {
							url = "http://localhost:8080/funcionario/editar/" + ui.item.value;
							document.location.href = url;
							return false;
						}
					});
				});
			}
			function muda2() {
				$('#txtBusca').unmask();
				$('#txtBusca').val("");
				$("#dropdownMenu2").text("Nome");
				$("#dropdownMenu2").val("2");
				$( function() {
					$("#txtBusca").autocomplete({
						source: "http://localhost:8080/funcionario/buscar/2",
						minLength: 2,
						select: function (event, ui) {
							url = "http://localhost:8080/funcionario/editar/" + ui.item.value;
							document.location.href = url;
							return false;
						}
					});
				});
			}
			
			function muda3() {
				$('#txtBusca').unmask();
				$('#txtBusca').val("");
				$('#txtBusca').mask('000.000.000-00');
				$("#dropdownMenu2").text("CPF");
				$("#dropdownMenu2").val("3");
				$( function() {
					$("#txtBusca").autocomplete({
						source: "http://localhost:8080/funcionario/buscar/3",
						minLength: 2,
						select: function (event, ui) {
							url = "http://localhost:8080/funcionario/editar/" + ui.item.value;
							document.location.href = url;
							return false;
						}
					});
				});
			}
			
			function muda4() {
				$('#txtBusca').unmask();
				$('#txtBusca').val("");
				$('#txtBusca').mask('(00) 9 0000-0000');
				$("#dropdownMenu2").text("Celular");
				$("#dropdownMenu2").val("4");
				$( function() {
					$("#txtBusca").autocomplete({
						source: "http://localhost:8080/funcionario/buscar/4",
						minLength: 3,
						select: function (event, ui) {
							url = "http://localhost:8080/funcionario/editar/" + ui.item.value;
							document.location.href = url;
							return false;
						}
					});
				});
			}
			
			function muda5() {
				$('#txtBusca').unmask();
				$('#txtBusca').val("");
				$("#dropdownMenu2").text("CRM");
				$("#dropdownMenu2").val("5");
				$( function() {
					$("#txtBusca").autocomplete({
						source: "http://localhost:8080/funcionario/buscar/5",
						minLength: 2,
						select: function (event, ui) {
							url = "http://localhost:8080/funcionario/editar/" + ui.item.value;
							document.location.href = url;
							return false;
						}
					});
				});
			}
			
			function muda6() {
				$('#txtBusca').unmask();
				$('#txtBusca').val("");
				$("#dropdownMenu2").text("Especialidade");
				$("#dropdownMenu2").val("6");
				$( function() {
					$("#txtBusca").autocomplete({
						source: "http://localhost:8080/funcionario/buscar/6",
						minLength: 2,
						select: function (event, ui) {
							url = "http://localhost:8080/funcionario/editar/" + ui.item.value;
							document.location.href = url;
							return false;
						}
					});
				});
			}