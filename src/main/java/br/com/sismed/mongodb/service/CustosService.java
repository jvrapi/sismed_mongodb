package br.com.sismed.mongodb.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Custos;
import br.com.sismed.mongodb.repository.CustosRepository;

@Service
public class CustosService {

	@Autowired
	private CustosRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Transactional(readOnly = false)
	public void salvar(Custos custos) {
		repository.save(custos);
	}

	@Transactional(readOnly = true)
	public List<Custos> buscarPorPaciente(String paciente) {

		return repository.findByPaciente(paciente);
	}

	@Transactional(readOnly = true)
	public BigDecimal buscarReceitaPorPaciente(String paciente) {
		List<Custos> valores = buscarPorPaciente(paciente);
		BigDecimal receita = BigDecimal.ZERO;
		for (Custos custos : valores) {
			receita = receita.add(custos.getValor());
		}
		return receita;
	}

	@Transactional(readOnly = true)
	public BigDecimal buscarReceitaPorConvenio(String convenio) {
		List<Custos> valores = buscarPorConvenio(convenio);
		BigDecimal receita = BigDecimal.ZERO;
		for (Custos custos : valores) {
			receita = receita.add(custos.getValor());
		}
		return receita;
	}

	@Transactional(readOnly = true)
	public BigDecimal receitaTodosConvenios() {
		List<Custos> valores = buscarTodosConvenios();
		BigDecimal receita = BigDecimal.ZERO;
		for (Custos custos : valores) {
			receita = receita.add(custos.getValor());
		}
		return receita;
	}

	@Transactional(readOnly = true)
	public BigDecimal buscarReceitaPorFuncionario(String funcionario) {
		List<Custos> valores = buscarPorFuncionario(funcionario);
		BigDecimal receita = BigDecimal.ZERO;
		for (Custos custos : valores) {
			receita = receita.add(custos.getValor());
		}
		return receita;
	}

	@Transactional(readOnly = true)
	public BigDecimal buscarReceitaPorDatas(LocalDate inicio, LocalDate fim) {
		List<Custos> valores = buscarPorDatas(inicio, fim);
		BigDecimal receita = BigDecimal.ZERO;
		for (Custos custos : valores) {
			receita = receita.add(custos.getValor());
		}
		return receita;
	}

	@Transactional(readOnly = true)
	public BigDecimal receitaPacientePeriodo(String paciente, LocalDate inicio, LocalDate fim) {
		List<Custos> valores = pacientePeriodo(paciente, inicio, fim);
		BigDecimal receita = BigDecimal.ZERO;
		for (Custos custos : valores) {
			receita = receita.add(custos.getValor());
		}
		return receita;
	}

	@Transactional(readOnly = true)
	public BigDecimal receitaConvenioPeriodo(String convenio, LocalDate dataInicio, LocalDate dataFim) {
		List<Custos> valores = convenioPeriodo(convenio, dataInicio, dataFim);
		BigDecimal receita = BigDecimal.ZERO;
		for (Custos custos : valores) {
			receita = receita.add(custos.getValor());
		}
		return receita;
	}

	@Transactional(readOnly = true)
	public BigDecimal receitaFuncionarioPeriodo(String funcionario, LocalDate dataInicio, LocalDate dataFim) {
		List<Custos> valores = funcionarioPeriodo(funcionario, dataInicio, dataFim);
		BigDecimal receita = BigDecimal.ZERO;
		for (Custos custos : valores) {
			receita = receita.add(custos.getValor());
		}
		return receita;
	}

	@Transactional(readOnly = true)
	public List<Custos> buscarPorConvenio(String convenio) {
		return repository.findByConvenio(convenio);
	}

	@Transactional(readOnly = true)
	public List<Custos> buscarTodosConvenios() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Custos> buscarPorFuncionario(String funcionario) {
		return repository.findByFuncionario(funcionario);
	}

	@Transactional(readOnly = true)
	public List<Custos> buscarPorDatas(LocalDate data1, LocalDate data2) {
		Query query = new Query().addCriteria(Criteria.where("data").gt(data1).lte(data2));
		return mongoTemplate.find(query, Custos.class);

	}

	@Transactional(readOnly = true)
	public List<Custos> pacientePeriodo(String paciente, LocalDate dataInicio, LocalDate dataFim) {
		Query query = new Query()
				.addCriteria(Criteria.where("data").gt(dataInicio).lte(dataFim).and("paciente").is(paciente));
		return mongoTemplate.find(query, Custos.class);
	}

	@Transactional(readOnly = true)
	public List<Custos> convenioPeriodo(String convenio, LocalDate dataInicio, LocalDate dataFim) {
		Query query = new Query()
				.addCriteria(Criteria.where("data").gt(dataInicio).lte(dataFim).and("convenio").is(convenio));
		return mongoTemplate.find(query, Custos.class);
	}

	@Transactional(readOnly = true)
	public List<Custos> funcionarioPeriodo(String funcionario, LocalDate dataInicio, LocalDate dataFim) {
		Query query = new Query()
				.addCriteria(Criteria.where("data").gt(dataInicio).lte(dataFim).and("funcionario").is(funcionario));
		return mongoTemplate.find(query, Custos.class);
	}

}
