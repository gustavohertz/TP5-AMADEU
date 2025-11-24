package org.example.service;

import org.example.funcionario.*;
import org.example.funcionario.validacao.ValidadorFuncionario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository repository;

    // Spy cria uma lista real onde podemos adicionar mocks se necessário
    @Spy
    private List<ValidadorFuncionario> validadores = new ArrayList<>();

    @InjectMocks
    private FuncionarioService service;

    @Test
    void deveSalvarFuncionarioQuandoValidacaoPassar() {
        Funcionario f = new Funcionario();
        f.setNome("Teste");

        when(repository.save(any(Funcionario.class))).thenReturn(f);

        Funcionario salvo = service.salvar(f);

        assertNotNull(salvo);
        verify(repository, times(1)).save(f);
    }

    @Test
    void deveFalharQuandoValidadorLancarExcecao() {
        Funcionario f = new Funcionario();

        // Simula um validador que falha
        ValidadorFuncionario validadorMock = mock(ValidadorFuncionario.class);
        doThrow(new RuntimeException("Erro de validação")).when(validadorMock).validar(any(), any());

        validadores.add(validadorMock); // Adiciona o validador que falha à lista do service

        assertThrows(RuntimeException.class, () -> service.salvar(f));

        // Garante que NADA foi salvo no banco
        verify(repository, never()).save(any());
    }
}