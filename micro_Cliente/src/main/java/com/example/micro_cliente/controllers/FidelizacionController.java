package com.example.micro_cliente.controllers;

import com.example.micro_cliente.dto.catalogo.ProductoRequest;
import com.example.micro_cliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FidelizacionController {
    private final ClienteService clienteService;

    @Autowired
    public FidelizacionController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/getAllProductosOfertaActivas")
    public ResponseEntity<List<ProductoRequest>> getAllProductos() {
        List<ProductoRequest> productos = clienteService.getAllProductosOfertaActiva();
        return ResponseEntity.ok(productos);
    }
}
