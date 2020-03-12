package br.com.sismed.mongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sismed.mongodb.domain.RegistroClinico;
import br.com.sismed.mongodb.service.PacienteService;
import br.com.sismed.mongodb.service.RClinicoService;

@Controller
@RequestMapping("/registroclinico")
public class RClinicoController extends AbstractController{
	
	@Autowired
	private RClinicoService service;
	
	@Autowired
	private PacienteService pacienteService;
	
	@GetMapping("/listar")
	public String listar(RegistroClinico registroclinico, ModelMap model) {
		model.addAttribute("registro", service.listar());
		return "registro_clinico/busca";
	}
	
	@GetMapping("/cadastrar/{id}")
	public String cadastro(@ModelAttribute("registroclinico") RegistroClinico registroclinico, @PathVariable("id") String id, ModelMap model) {
		model.addAttribute("paciente", pacienteService.buscarPorId(id).get());
		return "registro_clinico/cadastropac";
	}
	
	@PostMapping("/salvar")
	public String salvar(RegistroClinico registroclinico, RedirectAttributes attr) {
		service.salvar(registroclinico);
		attr.addFlashAttribute("success", "Registro cadastrado com sucesso");
		return "redirect:/registroclinico/cadastrar/" + registroclinico.getPaciente_id().getId();
	}

}
