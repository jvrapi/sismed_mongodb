package br.com.sismed.mongodb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.repository.ConvenioRepository;

@Service
public class ConvenioService {

	@Autowired
	private ConvenioRepository repository;
	
	public List<Convenio> findAll(){
		return repository.findAll();
	}
}
