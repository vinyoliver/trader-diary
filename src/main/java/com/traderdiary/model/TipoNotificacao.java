package com.traderdiary.model;

public enum TipoNotificacao {

    TRADE_PENDENTE("Trade Pendente");

    private final String descricao;

    TipoNotificacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
