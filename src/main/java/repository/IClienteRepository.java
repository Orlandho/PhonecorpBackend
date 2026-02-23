package com.phonecorp.phonecorpbackend.repository;

import com.phonecorp.phonecorpbackend.domain.EntidadCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClienteRepository extends JpaRepository<EntidadCliente, Integer> {

    @Query("SELECT c FROM EntidadCliente c WHERE c.dniCe = :dniCe")
    Optional<EntidadCliente> buscarPorDniCe(@Param("dniCe") String dniCe);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
           "FROM EntidadCliente c WHERE c.dniCe = :dniCe")
    boolean existePorDniCe(@Param("dniCe") String dniCe);
}