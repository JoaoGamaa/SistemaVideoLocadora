# Sistema de Videolocadora

[![CI](https://github.com/JoaoGamaa/SistemaVideoLocadora/actions/workflows/ci.yml/badge.svg)](https://github.com/JoaoGamaa/SistemaVideoLocadora/actions/workflows/ci.yml)

Aplicação full stack para gerenciar os cadastros básicos de uma videolocadora. O projeto possui uma interface Angular, uma API REST com Spring Boot e persistência local em banco H2.

## Funcionalidades

- CRUD completo de atores;
- CRUD completo de diretores;
- CRUD completo de classes e valores de diária;
- CRUD completo de títulos, vinculados a diretor e classe;
- CRUD completo dos itens físicos do acervo;
- busca nas listagens;
- atualização manual dos dados pela API;
- validação de campos obrigatórios e nomes duplicados;
- mensagens de sucesso e erro na interface;
- painel inicial com o total de registros;
- persistência dos dados entre reinicializações do backend.

## Tecnologias

### Frontend

- Angular 20;
- TypeScript 5.9;
- Angular Forms;
- Angular HttpClient;
- RxJS.

### Backend

- Java 21;
- Spring Boot 4.1;
- Spring Web MVC;
- Spring Data JPA;
- Hibernate;
- Maven Wrapper;
- H2 Database.

## Estrutura do projeto

```text
SistemaVideoLocadora/
├── Front-end/   # aplicação Angular
├── back-end/    # API REST Spring Boot
└── .github/     # workflow de integração contínua
```

O backend segue a separação em camadas:

```text
controller → service → repository → H2
```

## Pré-requisitos

- Java 21;
- Node.js 22.12 ou superior;
- npm 10 ou superior.

Não é necessário instalar Maven globalmente, pois o projeto inclui o Maven Wrapper.

## Executando o projeto

### 1. Backend

No PowerShell:

```powershell
cd back-end
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
cd back-end
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

A documentação interativa estará disponível em:

```text
http://localhost:8080/swagger-ui.html
```

O documento OpenAPI em JSON poderá ser consultado em:

```text
http://localhost:8080/v3/api-docs
```

### 2. Frontend

Em outro terminal:

```powershell
cd Front-end
npm install
npm start
```

A aplicação ficará disponível em `http://localhost:4200`.

## Endpoints da API

| Recurso | Método | Endpoint | Operação |
| --- | --- | --- | --- |
| Atores | GET | `/api/atores` | Listar atores |
| Atores | GET | `/api/atores/{id}` | Consultar ator |
| Atores | POST | `/api/atores` | Cadastrar ator |
| Atores | PUT | `/api/atores/{id}` | Atualizar ator |
| Atores | DELETE | `/api/atores/{id}` | Excluir ator |
| Diretores | GET | `/api/diretores` | Listar diretores |
| Diretores | GET | `/api/diretores/{id}` | Consultar diretor |
| Diretores | POST | `/api/diretores` | Cadastrar diretor |
| Diretores | PUT | `/api/diretores/{id}` | Atualizar diretor |
| Diretores | DELETE | `/api/diretores/{id}` | Excluir diretor |
| Classes | GET | `/api/classes` | Listar classes |
| Classes | GET | `/api/classes/{id}` | Consultar classe |
| Classes | POST | `/api/classes` | Cadastrar classe |
| Classes | PUT | `/api/classes/{id}` | Atualizar classe |
| Classes | DELETE | `/api/classes/{id}` | Excluir classe |
| Títulos | GET | `/api/titulos` | Listar títulos |
| Títulos | GET | `/api/titulos/{id}` | Consultar título |
| Títulos | POST | `/api/titulos` | Cadastrar título |
| Títulos | PUT | `/api/titulos/{id}` | Atualizar título |
| Títulos | DELETE | `/api/titulos/{id}` | Excluir título |
| Itens | GET | `/api/itens` | Listar itens |
| Itens | GET | `/api/itens/{id}` | Consultar item |
| Itens | POST | `/api/itens` | Cadastrar item |
| Itens | PUT | `/api/itens/{id}` | Atualizar item |
| Itens | DELETE | `/api/itens/{id}` | Excluir item |

Exemplo de cadastro de ator:

```json
{
  "nome": "Fernanda Montenegro"
}
```

Exemplo de cadastro de classe:

```json
{
  "nome": "Lançamento",
  "valorDiaria": 12.5
}
```

Exemplo de cadastro de título:

```json
{
  "nome": "Central do Brasil",
  "anoLancamento": 1998,
  "genero": "Drama",
  "diretor": { "id": 1 },
  "classe": { "id": 1 }
}
```

Exemplo de cadastro de item:

```json
{
  "numeroSerie": "DVD-0001",
  "tipo": "DVD",
  "dataAquisicao": "2026-10-14",
  "disponivel": true,
  "titulo": { "id": 1 }
}
```

## Banco de dados H2

O banco é criado automaticamente no primeiro início do backend:

```text
back-end/data/locadora.mv.db
```

Esse arquivo é local e não é versionado pelo Git.

Com o backend em execução, o console de desenvolvimento pode ser acessado em:

```text
http://localhost:8080/h2-console
```

Use as seguintes credenciais:

```text
JDBC URL: jdbc:h2:file:./data/locadora;AUTO_SERVER=TRUE
User Name: sa
Password: deixar em branco
```

> O console H2 deve ser utilizado apenas durante o desenvolvimento.

## Testes

### Backend

```powershell
cd back-end
.\mvnw.cmd test
```

### Frontend

```powershell
cd Front-end
npm ci
npm run test:ci
npm run build
```

O workflow em `.github/workflows/ci.yml` executa automaticamente os testes e builds em cada `push` e `pull_request`.

## Antes de enviar alterações

```powershell
git status
git add .
git commit -m "feat: implementa CRUD completo com Angular, Spring Boot e H2"
git push origin main
```

Confira o nome da branch atual antes do último comando com `git branch --show-current`.
