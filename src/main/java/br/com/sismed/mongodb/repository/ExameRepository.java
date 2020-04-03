package br.com.sismed.mongodb.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Exame;

@Repository
public interface ExameRepository extends MongoRepository<Exame, String>{

	List<Exame> findByPaciente(String paciente_id);

	@Query("{ 'nome' : { $regex : '?0' , $options: 'i'}}")
	List<Exame> findByNome(String exame);

	@Query("{ 'data_coleta' : ?0}")
	List<Exame> findByDataColeta(LocalDate data_coleta);

	List<Exame> findByNomeAndPaciente(String exame, String paciente_id);

	

}
