package br.com.sismed.mongodb.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.domain.TConvenio;

@Repository
public interface ConvenioRepository extends MongoRepository<Convenio, String>{

	 Convenio findTopByOrderByIdDesc();

	 
	
}
