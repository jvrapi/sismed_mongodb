package br.com.sismed.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Custos;

@Repository
public interface CustosRepository extends MongoRepository<Custos, String>{

	List<Custos> findByPaciente_id(String paciente);

	List<Custos> findByConvenio_id(String convenio);

}
