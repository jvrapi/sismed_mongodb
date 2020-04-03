package br.com.sismed.mongodb.domain;

import java.time.LocalDate;

public class ListExame extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private String exame_nome;
	private String exame_tipo;
	private Paciente paciente;
	private LocalDate data_coleta;
	private LocalDate data_envio;
	private LocalDate data_retorno;

	public String getExame_nome() {
		return exame_nome;
	}

	public void setExame_nome(String exame_nome) {
		this.exame_nome = exame_nome;
	}

	public String getExame_tipo() {
		return exame_tipo;
	}

	public void setExame_tipo(String exame_tipo) {
		this.exame_tipo = exame_tipo;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
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

}
