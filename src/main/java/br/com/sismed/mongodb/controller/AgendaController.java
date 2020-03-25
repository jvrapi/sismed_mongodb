package br.com.sismed.mongodb.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
import br.com.sismed.mongodb.domain.ListAgendamentos;
import br.com.sismed.mongodb.domain.Log;
import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.domain.Procedimento;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.AgendaService;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.CustosService;
import br.com.sismed.mongodb.service.FuncionarioService;
import br.com.sismed.mongodb.service.LogService;
import br.com.sismed.mongodb.service.PacienteService;
import br.com.sismed.mongodb.service.ProcedimentoService;
import br.com.sismed.mongodb.service.TConvenioService;

@Controller
@RequestMapping("/agenda")
public class AgendaController extends AbstractController {

	@Autowired
	private AgendaService service;

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private TConvenioService tconvenioService;

	@Autowired
	private ProcedimentoService procedimentoService;

	@Autowired
	private ConvenioService convenioService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private LogService logService;

	@Autowired
	private CustosService custosService;

	@GetMapping("/agendamentos")
	public String abrirAgendaDoDia(ModelMap model, @AuthenticationPrincipal User user) {
		Funcionario funcionario = funcionarioService.buscarPorCpf(user.getUsername());
		String perfil = funcionario.getPerfil().getId();
		String medico_id = funcionario.getId();

		List<Funcionario> medicos = funcionarioService.buscarTodos();

		model.addAttribute("usuario", perfil);
		model.addAttribute("funcionario", funcionario.getNome());
		model.addAttribute("medicos", medicos);
		model.addAttribute("medico_id", medico_id);
		return "agenda/agendamentos";
	}

	@GetMapping("/agendar/{id}")
	public String abrirPaginaAgendamento(@PathVariable("id") String id, ModelMap model, Agenda agendar) {
		Paciente paciente = pacienteService.buscarPorId(id).get();
		TConvenio tc = tconvenioService.buscarPorId(paciente.getTipo_convenio()).get();

		Convenio convenio = convenioService.buscarPorId(tc.getConvenio()).get();
		model.addAttribute("paciente", paciente);
		model.addAttribute("funcionario", funcionarioService.buscarMedicos());
		model.addAttribute("convenio", convenio);
		model.addAttribute("tipoConvenio", tc);
		return "agenda/agendarPacienteCadastrado";
	}

	@PostMapping("/salvar1")
	public String salvar(Agenda agenda, RedirectAttributes attr) {
		if (service.ultimoAgendamento(agenda.getPaciente()) == null) {
			agenda.setPrimeira_vez(1L);
			agenda.setPagou(1L);
			agenda.setCompareceu(1L);
		} else {
			agenda.setPrimeira_vez(0L);
			agenda.setPagou(1L);
			agenda.setCompareceu(1L);
		}

		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String dataAgendada = agenda.getData().format(formatador);
		service.salvar(agenda);
		attr.addFlashAttribute("sucesso", "Paciente Agendado para o dia " + dataAgendada + " As " + agenda.getHora());
		return "redirect:/agenda/agendamentos";
	}

	@GetMapping("/agendaFuncionario/{id}")
	public String agendaFuncionario(@PathVariable("id") String id, Agenda agenda, ModelMap model) {

		List<Agenda> agendamentos = service.ListarAgendamentosMedico(id);
		List<ListAgendamentos> ListAgendamentos = new ArrayList<ListAgendamentos>();

		for (Agenda a : agendamentos) {
			ListAgendamentos la = new ListAgendamentos();
			TConvenio tc = tconvenioService.buscarPorId(a.getTipo_convenio()).get();
			Convenio c = convenioService.buscarPorId(tc.getConvenio()).get();
			Paciente p = pacienteService.buscarPorId(a.getPaciente()).get();
			/* Informações do agendamento */
			la.setId(a.getId());
			la.setAgendamento_convenio(c.getNome());
			la.setAgendamento_hora(a.getHora());
			la.setAgendamento_data(a.getData());
			la.setAgendamento_observacao(a.getObservacao());
			la.setPrimeira_vez(a.getPrimeira_vez());
			la.setPagou(a.getPagou());
			la.setCompareceu(a.getCompareceu());
			/* Informaçãos do paciente */
			la.setPaciente_id(p.getId());
			la.setPaciente_matricula(p.getProntuario());
			la.setPaciente_celular(p.getCelular());

			la.setPaciente_telefone(p.getTelefone_fixo());

			la.setPaciente_nascimento(p.getData_nascimento());
			la.setPaciente_nome(p.getNome());

			ListAgendamentos.add(la);

		}
		model.addAttribute("agendamentos", ListAgendamentos);

		return "fragmentos/agendaFuncionario :: resultsList";
	}

