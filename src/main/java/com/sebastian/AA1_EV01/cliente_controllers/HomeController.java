package com.sebastian.AA1_EV01.cliente_controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sebastian.AA1_EV01.cliente_models.Cliente;
import com.sebastian.AA1_EV01.cliente_repositories.ClientesRepository;
import com.sebastian.AA1_EV01.productos_models.Servicios;
import com.sebastian.AA1_EV01.productos_repositories.ServiciosRepository;



@Controller
public class HomeController {
	@Autowired
	private ClientesRepository repo;
	
	@Autowired
	private ServiciosRepository serv;
	
	@GetMapping({"", "/"})
	public String showClientList(Model model) {
	    // Obtener la lista de servicios y agregarla al modelo
	    List<Servicios> servicios = serv.findAll();
	    model.addAttribute("servicios", servicios);

	    // Obtener el contexto de autenticación
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    // Verificar si el usuario está autenticado
	    if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
	        // Si el usuario está autenticado
	        int cedula = Integer.parseInt(((UserDetails) authentication.getPrincipal()).getUsername());
	        Cliente cliente = repo.findByCedula(cedula);

	        model.addAttribute("cedula", cliente.getCedula());
	        model.addAttribute("nombre", cliente.getNombre());
	    } else {
	        // Si el usuario no está autenticado, no mostrar información personal
	        model.addAttribute("message", "No estás autenticado. Por favor, inicia sesión para ver la lista de clientes.");
	    }

	    return "/clientes/index"; // Retornar la vista
	}


	
	

	
	   @GetMapping("/login")
	    public String loginPage() {
	        return "clientes/login"; // Nombre del archivo sin extensión
	    }
}