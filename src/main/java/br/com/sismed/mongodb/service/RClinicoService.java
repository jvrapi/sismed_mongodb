package br.com.sismed.mongodb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sismed.mongodb.domain.RegistroClinico;
import br.com.sismed.mongodb.repository.RClinicoRepository;

@Service
public class RClinicoService {
	
	@Autowired
	private RClinicoRepository rcRepository;
	
	public List<RegistroClinico> listar() {
		return rcRepository.findAll();
	}
	
	public void salvar(RegistroClinico registroclinico) {
		rcRepository.save(registroclinico);
	}

}
