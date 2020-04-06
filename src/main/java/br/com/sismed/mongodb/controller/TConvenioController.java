package br.com.sismed.mongodb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.domain.LabelValue;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.TConvenioService;


@RequestMapping("/tconvenios")
@Controller
public class TConvenioController {

	@Autowired
	private ConvenioService service;
	
	@Autowired
	private TConvenioService tservice;
	
	@GetMapping("/cadastrar/{id}")
	public String cadastrarTipo(@PathVariable("id")String id, ModelMap model,  @ModelAttribute("tconvenio") TConvenio tconvenio) {
		Convenio convenio = service.buscarPorId(id).get();
		model.addAttribute("convenio", convenio);
		return "tconvenio/cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvarTipo(TConvenio tconvenio, RedirectAttributes attr) {
		String id = tconvenio.getConvenio();
		tservice.salvar(tconvenio);
		attr.addFlashAttribute("sucesso", "Tipo de Convenio cadastrado com sucesso");
		return "redirect:/tconvenios/cadastrar/"+id;
	}
	
	@GetMapping("/listar/{id}")
	public String listarTipos(@PathVariable("id") String id, ModelMap model, @RequestParam(value = "page", required=false, defaultValue="1") int page) {
		PageRequest pagerequest = PageRequest.of(page-1, 5, Sort.by("nome").ascending());
		Page<TConvenio> tc = tservice.listarTodosComPaginacao(id, pagerequest);
		Convenio convenio = service.buscarPorId(id).get();
		model.addAttribute("convenio", convenio);
		model.addAttribute("tconvenio", tc);
		
		int totalPages = tc.getTotalPages();
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
		return "tconvenio/lista";
	}
	
	@GetMapping("/buscar/{id}")
	@ResponseBody
	public List<LabelValue> buscar(@PathVariable("id") String id, @RequestParam (value="term", required=false, defaultValue="") String term) {
		List<LabelValue> suggeestions = new ArrayList<LabelValue>();
		List<TConvenio> allTipos = tservice.ListarPorNome(term, id);
		for (TConvenio tconvenio : allTipos) {
			LabelValue lv = new LabelValue();
			lv.setLabel(tconvenio.getNome());
			lv.setValue(tconvenio.getId());
			suggeestions.add(lv);
		}
		return suggeestions;	
	}
	
	@GetMapping("/editar/{id}")
	public String editarTipo(@PathVariable("id") String id, ModelMap model) {
		TConvenio tc = tservice.buscarPorId(id).get();
		model.addAttribute("tconvenio", tc);
		return "tconvenio/editar";
	}
	
	@PostMapping("/atualizar")
	public String atualizarTipo(TConvenio tconvenio, RedirectAttributes attr) {
		String id = tconvenio.getId();
		tservice.salvar(tconvenio);
		attr.addFlashAttribute("sucesso", "Tipo de Convenio alterado com sucesso");
		return "redirect:/tconvenios/editar/" + id;
	}
	
	@GetMapping("/excluir/{id}/{convenio_id}")
	public String excluirTipo(@PathVariable String id, @PathVariable String convenio_id, RedirectAttributes attr) {
		tservice.excluir(id);
		attr.addFlashAttribute("success", "Tipo de Convenio excluido com sucesso");
		return"redirect:/tconvenios/listar/" + convenio_id;
	}
}
