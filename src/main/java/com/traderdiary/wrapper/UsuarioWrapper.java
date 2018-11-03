package com.traderdiary.wrapper;

import com.traderdiary.model.Usuario;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

@Data
public class UsuarioWrapper {

    private Long id;

    private String nomePerfil;

    private String nomeUsuario;

    private boolean selecionado;

    public UsuarioWrapper(Usuario usuario) {
        this.id = usuario.getId();
        this.nomePerfil = usuario.getPerfil().getNome();
        this.nomeUsuario = (usuario.getNome() + " " + ObjectUtils.firstNonNull(usuario.getSobrenome(), "")).trim();
    }

}
