package com.sebastian.AA1_EV01.cliente_repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sebastian.AA1_EV01.cliente_models.*;



public interface ClientesRepository extends JpaRepository<Cliente, Integer> {
	
	public Cliente findByCedula(int cedula);
	

}
