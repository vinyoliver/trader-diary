package com.traderdiary.wrapper;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.traderdiary.model.Autoridade;
import com.traderdiary.model.Perfil;
import com.traderdiary.model.Unidade;
import com.traderdiary.model.Usuario;

import java.util.List;
import java.util.Set;

public final class UsuarioLogadoWrapper {

    private final Long id;
    private final Long idEmpresa;
    private final Long idUnidadeSelecionada;
    private final String nome;
    private final List<Autoridade> autoridades;
    private final Set<Unidade> unidades;

    @JsonIgnoreProperties(value = {"autoridades"})
    private final Perfil perfil;

    public UsuarioLogadoWrapper(Usuario usuario, List<Autoridade> autoridades) {
        this.nome = usuario.getNome();
        this.autoridades = autoridades;
        this.id = usuario.getId();
        this.idEmpresa = usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null;
        this.unidades = usuario.getUnidades();
        this.idUnidadeSelecionada = null;
        this.perfil = usuario.getPerfil();
    }

    public Long getId() {
        return id;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public List<Autoridade> getAutoridades() {
        return autoridades;
    }

    public Set<Unidade> getUnidades() {
        return unidades;
    }

    public Long getIdUnidadeSelecionada() {
        return idUnidadeSelecionada;
    }

    public Perfil getPerfil() {
        return perfil;
    }
}
