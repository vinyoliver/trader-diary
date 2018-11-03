package com.traderdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Unidade extends ModelBase {

    @NotNull
    @Column(length = 100)
    private String nome;

    @NotNull
    @Column
    private Long codigo;

    @NotNull
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro = LocalDate.now();

    @ManyToOne
    @NotNull
    @JsonIgnore
    private Usuario usuario;

    public Unidade() {
        super();
    }

    public Unidade(String nome, LocalDate dataCadastro) {
        this.nome = nome;
        this.dataCadastro = dataCadastro;
    }
}
