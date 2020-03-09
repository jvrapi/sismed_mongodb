package br.com.sismed.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.TConvenio;

@Repository
public interface TConvenioRepository extends MongoRepository<TConvenio,String>{

}
