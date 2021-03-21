package com.uff.pedalauff.repo;

import com.uff.pedalauff.modelo.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepo extends CrudRepository<Usuario, Integer> {

}
