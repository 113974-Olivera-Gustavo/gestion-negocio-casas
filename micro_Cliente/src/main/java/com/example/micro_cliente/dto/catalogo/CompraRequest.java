package com.example.micro_cliente.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompraRequest {
    private Long nroDoc;
    private List<ProductosCanjeados> productosCanjeados;
}
