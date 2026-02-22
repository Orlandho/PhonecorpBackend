package repository;

import domain.EntidadInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio: IInventarioRepository
 * Capa: Entidad (BCE)
 * Responsabilidad: Gestionar el acceso a los datos del stock físico de los productos.
 * Es utilizado por GestorCatalogo para mostrar disponibilidad y por GestorInventarioDespacho 
 * para reducir el stock tras una venta.
 */
@Repository
public interface IInventarioRepository extends JpaRepository<EntidadInventario, Integer> {

    /**
     * Método: consultarStock
     * Propósito: Obtener la cantidad física real de un equipo en el Kardex.
     * Este método es consumido por el CatalogoController para validar disponibilidad 
     * antes de permitir la adición al carrito o la generación de la orden.
     * * @param idProducto Identificador del producto a consultar.
     * @return Cantidad de stock disponible.
     */
    @Query("SELECT i.cantidadStock FROM EntidadInventario i WHERE i.idProducto = :idProducto")
    int consultarStock(@Param("idProducto") Integer idProducto);
}