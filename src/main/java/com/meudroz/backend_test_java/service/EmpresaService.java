package com.meudroz.backend_test_java.service;

import com.meudroz.backend_test_java.dto.EmpresaRequestDTO;
import com.meudroz.backend_test_java.dto.EmpresaResponseDTO;
import com.meudroz.backend_test_java.model.Empresa;
import com.meudroz.backend_test_java.repository.EmpresaRepository;
import com.meudroz.backend_test_java.utils.CnpjUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    private static final Logger log = LoggerFactory.getLogger(EmpresaService.class);

    private final EmpresaRepository repository;

    public EmpresaService(EmpresaRepository repository) {

        this.repository = repository;
        log.info("Serviço de Empresa inicializado");
    }

    public List<EmpresaResponseDTO> listar() {
        log.info("Listando todas as empresas");
        List<EmpresaResponseDTO> lista = repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
        log.debug("Total de empresas encontradas: {}", lista.size());
        return lista;
    }

    public Optional<EmpresaResponseDTO> buscarPorCnpj(String cnpj) {
        log.info("Buscando empresa pelo CNPJ={}", cnpj);

        String cleanCnpj = CnpjUtils.clean(cnpj);
        log.debug("CNPJ limpo para busca: {}", cleanCnpj);

        return repository.findByCnpj(cleanCnpj)
                .map(this::toResponseDTO)
                .map(dto -> {
                    log.debug("Empresa encontrada: {}", dto);
                    return dto;
                });
    }

    public EmpresaResponseDTO criar(EmpresaRequestDTO dto) {
        log.info("Criando nova empresa: {}", dto);

        Empresa entidade = toEntity(dto);
        Empresa salvo = repository.save(entidade);
        log.debug("Empresa persistida com ID={}", salvo.getId());
        return toResponseDTO(salvo);
    }

    public Optional<EmpresaResponseDTO> atualizar(String cnpj, EmpresaRequestDTO dto) {
        log.info("Atualizando empresa CNPJ={} com dados {}", cnpj, dto);

        String cleanCnpj = CnpjUtils.clean(cnpj);
        return repository.findByCnpj(cleanCnpj)
                .map(existing -> {
                    existing.setNome(dto.getNome());
                    existing.setEndereco(dto.getEndereco());
                    existing.setTelefone(dto.getTelefone());
                    Empresa updated = repository.save(existing);
                    log.debug("Empresa atualizada: {}", updated);
                    return toResponseDTO(updated);
                });
    }

    public boolean deletar(String cnpj) {
        log.info("Removendo empresa com CNPJ={}", cnpj);

        String cleanCnpj = CnpjUtils.clean(cnpj);
        Optional<Empresa> empresa = repository.findByCnpj(cleanCnpj);
        if (empresa.isPresent()) {
            repository.delete(empresa.get());
            log.debug("Empresa removida: {}", empresa.get());
            return true;
        }
        log.warn("Nenhuma empresa encontrada para remoção com CNPJ={}", cnpj);
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
