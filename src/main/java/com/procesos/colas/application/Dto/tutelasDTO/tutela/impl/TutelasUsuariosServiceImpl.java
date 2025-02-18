package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.prolinktic.sgdea.application.services.tutela.TutelasUsuariosService;
import com.prolinktic.sgdea.application.services.tutela.dto.TutelaUsuarioDto;
import com.prolinktic.sgdea.domain.model.autentificacion.Usuarios;
import com.prolinktic.sgdea.infrastructure.dao.autenticacion.UsuariosDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TutelasUsuariosServiceImpl implements TutelasUsuariosService {

    private final UsuariosDao usuariosDao;
    private final List<String> roles = Arrays.asList("Asignador Responsable","Asignador Líder","Gestionador", "Asignador Lider");
    private final String oficina = "GRUPO TUTELAS";

    @Override
    public List<TutelaUsuarioDto> obtenerUsuariosAsignadores() {
        List<Usuarios> listadoUsuariosAsignadores = usuariosDao.findByUsuarioRelaciones_Rol_NombreInAndUsuarioRelaciones_Oficina_Nombre(roles, oficina);
        return listadoUsuariosAsignadores.stream().map(this::usuarioEntidaADto).toList();
    }

    private TutelaUsuarioDto usuarioEntidaADto(Usuarios usuario) {
        return TutelaUsuarioDto.builder()
                .id(usuario.getId())
                .usuario(usuario.getUserName())
                .nombre(usuario.getFirstname()+" "+usuario.getLastname())
                .oficina(oficina)
                .cargo(usuario.getCargo().getNombre())
                .rol(usuario.getUsuarioRelaciones().stream().map(item -> item.getRol().getNombre()).collect(Collectors.joining(",")))
                .build();
    }
}
