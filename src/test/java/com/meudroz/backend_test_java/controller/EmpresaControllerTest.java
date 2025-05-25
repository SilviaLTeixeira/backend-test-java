package com.meudroz.backend_test_java.controller;

import com.meudroz.backend_test_java.dto.EmpresaResponseDTO;
import com.meudroz.backend_test_java.service.EmpresaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpresaController.class)
class EmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmpresaService service;

    @Test
    void listarEmpresas_deveRetornar200eLista() throws Exception {
        var lista = List.of(
                new EmpresaResponseDTO("A", "12345678000112", "R1", "111"),
                new EmpresaResponseDTO("B", "98765432000198", "R2", "222")
        );
        when(service.listar()).thenReturn(lista);

        mockMvc.perform(get("/empresas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome").value("A"))
                .andExpect(jsonPath("$[0].cnpj").value("12345678000112"))
                .andExpect(jsonPath("$[1].telefone").value("222"));
    }

    @Test
    void buscarPorCnpj_existente_deveRetornar200ComBody() throws Exception {
        var dto = new EmpresaResponseDTO("X", "12345678000112", "Rua X", "999");
        when(service.buscarPorCnpj("12345678000112")).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/empresas/12345678000112"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("X"))
                .andExpect(jsonPath("$.endereco").value("Rua X"));
    }

    @Test
    void buscarPorCnpj_naoExistente_deveRetornar404() throws Exception {
        when(service.buscarPorCnpj("00000000000000")).thenReturn(Optional.empty());

        mockMvc.perform(get("/empresas/00000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cadastrar_deveRetornar201Created() throws Exception {
        var resp = new EmpresaResponseDTO("Nova", "12345678000112", "End", "333");
        when(service.criar(any())).thenReturn(resp);

        String payload = """
      {
        "nome":"Nova",
        "cnpj":"12345678000112",
        "endereco":"End",
        "telefone":"333"
      }
      """;

        mockMvc.perform(post("/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/empresas/12345678000112"))
                .andExpect(jsonPath("$.cnpj").value("12345678000112"));
    }

    @Test
    void atualizar_existente_deveRetornar204() throws Exception {
        var updated = new EmpresaResponseDTO("Upd", "12345678000112", "RUpd", "444");
        when(service.atualizar(eq("12345678000112"), any()))
                .thenReturn(Optional.of(updated));

        String payload = """
      {
        "nome":"Upd",
        "cnpj":"12345678000112",
        "endereco":"RUpd",
        "telefone":"444"
      }
      """;

        mockMvc.perform(put("/empresas/12345678000112")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNoContent());
    }

    @Test
    void atualizar_naoExistente_deveRetornar404() throws Exception {
        when(service.atualizar(eq("00000000000000"), any()))
                .thenReturn(Optional.empty());

        String payload = """
      {
        "nome":"N",
        "cnpj":"00000000000000"
      }
      """;

        mockMvc.perform(put("/empresas/00000000000000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_existente_deveRetornar204() throws Exception {
        when(service.deletar("123")).thenReturn(true);

        mockMvc.perform(delete("/empresas/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletar_naoExistente_deveRetornar404() throws Exception {
        when(service.deletar("000")).thenReturn(false);

        mockMvc.perform(delete("/empresas/000"))
                .andExpect(status().isNotFound());
    }
}
