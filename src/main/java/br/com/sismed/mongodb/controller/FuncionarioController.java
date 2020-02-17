package br.com.sismed.mongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sismed.mongodb.service.FuncionarioService;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

	@Autowired
	private FuncionarioService service;
	
	@GetMapping("/listar")
	public String listarTodos(ModelMap model){
		model.addAttribute("funcionario", service.buscarTodos());
		return "/home";
	}
}
