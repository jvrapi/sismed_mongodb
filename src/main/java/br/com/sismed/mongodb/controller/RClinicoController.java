package br.com.sismed.mongodb.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.LabelValue;
import br.com.sismed.mongodb.domain.ListRegistroAnteriores;
import br.com.sismed.mongodb.domain.ListRegistros;
import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.domain.RegistroClinico;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.AgendaService;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.FuncionarioService;
import br.com.sismed.mongodb.service.PacienteService;
import br.com.sismed.mongodb.service.RClinicoService;
import br.com.sismed.mongodb.service.TConvenioService;

@Controller
@RequestMapping("/registroclinico")
public class RClinicoController extends AbstractController{
	
	@Autowired
	private RClinicoService service;
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private AgendaService agendaService;
	
	@Autowired
	private TConvenioService tipoConvenioService;
	
	@Autowired
	private ConvenioService convenioService;
	
	@GetMapping("/listar")
	public String listar(RegistroClinico registroclinico, ModelMap model) {
		List<RegistroClinico> allRegistros = service.listar();
		List<ListRegistros> registrosPorPaciente = new ArrayList<ListRegistros>();
		String pacienteId = ""; // Variavel auxiliar para verificar se o paciente já esta dentro do array de retorno para a pagina
		/*Pecorre todos os registros clinicos para poder filtrar por paciente*/
		for(RegistroClinico rc : allRegistros) {
			Paciente paciente = pacienteService.buscarPorId(rc.getPaciente()).get();
			RegistroClinico ultimoRegistro = service.ultimoRegistroPaciente(paciente.getId());
			Long total_registros = Long.valueOf(service.listarRegistrosPorPaciente(paciente.getId()).size());
			ListRegistros lr = new ListRegistros();
			
			lr.setPaciente_id(paciente.getId());
			lr.setPaciente_nome(paciente.getNome());
			lr.setPaciente_prontuario(paciente.getProntuario());
			lr.setDescricao(ultimoRegistro.getDescricao());
			lr.setData(ultimoRegistro.getData());
			lr.setHora(ultimoRegistro.getHora());
			lr.setTotal_registros(total_registros);
			
			if(registrosPorPaciente.isEmpty()) {
				pacienteId = rc.getPaciente();
				registrosPorPaciente.add(lr);
			}
			else if(!pacienteId.equals(rc.getPaciente())) {
				pacienteId = rc.getPaciente();
				registrosPorPaciente.add(lr);
			}
			
		}
		
		
		model.addAttribute("registro", registrosPorPaciente);
		return "registro_clinico/busca";
	}
	
	@GetMapping("/cadastrar/{id}/{agendamentoId}")
	public String cadastro(@ModelAttribute("registroclinico") RegistroClinico registroclinico, @PathVariable("id") String id, @PathVariable("agendamentoId") String agendamentoId,  ModelMap model, @AuthenticationPrincipal User user) {
		Paciente paciente =  pacienteService.buscarPorId(id).get();
		Funcionario medico = funcionarioService.buscarPorCpf(user.getUsername());
		TConvenio tipoConvenioPaciente = tipoConvenioService.buscarPorId(paciente.getTipo_convenio()).get();
		Convenio convenioPaciente = convenioService.buscarPorId(tipoConvenioPaciente.getConvenio()).get();
		
		model.addAttribute("paciente",paciente);
		model.addAttribute("funcionario", medico);
		model.addAttribute("tipoConvenioPaciente", tipoConvenioPaciente);
		model.addAttribute("convenioPaciente", convenioPaciente);
		if(agendamentoId.equals("null")) {
			model.addAttribute("agendamento", null);
		}
		else {
			model.addAttribute("agendamento", agendaService.buscarPorId(agendamentoId).get());
		}
		return "registro_clinico/cadastropac";
	}
	
	@PostMapping("/salvar")
	public String salvar(RegistroClinico registroclinico, RedirectAttributes attr) {
		RegistroClinico ultimoRegistro = service.ultimoRegistro();
		Paciente paciente = pacienteService.buscarPorId(registroclinico.getPaciente()).get();
		Long numero;
		if(ultimoRegistro != null) {
			numero = ultimoRegistro.getNumero() + 1;
		}else {
			numero = 1L;
		}
		registroclinico.setNumero(numero);
		service.salvar(registroclinico);
		attr.addFlashAttribute("success", "Registro cadastrado com sucesso");
		return "redirect:/registroclinico/cadastrar/" + paciente.getId() + "/null";
	}
	
