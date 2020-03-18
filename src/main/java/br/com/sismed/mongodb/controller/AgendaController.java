package br.com.sismed.mongodb.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.LabelValue;
import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.domain.Procedimento;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.AgendaService;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.FuncionarioService;
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

	@GetMapping()
	public ResponseEntity<Agenda> mostrarDados() {
		String paciente_id = "5e6a79bdebc6e0627990d464";
		Agenda agendamento = service.ultimoAgendamento(paciente_id);
		return ResponseEntity.ok().body(agendamento);
	}

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
		Convenio convenio = convenioService.buscarPorId(paciente.getTipo_convenio().getConvenio().getId()).get();
		model.addAttribute("paciente", paciente);
		model.addAttribute("funcionario", funcionarioService.buscarMedicos());
		model.addAttribute("convenio", convenio);

		return "agenda/agendarPacienteCadastrado";
	}

	@PostMapping("/salvar1")
	public String salvar(Agenda agenda, RedirectAttributes attr) {
		if (service.ultimoAgendamento(agenda.getPaciente().getId()) == null) {
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

		model.addAttribute("agendamentos", agendamentos);
		for (Agenda a : agendamentos) {
			model.addAttribute("data", a.getData());
		}
		return "fragmentos/agendaFuncionario :: resultsList";
	}

	@GetMapping("/agendaMedico")
	public String agendaMedico(Agenda agenda, ModelMap model, @AuthenticationPrincipal User user) {

		Funcionario medico = funcionarioService.buscarPorCpf(user.getUsername());
		String perfil = medico.getPerfil().getId();
		String medico_id = medico.getId();
		List<Agenda> agendamentos = service.ListarAgendamentosMedico(medico_id);

		for (Agenda a : agendamentos) {
			model.addAttribute("data", a.getData());

		}
		model.addAttribute("usuario", perfil);
		model.addAttribute("funcionario", medico.getNome());
		model.addAttribute("agendamentos", agendamentos);

		return "fragmentos/agendaFuncionario :: resultsList";
	}

	@GetMapping("buscarAgendamento/{data}/{medico}")
	public String buscarAgendamento(@PathVariable("data") String data, @PathVariable("medico") String medico,
			ModelMap model, Agenda agenda) {

		LocalDate dataAgendamento = LocalDate.parse(data);
		List<Agenda> agendamentos = service.buscarAgendamentos(dataAgendamento, medico);
		model.addAttribute("agendamentos", agendamentos);

		return "fragmentos/agendaFuncionario :: resultsList";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") String id, ModelMap model) {
		Agenda agendamento = service.buscarPorId(id).get();
		String funcionario_id = agendamento.getFuncionario().getId();
		String convenio_id = agendamento.getTipo_convenio().getConvenio().getId();
		
		Funcionario medico = funcionarioService.buscarporId(funcionario_id).get();
		List<Convenio> conveniosAceitos = new ArrayList<Convenio>();
		
		String convenioInserido = "";
		for (TConvenio tc : medico.getTconvenio()) {
			//listar os convenios aceitos pelo medico
			if (conveniosAceitos.isEmpty()) {
				// Primeiro a ser inserido no array
				convenioInserido = tc.getConvenio().getId();
				conveniosAceitos.add(tc.getConvenio());

			} else if (!tc.getConvenio().getId().equals(convenioInserido)) {
				// Verificação para ver se o convenio ja esta dentro do array para que não haja
				// repetição
				convenioInserido = tc.getConvenio().getId();
				conveniosAceitos.add(tc.getConvenio());
			}

		}
		
		
		
		
		model.addAttribute("agendamento", agendamento);
		model.addAttribute("funcionario", funcionarioService.buscarMedicos());
		model.addAttribute("convenio", conveniosAceitos);
		model.addAttribute("tipoConvenio", tconvenioService.listarTodos(convenio_id));
		return "agenda/editar";
	}
	
	@PostMapping("/atualizar")
	public String atualizarAgendamento(Agenda agenda, RedirectAttributes attr, @AuthenticationPrincipal User user) {
		/*Agenda a = service.buscarPorId(agenda.getId());
		if (!agenda.getData().isEqual(a.getData())) {
			Log l = new Log();
			Login login = lservice.BuscarPorCPF(user.getUsername());
			DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			l.setData(LocalDate.now());
			l.setFuncionario_id(login.getFuncionario_id());
			l.setHora(LocalTime.now());
			l.setDescricao(
					"ALTERAÇÃO NA DATA DE AGENDAMENTO: NOME DO PACIENTE: " + a.getPaciente_id().getNome() + ". DO DIA "
							+ a.getData().format(formatador) + " PARA O DIA " + agenda.getData().format(formatador));
			logservice.salvar(l);
		}*/

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
		Long prontuario = p.getMatricula();
		prontuario+=1;

		model.addAttribute("funcionario", funcionarioService.buscarMedicos());
		model.addAttribute("prontuario", prontuario);

		return "agenda/preCadastro";
	}
	
	@PostMapping("/salvarPreCadastro")
	public String salvarPreCadastro(Agenda agenda, Paciente paciente, RedirectAttributes attr) {
		agenda.setPrimeira_vez(1L);
		agenda.setPagou(1L);
		agenda.setCompareceu(1L);
		agenda.setPaciente(paciente);
		paciente.setTipo_convenio(agenda.getTipo_convenio());
		paciente.setSituacao("A");
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String dataAgendada = agenda.getData().format(formatador);
		pacienteService.salvar(paciente);
		service.salvar(agenda);
		attr.addFlashAttribute("sucesso", "Paciente Agendado para o dia " + dataAgendada + " As " + agenda.getHora());
		return "redirect:/agenda/agendamentos";
	}
	

	/* Metodos para JS */
	@GetMapping("/convenio/{convenio}/{medico}")
	public @ResponseBody List<TConvenio> listTipoConvenio(@PathVariable("convenio") String convenio,
			@PathVariable("medico") String medico, Agenda agenda) {
		Funcionario funcionario = funcionarioService.buscarporId(medico).get();
		List<TConvenio> tiposConveniosAceitos = new ArrayList<TConvenio>();
		for (TConvenio t : funcionario.getTconvenio()) {
			if (t.getConvenio().getId().equals(convenio)) {
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
		String convenioInserido = "";
		for (TConvenio tc : medico.getTconvenio()) {
			if (conveniosAceitos.isEmpty()) {
				// Primeiro a ser inserido no array
				convenioInserido = tc.getConvenio().getId();
				conveniosAceitos.add(tc.getConvenio());

			} else if (!tc.getConvenio().getId().equals(convenioInserido)) {
				// Verificação para ver se o convenio ja esta dentro do array para que não haja
				// repetição
				convenioInserido = tc.getConvenio().getId();
				conveniosAceitos.add(tc.getConvenio());

			}

		}
		return conveniosAceitos;
	}

	@GetMapping("funcionario/{id}")
	public @ResponseBody Funcionario funcionarioInfo(@PathVariable("id") String id, Agenda agenda) {
		return funcionarioService.buscarporId(id).get();
	}

}
