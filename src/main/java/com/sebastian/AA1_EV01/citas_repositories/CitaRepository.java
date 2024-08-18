package com.sebastian.AA1_EV01.citas_repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sebastian.AA1_EV01.citas_models.Citas;



public interface CitaRepository extends JpaRepository<Citas,Integer>{
	
	 List<Citas> findByFecha(Date fecha);

}
