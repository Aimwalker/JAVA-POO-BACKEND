# PDV Posto de Combustível - Backend+frontend

Este projeto é a API de backend para um sistema de Ponto de Venda (PDV) para postos de combustível, desenvolvido em Java com o framework Spring Boot.

## Visão Geral

A API fornece endpoints para gerenciar as principais entidades de um posto de combustível, incluindo:
- Funcionários e Acessos
- Produtos (Combustíveis e Conveniência)
- Clientes e Contas de Crédito ("Fiado")
- Bombas, Tanques e Abastecimentos
- Vendas e Itens de Venda
- Controle de Caixa (Abertura e Fechamento)
- Relatórios e Reemissão de Cupons Fiscais

## Pré-requisitos

Antes de começar, certifique-se de ter os seguintes softwares instalados em sua máquina:
- **Java Development Kit (JDK)**: Versão 17 ou superior.
- **Apache Maven**: Versão 3.8 ou superior.
- **PostgreSQL**: Versão 14 ou superior.
- **Uma IDE Java**: IntelliJ IDEA ou Eclipse.

## 1. Configuração do Banco de Dados

1.  **Instale o PostgreSQL** em sua máquina.
2.  Abra uma ferramenta de gerenciamento de banco de dados (como o DBeaver ou o pgAdmin).
3.  **Crie um novo banco de dados** com o nome `pdv_postocombustivel`.
    ```sql
    CREATE DATABASE pdv_postocombustivel;
    ```
4.  Certifique-se de que o usuário e a senha do seu PostgreSQL estejam acessíveis. O padrão geralmente é `postgres` para o usuário.

## 2. Configuração do Backend

1.  **Clone ou baixe este repositório.**
2.  Abra o arquivo `src/main/resources/application.properties`.
3.  **Verifique e ajuste as configurações do banco de dados** para corresponder às suas credenciais do PostgreSQL:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/pdv_postocombustivel
    spring.datasource.username=postgres
    spring.datasource.password=SUA_SENHA_AQUI
    ```
    Substitua `SUA_SENHA_AQUI` pela senha que você configurou para o usuário `postgres` durante a instalação do PostgreSQL.

## 3. Executando o Backend

Existem duas maneiras principais de executar a aplicação backend:

### Via IDE (Recomendado para Desenvolvimento)

1.  **Abra o projeto** na sua IDE (IntelliJ IDEA ou Eclipse).
2.  Aguarde a IDE baixar todas as dependências do Maven (pode levar alguns minutos na primeira vez).
3.  Localize a classe principal `PdvpostocombustivelApplication.java`.
4.  **Clique com o botão direito** no arquivo e selecione **"Run 'PdvpostocombustivelApplication'"**.
5.  O servidor backend iniciará na porta `8080`. Você verá os logs no console da sua IDE.

### Via Linha de Comando (Maven)

1.  **Abra um terminal** ou prompt de comando.
2.  Navegue até o diretório raiz do projeto backend (`pdvpostocombustivel`).
3.  Execute o seguinte comando Maven para compilar e iniciar a aplicação:
    ```sh
    mvn spring-boot:run
    ```
4.  O servidor backend iniciará na porta `8080`.

## 4. Acessando a Aplicação e a API

-   **API**: A API estará disponível em `http://localhost:8080`.
-   **Documentação da API (Swagger UI)**: Para explorar e testar os endpoints da API visualmente, acesse a seguinte URL no seu navegador:
    [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 5. Executando o Frontend

Para que a aplicação completa funcione, o backend precisa estar em execução. Depois de iniciar o backend, siga os passos para executar o frontend.

1.  **Abra o projeto frontend** (`pdvfrontend`) em uma nova janela da sua IDE.
2.  Aguarde a IDE baixar as dependências do Maven para o projeto frontend.
3.  Localize a classe principal `MainApp.java` (ou a classe que contém o método `main`).
4.  **Clique com o botão direito** no arquivo e selecione **"Run 'MainApp.main()'"**.
5.  A janela de login da aplicação desktop Java Swing será exibida. Você pode usar as credenciais de teste (ex: `gerente@email.com`, senha `123456`) para fazer o login.

---
*Este README foi gerado para auxiliar na execução e configuração do projeto.*
