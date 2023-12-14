package com.example.micro_cliente.service;

import com.example.micro_cliente.entities.ClienteEntity;
import com.example.micro_cliente.entities.TipoDocumentoEntity;
import com.example.micro_cliente.models.Cliente;
import com.example.micro_cliente.models.TipoDocumento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClienteService {
    TipoDocumentoEntity createTipoDocumento(TipoDocumento tipoDocumento);
    List<TipoDocumentoEntity> getAllTipoDocumento();
    TipoDocumentoEntity deleteTipoDocumento(Long id);
    ClienteEntity createCliente(Cliente cliente);
    List<ClienteEntity> getAllClientes();
    ClienteEntity getClienteByNroDoc(Long nroDoc);
}
