package br.com.sismed.mongodb.domain;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document(collection="sismed_exame")
public class Exame extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private String nome;

	private String descricao;

	private String valor;

	private String funcionario_lab;

	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data_coleta;

	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data_envio;

	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data_retorno;

	private String paciente_id;

	private String funcionario_id;

	private String tipo;

	private String laboratorio_id;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getData_coleta() {
		return data_coleta;
	}

	public void setData_coleta(LocalDate data_coleta) {
		this.data_coleta = data_coleta;
	}

	public LocalDate getData_envio() {
		return data_envio;
	}

	public void setData_envio(LocalDate data_envio) {
		this.data_envio = data_envio;
	}

	public LocalDate getData_retorno() {
		return data_retorno;
	}

	public void setData_retorno(LocalDate data_retorno) {
		this.data_retorno = data_retorno;
	}

	public String getPaciente_id() {
		return paciente_id;
	}

	public void setPaciente_id(String paciente_id) {
		this.paciente_id = paciente_id;
	}

	public String getFuncionario_id() {
		return funcionario_id;
	}

	public void setFuncionario_id(String funcionario_id) {
		this.funcionario_id = funcionario_id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFuncionario_lab() {
		return funcionario_lab;
	}

	public void setFuncionario_lab(String funcionario_lab) {
		this.funcionario_lab = funcionario_lab;
	}

	public String getLaboratorio_id() {
		return laboratorio_id;
	}

	public void setLaboratorio_id(String laboratorio_id) {
		this.laboratorio_id = laboratorio_id;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
