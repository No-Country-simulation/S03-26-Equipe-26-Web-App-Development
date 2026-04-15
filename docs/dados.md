# Dados

Este documento descreve o estado atual das fontes de dados, contratos e pipeline de persistencia da aplicacao.

## Fontes de dados no repositorio

Atualmente existem multiplas fontes de dados com papeis distintos:

- `backend/src/main/resources/import.sql`
- `backend/src/main/resources/traffic_data.json`
- `traffic_data.json` (raiz)
- `generator.py` (geracao auxiliar)

## O que o backend usa no estado atual

### Persistencia principal

- Banco: PostgreSQL (`application.properties`)
- Entidade: `TrafficData`
- Tabela: `traffic_data`

### Carga via endpoint

- `POST /traffic/load` chama `TrafficService.loadData()`
- Fluxo atual consome SPTrans (`/Corredor`) e nao JSON local

### Massa SQL

- Existe `import.sql` com registros de trafego e geometria (`POINT`)
- Mantido como baseline para ambiente local e testes de consistencia

## Modelo de dado persistido (`TrafficData`)

Campos principais:

- `id` (PK)
- `idvia`
- `nome`
- `tipo` (`TypeOfRoute`)
- `hora` (`LocalDateTime`)
- `clima` (`Climate`)
- `volume`
- `capacidade`
- `nivel`
- `status` (`StatusTrafego`)
- `alerta` (`TrafficAlert`)
- `geom` (`Point`, SRID 4326)

Regra de integridade importante:

- unique constraint em `idvia + hora`

## Contrato de carga por DTO (`TrafficDataDTO`)

Campos esperados pelo DTO:

- `idvia`, `nome`, `tipo`, `hora`
- `clima`, `volume`, `capacidade`, `nivel`, `status`, `alerta`
- `lat`, `lng`

Observacao:

- O DTO permanece util para carga JSON, mas o fluxo de `POST /traffic/load` na branch atual esta orientado a SPTrans.

## Fluxo de transformacao para resposta da API

`TrafficData` -> `TrafficResponse`

A resposta exposta para frontend prioriza:

- metadados de via/trafego
- coordenadas `lat/lng`
- sem exposicao direta de `geom`

## Divergencias e pontos de atencao

- Existem arquivos de dados em duplicidade (raiz e `backend/resources`).
- O fluxo historicamente documentado como JSON local nao representa mais o endpoint de carga atual.
- Parte do comportamento depende de APIs externas (SPTrans, TomTom, geocoding), com fallback em caso de falha.

## Recomendacoes tecnicas

1. Definir oficialmente uma unica fonte de massa estatica (`import.sql` ou `traffic_data.json` versionado no backend).
2. Documentar quando usar carga SPTrans vs massa local.
3. Criar `schema` de entrada versionado para JSON (se o fluxo voltar a ser usado em producao).
4. Evitar versionamento de dados sensiveis e tokens no repositorio.
