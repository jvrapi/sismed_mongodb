package br.com.sismed.mongodb.domain;



import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sismed_funcionario_tconvenio")
public class FuncTConv extends AbstractEntity{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Funcionario funcionario;
	
	
	private TConvenio tconvenio;
	
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public TConvenio getTconvenio() {
		return tconvenio;
	}
	public void setTconvenio(TConvenio tconvenio) {
		this.tconvenio = tconvenio;
	}
	

}
