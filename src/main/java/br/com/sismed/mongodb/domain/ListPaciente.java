package br.com.sismed.mongodb.domain;

public class ListPaciente extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private Long paciente_prontuario;
	private String paciente_nome;
	private String paciente_cpf;
	private String paciente_rg;
	private String paciente_convenio;
	private String paciente_tipo_convenio;
	private String paciente_telefone;
	private String paciente_celular;

	public Long getPaciente_prontuario() {
		return paciente_prontuario;
	}

	public void setPaciente_prontuario(Long paciente_prontuario) {
		this.paciente_prontuario = paciente_prontuario;
	}

	public String getPaciente_nome() {
		return paciente_nome;
	}

	public void setPaciente_nome(String paciente_nome) {
		this.paciente_nome = paciente_nome;
	}

	public String getPaciente_cpf() {
		return paciente_cpf;
	}

	public void setPaciente_cpf(String paciente_cpf) {
		this.paciente_cpf = paciente_cpf;
	}

	public String getPaciente_rg() {
		return paciente_rg;
	}

	public void setPaciente_rg(String paciente_rg) {
		this.paciente_rg = paciente_rg;
	}

	public String getPaciente_convenio() {
		return paciente_convenio;
	}

	public void setPaciente_convenio(String paciente_convenio) {
		this.paciente_convenio = paciente_convenio;
	}

	public String getPaciente_tipo_convenio() {
		return paciente_tipo_convenio;
	}

	public void setPaciente_tipo_convenio(String paciente_tipo_convenio) {
		this.paciente_tipo_convenio = paciente_tipo_convenio;
	}

	public String getPaciente_telefone() {
		return paciente_telefone;
	}

	public void setPaciente_telefone(String paciente_telefone) {
		this.paciente_telefone = paciente_telefone;
	}

	public String getPaciente_celular() {
		return paciente_celular;
	}

	public void setPaciente_celular(String paciente_celular) {
		this.paciente_celular = paciente_celular;
	}

}
