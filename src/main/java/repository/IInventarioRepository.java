package repository;

import domain.EntidadInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IInventarioRepository extends JpaRepository<EntidadInventario, Integer> {

    @Query("SELECT i.stockFisico FROM EntidadInventario i WHERE i.idProducto = :idProducto")
    Optional<Integer> consultarStock(@Param("idProducto") Integer idProducto);

    // Alternativa sin JPQL (Spring Data lo genera):
    Optional<EntidadInventario> findByIdProducto(Integer idProducto);
}