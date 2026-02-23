package repository;

import domain.EntidadComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IComprobanteRepository extends JpaRepository<EntidadComprobante, Integer> {

    @Query("SELECT c FROM EntidadComprobante c WHERE c.pago.idPago = :idPago")
    Optional<EntidadComprobante> buscarPorIdPago(@Param("idPago") Integer idPago);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
           "FROM EntidadComprobante c WHERE c.pago.idPago = :idPago")
    boolean existePorIdPago(@Param("idPago") Integer idPago);

    @Query("SELECT c FROM EntidadComprobante c " +
           "WHERE c.tipoComprobante = :tipo AND c.serie = :serie AND c.numeroCorrelativo = :numero")
    Optional<EntidadComprobante> buscarPorCorrelativo(
            @Param("tipo") String tipo,
            @Param("serie") String serie,
            @Param("numero") String numero
    );

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
           "FROM EntidadComprobante c " +
           "WHERE c.tipoComprobante = :tipo AND c.serie = :serie AND c.numeroCorrelativo = :numero")
    boolean existePorCorrelativo(
            @Param("tipo") String tipo,
            @Param("serie") String serie,
            @Param("numero") String numero
    );
}