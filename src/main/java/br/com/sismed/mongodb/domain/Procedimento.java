package br.com.sismed.mongodb.domain;


import java.math.BigDecimal;


import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "sismed_procedimento")
public  class Procedimento extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;


	private String descricao;
	
	
	private BigDecimal valor;
	
	private Convenio convenio;
	
	
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	
	
		
	
}
