package com.traderdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class Stop extends ModelBase {

    @NotNull
    private BigDecimal valor;

    @NotNull
    private LocalDateTime data;

    @ManyToOne
    @JsonIgnore
    @NotNull
    private Operacao operacao;

//	@NotNull
//	@Column(length = 20)
//	@Enumerated(EnumType.STRING)
//	private TipoStop tipo;

    public Stop() {
    }

    public Stop(Operacao operacao, BigDecimal valor) {
        this.operacao = operacao;
        this.data = LocalDateTime.now();
        this.valor = valor;
    }
}
