package com.traderdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Data
@Entity
@JsonIgnoreProperties(value = {"codigoIBGE", "estado", "ativo"})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Cidade extends ModelBase {

    @Column(name = "codigo_ibge", unique = true)
    private long codigoIBGE;

    @NotNull
    @Column(length = 100)
    private String nome;

    @NotNull
    @ManyToOne
    private Estado estado;

}
