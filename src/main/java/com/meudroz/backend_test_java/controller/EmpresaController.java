package com.meudroz.backend_test_java.controller;

import com.meudroz.backend_test_java.dto.EmpresaRequestDTO;
import com.meudroz.backend_test_java.dto.EmpresaResponseDTO;
import com.meudroz.backend_test_java.service.EmpresaService;
import com.meudroz.backend_test_java.utils.CnpjUtils;
import com.meudroz.backend_test_java.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresas", description = "Endpoints para cadastro e consulta de empresas")
public class EmpresaController {

  private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);
  private final EmpresaService service;

  public EmpresaController(EmpresaService service) {
    this.service = service;
    log.info("Controlador de Empresa inicializado");
  }

  @Operation(summary = "Listar todas as empresas")
  @ApiResponse(responseCode = "200", description = "Lista de empresas retornada com sucesso")
  @GetMapping
  public ResponseEntity<List<EmpresaResponseDTO>> listar() {
    log.info("Requisição GET /empresas");
    List<EmpresaResponseDTO> lista = service.listar();
    return ResponseEntity.ok(lista);
  }

  @Operation(summary = "Buscar empresa pelo CNPJ", description = "Retorna os dados de uma empresa existente, formatando o CNPJ.")
  @ApiResponse(responseCode = "200", description = "Empresa encontrada")
  @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
  @GetMapping("/{cnpj}")
  public ResponseEntity<EmpresaResponseDTO> buscarPorCnpj(
          @Parameter(description = "CNPJ sem formatação (apenas dígitos)", example = "12345678000112")
          @PathVariable String cnpj
  ) {
    log.info("Requisição GET /empresas/{}", cnpj);
    return ResponseUtil.wrapOrNotFound(
            service.buscarPorCnpj(cnpj)
    );
  }

  @Operation(summary = "Cadastrar nova empresa", description = "Valida e cadastra uma nova empresa.")
  @ApiResponse(responseCode = "201", description = "Empresa cadastrada com sucesso")
  @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados")
  @PostMapping
  public ResponseEntity<EmpresaResponseDTO> cadastrar(
          @Parameter(description = "Dados da empresa a ser criada")
          @Valid @RequestBody EmpresaRequestDTO dto,
          UriComponentsBuilder uriBuilder
  ) {
    log.info("Requisição POST /empresas com corpo {}", dto);
    EmpresaResponseDTO criado = service.criar(dto);
    URI location = uriBuilder
            .path("/empresas/{cnpj}")
            .buildAndExpand(CnpjUtils.clean(criado.getCnpj()))
            .toUri();
    return ResponseEntity.created(location).body(criado);
  }

  @Operation(summary = "Atualizar empresa pelo CNPJ", description = "Atualiza os dados de uma empresa existente.")
  @ApiResponse(responseCode = "204", description = "Empresa atualizada com sucesso")
  @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados")
  @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
  @PutMapping("/{cnpj}")
  public ResponseEntity<Void> atualizar(
          @Parameter(description = "CNPJ sem formatação (apenas dígitos)", example = "12345678000112")
          @PathVariable String cnpj,
          @Parameter(description = "Dados para atualização da empresa")
          @Valid @RequestBody EmpresaRequestDTO dto
  ) {
    log.info("Requisição PUT /empresas/{} com corpo {}", cnpj, dto);
    boolean updated = service.atualizar(cnpj, dto).isPresent();
    if (updated) {
      return ResponseEntity.noContent().build();
    } else {
      log.warn("Tentativa de atualização falhou: nenhum registro encontrado para CNPJ={}", cnpj);
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Remover empresa pelo CNPJ", description = "Deleta uma empresa existente pelo CNPJ.")
  @ApiResponse(responseCode = "204", description = "Empresa removida com sucesso")
  @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
  @DeleteMapping("/{cnpj}")
  public ResponseEntity<Void> deletar(
          @Parameter(description = "CNPJ sem formatação (apenas dígitos)", example = "12345678000112")
          @PathVariable String cnpj
  ) {
    log.info("Requisição DELETE /empresas/{}", cnpj);
    boolean removed = service.deletar(cnpj);
    if (removed) {
      return ResponseEntity.noContent().build();
    } else {
      log.warn("Tentativa de remoção falhou: nenhum registro encontrado para CNPJ={}", cnpj);
      return ResponseEntity.notFound().build();
    }
  }
}
