package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.prolinktic.sgdea.application.services.tutela.TutelaCausalService;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.TutelaCausalesDao;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.repository.tutela.dto.TutelaCausalDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutelaCausalServiceImp implements TutelaCausalService {

    private final TutelaCausalesDao dao;

    public TutelaCausalServiceImp(TutelaCausalesDao dao) {
        this.dao = dao;
    }

    @Override
    public List<TutelaCausalDto> obtenerCausales() {
        return dao.obtenerCausales();
    }
}
