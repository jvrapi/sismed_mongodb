package br.com.sismed.mongodb.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document(collection = "sismed_agenda")
public class Agenda extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Paciente paciente;

	private Procedimento procedimento;

	private Funcionario funcionario;

	private TConvenio tipo_convenio;

	private String observacao;

	private Long compareceu;

	private Long primeira_vez;

	private Long pagou;

	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data;

	private LocalTime hora;

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Procedimento getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(Procedimento procedimento) {
		this.procedimento = procedimento;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public TConvenio getTipo_convenio() {
		return tipo_convenio;
	}

	public void setTipo_convenio(TConvenio tipo_convenio) {
		this.tipo_convenio = tipo_convenio;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Long getCompareceu() {
		return compareceu;
	}

	public void setCompareceu(Long compareceu) {
		this.compareceu = compareceu;
	}

	public Long getPrimeira_vez() {
		return primeira_vez;
	}

	public void setPrimeira_vez(Long primeira_vez) {
		this.primeira_vez = primeira_vez;
	}

	public Long getPagou() {
		return pagou;
	}

	public void setPagou(Long pagou) {
		this.pagou = pagou;
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

	public Boolean compararDatas(LocalDate agendamento) {

		LocalDate dataAtual = LocalDate.now();

		return agendamento.isBefore(dataAtual);

	}
}
