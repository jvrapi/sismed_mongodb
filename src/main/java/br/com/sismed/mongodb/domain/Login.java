package br.com.sismed.mongodb.domain;

public class Login {

	private String senha;
	private String codigo_verificar;

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCodigo_verificar() {
		return codigo_verificar;
	}

	public void setCodigo_verificar(String codigo_verificar) {
		this.codigo_verificar = codigo_verificar;
	}

}
