# Projeto Microsserviços – Pedidos & Notificação

## Visão geral
Este repositório contém **três micro‑serviços** independentes que se comunicam via **fila RabbitMQ** e que fazem parte de um pipeline de processamento de pedidos:

| Serviço | Descrição | Tecnologias principais |
|---------|-----------|------------------------|
| **pedidos‑api** | API REST exposição CRUD de `Pedido`, `Produto` e `ItemPedido` | Spring Boot 3.5, Spring AMQP, Spring Web, Lombok, Postman (para testes), Java 21 |
| **processador** | Consumidor da fila, processa pedidos recebidos pela API, armazena em **PostgreSQL** | Spring Boot 3.5, Spring Data JPA, Spring AMQP, PostgreSQL, Jackson |
| **notificacao** | Consumidor de eventos, envia **e‑mails** de notificação (usando Spring Mail + MailHog para testes) | Spring Boot 3.5, Spring AMQP, Spring Mail, Lombok |

A comunicação entre os serviços ocorre apenas através do RabbitMQ, garantindo isolamento e escalabilidade.

---

## Requisitos de execução

* Docker 24+ (incluindo Docker Compose)
* (Opcional) **Java 21** instalado para execução local (sem Docker)

---

## Como rodar o projeto com Docker

Na raiz do repositório execute:

```bash
docker compose up -d
```

Os contêineres criados:

```
| Container              | Porta exposta |
|------------------------|----------------|
| rabbitmq               | 15672 (mgmt), 5672 (AMQP) |
| mailhog                | 8025 (UI), 1025 (SMTP) |
| postgresql-15.7        | 5432 |
| pedidos-api            | 8080 |
| processador            | 8081 |
| notificacao            | 8082 |
```

**Verificar status**
```bash
docker compose ps
```

**Parar**
```bash
docker compose down
```

### Testando a API via Swagger
Depois que os containers estiverem ativos, a documentação automática do SpringDoc fica disponível em:

```
http://localhost:8080/swagger-ui.html    # pedidos‑api
http://localhost:8081/swagger-ui.html    # processador (exibe apenas o consumer)
http://localhost:8082/swagger-ui.html    # notificacao (exibe apenas o consumer)
```

### Enviando um pedido
```http
POST http://localhost:8080/pedidos
Content-Type: application/json

{
  "nomeCliente": "Ana Silva",
  "itens": [
    { "produtoId": 1, "quantidade": 2 },
    { "produtoId": 3, "quantidade": 1 }
  ]
}
```
O serviço `pedidos‑api` grava o pedido no banco interno e então publica uma mensagem RabbitMQ. O `processador` consome essa mensagem, atualiza o status, grava no PostgreSQL e publica um evento de confirmação que o serviço `notificacao` consome e gera o e‑mail (visualizado em MailHog em `http://localhost:8025`).

---

## Estrutura de pastas

```
microsservicos/
├─ docker-compose.yml
├─ pedidos-api/
│   ├─ src/ (Spring Boot API)
├─ processador/
│   ├─ src/ (Spring Boot consumer + JPA)
├─ notificacao/
│   ├─ src/ (Spring Boot consumer + mail)
└─ README.md
```

---

## Observações importantes

* **Banco de dados** – O `processador` cria as tabelas automaticamente (Hibernate JPA). Se precisar de dados seed, faça um script SQL na pasta `docker-build/seed` ou `src/main/resources/data.sql`.
* **RabbitMQ** – O tópico padrão usado é `pedidos.queue`. Alterar a rota ou a chave de binding requer atualizar `application.properties` nos três projetos.
* **MailHog** – Forma prática de testar e-mails sem enviar para o mundo real. Neste ambiente de desenvolvimento, o serviço de e‑mail do `notificacao` está configurado para usar o SMTP do MailHog.
* **Logs** – Todos os serviços têm log em `stdout`, acessível via `docker compose logs -f <container>`.

---

## Próximos passos recomendados

1. Implementar **compensação** em `processador` caso falhe ao salvar em PostgreSQL.
2. Adicionar escalabilidade horizontal: executar múltiplas réplicas do `processador` / `notificacao` com regras de queuing.
3. Criar testes de integração CI usando GitHub Actions e a imagem `microsservicos/ci-tests`.

---

## Suporte e documentação adicional

Veja os `pom.xml` de cada módulo para entender as dependências específicas nos diretórios `pedidos-api`, `processador` e `notificacao`. O arquivo `docker-compose.yml` já contém todas as volumes necessários para persistência de dados.

---
