package br.com.sismed.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.Perfil;
import br.com.sismed.mongodb.domain.PerfilTipo;
import br.com.sismed.mongodb.service.FuncionarioService;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController extends AbstractController {

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
	public String salvar(Funcionario funcionario, RedirectAttributes attr) {

		Funcionario func = service.ultimoRegistro();
		Long matricula;
		Integer crm = funcionario.getCrm();
		Perfil perfil = new Perfil();
		
		if (crm == null) {
			perfil.setId("2");
			perfil.setDesc("FUNCIONARIO");
			funcionario.setPerfil(perfil);
			
		} else {
			perfil.setId("1");
			perfil.setDesc("MEDICO");
			funcionario.setPerfil(perfil);
		}
		if (func != null) {
			matricula = func.getMatricula() + 1;
		} else {
			matricula = 1L;
		}

		funcionario.setMatricula(matricula);

		service.salvar(funcionario);

		attr.addFlashAttribute("sucesso", "Funcionario(a) cadastrado(a) com sucesso");

		return "redirect:/funcionario/listar";

	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") String id, ModelMap model) {
		model.addAttribute("funcionario", service.buscarporId(id).get());

		return "funcionario/editar";
	}

	@PostMapping("/editar")
	public String editar(Funcionario funcionario, RedirectAttributes attr) {
		service.salvar(funcionario);
		String id = funcionario.getId();
		attr.addFlashAttribute("sucesso", "Funcionario(a) alterado(a) com sucesso");
		return "redirect:/funcionario/editar/" + id;
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") String id, RedirectAttributes attr) {
		service.excluir(id);
		attr.addFlashAttribute("sucesso", "Funcionario(a) excluído(a) com sucesso");
		return "redirect:/funcionario/listar";
	}

}
