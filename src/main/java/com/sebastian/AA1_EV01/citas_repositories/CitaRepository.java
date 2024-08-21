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
	 List<Citas> findByCedulaCliente(@Param("cliente_cedula") int cliente_cedula);
	 

}
