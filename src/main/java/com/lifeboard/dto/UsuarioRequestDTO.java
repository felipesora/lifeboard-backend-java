package com.lifeboard.dto;

import jakarta.validation.constraints.*;

public class UsuarioRequestDTO {

    @NotNull(message = "Nome é obrigatório.")
    @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 150, message = "A senha deve ter entre 6 e 150 caracteres")
    private String senha;

    public UsuarioRequestDTO() {
    }

    public UsuarioRequestDTO(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
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
}
