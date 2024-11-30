package com.sebastian.AA1_EV01.citas_models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name= "metododepago")
public class MetodosPago {
	@Id
	private int idmetodo;
	private String nombremetodo;
	public int getIdmetodo() {
		return idmetodo;
	}
	public void setIdmetodo(int idmetodo) {
		this.idmetodo = idmetodo;
	}
	public String getNombremetodo() {
		return nombremetodo;
	}
	public void setNombremetodo(String nombremetodo) {
		this.nombremetodo = nombremetodo;
	}

}
