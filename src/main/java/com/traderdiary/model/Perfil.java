package com.traderdiary.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Perfil extends ModelBase {

    @NotNull
    @Column(unique = true, nullable = false, length = 50)
    private String nome;

    @NotNull
    @Column(nullable = false, length = 300)
    private String descricao;

    private boolean visivel;

    @ElementCollection
    @CollectionTable(name = "permissao",
            uniqueConstraints = @UniqueConstraint(columnNames = {"perfil_id", "autoridade"}),
            joinColumns = @JoinColumn(name = "perfil_id"))
    @Column(name = "autoridade", length = 100)
    @Enumerated(EnumType.STRING)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Autoridade> autoridades = new HashSet<>();

}
