package br.com.sismed.mongodb.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sismed.mongodb.domain.Agenda;
import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.domain.Custos;
import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.LabelValue;
import br.com.sismed.mongodb.domain.ListCustos;
import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.service.AgendaService;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.CustosService;
import br.com.sismed.mongodb.service.FuncionarioService;
import br.com.sismed.mongodb.service.PacienteService;

@Controller
@RequestMapping("/relatorio")
public class RelatorioController extends AbstractController {

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private ConvenioService convenioService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private CustosService service;

	@Autowired
	private AgendaService agendaService;

	@GetMapping("/listar")
	public String listar() {
		return "relatorio/lista";
	}

	@GetMapping
	public ResponseEntity<List<Custos>> mostrarDados() {
		LocalDate data1 = LocalDate.parse("2020-01-01");
		LocalDate data2 = LocalDate.now();
		String paciente = "5e7a4837065b4d61f67281b6";
		List<Custos> lista = service.pacientePeriodo(paciente, data1, data2);
		return ResponseEntity.ok().body(lista);
	}

	@GetMapping("/buscar/{id}")
	@ResponseBody
	public List<LabelValue> listar(@PathVariable("id") Integer id,
			@RequestParam(value = "term", required = false, defaultValue = "") String term) {
		List<LabelValue> suggeestions = new ArrayList<LabelValue>();
		if (id == 1) {
			List<Paciente> allPacientes = pacienteService.ListarPacNome(term);
			for (Paciente paciente : allPacientes) {
				LabelValue lv = new LabelValue();
				lv.setLabel(paciente.getNome());
				lv.setValue2(paciente.getId());
				suggeestions.add(lv);
			}
		} else if (id == 2) {
			List<Convenio> allConvenios = convenioService.ListarPorNome(term);
			for (Convenio convenio : allConvenios) {
				LabelValue lv = new LabelValue();
				lv.setLabel(convenio.getNome());
				lv.setValue2(convenio.getId());
				suggeestions.add(lv);
			}
		}

		else if (id == 3) {
			List<Funcionario> allFunc = funcionarioService.ListarFuncionarioNome(term);
			for (Funcionario f : allFunc) {

				LabelValue lv = new LabelValue();
				lv.setLabel(f.getNome());
				lv.setValue2(f.getId());
				suggeestions.add(lv);
			}
		}

		return suggeestions;

	}

