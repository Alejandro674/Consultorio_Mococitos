package com.desarrollo.repositories.administracion;

import com.desarrollo.entities.administracion.Usuario;
import com.desarrollo.repositories.administracion.jpa.UsuarioJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author ivanLuna
 */
@Transactional
@Repository
@RequiredArgsConstructor
public class UsuarioRepository {

    private final UsuarioJpa usuarioJpa;

    public void guardarUsuario (Usuario usuario) {
        usuarioJpa.save( usuario );
    }

    public Usuario buscarPorNombreUsuario ( String nombreUsuario ) {
        Objects.requireNonNull( nombreUsuario, "Debe de ingresar un nombre de usuario." );
        return usuarioJpa.findByNombreUsuario( nombreUsuario );
    }

}
