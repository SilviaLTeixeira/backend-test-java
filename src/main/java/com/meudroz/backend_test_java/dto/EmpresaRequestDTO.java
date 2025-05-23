package com.meudroz.backend_test_java.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório.")
    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve ter exatamente 14 dígitos numéricos.")
    private String cnpj;

    @Size(max = 200, message = "O endereço pode ter no máximo 200 caracteres.")
    private String endereco;

    @Size(max = 20, message = "O telefone pode ter no máximo 20 caracteres.")
    private String telefone;
}
