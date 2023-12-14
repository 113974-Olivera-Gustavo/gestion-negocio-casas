package com.example.micro_cliente.service.impl;

import com.example.micro_cliente.entities.ClienteEntity;
import com.example.micro_cliente.entities.TipoDocumentoEntity;
import com.example.micro_cliente.models.Cliente;
import com.example.micro_cliente.models.TipoDocumento;
import com.example.micro_cliente.repositories.ClienteRepository;
import com.example.micro_cliente.repositories.TipoDocumentoRepository;
import com.example.micro_cliente.service.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ClienteServiceImpl implements ClienteService {

    private static final String EMAIL_REGEX = ".*@(gmail|hotmail)\\..*";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

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

    @Override
    public TipoDocumentoEntity deleteTipoDocumento(Long id) {
        Optional<TipoDocumentoEntity> existeTipoDocumento = tipoDocumentoRepository.findAll().stream()
                .filter(tipoDocumentoEntity -> tipoDocumentoEntity.getId().equals(id)).findFirst();
        if(!existeTipoDocumento.isPresent()){
            throw new NullPointerException("No existe un tipo de documento con ese id");
        }
        tipoDocumentoRepository.deleteById(id);
        return existeTipoDocumento.get();
    }

    @Override
    public ClienteEntity createCliente(Cliente cliente) {
        if(cliente == null){
            throw new NullPointerException("Cliente no puede ser nulo");
        }
        Optional<ClienteEntity> existeEmail = clienteRepository.findByEmail(cliente.getEmail());
        if(existeEmail.isPresent()){
            throw new IllegalArgumentException("Ya existe un cliente con ese email");
        }
        if (!validarEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Formato de email no válido");
        }

        Optional<ClienteEntity> existeTelefono = clienteRepository.findByTelefono(cliente.getTelefono());
        if(existeTelefono.isPresent()){
            throw new IllegalArgumentException("Ya existe un cliente con ese telefono");
        }
        Optional<ClienteEntity> existeNroDoc = clienteRepository.findByNroDoc(cliente.getNroDoc());
        if(existeNroDoc.isPresent()){
            throw new IllegalArgumentException("Ya existe un cliente con ese nro de documento");
        }
        ClienteEntity clienteEntity = modelMapper.map(cliente, ClienteEntity.class);
        return clienteRepository.save(clienteEntity);

    }

    @Override
    public List<ClienteEntity> getAllClientes() {
        List<ClienteEntity> clientes = clienteRepository.findAll();
        if(clientes.isEmpty()){
            throw new NullPointerException("No hay clientes");
        }
        return clientes;
    }

    @Override
    public ClienteEntity getClienteByNroDoc(Long nroDoc) {
        Optional<ClienteEntity> existeNroDoc = clienteRepository.findByNroDoc(nroDoc);
        if(!existeNroDoc.isPresent()){
            throw new NullPointerException("No existe un cliente con ese nro de documento");
        }
        return existeNroDoc.get();
    }

    private boolean validarEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

}
