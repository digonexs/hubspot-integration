![Meetime](arquivos/imagens/Meetime-logo.png)

# Integração com HubSpot – Desafio Técnico Meetime

Este projeto foi desenvolvido como parte do **desafio técnico da Meetime**, com o objetivo de demonstrar habilidades em integração de APIs externas, boas práticas de desenvolvimento backend e conhecimento sobre protocolos de autenticação.

O desafio propõe a criação de uma aplicação em **Java com Spring Boot** que se integre à API do **HubSpot**, permitindo que um usuário realize a autenticação via **OAuth 2.0**, e, a partir disso, possa:

- Criar contatos em sua conta do HubSpot;
- Listar os contatos já existentes;
- Receber notificações via **Webhooks** quando contatos forem criados ou atualizados.

---

## 🛠️ Tecnologias utilizadas

A aplicação foi desenvolvida utilizando as seguintes tecnologias e bibliotecas:

### Backend
- **Java 21** – Linguagem principal utilizada no projeto
- **Spring Boot** – Framework para desenvolvimento da API REST
- **Spring Web** – Módulo para construção de endpoints RESTful
- **Spring Security** – Utilizado de forma mínima para tratar redirecionamentos
- **Lombok** – Reduz a verbosidade do código com anotações como `@Data` e `@Builder`
- **RestTemplate** – Cliente HTTP para comunicação com a API da HubSpot
- **OpenAPI (SpringDoc)** – Geração automática da documentação Swagger
- **Jakarta Servlet API** – Usada para recebimento e leitura de payloads do Webhook
- **Maven** – Gerenciador de dependências e build do projeto

### Segurança e autenticação
- **OAuth 2.0** – Protocolo utilizado para autenticação via HubSpot
- **Bearer Token** – Utilizado para autenticar requisições à API da HubSpot
- **application-example.properties** – Arquivo de exemplo para configuração segura

### Testes manuais e validação
- **Postman** – Utilizado extensivamente para testes manuais de todos os endpoints
- **Swagger UI** – Interface interativa para testar e visualizar os endpoints da API

### Outros
- **Ngrok** – Túnel HTTP para expor a aplicação local publicamente e permitir testes com Webhooks da HubSpot
- **Git** – Versionamento de código e controle de histórico
- **GitHub** – Repositório remoto público para entrega do desafio

---

## ✅ Funcionalidades implementadas

A aplicação atende todos os requisitos propostos no desafio, incluindo:

- **Autenticação OAuth 2.0 com o HubSpot**
- **Criação de contatos no HubSpot**
- **Listagem de contatos cadastrados**
- **Recebimento de Webhooks**
- **Tratamento de erros**
- **Documentação Swagger**
- **Testes manuais documentados**

---

## 💻 Como rodar o projeto localmente

### Pré-requisitos

- Java 21
- Git
- IntelliJ IDEA
- Ngrok
- Postman

### 1. Criar uma conta e aplicativo no HubSpot

1. Crie conta em: https://developers.hubspot.com
2. Crie um App com Redirect URL: `http://localhost:8080/oauth/callback`
3. Escopos: `crm.objects.contacts.read`, `crm.objects.contacts.write`, `crm.schemas.contacts.write`, `oauth`
4. Copie o Client ID e Client Secret

### 2. Clonar o repositório

```bash
git clone https://github.com/digonexs/hubspot-integration
```
```bash
cd hubspot-integration
```
### 3. Configurar o application.properties

1. Copie `application-example.properties` como `application.properties`
2. Preencha com seu client.id e client.secret:

```properties
hubspot.oauth.client.id=SEU_CLIENT_ID
hubspot.oauth.client.secret=SEU_CLIENT_SECRET
hubspot.oauth.redirect.uri=http://localhost:8080/oauth/callback
hubspot.oauth.scope=crm.objects.contacts.read crm.objects.contacts.write crm.schemas.contacts.write
```

### 4. Executar o projeto

```bash
./mvnw clean install
```

```bash
./mvnw spring-boot:run
```

