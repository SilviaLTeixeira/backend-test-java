package com.meudroz.backend_test_java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmpresaResponseDTO {

    private String nome;
    private String cnpj;
    private String endereco;
    private String telefone;
}
