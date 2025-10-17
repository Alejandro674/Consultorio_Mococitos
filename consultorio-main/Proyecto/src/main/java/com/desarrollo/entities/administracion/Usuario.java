package com.desarrollo.entities.administracion;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author ivanLuna
 */
@Entity
public class Usuario implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;
    private String nombreUsuario;
    private String password;
    private String email;
    private String nombre;
    private String paterno;
    private String materno;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPaterno() { return paterno; }
    public void setPaterno(String paterno) { this.paterno = paterno; }
    public String getMaterno() { return materno; }
    public void setMaterno(String materno) { this.materno = materno; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals( getId(), usuario.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hash( getId() );
    }
}
