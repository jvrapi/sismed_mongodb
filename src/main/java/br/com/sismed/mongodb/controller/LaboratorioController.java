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
import br.com.sismed.mongodb.domain.LabTConv;
import br.com.sismed.mongodb.domain.Laboratorio;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.FuncionarioService;
import br.com.sismed.mongodb.service.LaboratorioService;
import br.com.sismed.mongodb.service.TConvenioService;

@Controller
@RequestMapping("/laboratorio")
public class LaboratorioController {
	
	@Autowired
	private LaboratorioService laboratorioService;
	
	@Autowired
	private ConvenioService convenioService;
	
	@Autowired 
	private TConvenioService tipoConvenioService;
	
	@Autowired 
	FuncionarioService funcionarioSerrvice;
	
	
	
	@GetMapping("/listar")
	public String listar(ModelMap model, @RequestParam(value = "page", required=false, defaultValue="1") int page) {
		
		PageRequest pagerequest = PageRequest.of(page-1, 5, Sort.by("nome").ascending());
		Page<Laboratorio> laboratorio = laboratorioService.buscarTodos(pagerequest);
		model.addAttribute("laboratorio", laboratorio);
		
		int lastPage = laboratorio.getTotalPages();
		
		if (lastPage == 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, 1).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(lastPage == 2) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, lastPage).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if (page == 2 && lastPage == 3) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, page + 1).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(page == 1 || page == 2) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, page + 2).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(page > 2 && page < lastPage - 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(page - 2, page + 2).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(page == lastPage - 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(page-2, lastPage).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		else if(page == lastPage) {
			List<Integer> pageNumbers = IntStream.rangeClosed(lastPage - 2, lastPage).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		
		return "laboratorio/lista"; 
	}
	
	
	
	@GetMapping("/cadastrar") 
	public String cadastrar(Laboratorio laboratorio) {
		return "laboratorio/cadastro"; 
	}
	
	@PostMapping("/salvar")
	public String salvar(Laboratorio laboratorio, RedirectAttributes attr) {
		laboratorioService.salvar(laboratorio);
		attr.addFlashAttribute("success","Laboratório cadastrado com sucesso");
		return "redirect:/laboratorio/listar";
	}
	
	@GetMapping("/editar/{id}") 
	public String preEditar(@PathVariable("id") String id, ModelMap model, @ModelAttribute("labtconv") LabTConv labtconv) {
		Laboratorio laboratorio = laboratorioService.buscarPorId(id).get();
		List<Convenio> conveniosLaboratorio = new ArrayList<Convenio>();
		for(String tc : laboratorio.getTipo_convenio()) {
			TConvenio tipos = tipoConvenioService.buscarPorId(tc).get();
			Convenio convenio = convenioService.buscarPorId(tipos.getConvenio()).get();
			conveniosLaboratorio.add(convenio);
			
		}
		model.addAttribute("laboratorio", laboratorio);
		model.addAttribute("convenio",conveniosLaboratorio);
		model.addAttribute("allconvenios", convenioService.buscarTodos());
		return "laboratorio/editar";
	}
	
	@GetMapping("/convenio/{id}/{labId}")
	public @ResponseBody List<TConvenio> listTipoConvenio(@PathVariable("id") Long id, @PathVariable("labId") Long labId) {
		return tipoConvenioService.BuscarTConvenioLab(id, labId);
	}
	
	@GetMapping("/allconvenios/{id}/{labId}")
	public @ResponseBody List<TConvenio> listAllTipoConvenio(@PathVariable("id") Long id, @PathVariable("labId") Long labId) {
		return tipoConvenioService.ListaComboBoxLab(id, labId);
	}
	
	/*@GetMapping("/excluirTConv/{id}/{labId}")
	@ResponseBody
	public void excluirTConv(@PathVariable("id") Long id, @PathVariable("labId") Long labId) {
		ltcService.excluir(id, labId);
	}
	
	@PostMapping("/salvarTConv/{labId}")
	public String salvarTConv(LabTConv labtconv, @PathVariable("labId") Long labId) {
		ltcService.salvar(labtconv);
		return "redirect:/laboratorio/editar/" + labId;
	}*/
	

}
