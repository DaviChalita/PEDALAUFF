package com.uff.pedalauff.repo;

import com.uff.pedalauff.modelo.Aluguel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AluguelRepo extends CrudRepository<Aluguel, Integer> {
    @Query("select idAluguel from aluguel where usuarioAlugado.idUsuario = ?1 and bicicletaAlugada.idBicicleta = ?2")
    Integer findByIdUsuarioAndIdBicicleta(Integer idUsuario, Integer idBicileta);
}
