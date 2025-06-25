package com.lifeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lifeboard.model.Financeiro;

@JsonPropertyOrder({ "id_usuario", "nome", "email", "senha", "financeiro"})
public class UsuarioResponseDTO {

    @JsonProperty("id_usuario")
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private FinanceiroResponseDTO financeiro;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Long id, String nome, String email, String senha, FinanceiroResponseDTO financeiro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.financeiro = financeiro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public FinanceiroResponseDTO getFinanceiro() {
        return financeiro;
    }

    public void setFinanceiro(FinanceiroResponseDTO financeiro) {
        this.financeiro = financeiro;
    }
}
