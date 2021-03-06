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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.LabelValue;
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
	public String listarTodos(ModelMap model, @RequestParam(value = "page", required=false, defaultValue="1") int page) {
		PageRequest pagerequest = PageRequest.of(page-1, 13, Sort.by("nome").ascending());
		Page<Funcionario> listFuncionario = service.buscarTodosComPaginacao(pagerequest);
		model.addAttribute("funcionario", listFuncionario);
		int lastPage = listFuncionario.getTotalPages();
		if (lastPage == 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, 1).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		} else if (lastPage == 2) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, lastPage).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		} else if (page == 2 && lastPage == 3) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, page + 1).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		} else if (page == 1 || page == 2) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, page + 2).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		} else if (page > 2 && page < lastPage - 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(page - 2, page + 2).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		} else if (page == lastPage - 1) {
			List<Integer> pageNumbers = IntStream.rangeClosed(page - 2, lastPage).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		} else if (page == lastPage) {
			List<Integer> pageNumbers = IntStream.rangeClosed(lastPage - 2, lastPage).boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
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
		
		
		service.salvar(funcionario);
		
		attr.addFlashAttribute("sucesso", "Funcionario(a) cadastrado(a) com sucesso");
		
		return "redirect:/funcionario/listar";

	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") String id, ModelMap model) {
		Funcionario funcionario = service.buscarporId(id).get();
		List<Convenio> listConvenio = new ArrayList<>();
		String convenioInserido = "";
		Convenio c;
		TConvenio tc;
		if(funcionario.getCrm() != null) {
			if(!funcionario.getTconvenio().isEmpty()) {
				for(String tipos: funcionario.getTconvenio()) {
					
					tc = tcService.buscarPorId(tipos).get();
					if(listConvenio.isEmpty()) {
						convenioInserido = tc.getConvenio();
						c = cService.buscarPorId(convenioInserido).get();
						listConvenio.add(c);
						
					}
					else if(!tc.getConvenio().equals(convenioInserido)) {
						convenioInserido = tc.getConvenio();
						c = cService.buscarPorId(convenioInserido).get();
						listConvenio.add(c);
					}
				}
			}
			model.addAttribute("funcionario", funcionario);
			// modal de cadastro
			model.addAttribute("allConvenios", cService.buscarTodos());
			//modal de exclus??o
			model.addAttribute("convenios", listConvenio);
		}
		else {
			model.addAttribute("funcionario", funcionario);
		}
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
		attr.addFlashAttribute("sucesso", "Funcionario(a) exclu??do(a) com sucesso");
		return "redirect:/funcionario/listar";
	}
	
	@GetMapping("/buscar/{id}")
	@ResponseBody
	public List<LabelValue> buscar(@PathVariable("id") Integer id,
			@RequestParam(value = "term", required = false, defaultValue = "") String term) {
		List<LabelValue> suggeestions = new ArrayList<LabelValue>();

		if (id == 1) {
			Long matricula = Long.parseLong(term);
			Funcionario allFuncionario = service.buscarPorMatricula(matricula);
			if(allFuncionario != null) {
				LabelValue lv = new LabelValue();
				lv.setLabel(allFuncionario.getNome());
				lv.setValue(allFuncionario.getId());
				suggeestions.add(lv);
			}
		}

		else if (id == 2) {
			List<Funcionario> allFuncionario = service.ListarFuncionarioNome(term);
			for (Funcionario funcionario : allFuncionario) {
				LabelValue lv = new LabelValue();
				lv.setLabel(funcionario.getNome());
				lv.setValue(funcionario.getId());
				suggeestions.add(lv);
			}
		}

		else if (id == 3) {
			List<Funcionario> allFuncionario = service.ListarFuncionarioCPF(term);
			for (Funcionario funcionario : allFuncionario) {
				LabelValue lv = new LabelValue();
				lv.setLabel(funcionario.getNome());
				lv.setValue(funcionario.getId());
				suggeestions.add(lv);
			}
		}

		else if (id == 4) {
			List<Funcionario> allFuncionario = service.ListarFuncionarioCelular(term);
			for (Funcionario funcionario : allFuncionario) {
				LabelValue lv = new LabelValue();
				lv.setLabel(funcionario.getNome());
				lv.setValue(funcionario.getId());
				suggeestions.add(lv);
			}
		}

		else if (id == 5) {
			List<Funcionario> allFuncionario = service.ListarFuncionarioCRM(term);
			for (Funcionario funcionario : allFuncionario) {
				LabelValue lv = new LabelValue();
				lv.setLabel(funcionario.getNome());
				lv.setValue(funcionario.getId());
				suggeestions.add(lv);
			}
		}

		else if (id == 6) {
			List<Funcionario> allFuncionario = service.ListarFuncionarioEspecialidade(term);
			for (Funcionario funcionario : allFuncionario) {
				LabelValue lv = new LabelValue();
				lv.setLabel(funcionario.getNome());
				lv.setValue(funcionario.getId());
				suggeestions.add(lv);
			}
		}

		return suggeestions;
	}
	
	//modal de cadastro
	@ResponseBody
	@GetMapping("/listarTiposPorConvenio/{id}/{funcId}")
	public List<TConvenio> listarTiposPorConvenio(@PathVariable("id") String id, @PathVariable("funcId") String funcId) {
		List<String> funcTipos = service.buscarporId(funcId).get().getTconvenio();
		List<TConvenio> funcTipos2 = new ArrayList<TConvenio>();
		for(String tipos: funcTipos) {
			TConvenio tc = tcService.buscarPorId(tipos).get();
			funcTipos2.add(tc);
			
		}
		List<TConvenio> todosTipos = tcService.listarTodos(id);
		List<TConvenio> tiposNaoCadastrados = new ArrayList<TConvenio>();
		int flag;
		for (TConvenio todosTipos2 : todosTipos) {
			flag = 1;
			for (TConvenio funcTipos3 : funcTipos2) {
				if(todosTipos2.getId().equals(funcTipos3.getId())) {
					flag = 0;
					break;
				}
			}
			if(flag == 1) 
				tiposNaoCadastrados.add(todosTipos2);
		}
		return tiposNaoCadastrados;		
	}
	
	//modal de exclus??o
	@ResponseBody
	@GetMapping("/listarTiposPorConvenioFunc/{convId}/{funcId}")
	public List<TConvenio> listarTiposPorConvenioFunc(@PathVariable("convId") String convId,  @PathVariable("funcId") String funcId) {
		Funcionario funcionario = service.buscarporId(funcId).get();
		List<TConvenio> listTConvenio = new ArrayList<TConvenio>();
		List<String> funcTipos = funcionario.getTconvenio();
		List<TConvenio> tiposAceitos =  new ArrayList<TConvenio>();
		
		for(String tipos : funcTipos) {
			TConvenio tc = tcService.buscarPorId(tipos).get();
			tiposAceitos.add(tc);
		}
		for (TConvenio tConvenio : tiposAceitos) {
			if(tConvenio.getConvenio().equals(convId)) {
				listTConvenio.add(tConvenio);
			}	
		}
		return listTConvenio;
	}
	
	@PostMapping("/salvarTConv")
	public String salvarTConv(@RequestParam("tconvenio") List<String> tconvenios, @RequestParam("idModal") String id) {
		Funcionario funcionario = service.buscarporId(id).get();
		if(funcionario.getTconvenio() != null) {
			funcionario.getTconvenio().addAll(tconvenios);
		}
		else {	
			funcionario.setTconvenio(tconvenios);
		}
		service.editar(funcionario);
		return "redirect:/funcionario/editar/" + funcionario.getId();
	}
	
	@PostMapping("/excluirTConv")
	public String excluirTConv(@RequestParam("idModalExcluir") String funcId, @RequestParam("tconvenio") List<TConvenio> tconvenios) {
		Funcionario medico = service.buscarporId(funcId).get();
		
		for (TConvenio tConvenio : tconvenios) {
			medico.getTconvenio().remove(tConvenio.getId());
		}
		service.editar(medico);
		return "redirect:/funcionario/editar/" + funcId;
	}
	
	
	
}
