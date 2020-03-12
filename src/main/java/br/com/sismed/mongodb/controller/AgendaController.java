package br.com.sismed.mongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.sismed.mongodb.domain.Agenda;
import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.service.AgendaService;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.FuncionarioService;
import br.com.sismed.mongodb.service.PacienteService;
import br.com.sismed.mongodb.service.ProcedimentoService;
import br.com.sismed.mongodb.service.TConvenioService;

@Controller
@RequestMapping("/agenda")
public class AgendaController extends AbstractController{
	
	@Autowired
	private AgendaService service;

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private TConvenioService tconvenioService;

	@Autowired
	private ProcedimentoService procedimentoService;

	@Autowired
	private ConvenioService convenioService;

	@Autowired
	private FuncionarioService funcionarioService;

	@GetMapping("/agendamentos")
	public String abrirAgendaDoDia(ModelMap model, @AuthenticationPrincipal User user) {
		return "";
	}
	
	@GetMapping("/agendar/{id}")
	public String abrirPaginaAgendamento(@PathVariable("id") String id, ModelMap model, Agenda agendar) {
		Paciente paciente = pacienteService.buscarPorId(id).get();
		Convenio convenio = convenioService.buscarPorId(paciente.getTipo_convenio().getConvenio()).get();
		model.addAttribute("paciente", paciente);
		model.addAttribute("funcionario", funcionarioService.buscarTodos());
		model.addAttribute("convenio", convenio);
	
		return "agenda/agendarPacienteCadastrado";
	}

}
