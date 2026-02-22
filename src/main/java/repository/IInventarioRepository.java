package repository;

import domain.EntidadInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la Entidad Inventario.
 * Responsabilidad: Gestionar el stock fisico real de los productos para la venta y el despacho.
 */
@Repository
public interface IInventarioRepository extends JpaRepository<EntidadInventario, Integer> {
}