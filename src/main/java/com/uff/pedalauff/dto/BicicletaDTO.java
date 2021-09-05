package com.uff.pedalauff.dto;

import com.uff.pedalauff.enums.EstadoBicicleta;
import com.uff.pedalauff.modelo.Bicicleta;
import lombok.Data;

@Data
public class BicicletaDTO {
    private Integer idBicicleta;
    private String qrCode;
    private EstadoBicicleta estadoAtual;

    public Bicicleta transformaParaObjeto() {
        return new Bicicleta(idBicicleta, qrCode, estadoAtual);
    }
}
