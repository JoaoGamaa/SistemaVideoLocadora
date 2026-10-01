# Sistema Video Locadora

Aplicação para gerenciar o acervo de uma locadora, com interface web em Angular e uma API em Spring Boot em desenvolvimento.

## Funcionalidades atuais

- painel inicial com resumo dos cadastros;
- cadastro, edição, busca e exclusão de atores;
- cadastro, edição, busca e exclusão de diretores;
- cadastro, edição, busca e exclusão de classes e seus valores de diária;
- persistência dos cadastros do front-end no `localStorage` do navegador;
- domínio, repositório em memória e serviço de cadastro de atores no back-end.

> O front-end ainda não consome a API. Os dados da interface ficam no navegador, enquanto o back-end mantém os dados apenas em memória durante a execução.

## Tecnologias

- Angular 20, TypeScript e Angular SSR;
- Java 21 e Spring Boot 4;
- Maven Wrapper;
- Jasmine e Karma no front-end;
- JUnit 5 no back-end.

## Estrutura

```text
.
|-- Front-end/   # Aplicacao Angular
`-- back-end/    # API Spring Boot
```

## Pré-requisitos

- Node.js 20.19+ ou 22.12+;
- npm 10+;
- JDK 21+.

Não é necessário instalar Angular CLI nem Maven globalmente.

## Como executar

### Front-end

```bash
cd Front-end
npm ci
npm start
```

Acesse `http://localhost:4200`.

### Back-end

No Linux ou macOS:

```bash
cd back-end
./mvnw spring-boot:run
```

No Windows:

```powershell
cd back-end
.\mvnw.cmd spring-boot:run
```

Por padrão, a aplicação Spring Boot inicia em `http://localhost:8080`.

## Validação

Front-end:

```bash
cd Front-end
npm run build
npm run test:ci
```

Back-end no Linux ou macOS:

```bash
cd back-end
./mvnw test
```

No Windows, substitua `./mvnw` por `.\mvnw.cmd`.

Os mesmos comandos são executados automaticamente pelo workflow de integração contínua em cada `push` e `pull request`.
