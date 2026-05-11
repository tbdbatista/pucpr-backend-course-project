# Trabalho Final - Desenvolvimento de Backend - Turma U  

**Período:** 11/04/2026 a 18/04/2026  
**Código:** POLS_580148  
**Aluno:** Thiago Beltrami Dias Batista

## Sobre o projeto

Este projeto implementa um backend de autenticação com Spring Boot e Kotlin.
De forma simples, ele organiza recursos de usuários e papéis (roles), permitindo
operações básicas para cadastro, consulta e gerenciamento dessas entidades.

## Tecnologias principais

- Kotlin
- Spring Boot
- Spring Data JPA
- Gradle

## Extensao implementada no projeto

Foram adicionadas duas novas classes com relacionamento many-to-many:

- `Project`
- `Tag`

Tambem foram incluidos:

- CRUD completo para `Project` (e suporte completo para `Tag`)
- Endpoints para associar e remover associacao entre `Project` e `Tag`
- Consulta de projetos com filtro e ordenacao por query params
- Logs e excecoes centralizadas para os principais servicos
- Autenticacao simples para acoes destrutivas/modificadoras (`PUT` e `DELETE`)

### Autenticacao usada

As rotas de `PUT` e `DELETE` em `/projects/**` e `/tags/**` exigem o header:

- `X-Admin-Token: admin`

Valor configuravel em `application.yaml`:

- `auth.adminToken`

## Endpoints principais da extensao

- `POST /api/projects`
- `GET /api/projects`
- `GET /api/projects/{id}`
- `PUT /api/projects/{id}` (com token)
- `DELETE /api/projects/{id}` (com token)
- `PUT /api/projects/{id}/tags/{tagName}` (com token)
- `DELETE /api/projects/{id}/tags/{tagName}` (com token)
- `POST /api/tags`
- `GET /api/tags`

## Como testar (simples)

1. Subir a aplicacao:

```bash
./gradlew bootRun
```

2. Criar uma tag:

```bash
curl -X POST "http://localhost:8080/api/tags" \
  -H "Content-Type: application/json" \
  -d '{"name":"DEV","description":"Tag de desenvolvimento"}'
```

3. Criar um projeto:

```bash
curl -X POST "http://localhost:8080/api/projects" \
  -H "Content-Type: application/json" \
  -d '{"name":"Projeto API","description":"Projeto de exemplo","tags":[]}'
```

4. Consultar com filtro e ordenacao:

```bash
curl "http://localhost:8080/api/projects?tag=DEV&name=Projeto&sortBy=NAME&sortDir=ASC"
```

5. Associar tag ao projeto (com token):

```bash
curl -X PUT "http://localhost:8080/api/projects/1/tags/DEV" \
  -H "X-Admin-Token: admin"
```

## Vídeo de explicação (futuro)

> Link do vídeo: _a adicionar_
