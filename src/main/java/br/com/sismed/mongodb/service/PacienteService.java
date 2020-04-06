package br.com.sismed.mongodb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Paciente;
import br.com.sismed.mongodb.repository.PacienteRepository;

@Service
public class PacienteService {

	@Autowired
	private PacienteRepository pRepository;
	
	@Autowired
	private MongoTemplate template;

	@Transactional(readOnly = true)
	public Page<Paciente> buscarTodosComPaginacao(Pageable pageable) {
		return pRepository.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public List<Paciente> buscarTodos() {
		return pRepository.findAll();
	}

	public void salvar(Paciente paciente) {
		Paciente lastPaciente = lastPaciente();
		if (lastPaciente != null) {
			Long matricula = lastPaciente.getProntuario() + 1;
			paciente.setProntuario(matricula);
		} else {
			paciente.setProntuario(1L);
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
	
	public List<Paciente> ListarPacId(String dado) {
		Long matricula = Long.parseLong(dado);
		return pRepository.findByProntuario(matricula);
	}
	
	
	public List<Paciente> ListarPacNome(String dado) {
		return pRepository.findByNomeRegex(dado);
	}

	
	public List<Paciente> PesquisarCPF(String dado) {
		return pRepository.findByCpfRegex(dado);
	}

	
	public List<Paciente> PesquisarTelefone(String dado) {
		Query query = new Query();
		
		if(dado.length() <= 3) {
			query.addCriteria(Criteria.where("telefone_fixo").regex("\\" + dado));
		}
		else {
			String array[] = dado.split("\\)");
			query.addCriteria(Criteria.where("telefone_fixo").regex("\\" + array[0] + "\\)" + array[1]));
		}
		return template.find(query, Paciente.class);
	}
	
	public List<Paciente> PesquisarCelular(String dado) {
		Query query = new Query();
		
		if(dado.length() <= 3) {
			query.addCriteria(Criteria.where("celular").regex("\\" + dado));
		}
		else {
			String array[] = dado.split("\\)");
			query.addCriteria(Criteria.where("celular").regex("\\" + array[0] + "\\)" + array[1]));
		}
		return template.find(query, Paciente.class);
	} 
}
