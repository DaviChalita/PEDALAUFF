package com.uff.pedalauff.repo;

import com.uff.pedalauff.modelo.Vaga;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VagaRepo extends CrudRepository<Vaga, Integer> {
    Vaga findByQrCode(String qrCode);

    @Query(value = "select p.idPosto, count(v.disponibilidade) as quantidadeVaga from vaga v join posto p " +
            "on v.posto.idPosto = p.idPosto where v.disponibilidade = True group by p.idPosto")
    Integer qtdVagasDisp(Integer idPosto);

}
