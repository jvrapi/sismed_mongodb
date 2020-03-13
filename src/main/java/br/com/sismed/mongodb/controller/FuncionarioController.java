package br.com.sismed.mongodb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.Login;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.FuncionarioService;
import br.com.sismed.mongodb.service.TConvenioService;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController extends AbstractController{

	@Autowired
	private FuncionarioService service;
	
	@Autowired
	private ConvenioService cService;

	@Autowired
	private TConvenioService tcService;
	
	@GetMapping("/listar")
	public String listarTodos(ModelMap model) {
		List<Funcionario> listFuncionario = service.buscarTodos();
		model.addAttribute("funcionario", listFuncionario);
		return "funcionario/lista";
	}
	
	@GetMapping("/listarMedicos")
	public List<Funcionario> listarTodos1(ModelMap model) {
		List<Funcionario> listaMedico = service.buscarMedicos();
		//model.addAttribute("funcionario", listaMedico);
		return listaMedico;
	}
	
	@GetMapping("/listarFuncTiposConvenios")
	public String listarFuncTipoConvenio(ModelMap model) {
		List<Funcionario> listaTipoConvenio = service.mostrarTipoConvenios();
		model.addAttribute("funcionario", listaTipoConvenio);
		return "funcionario/listarFuncTipoConvenios";
	}
	
	@GetMapping("/listarFuncConvenios")
	public String listarFuncConvenio(ModelMap model) {
		List<Funcionario> listaConvenio = service.mostrarConvenios();
		model.addAttribute("funcionario", listaConvenio);
		return "funcionario/listarConvenios";
	}

	@GetMapping("/cadastrar")
	public String cadastrar(Funcionario funcionario) {
		return "funcionario/cadastro";
	}

	@PostMapping("/salvar")
	public String salvar(Funcionario funcionario, RedirectAttributes attr) {

		Funcionario func = service.ultimoRegistro();
		Login l = new Login();
		l.setSenha(new BCryptPasswordEncoder().encode(funcionario.getLogin().getSenha()));
		
		funcionario.setLogin(l);
		
		Long matricula;

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
		Funcionario funcionario = service.buscarporId(id).get();
		List<Convenio> listConvenio = new ArrayList<>();
		for(int i = 0; i < funcionario.getTconvenio().size(); i++) {
			listConvenio.add(funcionario.getTconvenio().get(i).getConvenio());
		}
		model.addAttribute("funcionario", funcionario);
		model.addAttribute("allConvenios", cService.buscarTodos());
		model.addAttribute("convenios", listConvenio);
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
	public String excluir(@PathVariable("id") String id,RedirectAttributes attr) {
		service.excluir(id);
		attr.addFlashAttribute("sucesso", "Funcionario(a) excluído(a) com sucesso");
		return "redirect:/funcionario/listar";
	}
	
	@ResponseBody
	@GetMapping("/listarTiposPorConvenio/{id}")
	public List<TConvenio> listarTiposPorConvenio(@PathVariable("id") String id) {
		return tcService.listarTodos(id);
	}
	
	@PostMapping("/salvarTConv")
	public String salvarTConv(@RequestParam("tconvenio") List<TConvenio> tconvenios, @RequestParam("idModal") String id) {
		Funcionario funcionario = service.buscarporId(id).get();
		funcionario.setTconvenio(tconvenios);
		service.salvar(funcionario);
		return "redirect:/funcionario/editar/" + funcionario.getId();
	}
	
	@PostMapping("/excluirTConv")
	public String excluirTConv(@RequestParam("idModalExcluir") String funcId, @RequestParam("tconvenio") List<TConvenio> tconvenios) {
		for (TConvenio tconvenio : tconvenios) {
			service.apagarTConv(funcId, tconvenio.getId());
		}
		return "redirect:/funcionario/editar/" + funcId;
	}
	
}
