package br.com.sismed.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Laboratorio;

@Repository
public interface LaboratorioRepository extends MongoRepository<Laboratorio, String>{

	@Query("{ 'nome' : { $regex : '?0' , $options: 'i'}}")
	List<Laboratorio> findByNome(String nome);
	
	@Query("{ 'endereco.bairro' : { $regex : '?0' , $options: 'i'}}")
	List<Laboratorio> findByBairro(String nome);
	
	@Query("{ 'telefone_fixo' : { $regex : '?0' , $options: 'i'}}")
	List<Laboratorio> findByTelefone(String nome);

	@Query("{'tipo_convenio' : ?0 }")
	List<Laboratorio> listarLaboratorioPorTipoConvenio(String id);
}
