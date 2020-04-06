package br.com.sismed.mongodb.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Convenio;

@Repository
public interface ConvenioRepository extends MongoRepository<Convenio, String>{

	 Convenio findTopByOrderByIdDesc();

	List<Convenio> findByNome(String term);

	@Query("{ 'nome': { $regex: '?0', $options: 'i' } }")
	public List<Convenio> findByNomeRegex(String term);

	@Query("{ 'cnpj': { $regex: '?0', $options: 'i' } }")
	public List<Convenio> findByCnpjRegex(String term);

	@Query("{ 'registro_ans': { $regex: '?0', $options: 'i' } }")
	public List<Convenio> findByAnsRegex(String term);

	 
	
}
