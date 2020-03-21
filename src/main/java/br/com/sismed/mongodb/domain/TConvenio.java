package br.com.sismed.mongodb.domain;

import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="sismed_tipo_convenio")
public class TConvenio extends AbstractEntity{

	private static final long serialVersionUID = 1L;


	private String nome;
	
	
	private String convenio;
	
	
    Set<LabTConv> tconv;
	
	
    Set<FuncTConv> tconvenio;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	
	
	
	
	
}
