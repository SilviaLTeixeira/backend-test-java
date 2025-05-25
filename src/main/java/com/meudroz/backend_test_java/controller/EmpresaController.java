package com.meudroz.backend_test_java.controller;

import com.meudroz.backend_test_java.dto.EmpresaRequestDTO;
import com.meudroz.backend_test_java.dto.EmpresaResponseDTO;
import com.meudroz.backend_test_java.service.EmpresaService;
import com.meudroz.backend_test_java.utils.CnpjUtils;
import com.meudroz.backend_test_java.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
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
  @GetMapping
  public ResponseEntity<List<EmpresaResponseDTO>> listar() {
    log.info("Requisição GET /empresas");
    List<EmpresaResponseDTO> lista = service.listar();
    return ResponseEntity.ok(lista);
  }

  @Operation(summary = "Buscar uma empresa pelo CNPJ")
  @GetMapping("/{cnpj}")
  public ResponseEntity<EmpresaResponseDTO> buscarPorCnpj(@PathVariable String cnpj) {
    log.info("Requisição GET /empresas/{}", cnpj);
    return ResponseUtil.wrapOrNotFound(
            service.buscarPorCnpj(cnpj)
    );
  }

  @Operation(summary = "Cadastrar uma nova empresa")
  @PostMapping
  public ResponseEntity<EmpresaResponseDTO> cadastrar(
          @Valid @RequestBody EmpresaRequestDTO dto,
          UriComponentsBuilder uriBuilder) {
    log.info("Requisição POST /empresas com corpo {}", dto);
    EmpresaResponseDTO criado = service.criar(dto);
    URI location = uriBuilder
            .path("/empresas/{cnpj}")
            .buildAndExpand(CnpjUtils.clean(criado.getCnpj()))
            .toUri();
    return ResponseEntity.created(location).body(criado);
  }

  @Operation(summary = "Atualizar dados de uma empresa pelo CNPJ")
  @PutMapping("/{cnpj}")
  public ResponseEntity<Void> atualizar(
          @PathVariable String cnpj,
          @Valid @RequestBody EmpresaRequestDTO dto) {
    log.info("Requisição PUT /empresas/{}", cnpj);

    boolean updated = service.atualizar(cnpj, dto).isPresent();
    if (updated) {
      return ResponseEntity.noContent().build();
    } else {
      log.warn("Nenhuma empresa encontrada para atualização com CNPJ={}", cnpj);
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Remover uma empresa pelo CNPJ")
  @DeleteMapping("/{cnpj}")
  public ResponseEntity<Void> deletar(@PathVariable String cnpj) {
    log.info("Requisição DELETE /empresas/{}", cnpj);
    boolean removed = service.deletar(cnpj);
    return removed ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
  }
}
