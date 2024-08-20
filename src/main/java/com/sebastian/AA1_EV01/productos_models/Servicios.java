package com.sebastian.AA1_EV01.productos_models;



import com.sebastian.AA1_EV01.categoria_models.Categorias;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name= "servicio")
public class Servicios {
	@Id
	//@GeneratedValue(strategy= GenerationType.IDENTITY)
	
	private int idservicio;
	private String titulo;
	@Column(columnDefinition = "TEXT")
	
	private String descripcion;
	private String imagen;
	private int precio;
	private Integer duracion;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoriaservicio_idcategoria", insertable = false, updatable = false)
	 private Categorias categoriaservicio;
	
	public Categorias getCategoriaservicio() {
        return categoriaservicio;
    }

    public void setCategoriaservicio(Categorias categoriaservicio) {
        this.categoriaservicio = categoriaservicio;
    }
	
	public int getIdservicio() {
		return idservicio;
	}
	public void setIdservicio(int idservicio) {
		this.idservicio = idservicio;
	}
	
	public int getPrecio() {
		return precio;
	}
	public void setPrecio(int precio) {
		this.precio = precio;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public Integer getDuracion() {
		return duracion;
	}
	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}
	
	
}
