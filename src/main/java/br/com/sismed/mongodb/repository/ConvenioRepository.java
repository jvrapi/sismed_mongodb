package br.com.sismed.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Convenio;

@Repository
public interface ConvenioRepository extends MongoRepository<Convenio, Long>{

	 Convenio findTopByOrderByIdDesc();
}
