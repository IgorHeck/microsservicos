# Microsservicos Projeto

## Descrição
Este projetoบริหาร three serviços independentes conectados por uma fila RabbitMQ para processamento assínrono de mensagens. Cada serviço foi projetado para rodar em contéiner Docker, permitindo escalabilidade e isolamento de dependências.

## Serviços
1. **Serviço A**
   - Finalidade: [Descrever função]
   - Tecnologias: [Ex: Node.js, Python, etc.]

2. **Serviço B**
   - Finalidade: [Descrever função]
   - Tecnologias: [Ex: Java, Go, etc.]

3. **Serviço C**
   - Finalidade: [Descrever função]
   - Tecnologias: [Ex: .NET, Rust, etc.]

## Comunicação entre Serviços
- Utiliza RabbitMQ para mensagens assínronas
- Exemplo: Serviço A envia mensagens para a fila `tarefas-pendentes`, que Sério B e C consomem

## Requisitos para Execução
- Docker Gateway instalado
- Banco de dados (se houver)
- RabbitMQ Server

## Como Rodar
1. **Inicializar Docker**
   ```bash
   docker-compose up -d
   ```
2. **Verificar Status**
   ```bash
   docker-compose ps
   ```
3. **Testar Comunicação**
   - Enviar mensagem de teste via CLI ou API de um serviço

## Como Funciona
- Quando uma tarefa é criada, o cliente envia uma mensagem para RabbitMQ
- Serviço A escuta a fila, processa a tarefa e guarda no banco de dados
- Serviços B e C consumem mensagens da fila de acordo com sua lógica de negocio
- Respostas são armazenadas e disponibilizadas quando necessário

## Dependências Adicionais
- [Listar quaisquer dependências de pacotes ou libs específicas]