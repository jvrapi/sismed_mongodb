package br.com.sismed.mongodb.domain;

import java.time.LocalDate;



import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;




@Document(collection = "sismed_exame")
public class Exame extends AbstractEntity{

	private static final long serialVersionUID = 1L;

	private String nome;
	
	private String descricao;
	
	private String valor;
	
	private String funcionario_lab;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data_coleta;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data_envio;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate data_retorno;
	
	private Paciente paciente_id;
	
	private Funcionario funcionario_id;
	
	private TConvenio tipo;
	
	
	private Laboratorio laboratorio_id;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public Paciente getPaciente_id() {
		return paciente_id;
	}

	public void setPaciente_id(Paciente paciente_id) {
		this.paciente_id = paciente_id;
	}

	public Funcionario getFuncionario_id() {
		return funcionario_id;
	}

	public void setFuncionario_id(Funcionario funcionario_id) {
		this.funcionario_id = funcionario_id;
	}

	public TConvenio getTipo() {
		return tipo;
	}

	public void setTipo(TConvenio tipo) {
		this.tipo = tipo;
	}

	public String getFuncionario_lab() {
		return funcionario_lab;
	}

	public void setFuncionario_lab(String funcionario_lab) {
		this.funcionario_lab = funcionario_lab;
	}

	public Laboratorio getLaboratorio_id() {
		return laboratorio_id;
	}

	public void setLaboratorio_id(Laboratorio laboratorio_id) {
		this.laboratorio_id = laboratorio_id;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	
	
	/*public boolean VerificaDataColetaEnvio(){
		
		boolean data;
		
		if(getData_coleta().isBefore(getData_envio())) {
			
			data = true;
		}
		else {
			
			data = false;
		}

		return data;
		}
	
		public boolean VerificaDataEnvioRetorno(){
			
			boolean data;
			
			if(getData_envio().isAfter(getData_retorno())) {
				
				data = true;
			}
			else {
				
				data = false;
			}

			return data;
			}*/
	
	
}
