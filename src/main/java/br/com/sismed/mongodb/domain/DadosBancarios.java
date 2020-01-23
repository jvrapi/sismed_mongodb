package br.com.sismed.mongodb.domain;



import org.springframework.data.mongodb.core.mapping.Document;




@Document(collection = "sismed_dados_bancarios")
public class DadosBancarios extends AbstractEntity{

	private static final long serialVersionUID = 1L;


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
