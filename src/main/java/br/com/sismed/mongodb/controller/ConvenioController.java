package br.com.sismed.mongodb.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.service.ConvenioService;

@Controller
@RequestMapping("/convenios")
public class ConvenioController {

	@Autowired
	private ConvenioService service;

	@GetMapping("/mostrarDados")
	public ResponseEntity<Convenio> listarTodos() {

		Convenio lista = service.lastRecord();
		
		

		return ResponseEntity.ok().body(lista);
	}

	@GetMapping("/listar")
	public String listarDados(ModelMap model) {
		model.addAttribute("convenios", service.buscarTodos());
		
		
		return "convenio/lista";
	}
	
	@GetMapping("/cadastrar")
	public String cadastrarConvenio(Convenio convenio) {
		return "convenio/cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvarCadastro(Convenio convenio,RedirectAttributes attr) {
		try {
			service.salvar(convenio);
			attr.addFlashAttribute("sucesso","Convenio cadastrado com sucesso");
		}catch(Exception a) {
			
			attr.addFlashAttribute("falha","Erro ao salvar Convenio");
			return "redirect:/convenios/listar";
		}
		
		 return "redirect:/convenios/listar";
	}
	
	@GetMapping("/editar/{id}")
	public String abrirEdicao(@PathVariable String id, ModelMap model) {
		Convenio convenio = service.buscarPorId(id).get();
		
		model.addAttribute("convenio", convenio);
		return "convenio/editar";
	}
	
	@PostMapping("/atualizar")
	public String atualizarConvenio(Convenio convenio, RedirectAttributes attr) {
		
		String id = convenio.getId();
		try {
			service.salvar(convenio);
			attr.addFlashAttribute("sucesso","Convenio atualizado com sucesso");
		}catch(Exception a) {
			
			attr.addFlashAttribute("falha","Erro ao salvar Convenio");
			return "redirect:/convenios/listar";
		}
		
		return "redirect:/convenios/editar/" + id;
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirConvenio(@PathVariable String id,RedirectAttributes attr) {
		service.excluir(id);
		attr.addFlashAttribute("sucesso", "Convenio excluido com sucesso");
		return "redirect:/convenios/listar";

	}

}
