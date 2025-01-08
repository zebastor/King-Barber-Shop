package com.sebastian.AA1_EV01.cliente_controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

import com.sebastian.AA1_EV01.cliente_repositories.BarberosRepository2;

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

	@Autowired
	private BarberosRepository2 barberoRepo;

	@GetMapping({"", "/"})
	public String showClientList(Model model) {
		// Obtener la lista de servicios y categorías y agregarlos al modelo
		List<Servicios> servicios = serv.findAll();
		model.addAttribute("servicios", servicios);

		List<Categorias> categorias = categ.findAll();
		model.addAttribute("categorias", categorias);

		// Obtener el contexto de autenticación
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Verificar si el usuario está autenticado
		if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
			String username = ((UserDetails) authentication.getPrincipal()).getUsername();
			int cedula = Integer.parseInt(username);

			// Verificar si el usuario es un Cliente
			Cliente cliente = repo.findByCedula(cedula);
			if (cliente != null) {
				model.addAttribute("cedula", cliente.getCedula());
				model.addAttribute("nombre", cliente.getNombre());
				return "/clientes/index"; // Página de clientes
			}

			// Verificar si el usuario es un Barbero
			Barberos barbero = barberrepo.findByCedula(cedula);
			if (barbero != null) {
				model.addAttribute("cedula", barbero.getCedula());
				model.addAttribute("nombre", barbero.getNombre());

				// Citas del barbero
				List<Citas> misCitasPendientes = citarep.findByCedulaBarbero(cedula);
				model.addAttribute("misCitasPendientes", misCitasPendientes);

				return "/clientes/indexBarbero"; // Página de barberos
			}

			// Si no se encuentra ni Cliente ni Barbero
			model.addAttribute("message", "Usuario no encontrado. Por favor, verifica tus credenciales.");
			return "error/usuario_no_encontrado";
		}

		// Si no está autenticado, mostrar el índice por defecto (clientes/index)
		return "/clientes/index";
	}


	@GetMapping("/{fecha}")
	public String showClientListByDate(@PathVariable("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha,
									   Model model) {
		// Obtener el contexto de autenticación
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Verificar si el usuario está autenticado
		if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
			String username = ((UserDetails) authentication.getPrincipal()).getUsername();
			int cedula = Integer.parseInt(username);

			// Verificar si el usuario es un Barbero
			Barberos barbero = barberrepo.findByCedula(cedula);
			if (barbero != null) {
				model.addAttribute("cedula", barbero.getCedula());
				model.addAttribute("nombre", barbero.getNombre());

				// Obtener las citas del barbero para la fecha seleccionada
				List<Citas> citasDelBarbero = citarep.findByCedulaBarberoAndFecha(cedula, java.sql.Date.valueOf(fecha));
				model.addAttribute("misCitasPendientes", citasDelBarbero);
				model.addAttribute("fechaSeleccionada", fecha);

				return "/clientes/indexBarbero"; // Página de barberos con las citas filtradas por fecha
			}

			// Si no se encuentra el Barbero
			model.addAttribute("message", "Usuario no encontrado. Por favor, verifica tus credenciales.");
			return "error/usuario_no_encontrado";
		}

		// Si no está autenticado, mostrar el índice por defecto (clientes/index)
		return "/clientes/index";
	}



	@PostMapping("/{idcita}")
	public String realizarCita(@RequestParam("idcita") int id) {
		Optional<Citas> optionalCita = citarep.findById(id);
		if (optionalCita.isPresent()) {
			Citas cita = optionalCita.get();
			if ("pendiente".equalsIgnoreCase(cita.getEstado())) {
				cita.setEstado("realizado");
				citarep.save(cita);
			}
		}
		return "redirect:/"; // Redirige a la página inicial correctamente
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
	public String loginPage(Authentication authentication) {
		// Si el usuario ya está autenticado, redirige dependiendo del rol
		if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
			if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_BARBERO"))) {
				return "redirect:clientes/indexBarbero"; // Redirigir a la página de Barbero
			} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENTE"))) {
				return "redirect:/"; // Redirigir a la página de Cliente
			}
		}
		// Si no está autenticado, muestra la página de login
		return "clientes/login";
	}







	@GetMapping("/detalleServicio/{idservicio}")
	public String mostrarDetalleServicio(@PathVariable("idservicio") int idservicio,		   Model model) {
		// Buscar el servicio por su ID
		Servicios servicio = serv.findById(idservicio);


		// Agregar el servicio al modelo
		model.addAttribute("servicio", servicio);



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
			nuevaCita.setIdmetodopago(idMetodoPago);
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
	public String showConfirm() {
		return "clientes/confirmarCita";
	}



	@PostMapping("/eliminarCita")
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


	@PostMapping("/modificarCita")
	public String modificarDetalleServicio(@RequestParam("idcita") int idcita, Model model) {
		// Buscar la cita por su ID
		Citas cita = citarep.findById(idcita).orElseThrow(() -> new IllegalArgumentException("Invalid cita ID:" + idcita));

		// Agregar la cita al modelo
		model.addAttribute("cita", cita);

		return "clientes/modificarCita";
	}



	@PostMapping("/modificarBarberoCita")
	public String modificarBarberoCita(@RequestParam("idcita") int idcita,
									   @RequestParam("fecha") String fechaString,
									   @RequestParam("hora") LocalTime horainicio,
									   @RequestParam("idservicio") int idservicio,
									   Model model) throws ParseException {
		// Buscar la cita por ID
		Citas cita = citarep.findById(idcita).orElseThrow(() -> new IllegalArgumentException("Invalid cita ID: " + idcita));

		// Obtener el servicio y calcular la hora final
		Servicios servicio = serv.findById(idservicio);
		LocalTime horafinal = horainicio.plusMinutes(servicio.getDuracion());

		// Agregar datos al modelo
		model.addAttribute("servicio", servicio);
		model.addAttribute("horaSeleccionadaFin", horafinal);
		model.addAttribute("cita", cita);
		model.addAttribute("fecha", fechaString);
		model.addAttribute("hora", horainicio);

		// Parsear la fecha
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date fecha = dateFormat.parse(fechaString);

		// Obtener la lista de barberos disponibles
		List<Barberos> barberosDisponibles = barberrepo.findBarberosDisponibles(fecha, horainicio, horafinal);
		model.addAttribute("barberos", barberosDisponibles);

		return "clientes/modificarBarberoCita";
	}


	@PostMapping("/confirmarCambios")
	public String confirmarCambios(@RequestParam("idcita") int idcita,
								   @RequestParam("fecha") String fechaString,
								   @RequestParam("hora") LocalTime horainicio,
								   @RequestParam("horaSeleccionadaFin") LocalTime horafinal,
								   @RequestParam("cedula") int cedula,
								   @RequestParam("idservicio") int idservicio,
								   @RequestParam("categoriaId") int categoriaId,
								   @RequestParam("metodoPagoSeleccionado") int metodoPagoSeleccionado,
								   @RequestParam("barberoSeleccionado") int barberoCedula)throws ParseException  {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date fecha = dateFormat.parse(fechaString);
		// Lógica para actualizar la cita en la base de datos
		Citas cita = citarep.findById(idcita).orElseThrow(() -> new IllegalArgumentException("Invalid cita ID: " + idcita));
		cita.setIdcita(idcita);
		cita.setFecha(fecha);
		cita.setHorainicio(horainicio);
		cita.setHorafin(horafinal);
		cita.setCliente_cedula(cedula);
		cita.setIdservicio(idservicio);
		cita.setIdmetodopago(metodoPagoSeleccionado);
		cita.setBarbero_cedula(barberoCedula);

		// Guardar cambios en la base de datos
		citarep.save(cita);

		return "clientes/confirmarCambios";
	}













}