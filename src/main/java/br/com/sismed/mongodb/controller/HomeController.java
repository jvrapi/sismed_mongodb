package br.com.sismed.mongodb.controller;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.service.FuncionarioService;

@Controller
public class HomeController{
	@Autowired
	private FuncionarioService funcionarioService;
	@GetMapping({ "/", "/home" })
	public String paginaInicial(@AuthenticationPrincipal User user, ModelMap model) {
		Funcionario funcionario = funcionarioService.buscarPorCpf(user.getUsername());
		String pattern = "\\S+";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(funcionario.getNome());

		if (m.find()) {
	         // escreva o grupo encontrado
			model.addAttribute("usuario",  m.group(0));
	         
	      } else {
	         // mensagem de erro
	    	  model.addAttribute("usuario",  funcionario.getNome());
	      }
		
		 
		return "home";
	}

	// abrir pagina login
	@GetMapping({ "/login" })
	public String login() {

		return "login";
	}

	// login invalido
	@GetMapping({ "/login-error" })
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Crendenciais inválidas!");
		model.addAttribute("texto", "Login ou senha incorretos, tente novamente.");
		model.addAttribute("subtexto", "Acesso permitido apenas para cadastros já ativados.");
		return "login";
	}

	// acesso negado
	@GetMapping({ "/acesso-negado" })
	public String acessoNegado(ModelMap model, HttpServletResponse resp) {
		model.addAttribute("status", resp.getStatus()); // codigo do status
		model.addAttribute("error", "Acesso Negado");
		model.addAttribute("message", "Você não tem acesso a está area ou ação");

		return "error";
	}
	

}


