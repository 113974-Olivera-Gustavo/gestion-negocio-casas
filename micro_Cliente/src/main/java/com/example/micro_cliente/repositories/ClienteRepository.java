package com.example.micro_cliente.repositories;

import com.example.micro_cliente.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByEmail(String email);

    Optional<ClienteEntity> findByTelefono(String telefono);

    Optional<ClienteEntity> findByNroDoc(Long nroDoc);
}
