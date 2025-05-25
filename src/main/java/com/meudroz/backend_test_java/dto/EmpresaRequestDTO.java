package com.meudroz.backend_test_java.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "EmpresaRequestDTO", description = "Dados enviados para criar/atualizar uma empresa")
public class EmpresaRequestDTO {

    @NotBlank(message = "{empresa.nome.required}")
    @Size(max = 100, message = "{empresa.nome.size}")
    @Schema(description = "Nome fantasia da empresa", example = "Acme Ltda")
    private String nome;

    @NotBlank(message = "{empresa.cnpj.required}")
    @Pattern(regexp = "\\d{14}", message = "{empresa.cnpj.pattern}")
    @Schema(description = "CNPJ sem formatação (apenas dígitos)", example = "12345678000112")
    private String cnpj;

    @Size(max = 200, message = "{empresa.endereco.size}")
    @Schema(description = "Endereço completo da empresa", example = "Rua Exemplo, 123, São Paulo")
    private String endereco;

    @Size(max = 20, message = "{empresa.telefone.size}")
    @Schema(description = "Telefone de contato", example = "(11)91234-5678")
    private String telefone;
}
