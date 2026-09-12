# Projeto gerenciador de estudos

API REST para gerenciamento de estudos, desenvolvida com Java e Spring Boot.

O projeto permite o gerenciamento de usuários, matérias, sessões de estudo, revisões e metas, utilizando autenticação e autorização baseada em **Spring Security e JWT**.

---

* [Sobre o projeto](#sobre-o-projeto)
* [Tecnologias](#tecnologias)
* [Modelo de dados](#modelo-de-dados)
* [Diagrama de instâncias](#diagrama-de-instâncias)
* [Banco de dados e migrations](#banco-de-dados-e-migrations)
* [Autenticação](#autenticação)
* [Execução com Docker](#execução-com-docker)
* [Swagger / OpenAPI](#swagger--openapi)

---

## Sobre o projeto

O **Study API** é uma API REST desenvolvida para auxiliar no gerenciamento de uma rotina de estudos.

Entre as principais funcionalidades estão:

* Cadastro e gerenciamento de usuários;
* Gerenciamento de matérias;
* Criação e acompanhamento de sessões de estudo;
* Controle de revisões;
* Gerenciamento de metas;
* Controle de acesso baseado em funções;
* Autenticação utilizando JWT.

---

## Tecnologias

* Java
* Spring Boot
* Spring Security
* JWT
* JPA / Hibernate
* Flyway
* Docker
* JUnit
* Mockito

---

## Modelo de dados

![Modelo de Entidade](docs/modelo-entidade.png)

---

## Banco de dados e migrations

A estrutura do banco de dados é versionada utilizando o **Flyway**.

As migrations responsáveis pela criação das tabelas e relacionamentos estão disponíveis em:

[`src/main/resources/db/migration`](src/main/resources/db/migration)

### [`Migration de criação do banco`](src/main/resources/db/migration/V1__create_tables.sql)

### [`Migration de dados iniciais`](src/main/resources/db/testdata/V2__insert_test_data.sql)

---

## Autenticação

A API utiliza **Spring Security e JWT** para autenticação e autorização.

O acesso aos endpoints protegidos requer um token JWT válido.

Para facilitar os testes da API, as collections com as requisições podem ser encontradas em:

* [`Collection HAR`](docs/collection.har)
* [`Collection YAML`](docs/collection.yaml)

### Obtendo o token

Para obter um token, envie uma requisição:

```http
POST /users/login
```

Utilize o seguinte corpo JSON:

```json
{
    "email": "maria@gmail.com",
    "password": "123456"
}
```

A API retornará um token JWT.

Após obter o token, utilize-o nas requisições aos endpoints protegidos através do header:

```http
Authorization: Bearer <token>
```

---

## Execução com Docker

A aplicação está disponível no Docker Hub através da imagem:

```text
wlailson/study-api-backend:1.0.0
```

### Baixar a imagem

```bash
docker pull wlailson/study-api-backend:1.0.0
```

### Executar a aplicação

```bash
docker run -p 8080:8080 wlailson/study-api-backend:1.0.0
```

Após iniciar o container, a API estará disponível em:

```text
http://localhost:8080
```

---

## Swagger / OpenAPI

A API possui documentação interativa através do **Swagger/OpenAPI**.

Após iniciar a aplicação, acesse:

```text
http://localhost:8080/swagger-ui.html
```

O Swagger permite visualizar os endpoints disponíveis, consultar seus parâmetros e realizar requisições diretamente pela interface.

Para acessar endpoints protegidos:

1. Faça login através de `POST /users/login`;
2. Copie o token JWT retornado;
3. Clique em **Authorize** no Swagger;
4. Informe o token JWT.

---
