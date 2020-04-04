package br.com.sismed.mongodb.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.TConvenio;

@Repository
public interface TConvenioRepository extends MongoRepository<TConvenio,String>{

	
	public List<TConvenio> findByConvenio(String id);
	
	@Query("{ 'convenio' : ?0}")
	public Page<TConvenio> listarTodosComPagincacao(String id, Pageable pegeable);
	
	@Query("{ 'nome' : { '$regex' : ?0 , $options: 'i'}}")
	public TConvenio findByNome(String nome);
}
