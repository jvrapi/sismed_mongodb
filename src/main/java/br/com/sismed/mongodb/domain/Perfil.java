package br.com.sismed.mongodb.domain;



import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "sismed_perfil")
public class Perfil extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	private String desc;
	
	public Perfil() {
		super();
	}

	
	public Perfil(Long id) {
		super.setId(id);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
