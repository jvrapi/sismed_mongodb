package br.com.sismed.mongodb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.sismed.mongodb.domain.ListPaciente;
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
	public String buscarTodos(ModelMap model) {
		List<Paciente> paciente = pService.buscarTodos();
		List<ListPaciente> listaPacientes = new ArrayList<ListPaciente>();
		for(Paciente p : paciente) {
			TConvenio tc = tcService.buscarPorId(p.getTipo_convenio()).get();
			
			Convenio c = cService.buscarPorId(tc.getConvenio()).get();
			ListPaciente lp = new ListPaciente();
			lp.setId(p.getId());
			lp.setPaciente_prontuario(p.getProntuario());
			lp.setPaciente_nome(p.getNome());
			lp.setPaciente_rg(p.getRg());
			lp.setPaciente_cpf(p.getCpf());
			lp.setPaciente_convenio(c.getNome());
			lp.setPaciente_tipo_convenio(tc.getNome());
			lp.setPaciente_telefone(p.getTelefone_fixo());
			lp.setPaciente_celular(p.getCelular());
			listaPacientes.add(lp);
		}
		model.addAttribute("paciente", listaPacientes);
		return "/pacientes/lista";
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
