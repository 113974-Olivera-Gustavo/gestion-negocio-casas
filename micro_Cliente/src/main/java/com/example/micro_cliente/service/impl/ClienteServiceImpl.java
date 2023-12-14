package com.example.micro_cliente.service.impl;

import com.example.micro_cliente.client.FidelizacionClient;
import com.example.micro_cliente.dto.catalogo.FacturacionRequest;
import com.example.micro_cliente.dto.catalogo.FacturacionSolicitud;
import com.example.micro_cliente.dto.catalogo.ProductoRequest;
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
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    private static final String EMAIL_REGEX = ".*@(gmail|hotmail)\\..*";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final ClienteRepository clienteRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final ModelMapper modelMapper;
    private final FidelizacionClient fidelizacionClient;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository, TipoDocumentoRepository tipoDocumentoRepository, ModelMapper modelMapper, FidelizacionClient fidelizacionClient) {
        this.clienteRepository = clienteRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.modelMapper = modelMapper;
        this.fidelizacionClient = fidelizacionClient;
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

    //Fidelizacion


    @Override
    public List<ProductoRequest> getAllProductosOfertaActiva() {
        try {
            if(fidelizacionClient == null){
                throw new NullPointerException("No se pudo conectar con el servicio de fidelizacion");
            }
            List<ProductoRequest> productos = fidelizacionClient.getAllProductos();
            return productos.stream()
                    .filter(this::tieneOfertasActivas)
                    .collect(Collectors.toList());
        }catch (RuntimeException e){
            throw new RuntimeException("No se pudo encontrar ofertas activas"+ e.getMessage());
        }
    }

    @Override
    public Cliente setearPuntos(FacturacionRequest facturacionRequest) {
        Long nroDocAPI = facturacionRequest.getNroDoc();

       List<FacturacionSolicitud> facturacionSolicitud = fidelizacionClient.getAllFacturacion();

       Optional<FacturacionSolicitud> facturacionOptional = facturacionSolicitud.stream()
               .filter(facturacion -> facturacion.getNroDoc().equals(nroDocAPI)).findFirst();

        if (facturacionOptional.isPresent()) {

            FacturacionSolicitud facturacion = facturacionOptional.get();
            Optional <ClienteEntity> clienteOptional = clienteRepository.findByNroDoc(nroDocAPI);

            if(clienteOptional.isPresent()){
                ClienteEntity cliente = clienteOptional.get();
                //Metodo setear puntos...seguir con el seteo de puntos, resta de puntos.
            }
        }
    }


    //Metodos auxiliares

    private boolean validarEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    private Boolean tieneOfertasActivas(ProductoRequest productoRequest){
        return productoRequest.getOfertas().stream().anyMatch(oferta -> oferta.getActivo());
    }
}
