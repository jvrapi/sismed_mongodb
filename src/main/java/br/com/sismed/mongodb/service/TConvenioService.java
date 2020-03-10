package br.com.sismed.mongodb.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.repository.TConvenioRepository;

@Service
public class TConvenioService {

	@Autowired
	private TConvenioRepository repository;
	
	@Autowired
	MongoOperations template;
	
	/*@Transactional(readOnly=true)
	public List<TConvenio> listarTodos(String id){
		return repository.findByConvenio_id(id);
	}*/
	
	@Transactional(readOnly=true)
	public Optional<TConvenio> buscarPorId(String id) {
		return repository.findById(id);
	}
	
	
	@Transactional(readOnly=false)
	public void excluir(String id) {
		repository.deleteById(id);
	}
	
	@Transactional(readOnly=false)
	public void salvar(TConvenio tconvenio, String id) {
		Convenio e = template.findOne(new Query(Criteria.where("id").is(id)), Convenio.class);
		if (e != null) {
			e.getTipos().add(tconvenio);
			template.save(e);
		}
		
		
	}
	
	
}
