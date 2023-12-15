package com.example.micro_cliente.service.impl;

import com.example.micro_cliente.client.FidelizacionClient;
import com.example.micro_cliente.dto.catalogo.*;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                if(cliente.getNroDoc().equals(nroDocAPI)){
                    // Obtenemos el monto total facturado por el cliente desde la API
                    BigDecimal totalAmountBilledAPI = facturacion.getTotalAmountBilled();
                    Double puntosAdicionales = totalAmountBilledAPI.divide(new BigDecimal("5000"), RoundingMode.FLOOR).doubleValue()*10;
                    // Sumamos los nuevos puntos a los puntos existentes
                    Double nuevosPuntos = cliente.getPuntosBeneficio() + puntosAdicionales;

                    // Actualizamos los puntos del cliente en la entidad y guardamos en la base de datos
                    cliente.setPuntosBeneficio(nuevosPuntos);
                    clienteRepository.save(cliente);

                    return modelMapper.map(cliente, Cliente.class);
                }
                else {
                    throw new RuntimeException("El DNI ingresado no coincide con el DNI del cliente.");
                }
            } else {
                throw new RuntimeException("El DNI ingresado no coincide con el DNI del cliente.");
            }
        } else {
            throw new RuntimeException("DNI no encontrado en la API externa.");

        }
    }

    @Override
    public void procesarCompra(CompraRequest compra) {
        List<ProductoRequest> productosEnOferta =getAllProductosOfertaActiva();
        for(ProductosCanjeados productosCanjeados : compra.getProductosCanjeados()){
            String codigoProducto = productosCanjeados.getCodigo();
            int cantidadComprada = productosCanjeados.getCantidad();

            //Encontrar codigo en la oferta
            Optional<ProductoRequest> productoOptional =productosEnOferta.stream()
                    .filter(producto -> producto.getCodigo().equals(codigoProducto))
                    .findFirst();
            if(productoOptional.isPresent()){
                Double puntosPorProducto = productoOptional.get().getOfertas().get(0).getPuntos();
                Double puntosTotales = puntosPorProducto * cantidadComprada;

                restarPuntosAlCliente(compra.getNroDoc(), puntosTotales);
            }
            else{
                throw new RuntimeException("Producto no encontrado en la oferta: "+codigoProducto);
            }
        }
    }
    //Metodos auxiliares
    public void restarPuntosAlCliente(Long nroDoc, Double puntosTotales) {
        Optional<ClienteEntity> cliente = clienteRepository.findByNroDoc(nroDoc);
        if(cliente.isPresent()){
            if(cliente.get().getPuntosBeneficio() < puntosTotales){
                throw new RuntimeException("El cliente no tiene suficientes puntos para realizar la compra");
            }
            ClienteEntity clienteEntity = cliente.get();
            Double puntosActuales = clienteEntity.getPuntosBeneficio();
            Double nuevosPuntos = Math.max(0, puntosActuales - puntosTotales);

            clienteEntity.setPuntosBeneficio(nuevosPuntos);
            clienteRepository.save(clienteEntity);
        }
        else {
            throw new RuntimeException("No se encontro un cliente con el numero de documento: " + nroDoc);
        }
    }
    private boolean validarEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    private Boolean tieneOfertasActivas(ProductoRequest productoRequest){
        return productoRequest.getOfertas().stream().anyMatch(oferta -> oferta.getActivo());
    }
}
