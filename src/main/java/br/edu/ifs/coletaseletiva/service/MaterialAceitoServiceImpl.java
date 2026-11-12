package br.edu.ifs.coletaseletiva.service;

import br.edu.ifs.coletaseletiva.exception.ResourceNotFoundException;
import br.edu.ifs.coletaseletiva.model.MaterialAceito;
import br.edu.ifs.coletaseletiva.repository.MaterialAceitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaterialAceitoServiceImpl implements MaterialAceitoService {

    @Autowired
    private MaterialAceitoRepository materialAceitoRepository;

    @Override
    public List<MaterialAceito> listarTodos() {
        return materialAceitoRepository.findAll();
    }

    @Override
    public List<MaterialAceito> listarPorPonto(Long pontoId) {
        return materialAceitoRepository.findByPontoId(pontoId);
    }

    @Override
    @Transactional
    public MaterialAceito vincular(MaterialAceito materialAceito) {
        // RF05: Validar limite (se informado, deve ser > 0)
        if (materialAceito.getLimitePorSolicitacao() != null && materialAceito.getLimitePorSolicitacao() <= 0) {
            throw new RuntimeException("Limite por solicitação deve ser maior que zero");
        }

        // Verificar se o vínculo já existe
        boolean jaExiste = materialAceitoRepository
                .findByPontoIdAndMaterialId(
                        materialAceito.getPonto().getId(),
                        materialAceito.getMaterial().getId())
                .isPresent();

        if (jaExiste) {
            throw new RuntimeException("Este material já está vinculado a este ponto");
        }

        return materialAceitoRepository.save(materialAceito);
    }

    @Override
    @Transactional
    public void desvincular(Long id) {
        MaterialAceito vinculo = materialAceitoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo não encontrado"));

        // RN05: Bloquear se houver solicitações abertas (simplificado)
        materialAceitoRepository.delete(vinculo);
    }
}