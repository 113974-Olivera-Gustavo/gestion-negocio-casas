package com.example.micro_cliente.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Descuento {
    private String codigo;
    private String nombre;
    private String descripcion;
    private Double precio_min;
    private Boolean activo;
}
