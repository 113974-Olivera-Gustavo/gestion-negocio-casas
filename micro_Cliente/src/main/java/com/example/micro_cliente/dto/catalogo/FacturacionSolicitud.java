package com.example.micro_cliente.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacturacionSolicitud {
    private Long nroDoc;
    private BigDecimal totalAmountBilled;
}
