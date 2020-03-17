package br.com.sismed.mongodb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.repository.FuncionarioRepository;

@Service
public class FuncionarioService implements UserDetailsService{

	@Autowired
	private FuncionarioRepository repository;
	
	public List<Funcionario> buscarTodos() {
		return repository.findAll();
	}
	
	public List<Funcionario> buscarMedicos(){
		return repository.listarPorCrm();
	}
	
	//Convenios aceitos por cada funcionário
	//Duas listas: tipo de convenio aceito por cada funcionario e convenios aceitos por cada funcionario
	
	public List<Funcionario> mostrarTipoConvenios(){
		return repository.findAll();
	}
	
	public List<Funcionario> mostrarConvenios(){
		return repository.findAll();
	}

	public Page<Funcionario> buscarTodos(PageRequest pagerequest) {
		
		return null;
	}

	public Funcionario salvar(Funcionario funcionario) {
		
		return repository.save(funcionario);
	}
	
	public Funcionario ultimoRegistro() {
		return repository.findTopByOrderByIdDesc();
		
	}

	public Optional<Funcionario> buscarporId(String id) {
		return repository.findById(id);
	}

	public Funcionario buscarPorCpf(String cpf) {
		return repository.findBycpf(cpf);
	}
	
	public void excluir(String id) {
		repository.deleteById(id);
	}

	@Override @Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Funcionario funcionario = buscarPorCpf(username);
		
		return new User(
				funcionario.getCpf(),
				funcionario.getLogin().getSenha(),
				AuthorityUtils.createAuthorityList(funcionario.getPerfil().getDesc())
				
			);
	}

	public void apagarTConv(String funcId, String tconvId) {
		repository.apagarTConv(funcId, tconvId);
	}
	
}
