package com.traderdiary.utils;

import br.com.caelum.stella.format.CNPJFormatter;
import br.com.caelum.stella.format.CPFFormatter;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

public class BRUtils {

    private static final CNPJFormatter cnpjFormatter = new CNPJFormatter();

    private static final CPFFormatter cpfFormatter = new CPFFormatter();

    public static void validadorDeCPF(String cpf) throws InvalidStateException {
        CPFFormatter cpfFormatter = new CPFFormatter();
        cpfFormatter.format(cpf);

        CPFValidator cpfValidator = new CPFValidator();
        cpfValidator.assertValid(cpf);
    }

    public static void validadorDeCNPJ(String cnpj) throws InvalidStateException {
        CNPJFormatter cnpjFormatter = new CNPJFormatter();
        cnpjFormatter.format(cnpj);

        CNPJValidator cnpjValidator = new CNPJValidator();
        cnpjValidator.assertValid(cnpj);
    }

    public static String formatCNPJ(String cnpj) {
        return cnpjFormatter.format(cnpj);
    }

    public static String formatCPF(String cpf) {
        return cpfFormatter.format(cpf);
    }

}