	@GetMapping("/find/{id}")
	public String find(ModelMap model, @RequestParam(value = "page", required=false, defaultValue="1") int page, @PathVariable("id") String id) {
		PageRequest pagerequest = PageRequest.of(page-1, 4, Sort.by("id").descending());
		Page<RegistroClinico> rclinico = service.listarRegistros(id, pagerequest);
		List<ListRegistroAnteriores> registroAnteriores = new ArrayList<ListRegistroAnteriores>();
		for(RegistroClinico registro: rclinico) {
		
			Paciente paciente = pacienteService.buscarPorId(registro.getPaciente()).get();
			
			Funcionario funcionario = funcionarioService.buscarporId(registro.getFuncionario_id()).get();
			ListRegistroAnteriores ra = new ListRegistroAnteriores();
			ra.setId(registro.getId());
			ra.setNumero(registro.getNumero());
			
			ra.setDescricao(registro.getDescricao());
			ra.setData(registro.getData());
			ra.setHora(registro.getHora());
			
			ra.setFuncionario(funcionario);
			ra.setPaciente(paciente);
			
			
			if(registro.getAgendamento_id() != null) {
				Agenda agendamento  = agendaService.buscarPorId(registro.getAgendamento_id()).get();
				ra.setAgendamento(agendamento);
				
			}else {
				ra.setAgendamento(null);
			}
			registroAnteriores.add(ra);
		}
		model.addAttribute("registroAnteriores", registroAnteriores);
		model.addAttribute("registro", rclinico);
		int totalPages = rclinico.getTotalPages();
		if (totalPages == 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, 1).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(totalPages == 2) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if (page == 2 && totalPages == 3) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, page + 1).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(page == 1 || page == 2) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, page + 2).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(page > 2 && page < totalPages - 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(page - 2, page + 2).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(page == totalPages - 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(page-2, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(page == totalPages) {
			List<Integer> pageNumbers = IntStream.rangeClosed(totalPages - 2, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		return "fragmentos/tabelaRegistroPorPac :: resultsList";
	}
	
	@GetMapping("/buscar/{id}")
	@ResponseBody
	public List<LabelValue> listar(@PathVariable("id") Integer id, @RequestParam (value="term", required=false, defaultValue="") String term) {
		List<LabelValue> suggeestions = new ArrayList<LabelValue>();
		if(id == 1) {
			List<Paciente> allPacientes = pacienteService.ListarPacId(term);
			for (Paciente paciente : allPacientes) {
				LabelValue lv = new LabelValue();
				lv.setLabel(paciente.getNome());
				lv.setValue2(paciente.getId());
				suggeestions.add(lv);
			}
		}
		else if(id == 2) {
			List<Paciente> allPacientes = pacienteService.ListarPacNome(term);
			for (Paciente paciente : allPacientes) {
				LabelValue lv = new LabelValue();
				lv.setLabel(paciente.getNome());
				lv.setValue2(paciente.getId());
				suggeestions.add(lv);
			}
		}
		return suggeestions;
	}
	
	@PostMapping("/editar")
	public String editar(RegistroClinico registroclinico, RedirectAttributes attr) {
		Paciente paciente = pacienteService.buscarPorId(registroclinico.getPaciente()).get();
		if(registroclinico.getAgendamento_id() == "") {
			registroclinico.setAgendamento_id(null);
		}
		LocalTime agora = LocalTime.now();
		registroclinico.setHora(agora);
		service.salvar(registroclinico);
		attr.addFlashAttribute("sucesso", "Registro alterado com sucesso");
		return "redirect:/registroclinico/cadastrar/" + paciente.getId() + "/null";
	}
	
	@GetMapping("/excluir/{id}/{pid}")
	public String excluir(@PathVariable("id") String id, @PathVariable("pid") String pid, @AuthenticationPrincipal User user, RedirectAttributes attr) {
		service.excluir(id);
		attr.addFlashAttribute("sucesso", "Registro excluído com sucesso");
		return "redirect:/registroclinico/cadastrar/" + pid + "/null";
	}

}
