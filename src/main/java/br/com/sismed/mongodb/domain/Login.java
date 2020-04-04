package br.com.sismed.mongodb.domain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Login {

	private String senha;
	private String codigo;

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = new BCryptPasswordEncoder().encode(senha);
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	

}
