package com.busca.comparativa.service;

import com.busca.comparativa.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BuscaService {

    private final JdbcTemplate jdbcTemplate;
    private final DatabaseService dbService;

    // Estruturas de dados em memória
    private List<Usuario> usuariosList; // Usado para Sequencial (O(N))
    private Map<String, Usuario> usuariosHashMap; // Usado para HashMap (O(1))

    public BuscaService(JdbcTemplate jdbcTemplate, DatabaseService dbService) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbService = dbService;
    }

    @PostConstruct
    public void loadDataIntoMemory() {
        // Carrega todos os registros do DB para a memória.
        usuariosList = dbService.getAllUsuarios(); 
        usuariosHashMap = new HashMap<>();

        // Cria o HashMap usando 'email' como chave
        for (Usuario u : usuariosList) {
            usuariosHashMap.put(u.getEmail(), u);
        }
        System.out.println("HashMap e Lista carregados na memória: " + usuariosList.size() + " registros.");
    }

    public double pesquisaSequencial(int chaveId) {
        long startTime = System.nanoTime();
        // A busca ocorre percorrendo a lista em Java (Simulando O(N))
        for (Usuario u : usuariosList) {
            if (u.getId() == chaveId) {
                break;
            }
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000.0; // ms
    }

    public double pesquisaIndexada(String chaveCpf) {
        long startTime = System.nanoTime();
        
        // A busca usa o índice B-tree no DB (O(log N))
        String sql = "SELECT nome FROM Usuarios WHERE cpf = ?";
        jdbcTemplate.queryForObject(sql, String.class, chaveCpf);

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000.0; // ms
    }

    public double pesquisaHashMap(String chaveEmail) {
        long startTime = System.nanoTime();
        
        // Acesso direto em memória (O(1))
        usuariosHashMap.get(chaveEmail); 
        
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000.0; // ms
    }
}