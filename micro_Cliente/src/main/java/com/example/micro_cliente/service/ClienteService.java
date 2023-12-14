package com.example.micro_cliente.service;

import com.example.micro_cliente.entities.TipoDocumentoEntity;
import com.example.micro_cliente.models.TipoDocumento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClienteService {
    TipoDocumentoEntity createTipoDocumento(TipoDocumento tipoDocumento);
    List<TipoDocumentoEntity> getAllTipoDocumento();
}
