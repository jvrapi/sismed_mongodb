package br.com.sismed.mongodb.domain;

public class Perfil {

	private static final long serialVersionUID = 1L;
	private String descricao;
	private Long id;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
