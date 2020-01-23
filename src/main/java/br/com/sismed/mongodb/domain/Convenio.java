package br.com.sismed.mongodb.domain;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="sismed_convenio")
public class Convenio extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private String nome;

	private LocalDate data_adesao;

	private String cnpj;

	private String registro_ans;
	
	public Convenio() {
		
	}
	
	public Convenio(String nome, LocalDate data_adesao, String cnpj, String registro_ans) {
		super();
		this.nome = nome;
		this.data_adesao = data_adesao;
		this.cnpj = cnpj;
		this.registro_ans = registro_ans;
	}



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

	
}
