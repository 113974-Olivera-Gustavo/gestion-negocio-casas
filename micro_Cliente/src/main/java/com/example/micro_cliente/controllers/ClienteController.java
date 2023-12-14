package com.example.micro_cliente.controllers;

import com.example.micro_cliente.entities.TipoDocumentoEntity;
import com.example.micro_cliente.models.Cliente;
import com.example.micro_cliente.models.TipoDocumento;
import com.example.micro_cliente.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    @GetMapping("/getAllTipoDocumento")
    public ResponseEntity<List<TipoDocumentoEntity>> getTipoDocumento() {
        List<TipoDocumentoEntity> tipoDocumento = clienteService.getAllTipoDocumento();
        return ResponseEntity.ok(tipoDocumento);
    }
    @PostMapping("/postTipoDocumento")
    public ResponseEntity<TipoDocumentoEntity> postTipoDocumento(@RequestBody TipoDocumento tipoDocumento ){
        if(tipoDocumento == null){
            return ResponseEntity.badRequest().body(null);
        }
        TipoDocumentoEntity createdTipoDocumento = clienteService.createTipoDocumento(tipoDocumento);
        return ResponseEntity.ok(createdTipoDocumento);
    }
}
