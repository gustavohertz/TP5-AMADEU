package org.example.funcionario.validacao;

import org.example.funcionario.Funcionario;
import org.example.funcionario.FuncionarioRepository;

public interface ValidadorFuncionario {
    // Contrato para todas as regras de validação
    void validar(Funcionario funcionario, FuncionarioRepository repository);
}