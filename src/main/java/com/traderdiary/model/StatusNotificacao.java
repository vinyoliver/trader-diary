package com.traderdiary.model;

public enum StatusNotificacao {

    EM_PROGRESSO("Em Progresso"),
    FINALIZADA("Finalizada");

    private final String descricao;

    StatusNotificacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