	@GetMapping("/agendaMedico")
	public String agendaMedico(Agenda agenda, ModelMap model, @AuthenticationPrincipal User user) {

		Funcionario medico = funcionarioService.buscarPorCpf(user.getUsername());
		String perfil = medico.getPerfil().getId();
		String medico_id = medico.getId();
		List<Agenda> agendamentos = service.ListarAgendamentosMedico(medico_id);
		List<ListAgendamentos> ListAgendamentos = new ArrayList<ListAgendamentos>();

		for (Agenda a : agendamentos) {
			ListAgendamentos la = new ListAgendamentos();
			TConvenio tc = tconvenioService.buscarPorId(a.getTipo_convenio()).get();
			Convenio c = convenioService.buscarPorId(tc.getConvenio()).get();
			Paciente p = pacienteService.buscarPorId(a.getPaciente()).get();
			/* Informações do agendamento */
			la.setId(a.getId());
			la.setAgendamento_convenio(c.getNome());
			la.setAgendamento_hora(a.getHora());
			la.setAgendamento_data(a.getData());
			la.setAgendamento_observacao(a.getObservacao());
			la.setPrimeira_vez(a.getPrimeira_vez());
			la.setPagou(a.getPagou());
			la.setCompareceu(a.getCompareceu());
			/* Informaçãos do paciente */
			la.setPaciente_id(p.getId());
			la.setPaciente_matricula(p.getProntuario());
			la.setPaciente_celular(p.getCelular());

			la.setPaciente_telefone(p.getTelefone_fixo());

			la.setPaciente_nascimento(p.getData_nascimento());
			la.setPaciente_nome(p.getNome());

			ListAgendamentos.add(la);

		}
		model.addAttribute("usuario", perfil);
		model.addAttribute("funcionario", medico.getNome());
		model.addAttribute("agendamentos", ListAgendamentos);

		return "fragmentos/agendaFuncionario :: resultsList";
	}

	@GetMapping("buscarAgendamento/{data}/{medico}")
	public String buscarAgendamento(@PathVariable("data") String data, @PathVariable("medico") String medico,
			ModelMap model, Agenda agenda) {

		LocalDate dataAgendamento = LocalDate.parse(data);
		List<Agenda> agendamentos = service.buscarAgendamentos(dataAgendamento, medico);
		List<ListAgendamentos> ListAgendamentos = new ArrayList<ListAgendamentos>();
		for (Agenda a : agendamentos) {
			ListAgendamentos la = new ListAgendamentos();
			TConvenio tc = tconvenioService.buscarPorId(a.getTipo_convenio()).get();
			Convenio c = convenioService.buscarPorId(tc.getConvenio()).get();
			Paciente p = pacienteService.buscarPorId(a.getPaciente()).get();
			/* Informações do agendamento */
			la.setId(a.getId());
			la.setAgendamento_convenio(c.getNome());
			la.setAgendamento_hora(a.getHora());
			la.setAgendamento_data(a.getData());
			la.setAgendamento_observacao(a.getObservacao());
			la.setPrimeira_vez(a.getPrimeira_vez());
			la.setPagou(a.getPagou());
			la.setCompareceu(a.getCompareceu());
			/* Informaçãos do paciente */
			la.setPaciente_id(p.getId());
			la.setPaciente_matricula(p.getProntuario());
			la.setPaciente_celular(p.getCelular());

			la.setPaciente_telefone(p.getTelefone_fixo());

			la.setPaciente_nascimento(p.getData_nascimento());
			la.setPaciente_nome(p.getNome());

			ListAgendamentos.add(la);

		}
		model.addAttribute("agendamentos", ListAgendamentos);

		return "fragmentos/agendaFuncionario :: resultsList";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") String id, ModelMap model) {
		Agenda agendamento = service.buscarPorId(id).get();
		TConvenio tipoConvenioAgendamento = tconvenioService.buscarPorId(agendamento.getTipo_convenio()).get();
		Convenio convenioAgendamento = convenioService.buscarPorId(tipoConvenioAgendamento.getConvenio()).get();
		Procedimento procedimentoAgendamento = procedimentoService.buscarPorId(agendamento.getProcedimento()).get();
		Paciente paciente = pacienteService.buscarPorId(agendamento.getPaciente()).get();
		TConvenio pacienteTipoConvenio = tconvenioService.buscarPorId(paciente.getTipo_convenio()).get();
		Convenio convenioPaciente = convenioService.buscarPorId(pacienteTipoConvenio.getConvenio()).get();
		String funcionario_id = agendamento.getFuncionario();
		String convenio_id = convenioAgendamento.getId();

		Funcionario medico = funcionarioService.buscarporId(funcionario_id).get();
		List<Convenio> conveniosAceitos = new ArrayList<Convenio>();
		List<TConvenio> medicoTipos = new ArrayList<TConvenio>();
		for (String tipos : medico.getTconvenio()) {
			// Transforma o array de String em um Array de Tipos de Convenios
			TConvenio tc = tconvenioService.buscarPorId(tipos).get();
			medicoTipos.add(tc);
		}
		String convenioInserido = "";
		for (TConvenio tc : medicoTipos) {
			// listar os convenios aceitos pelo medico
			if (conveniosAceitos.isEmpty()) {
				// Primeiro a ser inserido no array
				convenioInserido = tc.getConvenio();
				Convenio convenioAceito = convenioService.buscarPorId(convenioInserido).get();
				conveniosAceitos.add(convenioAceito);

			} else if (!tc.getConvenio().equals(convenioInserido)) {
				// Verificação para ver se o convenio ja esta dentro do array para que não haja
				// repetição
				Convenio convenioAceito = convenioService.buscarPorId(convenioInserido).get();
				conveniosAceitos.add(convenioAceito);
			}

		}

		model.addAttribute("agendamento", agendamento);
		model.addAttribute("funcionario", funcionarioService.buscarMedicos());
		model.addAttribute("convenio", conveniosAceitos);
		model.addAttribute("tipoConvenio", tconvenioService.listarTodos(convenio_id));
		model.addAttribute("paciente", paciente);
		model.addAttribute("convenioPaciente", convenioPaciente);
		model.addAttribute("tipoConvenioPaciente", pacienteTipoConvenio);
		model.addAttribute("medico", medico);
		model.addAttribute("convenioAgendamento", convenioAgendamento);
		model.addAttribute("procedimento", procedimentoAgendamento);
		return "agenda/editar";
	}

