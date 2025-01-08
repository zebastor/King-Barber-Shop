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
						.requestMatchers("/clientes/**").permitAll()
						.requestMatchers("/barberos/**").hasRole("BARBERO")
						.requestMatchers("/").permitAll()
						.requestMatchers("/register").permitAll()
						.requestMatchers("/images/**").permitAll()
						.requestMatchers("/logout").permitAll()
						.requestMatchers("/barberos/index").permitAll()
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
