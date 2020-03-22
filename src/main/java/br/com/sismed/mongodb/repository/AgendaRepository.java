package br.com.sismed.mongodb.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Agenda;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, String>{

	
	List<Agenda> findByFuncionarioAndDataOrderByHora(String id, LocalDate data);

	Agenda findTopByPacienteOrderByIdDesc(String paciente_id);

	List<Agenda> findByData(LocalDate now);

	Page<Agenda> findByPaciente(String id, PageRequest pagerequest);


}
