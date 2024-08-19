package com.sebastian.AA1_EV01.citas_repositories;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sebastian.AA1_EV01.citas_models.Barberos;



public interface BarberosRepository extends JpaRepository<Barberos,Integer> {
	
	List<Barberos> findByNombre(String nombre);
	
	@Query("SELECT b FROM Barberos b WHERE b.cedula NOT IN (SELECT c.barbero_cedula FROM Citas c WHERE c.fecha = :fecha AND " +
		       "(:horainicio < c.horafin AND :horafinal > c.horainicio))")
		List<Barberos> findBarberosDisponibles(@Param("fecha") Date fecha, 
		                                       @Param("horainicio") LocalTime horainicio, 
		                                       @Param("horafinal") LocalTime horafinal);



}
