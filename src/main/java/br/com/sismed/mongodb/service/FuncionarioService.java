package br.com.sismed.mongodb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.Login;
import br.com.sismed.mongodb.domain.Perfil;
import br.com.sismed.mongodb.domain.TConvenio;
import br.com.sismed.mongodb.repository.FuncionarioRepository;

@Service
public class FuncionarioService implements UserDetailsService {

	@Autowired
	private FuncionarioRepository repository;

	@Autowired
	private MongoTemplate template;

	@Autowired
	private EmailService emailService;

	@Autowired
	private TConvenioService tipoConvenioService;

	@Transactional(readOnly = true)
	public List<Funcionario> buscarTodos() {
		return repository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<Funcionario> buscarTodosComPaginacao(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public List<Funcionario> buscarMedicos() {
		return repository.listarPorCrm();
	}

	// Convenios aceitos por cada funcionário
	// Duas listas: tipo de convenio aceito por cada funcionario e convenios aceitos
	// por cada funcionario
	@Transactional(readOnly = true)
	public List<Funcionario> mostrarTipoConvenios() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Funcionario> mostrarConvenios() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Page<Funcionario> buscarTodos(PageRequest pagerequest) {

		return null;
	}

	@Transactional(readOnly = false)
	public void salvar(Funcionario funcionario) {
		Funcionario func = ultimoRegistro();
		Perfil p = new Perfil();
		Long matricula;
		if (func != null) {
			matricula = func.getMatricula() + 1;
		} else {
			matricula = 1L;
		}

		funcionario.setMatricula(matricula);
		
		
		if (funcionario.getCrm() != null) {
			TConvenio tipo = tipoConvenioService.buscarPorNome("particular");
			List<String> tipos = new ArrayList<String>();
			tipos.add(tipo.getId());
			funcionario.setTconvenio(tipos);
			p.setId(2L);
			p.setDescricao("MEDICO");
			
		}else {
			p.setId(3L);
			p.setDescricao("FUNCIONARIO");
		}
		funcionario.setPerfil(p);
		repository.save(funcionario);
	}

	@Transactional(readOnly = false)
	public void editar(Funcionario funcionario) {
		repository.save(funcionario);
	}

	@Transactional(readOnly = true)
	public Funcionario ultimoRegistro() {
		return repository.findTopByOrderByIdDesc();

	}

	@Transactional(readOnly = true)
	public Optional<Funcionario> buscarporId(String id) {
		return repository.findById(id);
	}

	@Transactional(readOnly = true)
	public Funcionario buscarPorCpf(String cpf) {
		return repository.findBycpf(cpf);
	}

	@Transactional(readOnly = false)
	public void excluir(String id) {
		repository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Funcionario funcionario = buscarPorCpf(username);

		return new User(funcionario.getCpf(), funcionario.getLogin().getSenha(),
				AuthorityUtils.createAuthorityList(funcionario.getPerfil().getDescricao())

		);
	}

	@Transactional(readOnly = true)
	public List<Funcionario> ListarFuncionarioNome(String dado) {

		return repository.findByNome(dado);
	}

	@Transactional(readOnly = true)
	public Funcionario buscarFuncionarioAtivo(String cpf) {
		Query query = new Query().addCriteria(Criteria.where("cpf").is(cpf).and("data_termino").is(null));
		return template.findOne(query, Funcionario.class);
	}

	@Transactional(readOnly = false)
	public void pedidoRedefinirSenha(String cpf, String email) throws MessagingException {
		/*
		 * busca pelo login a partir do cpf e testa se o mesmo ainda possui acesso a o
		 * sistema; o funcionario só tera acesso a o sistema caso a data de termino dele
		 * seja nula. Se a data de termino dele é nula, isso indica que o mesmo ainda
		 * possui vinculo ao SISMED; Caso não encontre nenhum, ele lançara uma excessão
		 * dizendo que o funcionario não possui acesso ao sistema
		 */
		Funcionario funcionario = buscarFuncionarioAtivo(cpf);

		// gera um codgio verificador aleatorio com 6 digitos
		String verificador = RandomStringUtils.randomAlphanumeric(6);

		// seta o codigo verificador na tabela para uma verificação na hora em que o
		// usuario informar o codigo
		Login login = funcionario.getLogin();
		login.setCodigo(verificador);
		funcionario.setLogin(login);
		salvar(funcionario);
		// chama o metodo responsavel por enviar o email informado em tela, junto com o
		// codigo verificador
		emailService.enviarPedidoRedefinicaoSenha(email, verificador);
	}

	@Transactional(readOnly = false)
	public void alterarSenha(Funcionario funcionario, String senha) {
		Login l = funcionario.getLogin();
		l.setSenha(senha);

		funcionario.setLogin(l);

		repository.save(funcionario);
	}
	
	@Transactional(readOnly = true)
	public Funcionario buscarPorMatricula(Long dado) {
		return repository.findByMatricula(dado);
	}

	public List<Funcionario> ListarFuncionarioCPF(String dado) {
		return repository.findByCpfRegex(dado);
	}

	public List<Funcionario> ListarFuncionarioCelular(String dado) {
		Query query = new Query();
		
		if(dado.length() <= 3) {
			query.addCriteria(Criteria.where("celular").regex("\\" + dado));
		}
		else {
			String array[] = dado.split("\\)");
			query.addCriteria(Criteria.where("celular").regex("\\" + array[0] + "\\)" + array[1]));
		}
		return template.find(query, Funcionario.class);
	}

	public List<Funcionario> ListarFuncionarioCRM(String dado) {
		return repository.findByCrmRegex(dado);
	}

	public List<Funcionario> ListarFuncionarioEspecialidade(String dado) {
		return repository.findByEspecialidadeRegex(dado);
	}
}
