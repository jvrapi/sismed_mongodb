package br.com.sismed.mongodb.domain;


public class LabelValue {
	private String convenio;
	private String tipo;
	private String value;
	private String label;
	private String nome_tipo;
	private String nome_convenio;
	private String value2;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getConvenio() {
		return convenio;
	}
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNome_tipo() {
		return nome_tipo;
	}
	public void setNome_tipo(String nome_tipo) {
		this.nome_tipo = nome_tipo;
	}
	public String getNome_convenio() {
		return nome_convenio;
	}
	public void setNome_convenio(String nome_convenio) {
		this.nome_convenio = nome_convenio;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	
}
