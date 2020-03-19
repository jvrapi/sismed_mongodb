package br.com.sismed.mongodb.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import br.com.sismed.mongodb.domain.Custos;
import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.LabelValue;
import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.service.ConvenioService;
import br.com.sismed.mongodb.service.CustosService;
import br.com.sismed.mongodb.service.FuncionarioService;
import br.com.sismed.mongodb.service.PacienteService;



@Controller
@RequestMapping("/relatorio")
public class RelatorioController extends AbstractController{
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private ConvenioService convenioService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private CustosService service;
	
	
	@GetMapping("/listar")
	public String listar() {
		return "relatorio/lista";
	}
	
	@GetMapping
	public ResponseEntity<List<Custos>> mostrarDados(){
		List<Custos> lista = service.buscarPorConvenio("5e67db1439b944511513af1a");
		return ResponseEntity.ok().body(lista);
	}
	
	
	@GetMapping("/buscar/{id}")
	@ResponseBody
	public List<LabelValue> listar(@PathVariable("id") Integer id,
			@RequestParam(value = "term", required = false, defaultValue = "") String term) {
		List<LabelValue> suggeestions = new ArrayList<LabelValue>();
		if (id == 1) {
			List<Paciente> allPacientes = pacienteService.ListarPacNome(term);
			for (Paciente paciente : allPacientes) {
				LabelValue lv = new LabelValue();
				lv.setLabel(paciente.getNome());
				lv.setValue2(paciente.getId());
				suggeestions.add(lv);
			}
		} else if (id == 2) {
			List<Convenio> allConvenios = convenioService.ListarPorNome(term);
			for (Convenio convenio : allConvenios) {
				LabelValue lv = new LabelValue();
				lv.setLabel(convenio.getNome());
				lv.setValue2(convenio.getId());
				suggeestions.add(lv);
			}
		}

		else if (id == 3) {
			List<Funcionario> allFunc = funcionarioService.ListarFuncionarioNome(term);
			for (Funcionario f : allFunc) {
				
				LabelValue lv = new LabelValue();
				lv.setLabel(f.getNome());
				lv.setValue2(f.getId());
				suggeestions.add(lv);
			}
		}

		return suggeestions;

	}
	
