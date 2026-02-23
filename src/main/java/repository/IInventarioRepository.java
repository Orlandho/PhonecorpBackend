package com.phonecorp.phonecorpbackend.repository;

import com.phonecorp.phonecorpbackend.domain.EntidadInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IInventarioRepository extends JpaRepository<EntidadInventario, Integer> {

    @Query("SELECT i.stockFisico FROM EntidadInventario i WHERE i.producto.idProducto = :idProducto")
    Optional<Integer> consultarStock(@Param("idProducto") Integer idProducto);

    Optional<EntidadInventario> findByProducto_IdProducto(Integer idProducto);

    // Metodo batch para resolver el problema N+1 en el catalogo:
    // trae todos los registros de inventario para una lista de IDs en una sola consulta SQL
    @Query("SELECT i FROM EntidadInventario i WHERE i.producto.idProducto IN :ids")
    List<EntidadInventario> findByProducto_IdProductoIn(@Param("ids") List<Integer> ids);
}