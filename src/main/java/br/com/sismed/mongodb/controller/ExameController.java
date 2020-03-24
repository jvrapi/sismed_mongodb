package br.com.sismed.mongodb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.domain.Exame;
import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.LabFlag;
import br.com.sismed.mongodb.domain.LabelValue;
import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.ExameService;
import br.com.sismed.mongodb.service.FuncionarioService;
import br.com.sismed.mongodb.service.LaboratorioService;
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

	@GetMapping("/cadastrar")
	public String cadastrar(ModelMap model, Exame exame) {
		model.addAttribute("paciente", pacienteService.buscarTodos());
		model.addAttribute("funcionario", funcionarioService.buscarTodos());
		return "exame/cadastro";
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
			lv.setValue2(paciente.getId());
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
	
	@GetMapping("/buscarlab/{id}")
	@ResponseBody
	public LabFlag listarlab(@PathVariable("id") Long id) {
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
