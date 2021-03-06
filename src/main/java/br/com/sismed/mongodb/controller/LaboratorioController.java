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
import br.com.sismed.mongodb.domain.LabelValue;
import br.com.sismed.mongodb.domain.Laboratorio;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.FuncionarioService;
import br.com.sismed.mongodb.service.LaboratorioService;
import br.com.sismed.mongodb.service.TConvenioService;

@Controller
@RequestMapping("/laboratorio")
public class LaboratorioController extends AbstractController {

	@Autowired
	private LaboratorioService laboratorioService;

	@Autowired
	private ConvenioService convenioService;

	@Autowired
	private TConvenioService tipoConvenioService;

	@Autowired
	FuncionarioService funcionarioSerrvice;

	@GetMapping("/listar")
	public String listar(ModelMap model, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

		PageRequest pagerequest = PageRequest.of(page - 1, 5, Sort.by("nome").ascending());
		Page<Laboratorio> laboratorio = laboratorioService.buscarTodos(pagerequest);
		model.addAttribute("laboratorio", laboratorio);

		int lastPage = laboratorio.getTotalPages();

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

		return "laboratorio/lista";
	}
	
	@GetMapping("/buscar/{id}")
	@ResponseBody
	public List<LabelValue> buscar (@PathVariable("id")Integer id, @RequestParam (value="term", required=false, defaultValue="") String term){
		List<LabelValue> suggeestions = new ArrayList<LabelValue>();
		
		if(id == 2) {
			List<Laboratorio> allLaboratorio = laboratorioService.ListarLaboratorioNome(term);
			for (Laboratorio laboratorio : allLaboratorio) {
				LabelValue lv = new LabelValue();
				lv.setLabel(laboratorio.getNome());
				lv.setValue(laboratorio.getId());
				suggeestions.add(lv);
			}
		}
		
		else if(id == 3) {
			List<Laboratorio> allLaboratorio = laboratorioService.ListarLaboratorioTelefone(term);
			for (Laboratorio laboratorio : allLaboratorio) {
				LabelValue lv = new LabelValue();
				lv.setLabel(laboratorio.getNome());
				lv.setValue(laboratorio.getId());
				suggeestions.add(lv);
			}
		}
		
		else if(id == 4) {
			List<Laboratorio> allLaboratorio = laboratorioService.ListarLaboratorioBairro(term);
			for (Laboratorio laboratorio : allLaboratorio) {
				LabelValue lv = new LabelValue();
				lv.setLabel(laboratorio.getNome());
				lv.setValue(laboratorio.getId());
				suggeestions.add(lv);
			}
		}
		
		return suggeestions;
	}

	@GetMapping("/cadastrar")
	public String cadastrar(Laboratorio laboratorio) {
		return "laboratorio/cadastro";
	}

	@PostMapping("/salvar")
	public String salvar(Laboratorio laboratorio, RedirectAttributes attr) {
		laboratorioService.salvar(laboratorio);
		attr.addFlashAttribute("sucesso", "Laborat??rio cadastrado com sucesso");
		return "redirect:/laboratorio/listar";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") String id, ModelMap model) {
		Laboratorio laboratorio = laboratorioService.buscarPorId(id).get();
		List<Convenio> conveniosLaboratorio = new ArrayList<Convenio>();

		String convenioInserido = "";
		for (String tc : laboratorio.getTipo_convenio()) {
			TConvenio tipos = tipoConvenioService.buscarPorId(tc).get();
			Convenio convenio = convenioService.buscarPorId(tipos.getConvenio()).get();
			if (conveniosLaboratorio.isEmpty()) {
				convenioInserido = convenio.getId();
				conveniosLaboratorio.add(convenio);
			} else if (!convenio.getId().equals(convenioInserido)) {
				convenioInserido = convenio.getId();
				conveniosLaboratorio.add(convenio);
			}

		}

		model.addAttribute("laboratorio", laboratorio);
		model.addAttribute("convenio", conveniosLaboratorio);
		model.addAttribute("allconvenios", convenioService.buscarTodos());
		return "laboratorio/editar";
	}

	@GetMapping("/convenio/{id}/{labId}")
	public @ResponseBody List<TConvenio> listTipoConvenio(@PathVariable("id") String convenioId,
			@PathVariable("labId") String labId) {
		// Lista os tipos de convenios aceitos por cada laboratorio
		Laboratorio laboratorio = laboratorioService.buscarPorId(labId).get();
		List<TConvenio> tiposAceitos = new ArrayList<TConvenio>();
		for (String tipos : laboratorio.getTipo_convenio()) {
			TConvenio tc = tipoConvenioService.buscarPorId(tipos).get();
			if (tc.getConvenio().equals(convenioId)) {
				// verifica se o tipo de convenio e do convenio escolhido
				tiposAceitos.add(tc);
			}
		}

		return tiposAceitos;
	}

	@GetMapping("/allconvenios/{id}/{labId}")
	public @ResponseBody List<TConvenio> listAllTipoConvenio(@PathVariable("id") String convenioId,
			@PathVariable("labId") String labId) {
		List<TConvenio> todosTipos = tipoConvenioService.listarTodos(convenioId); // pega todos os tipos de convenio do convenio selecionado
		List<TConvenio> tiposNaoCadastrados = new ArrayList<TConvenio>(); // array de retorno com todos os tipos n??o cadastrados do convenio selecionado
		Laboratorio laboratorio = laboratorioService.buscarPorId(labId).get(); // Objeto do laboratorio que esta sendo/ editado no momento
		for (TConvenio tc : todosTipos) { // Itera????o em todos os tipos de convenio do convenio selecionado
			if (!laboratorio.getTipo_convenio().contains(tc.getId())) {
				/*
				 * Compra????o para ver se a lista de tipos do laboratorio contem o tipo atual da
				 * itera????o A compara????o e feita com a propriedade contains do arraylist. Caso a
				 * lista n??o possua o id do tipo atual da itera????o, isso quer dizer que o tipo
				 * atual da itera????o n??o e cadastrado no laboratorio, logo ele ?? incluido no
				 * array de retorno
				 */
				tiposNaoCadastrados.add(tc); // inclui o tipo de convenio n??o cadastrado ao array de retorno
			}
		}

		return tiposNaoCadastrados;
	}

	@PostMapping("/salvarTConv/{labId}")
	public String salvarTConv(@RequestParam("alltipo_convenio_id") String tipo, @PathVariable("labId") String labId, RedirectAttributes attr) {
		Laboratorio lab = laboratorioService.buscarPorId(labId).get();
		
		lab.getTipo_convenio().add(tipo);
		laboratorioService.editar(lab);
		attr.addFlashAttribute("sucesso", "Novo tipo de convenio cadastrado com sucesso");
		return "redirect:/laboratorio/editar/" + labId;
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") String laboratorioId, RedirectAttributes attr) {
		laboratorioService.excluir(laboratorioId);
		attr.addFlashAttribute("sucesso", "Laboratorio excluido com sucesso");
		return "redirect:/laboratorio/listar";
	}
	
	@GetMapping("/excluirTConv/{id}/{labId}")
	@ResponseBody
	public void excluirTConv(@PathVariable("id") String id, @PathVariable("labId") String labId) {
		Laboratorio laboratorio = laboratorioService.buscarPorId(labId).get();
		if(laboratorio.getTipo_convenio().contains(id)) {
			laboratorio.getTipo_convenio().remove(id);
		}
		laboratorioService.editar(laboratorio);
	}

}
