package br.com.sismed.mongodb.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Log;
import br.com.sismed.mongodb.repository.LogRepository;

@Service
public class LogService {

	@Autowired
	private LogRepository repository;
	
	
	@Transactional(readOnly = true)
	public Page<Log> listarTodos(Pageable pageable){
		return repository.findAll(pageable);
	}
	
	@Transactional(readOnly=false)
	public void salvar(Log log) {
		Integer totalLog = repository.findAll().size();
		if(totalLog == 50) {
			Log primeiro_documento = primeiro();
			repository.deleteById(primeiro_documento.getId());
		}
		repository.save(log);
	}
	
	@Transactional(readOnly = true)
	public Log primeiro() {
		return repository.findTopByOrderById();
	}
}
