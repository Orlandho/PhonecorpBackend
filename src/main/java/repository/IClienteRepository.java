package repository;

import domain.EntidadCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio: IClienteRepository
 * Capa: Persistencia (BCE - Entidad/Acceso a datos)
 * Responsabilidad: Gestionar el acceso a datos de Cliente.
 *
 * SOLID - SRP:
 * Este repositorio SOLO se encarga de operaciones de BD (sin lógica de negocio).
 */
@Repository
public interface IClienteRepository extends JpaRepository<EntidadCliente, Integer> {

    /**
     * Método: existePorDniCe
     * Propósito: validar unicidad de dni_ce (regla soportada por UNIQUE en BD).
     *
     * Nota: JPQL usa atributos Java, no columnas SQL.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM EntidadCliente c WHERE c.dniCe = :dniCe")
    boolean existePorDniCe(@Param("dniCe") String dniCe);

    /**
     * Método: buscarPorDniCe
     * Propósito: obtener un cliente por DNI/CE.
     */
    @Query("SELECT c FROM EntidadCliente c WHERE c.dniCe = :dniCe")
    Optional<EntidadCliente> buscarPorDniCe(@Param("dniCe") String dniCe);
}