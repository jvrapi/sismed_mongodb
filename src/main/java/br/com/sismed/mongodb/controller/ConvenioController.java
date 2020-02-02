package br.com.sismed.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.service.ConvenioService;

@RestController
@RequestMapping("/convenios")
public class ConvenioController {
	
	@Autowired
	private ConvenioService service;
	
	@GetMapping("/listar")
	public String listarTodos(ModelMap model){
		model.addAttribute("convenios", service.buscarTodos());
		return "/home";
	}

}
