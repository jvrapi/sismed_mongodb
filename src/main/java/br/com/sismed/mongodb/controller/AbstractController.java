package br.com.sismed.mongodb.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.service.FuncionarioService;



public abstract class AbstractController {
	@Autowired
	private FuncionarioService funcionarioService;
	
	@ModelAttribute("usuarioLogado")
	public String usuarioLogado(@AuthenticationPrincipal User user, ModelMap model) {
		Funcionario funcionario = funcionarioService.buscarPorCpf(user.getUsername());
		String pattern = "\\S+";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(funcionario.getNome());
		String retorno = "";
		if (m.find()) {
			retorno = m.group(0);

			model.addAttribute("usuario", m.group(0));

		} else {
			// mensagem de erro
			retorno = funcionario.getNome();
		}

		return retorno;
	}
	
	
}
