package br.com.sismed.mongodb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.repository.TConvenioRepository;

@Service
public class TConvenioService {

	@Autowired
	private TConvenioRepository repository;
	
	
	@Transactional(readOnly=true)
	public List<TConvenio> listarTodos(){
		return repository.findAll();
	}
	
	@Transactional(readOnly=false)
	public void salvar(TConvenio tconvenio) {
		repository.save(tconvenio);
	}
}
