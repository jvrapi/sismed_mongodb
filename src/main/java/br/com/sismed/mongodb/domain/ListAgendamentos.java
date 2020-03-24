package br.com.sismed.mongodb.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class ListAgendamentos extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Long paciente_matricula;
	private String paciente_id;
	private String paciente_nome;
	private String paciente_celular;
	private String paciente_telefone;
	private LocalDate paciente_nascimento;
	private LocalDate agendamento_data;
	private LocalTime agendamento_hora;
	private String agendamento_convenio;
	private String agendamento_observacao;
	private Long primeira_vez;
	private Long compareceu;
	private Long pagou;
	private String medico_nome;
	private String medico_especialidade;

	public Long getPaciente_matricula() {
		return paciente_matricula;
	}

	public void setPaciente_matricula(Long paciente_matricula) {
		this.paciente_matricula = paciente_matricula;
	}

	public String getPaciente_id() {
		return paciente_id;
	}

	public void setPaciente_id(String paciente_id) {
		this.paciente_id = paciente_id;
	}

	public String getPaciente_nome() {
		return paciente_nome;
	}

	public void setPaciente_nome(String paciente_nome) {
		this.paciente_nome = paciente_nome;
	}

	public String getPaciente_celular() {
		return paciente_celular;
	}

	public void setPaciente_celular(String paciente_celular) {
		this.paciente_celular = paciente_celular;
	}

	public String getPaciente_telefone() {
		return paciente_telefone;
	}

	public void setPaciente_telefone(String paciente_telefone) {
		this.paciente_telefone = paciente_telefone;
	}

	public LocalDate getPaciente_nascimento() {
		return paciente_nascimento;
	}

	public void setPaciente_nascimento(LocalDate paciente_nascimento) {
		this.paciente_nascimento = paciente_nascimento;
	}

	public LocalDate getAgendamento_data() {
		return agendamento_data;
	}

	public void setAgendamento_data(LocalDate agendamento_data) {
		this.agendamento_data = agendamento_data;
	}

	public LocalTime getAgendamento_hora() {
		return agendamento_hora;
	}

	public void setAgendamento_hora(LocalTime agendamento_hora) {
		this.agendamento_hora = agendamento_hora;
	}

	public String getAgendamento_convenio() {
		return agendamento_convenio;
	}

	public void setAgendamento_convenio(String agendamento_convenio) {
		this.agendamento_convenio = agendamento_convenio;
	}

	public String getAgendamento_observacao() {
		return agendamento_observacao;
	}

	public void setAgendamento_observacao(String agendamento_observacao) {
		this.agendamento_observacao = agendamento_observacao;
	}

	public Long getPrimeira_vez() {
		return primeira_vez;
	}

	public void setPrimeira_vez(Long primeira_vez) {
		this.primeira_vez = primeira_vez;
	}

	public Long getCompareceu() {
		return compareceu;
	}

	public void setCompareceu(Long compareceu) {
		this.compareceu = compareceu;
	}

	public Long getPagou() {
		return pagou;
	}

	public void setPagou(Long pagou) {
		this.pagou = pagou;
	}

	public String getMedico_nome() {
		return medico_nome;
	}

	public void setMedico_nome(String medico_nome) {
		this.medico_nome = medico_nome;
	}

	public String getMedico_especialidade() {
		return medico_especialidade;
	}

	public void setMedico_especialidade(String medico_especialidade) {
		this.medico_especialidade = medico_especialidade;
	}

	public Boolean compararDatas(LocalDate agendamento) {

		LocalDate dataAtual = LocalDate.now();

		return agendamento.isBefore(dataAtual);

	}
	
	public int calcularIdade(LocalDate nascimento) {
	
		LocalDate dataAtual = LocalDate.now();

		// Dados da data atual
		int anoAtual = dataAtual.getYear();
		int mesAtual = dataAtual.getMonthValue();
		int diaAtual = dataAtual.getDayOfWeek().ordinal();

		// Dados do paciente
		int anoPaciente = nascimento.getYear();
		int mesPaciente = nascimento.getMonthValue();
		int diaPaciente = nascimento.getDayOfWeek().ordinal();

		int idade = anoAtual - anoPaciente;

		if (mesAtual < mesPaciente) {
			idade--;
		} else if (mesAtual == mesPaciente) {
			if (diaAtual < diaPaciente) {
				idade--;
			}
		}

		return idade;

	}
}
