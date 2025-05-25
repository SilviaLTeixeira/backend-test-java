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

    @NotBlank(message = "{empresa.nome.required}")
    @Size(max = 100, message = "{empresa.nome.size}")
    private String nome;

    @NotBlank(message = "{empresa.cnpj.required}")
    @Pattern(regexp = "\\d{14}", message = "{empresa.cnpj.pattern}")
    private String cnpj;

    @Size(max = 200, message = "{empresa.endereco.size}")
    private String endereco;

    @Size(max = 20, message = "{empresa.telefone.size}")
    private String telefone;
}