### 5. Acessar Swagger

```
http://localhost:8080/swagger-ui.html
```

### 6. Ngrok para Webhooks

1. Baixe e instale https://ngrok.com
2. Autentique: `ngrok config add-authtoken SEU_TOKEN`
3. Rode: `ngrok http 8080`
4. Copie a URL e configure no app da HubSpot: `https://xxx.ngrok.io/webhook/contact`

---

## ✅ Testes manuais e critérios de aceite

### Autenticar no HubSpot

1. Acesse `http://localhost:8080/oauth/authorize-url` no navegador
2. Copie a URL gerada, cole em uma novaguia e autorize o app
3. Veja o access_token no terminal

### Como usar o token

```
Authorization: Bearer SEU_ACCESS_TOKEN
```

### Testes

| # | Cenário | Método | Endpoint |
|--|---------|--------|----------|
| 1 | Gerar URL de autorização | GET | `/oauth/authorize-url` |
| 2 | Callback (code → token) | GET | `/oauth/callback` |
| 3 | Criar contato válido | POST | `/oauth/contacts` |
| 4 | Criar contato inválido | POST | `/oauth/contacts` |
| 5 | Criar contato com campos nulos | POST | `/oauth/contacts` |
| 6 | Criar contato sem token | POST | `/oauth/contacts` |
| 7 | Listar contatos com token | GET | `/oauth/contacts` |
| 8 | Listar contatos sem token | GET | `/oauth/contacts` |
| 9 | Webhook: criação de contato | POST (HubSpot) | `/webhook/contact` |
| 10 | Webhook: alteração de contato | POST (HubSpot) | `/webhook/contact` |

---

### Coleção Postman

(`/arquivos/json_collections/Meetime HubSpot Integration.postman_collection.json`)

- Para facilitar os testes da API, incluímos uma **coleção do Postman pronta** com todos os endpoints e exemplos de requisições.

#### Como importar para o Postman:
1. Abra o Postman
2. Vá para **File > Import**
3. Clique em **Upload Files**
4. Selecione o arquivo `.json` localizado no caminho acima
5. Clique em **Import**

#### O que está incluso na coleção:
- Criação de contatos com diferentes cenários (válido, inválido, sem token, campos nulos)
- Listagem de contatos (com/sem token)
- Simulação de Webhooks de criação e alteração de contato

---

Pronto! A aplicação está pronta para testes reais com OAuth, API do HubSpot e Webhooks.

---

## 💻 Documentação Técnica

#### Decisões Técnicas:

Durante o desenvolvimento deste projeto, tomei as seguintes decisões:

- **Clareza e simplicidade**: O objetivo foi manter o código legível e de fácil manutenção.
- **Separação de responsabilidades**: Aplicamos a arquitetura em camadas (`Controller`, `Service`, `DTO`) para manter a organização e facilitar futuras evoluções.

#### Tecnologias e Bibliotecas Utilizadas:

| Tecnologia | Motivo                                                                          |
|------------|---------------------------------------------------------------------------------|
| **Spring Boot** | Framework principal para construção da API REST                                 |
| **Spring Web** | Permite a criação de endpoints HTTP RESTful                                     |
| **Spring Validation** | Usado para validar os dados dos DTOs (como email e campos obrigatórios)         |
| **Springdoc OpenAPI** | Geração automática da documentação Swagger da API                               |
| **Lombok** | Redução de boilerplate com geração automática de getters/setters e construtores |
| **Ngrok** | Criação de túnel público para testes de Webhook local                           |
| **Postman** | Plataforma de testes da API com diversos cenários mapeados                      |


#### Possíveis Melhorias Futuras:

- **Centralizar tratamento de exceções** compatível com Swagger.
- **Persistência em banco local** para guardar tokens e log de contatos criados.
- **Renovação automática de token** (usando refresh token quando disponível).
- **Testes automatizados** de integração e cobertura de endpoints.
- **Melhoria de documentação Swagger**, com exemplos de payloads nos endpoints.

---




