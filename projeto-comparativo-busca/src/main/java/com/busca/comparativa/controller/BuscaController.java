package com.busca.comparativa.controller;

import com.busca.comparativa.service.BuscaService;
import com.busca.comparativa.service.DatabaseService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "*") // Permite requisições do frontend em qualquer domínio
public class BuscaController {

    private final BuscaService buscaService;
    private final DatabaseService dbService;
    
    // Obtém o número de registros do service
    private final int NUM_REGISTROS;

    public BuscaController(BuscaService buscaService, DatabaseService dbService) {
        this.buscaService = buscaService;
        this.dbService = dbService;
        this.NUM_REGISTROS = dbService.getNumRegistros();
    }

    @GetMapping("/api/comparar-buscas")
    public List<Map<String, String>> compararBuscas() {
        
        Random random = new Random();
        int idAleatorio = random.nextInt(NUM_REGISTROS) + 1;
        String cpfAleatorio = String.format("%09d-00", idAleatorio);
        String emailAleatorio = "usuario_" + idAleatorio + "@mail.com";

        List<Map<String, String>> resultados = new ArrayList<>();

        // 1. Pesquisa Sequencial
        double tempoSeq = buscaService.pesquisaSequencial(idAleatorio);
        resultados.add(Map.of(
            "tipo", "Pesquisa Sequencial (O(N))", 
            "chave", String.valueOf(idAleatorio) + " (ID)", 
            "tempo", String.format("%.3f ms", tempoSeq)
        ));

        // 2. Pesquisa Indexada
        double tempoIndex = buscaService.pesquisaIndexada(cpfAleatorio);
        resultados.add(Map.of(
            "tipo", "Pesquisa Indexada (O(log N))", 
            "chave", cpfAleatorio + " (CPF)", 
            "tempo", String.format("%.3f ms", tempoIndex)
        ));

        // 3. Pesquisa HashMap
        double tempoHash = buscaService.pesquisaHashMap(emailAleatorio);
        resultados.add(Map.of(
            "tipo", "Pesquisa HashMap (O(1))", 
            "chave", emailAleatorio + " (Email)", 
            "tempo", String.format("%.3f ms", tempoHash)
        ));
        
        return resultados;
    }
}