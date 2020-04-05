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
import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.PacienteService;
import br.com.sismed.mongodb.service.TConvenioService;

@Controller
@RequestMapping("/pacientes")
public class PacienteController extends AbstractController{
	
	@Autowired
	private PacienteService pService;
	
	@Autowired
	private ConvenioService cService;
	
	@Autowired
	private TConvenioService tcService;
	
	@GetMapping("/listar")
	public String buscarTodos(ModelMap model, @RequestParam(value = "page", required=false, defaultValue="1") int page) {
		PageRequest pagerequest = PageRequest.of(page-1, 13, Sort.by("nome").ascending());
		Page<Paciente> paciente = pService.buscarTodosComPaginacao(pagerequest);
		for(int i=0; i<paciente.getContent().size(); i++) {
			TConvenio tc = tcService.buscarPorId(paciente.getContent().get(i).getTipo_convenio()).get();
			Convenio c = cService.buscarPorId(tc.getConvenio()).get();
			paciente.getContent().get(i).setTipo_convenio(tc.getNome());
			paciente.getContent().get(i).setConvenio(c.getNome());;
		}
		model.addAttribute("paciente", paciente);
		
		int totalPages = paciente.getTotalPages();
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
		return "/pacientes/lista";
	}
	
	@ResponseBody
	@GetMapping("/teste")
	public Page<Paciente> teste() {
		PageRequest pagerequest = PageRequest.of(0, 1, Sort.by("nome").ascending());
		Page<Paciente> paciente = pService.buscarTodosComPaginacao(pagerequest);
		return paciente;
	}
	
	@GetMapping("/cadastrar")
	public String cadastrar(Paciente paciente, ModelMap model) {
		model.addAttribute("convenio", cService.buscarTodos());
		return "pacientes/cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvar(Paciente paciente, RedirectAttributes attr) {
		pService.salvar(paciente);
		
		attr.addFlashAttribute("sucesso", "Paciente cadastrado com sucesso");
		return "redirect:/pacientes/listar";
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") String id, ModelMap model) {
		Paciente paciente = pService.buscarPorId(id).get();
		TConvenio tipoPaciente = tcService.buscarPorId(paciente.getTipo_convenio()).get();
		Convenio convenioPaciente = cService.buscarPorId(tipoPaciente.getConvenio()).get();
		model.addAttribute("paciente", paciente);
		model.addAttribute("tipoconvenio", tcService.listarTodos(convenioPaciente.getId()));
		return "pacientes/editar";		
	}
	
	@PostMapping("/editar")
	public String editar(Paciente paciente, RedirectAttributes attr) {
		pService.salvar(paciente);
		attr.addFlashAttribute("sucesso", "Paciente alterado com sucesso");
		return "redirect:/pacientes/editar/" + paciente.getId();
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") String id, RedirectAttributes attr) {
		pService.excluir(id);
		attr.addFlashAttribute("sucesso", "Paciente excluido com sucesso");
		return "redirect:/pacientes/listar";
	}
	
	@GetMapping("/buscar/{id}")
	@ResponseBody
	public List<LabelValue> listar(@PathVariable("id") Integer id, @RequestParam (value="term", required=false, defaultValue="") String term) {
		List<LabelValue> suggeestions = new ArrayList<LabelValue>();
		if(id == 1) {
			List<Paciente> allPacientes = pService.ListarPacId(term);
			for (Paciente paciente : allPacientes) {
				LabelValue lv = new LabelValue();
				lv.setLabel(paciente.getNome());
				lv.setValue2(paciente.getId());
				suggeestions.add(lv);
			}
		}
		else if(id == 2) {
			List<Paciente> allPacientes = pService.ListarPacNome(term);
			for (Paciente paciente : allPacientes) {
				LabelValue lv = new LabelValue();
				lv.setLabel(paciente.getNome());
				lv.setValue2(paciente.getId());
				suggeestions.add(lv);
			}
		}
		else if(id == 3) {
			List<Paciente> allPacientes = pService.PesquisarCPF(term);
			for (Paciente paciente : allPacientes) {
				LabelValue lv = new LabelValue();
				lv.setLabel(paciente.getNome());
				lv.setValue2(paciente.getId());
				suggeestions.add(lv);
			}
		}
		else if(id == 4) {
			
			List<Paciente> allPacientes = pService.PesquisarCelular(term);
			for (Paciente paciente : allPacientes) {
				LabelValue lv = new LabelValue();
				lv.setLabel(paciente.getNome());
				lv.setValue2(paciente.getId());
				suggeestions.add(lv);
			}
			
			if(term.length() <=3) {
				System.out.println("aqui");
				System.out.println(term);
			}
			else {
				System.out.println("ali");
				String array[] = term.split("\\)");
				System.out.println(array[0]);
				System.out.println(")" + array[1]);
			}
		}
		return suggeestions;
	}
	
	@ResponseBody
	@GetMapping("/convenio/{id}")
	public List<TConvenio> listTipoConvenio(@PathVariable("id") String id, Paciente paciente) {
		return tcService.listarTodos(id);
	}
	
	@ModelAttribute("convenio")
	public List<Convenio> listConvenio() {
		return cService.buscarTodos();
	}
}
