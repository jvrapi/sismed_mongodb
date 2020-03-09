package br.com.sismed.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Paciente;

@Repository
public interface PacienteRepository extends MongoRepository<Paciente, String>{
	
	Paciente findTopByOrderByIdDesc();
}
