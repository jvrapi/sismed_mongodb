package br.com.sismed.mongodb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.RegistroClinico;

@Repository
public interface RClinicoRepository extends MongoRepository<RegistroClinico, String>{

	public Page<RegistroClinico> findByPaciente_id(String pacienteId, PageRequest pagerequest);
	
	RegistroClinico findTopByOrderByIdDesc();
}
