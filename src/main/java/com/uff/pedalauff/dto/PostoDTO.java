package com.uff.pedalauff.dto;

import com.uff.pedalauff.modelo.Posto;
import lombok.Getter;

@Getter
public class PostoDTO {
    private Integer idPosto;
    private String endereco;
    private String campus;

    public Posto transformaParaObjeto() {
        return new Posto(idPosto, endereco, campus);
    }

}
