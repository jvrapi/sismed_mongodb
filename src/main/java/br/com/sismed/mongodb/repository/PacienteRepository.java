package br.com.sismed.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Paciente;

@Repository
public interface PacienteRepository extends MongoRepository<Paciente, String> {

	Paciente findTopByOrderByIdDesc();

	List<Paciente> findByProntuario(Long dado);

	@Query("{ 'nome' : { $regex : '?0' , $options: 'i'}}")
	List<Paciente> findByNomeRegex(String dado);

	@Query("{ 'cpf' : { $regex : '?0' , $options: 'i'}}")
	List<Paciente> findByCpfRegex(String dado);

	@Query("{ 'telefone_fixo' : { $regex : '?0' , $options: 'i'}}")
	List<Paciente> findByTelefone_fixo(String dado);

	@Query("{ 'celular' : { $regex : '?0' , $options: 'i'}}")
	List<Paciente> findByCelularRegex(String dado);

}
