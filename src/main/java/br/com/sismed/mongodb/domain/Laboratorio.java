package br.com.sismed.mongodb.domain;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="sismed_laboratorio")
public class Laboratorio extends AbstractEntity{

	private static final long serialVersionUID = 1L;

	private String cnpj;
	
	private String nome;
	
	private String responsavel;
	
	private String telefone_fixo;
	
	private String email;
	
	private Endereco endereco;
	
    List<String> tipo_convenio;
	
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getTelefone_fixo() {
		return telefone_fixo;
	}

	public void setTelefone_fixo(String telefone_fixo) {
		this.telefone_fixo = telefone_fixo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<String> getTipo_convenio() {
		return tipo_convenio;
	}

	public void setTipo_convenio(List<String> tipo_convenio) {
		this.tipo_convenio = tipo_convenio;
	}
	
	
	
}
