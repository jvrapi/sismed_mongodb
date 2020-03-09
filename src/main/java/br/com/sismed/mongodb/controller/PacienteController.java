package br.com.sismed.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.PacienteService;
import br.com.sismed.mongodb.service.TConvenioService;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {
	
	@Autowired
	private PacienteService pService;
	
	@Autowired
	private ConvenioService cService;
	
	@Autowired
	private TConvenioService tcService;
	
	@GetMapping("/listar")
	public String buscarTodos(ModelMap model) {
		model.addAttribute("paciente", pService.buscarTodos());
		return "/pacientes/lista";
	}
	
	@GetMapping("/cadastrar")
	public String cadastrar(Paciente paciente, ModelMap model) {
		model.addAttribute("convenio", cService.buscarTodos());
		return "pacientes/cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvar(Paciente paciente, RedirectAttributes attr) {
		pService.salvar(paciente);
		attr.addFlashAttribute("sucesso", "Paciente cadastrado com sucesso");
		return "redirect:/pacientes/listar";
	}

	@ResponseBody
	@GetMapping("/convenio/{id}")
	public List<TConvenio> listTipoConvenio(@PathVariable("id") String id, Paciente paciente) {
		return tcService.listarTodos(id);
	}
}
