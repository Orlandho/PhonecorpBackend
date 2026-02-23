package repository;

import domain.EntidadInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IInventarioRepository extends JpaRepository<EntidadInventario, Integer> {

    @Query("SELECT i.stockFisico FROM EntidadInventario i WHERE i.producto.idProducto = :idProducto")
    Optional<Integer> consultarStock(@Param("idProducto") Integer idProducto);

    Optional<EntidadInventario> findByProducto_IdProducto(Integer idProducto);
}