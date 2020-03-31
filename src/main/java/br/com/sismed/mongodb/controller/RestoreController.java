package br.com.sismed.mongodb.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/restore")
public class RestoreController {
	@Value("${spring.data.mongodb.username}")
	private String host;

	@Value("${spring.data.mongodb.password}")
	private String password;

	@Value("${spring.data.mongodb.database}")
	private String dataBase;
	
	@GetMapping
	public String listar() {

		return "restore/tabelas";
	}
	
	@PostMapping
	public String realizarRestore(@RequestParam("tabelas") List<String> tabelas, @RequestParam("data") String data, RedirectAttributes attr, ModelMap model) throws IOException {

		String caminho = "c:\\sismed\\backup\\" + data + "\\" + dataBase+"\\";
		String retorno ="";
		for (String t : tabelas) {
			String arquivo = t + ".bson";
			String dump = "mongorestore -u " + host+  " -p " + password + " --authenticationDatabase \"admin\" --db " + dataBase + " --collection " + t + " " + caminho + arquivo;
			String[] comando = {"cd C:\\Program Files\\MongoDB\\Server\\4.2\\bin",dump};
			
			try {
				
				FileInputStream stream = new FileInputStream(caminho + arquivo);
				InputStreamReader reader = new InputStreamReader(stream);
				BufferedReader br = new BufferedReader(reader);
				String linha = br.readLine();
				while (linha != null) {
					
					linha = br.readLine();
				}
				ProcessBuilder builder = new ProcessBuilder("cmd", "/c", String.join("& ", comando));
				builder.redirectErrorStream(true);
				builder.start();
				attr.addFlashAttribute("sucesso", "Dados restaurados com sucesso");
				retorno = "redirect:/restore";
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
