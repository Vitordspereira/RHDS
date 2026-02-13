package com.hub.hds.repository.vaga;

import com.hub.hds.models.vaga.CategoriaVaga;
import com.hub.hds.models.vaga.StatusVaga;
import com.hub.hds.models.vaga.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {
    @Query("""
    SELECT DISTINCT v FROM Vaga v
    LEFT JOIN v.localizacao loc
    WHERE v.statusVaga = :status
      AND (
            loc.cidade = :cidade
            OR loc.cidade IS NULL
          )
    ORDER BY v.createdAt DESC
""")
    List<Vaga> buscarVagasParaMatch(
            @Param("status") StatusVaga status,
            @Param("cidade") String cidade
    );


        List<Vaga> findByCategoriaVaga(CategoriaVaga categoriaVaga);

}
