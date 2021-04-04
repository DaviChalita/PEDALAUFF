package com.uff.pedalauff.repo;

import com.uff.pedalauff.modelo.Posto;
import org.springframework.data.repository.CrudRepository;

public interface PostoRepo extends CrudRepository<Posto, Integer> {
    Posto findByIdPosto(int idPosto);
}
