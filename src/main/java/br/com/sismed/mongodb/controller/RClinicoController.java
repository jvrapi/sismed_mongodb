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

import br.com.sismed.mongodb.domain.LabelValue;
import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.domain.RegistroClinico;
import br.com.sismed.mongodb.service.AgendaService;
import br.com.sismed.mongodb.service.FuncionarioService;
import br.com.sismed.mongodb.service.PacienteService;
import br.com.sismed.mongodb.service.RClinicoService;

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
	
	@GetMapping("/listar")
	public String listar(RegistroClinico registroclinico, ModelMap model) {
		model.addAttribute("registro", service.listar());
		return "registro_clinico/busca";
	}
	
	@GetMapping("/cadastrar/{id}/{agendamentoId}")
	public String cadastro(@ModelAttribute("registroclinico") RegistroClinico registroclinico, @PathVariable("id") String id, @PathVariable("agendamentoId") String agendamentoId,  ModelMap model, @AuthenticationPrincipal User user) {
		model.addAttribute("paciente", pacienteService.buscarPorId(id).get());
		model.addAttribute("funcionario", funcionarioService.buscarPorCpf(user.getUsername()));
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
		service.salvar(registroclinico);
		attr.addFlashAttribute("success", "Registro cadastrado com sucesso");
		return "redirect:/registroclinico/cadastrar/" + registroclinico.getPaciente().getId() + "/null";
	}
	
	@GetMapping("/find/{id}")
	public String find(ModelMap model, @RequestParam(value = "page", required=false, defaultValue="1") int page, @PathVariable("id") String id) {
		PageRequest pagerequest = PageRequest.of(page-1, 4, Sort.by("id").descending());
		Page<RegistroClinico> rclinico = service.listarRegistros(id, pagerequest);
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
		String id = registroclinico.getPaciente().getId();
		LocalTime agora = LocalTime.now();
		registroclinico.setHora(agora);
		service.salvar(registroclinico);
		attr.addFlashAttribute("sucesso", "Registro alterado com sucesso");
		return "redirect:/registroclinico/cadastrar/" + id + "/null";
	}
	
	@GetMapping("/excluir/{id}/{pid}")
	public String excluir(@PathVariable("id") String id, @PathVariable("pid") String pid, @AuthenticationPrincipal User user, RedirectAttributes attr) {
		service.excluir(id);
		attr.addFlashAttribute("sucesso", "Registro excluído com sucesso");
		return "redirect:/registroclinico/cadastrar/" + pid + "/null";
	}

}
