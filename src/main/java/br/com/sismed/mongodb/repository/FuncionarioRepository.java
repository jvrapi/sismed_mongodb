package br.com.sismed.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Funcionario;

@Repository
public interface FuncionarioRepository extends MongoRepository<Funcionario, String> {
	
	Funcionario findTopByOrderByIdDesc();
	
	Funcionario findBycpf(String cpf);
	
	@Query(value="{ 'crm':{ $ne: null } }")
	List<Funcionario> listarPorCrm();
	
	@Query(value ="{$update: {'_id': ?0}, {$pull: {'tconvenio': { '_id': ?1} } } }")
	public void apagarTConv(String funcId, String tconvId);

	@Query("{ 'nome' : { '$regex' : ?0 , $options: 'i'}}")
	public List<Funcionario> findByNome(String dado);

	public Funcionario findByMatricula(Long dado);

	@Query("{ 'cpf': { $regex: '?0', $options: 'i' } }")
	public List<Funcionario> findByCpfRegex(String dado);

	@Query("{ 'crm': { $regex: '?0', $options: 'i' } }")
	public List<Funcionario> findByCrmRegex(String dado);

	@Query("{ 'especialidade': { $regex: '?0', $options: 'i' } }")
	public List<Funcionario> findByEspecialidadeRegex(String dado);
	
}
