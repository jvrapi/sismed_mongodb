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

	@GetMapping("/cadastrar")
	public String cadastrar(Laboratorio laboratorio) {
		return "laboratorio/cadastro";
	}

	@PostMapping("/salvar")
	public String salvar(Laboratorio laboratorio, RedirectAttributes attr) {
		laboratorioService.salvar(laboratorio);
		attr.addFlashAttribute("sucesso", "Laboratório cadastrado com sucesso");
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
		List<TConvenio> tiposNaoCadastrados = new ArrayList<TConvenio>(); // array de retorno com todos os tipos não cadastrados do convenio selecionado
		Laboratorio laboratorio = laboratorioService.buscarPorId(labId).get(); // Objeto do laboratorio que esta sendo/ editado no momento
		for (TConvenio tc : todosTipos) { // Iteração em todos os tipos de convenio do convenio selecionado
			if (!laboratorio.getTipo_convenio().contains(tc.getId())) {
				/*
				 * Compração para ver se a lista de tipos do laboratorio contem o tipo atual da
				 * iteração A comparação e feita com a propriedade contains do arraylist. Caso a
				 * lista não possua o id do tipo atual da iteração, isso quer dizer que o tipo
				 * atual da iteração não e cadastrado no laboratorio, logo ele é incluido no
				 * array de retorno
				 */
				tiposNaoCadastrados.add(tc); // inclui o tipo de convenio não cadastrado ao array de retorno
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

	/*
	 * @GetMapping("/excluirTConv/{id}/{labId}")
	 * 
	 * @ResponseBody public void excluirTConv(@PathVariable("id") Long
	 * id, @PathVariable("labId") Long labId) { ltcService.excluir(id, labId); }
	 * 
	 * 
	 */

}
