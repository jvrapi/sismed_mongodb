package br.com.sismed.mongodb.service;

import java.util.List;

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
		repository.save(log);
	}
}
