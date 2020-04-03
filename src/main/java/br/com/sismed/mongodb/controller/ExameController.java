package br.com.sismed.mongodb.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
import br.com.sismed.mongodb.domain.Exame;
import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.LabFlag;
import br.com.sismed.mongodb.domain.LabelValue;
import br.com.sismed.mongodb.domain.ListExame;
import br.com.sismed.mongodb.domain.Log;
import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.ExameService;
import br.com.sismed.mongodb.service.FuncionarioService;
import br.com.sismed.mongodb.service.LaboratorioService;
import br.com.sismed.mongodb.service.LogService;
import br.com.sismed.mongodb.service.PacienteService;
import br.com.sismed.mongodb.service.TConvenioService;

@Controller
@RequestMapping("/exame")
public class ExameController extends AbstractController {

	@Autowired
	private ExameService exameService;

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private TConvenioService tipoConvenioService;

	@Autowired
	private ConvenioService convenioService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private LaboratorioService laboratorioService;

	@Autowired
	private LogService logService;

	@GetMapping("/listar")

	public String listar(ModelMap model, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		PageRequest pagerequest = PageRequest.of(page - 1, 5, Sort.by("id").descending());
		Page<Exame> exame = exameService.buscarTodos(pagerequest);
		List<ListExame> listarExames = new ArrayList<ListExame>();
		for (Exame e : exame) {
			Paciente p = pacienteService.buscarPorId(e.getPaciente()).get();
			ListExame le = new ListExame();
			le.setId(e.getId());
			le.setExame_nome(e.getNome());
			le.setExame_tipo(e.getTipo());
			le.setPaciente(p);
			le.setData_coleta(e.getData_coleta());
			le.setData_envio(e.getData_envio());
			le.setData_retorno(e.getData_retorno());
			listarExames.add(le);
		}
		model.addAttribute("listExames", listarExames);
		model.addAttribute("exame", exame);
		int lastPage = exame.getTotalPages();

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

		return "exame/lista";
	}

