package br.com.sismed.mongodb.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
import br.com.sismed.mongodb.domain.LabelValue;
import br.com.sismed.mongodb.service.ConvenioService;

@Controller
@RequestMapping("/convenios")
public class ConvenioController extends AbstractController{

	@Autowired
	private ConvenioService service;

	@GetMapping("/mostrarDados")
	public ResponseEntity<Convenio> listarTodos() {

		Convenio lista = service.lastRecord();
		
		

		return ResponseEntity.ok().body(lista);
	}

	@GetMapping("/listar")
	public String listarDados(ModelMap model, @RequestParam(value = "page", required=false, defaultValue="1") int page) {
		PageRequest pagerequest = PageRequest.of(page-1, 2, Sort.by("nome").ascending());
		Page<Convenio> convenio = service.buscarTodosComPaginacao(pagerequest);
		model.addAttribute("convenio", convenio);
		
		int totalPages = convenio.getTotalPages();
		if (totalPages == 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, 1).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(totalPages == 2) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if (page == 2 && totalPages == 3) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, page + 1).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(page == 1 || page == 2) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, page + 2).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(page > 2 && page < totalPages - 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(page - 2, page + 2).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(page == totalPages - 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(page-2, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(page == totalPages) {
			List<Integer> pageNumbers = IntStream.rangeClosed(totalPages - 2, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		
		return "convenio/lista";
	}
	
	@GetMapping("/cadastrar")
	public String cadastrarConvenio(Convenio convenio) {
		return "convenio/cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvarCadastro(Convenio convenio,RedirectAttributes attr) {
		try {
			service.salvar(convenio);
			attr.addFlashAttribute("sucesso","Convenio cadastrado com sucesso");
		}catch(Exception a) {
			
			attr.addFlashAttribute("falha","Erro ao salvar Convenio");
			return "redirect:/convenios/listar";
		}
		
		 return "redirect:/convenios/listar";
	}
	
	@GetMapping("/editar/{id}")
	public String abrirEdicao(@PathVariable String id, ModelMap model) {
		Convenio convenio = service.buscarPorId(id).get();
		
		model.addAttribute("convenio", convenio);
		return "convenio/editar";
	}
	
	@PostMapping("/atualizar")
	public String atualizarConvenio(Convenio convenio, RedirectAttributes attr) {
		
		String id = convenio.getId();
		try {
			service.salvar(convenio);
			attr.addFlashAttribute("sucesso","Convenio atualizado com sucesso");
		}catch(Exception a) {
			
			attr.addFlashAttribute("falha","Erro ao salvar Convenio");
			return "redirect:/convenios/listar";
		}
		
		return "redirect:/convenios/editar/" + id;
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirConvenio(@PathVariable String id,RedirectAttributes attr) {
		service.excluir(id);
		attr.addFlashAttribute("sucesso", "Convenio excluido com sucesso");
		return "redirect:/convenios/listar";

	}
	
	@GetMapping("/buscar/{id}")
	@ResponseBody
	public List<LabelValue> listar(@PathVariable("id") Integer id, @RequestParam (value="term", required=false, defaultValue="") String term) {
		List<LabelValue> suggeestions = new ArrayList<LabelValue>();
		if(id == 1) {
			List<Convenio> allConvenios = service.listarPorNomeRegex(term);
			for (Convenio convenio : allConvenios) {
				LabelValue lv = new LabelValue();
				lv.setLabel(convenio.getNome());
				lv.setValue(convenio.getId());
				suggeestions.add(lv);
			}
		}
		else if(id == 2) {
			List<Convenio> allConvenios = service.ListarPorCNPJRegex(term);
			for (Convenio convenio: allConvenios) {
				LabelValue lv = new LabelValue();
				lv.setLabel(convenio.getNome());
				lv.setValue(convenio.getId());
				suggeestions.add(lv);
			}
		}
		else if(id == 3) {
			List<Convenio> allConvenios = service.ListarPorANSRegex(term);
			for (Convenio convenio: allConvenios) {
				LabelValue lv = new LabelValue();
				lv.setLabel(convenio.getNome());
				lv.setValue(convenio.getId());
				suggeestions.add(lv);
			}
		}
		
		return suggeestions;	
	}

}
