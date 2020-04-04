package br.com.sismed.mongodb.config;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;




import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class BackupAutomatico {

	private static final String TIME_ZONE = "America/Sao_Paulo";


	@Value("${spring.data.mongodb.username}")
	private String host;

	@Value("${spring.data.mongodb.password}")
	private String password;
	
	@Value("${spring.data.mongodb.database}")
	private String dataBase;

	@Scheduled(cron = "0 20 13 * * *", zone = TIME_ZONE) // anotação que faz o agendamento da execução. O cron serve
															// para dizer quando sera executado
	public void gerarBackup(){
		
		
		List<String> colecoes = preencherArray();
		
		LocalDate data = LocalDate.now();
		
		for(String t : colecoes) {
			

			// caminho aonde sera salvo o arquivo mysql. Nesse momento, a pasta com a data
			// atualnão está criada
			String caminho = "c:\\sismed\\backup\\" + data;

			// String do mysql para gerar o arquivo sql
			String dump = "mongodump -u " + host + " -p " + password + "  --authenticationDatabase \"admin\" --db " + dataBase + " --collection " + t + " --out " + caminho;

			/*
			 * Comandos que serão executados a primeira string, define aonde sera salvo o
			 * arquivo de backup. logo depois, cria uma pasta com a data atual, atraves do
			 * comando 'md' e por ultimo, ultiliza o comando dump do mysql da maquina para
			 * gerar o arquivo
			 * 
			 */
			String[] comando = { "cd c:\\sismed\\backup", "md " + data,dump };
			try {
				ProcessBuilder builder = new ProcessBuilder("cmd", "/c", String.join("& ", comando));
				builder.redirectErrorStream(true);
				builder.start();

			} catch (Exception a) {
				a.printStackTrace();
			}
		}

		
	}
	
	public List<String> preencherArray(){
		List<String> colecoes = new ArrayList<String>();
		colecoes.add("sismed_agenda");
		colecoes.add("sismed_convenio");
		colecoes.add("sismed_custos");
		colecoes.add("sismed_exame");
		colecoes.add("sismed_funcionario");
		colecoes.add("sismed_laboratorio");
		colecoes.add("sismed_log");
		colecoes.add("sismed_paciente");
		colecoes.add("sismed_procedimento");
		colecoes.add("sismed_registro_clinico");
		colecoes.add("sismed_tipo_convenio");
		
		return colecoes;
	}

}
