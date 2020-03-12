package br.com.sismed.mongodb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.sismed.mongodb.domain.PerfilTipo;
import br.com.sismed.mongodb.service.FuncionarioService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
    private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
    
    @Autowired
    private FuncionarioService service;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.authorizeRequests()
    	.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**","/primeiroAcesso","/verificarCPF", "/login/**","/recuperar/senha","/cadastrarSenha", "/redefinir/senha","/nova/senha","/usuario/**" ).permitAll()
    	
    	//acessos privados de medico
    			.antMatchers("/RegistroClinico/**").hasAnyAuthority(ADMIN,MEDICO)
    			
    			.antMatchers("/funcionario/cadastrar", "/funcionario/editar/**").hasAuthority(ADMIN)
    			
    			
    			
    			
    			.anyRequest().authenticated()
    			.and()
    				.formLogin()
    				.loginPage("/login")
    				.defaultSuccessUrl("/", true)
    				.failureUrl("/login-error")
    				.permitAll()
    			.and()
    				.logout()
    				.logoutSuccessUrl("/")
    			.and()
    				.exceptionHandling()
    				.accessDeniedPage("/acesso-negado");
    }
    
    @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder()); // tipo de criptografia
	}
}
