package com.traderdiary.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class ModelBaseUnidade extends ModelBase {

    @NotNull
    @ManyToOne
    private Unidade unidade;

    @NotNull
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();

}
