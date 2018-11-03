package com.traderdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Operacao extends ModelBase {

    @NotNull
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Finalidade finalidade;

    @NotNull
    @Column(length = 10)
    private String papel;

    private long quantidade;

    @Column(name = "preco_entrada")
    @NotNull
    private BigDecimal precoEntrada;

    @Column(name = "preco_saida")
    private BigDecimal precoSaida;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operacao")
    @OrderBy("data desc")
    private List<StopLoss> stopLoss = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operacao")
    @OrderBy("data desc")
    private List<StopGain> stopGain = new ArrayList<>();

    @Column(length = 1000, name = "descricao_inicio")
    private String descricaoInicio;

    @Column(length = 1000, name = "descricao_termino")
    private String descricaoTermino;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "operacao")
    private List<ImagemOperacao> imagens = new ArrayList<>();

    @ManyToOne
    @NotNull
    @JsonIgnore
    private Usuario usuario;

    @Column(name = "data_inicio")
    @NotNull
    private LocalDateTime dataInicio;

    @Column(name = "data_termino")
    private LocalDateTime dataTermino;

    @NotNull
    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private Setup setup;

    @Column(name = "data_cadastro")
    @NotNull
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @NotNull
    private Long codigo;

    private boolean encerrada;

    @Transient
    private BigDecimal stop;

    @Transient
    private BigDecimal target;


    public Operacao() {
    }

    public void updateStopLossOrGain() {
        if (this.isNovo() || stop.compareTo(stopLoss.get(0).getValor()) != 0) {
            addStopLoss(stop);
        }
        if (this.isNovo() || target.compareTo(stopGain.get(0).getValor()) != 0) {
            addStopGain(target);
        }
    }

    public void addStopLoss(BigDecimal value) {
        stopLoss.add(new StopLoss(this, value));
    }

    public void addStopGain(BigDecimal value) {
        stopGain.add(new StopGain(this, value));
    }


    @JsonIgnore
    public List<ImagemOperacao> getImagens() {
        return this.imagens;
    }

    @JsonProperty
    public void setImagens(List<ImagemOperacao> imagens) {
        this.imagens = imagens;
    }


    public List<ImagemOperacao> getImagensEntrada() {
        return imagens.stream().filter(p -> p.getTipoImagem() == TipoImagem.ENTRADA).collect(Collectors.toList());
    }

    public List<ImagemOperacao> getImagensSaida() {
        return imagens.stream().filter(p -> p.getTipoImagem() == TipoImagem.SAIDA).collect(Collectors.toList());
    }

    @JsonIgnore
    public BigDecimal getValorSaida() {
        if (this.finalidade == Finalidade.COMPRA) {
            return precoSaida.subtract(precoEntrada);
        } else {
            return precoEntrada.subtract(precoSaida);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId(), this.getPapel());
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Operacao)) {
            return false;
        }
        Operacao other = (Operacao) obj;
        if (this.getId() != null && other.getId() != null) {
            return Objects.equal(this.getId(), other.getId());
        }
        return super.equals(obj);
    }

}
