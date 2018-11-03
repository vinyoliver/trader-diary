package com.traderdiary.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notificacao extends ModelBase {

    @Column(length = 100)
    @Enumerated(EnumType.STRING)
    private TipoNotificacao tipo;

    @Column(length = 100)
    @Enumerated(EnumType.STRING)
    private StatusNotificacao status;

    @Column(length = 100)
    private String titulo;

    @NotNull
    @Column(length = 2000)
    private String mensagem;

    @NotNull
    @Column(name = "data_envio")
    private LocalDateTime dataEnvio = LocalDateTime.now();

    @ManyToOne
    private Usuario usuarioEnvio;

    @OneToMany(mappedBy = "notificacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificacaoUsuario> notificacaoUsuarios = new ArrayList<>();

    public String getDescricaoTipo() {
        return tipo != null ? tipo.getDescricao() : null;
    }

    public String getDescricaoStatus() {
        if (status != null)
            return status.getDescricao();
        return null;
    }
}