package com.example.micro_cliente.entities;

import com.example.micro_cliente.models.TipoDocumento;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nombre;
    @Column
    private String apellido;
    @ManyToOne
    @JoinColumn(name = "id_tipo_doc")
    private TipoDocumentoEntity tipoDocumento;
    @Column
    private Long nroDoc;
    @Column
    private String domicilio;
    @Column
    private String telefono;
    @Column
    private String email;
    @Column
    @Min(0)
    @Max(5000)
    private Double puntosBeneficio;
}
