package repository;

import domain.EntidadInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio: IInventarioRepository
 * Capa: Entidad BCE
 * Responsabilidad: Gestionar el acceso a los datos del inventario.
 */
@Repository
public interface IInventarioRepository extends JpaRepository<EntidadInventario, Integer> {

    /**
     * Metodo: consultarStock
     * Proposito: Obtener la cantidad fisica real de un equipo en la base de datos.
     * Se ajusto la consulta JPQL para apuntar al atributo stockFisico 
     * que representa la columna stock_fisico de la tabla Inventario.
     */
    @Query("SELECT i.stockFisico FROM EntidadInventario i WHERE i.idProducto = :idProducto")
    int consultarStock(@Param("idProducto") Integer idProducto);
}