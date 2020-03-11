package br.com.sismed.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Procedimento;

@Repository
public interface ProcedimentoRepository extends MongoRepository<Procedimento, String>{

	public List<Procedimento> findByConvenio(String id);
}
