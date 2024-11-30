package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.ErrorApi;
import ar.edu.utn.frc.tup.lciii.dtos.common.PoolDTO;
import ar.edu.utn.frc.tup.lciii.services.RwcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class RwcController {
    private final RwcService rwcService;

    @GetMapping("/pools")
    public ResponseEntity<List<PoolDTO>> getAllPools() {
        return ResponseEntity.ok(rwcService.getResults());
    }
    @GetMapping("/pools/{poolId}")
    public ResponseEntity<PoolDTO> getPoolById(@PathVariable String poolId) {
        return ResponseEntity.ok(rwcService.getOneResult(poolId));
    }
}
