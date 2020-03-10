package br.com.sismed.mongodb.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.repository.ConvenioRepository;


@Service
public class ConvenioService {

	@Autowired
	private ConvenioRepository repository;

	

	@Transactional(readOnly = true)
	public List<Convenio> buscarTodos() {
		return repository.findAll();
	}

	@Transactional(readOnly = false)
	public void salvar(Convenio convenio) {
		repository.save(convenio);

	}

	@Transactional(readOnly = true)
	public Convenio lastRecord() {
		return repository.findTopByOrderByIdDesc();
	}

	@Transactional(readOnly = true)
	public Optional<Convenio> buscarPorId(String id) {
		return repository.findById(id);
	}

	@Transactional(readOnly = false)
	public void excluir(String id) {
		repository.deleteById(id);
	}

	

}
