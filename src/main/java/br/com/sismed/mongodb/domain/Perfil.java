package br.com.sismed.mongodb.domain;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "sismed_perfil")
public class Perfil extends AbstractEntity {

	
	private static final long serialVersionUID = 1L;
	private String descricao;
	
	public Perfil() {
		super();
	}

	
	public Perfil(String id) {
		super.setId(id);
	}

	public String getDesc() {
		return descricao;
	}

	public void setDesc(String descricao) {
		this.descricao = descricao;
	}
	
	
}
