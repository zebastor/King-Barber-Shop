package com.sebastian.AA1_EV01.productos_repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sebastian.AA1_EV01.productos_models.Servicios;



public interface ServiciosRepository extends JpaRepository<Servicios, Integer> {
	
	public Servicios findById(int idservicio);

}
