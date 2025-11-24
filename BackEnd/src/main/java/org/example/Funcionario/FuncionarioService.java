package org.example.funcionario;

import org.example.funcionario.validacao.ValidadorFuncionario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FuncionarioService {

    private static final Logger logger = LoggerFactory.getLogger(FuncionarioService.class);
    private final FuncionarioRepository repository;
    private final List<ValidadorFuncionario> validadores; // Polimorfismo: Lista contendo todas as regras

    // Injeção via Construtor (Favorece Imutabilidade e Testabilidade)
    @Autowired
    public FuncionarioService(FuncionarioRepository repository, List<ValidadorFuncionario> validadores) {
        this.repository = repository;
        this.validadores = validadores;
    }

    public List<Funcionario> listarTodos() {
        return repository.findAll();
    }

    public Funcionario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com ID: " + id));
    }

    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        logger.info("Iniciando validação para criação de funcionário: {}", funcionario.getNome());

        // Executa todas as regras de validação injetadas
        validadores.forEach(v -> v.validar(funcionario, repository));

        return repository.save(funcionario);
    }

    @Transactional
    public Funcionario atualizar(Long id, Funcionario dados) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Funcionário não encontrado");
        }

        dados.setId(id); // Garante a integridade do ID
        logger.info("Validando atualização do funcionário ID: {}", id);

        validadores.forEach(v -> v.validar(dados, repository));

        return repository.save(dados);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Funcionário não encontrado");
        repository.deleteById(id);
    }
}