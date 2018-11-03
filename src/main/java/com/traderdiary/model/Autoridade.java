package com.traderdiary.model;

import org.springframework.security.core.GrantedAuthority;

public enum Autoridade implements GrantedAuthority {

    ROLE_EDITAR_OPERACAO,
    ROLE_CONSULTAR_OPERACAO,
    ROLE_CONSULTAR_USUARIO,
    ROLE_EDITAR_USUARIO,
    ROLE_SYSTEM_ADMIN,
    ROLE_ACESSAR_PLANO_MANUTENCAO,
    ROLE_CONSULTAR_PERFIL,
    ROLE_EDITAR_PERFIL;

    @Override
    public String getAuthority() {
        return this.toString();
    }
}
