package com.uff.pedalauff.repo;

import com.uff.pedalauff.modelo.Vaga;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VagaRepo extends CrudRepository<Vaga, Integer> {
    Vaga findByQrCode(String qrCode);

    @Query("select disponibilidade from vaga " +
            "where posto.idPosto = ?1 and disponibilidade = true")
    List<Boolean> qtdVagasDisp(Integer idPosto);

    @Query("select disponibilidade from vaga " +
            "where posto.idPosto = ?1 and disponibilidade = false")
    List<Boolean> qtdBicicletasDisp(Integer idPosto);

    @Query("select idVaga from vaga " +
            "where bicicleta.idBicicleta = ?1")
    Integer findByBicicleta(Integer idBicicleta);
    
    @Query("select idVaga from vaga where bicicleta.qrCode = ?1")
    Integer findByBicicletaQrCode(String qrCode);
}
