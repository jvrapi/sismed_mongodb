package br.com.sismed.mongodb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sismed.mongodb.repository.AgendaRepository;

@Service
public class AgendaService {

	@Autowired
	private AgendaRepository repository;
}
