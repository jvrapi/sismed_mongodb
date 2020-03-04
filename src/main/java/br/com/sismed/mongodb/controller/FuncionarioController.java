package br.com.sismed.mongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.repository.FuncionarioRepository;
import br.com.sismed.mongodb.service.FuncionarioService;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {

	@Autowired
	private FuncionarioService service;
	
	@Autowired
	private FuncionarioRepository slvr;
	
	@GetMapping("/listar")
	public String listarTodos(ModelMap model){
		model.addAttribute("funcionario", service.buscarTodos());
		return "funcionario/lista";
	}
	
	@GetMapping("/cadastrar")
	public String cadastrar(Funcionario funcionario) {
		return "funcionario/cadastro";
	}
	
	@GetMapping("/salvar")
	public String salvar(Funcionario funcionario) {

		service.salvar(funcionario);
		return "funcionario/listar";
	}
}