	@PostMapping("/atualizar")
	public String atualizarAgendamento(Agenda agenda, RedirectAttributes attr, @AuthenticationPrincipal User user) {
		Agenda a = service.buscarPorId(agenda.getId()).get();
		Paciente paciente = pacienteService.buscarPorId(a.getPaciente()).get();
		if (!agenda.getData().isEqual(a.getData())) {
			Log l = new Log();
			Funcionario f = funcionarioService.buscarPorCpf(user.getUsername());
			DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			l.setData(LocalDate.now());
			l.setFuncionario_id(f.getId());
			l.setHora(LocalTime.now());
			l.setDescricao("ALTERAÇÃO NA DATA DE AGENDAMENTO: NOME DO PACIENTE: " + paciente.getNome() + ". DO DIA "
					+ a.getData().format(formatador) + " PARA O DIA " + agenda.getData().format(formatador));
			logService.salvar(l);
		}
		if (agenda.getObservacao().isEmpty()) {
			agenda.setObservacao(null);
		}
		service.salvar(agenda);
		attr.addFlashAttribute("sucesso", "Informações alteradas com sucesso!");
		return "redirect:/agenda/agendamentos";
	}

	@GetMapping("/listar/{id}")
	@ResponseBody
	public List<LabelValue> listar(@PathVariable("id") Integer id,
			@RequestParam(value = "term", required = false, defaultValue = "") String term) {
		List<LabelValue> suggeestions = new ArrayList<LabelValue>();
		if (id == 1) {
			List<Paciente> allPacientes = pacienteService.ListarPacNome(term);
			if (allPacientes.isEmpty()) {
				LabelValue lv = new LabelValue();
				lv.setLabel("Paciente não encontrado, Realizar Pre-Cadastro");
				lv.setValue2("0");
				suggeestions.add(lv);
			}
			for (Paciente paciente : allPacientes) {
				LabelValue lv = new LabelValue();
				lv.setLabel(paciente.getNome());
				lv.setValue2(paciente.getId());

				suggeestions.add(lv);
			}
		} else if (id == 2) {
			List<Paciente> allPacientes = pacienteService.ListarPacId(term);
			if (allPacientes.isEmpty()) {
				LabelValue lv = new LabelValue();
				lv.setLabel("Paciente não encontrado, Realizar Pre-Cadastro");
				lv.setValue2("0");
				suggeestions.add(lv);
			}
			for (Paciente paciente : allPacientes) {
				LabelValue lv = new LabelValue();
				lv.setLabel(paciente.getNome());
				lv.setValue2(paciente.getId());
				suggeestions.add(lv);
			}
		} else if (id == 3) {
			List<Paciente> allPacientes = pacienteService.PesquisarCPF(term);
			if (allPacientes.isEmpty()) {
				LabelValue lv = new LabelValue();
				lv.setLabel("Paciente não encontrado, Realizar Pre-Cadastro");
				lv.setValue2("0");
				suggeestions.add(lv);
			}
			for (Paciente paciente : allPacientes) {
				LabelValue lv = new LabelValue();
				lv.setLabel(paciente.getNome());
				lv.setValue2(paciente.getId());
				suggeestions.add(lv);
			}
		} else if (id == 4) {
			List<Paciente> allPacientes = pacienteService.PesquisarTelefone(term);
			if (allPacientes.isEmpty()) {
				LabelValue lv = new LabelValue();
				lv.setLabel("Paciente não encontrado, Realizar Pre-Cadastro");
				lv.setValue2("0");
				suggeestions.add(lv);
			}
			for (Paciente paciente : allPacientes) {
				LabelValue lv = new LabelValue();
				lv.setLabel(paciente.getNome());
				lv.setValue2(paciente.getId());
				suggeestions.add(lv);
			}
		}
		return suggeestions;
	}

