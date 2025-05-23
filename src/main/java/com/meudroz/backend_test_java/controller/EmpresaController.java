package com.meudroz.backend_test_java.controller;

import com.meudroz.backend_test_java.dto.EmpresaRequestDTO;
import com.meudroz.backend_test_java.dto.EmpresaResponseDTO;
import com.meudroz.backend_test_java.service.EmpresaService;
import com.meudroz.backend_test_java.utils.CnpjUtils;
import com.meudroz.backend_test_java.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresas", description = "Endpoints para cadastro e consulta de empresas")
public class EmpresaController {

  private final EmpresaService service;

  public EmpresaController(EmpresaService service) {
    this.service = service;
  }

  @Operation(summary = "Listar todas as empresas")
  @GetMapping
  public ResponseEntity<List<EmpresaResponseDTO>> listar() {
    List<EmpresaResponseDTO> lista = service.listar();
    return ResponseEntity.ok(lista);
  }

  @Operation(summary = "Buscar uma empresa pelo CNPJ")
  @GetMapping("/{cnpj}")
  public ResponseEntity<EmpresaResponseDTO> buscarPorCnpj(@PathVariable String cnpj) {
    return ResponseUtil.wrapOrNotFound(
            service.buscarPorCnpj(cnpj)
    );
  }

  @Operation(summary = "Cadastrar uma nova empresa")
  @PostMapping
  public ResponseEntity<EmpresaResponseDTO> cadastrar(
          @Valid @RequestBody EmpresaRequestDTO dto,
          UriComponentsBuilder uriBuilder) {
    EmpresaResponseDTO criado = service.criar(dto);
    URI location = uriBuilder
            .path("/empresas/{cnpj}")
            .buildAndExpand(CnpjUtils.clean(criado.getCnpj()))
            .toUri();
    return ResponseEntity.created(location).body(criado);
  }

  @Operation(summary = "Atualizar dados de uma empresa pelo CNPJ")
  @PutMapping("/{cnpj}")
  public ResponseEntity<EmpresaResponseDTO> atualizar(
          @PathVariable String cnpj,
          @Valid @RequestBody EmpresaRequestDTO dto) {
    return ResponseUtil.wrapOrNotFound(
            service.atualizar(cnpj, dto)
    );
  }

  @Operation(summary = "Remover uma empresa pelo CNPJ")
  @DeleteMapping("/{cnpj}")
  public ResponseEntity<Void> deletar(@PathVariable String cnpj) {
    boolean removed = service.deletar(cnpj);
    return removed ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
  }
}
