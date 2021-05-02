package com.uff.pedalauff.repo;

import com.uff.pedalauff.modelo.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepo extends CrudRepository<Usuario, Integer> {

    Usuario findByEmail(String email);

    Usuario findBySenha(String senha);

    @Query("select idUsuario from usuario where email like ?1 and senha like ?2")
    Integer findByEmailAndSenha(String email, String senha);

    @Query("select idAluguel from aluguel where usuarioAlugado.idUsuario = ?1 and dthrDevolucao is NULL")
    int checkBicicletaNDevolvida(Integer idUsuario);
}