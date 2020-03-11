package br.com.sismed.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.sismed.mongodb.domain.Funcionario;

@Repository
public interface FuncionarioRepository extends MongoRepository<Funcionario, String>{
	
	@Query (value = "{ 'id':?0 }")
	public List<Funcionario> ListarFuncionarioId(Long id);
	
	@Query ("{ 'nome':?0 }")
	public List<Funcionario> ListarFuncionarioNome(String nome);
	
	@Query ("{ 'cpf':?0 }")
	public List<Funcionario> ListarFuncionarioCPF(String cpf);
	
	@Query ("{ 'celular':?0 }")
	public List<Funcionario> ListarFuncionarioCelular(String celular);
	
	@Query ("{ 'especialidade':?0 }")
	public List<Funcionario> ListarFuncionarioEspecialidade(String especialidade);
	
	@Query (value="{ 'crm':?0 }")
	public List<Funcionario> ListarFuncionarioCRM(String crm);
	
	Funcionario findTopByOrderByIdDesc();
	
}
