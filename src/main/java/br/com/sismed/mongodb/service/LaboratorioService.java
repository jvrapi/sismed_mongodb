package br.com.sismed.mongodb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Laboratorio;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.repository.LaboratorioRepository;

@Service
public class LaboratorioService {

	@Autowired
	private LaboratorioRepository repository;

	@Autowired
	private TConvenioService tipoConvenioService;

	@Transactional(readOnly = true)
	public List<Laboratorio> buscarTodos() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Laboratorio> buscarPorId(String id) {
		return repository.findById(id);
	}

	@Transactional(readOnly = false)
	public void salvar(Laboratorio laboratorio) {
		TConvenio tipo = tipoConvenioService.buscarPorNome("particular");
		List<String> tipos = new ArrayList<String>();
		tipos.add(tipo.getId());
		laboratorio.setTipo_convenio(tipos);
		repository.save(laboratorio);

	}

	@Transactional(readOnly = false)
	public void editar(Laboratorio laboratorio) {
		repository.save(laboratorio);
	}

	@Transactional(readOnly = true)
	public Page<Laboratorio> buscarTodos(Pageable pageable) {

		return repository.findAll(pageable);
	}

	@Transactional(readOnly = false)
	public void excluir(String laboratorioId) {
		repository.deleteById(laboratorioId);

	}

	@Transactional(readOnly = true)
	public List<Laboratorio> ListarLaboratorioNome(String term) {
		return repository.findByNome(term);
	}

	public List<Laboratorio> ListarLaboratorioTelefone(String term) {
		return repository.findByTelefone(term);
	}

	public List<Laboratorio> ListarLaboratorioBairro(String term) {
		return repository.findByBairro(term);
	}
}
