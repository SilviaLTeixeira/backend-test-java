package com.meudroz.backend_test_java.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "EmpresaResponseDTO", description = "Dados de saída de uma empresa já cadastrada")
public class EmpresaResponseDTO {

    @Schema(description = "Nome fantasia da empresa", example = "Acme Ltda")
    private String nome;

    @Schema(description = "CNPJ formatado", example = "12.345.678/0001-12")
    private String cnpj;

    @Schema(description = "Endereço completo da empresa", example = "Rua Exemplo, 123, São Paulo")
    private String endereco;

    @Schema(description = "Telefone de contato", example = "(11)91234-5678")
    private String telefone;
}
