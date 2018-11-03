package com.traderdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Objects;
import com.traderdiary.utils.Credentials;
import com.traderdiary.utils.FileUtils;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.text.MessageFormat;

@Data
@Entity
@Table(name = "imagem_operacao")
public class ImagemOperacao extends Anexo {

    @ManyToOne
    @NotNull
    @JsonIgnore
    protected Operacao operacao;

    @Column(length = 300)
    protected String descricao;

    @Column(length = 15, nullable = false, name = "tipo_imagem")
    @Enumerated(EnumType.STRING)
    private TipoImagem tipoImagem;

    public ImagemOperacao() {
    }

    public ImagemOperacao(String filename, String tipo, Operacao operacao, TipoImagem tipoImagem) {
        setNomeFisico(gerarNomeFisico(filename));
        this.operacao = operacao;
        this.tipoImagem = tipoImagem;
    }

    @JsonIgnore
    public String getPath() {
        return super.gerarUrl("usuario/{0}/operacao/{1}", this.getOperacao().getUsuario().getId().toString(), this.getOperacao().getId().toString());
    }

    @JsonIgnore
    public String getPathSmall() {
        final String extension = FileUtils.getExtension(getNomeFisico());
        String url = MessageFormat.format("usuario/{0}/operacao/{1}", this.getOperacao().getUsuario().getId().toString(), this.getOperacao().getId().toString()) + File.separator;
        return url + getNomeFisico().replace(extension, "_small" + extension);
    }


    @JsonInclude
    public String getSrc() {
        return Credentials.AMAZON_S3_URL + getPath();
    }

    public String getSrcSmall() {
        return Credentials.AMAZON_S3_URL + getPathSmall();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId(), this.getNomeFisico());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ImagemOperacao)) {
            return false;
        }
        ImagemOperacao other = (ImagemOperacao) obj;
        if (this.getId() != null && other.getId() != null) {
            return Objects.equal(this.getId(), other.getId());
        }
        return Objects.equal(this.getNomeFisico(), other.getNomeFisico()) && this.getOperacao().equals(other.getOperacao());
    }

}
