package br.com.sismed.mongodb.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;



import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "sismed_custos")
public class Custos extends AbstractEntity{

	
	private static final long serialVersionUID = 1L;

	private String agendamento;
	
	
	private String paciente;
	
	
	private String convenio;
	
	
	private String procedimento;
	
	
	private String funcionario;
	
	private LocalDate data ;
	
	private LocalTime hora;
	

	private BigDecimal valor;


	public String getAgendamento() {
		return agendamento;
	}


	public void setAgendamento(String agendamento) {
		this.agendamento = agendamento;
	}


	public String getPaciente() {
		return paciente;
	}


	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}


	public String getConvenio() {
		return convenio;
	}


	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}


	public String getProcedimento() {
		return procedimento;
	}


	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}


	public String getFuncionario() {
		return funcionario;
	}


	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
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


	public BigDecimal getValor() {
		return valor;
	}


	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	
	
	
	
}
