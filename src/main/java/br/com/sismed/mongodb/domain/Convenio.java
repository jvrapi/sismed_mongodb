package br.com.sismed.mongodb.domain;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;



@Document(collection = "sismed_convenio")
public class Convenio extends AbstractEntity{


	private String nome;
	
	
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data_adesao ;
	
	
	
	private String cnpj;
	
	
	private String registro_ans;

	private DadosBancarios dadosb;
	
	

	
	
	/*Metodos get's e set's */

	
	public String getNome() {
		return nome;
	}
	
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
	public LocalDate getData_adesao() {
		return data_adesao;
	}


	public void setData_adesao(LocalDate data_adesao) {
		this.data_adesao = data_adesao;
	}


	public String getCnpj() {
		return cnpj;
	}


	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}


	public String getRegistro_ans() {
		return registro_ans;
	}


	public void setRegistro_ans(String registro_ans) {
		this.registro_ans = registro_ans;
	}


	public DadosBancarios getDadosb() {
		return dadosb;
	}


	public void setDadosb(DadosBancarios dadosb) {
		this.dadosb = dadosb;
	}




		
	
}
