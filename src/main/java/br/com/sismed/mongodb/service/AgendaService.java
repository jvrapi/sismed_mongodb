package br.com.sismed.mongodb.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Agenda;
import br.com.sismed.mongodb.repository.AgendaRepository;

@Service
public class AgendaService {

	@Autowired
	private AgendaRepository repository;
	
	
	@Transactional(readOnly=false)
	public Agenda salvar(Agenda agenda) {
		return repository.save(agenda);
	}


	public List<Agenda> ListarAgendamentosMedico(String medico_id) {
		
		return repository.findByFuncionario_idAndData(medico_id, LocalDate.now());
	}
	
	
}
