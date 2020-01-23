package br.com.sismed.mongodb.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection="sismed_custos")
public class Custos extends AbstractEntity{

	private static final long serialVersionUID = 1L;

	private Agenda agendamento;
	
	
	private Paciente paciente;
	
	
	private Convenio convenio;
	
	
	private Procedimento procedimento;
	
	
	private Funcionario funcionario;
	
	private LocalDate data ;
	
	private LocalTime hora;
	

	private BigDecimal valor;

	public Agenda getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agenda agendamento) {
		this.agendamento = agendamento;
	}

	

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public Procedimento getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(Procedimento procedimento) {
		this.procedimento = procedimento;
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


	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	
	
	
	
}
