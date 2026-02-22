package repository;

import domain.EntidadCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la Entidad Cliente.
 * Responsabilidad: Operaciones de acceso a datos de los clientes.
 * Permite validar duplicidad en el registro.
 */
@Repository
public interface IClienteRepository extends JpaRepository<EntidadCliente, Integer> {
    boolean existsByDniCe(String dniCe);
}