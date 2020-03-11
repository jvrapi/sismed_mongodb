package br.com.sismed.mongodb.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.TConvenioService;


@RequestMapping("/tconvenios")
@Controller
public class TConvenioController {

	@Autowired
	private ConvenioService service;
	
	@Autowired
	private TConvenioService tservice;
	
	@GetMapping("/cadastrar/{id}")
	public String cadastrarTipo(@PathVariable("id")String id, ModelMap model,  @ModelAttribute("tconvenio") TConvenio tconvenio) {
		Convenio convenio = service.buscarPorId(id).get();
		model.addAttribute("convenio", convenio);
		return "tconvenio/cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvarTipo(TConvenio tconvenio, RedirectAttributes attr) {
		String id = tconvenio.getConvenio();
		tservice.salvar(tconvenio);
		attr.addFlashAttribute("sucesso", "Tipo de Convenio cadastrado com sucesso");
		return "redirect:/tconvenios/listar/"+id;
	}
	
	@GetMapping("/listar/{id}")
	public String listarTipos(@PathVariable("id") String id, ModelMap model) {
		List<TConvenio> tc = tservice.listarTodos(id);
		Convenio convenio = service.buscarPorId(id).get();
		model.addAttribute("convenio", convenio);
		model.addAttribute("tconvenio", tc);
		return "tconvenio/lista";
	}
	
	@GetMapping("/editar/{id}")
	public String editarTipo(@PathVariable("id") String id, ModelMap model) {
		TConvenio tc = tservice.buscarPorId(id).get();
		Convenio convenio = service.buscarPorId(tc.getConvenio()).get();
		model.addAttribute("convenio", convenio);
		model.addAttribute("tconvenio", tc);
		return "tconvenio/editar";
	}
	
	@PostMapping("/atualizar")
	public String atualizarTipo(TConvenio tconvenio, RedirectAttributes attr) {
		String id = tconvenio.getId();
		tservice.salvar(tconvenio);
		attr.addFlashAttribute("sucesso", "Tipo de Convenio alterado com sucesso");
		return "redirect:/tconvenios/editar/" + id;
	}
	
	@GetMapping("/excluir/{id}/{convenio_id}")
	public String excluirTipo(@PathVariable("id") String id, @PathVariable("convenio_id") String convenio_id, RedirectAttributes attr) {
		tservice.excluir(id);
		attr.addFlashAttribute("sucesso", "Tipo de Convenio excluido com sucesso");
		return"redirect:/tconvenios/listar/" + convenio_id;
	}
}
