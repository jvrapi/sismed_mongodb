package br.com.sismed.mongodb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.RegistroClinico;
import br.com.sismed.mongodb.repository.RClinicoRepository;

@Service
public class RClinicoService {

	@Autowired
	private RClinicoRepository rcRepository;

	@Transactional(readOnly = false)
	public List<RegistroClinico> listar() {
		return rcRepository.findAll();
	}

	@Transactional(readOnly = true)
	public void salvar(RegistroClinico registroclinico) {
		rcRepository.save(registroclinico);
	}

	@Transactional(readOnly = true)
	public Optional<RegistroClinico> buscarPorId(String id) {
		return rcRepository.findById(id);
	}

	@Transactional(readOnly = false)
	public void excluir(String id) {
		rcRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Page<RegistroClinico> listarRegistros(String pacienteId, PageRequest pagerequest) {
		return rcRepository.findByPaciente(pacienteId, pagerequest);
	}
	
	@Transactional(readOnly = true)
	public RegistroClinico ultimoRegistro() {
		return rcRepository.findTopByOrderByIdDesc();
	}

}
