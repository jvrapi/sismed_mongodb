package br.com.sismed.mongodb.domain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Login {

	private String senha;
	private String codigo_verificar;

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = new BCryptPasswordEncoder().encode(senha);
	}

	public String getCodigo_verificar() {
		return codigo_verificar;
	}

	public void setCodigo_verificar(String codigo_verificar) {
		this.codigo_verificar = codigo_verificar;
	}

}
