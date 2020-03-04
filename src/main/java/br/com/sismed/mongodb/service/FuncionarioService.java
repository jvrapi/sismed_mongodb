package br.com.sismed.mongodb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository repository;
	
	public List<Funcionario> buscarTodos() {
		return repository.findAll();
	}

	public Page<Funcionario> buscarTodos(PageRequest pagerequest) {
		
		return null;
	}

	public Funcionario salvar(Funcionario funcionario) {
		
		return repository.save(funcionario);
	}

}
