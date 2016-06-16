package com.pernix_central.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Usuario entity.
 */
public class UsuarioDTO implements Serializable {

    private Long id;

    private String name;

    @NotNull
    private String email;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) o;

        if ( ! Objects.equals(id, usuarioDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", email='" + email + "'" +
            '}';
    }
}
