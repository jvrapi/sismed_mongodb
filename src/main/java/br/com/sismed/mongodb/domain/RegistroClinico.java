package br.com.sismed.mongodb.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document(collection = "sismed_registro_clinico")
public class RegistroClinico extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;

	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data;
	
	private LocalTime hora;
	
	private String descricao;
	
	private Paciente paciente_id;
	
	private Funcionario funcionario_id;
	
	private Agenda agendamento_id;
	
	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Agenda getAgendamento_id() {
		return agendamento_id;
	}

	public void setAgendamento_id(Agenda agendamento_id) {
		this.agendamento_id = agendamento_id;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Funcionario getFuncionario_id() {
		return funcionario_id;
	}

	public void setFuncionario_id(Funcionario funcionario_id) {
		this.funcionario_id = funcionario_id;
	}

	public Paciente getPaciente_id() {
		return paciente_id;
	}

	public void setPaciente_id(Paciente paciente_id) {
		this.paciente_id = paciente_id;
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
