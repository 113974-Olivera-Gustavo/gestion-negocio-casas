package com.example.micro_cliente.dto;

import com.example.micro_cliente.models.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteRequest {
    private String nombre;
    private String apellido;
    private TipoDocumento tipoDocumento;
    private Long nroDoc;
    private String domicilio;
    private String telefono;
    private String email;
    private Long puntosBeneficio;
}