	@GetMapping("/preCadastro")
	public String preCadastro(Agenda agenda, Paciente paciente, ModelMap model) {
		Paciente p = pacienteService.lastPaciente();
		Long prontuario;
		if (p == null) {
			prontuario = 1L;
		} else {
			prontuario = p.getProntuario() + 1;
		}
		model.addAttribute("funcionario", funcionarioService.buscarMedicos());
		model.addAttribute("prontuario", prontuario);

		return "agenda/preCadastro";
	}

	@PostMapping("/salvarPreCadastro")
	public String salvarPreCadastro(Agenda agenda, Paciente paciente, RedirectAttributes attr) {
		paciente.setTipo_convenio(agenda.getTipo_convenio());
		paciente.setSituacao("A");
		pacienteService.salvar(paciente);

		agenda.setPrimeira_vez(1L);
		agenda.setPagou(1L);
		agenda.setCompareceu(1L);
		agenda.setPaciente(paciente.getId());
		service.salvar(agenda);
		
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String dataAgendada = agenda.getData().format(formatador);

		attr.addFlashAttribute("sucesso", "Paciente Agendado para o dia " + dataAgendada + " As " + agenda.getHora());
		return "redirect:/agenda/agendamentos";
	}

	@GetMapping("/finalizar")
	public String finalizarAtendimento(RedirectAttributes attr) {
		List<Agenda> encerrar = service.encerrarAtendimento();
		for (Agenda a : encerrar) {
			if (a.getPrimeira_vez() == 1 && a.getCompareceu() == 0) {
				// paciente pre cadastrado, porem não compareceu;
				Paciente p = pacienteService.buscarPorId(a.getPaciente()).get();

				p.setSituacao("NC");

				pacienteService.salvar(p);
			}
			if (a.getPagou() == 1) {
				// preenchendo a tabela de custos
				Custos c = new Custos();
				TConvenio tc = tconvenioService.buscarPorId(a.getTipo_convenio()).get();
				Convenio convenioAgendamento = convenioService.buscarPorId(tc.getConvenio()).get();
				Procedimento procedimentoAgendamento = procedimentoService.buscarPorId(a.getProcedimento()).get();
				c.setAgendamento(a.getId());

				c.setConvenio(convenioAgendamento.getId());
				c.setFuncionario(a.getFuncionario());
				c.setData(a.getData());
				c.setHora(a.getHora());
				c.setProcedimento(a.getProcedimento());
				c.setPaciente(a.getPaciente());
				c.setValor(procedimentoAgendamento.getValor());
				custosService.salvar(c);

			}
		}
		attr.addFlashAttribute("sucesso", "Atendimento finalizado com sucesso!");
		return "redirect:/agenda/agendamentos";
	}

