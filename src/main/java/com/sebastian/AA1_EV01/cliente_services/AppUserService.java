package com.sebastian.AA1_EV01.cliente_services;

import com.sebastian.AA1_EV01.citas_models.Barberos;
import com.sebastian.AA1_EV01.cliente_repositories.BarberosRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sebastian.AA1_EV01.cliente_models.Cliente;
import com.sebastian.AA1_EV01.cliente_repositories.ClientesRepository;



@Service
public class AppUserService implements UserDetailsService {

	@Autowired
	private ClientesRepository clienteRepo;

	@Autowired
	private BarberosRepository2 barberoRepo;

	@Override
	public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {
		int cedulaInt = Integer.parseInt(cedula);

		// Verifica primero en la tabla cliente
		Cliente cliente = clienteRepo.findByCedula(cedulaInt);
		if (cliente != null) {
			return User.withUsername(String.valueOf(cedulaInt))
					.password(cliente.getClave())
					.roles("CLIENTE")  // Aquí asignas el rol correspondiente
					.build();
		}

		// Si no se encuentra en cliente, verifica en barbero
		Barberos barbero = barberoRepo.findByCedula(cedulaInt);
		if (barbero != null) {
			return User.withUsername(String.valueOf(cedulaInt))
					.password(barbero.getClave())
					.roles("BARBERO")  // Aquí asignas el rol correspondiente
					.build();
		}

		// Si no se encuentra en ninguna de las dos tablas, lanzamos una excepción
		throw new UsernameNotFoundException("Usuario no encontrado");
	}
}
