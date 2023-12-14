package com.example.micro_cliente.service.impl;

import com.example.micro_cliente.entities.TipoDocumentoEntity;
import com.example.micro_cliente.models.TipoDocumento;
import com.example.micro_cliente.repositories.ClienteRepository;
import com.example.micro_cliente.repositories.TipoDocumentoRepository;
import com.example.micro_cliente.service.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository, TipoDocumentoRepository tipoDocumentoRepository, ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public TipoDocumentoEntity createTipoDocumento(TipoDocumento tipoDocumento) {
        if(tipoDocumento == null){
            throw new NullPointerException("Tipo de documento no puede ser nulo");
        }
        TipoDocumentoEntity tipoDocumentoEntity = modelMapper.map(tipoDocumento, TipoDocumentoEntity.class);
        return tipoDocumentoRepository.save(tipoDocumentoEntity);
    }

    @Override
    public List<TipoDocumentoEntity> getAllTipoDocumento() {
        if(tipoDocumentoRepository.findAll().isEmpty()){
            throw new NullPointerException("No hay tipos de documento");
        }
        return tipoDocumentoRepository.findAll();
    }

}
