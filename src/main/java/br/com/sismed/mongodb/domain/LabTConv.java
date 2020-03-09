package br.com.sismed.mongodb.domain;



import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "sismed_laboratorio_tconvenio")
public class LabTConv extends AbstractEntity{

	private static final long serialVersionUID = 1L;


	private TConvenio tconvenio_id;
	
	
	private Laboratorio laboratorio_id;

	public TConvenio getTconvenio_id() {
		return tconvenio_id;
	}

	public void setTconvenio_id(TConvenio tconvenio_id) {
		this.tconvenio_id = tconvenio_id;
	}

	public Laboratorio getLaboratorio_id() {
		return laboratorio_id;
	}

	public void setLaboratorio_id(Laboratorio laboratorio_id) {
		this.laboratorio_id = laboratorio_id;
	}

	
}
