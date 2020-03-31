package br.com.sismed.mongodb.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/backup")
public class Backupcontroller {
	
	@Value("${spring.data.mongodb.username}")
	private String host;

	@Value("${spring.data.mongodb.password}")
	private String password;
	
	@Value("${spring.data.mongodb.database}")
	private String dataBase;

	@GetMapping
	public String listar() {

		return "backup/tabelas";
	}

	@PostMapping("/gerar")
	public String gerarBackup(@RequestParam("tabelas") List<String> tabelas, @AuthenticationPrincipal User user, RedirectAttributes attr, ModelMap model) {

		LocalDate data = LocalDate.now();

		String caminho = "c:\\sismed\\backup\\" + data ;
		String retorno ="";
		for (String t : tabelas) {
			String dump = "mongodump -u " + host + " -p " + password + "  --authenticationDatabase \"admin\" --db " + dataBase + " --collection " + t + " --out " + caminho;

			String[] comando = { "cd c:\\sismed\\backup", "md " + data, dump };

			try {

				ProcessBuilder builder = new ProcessBuilder("cmd", "/c", String.join("& ", comando));
				
				builder.redirectErrorStream(true);
				builder.start();
				attr.addFlashAttribute("sucesso", "Copia de dados realizada com sucesso");
				retorno = "redirect:/backup";
			} catch (Exception a) {
				model.addAttribute("error", "Não foi possivel realizar a operação");
				model.addAttribute("message", "Entre em contato com suporte para solucionar esse problema");
				retorno = "error";
				a.printStackTrace();
			}

		}
		
		return retorno;
	}

}
