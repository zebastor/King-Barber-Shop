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



@Controller
public class HomeController {
	@Autowired
	private ClientesRepository repo;
	
	@GetMapping({"", "/"})
public String showClientList(Model model) {
		
		
		  // Obtener el contexto de autenticación
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
	    // Verificar si el usuario está autenticado
	    if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
	        // Si el usuario está autenticado
	        List<Cliente> clientes = repo.findAll();
	        model.addAttribute("cliente", clientes);

	        // Obtener el nombre del usuario autenticado
	        int cedula = Integer.parseInt(((UserDetails) authentication.getPrincipal()).getUsername());
	        Cliente cliente = repo.findByCedula(cedula);
	        model.addAttribute("cedula", cliente.getCedula());
	        model.addAttribute("nombre", cliente.getNombre()); // Agregar nombre al modelo
	    } else {
	        // Si el usuario no está autenticado, puedes mostrar un mensaje o contenido diferente
	        model.addAttribute("message", "No estás autenticado. Por favor, inicia sesión para ver la lista de clientes.");
	    }
	    
	    return "/clientes/index"; // Retornar la misma vista en ambos casos
	}


	
	

	
	   @GetMapping("/login")
	    public String loginPage() {
	        return "clientes/login"; // Nombre del archivo sin extensión
	    }
}