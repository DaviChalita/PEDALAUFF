package com.uff.pedalauff.consts;

public final class PedalaUffConstants {

    private PedalaUffConstants(){
        throw new IllegalStateException("Classe de utilidade");
    }

    public static final String LOGAR_NO_SITE = "Você não possui acesso para realizar tal ação.";
    public static final StringBuilder LOGAR_NO_SITE_BUILDER = new StringBuilder(LOGAR_NO_SITE);
    public static final String ADMIN = "ADMIN";
}
