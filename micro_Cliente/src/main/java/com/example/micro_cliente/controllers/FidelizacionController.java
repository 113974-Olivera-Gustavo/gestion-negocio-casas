package com.example.micro_cliente.controllers;

import com.example.micro_cliente.dto.catalogo.CompraRequest;
import com.example.micro_cliente.dto.catalogo.FacturacionRequest;
import com.example.micro_cliente.dto.catalogo.ProductoRequest;
import com.example.micro_cliente.entities.ClienteEntity;
import com.example.micro_cliente.models.Cliente;
import com.example.micro_cliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PostMapping("/setearPuntos")
    public ResponseEntity<Cliente> actualizarPuntos(@RequestBody FacturacionRequest facturacionRequest){
        Cliente cliente =clienteService.setearPuntos(facturacionRequest);
        return ResponseEntity.ok(cliente);
    }

    //Metodo que resta la cantidad de puntos al cliente
    @PostMapping("/procesar-compra")
    public ResponseEntity<String> procesarCompra(@RequestBody CompraRequest compraRequest) {
        try {
            // Procesar la compra y restar los puntos al cliente
            clienteService.procesarCompra(compraRequest);
            return ResponseEntity.ok("Compra procesada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la compra: " + e.getMessage());
        }
    }

}
