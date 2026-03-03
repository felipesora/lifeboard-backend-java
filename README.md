<h1>
  <span>
    <img src="docs/logo-lifeboard-branca.png" alt="LifeBoard Logo" width="60" style="vertical-align: middle;" />
  </span>
  <span style="vertical-align: middle;">LifeBoard – Backend (Java + Spring Boot)</span>
</h1>

O **LifeBoard Backend** é uma API REST desenvolvida em **Java 21 + Spring Boot**, responsável por gerenciar autenticação, finanças pessoais, metas financeiras e organização de tarefas. Ele fornece toda a infraestrutura de dados, segurança e regras de negócio utilizadas pelo frontend do LifeBoard.

---

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
- **Spring Security + JWT**
- **Spring Data JPA / Hibernate**
- **PostgreSQL Database**
- **Bean Validation**
- **SpringDoc / Swagger (OpenAPI)**
- **Lombok**
- **JUnit 5 + Mockito**
- **Maven**

---

## 🔐 Principais Funcionalidades

- ✔️ Autenticação com JWT (login, cadastro e proteção de rotas)
- ✔️ Gerenciamento financeiro (saldo, salário, gastos, histórico)
- ✔️ Controle completo de transações (entradas, saídas, aplicação e resgate em metas)
- ✔️ Sistema de metas financeiras com progressão dinâmica
- ✔️ Módulo de tarefas com suporte a Kanban e controle por status
- ✔️ Exportações e integrações preparadas para o frontend
- ✔️ API documentada via Swagger

---

## ⚙️ Configuração do Ambiente

**🔧 Pré-requisitos**

Certifique-se de ter instalado:

- Java 21
- Maven 3.9+
- PostgreSQL Database (local ou container)
- IDE de sua preferência (IntelliJ, Eclipse, VS Code)

---

## 📁 Configuração do application.properties

Arquivo:

`src/main/resources/application.properties`

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

api.security.token.secret=${JWT_SECRET}
```

📌 Observações:

- Configure as variáveis de ambiente conforme sua instalação PostgreSQL.
- Em produção, prefira ddl-auto=validate.
- Use uma chave JWT forte.

---

## ▶️ Como Rodar o Projeto

### 🔹 Via IDE

Abra o projeto e execute a classe principal:

```properties
LifeboardBackendJavaApplication.java
```

### 🔹 Via terminal

```properties
mvn spring-boot:run
```

---

## 🌐 Endpoints e Documentação

Após rodar a aplicação:

**API Principal**

[http://localhost:8080](http://localhost:8080)

**Swagger / OpenAPI**

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🧪 Testes

Testes automatizados com:

- JUnit 5
- Spring Boot Test
- Mockito

Para executar:

```properties
mvn test
```

---

## 🔗 Repositórios Relacionados

### 📘 Documentação Geral do Projeto LifeBoard

Repositório contendo toda a documentação completa do sistema, visão funcional, arquitetura e orientações gerais do projeto:

- 🔗 LifeBoard – Documentação Geral

    [https://github.com/felipesora/LifeBoard](https://github.com/felipesora/LifeBoard)

### 🖥️ Frontend – React + Vite + TypeScript

Repositório do frontend do LifeBoard, responsável pela interface do usuário e integração direta com esta API:

- 🔗 LifeBoard Frontend
    
    [https://github.com/felipesora/lifeboard-frontend](https://github.com/felipesora/lifeboard-frontend)

---

## 👨‍💻 Autor
Desenvolvido por **Felipe Sora**

🔗 GitHub: https://github.com/felipesora

🔗 LinkedIn: https://www.linkedin.com/in/felipesora/