	@PostMapping("/buscarlista")
	public String buscarlista(ModelMap model, @RequestParam("id_paciente") String paciente_id,
			@RequestParam("nome_exame") String exame, @RequestParam("coleta_data") String data) {

		if (exame == "" && data == "") {
			// Busca apenas por paciente
			List<Exame> resultados = exameService.listarExamesPorPaciente(paciente_id);
			List<ListExame> listarResultados = new ArrayList<ListExame>();
			for (Exame e : resultados) {
				Paciente p = pacienteService.buscarPorId(e.getPaciente()).get();
				ListExame le = new ListExame();
				le.setId(e.getId());
				le.setExame_nome(e.getNome());
				le.setExame_tipo(e.getTipo());
				le.setPaciente(p);
				le.setData_coleta(e.getData_coleta());
				le.setData_envio(e.getData_envio());
				le.setData_retorno(e.getData_retorno());
				listarResultados.add(le);
			}
			model.addAttribute("exame", listarResultados);
		} else if (paciente_id == "" && data == "") {

			// Busca Apenas por nome do exame
			List<Exame> resultados = exameService.listarExamesPorNome(exame);
			List<ListExame> listarResultados = new ArrayList<ListExame>();
			for (Exame e : resultados) {
				Paciente p = pacienteService.buscarPorId(e.getPaciente()).get();
				ListExame le = new ListExame();
				le.setId(e.getId());
				le.setExame_nome(e.getNome());
				le.setExame_tipo(e.getTipo());
				le.setPaciente(p);
				le.setData_coleta(e.getData_coleta());
				le.setData_envio(e.getData_envio());
				le.setData_retorno(e.getData_retorno());
				listarResultados.add(le);
			}
			model.addAttribute("exame", listarResultados);
		} else if (paciente_id == "" && exame == "") {
			//Busca apenas por data de coleta
			List<Exame> resultados = exameService.listarExamesPorDataColeta(data);
			List<ListExame> listarResultados = new ArrayList<ListExame>();
			for (Exame e : resultados) {
				Paciente p = pacienteService.buscarPorId(e.getPaciente()).get();
				ListExame le = new ListExame();
				le.setId(e.getId());
				le.setExame_nome(e.getNome());
				le.setExame_tipo(e.getTipo());
				le.setPaciente(p);
				le.setData_coleta(e.getData_coleta());
				le.setData_envio(e.getData_envio());
				le.setData_retorno(e.getData_retorno());
				listarResultados.add(le);
			}
			model.addAttribute("exame", listarResultados);
		}else if (data == "") {
			//Busca por Paciente e Nome do Exame
			List<Exame> resultados = exameService.listarExamesPorNomeEPaciente(exame,paciente_id);
			List<ListExame> listarResultados = new ArrayList<ListExame>();
			for (Exame e : resultados) {
				Paciente p = pacienteService.buscarPorId(e.getPaciente()).get();
				ListExame le = new ListExame();
				le.setId(e.getId());
				le.setExame_nome(e.getNome());
				le.setExame_tipo(e.getTipo());
				le.setPaciente(p);
				le.setData_coleta(e.getData_coleta());
				le.setData_envio(e.getData_envio());
				le.setData_retorno(e.getData_retorno());
				listarResultados.add(le);
			}
			model.addAttribute("exame", listarResultados);
		}  else if (exame == "") {
			// Busca por paciente e data de coleta
			List<Exame> resultados = exameService.listarExamesPorPacienteEDataColeta(paciente_id,data);
			List<ListExame> listarResultados = new ArrayList<ListExame>();
			for (Exame e : resultados) {
				Paciente p = pacienteService.buscarPorId(e.getPaciente()).get();
				ListExame le = new ListExame();
				le.setId(e.getId());
				le.setExame_nome(e.getNome());
				le.setExame_tipo(e.getTipo());
				le.setPaciente(p);
				le.setData_coleta(e.getData_coleta());
				le.setData_envio(e.getData_envio());
				le.setData_retorno(e.getData_retorno());
				listarResultados.add(le);
			}
			model.addAttribute("exame", listarResultados);
		} else if (paciente_id == "") {
			//Busca por nome do exame e data de coleta
			List<Exame> resultados = exameService.listarExamesPorNomeEDataColeta(exame,data);
			List<ListExame> listarResultados = new ArrayList<ListExame>();
			for (Exame e : resultados) {
				Paciente p = pacienteService.buscarPorId(e.getPaciente()).get();
				ListExame le = new ListExame();
				le.setId(e.getId());
				le.setExame_nome(e.getNome());
				le.setExame_tipo(e.getTipo());
				le.setPaciente(p);
				le.setData_coleta(e.getData_coleta());
				le.setData_envio(e.getData_envio());
				le.setData_retorno(e.getData_retorno());
				listarResultados.add(le);
			}
			model.addAttribute("exame", listarResultados);
		}else if (paciente_id != "" && exame != "" && data != "") {
			//Busca por paciente, nome do exame e por data de coleta
			List<Exame> resultados = exameService.listarExamesPorNomeEPacienteEDataColeta(exame,paciente_id,data);
			List<ListExame> listarResultados = new ArrayList<ListExame>();
			for (Exame e : resultados) {
				Paciente p = pacienteService.buscarPorId(e.getPaciente()).get();
				ListExame le = new ListExame();
				le.setId(e.getId());
				le.setExame_nome(e.getNome());
				le.setExame_tipo(e.getTipo());
				le.setPaciente(p);
				le.setData_coleta(e.getData_coleta());
				le.setData_envio(e.getData_envio());
				le.setData_retorno(e.getData_retorno());
				listarResultados.add(le);
			}
			model.addAttribute("exame", listarResultados);
		}
		return "exame/buscar_lista";
	}

	@GetMapping("/cadastrar")
	public String cadastrar(ModelMap model, Exame exame) {
		model.addAttribute("paciente", pacienteService.buscarTodos());
		model.addAttribute("funcionario", funcionarioService.buscarTodos());
		return "exame/cadastro";
	}

	@PostMapping("/salvar")
	public String salvar(Exame exame, RedirectAttributes attr) {
		attr.addFlashAttribute("sucesso", "Exame cadastrado com sucesso");
		exameService.salvar(exame);
		return "redirect:/exame/listar";
	}

