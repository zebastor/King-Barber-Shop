package com.sebastian.AA1_EV01.citas_models;

import java.time.LocalTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "cita")
public class Citas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idcita;
	private Date fecha;
	private LocalTime horainicio;
	private LocalTime horafin;
	private String estado;
	private int cliente_cedula;
	private int idmetodopago;

	private int barbero_cedula;
	private int idservicio;
	private int idcategoria;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idservicio", insertable = false, updatable = false)
	 private Servicios servicio;

    // Getters y Setters

    public Servicios getServicio() {
        return servicio;
    }

    public void setServicio(Servicios servicio) {
        this.servicio = servicio;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barbero_cedula", insertable = false, updatable = false)
	 private Barberos barbero;

    // Getters y Setters

    public Barberos getBarbero() {
        return barbero;
    }

    public void setBarbero(Barberos barbero) {
        this.barbero = barbero;
    }
	
	public int getIdcita() {
		return idcita;
	}
	public void setIdcita(int idcita) {
		this.idcita = idcita;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public LocalTime getHorainicio() {
		return horainicio;
	}
	public void setHorainicio(LocalTime horainicio) {
		this.horainicio = horainicio;
	}
	public LocalTime getHorafin() {
		return horafin;
	}
	public void setHorafin(LocalTime horafin) {
		this.horafin = horafin;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getCliente_cedula() {
		return cliente_cedula;
	}
	public void setCliente_cedula(int cliente_cedula) {
		this.cliente_cedula = cliente_cedula;
	}
	public int getIdMetodopago() {
		return idmetodopago;
	}
	public void setIdMetodopago(int idmetodopago) {
		this.idmetodopago = idmetodopago;
	}
	public int getBarbero_cedula() {
		return barbero_cedula;
	}
	public void setBarbero_cedula(int barbero_cedula) {
		this.barbero_cedula = barbero_cedula;
	}
	public int getIdservicio() {
		return idservicio;
	}
	public void setIdservicio(int idservicio) {
		this.idservicio = idservicio;
	}
	public int getIdcategoria() {
		return idcategoria;
	}
	public void setIdcategoria(int idcategoria) {
		this.idcategoria = idcategoria;
	}

}
