package com.sebastian.AA1_EV01.cliente_models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RegistroDto {

	@NotNull(message = "Se requiere la cedula")
	
	private int cedula;
	@NotEmpty(message="Se requiere el nombre")
	private String nombre;
	@NotEmpty(message="Se requiere el apellido")
	private String apellido;
	@Min(value = 15, message = "La edad mínima es 15")
	@Max(value = 100, message = "La edad máxima es 100")
	private int edad;
	@NotEmpty(message="Se requiere un correo")
	@Email(message="email no valido")
	private String correo;
	@NotEmpty(message="Se requiere una clave")
	private String clave;
	private String confirmClave;
	@NotNull(message = "Se requiere la cedula")

@Pattern(regexp = "^[0-9]+$", message = "El teléfono solo puede contener números")
	private String telefono;
	public int getCedula() {
		return cedula;
	}
	public void setCedula(int cedula) {
		this.cedula = cedula;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getConfirmClave() {
		return confirmClave;
	}
	public void setConfirmClave(String confirmClave) {
		this.confirmClave = confirmClave;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
}
