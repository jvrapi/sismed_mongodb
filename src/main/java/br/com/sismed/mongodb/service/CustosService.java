package br.com.sismed.mongodb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Custos;
import br.com.sismed.mongodb.repository.CustosRepository;

@Service
public class CustosService {

	@Autowired
	private CustosRepository repository;
	
	
	@Transactional(readOnly=false)
	public void salvar(Custos custos) {
		repository.save(custos);
	}
}
