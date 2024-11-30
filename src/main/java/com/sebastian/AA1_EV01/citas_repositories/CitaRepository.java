package com.sebastian.AA1_EV01.citas_repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sebastian.AA1_EV01.citas_models.Citas;



public interface CitaRepository extends JpaRepository<Citas,Integer>{
	
	
	
	 List<Citas> findByFecha(Date fecha);
	 
	 
	 @Query("SELECT c FROM Citas c WHERE c.cliente_cedula = :cliente_cedula ORDER BY c.fecha DESC")
	 List<Citas> findByCedulaCliente(int cliente_cedula);
	 
	 
	 @Query("SELECT c FROM Citas c WHERE c.cliente_cedula = :cliente_cedula AND (c.fecha > CURRENT_DATE OR (c.fecha = CURRENT_DATE AND c.horafin > CURRENT_TIME)) ORDER BY c.fecha DESC")
	 List<Citas> findByCedulaClientePendientes(int cliente_cedula);

	 

}
