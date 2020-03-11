package br.com.sismed.mongodb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.repository.PacienteRepository;

@Service
public class PacienteService {

	@Autowired
	private PacienteRepository pRepository;

	@Transactional(readOnly = true)
	public List<Paciente> buscarTodos() {
		return pRepository.findAll();
	}
	
	public void salvar(Paciente paciente) {
		Paciente lastPaciente = lastPaciente();
		if(lastPaciente != null) {
			Long matricula = lastPaciente.getMatricula() + 1;
			paciente.setMatricula(matricula);
		}
		else {
			paciente.setMatricula(1L);
		}
		pRepository.save(paciente);
	}
	
	@Transactional(readOnly = true)
	public Paciente lastPaciente() {
		return pRepository.findTopByOrderByIdDesc();
	}
	
	@Transactional(readOnly = true)
	public Optional<Paciente> buscarPorId(String id) {
		return pRepository.findById(id);
	}
	
	public void excluir(String id) {
		pRepository.deleteById(id);
	}
}
