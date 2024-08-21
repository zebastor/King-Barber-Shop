package com.sebastian.AA1_EV01.citas_models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name= "categoriaservicio")
public class Categorias {
	@Id
	private int idcategoria;
	private String nombrecategoria;
	
	public int getIdcategoria() {
		return idcategoria;
	}
	public void setIdcategoria(int idcategoria) {
		this.idcategoria = idcategoria;
	}
	public String getNombrecategoria() {
		return nombrecategoria;
	}
	public void setNombrecategoria(String nombrecategoria) {
		this.nombrecategoria = nombrecategoria;
	}

}
