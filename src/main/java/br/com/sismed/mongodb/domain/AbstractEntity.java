package br.com.sismed.mongodb.domain;




import org.springframework.data.annotation.Id;


/* Super classe Abstrata das classes entidades*/


public abstract class AbstractEntity {

	@Id
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
