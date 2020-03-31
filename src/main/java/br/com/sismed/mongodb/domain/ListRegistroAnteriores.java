package br.com.sismed.mongodb.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class ListRegistroAnteriores extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Paciente paciente;
	private Agenda agendamento;
	private Funcionario funcionario;
	private String descricao;
	private LocalDate data;
	private LocalTime hora;
	private Long numero;

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Agenda getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agenda agendamento) {
		this.agendamento = agendamento;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}
	
	public int compararDatas(LocalDate rclinicoData) {
		LocalDate dataAtual = LocalDate.now();
		if(rclinicoData.isBefore(dataAtual)) {
			return 0;
		}
		else {
			return 1;
		}
	}

}
