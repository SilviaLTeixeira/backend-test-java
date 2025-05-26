# Backend Test Java

Este projeto é uma aplicação **Spring Boot** para cadastro de empresas, desenvolvido como parte do Desafio Técnico da Droz. Ele segue arquitetura em camadas (Model, Repository, Service, Controller), utiliza **DTOs**, **validações** com Bean Validation, tratamento centralizado de exceções, logging e documentação interativa via **Swagger/OpenAPI**.

---

##  Pré-requisitos

* Java 21 (JDK 21)
* Maven 3.9+
* Docker e Docker Compose (opcional para modo Docker)
* MySQL 8 (pode ser local ou via container)

---

##  Variáveis de ambiente

Para não expor credenciais sensíveis, a aplicação utiliza variáveis de ambiente:

| Variável      | Descrição                       | Exemplo |
| ------------- | ------------------------------- | ------- |
| `DB_USERNAME` | Usuário do banco de dados MySQL | `admin` |
| `DB_PASSWORD` | Senha do banco de dados MySQL   | `admin` |

Defina-as no shell:

```bash
export DB_USERNAME="admin"
export DB_PASSWORD="admin"
```

Ou crie um arquivo `.env` na raiz do projeto(pode-se usar o arquivo .env.example como exemplo)

---

##  Executando localmente

1. **Banco de dados**

    * **Opção 1**: MySQL instalado localmente

        * Crie o banco `backend-test-java`.
    * **Opção 2**: Subir apenas o container MySQL(se ja tiver os containers criados):

      ```bash
      
      docker-compose up -d mysql_backend_test_java

      ```


2. **Executar a aplicação**

   ```bash
   mvn clean install
   mvn spring-boot:run -Dspring-boot.run.profiles=local
        ou 
   em run/debug configurations do BackendTestJavaApplication 
   ir em Modify options > add VM options 
   > digitar "-Dspring.profiles.active=local"
   Rodar a aplicação no simbolo do run(triangulo)
   ```

---

## Executando com Docker Compose

Na raiz do projeto, certifique-se de ter o arquivo `.env` com as variáveis de ambiente definidas. Depois:

```bash
docker compose up --build -d
```

Para parar e remover containers:

```bash
docker compose down
```

---

##  Endpoints e Swagger

Após iniciar, acesse:

* **Swagger UI**: `http://localhost:8080/swagger-ui.html`
* **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

Use a interface para explorar e testar a API.

---


## Testes

O projeto inclui testes unitários para:

* **Controllers**: validação de endpoints e tratamento de erros.
* **Services**: regras de negócio.

Execute-os com:

```bash
mvn test
```

---

##  Logs

* Perfil **default** (Docker): nível `INFO` para todos os pacotes.
* Perfil **local**: nível `DEBUG` para `com.meudroz.backend_test_java`.

---



