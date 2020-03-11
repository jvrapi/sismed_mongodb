package br.com.sismed.mongodb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Procedimento;
import br.com.sismed.mongodb.repository.ProcedimentoRepository;

@Service
public class ProcedimentoService {

	@Autowired
	private ProcedimentoRepository repository;

	@Transactional(readOnly = false)
	public void salvar(Procedimento procedimento) {
		repository.insert(procedimento);
	}

	@Transactional(readOnly=true)
	public List<Procedimento> listarTodos(String id){
		return repository.findByConvenio(id);
	}
	
	@Transactional(readOnly = false)
	public void deletar(String id) {
		repository.deleteById(id);
	}

}
