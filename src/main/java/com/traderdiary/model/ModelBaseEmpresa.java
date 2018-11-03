package com.traderdiary.model;


import lombok.Data;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@Data
public abstract class ModelBaseEmpresa extends ModelBase {

    @NotNull
    @ManyToOne
    private Empresa empresa;

}
