package com.sebastian.AA1_EV01.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		return http
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/").permitAll()
				.requestMatchers("/register").permitAll()
				.requestMatchers("/login").permitAll()
				.requestMatchers("/confirmarCita").authenticated()
				.requestMatchers("/seleccionHora").authenticated()
				.requestMatchers("/detalleServicio").authenticated()
				.requestMatchers("/modificarCita").authenticated()
				.requestMatchers("/modificarBarberoCita").authenticated()
			
				.requestMatchers("/monfirmarCambios").authenticated()
				.requestMatchers("/misCitas").authenticated()
				.requestMatchers("/miCuenta").authenticated()
				.requestMatchers("/images/**").permitAll()
				.requestMatchers("/logout").permitAll()
				
				.anyRequest().authenticated()
				)
		
		.formLogin(form -> form
                .loginPage("/login") 
                .defaultSuccessUrl("/", true) 
                .failureUrl("/login?error=true") 
                .permitAll()
            )
		
		.formLogin(form -> form
				.defaultSuccessUrl("/", true))
		.logout(config -> config.logoutSuccessUrl("/"))
		.build();
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

