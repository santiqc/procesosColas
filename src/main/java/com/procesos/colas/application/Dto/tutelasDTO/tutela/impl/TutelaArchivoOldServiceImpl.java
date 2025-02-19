package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.procesos.colas.application.Dto.tutelasDTO.tutela.TutelaArchivoDao;
import com.procesos.colas.application.Dto.tutelasDTO.tutela.TutelaArchivoService;
import com.procesos.colas.domain.tutelas.TutelaArchivo;;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutelaArchivoOldServiceImpl implements TutelaArchivoService {

    private final TutelaArchivoDao dao;

    public TutelaArchivoOldServiceImpl(TutelaArchivoDao dao) {
        this.dao = dao;
    }


    @Override
    public List<TutelaArchivo> guardarArchivos(List<TutelaArchivo> archivos) {
        return dao.guardarArchivos(archivos);
    }

}
