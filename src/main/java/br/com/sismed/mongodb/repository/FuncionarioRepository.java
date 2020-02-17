package br.com.sismed.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Funcionario;

@Repository
public interface FuncionarioRepository extends MongoRepository<Funcionario, Long>{
	
	@Query ("{'id':?0}")
	List<Funcionario> ListarFuncionarioId(Long id);
	
	@Query ("{'nome'?0}")
	List<Funcionario> ListarFuncionarioNome(String nome);
	
	@Query ("{'cpf'?0}")
	List<Funcionario> ListarFuncionarioCPF(String cpf);
	
	@Query ("{'celular'?0}")
	List<Funcionario> ListarFuncionarioCelular(String celular);
	
	@Query ("{'especialidade'?0}")
	List<Funcionario> ListarFuncionarioEspecialidade(String especialidade);
	
	@Query ("{'especialidade'?0}")
	List<Funcionario> findAll();
	
}
