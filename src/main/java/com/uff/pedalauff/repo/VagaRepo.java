package com.uff.pedalauff.repo;

import com.uff.pedalauff.modelo.Vaga;
import org.springframework.data.repository.CrudRepository;

public interface VagaRepo extends CrudRepository<Vaga, Integer> {
    Vaga findByQrCode(String qrCode);
}
