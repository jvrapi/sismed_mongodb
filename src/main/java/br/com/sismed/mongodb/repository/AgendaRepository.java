package br.com.sismed.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Agenda;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, String>{

}