	@PostMapping("/gerar")
	public String gerarRelatorio(@RequestParam("paciente") String paciente, @RequestParam("convenio") String convenio,
			@RequestParam("dataInicioValor") String dataInicio, @RequestParam("dataFimValor") String dataFim,
			@RequestParam("funcionario") Long funcionario, RedirectAttributes attr, ModelMap model) {
		System.out.println(convenio);
		if (convenio == null && dataInicio == "" && funcionario == null) {
			// so por paciente
			Paciente p = pacienteService.buscarPorId(paciente).get();
			model.addAttribute("resultado", service.buscarPorPaciente(paciente));
			
			if(service.buscarReceitaPorPaciente(paciente) == null) {
				model.addAttribute("receita", "O paciente " + p.getNome() + " não gerou receita.");
			}
			else {
				model.addAttribute("receita", "O paciente " + p.getNome() + " Gerou uma receita de R$ " + service.buscarReceitaPorPaciente(paciente));
			}
			
		}

		else if (paciente == null && dataInicio == "" && funcionario == null) {
			// so por convenio
			if(!convenio.equals("0")) {
			Convenio c = convenioService.buscarPorId(convenio).get();
			model.addAttribute("resultado", service.buscarPorConvenio(convenio));
			if(service.buscarPorConvenio(convenio) == null) {
				model.addAttribute("receita", "O convênio " + c.getNome() + " não gerou receita.");
			}else {
				model.addAttribute("receita", "O convênio " + c.getNome() + " Gerou uma receita de R$ " + service.buscarPorConvenio(convenio));
			}
			
			}
			/*if (convenio.equals(0)) {
				model.addAttribute("resultado", service.buscarTodosConvenios());
				if(service.receitaTodosConvenios() == null) {
					model.addAttribute("receita", "Os convênios não geraram receita ");
					
				}else {
					model.addAttribute("receita", "Os convênios geraram uma receita de R$: " + service.receitaTodosConvenios());	
				}
				
			}*/
		}

		/*else if (paciente == null && convenio == null && dataInicio == "") {
			// so por funcionario
			Funcionario f = fservice.buscarporId(funcionario);
			model.addAttribute("resultado", service.buscarPorFuncionario(funcionario));
			if(service.buscarReceitaPorFuncionario(funcionario) == null) {
				model.addAttribute("receita", "O funcionario " + f.getNome() + " não gerou receita.");
			}else {
				model.addAttribute("receita", "O funcionario " + f.getNome() + " gerou uma receita de R$: " + service.buscarReceitaPorFuncionario(funcionario));
			}
			
		}

		else if (paciente == null && convenio == null && funcionario == null) {
			// entre um periodo
			LocalDate data1 = LocalDate.parse(dataInicio);
			LocalDate data2 = LocalDate.parse(dataFim);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String dataInicioFormatada = data1.format(formatter);
			String dataFimFormatada = data2.format(formatter);
			model.addAttribute("resultado", service.buscarPorDatas(dataInicio, dataFim));
			if(service.buscarReceitaPorDatas(dataInicio, dataFim) == null) {
				model.addAttribute("receita", "Entre o periodo de " + dataInicioFormatada + " a " + dataFimFormatada + " não foi gerada uma receita " );

			}else {
				model.addAttribute("receita", "Entre o periodo de " + dataInicioFormatada + " a " + dataFimFormatada + " foi gerada uma receita de R$: " + service.buscarReceitaPorDatas(dataInicio, dataFim));

			}
			
		} else if (funcionario == null && convenio == null) {
			// Paciente e Periodo
			Paciente p = pservice.buscarporId(paciente);
			LocalDate data1 = LocalDate.parse(dataInicio);
			LocalDate data2 = LocalDate.parse(dataFim);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String dataInicioFormatada = data1.format(formatter);
			String dataFimFormatada = data2.format(formatter);
			
			model.addAttribute("resultado", service.PacientePeriodo(paciente, dataInicio, dataFim));
			if(service.ReceitaPacientePeriodo(paciente, dataInicio, dataFim) == null) {
				model.addAttribute("receita", "O paciente "+ p.getNome() + " no periodo de "+ dataInicioFormatada + " a " + dataFimFormatada + " não gerou receita.");
			}
			else{
				model.addAttribute("receita", "O paciente "+ p.getNome() + " no periodo de "+ dataInicioFormatada + " a " + dataFimFormatada + " gerou uma receita de R$: " +service.ReceitaPacientePeriodo(paciente, dataInicio, dataFim));
			}
			
		} else if (funcionario == null && paciente == null) {
			// convenio periodo
			LocalDate data1 = LocalDate.parse(dataInicio);
			LocalDate data2 = LocalDate.parse(dataFim);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String dataInicioFormatada = data1.format(formatter);
			String dataFimFormatada = data2.format(formatter);
			
			if(convenio != 0){
			Convenio c = cservice.buscarPorId(convenio);
			model.addAttribute("resultado", service.ConvenioPeriodo(convenio, dataInicio, dataFim));
			if(service.ReceitaConvenioPeriodo(convenio, dataInicio, dataFim) == null) {
				model.addAttribute("receita", "O convenio "+ c.getNome() + 
						" no periodo de " + dataInicioFormatada + " a " + dataFimFormatada  + " não gerou uma receita. ");
			}else {
				model.addAttribute("receita", "O convenio "+ c.getNome() + 
						" no periodo de " + dataInicioFormatada + " a " + dataFimFormatada  + " gerou uma receita de R$: " + 
						service.ReceitaConvenioPeriodo(convenio, dataInicio, dataFim));
			}
			
			}
			
			if (convenio == 0) {
				model.addAttribute("resultado", service.TodosConvenioPeriodo(dataInicio, dataFim));
				if( service.ReceitaTodosConvenioPeriodo(dataInicio, dataFim) == null) {
					model.addAttribute("receita", "Entre o periodo de " + dataInicioFormatada + " a " + dataFimFormatada + " os convenios não geraram uma receita. ");
				}else {
					model.addAttribute("receita", "Entre o periodo de " + dataInicioFormatada + " a " + dataFimFormatada + " os convenios geraram uma receita de R$: " 
							+  service.ReceitaTodosConvenioPeriodo(dataInicio, dataFim));
				}
				
			}
		} else if (paciente == null && convenio == null) {
			// funcionario e periodo
			
			LocalDate data1 = LocalDate.parse(dataInicio);
			LocalDate data2 = LocalDate.parse(dataFim);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String dataInicioFormatada = data1.format(formatter);
			String dataFimFormatada = data2.format(formatter);
			
			Funcionario f = fservice.buscarporId(funcionario);
			model.addAttribute("resultado", service.FuncionarioPeriodo(funcionario, dataInicio, dataFim));
			if(service.ReceitaFuncionarioPeriodo(funcionario, dataInicio, dataFim) == null) {
				model.addAttribute("receita", "O medico "+ f.getNome() + " no periodo de "+ dataInicioFormatada + " a " + dataFimFormatada + " não gerou uma receita. ");
			}else {
				model.addAttribute("receita", "O medico "+ f.getNome() + " no periodo de "+ dataInicioFormatada + " a " + dataFimFormatada + " gerou uma receita de R$: " 
						+ service.ReceitaFuncionarioPeriodo(funcionario, dataInicio, dataFim));
			}
			

		}*/

		return "relatorio/lista";
	}
	
	@ModelAttribute("convenios")
	public List<Convenio> allConvenios() {
		return convenioService.buscarTodos();
	}
}
