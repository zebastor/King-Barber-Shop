package com.sebastian.AA1_EV01.cliente_controllers;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sebastian.AA1_EV01.categoria_models.Categorias;
import com.sebastian.AA1_EV01.categoria_repositories.CategoriaRepository;
import com.sebastian.AA1_EV01.citas_models.Citas;
import com.sebastian.AA1_EV01.citas_repositories.CitaRepository;
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
	
	@Autowired
	private CategoriaRepository categ;
	
	@Autowired
	private CitaRepository citarep;
	
	@GetMapping({"", "/"})
	public String showClientList(Model model) {
	    // Obtener la lista de servicios y agregarla al modelo
	    List<Servicios> servicios = serv.findAll();
	    model.addAttribute("servicios", servicios);
	    
   List<Categorias> categorias = categ.findAll();
	    model.addAttribute("categorias", categorias);

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
	        return "clientes/login"; 
	    }
	   
	   
	   
	   
	   
	   @GetMapping("/detalleServicio/{idservicio}")
	   public String mostrarDetalleServicio(@PathVariable("idservicio") int idservicio,
			   @RequestParam(value = "fecha", required = false) 
       @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
			   
			   Model model) {
	       // Buscar el servicio por su ID
	       Servicios servicio = serv.findById(idservicio);

	  
	           // Agregar el servicio al modelo
	           model.addAttribute("servicio", servicio);
	       
	       
	           // Obtener citas por fecha si se ha seleccionado una
	           List<Citas> citas;
	           if (fecha != null) {
	               citas = citarep.findByFecha(fecha);
	           } else {
	               citas = citarep.findAll();
	           }
	           model.addAttribute("citas", citas);
	        
	           
	       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		   
		        // Si el usuario está autenticado
		        int cedula = Integer.parseInt(((UserDetails) authentication.getPrincipal()).getUsername());
		        Cliente cliente = repo.findByCedula(cedula);

		        model.addAttribute("cedula", cliente.getCedula());
		        model.addAttribute("nombre", cliente.getNombre());
		    

	       // Retornar la vista que mostrará los detalles del servicio
	       return "detalleServicio";
	   } 
	   
	   @GetMapping("/filtrarCitasPorFecha")
	   @ResponseBody
	   public ResponseEntity<List<Citas>> filtrarCitasPorFecha(
	       @RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> fechaOpt) {

	       if (!fechaOpt.isPresent()) {
	           return ResponseEntity.badRequest().body(Collections.emptyList());
	       }

	       Date fecha = fechaOpt.get();
	       List<Citas> citas = citarep.findByFecha(fecha);
	       return ResponseEntity.ok(citas);
	   }
}