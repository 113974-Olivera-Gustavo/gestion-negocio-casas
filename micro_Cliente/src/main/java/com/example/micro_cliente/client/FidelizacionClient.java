package com.example.micro_cliente.client;

import com.example.micro_cliente.dto.catalogo.FacturacionSolicitud;
import com.example.micro_cliente.dto.catalogo.ProductoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FidelizacionClient {

    private final RestTemplate restTemplate;

    private String catalogo_URL = "https://my-json-server.typicode.com/113974-Olivera-Gustavo/api-catalogo-bd/productos";
    private String facturacion_URL = "http://my-json-server.typicode.com/114084-DIBELLA-THIAGO/billedClients/listado";

    public List<ProductoRequest> getAllProductos(){
        ResponseEntity<ProductoRequest[]> response = restTemplate.exchange(catalogo_URL, HttpMethod.GET,null, ProductoRequest[].class);
        ProductoRequest[] productos = response.getBody();
        return Arrays.asList(productos);
    }
    public List<FacturacionSolicitud> getAllFacturacion(){
        ResponseEntity<FacturacionSolicitud[]> response = restTemplate.exchange(facturacion_URL, HttpMethod.GET,null, FacturacionSolicitud[].class);
        FacturacionSolicitud[] facturaciones = response.getBody();
        return Arrays.asList(facturaciones);
    }
}
