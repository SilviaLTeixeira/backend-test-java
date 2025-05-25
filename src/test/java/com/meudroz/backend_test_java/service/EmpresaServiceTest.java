package com.meudroz.backend_test_java.service;

import com.meudroz.backend_test_java.dto.EmpresaRequestDTO;
import com.meudroz.backend_test_java.dto.EmpresaResponseDTO;
import com.meudroz.backend_test_java.model.Empresa;
import com.meudroz.backend_test_java.repository.EmpresaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository repository;

    @InjectMocks
    private EmpresaService service;

    @Test
    void listar_deveRetornarTodosComoDTO() {
        Empresa e1 = new Empresa(1L, "A", "12345678000112", "R1", "11");
        Empresa e2 = new Empresa(2L, "B", "98765432000198", "R2", "22");
        when(repository.findAll()).thenReturn(List.of(e1, e2));

        List<EmpresaResponseDTO> dtos = service.listar();

        assertThat(dtos).hasSize(2);
        assertThat(dtos.getFirst().getCnpj()).isEqualTo("12.345.678/0001-12");
        verify(repository).findAll();
    }

    @Test
    void buscarPorCnpj_quandoExistir_deveRetornarDTO() {
        Empresa e = new Empresa(1L, "A", "12345678000112", null, null);
        when(repository.findByCnpj("12345678000112")).thenReturn(Optional.of(e));

        Optional<EmpresaResponseDTO> opt = service.buscarPorCnpj("12345678000112");

        assertThat(opt).isPresent()
                .get()
                .extracting(EmpresaResponseDTO::getNome)
                .isEqualTo("A");
    }

    @Test
    void criar_deveSalvarELimparFormatacaoDoCnpj() {
        EmpresaRequestDTO req = new EmpresaRequestDTO("X", "12.345.678/0001-12", "E", "T");
        Empresa saved = new Empresa(1L, "X", "12345678000112", "E", "T");

        when(repository.save(any())).thenReturn(saved);

        EmpresaResponseDTO dto = service.criar(req);

        assertThat(dto.getCnpj()).isEqualTo("12.345.678/0001-12");
        verify(repository).save(argThat(e -> e.getCnpj().equals("12345678000112")));
    }

    @Test
    void atualizar_quandoNaoExistir_deveRetornarEmpty() {
        when(repository.findByCnpj("000")).thenReturn(Optional.empty());
        Optional<EmpresaResponseDTO> opt = service.atualizar("000", new EmpresaRequestDTO());
        assertThat(opt).isEmpty();
    }
}
