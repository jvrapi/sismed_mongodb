package br.com.sismed.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
		List<Funcionario> listFuncionario = service.buscarTodos();
		System.out.println(listFuncionario.get(0).getId());
		model.addAttribute("funcionario", listFuncionario);
		return "funcionario/lista";
	}
	
	@GetMapping("/cadastrar")
	public String cadastrar(Funcionario funcionario) {
		return "funcionario/cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvar(Funcionario funcionario) {

		service.salvar(funcionario);
		return "redirect:/funcionario/listar";
	}
}