	@PostMapping("/gerar")
	public String gerarRelatorio(@RequestParam("paciente") String paciente, @RequestParam("convenio") String convenio,
			@RequestParam("dataInicioValor") String dataInicio, @RequestParam("dataFimValor") String dataFim,
			@RequestParam("funcionario") String funcionario, RedirectAttributes attr, ModelMap model) {

		if (convenio == "" && dataInicio == "" && funcionario == "") {
			// so por paciente
			List<Custos> c = service.buscarPorPaciente(paciente);
			List<ListCustos> listarCustos = new ArrayList<ListCustos>();
			Paciente p = pacienteService.buscarPorId(paciente).get();

			for (Custos custos : c) {

				Funcionario f = funcionarioService.buscarporId(custos.getFuncionario()).get();
				Agenda agendamento = agendaService.buscarPorId(custos.getAgendamento()).get();
				Convenio convenioc = convenioService.buscarPorId(custos.getConvenio()).get();
				ListCustos lc = new ListCustos();
				lc.setFuncionario(f);
				lc.setAgendamento(agendamento);
				lc.setPaciente(p);
				lc.setData(custos.getData());
				lc.setValor(custos.getValor());
				lc.setConvenio(convenioc);
				listarCustos.add(lc);
			}
			model.addAttribute("resultado", listarCustos);

			if (service.buscarReceitaPorPaciente(paciente) == null) {
				model.addAttribute("receita", "O paciente " + p.getNome() + " não gerou receita.");
			} else {
				model.addAttribute("receita", "O paciente " + p.getNome() + " Gerou uma receita de R$ "
						+ service.buscarReceitaPorPaciente(paciente));
			}

		}

		else if (paciente == "" && dataInicio == "" && funcionario == "") {
			// so por convenio
			if (!convenio.equals("0")) {
				List<Custos> c = service.buscarPorConvenio(convenio);
				List<ListCustos> listarCustos = new ArrayList<ListCustos>();

				Convenio convenioCusto = convenioService.buscarPorId(convenio).get();

				for (Custos custos : c) {

					Funcionario f = funcionarioService.buscarporId(custos.getFuncionario()).get();
					Agenda agendamento = agendaService.buscarPorId(custos.getAgendamento()).get();
					Paciente p = pacienteService.buscarPorId(custos.getPaciente()).get();
					ListCustos lc = new ListCustos();
					lc.setFuncionario(f);
					lc.setAgendamento(agendamento);
					lc.setPaciente(p);
					lc.setData(custos.getData());
					lc.setValor(custos.getValor());
					lc.setConvenio(convenioCusto);
					listarCustos.add(lc);
				}
				model.addAttribute("resultado", listarCustos);
				if (service.buscarPorConvenio(convenio).isEmpty()) {
					model.addAttribute("receita", "O convênio " + convenioCusto.getNome() + " não gerou receita.");
				} else {
					model.addAttribute("receita", "O convênio " + convenioCusto.getNome() + " Gerou uma receita de R$ "
							+ service.buscarReceitaPorConvenio(convenio));
				}

			}

			if (convenio.equals("0")) {
				List<Custos> c = service.buscarTodosConvenios();
				List<ListCustos> listarCustos = new ArrayList<ListCustos>();

				for (Custos custos : c) {

					Funcionario f = funcionarioService.buscarporId(custos.getFuncionario()).get();
					Agenda agendamento = agendaService.buscarPorId(custos.getAgendamento()).get();
					Paciente p = pacienteService.buscarPorId(custos.getPaciente()).get();
					Convenio convenioCusto = convenioService.buscarPorId(custos.getConvenio()).get();
					ListCustos lc = new ListCustos();
					lc.setFuncionario(f);
					lc.setAgendamento(agendamento);
					lc.setPaciente(p);
					lc.setData(custos.getData());
					lc.setValor(custos.getValor());
					lc.setConvenio(convenioCusto);
					listarCustos.add(lc);
				}
				model.addAttribute("resultado", listarCustos);
				if (service.receitaTodosConvenios() == null) {
					model.addAttribute("receita", "Os convênios não geraram receita ");

				} else {
					model.addAttribute("receita",
							"Os convênios geraram uma receita de R$: " + service.receitaTodosConvenios());
				}

			}

		}

		else if (paciente == "" && convenio == "" && dataInicio == "") {
			// so por funcionario
			Funcionario f = funcionarioService.buscarporId(funcionario).get();
			List<Custos> c = service.buscarPorFuncionario(funcionario);
			List<ListCustos> listarCustos = new ArrayList<ListCustos>();

			for (Custos custos : c) {

				Agenda agendamento = agendaService.buscarPorId(custos.getAgendamento()).get();
				Paciente p = pacienteService.buscarPorId(custos.getPaciente()).get();
				Convenio convenioCusto = convenioService.buscarPorId(custos.getConvenio()).get();
				ListCustos lc = new ListCustos();
				lc.setFuncionario(f);
				lc.setAgendamento(agendamento);
				lc.setPaciente(p);
				lc.setData(custos.getData());
				lc.setValor(custos.getValor());
				lc.setConvenio(convenioCusto);
				listarCustos.add(lc);
			}

			model.addAttribute("resultado", listarCustos);

			if (service.buscarReceitaPorFuncionario(funcionario) == null) {
				model.addAttribute("receita", "O funcionario " + f.getNome() + " não gerou receita.");
			} else {
				model.addAttribute("receita", "O funcionario " + f.getNome() + " gerou uma receita de R$: "
						+ service.buscarReceitaPorFuncionario(funcionario));
			}

		}

		else if (paciente == "" && convenio == "" && funcionario == "") {
			// entre um periodo
			LocalDate inicio = LocalDate.parse(dataInicio);
			LocalDate fim = LocalDate.parse(dataFim);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String dataInicioFormatada = inicio.format(formatter);
			String dataFimFormatada = fim.format(formatter);
			BigDecimal receita = service.buscarReceitaPorDatas(inicio, fim);
			List<Custos> c = service.buscarPorDatas(inicio, fim);
			List<ListCustos> listarCustos = new ArrayList<ListCustos>();
			for (Custos custos : c) {

				Funcionario f = funcionarioService.buscarporId(custos.getFuncionario()).get();
				Agenda agendamento = agendaService.buscarPorId(custos.getAgendamento()).get();
				Paciente p = pacienteService.buscarPorId(custos.getPaciente()).get();
				Convenio convenioCusto = convenioService.buscarPorId(custos.getConvenio()).get();
				ListCustos lc = new ListCustos();
				lc.setFuncionario(f);
				lc.setAgendamento(agendamento);
				lc.setPaciente(p);
				lc.setData(custos.getData());
				lc.setValor(custos.getValor());
				lc.setConvenio(convenioCusto);
				listarCustos.add(lc);
			}

			model.addAttribute("resultado", listarCustos);
			if (receita == null) {
				model.addAttribute("receita", "Entre o periodo de " + dataInicioFormatada + " a " + dataFimFormatada
						+ " não foi gerada uma receita ");

			} else {
				model.addAttribute("receita", "Entre o periodo de " + dataInicioFormatada + " a " + dataFimFormatada
						+ " foi gerada uma receita de R$: " + receita);

			}
		}

		else if (funcionario == "" && convenio == "") {
			// Paciente e Periodo
			Paciente p = pacienteService.buscarPorId(paciente).get();
			LocalDate inicio = LocalDate.parse(dataInicio);
			LocalDate fim = LocalDate.parse(dataFim);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String dataInicioFormatada = inicio.format(formatter);
			String dataFimFormatada = inicio.format(formatter);
			BigDecimal receita = service.receitaPacientePeriodo(paciente, inicio, fim);
			List<Custos> c = service.pacientePeriodo(paciente, inicio, fim);
			List<ListCustos> listarCustos = new ArrayList<ListCustos>();
			for (Custos custos : c) {

				Funcionario f = funcionarioService.buscarporId(custos.getFuncionario()).get();
				Agenda agendamento = agendaService.buscarPorId(custos.getAgendamento()).get();

				Convenio convenioCusto = convenioService.buscarPorId(custos.getConvenio()).get();
				ListCustos lc = new ListCustos();
				lc.setFuncionario(f);
				lc.setAgendamento(agendamento);
				lc.setPaciente(p);
				lc.setData(custos.getData());
				lc.setValor(custos.getValor());
				lc.setConvenio(convenioCusto);
				listarCustos.add(lc);
			}

			model.addAttribute("resultado", listarCustos);
			if (receita == null) {
				model.addAttribute("receita", "O paciente " + p.getNome() + " no periodo de " + dataInicioFormatada
						+ " a " + dataFimFormatada + " não gerou receita.");
			} else {
				model.addAttribute("receita", "O paciente " + p.getNome() + " no periodo de " + dataInicioFormatada
						+ " a " + dataFimFormatada + " gerou uma receita de R$: " + receita);
			}

		}

		else if (funcionario == "" && paciente == "") {
			// convenio periodo
			LocalDate inicio = LocalDate.parse(dataInicio);
			LocalDate fim = LocalDate.parse(dataFim);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String dataInicioFormatada = inicio.format(formatter);
			String dataFimFormatada = fim.format(formatter);

			if (!convenio.equals("0")) {

				BigDecimal receita = service.receitaConvenioPeriodo(convenio, inicio, fim);
				List<Custos> c = service.convenioPeriodo(convenio, inicio, fim);
				List<ListCustos> listarCustos = new ArrayList<ListCustos>();
				Convenio convenioCusto = convenioService.buscarPorId(convenio).get();
				for (Custos custos : c) {

					Funcionario f = funcionarioService.buscarporId(custos.getFuncionario()).get();
					Agenda agendamento = agendaService.buscarPorId(custos.getAgendamento()).get();
					Paciente p = pacienteService.buscarPorId(custos.getPaciente()).get();

					ListCustos lc = new ListCustos();
					lc.setFuncionario(f);
					lc.setAgendamento(agendamento);
					lc.setPaciente(p);
					lc.setData(custos.getData());
					lc.setValor(custos.getValor());
					lc.setConvenio(convenioCusto);
					listarCustos.add(lc);
				}

				model.addAttribute("resultado", listarCustos);
				if (receita == null) {
					model.addAttribute("receita", "O convenio " + convenioCusto.getNome() + " no periodo de "
							+ dataInicioFormatada + " a " + dataFimFormatada + " não gerou uma receita. ");
				} else {
					model.addAttribute("receita", "O convenio " + convenioCusto.getNome() + " no periodo de "
							+ dataInicioFormatada + " a " + dataFimFormatada + " gerou uma receita de R$: " + receita);
				}

			}

			else {

				BigDecimal receita = service.buscarReceitaPorDatas(inicio, fim);
				List<Custos> c = service.buscarPorDatas(inicio, fim);
				List<ListCustos> listarCustos = new ArrayList<ListCustos>();
				for (Custos custos : c) {

					Funcionario f = funcionarioService.buscarporId(custos.getFuncionario()).get();
					Agenda agendamento = agendaService.buscarPorId(custos.getAgendamento()).get();
					Paciente p = pacienteService.buscarPorId(custos.getPaciente()).get();
					Convenio convenioCusto = convenioService.buscarPorId(custos.getConvenio()).get();
					ListCustos lc = new ListCustos();
					lc.setFuncionario(f);
					lc.setAgendamento(agendamento);
					lc.setPaciente(p);
					lc.setData(custos.getData());
					lc.setValor(custos.getValor());
					lc.setConvenio(convenioCusto);
					listarCustos.add(lc);
				}

				model.addAttribute("resultado", listarCustos);

				if (receita == null) {
					model.addAttribute("receita", "Entre o periodo de " + dataInicioFormatada + " a " + dataFimFormatada
							+ " os convenios não geraram uma receita. ");
				} else {
					model.addAttribute("receita", "Entre o periodo de " + dataInicioFormatada + " a " + dataFimFormatada
							+ " os convenios geraram uma receita de R$: " + receita);
				}

			}
		} else if (paciente == "" && convenio == "") {
			// funcionario e periodo

			LocalDate inicio = LocalDate.parse(dataInicio);
			LocalDate fim = LocalDate.parse(dataFim);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String dataInicioFormatada = inicio.format(formatter);
			String dataFimFormatada = fim.format(formatter);
			BigDecimal receita = service.receitaFuncionarioPeriodo(funcionario, inicio, fim);
			List<Custos> c = service.funcionarioPeriodo(funcionario, inicio, fim);
			List<ListCustos> listarCustos = new ArrayList<ListCustos>();
			Funcionario f = funcionarioService.buscarporId(funcionario).get();
			for (Custos custos : c) {
				Agenda agendamento = agendaService.buscarPorId(custos.getAgendamento()).get();
				Paciente p = pacienteService.buscarPorId(custos.getPaciente()).get();
				Convenio convenioCusto = convenioService.buscarPorId(custos.getConvenio()).get();
				ListCustos lc = new ListCustos();
				lc.setFuncionario(f);
				lc.setAgendamento(agendamento);
				lc.setPaciente(p);
				lc.setData(custos.getData());
				lc.setValor(custos.getValor());
				lc.setConvenio(convenioCusto);
				listarCustos.add(lc);
			}
			model.addAttribute("resultado", listarCustos);
			if (receita == null) {
				model.addAttribute("receita", "O medico " + f.getNome() + " no periodo de " + dataInicioFormatada
						+ " a " + dataFimFormatada + " não gerou uma receita. ");
			} else {
				model.addAttribute("receita", "O medico " + f.getNome() + " no periodo de " + dataInicioFormatada
						+ " a " + dataFimFormatada + " gerou uma receita de R$: " + receita);
			}

		}

		return "relatorio/lista";
	}

	@ModelAttribute("convenios")
	public List<Convenio> allConvenios() {
		return convenioService.buscarTodos();
	}
}
