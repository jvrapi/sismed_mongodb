package br.com.sismed.mongodb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.TConvenio;

import br.com.sismed.mongodb.repository.TConvenioRepository;

@Service
public class TConvenioService {

	@Autowired
	private TConvenioRepository repository;

	@Transactional(readOnly = true)
	public List<TConvenio> listarTodos(String id) {
		return repository.findByConvenio(id);
	}

	@Transactional(readOnly = true)
	public Optional<TConvenio> buscarPorId(String id) {
		return repository.findById(id);
	}

	@Transactional(readOnly = false)
	public void salvar(TConvenio tconvenio) {
		repository.save(tconvenio);
	}

	@Transactional(readOnly = false)
	public void excluir(String id) {
		repository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public TConvenio buscarPorNome(String nome) {
		return repository.findByNome(nome);
	}

}
