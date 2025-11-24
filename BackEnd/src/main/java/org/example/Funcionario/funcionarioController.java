package org.example.funcionario;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    @Autowired private FuncionarioService service;

    @GetMapping
    public List<Funcionario> listar() { return service.listarTodos(); }

    @GetMapping("/{id}")
    public Funcionario buscar(@PathVariable Long id) { return service.buscarPorId(id); }

    @PostMapping
    public Funcionario criar(@Valid @RequestBody Funcionario f) { return service.salvar(f); }

    @PutMapping("/{id}")
    public Funcionario atualizar(@PathVariable Long id, @Valid @RequestBody Funcionario f) {
        return service.atualizar(id, f);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}