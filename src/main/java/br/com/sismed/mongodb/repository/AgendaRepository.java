package br.com.sismed.mongodb.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Agenda;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, String>{

	
	List<Agenda> findByFuncionario_idAndDataOrderByHora(String id, LocalDate data);

	Agenda findTopByPaciente_idOrderByIdDesc(String paciente_id);

}
