package br.com.sismed.mongodb.service;

import java.util.List;

import org.springframework.stereotype.Controller;

import br.com.sismed.mongodb.domain.Procedimento;
import br.com.sismed.mongodb.repository.ProcedimentoRepository;

@Controller
public class ProcedimentoService {

	private ProcedimentoRepository repository;

	public List<Procedimento> listarTodos() {
		return null;
	}
}
