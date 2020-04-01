package br.com.sismed.mongodb.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class ListRegistros extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Long paciente_prontuario;
	private String paciente_id;
	private String paciente_nome;
	private LocalDate data;
	private LocalTime hora;
	private Long total_registros;
	private String descricao;

	public Long getPaciente_prontuario() {
		return paciente_prontuario;
	}

	public void setPaciente_prontuario(Long paciente_prontuario) {
		this.paciente_prontuario = paciente_prontuario;
	}

	public String getPaciente_id() {
		return paciente_id;
	}

	public void setPaciente_id(String paciente_id) {
		this.paciente_id = paciente_id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public Long getTotal_registros() {
		return total_registros;
	}

	public void setTotal_registros(Long total_registros) {
		this.total_registros = total_registros;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getPaciente_nome() {
		return paciente_nome;
	}

	public void setPaciente_nome(String paciente_nome) {
		this.paciente_nome = paciente_nome;
	}

	
}
