package com.traderdiary.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "stop_loss")
public class StopLoss extends Stop {

    public StopLoss() {
    }

    public StopLoss(Operacao operacao, BigDecimal valor) {
        super(operacao, valor);
    }

}
