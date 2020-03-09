package br.com.sismed.mongodb.domain;




import java.io.Serializable;

import org.springframework.data.annotation.Id;


/* Super classe Abstrata das classes entidades*/


public abstract class AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
