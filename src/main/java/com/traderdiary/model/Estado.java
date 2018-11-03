package com.traderdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@JsonIgnoreProperties(value = {"codigoIBGE", "nome", "ativo"})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Estado extends ModelBase implements Comparable<Estado> {

    @Column(name = "codigo_ibge", unique = true)
    private long codigoIBGE;

    @NotNull
    @Column(unique = true, length = 2)
    @Enumerated(EnumType.STRING)
    private UF uf;

    @NotNull
    @Column(length = 100)
    private String nome;

    @Override
    public int compareTo(Estado o) {
        return this.getUf().compareTo(o.getUf());
    }
}
