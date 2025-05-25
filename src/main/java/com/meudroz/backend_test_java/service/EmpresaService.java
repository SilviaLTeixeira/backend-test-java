package com.meudroz.backend_test_java.service;

import com.meudroz.backend_test_java.dto.EmpresaRequestDTO;
import com.meudroz.backend_test_java.dto.EmpresaResponseDTO;
import com.meudroz.backend_test_java.model.Empresa;
import com.meudroz.backend_test_java.repository.EmpresaRepository;
import com.meudroz.backend_test_java.utils.CnpjUtils;
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
        String cleanCnpj = CnpjUtils.clean(cnpj);
        return repository.findByCnpj(cleanCnpj)
                .map(this::toResponseDTO);
    }

    public EmpresaResponseDTO criar(EmpresaRequestDTO dto) {
        Empresa entidade = toEntity(dto);
        Empresa salvo = repository.save(entidade);
        return toResponseDTO(salvo);
    }

    public Optional<EmpresaResponseDTO> atualizar(String cnpj, EmpresaRequestDTO dto) {
        String cleanCnpj = CnpjUtils.clean(cnpj);
        return repository.findByCnpj(cleanCnpj)
                .map(existing -> {
                    existing.setNome(dto.getNome());
                    existing.setEndereco(dto.getEndereco());
                    existing.setTelefone(dto.getTelefone());
                    Empresa updated = repository.save(existing);
                    return toResponseDTO(updated);
                });
    }

    public boolean deletar(String cnpj) {
        String cleanCnpj = CnpjUtils.clean(cnpj);
        Optional<Empresa> empresa = repository.findByCnpj(cleanCnpj);
        if (empresa.isPresent()) {
            repository.delete(empresa.get());
            return true;
        }
        return false;
    }

    private EmpresaResponseDTO toResponseDTO(Empresa empresa) {
        return new EmpresaResponseDTO(
                empresa.getNome(),
                CnpjUtils.format(empresa.getCnpj()),
                empresa.getEndereco(),
                empresa.getTelefone()
        );
    }

    private Empresa toEntity(EmpresaRequestDTO dto) {
        Empresa e = new Empresa();
        e.setNome(dto.getNome());
        e.setCnpj(CnpjUtils.clean(dto.getCnpj()));
        e.setEndereco(dto.getEndereco());
        e.setTelefone(dto.getTelefone());
        return e;
    }
}
