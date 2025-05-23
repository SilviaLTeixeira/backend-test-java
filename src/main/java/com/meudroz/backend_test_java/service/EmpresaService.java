package com.meudroz.backend_test_java.service;

import com.meudroz.backend_test_java.dto.EmpresaRequestDTO;
import com.meudroz.backend_test_java.dto.EmpresaResponseDTO;
import com.meudroz.backend_test_java.model.Empresa;
import com.meudroz.backend_test_java.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EmpresaService {

    private final EmpresaRepository repository;

    public EmpresaService(EmpresaRepository repository) {
        this.repository = repository;
    }

    public List<EmpresaResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmpresaResponseDTO> buscarPorCnpj(String cnpj) {
        return repository.findByCnpj(cnpj)
                .map(this::toResponseDTO);
    }

    public EmpresaResponseDTO criar(EmpresaRequestDTO dto) {
        Empresa entidade = toEntity(dto);
        Empresa salvo = repository.save(entidade);
        return toResponseDTO(salvo);
    }

    public Optional<EmpresaResponseDTO> atualizar(String cnpj, EmpresaRequestDTO dto) {
        return repository.findByCnpj(cnpj)
                .map(existing -> {
                    existing.setNome(dto.getNome());
                    existing.setEndereco(dto.getEndereco());
                    existing.setTelefone(dto.getTelefone());
                    Empresa updated = repository.save(existing);
                    return toResponseDTO(updated);
                });
    }

    private EmpresaResponseDTO toResponseDTO(Empresa empresa) {
        return new EmpresaResponseDTO(
                empresa.getNome(),
                formatarCnpj(empresa.getCnpj()),
                empresa.getEndereco(),
                empresa.getTelefone()
        );
    }

    private Empresa toEntity(EmpresaRequestDTO dto) {
        Empresa e = new Empresa();
        e.setNome(dto.getNome());
        e.setCnpj(dto.getCnpj().replaceAll("[^0-9]", ""));
        e.setEndereco(dto.getEndereco());
        e.setTelefone(dto.getTelefone());
        return e;
    }

    private String formatarCnpj(String cnpj) {
        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }
}
