package com.sebastian.AA1_EV01.cliente_controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sebastian.AA1_EV01.citas_models.MetodosPago;
import com.sebastian.AA1_EV01.citas_models.Servicios;
import com.sebastian.AA1_EV01.MetodoPago_repositories.MetodosPagoRepository;
import com.sebastian.AA1_EV01.categoria_repositories.CategoriaRepository;
import com.sebastian.AA1_EV01.citas_models.Barberos;
import com.sebastian.AA1_EV01.citas_models.Categorias;
import com.sebastian.AA1_EV01.citas_models.Citas;
import com.sebastian.AA1_EV01.citas_repositories.BarberosRepository;
import com.sebastian.AA1_EV01.citas_repositories.CitaRepository;
import com.sebastian.AA1_EV01.cliente_models.Cliente;
import com.sebastian.AA1_EV01.cliente_repositories.ClientesRepository;
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
	
	@Autowired
	private BarberosRepository barberrepo;
	
	@Autowired
	private MetodosPagoRepository pagorepo;
	
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


	
	@GetMapping("/misCitas")
	public String listCitas(Model model) {
	    // Obtener la cédula del usuario autenticado
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = ((UserDetails) authentication.getPrincipal()).getUsername();
	    int cedula = Integer.parseInt(username);

	    // Obtener las citas del cliente usando la cédula del usuario autenticado
	    List<Citas> misCitas = citarep.findByCedulaCliente(cedula);
	  
	    List<Citas> misCitasPendientes = citarep.findByCedulaClientePendientes(cedula);
	    
	    // Agregar las citas al modelo
	    model.addAttribute("misCitas", misCitas);
	    model.addAttribute("misCitasPendientes", misCitasPendientes);
	    Cliente cliente = repo.findByCedula(cedula);
	    model.addAttribute("cliente", cliente);
	   

	    // Retornar la vista
	    return "clientes/misCitas"; 
	}



	
	   @GetMapping("/login")
	    public String loginPage() {
	        return "clientes/login"; 
	    }
	   
	   
	   
	   
	   
	   @GetMapping("/detalleServicio/{idservicio}")
	   public String mostrarDetalleServicio(@PathVariable("idservicio") int idservicio,		   Model model) {
	       // Buscar el servicio por su ID
	       Servicios servicio = serv.findById(idservicio);

	  
	           // Agregar el servicio al modelo
	           model.addAttribute("servicio", servicio);
	       
	       
	           // Obtener citas por fecha si se ha seleccionado una
	     //      List<Citas> citas = citarep.findAll();
	   	 //   model.addAttribute("citas", citas);
	        
	           
	   //    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		   
		        // Si el usuario está autenticado
		    //    int cedula = Integer.parseInt(((UserDetails) authentication.getPrincipal()).getUsername());
		    //    Cliente cliente = repo.findByCedula(cedula);

		    //    model.addAttribute("cedula", cliente.getCedula());
		  //      model.addAttribute("nombre", cliente.getNombre());
		    

	       // Retornar la vista que mostrará los detalles del servicio
	       return "clientes/detalleServicio";
	   } 
	   
	   
	   
	   
	   
	   @GetMapping("/seleccionHora/{idservicio}")
	   public String seleccionHora(@PathVariable("idservicio") int idservicio,
	                               @RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha, 
	                               @RequestParam("hora") LocalTime hora,
	                               Model model) {
	       // Buscar el servicio por su ID
	       Servicios servicio = serv.findById(idservicio);
	       model.addAttribute("servicio", servicio);

	       List<MetodosPago> metodosPago = pagorepo.findAll();
		    model.addAttribute("metodosPago", metodosPago);
	       
	       model.addAttribute("fechaSeleccionada", fecha);
	       model.addAttribute("horaSeleccionada", hora);

	       // Calcular la hora de finalización del servicio
	       LocalTime horaFinal = hora.plusMinutes(servicio.getDuracion());
	       model.addAttribute("horaSeleccionadaFin", horaFinal);

	       // Obtener los barberos disponibles
	       List<Barberos> barberosDisponibles = barberrepo.findBarberosDisponibles(fecha, hora, horaFinal);
	       model.addAttribute("barberos", barberosDisponibles);

	       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	       int cedula = Integer.parseInt(((UserDetails) authentication.getPrincipal()).getUsername());
	       Cliente cliente = repo.findByCedula(cedula);
	       model.addAttribute("cedula", cliente.getCedula());
	       model.addAttribute("nombre", cliente.getNombre());

	       return "clientes/seleccionHora";
	   }


	   @PostMapping("/seleccionHora")
	   public String confirmarCita(
	          @RequestParam("fechaSeleccionada") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
	          @RequestParam("horaSeleccionada") LocalTime horaInicio,
	          @RequestParam("horaSeleccionadaFin") LocalTime horaFin,
	          @RequestParam("cedula") int cedulaCliente,
	          @RequestParam("metodoPagoSeleccionado") int idMetodoPago,
	          @RequestParam("barberoSeleccionado") int cedulaBarbero,
	          @RequestParam("servicioId") int idServicio,
	          @RequestParam("categoriaId") int idCategoria,
	          @RequestParam("estado") String estado,
	          Model model) {
			try {
	       // Aquí puedes agregar la lógica para manejar la autenticación y otros aspectos
	     
	       // Crea una nueva instancia de la entidad Citas
	       Citas nuevaCita = new Citas();
	       nuevaCita.setFecha(fecha);
	       nuevaCita.setHorainicio(horaInicio);
	       nuevaCita.setHorafin(horaFin);
	       nuevaCita.setCliente_cedula(cedulaCliente);
	       nuevaCita.setIdMetodopago(idMetodoPago);
	       nuevaCita.setBarbero_cedula(cedulaBarbero);
	       nuevaCita.setIdservicio(idServicio);
	       nuevaCita.setIdcategoria(idCategoria);
	       nuevaCita.setEstado(estado);

	       // Guardar la cita en la base de datos
	       citarep.save(nuevaCita);
	       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	       int cedula = Integer.parseInt(((UserDetails) authentication.getPrincipal()).getUsername());
	       Cliente cliente = repo.findByCedula(cedula);
	       model.addAttribute("cedula", cliente.getCedula());
			}catch(Exception ex){
				
				System.out.println("Exception: "+ex.getMessage());
			}
	       // Redirigir a una página de confirmación
	       return "redirect:/confirmarCita";
	   }






	   @GetMapping("/confirmarCita")
	    public String showConfirm(Model model) {
		   
		   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	       int cedula = Integer.parseInt(((UserDetails) authentication.getPrincipal()).getUsername());
	       Cliente cliente = repo.findByCedula(cedula);
	       model.addAttribute("cedula", cliente.getCedula());
	        return "clientes/confirmarCita"; 
	    }
	    
	   
	   
		@GetMapping("/eliminarCita")
		public String deleteCita( @RequestParam int idcita) { 
			
			try {
				
				Citas cita = citarep.findById(idcita).get();
			
				
				citarep.delete(cita);
			}catch(Exception ex){
				
				System.out.println("Exception: "+ex.getMessage());
				
				
			}
			
			
			return "redirect:/misCitas";}
		
		
		@GetMapping("/miCuenta")
		public String miCuenta(Model model) {
		    // Obtener la cédula del usuario autenticado
		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		    int cedula = Integer.parseInt(username);

		    // Obtener las citas del cliente usando la cédula del usuario autenticado
		 
		  
		 
		    
		    // Agregar las citas al modelo
	
		    Cliente cliente = repo.findByCedula(cedula);
		    model.addAttribute("cliente", cliente);
		   

		    // Retornar la vista
		    return "clientes/miCuenta"; 
		}
		
		
		
	   
	
}