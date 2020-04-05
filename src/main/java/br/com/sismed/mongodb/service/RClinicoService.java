package br.com.sismed.mongodb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.RegistroClinico;
import br.com.sismed.mongodb.repository.RClinicoRepository;

@Service
public class RClinicoService {

	@Autowired
	private RClinicoRepository rcRepository;
	
	@Autowired
	private MongoTemplate template;

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
	
	@Transactional(readOnly = true)
	public List<RegistroClinico> listarRegistrosPorPaciente(String paciente_id){
		return rcRepository.findByPaciente(paciente_id);
	}
	
	@Transactional(readOnly = true)
	public RegistroClinico ultimoRegistroPaciente(String paciente_id) {
		return rcRepository.findTopByPacienteOrderByIdDesc(paciente_id);
	}
	
	@Transactional(readOnly = true)
	public Page<RegistroClinico> distinctRegistros(Pageable pegeable) {
		List<RegistroClinico> listComUmRegistro = new ArrayList<RegistroClinico>();
		Query query = new Query();
		List<String> ids = template.getCollection("sismed_registro_clinico").distinct("paciente", query.getQueryObject(), String.class).into(new ArrayList<>());
				
		for (String id : ids) {
			listComUmRegistro.add(rcRepository.findTopByPacienteOrderByIdDesc(id));
		}
		
		int start = (int) pegeable.getOffset();
		int end = (start + pegeable.getPageSize()) > listComUmRegistro.size() ? listComUmRegistro.size() : (start + pegeable.getPageSize());
		Page<RegistroClinico> listaPaginada = new PageImpl<RegistroClinico>(listComUmRegistro.subList(start, end), pegeable, listComUmRegistro.size());
		
		return listaPaginada;
	}

}
