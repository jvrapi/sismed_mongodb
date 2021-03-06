package br.com.sismed.mongodb.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	@Transactional(readOnly = true)
	public Page<Convenio> buscarTodosComPaginacao(Pageable pageable) {
		return repository.findAll(pageable);
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

	public List<Convenio> ListarPorNome(String term) {
		return repository.findByNome(term);
	}
	
	public List<Convenio> listarPorNomeRegex(String term) {
		return repository.findByNomeRegex(term);
	}

	public List<Convenio> ListarPorCNPJRegex(String term) {
		return repository.findByCnpjRegex(term);
	}

	public List<Convenio> ListarPorANSRegex(String term) {
		return repository.findByAnsRegex(term);
	}

	

}
