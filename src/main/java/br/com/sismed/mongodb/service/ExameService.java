package br.com.sismed.mongodb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import br.com.sismed.mongodb.domain.Exame;
import br.com.sismed.mongodb.repository.ExameRepository;

@Service
public class ExameService {

	@Autowired
	private ExameRepository repository;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Transactional(readOnly = false)
	public void salvar(Exame exame) {
		repository.save(exame);

	}

	@Transactional(readOnly = true)
	public Page<Exame> buscarTodos(PageRequest pagerequest) {
		return repository.findAll(pagerequest);
	}

	@Transactional(readOnly = true)
	public Optional<Exame> buscarporId(String id) {
		return repository.findById(id);
	}

	@Transactional(readOnly = false)
	public void excluir(String id) {
		repository.deleteById(id);

	}

	@Transactional(readOnly = true)
	public List<Exame> listarExamesPorPaciente(String paciente_id) {
		return repository.findByPaciente(paciente_id);
	}

	@Transactional(readOnly = true)
	public List<Exame> listarExamesPorNome(String exame) {
		return repository.findByNome(exame);
	}

	@Transactional(readOnly = true)
	public List<Exame> listarExamesPorDataColeta(String data) {
		LocalDate data_coleta = LocalDate.parse(data);
		return repository.findByDataColeta(data_coleta);
	}

	@Transactional(readOnly = true)
	public List<Exame> listarExamesPorNomeEPaciente(String exame, String paciente_id) {
		return repository.findByNomeAndPaciente(exame, paciente_id);
	}

	@Transactional(readOnly = true)
	public List<Exame> listarExamesPorPacienteEDataColeta(String paciente_id, String data) {
		LocalDate data_coleta = LocalDate.parse(data);
		Query query = new Query()
				.addCriteria(Criteria.where("data_coleta").is(data_coleta).and("paciente").is(paciente_id));
		return mongoTemplate.find(query, Exame.class);
	}

	public List<Exame> listarExamesPorNomeEDataColeta(String exame, String data) {
		LocalDate data_coleta = LocalDate.parse(data);
		Query query = new Query()
				.addCriteria(Criteria.where("nome").is(exame).and("data_coleta").is(data_coleta));
		return mongoTemplate.find(query, Exame.class);
	}

	public List<Exame> listarExamesPorNomeEPacienteEDataColeta(String exame, String paciente_id, String data) {
		LocalDate data_coleta = LocalDate.parse(data);
		Query query = new Query()
				.addCriteria(Criteria.where("nome").is(exame).and("data_coleta").is(data_coleta).and("paciente").is(paciente_id));
		return mongoTemplate.find(query, Exame.class);
	}
}
