package com.example.micro_cliente.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    private Long id;
    private String nombre;
    private String apellido;
    private TipoDocumento tipoDocumento;
    private Long nroDoc;
    private String domicilio;
    private String telefono;
    private String email;
    private Long puntosBeneficio;
}