	@GetMapping("/editar/{id}/{tcid}")
	public String preEditar(@PathVariable("id") String id, @PathVariable("tcid") String tcid, ModelMap model) {
		Exame exame = exameService.buscarporId(id).get();
		Funcionario funcionario = funcionarioService.buscarporId(exame.getFuncionario()).get();
		Paciente paciente = pacienteService.buscarPorId(exame.getPaciente()).get();
		TConvenio tc = tipoConvenioService.buscarPorId(exame.getTipo()).get();
		Convenio convenio = convenioService.buscarPorId(tc.getConvenio()).get();

		model.addAttribute("exame", exame);
		model.addAttribute("funcionario", funcionario);
		model.addAttribute("paciente", paciente);
		model.addAttribute("convenio", convenio);
		model.addAttribute("tipoConvenio", tc);
		model.addAttribute("laboratorio", laboratorioService.ListarLabTConv(tcid));
		return "exame/editar";
	}

	@PostMapping("/editar")
	public String editar(Exame exame, RedirectAttributes attr, @AuthenticationPrincipal User user) {

		Exame e = exameService.buscarporId(exame.getId()).get();
		Paciente p = pacienteService.buscarPorId(e.getPaciente()).get();
		Funcionario f = funcionarioService.buscarPorCpf(user.getUsername());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		if (!exame.getData_coleta().isEqual(e.getData_coleta())) {
			Log l = new Log();
			l.setData(LocalDate.now());
			l.setFuncionario(f.getId());
			l.setHora(LocalTime.now());
			l.setDescricao("ALTERÇÃO NA DATA DE COLETA: NOME DO EXAME " + e.getNome() + ". NOME DO PACIENTE: "
					+ p.getNome() + ". DA DATA " + e.getData_coleta().format(formatter) + " PARA A DATA "
					+ exame.getData_coleta().format(formatter));
			logService.salvar(l);
		}

		if (!exame.getData_envio().isEqual(e.getData_envio())) {
			Log l = new Log();
			l.setData(LocalDate.now());
			l.setFuncionario(f.getId());
			l.setHora(LocalTime.now());
			l.setDescricao("ALTERÇÃO NA DATA DE ENVIO: NOME DO EXAME " + e.getNome() + ". NOME DO PACIENTE "
					+ p.getNome() + ". DA DATA " + e.getData_envio().format(formatter) + " PARA A DATA "
					+ exame.getData_envio().format(formatter));
			logService.salvar(l);
		}
		if (exame.getData_retorno() != null) {
			System.out.println("prmeiro if");
			if (e.getData_retorno() != null && !exame.getData_retorno().equals(e.getData_retorno())) {
				System.out.println("segundo if");
				Log l = new Log();
				l.setData(LocalDate.now());
				l.setFuncionario(f.getId());
				l.setHora(LocalTime.now());
				l.setDescricao("ALTERÇÃO DE DATA DE RETORNO: NOME DO EXAME: " + e.getNome() + ". NOME DO PACIENTE: "
						+ p.getNome() + ". DA DATA " + e.getData_retorno().format(formatter) + " PARA A DATA "
						+ exame.getData_retorno().format(formatter));
				logService.salvar(l);
			} else if (e.getData_retorno() == null) {
				System.out.println("segundo if");
				Log l = new Log();
				l.setData(LocalDate.now());
				l.setFuncionario(f.getId());
				l.setHora(LocalTime.now());
				l.setDescricao("ALTERÇÃO DE DATA DE RETORNO: NOME DO EXAME: " + e.getNome() + ". NOME DO PACIENTE: "
						+ p.getNome() + ". DE DATA DE RETORNO PENDENTE PARA A DATA "
						+ exame.getData_retorno().format(formatter));
				logService.salvar(l);
			}
		} else if (e.getData_retorno() != null) {
			Log l = new Log();
			l.setData(LocalDate.now());
			l.setFuncionario(f.getId());
			l.setHora(LocalTime.now());
			l.setDescricao(
					"ALTERÇÃO DE DATA DE RETORNO: NOME DO EXAME: " + e.getNome() + ". NOME DO PACIENTE: " + p.getNome()
							+ ". DA DATA " + e.getData_retorno().format(formatter) + " PARA DATA DE RETORNO PENDENTE");
			logService.salvar(l);
		}
		exameService.salvar(exame);
		attr.addFlashAttribute("sucesso", "Exame alterado com sucesso");
		return "redirect:/exame/editar/" + exame.getId() + "/" + p.getTipo_convenio();
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") String id, ModelMap model, RedirectAttributes attr,
			@AuthenticationPrincipal User user) {

		String retorno = "";
		Exame e = exameService.buscarporId(id).get();
		Paciente p = pacienteService.buscarPorId(e.getPaciente()).get();
		try {
			Funcionario f = funcionarioService.buscarPorCpf(user.getUsername());
			Log l = new Log();
			l.setData(LocalDate.now());
			l.setFuncionario(f.getId());
			l.setHora(LocalTime.now());
			l.setDescricao("EXCLUSÃO DE EXAME: NOME DO EXAME: " + e.getNome() + ". NOME DO PACIENTE: " + p.getNome());
			logService.salvar(l);
			model.addAttribute("sucesso", "Exame excluído com sucesso");
			exameService.excluir(id);
			attr.addFlashAttribute("sucesso", "Exame excluido com sucesso");
			retorno = "redirect:/exame/listar";
		} catch (DataIntegrityViolationException error) {
			attr.addFlashAttribute("falha", "Não foi possível excluir");
			retorno = "redirect:/exame/listar";
		}
		return retorno;
	}

