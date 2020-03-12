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

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.domain.Procedimento;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.ProcedimentoService;

@Controller
@RequestMapping("/procedimentos")
public class ProcedimentoController {

	@Autowired
	private ConvenioService convenioService;

	@Autowired
	private ProcedimentoService procedimentoService;

	@GetMapping("/listar/{id}")
	public String listarProcedimentos(@PathVariable("id") String id, ModelMap model) {
		Convenio convenio = convenioService.buscarPorId(id).get();
		List<Procedimento> procedimentos = procedimentoService.listarTodos(id);
		model.addAttribute("procedimento", procedimentos);
		model.addAttribute("convenio", convenio);
		return "procedimentos/lista";
	}
	
	@GetMapping("/cadastrar/{id}")
	public String abrirPaginaCadastro(@PathVariable String id, ModelMap model, Procedimento procedimento) {
		Convenio convenio = convenioService.buscarPorId(id).get();
		model.addAttribute("convenio", convenio);
		return"procedimentos/cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvarCadastro(Procedimento procedimento, RedirectAttributes attr) {
		String id = procedimento.getConvenio();
		procedimentoService.salvar(procedimento);
		return "redirect:/procedimentos/listar/"+ id;
	}
	
	@GetMapping("/editar/{id}")
	public String abrirEdicao(@PathVariable String id, ModelMap model) {
		Procedimento procedimento = procedimentoService.buscarPorId(id).get();
		Convenio convenio = convenioService.buscarPorId(procedimento.getConvenio()).get();
		model.addAttribute("procedimento", procedimento);
		model.addAttribute("convenio", convenio);
		return "procedimentos/editar";
	}
	
	@PostMapping("/atualizar")
	public String atualizarProcedimento(Procedimento procedimento, RedirectAttributes attr) {
		String convenio = procedimento.getConvenio();		procedimentoService.salvar(procedimento);
		attr.addFlashAttribute("sucesso","Procedimento alterado com sucesso");
		return"redirect:/procedimentos/listar/" + convenio;
	}
	
	@GetMapping("/excluir/{id}/{convenio}")
	public String excluirProcedimento(@PathVariable("id") String procedimentoId, @PathVariable("convenio") String convenioId, RedirectAttributes attr) {
		procedimentoService.deletar(procedimentoId);
		attr.addFlashAttribute("sucesso", "Procedimento excluído com sucesso");
		return"redirect:/procedimentos/listar/" + convenioId;
	}
}
