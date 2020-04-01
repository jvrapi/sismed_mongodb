package br.com.sismed.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Log;

@Repository
public interface LogRepository extends MongoRepository<Log, String>{

	Log findTopByOrderById();
}
