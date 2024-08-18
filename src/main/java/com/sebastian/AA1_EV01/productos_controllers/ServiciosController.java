package com.sebastian.AA1_EV01.productos_controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sebastian.AA1_EV01.productos_models.Servicios;
import com.sebastian.AA1_EV01.productos_repositories.ServiciosRepository;


@Controller
public class ServiciosController {
	@Autowired
	private ServiciosRepository repo;
	
	
	   @GetMapping("/prueba")
	    public String getAllProducts(Model model) {
	        List<Servicios> servicios = repo.findAll();
	        model.addAttribute("servicios", servicios);
	        return "prueba"; // Asegúrate de que el nombre del archivo Thymeleaf sea 'listado.html'
	    }

}
