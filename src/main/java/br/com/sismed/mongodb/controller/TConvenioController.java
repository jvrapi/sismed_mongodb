package br.com.sismed.mongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.ConvenioService;


@RequestMapping("tconvenios")
@Controller
public class TConvenioController {

	@Autowired
	private ConvenioService service;
	
	@GetMapping("/cadastrar/{id}")
	public String listarTipos(@PathVariable("id")String id, ModelMap model,  @ModelAttribute("tconvenio") TConvenio tconvenio) {
		Convenio convenio = service.buscarPorId(id).get();
		model.addAttribute("convenio", convenio);
		return "tconvenio/cadastro";
	}
}
