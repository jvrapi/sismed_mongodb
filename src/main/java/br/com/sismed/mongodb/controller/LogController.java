package br.com.sismed.mongodb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.ListLog;
import br.com.sismed.mongodb.domain.Log;
import br.com.sismed.mongodb.service.FuncionarioService;
import br.com.sismed.mongodb.service.LogService;


@Controller
@RequestMapping("/registro")
public class LogController extends AbstractController{

	@Autowired
	private LogService service;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	
	@GetMapping
	public String listarLogs(ModelMap model, @RequestParam(value = "page", required=false, defaultValue="1") int page) {
		PageRequest pagerequest = PageRequest.of(page-1, 10);
		Page<Log> log = service.listarTodos(pagerequest);
		List<ListLog> listarLog = new ArrayList<ListLog>();
		for(Log resultados : log) {
			Funcionario funcionario = funcionarioService.buscarporId(resultados.getFuncionario()).get();
			ListLog ll = new ListLog();
			ll.setId(resultados.getId());
			ll.setData(resultados.getData());
			ll.setHora(resultados.getHora());
			ll.setDescricao(resultados.getDescricao());
			ll.setFuncionario(funcionario);
			listarLog.add(ll);
		}
		model.addAttribute("logs", log);
		model.addAttribute("resultados", listarLog);
		int totalPages = log.getTotalPages();
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
		return "log/lista";
	}
	
}
