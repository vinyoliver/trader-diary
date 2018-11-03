package com.traderdiary.model;

import com.traderdiary.utils.FileUtils;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.text.MessageFormat;
import java.time.LocalDate;

@MappedSuperclass
@Data
public abstract class Anexo extends ModelBase {

    @NotNull
    @Column(name = "nome_fisico")
    private String nomeFisico;

    @NotNull
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro = LocalDate.now();


    protected String gerarNomeFisico(String fileName) {
        return System.nanoTime() + FileUtils.getExtension(fileName);
    }

    protected String gerarUrl(final String path, String... params) {
        return MessageFormat.format(path, params) + File.separator + getNomeFisico();
    }


    public abstract String getPath();

}