	/* Metodos para JS */

	@GetMapping("/buscarpaciente")
	@ResponseBody
	public List<LabelValue> listarpaciente(
			@RequestParam(value = "term", required = false, defaultValue = "") String term) {
		List<LabelValue> suggeestions = new ArrayList<LabelValue>();

		List<Paciente> allPacientes = pacienteService.ListarPacNome(term);
		for (Paciente paciente : allPacientes) {
			TConvenio tc = tipoConvenioService.buscarPorId(paciente.getTipo_convenio()).get();
			Convenio c = convenioService.buscarPorId(tc.getConvenio()).get();
			LabelValue lv = new LabelValue();
			lv.setLabel(paciente.getNome());
			lv.setValue(paciente.getId());
			lv.setConvenio(c.getId());
			lv.setNome_convenio(c.getNome());
			lv.setTipo(tc.getId());
			lv.setNome_tipo(tc.getNome());
			suggeestions.add(lv);
		}

		return suggeestions;
	}

	@GetMapping("/buscarfuncionario")
	@ResponseBody
	public List<LabelValue> listarfuncionario(
			@RequestParam(value = "term", required = false, defaultValue = "") String term) {
		List<LabelValue> suggeestions = new ArrayList<LabelValue>();
		List<Funcionario> allFuncionarios = funcionarioService.ListarFuncionarioNome(term);
		for (Funcionario funcionario : allFuncionarios) {
			LabelValue lv = new LabelValue();
			lv.setLabel(funcionario.getNome());
			lv.setValue(funcionario.getId());

			suggeestions.add(lv);
		}
		return suggeestions;
	}

	@GetMapping("/buscarexame")
	@ResponseBody
	public List<LabelValue> listarexame(
			@RequestParam(value = "term", required = false, defaultValue = "") String term) {

		List<LabelValue> suggeestions = new ArrayList<LabelValue>();
		List<Exame> buscarExamePorNome = exameService.listarExamesPorNome(term);
		String exameInserido = "";
		for (Exame resultados : buscarExamePorNome) {
			
			LabelValue lv = new LabelValue();
			lv.setLabel(resultados.getNome());
			lv.setValue(resultados.getId());
			if (suggeestions.isEmpty()) {
				exameInserido = resultados.getNome();
				suggeestions.add(lv);
			} else if(!resultados.getNome().equals(exameInserido)) {
				exameInserido = resultados.getNome();
				suggeestions.add(lv);
			}
			
		}
		return suggeestions;
	}

	@GetMapping("/buscarlab/{id}")
	@ResponseBody
	public LabFlag listarlab(@PathVariable("id") String id) {
		LabFlag listLabFlag = new LabFlag();
		listLabFlag.setListLab(laboratorioService.ListarLabTConv(id));
		if (!listLabFlag.getListLab().isEmpty()) {
			listLabFlag.setFlag(1);
			return listLabFlag;
		} else {
			listLabFlag.setListLab(laboratorioService.buscarTodos());
			listLabFlag.setFlag(0);
			return listLabFlag;
		}
	}

}
