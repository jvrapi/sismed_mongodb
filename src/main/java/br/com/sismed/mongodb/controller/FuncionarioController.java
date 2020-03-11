package br.com.sismed.mongodb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.service.FuncionarioService;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {

	@Autowired
	private FuncionarioService service;

	@GetMapping("/listar")
	public String listarTodos(ModelMap model) {
		List<Funcionario> listFuncionario = service.buscarTodos();
		model.addAttribute("funcionario", listFuncionario);
		return "funcionario/lista";
	}

	@GetMapping("/cadastrar")
	public String cadastrar(Funcionario funcionario) {
		return "funcionario/cadastro";
	}

	@PostMapping("/salvar")
	public String salvar(Funcionario funcionario) {

		Funcionario func = service.ultimoRegistro();

		Long matricula;

		if (func != null) {
			matricula = func.getMatricula() + 1;
		} else {
			matricula = 1L;
		}

		funcionario.setMatricula(matricula);

		service.salvar(funcionario);
		return "redirect:/funcionario/listar";

	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") String id, ModelMap model) {

		// service.findOne(id).ifPresent(o -> model.addAttribute("id", o));

		model.addAttribute("funcionario", service.buscarporId(id).get());
		return "funcionario/editar";

	}
}
