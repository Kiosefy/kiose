package com.busca.comparativa.service;

import com.busca.comparativa.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.ArrayList;

@Service
public class DatabaseService {

    private final JdbcTemplate jdbcTemplate;

    // Obtém o número de registros do application.properties
    @Value("${app.num.registros}")
    private int numRegistros;

    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void setupAndPopulate() {
        createTable();
        populateTable();
    }

    private void createTable() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS Usuarios");
        jdbcTemplate.execute("CREATE TABLE Usuarios (" +
                "id INT PRIMARY KEY," +
                "cpf VARCHAR(15) UNIQUE NOT NULL," +
                "nome VARCHAR(100)," +
                "email VARCHAR(100)" +
                ")");
        // Cria o Índice Explícito em CPF para a Pesquisa Indexada
        jdbcTemplate.execute("CREATE INDEX idx_cpf ON Usuarios(cpf)");
    }

    private void populateTable() {
        System.out.println("Popuando a tabela com " + numRegistros + " registros...");
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 1; i <= numRegistros; i++) {
            // ID, CPF, NOME, EMAIL
            batchArgs.add(new Object[]{
                i, 
                String.format("%09d-00", i), 
                "Usuario " + i, 
                "usuario_" + i + "@mail.com"
            });
        }

        jdbcTemplate.batchUpdate("INSERT INTO Usuarios (id, cpf, nome, email) VALUES (?, ?, ?, ?)", batchArgs);
        System.out.println("População concluída.");
    }
    
    // Método para puxar todos os dados (usado para Pesquisa Sequencial/HashMap)
    public List<Usuario> getAllUsuarios() {
        return jdbcTemplate.query("SELECT * FROM Usuarios", (rs, rowNum) ->
            new Usuario(
                rs.getInt("id"),
                rs.getString("cpf"),
                rs.getString("nome"),
                rs.getString("email")
            )
        );
    }
    
    public int getNumRegistros() {
        return numRegistros;
    }
}