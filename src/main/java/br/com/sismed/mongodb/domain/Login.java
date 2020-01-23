package br.com.sismed.mongodb.domain;



import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="sismed_login")
public class Login extends AbstractEntity{

	private static final long serialVersionUID = 1L;

	private String senha;
	
	private String cpf;
	
	private Perfil perfis;
	
	private Funcionario funcionario_id;
	
	private String codigoVerificador;
	
	public Login() {
		super();
	}

	public Login(Long id) {
		super.setId(id);
	}
	
	public Login(String cpf) {
		this.cpf = cpf;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Perfil getPerfis() {
		return perfis;
	}

	public void setPerfis(Perfil perfis) {
		this.perfis = perfis;
	}

	public Funcionario getFuncionario_id() {
		return funcionario_id;
	}

	public void setFuncionario_id(Funcionario funcionario_id) {
		this.funcionario_id = funcionario_id;
	}

	public String getCodigoVerificador() {
		return codigoVerificador;
	}

	public void setCodigoVerificador(String codigoVerificador) {
		this.codigoVerificador = codigoVerificador;
	}

	

}
