package br.com.sismed.mongodb.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.sismed.mongodb.domain.Funcionario;
import br.com.sismed.mongodb.domain.Login;
import br.com.sismed.mongodb.service.FuncionarioService;

@Controller
public class HomeController {
	@Autowired
	private FuncionarioService funcionarioService;

	@GetMapping({ "/", "/home" })
	public String paginaInicial(@AuthenticationPrincipal User user, ModelMap model) {
		Funcionario funcionario = funcionarioService.buscarPorCpf(user.getUsername());
		String pattern = "\\S+";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(funcionario.getNome());

		if (m.find()) {
			// escreva o grupo encontrado
			model.addAttribute("usuario", m.group(0));

		} else {
			// mensagem de erro
			model.addAttribute("usuario", funcionario.getNome());
		}

		return "home";
	}

	// abrir pagina login
	@GetMapping({ "/login" })
	public String login() {

		return "login";
	}

	// login invalido
	@GetMapping({ "/login-error" })
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Crendenciais inválidas!");
		model.addAttribute("texto", "Login ou senha incorretos, tente novamente.");
		model.addAttribute("subtexto", "Acesso permitido apenas para cadastros já ativados.");
		return "login";
	}

	// acesso negado
	@GetMapping({ "/acesso-negado" })
	public String acessoNegado(ModelMap model, HttpServletResponse resp) {
		model.addAttribute("status", resp.getStatus()); // codigo do status
		model.addAttribute("error", "Acesso Negado");
		model.addAttribute("message", "Você não tem acesso a está area ou ação");

		return "error";
	}

	// abre a pagina para inserir email e cpf
	@GetMapping("/redefinir/senha")
	public String pedidoRedefirSenha() {
		return "usuario/pedido-recuperar-senha";
	}

	// recupera os dados para a verificação
	@GetMapping("/recuperar/senha")
	public String redefinirSenha(String email, String cpf, ModelMap model) throws MessagingException {
		// chamando o metodo no controller
		Funcionario funcionario = funcionarioService.buscarFuncionarioAtivo(cpf);
		String retorno = "";
		if (funcionario == null) {
			model.addAttribute("falha", "Esse CPF não possui acesso ao nosso sistema");
			retorno = "usuario/pedido-recuperar-senha";
		} else {

			funcionarioService.pedidoRedefinirSenha(cpf, email);
			String funcionario_id = funcionario.getId();
			Long perfil = funcionario.getPerfil().getId();

			model.addAttribute("sucesso", "Em instantes, você receberá um email para redefinir a sua senha");

			model.addAttribute("funcionario", new Funcionario(cpf));
			model.addAttribute("funcionario_id", funcionario_id);
			model.addAttribute("perfil", perfil);
			retorno = "usuario/recuperar-senha";
		}
		return retorno;
	}

	// salva a senha
	@PostMapping("/nova/senha")
	public String novaSenha(Funcionario funcionario, @RequestParam("senha") String senha, ModelMap model) {
		Funcionario f = funcionarioService.buscarPorCpf(funcionario.getCpf());

		if (!funcionario.getLogin().getCodigo().equals(f.getLogin().getCodigo())) {
			model.addAttribute("falha", "codigo verificador não confere");
			return "usuario/recuperar-senha";
		}

		Login login = f.getLogin();
		login.setCodigo(null);

		f.setLogin(login);

		funcionarioService.alterarSenha(f, senha);

		model.addAttribute("alerta", "sucesso");
		model.addAttribute("titulo", "Senha redefinida");
		model.addAttribute("texto", "Você já pode realizar o login");
		return "login";
	}
}
