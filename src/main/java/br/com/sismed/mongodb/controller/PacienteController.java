package br.com.sismed.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sismed.mongodb.domain.Convenio;
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
		List<Paciente> paciente = pService.buscarTodos();
		for(int i = 0; i < paciente.size(); i++) {
			Convenio convenio = cService.buscarPorId(paciente.get(i).getTipo_convenio().getConvenio()).get();
			paciente.get(i).setConvenio(convenio);
		}
		model.addAttribute("paciente", paciente);
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
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") String id, ModelMap model) {
		Paciente paciente = pService.buscarPorId(id).get();
		Convenio convenio = cService.buscarPorId(paciente.getTipo_convenio().getConvenio()).get();
		paciente.setConvenio(convenio);
		model.addAttribute("paciente", paciente);
		model.addAttribute("tipoconvenio", tcService.listarTodos(convenio.getId()));
		return "pacientes/editar";		
	}
	
	@PostMapping("/editar")
	public String editar(Paciente paciente, RedirectAttributes attr) {
		pService.salvar(paciente);
		attr.addFlashAttribute("sucesso", "Paciente alterado com sucesso");
		return "redirect:/pacientes/editar/" + paciente.getId();
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") String id, RedirectAttributes attr) {
		pService.excluir(id);
		attr.addFlashAttribute("sucesso", "Paciente excluido com sucesso");
		return "redirect:/pacientes/listar";
	}
	
	@ResponseBody
	@GetMapping("/convenio/{id}")
	public List<TConvenio> listTipoConvenio(@PathVariable("id") String id, Paciente paciente) {
		return tcService.listarTodos(id);
	}
	
	@ModelAttribute("convenio")
	public List<Convenio> listConvenio() {
		return cService.buscarTodos();
	}
}
