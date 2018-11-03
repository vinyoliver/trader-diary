package com.traderdiary.model;

import com.google.common.base.Objects;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Empresa extends ModelBase {

    @NotNull
    @Column(length = 100)
    private String nome;

    @NotNull
    @Column(name = "razao_social", length = 100)
    private String razaoSocial;

    @NotNull
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro = LocalDate.now();

    public Empresa() {
    }

    public Empresa(String nome, String razaoSocial, LocalDate dataCadastro) {
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.dataCadastro = dataCadastro;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId(), this.getNome());
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) obj;
        if (this.getId() != null && other.getId() != null) {
            return Objects.equal(this.getId(), other.getId());
        }
        return Objects.equal(this.getNome(), other.getNome())
                && Objects.equal(this.getRazaoSocial(), other.getRazaoSocial());
    }
}
