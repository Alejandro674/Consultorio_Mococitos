package com.desarrollo.repositories.administracion.jpa;

import com.desarrollo.entities.administracion.Usuario;
import com.desarrollo.repositories.DesarrolloJpa;

public interface UsuarioJpa extends DesarrolloJpa<Usuario, Integer> {

    Usuario findByNombreUsuario( String nombreUsuario );

}