	@GetMapping("/agendamentosAnteriores/{id}")
	public String find(ModelMap model, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@PathVariable("id") String id) {

		PageRequest pagerequest = PageRequest.of(page - 1, 3);

		Page<Agenda> agendamentos = service.agendamentosAnteriores(id, pagerequest);
		List<ListAgendamentos> agendamentosAnteriores = new ArrayList<ListAgendamentos>();

		for (Agenda a : agendamentos) {
			ListAgendamentos la = new ListAgendamentos();
			Funcionario f = funcionarioService.buscarporId(a.getFuncionario()).get();
			la.setMedico_nome(f.getNome());
			la.setMedico_especialidade(f.getEscolaridade());
			la.setAgendamento_data(a.getData());
			la.setAgendamento_hora(a.getHora());
			la.setPrimeira_vez(a.getPrimeira_vez());
			la.setCompareceu(a.getCompareceu());
			la.setAgendamento_observacao(a.getObservacao());
			agendamentosAnteriores.add(la);
		}

		model.addAttribute("agenda", agendamentosAnteriores);
		model.addAttribute("paginacao", agendamentos);
		int totalPages = agendamentos.getTotalPages();
		if (totalPages == 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, 1).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		} else if (totalPages == 2) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		} else if (page == 2 && totalPages == 3) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, page + 1).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		} else if (page == 1 || page == 2) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, page + 2).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		} else if (page > 2 && page < totalPages - 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(page - 2, page + 2).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		} else if (page == totalPages - 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(page - 2, totalPages).boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		} else if (page == totalPages) {
			List<Integer> pageNumbers = IntStream.rangeClosed(totalPages - 2, totalPages).boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		return "fragmentos/agendamentosAnteriores :: resultsList";
	}

	/* Metodos para JS */
	@GetMapping("/convenio/{convenio}/{medico}")
	public @ResponseBody List<TConvenio> listTipoConvenio(@PathVariable("convenio") String convenio,
			@PathVariable("medico") String medico, Agenda agenda) {
		Funcionario funcionario = funcionarioService.buscarporId(medico).get();
		List<TConvenio> tiposConveniosAceitos = new ArrayList<TConvenio>();
		List<TConvenio> tiposConveniosMedico = new ArrayList<TConvenio>();
		for (String tiposMedico : funcionario.getTconvenio()) {
			TConvenio tc = tconvenioService.buscarPorId(tiposMedico).get();
			tiposConveniosMedico.add(tc);
		}
		for (TConvenio t : tiposConveniosMedico) {
			if (t.getConvenio().equals(convenio)) {
				tiposConveniosAceitos.add(t);
			}
		}

		return tiposConveniosAceitos;
	}

	@GetMapping("/procedimento/{id}")
	public @ResponseBody List<Procedimento> listProcedimentos(@PathVariable("id") String id, Agenda agenda) {

		return procedimentoService.listarTodos(id);
	}

	@GetMapping("/valor/{id}")
	public @ResponseBody Procedimento Procedimento(@PathVariable("id") String id, Agenda agenda) {

		return procedimentoService.buscarPorId(id).get();
	}

	@GetMapping("funcionarioConvenio/{id}")
	public @ResponseBody List<Convenio> listarConvenios(@PathVariable("id") String id, Agenda agenda) {
		Funcionario medico = funcionarioService.buscarporId(id).get();
		List<Convenio> conveniosAceitos = new ArrayList<Convenio>();
		List<TConvenio> tiposAceitosMedico = new ArrayList<TConvenio>();
		for (String tiposMedico : medico.getTconvenio()) {
			TConvenio tc = tconvenioService.buscarPorId(tiposMedico).get();
			tiposAceitosMedico.add(tc);
		}
		String convenioInserido = "";
		for (TConvenio tc : tiposAceitosMedico) {
			if (conveniosAceitos.isEmpty()) {
				// Primeiro a ser inserido no array
				convenioInserido = tc.getConvenio();
				Convenio convenio = convenioService.buscarPorId(convenioInserido).get();
				conveniosAceitos.add(convenio);

			} else if (!tc.getConvenio().equals(convenioInserido)) {
				// Verificação para ver se o convenio ja esta dentro do array para que não haja
				// repetição
				convenioInserido = tc.getConvenio();
				Convenio convenio = convenioService.buscarPorId(convenioInserido).get();
				conveniosAceitos.add(convenio);

			}

		}
		return conveniosAceitos;
	}

	@GetMapping("funcionario/{id}")
	public @ResponseBody Funcionario funcionarioInfo(@PathVariable("id") String id, Agenda agenda) {
		return funcionarioService.buscarporId(id).get();
	}

}
