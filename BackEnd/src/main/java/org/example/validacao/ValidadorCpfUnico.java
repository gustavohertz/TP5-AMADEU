package org.example.funcionario.validacao;

import org.example.funcionario.Funcionario;
import org.example.funcionario.FuncionarioRepository;
import org.springframework.stereotype.Component;

@Component // Torna esta classe um Bean gerenciado pelo Spring
public class ValidadorCpfUnico implements ValidadorFuncionario {

    @Override
    public void validar(Funcionario funcionario, FuncionarioRepository repository) {
        if (funcionario.getCpf() == null) return;

        // Busca se existe alguém com este CPF
        repository.findByCpf(funcionario.getCpf())
                .filter(existente -> !existente.getId().equals(funcionario.getId())) // Ignora se for o próprio funcionário (edição)
                .ifPresent(s -> {
                    throw new RuntimeException("CPF já cadastrado no sistema.");
                });
    }
}