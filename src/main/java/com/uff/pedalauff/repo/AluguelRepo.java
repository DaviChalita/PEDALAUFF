package com.uff.pedalauff.repo;

import com.uff.pedalauff.modelo.Aluguel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AluguelRepo extends CrudRepository<Aluguel, Integer> {

    @Query("select idAluguel from aluguel where usuarioAlugado.idUsuario = ?1 and dthrDevolucao is null")
    Integer findByIdUsuarioAndBikeNDevolvida(Integer idUsuario);

    @Query("select bicicletaAlugada.idBicicleta from aluguel where idAluguel = ?1")
    Integer findIdBikeByIdAluguel(Integer idAluguel);
}
