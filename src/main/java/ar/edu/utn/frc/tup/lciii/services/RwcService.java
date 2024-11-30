package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.PoolDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RwcService {

    List<PoolDTO> getResults();

    PoolDTO getOneResult(String pool);



}
