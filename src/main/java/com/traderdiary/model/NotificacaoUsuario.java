package com.traderdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notificacao_usuario")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NotificacaoUsuario extends ModelBase {

    @NotNull
    @ManyToOne
    @JsonIgnoreProperties(value = {"autoridades", "perfil.autoridades"})
    private Usuario usuario;

    @Column(name = "data_leitura")
    private LocalDateTime dataLeitura;

    @NotNull
    @ManyToOne
    @JsonIgnoreProperties(value = {"notificacaoUsuarios"})
    private Notificacao notificacao;

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId(), this.getNotificacao(), this.getUsuario());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NotificacaoUsuario)) {
            return false;
        }
        NotificacaoUsuario other = (NotificacaoUsuario) obj;
        if (this.getId() != null && other.getId() != null) {
            return Objects.equal(this.getId(), other.getId());
        }
        return Objects.equal(this.getNotificacao(), other.getNotificacao())
                && Objects.equal(this.getUsuario().getId(), other.getUsuario().getId());
    }

}
