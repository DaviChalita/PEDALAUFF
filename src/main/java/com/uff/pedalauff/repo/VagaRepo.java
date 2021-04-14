package com.uff.pedalauff.repo;

import com.uff.pedalauff.modelo.Vaga;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VagaRepo extends CrudRepository<Vaga, Integer> {
    Vaga findByQrCode(String qrCode);

    @Query("select count(disponibilidade) from vaga " +
            "where posto.idPosto = ?1 and disponibilidade = true")
    Integer qtdVagasDisp(Integer idPosto);

}
