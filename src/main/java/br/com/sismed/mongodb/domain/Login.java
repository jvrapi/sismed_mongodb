package br.com.sismed.mongodb.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="sismed_login")
public class Login {

	private String senha;
	private Perfil perfil;

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

}
