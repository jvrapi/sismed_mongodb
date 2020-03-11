package br.com.sismed.mongodb.domain;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document(collection = "sismed_paciente")
public class Paciente extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;
	
	private Long matricula;
	
	private String nome;
	
	private String cpf;
	
	private String rg;
	
	private String orgao_emissor;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data_emissao;
	
	private String telefone_fixo;
	
	private String telefone_trabalho;
	
	private String celular;
	
	private String sexo;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data_nascimento;
	
	private String email;
	
	private String estado_civil;
	
	private String escolaridade;
	
	private String profissao;
	
	private String recomendacao;
	
	private String naturalidade;
	
	private String nacionalidade;
	
	private String situacao;

	private String carteira_convenio;

	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate validade;
	
	private TConvenio tipo_convenio;
	
	private Endereco endereco;
	
	private Convenio convenio;

	public Long getMatricula() {
		return matricula;
	}

	public void setMatricula(Long matricula) {
		this.matricula = matricula;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getOrgao_emissor() {
		return orgao_emissor;
	}

	public void setOrgao_emissor(String orgao_emissor) {
		this.orgao_emissor = orgao_emissor;
	}

	public LocalDate getData_emissao() {
		return data_emissao;
	}

	public void setData_emissao(LocalDate data_emissao) {
		this.data_emissao = data_emissao;
	}

	public String getTelefone_fixo() {
		return telefone_fixo;
	}

	public void setTelefone_fixo(String telefone_fixo) {
		this.telefone_fixo = telefone_fixo;
	}

	public String getTelefone_trabalho() {
		return telefone_trabalho;
	}

	public void setTelefone_trabalho(String telefone_trabalho) {
		this.telefone_trabalho = telefone_trabalho;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public LocalDate getData_nascimento() {
		return data_nascimento;
	}

	public void setData_nascimento(LocalDate data_nascimento) {
		this.data_nascimento = data_nascimento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado_civil() {
		return estado_civil;
	}

	public void setEstado_civil(String estado_civil) {
		this.estado_civil = estado_civil;
	}

	public String getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public String getRecomendacao() {
		return recomendacao;
	}

	public void setRecomendacao(String recomendacao) {
		this.recomendacao = recomendacao;
	}

	public String getNaturalidade() {
		return naturalidade;
	}

	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getCarteira_convenio() {
		return carteira_convenio;
	}

	public void setCarteira_convenio(String carteira_convenio) {
		this.carteira_convenio = carteira_convenio;
	}

	public LocalDate getValidade() {
		return validade;
	}

	public void setValidade(LocalDate validade) {
		this.validade = validade;
	}

	public TConvenio getTipo_convenio() {
		return tipo_convenio;
	}

	public void setTipo_convenio(TConvenio tipo_convenio) {
		this.tipo_convenio = tipo_convenio;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

}
