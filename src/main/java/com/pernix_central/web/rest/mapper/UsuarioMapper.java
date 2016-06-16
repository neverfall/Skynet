package com.pernix_central.web.rest.mapper;

import com.pernix_central.domain.*;
import com.pernix_central.web.rest.dto.UsuarioDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Usuario and its DTO UsuarioDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UsuarioMapper {

    UsuarioDTO usuarioToUsuarioDTO(Usuario usuario);

    List<UsuarioDTO> usuariosToUsuarioDTOs(List<Usuario> usuarios);

    Usuario usuarioDTOToUsuario(UsuarioDTO usuarioDTO);

    List<Usuario> usuarioDTOsToUsuarios(List<UsuarioDTO> usuarioDTOs);
}
