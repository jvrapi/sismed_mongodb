package br.com.sismed.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.RegistroClinico;

@Repository
public interface RClinicoRepository extends MongoRepository<RegistroClinico, String>{

}
