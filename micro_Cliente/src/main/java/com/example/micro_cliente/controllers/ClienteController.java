package com.example.micro_cliente.controllers;

import com.example.micro_cliente.entities.ClienteEntity;
import com.example.micro_cliente.entities.TipoDocumentoEntity;
import com.example.micro_cliente.models.Cliente;
import com.example.micro_cliente.models.TipoDocumento;
import com.example.micro_cliente.service.ClienteService;
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
    @DeleteMapping("/deleteTipoDocumento/{id}")
    public ResponseEntity<TipoDocumentoEntity> deleteTipoDocumento(@PathVariable Long id){
        TipoDocumentoEntity deletedTipoDocumento = clienteService.deleteTipoDocumento(id);
        return ResponseEntity.ok(deletedTipoDocumento);
    }

    @PostMapping("/postCliente")
    public ResponseEntity<ClienteEntity> postCliente(@RequestBody Cliente cliente){
        if(cliente == null){
            return ResponseEntity.badRequest().body(null);
        }
        ClienteEntity createdCliente = clienteService.createCliente(cliente);
        return ResponseEntity.ok(createdCliente);
    }
    @GetMapping("/getAllClientes")
    public ResponseEntity<List<ClienteEntity>> getAllClientes() {
        List<ClienteEntity> clientes = clienteService.getAllClientes();
        return ResponseEntity.ok(clientes);
    }
    @GetMapping("/getClienteByNroDoc/{nroDoc}")
    public ResponseEntity<ClienteEntity> getClienteByNroDoc(@PathVariable Long nroDoc) {
        ClienteEntity cliente = clienteService.getClienteByNroDoc(nroDoc);
        return ResponseEntity.ok(cliente);
    }
}
