package br.com.sismed.mongodb.domain;


public class DadosBancarios  extends AbstractEntity{
	

	private String banco;
	
	
	
	private String agencia;
	
	private String conta;
	
	
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
}
