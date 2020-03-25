package br.com.sismed.mongodb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Exame;
import br.com.sismed.mongodb.repository.ExameRepository;

@Service
public class ExameService {

	@Autowired
	private ExameRepository repository;

	@Transactional(readOnly = false)
	public void salvar(Exame exame) {
		repository.save(exame);

	}

	@Transactional(readOnly = true)
	public Page<Exame> buscarTodos(PageRequest pagerequest) {
		return repository.findAll(pagerequest);
	}

	@Transactional(readOnly = true)
	public Optional<Exame> buscarporId(String id) {
		return repository.findById(id);
	}

	@Transactional(readOnly = false)
	public void excluir(String id) {
		repository.deleteById(id);

	}
}
