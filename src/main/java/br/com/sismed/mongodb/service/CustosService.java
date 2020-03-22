package br.com.sismed.mongodb.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Custos;
import br.com.sismed.mongodb.repository.CustosRepository;

@Service
public class CustosService {

	@Autowired
	private CustosRepository repository;
	
	
	@Transactional(readOnly=false)
	public void salvar(Custos custos) {
		repository.save(custos);
	}

	@Transactional(readOnly=true)
	public List<Custos> buscarPorPaciente(String paciente) {
		
		return repository.findByPaciente(paciente);
	}

	@Transactional(readOnly=true)
	public BigDecimal buscarReceitaPorPaciente(String paciente) {
		List<Custos> valores = buscarPorPaciente(paciente);
		BigDecimal receita = BigDecimal.ZERO;
		for (Custos custos : valores) {
			receita = receita.add(custos.getValor());
		}
		return receita;
	}

	@Transactional(readOnly=true)
	public List<Custos> buscarPorConvenio(String convenio) {
		return repository.findByConvenio(convenio);
	}
	
}
