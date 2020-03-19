package br.com.sismed.mongodb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	
	public Optional<RegistroClinico> buscarPorId(String id) {
		return rcRepository.findById(id);
	}
	
	public void excluir(String id) {
		rcRepository.deleteById(id);
	}
	
	public Page<RegistroClinico> listarRegistros(String pacienteId, PageRequest pagerequest) {
		return rcRepository.findByPaciente_id(pacienteId, pagerequest);
	}

}
