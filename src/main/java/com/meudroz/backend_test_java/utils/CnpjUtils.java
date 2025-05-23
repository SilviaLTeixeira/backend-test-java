package com.meudroz.backend_test_java.utils;

public class CnpjUtils {

    public static String clean(String cnpj) {
        return cnpj == null ? null : cnpj.replaceAll("\\D", "");
    }

    public static String format(String cnpj) {
        String cleaned = clean(cnpj);
        if (cleaned == null || cleaned.length() != 14) return cnpj;
        return cleaned.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }
}
