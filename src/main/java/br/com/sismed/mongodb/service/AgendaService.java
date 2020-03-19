package br.com.sismed.mongodb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Agenda;
import br.com.sismed.mongodb.repository.AgendaRepository;

@Service
public class AgendaService {

	@Autowired
	private AgendaRepository repository;

	@Transactional(readOnly = false)
	public Agenda salvar(Agenda agenda) {
		return repository.save(agenda);
	}

	@Transactional(readOnly = true)
	public List<Agenda> ListarAgendamentosMedico(String medico_id) {
		return repository.findByFuncionario_idAndDataOrderByHora(medico_id, LocalDate.now());
	}

	@Transactional(readOnly = true)
	public Agenda ultimoAgendamento(String paciente_id) {
		return repository.findTopByPaciente_idOrderByIdDesc(paciente_id);
	}

	@Transactional(readOnly = true)
	public List<Agenda> buscarAgendamentos(LocalDate data, String medico) {
		return repository.findByFuncionario_idAndDataOrderByHora(medico, data);
	}

	@Transactional(readOnly = true)
	public Optional<Agenda> buscarPorId(String id) {
		return repository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public List<Agenda> encerrarAtendimento(){
		return repository.findByData(LocalDate.now());
	}

	public Page<Agenda> agendamentosAnteriores(String id, PageRequest pagerequest) {
		return repository.findByPaciente_id(id, pagerequest);
	}
	
}
