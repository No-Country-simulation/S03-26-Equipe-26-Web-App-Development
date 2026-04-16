# Dados

Este documento descreve o estado atual das fontes de dados, contratos e pipeline de persistência da aplicação.

## Fontes de dados no repositório

Atualmente existem múltiplas fontes de dados com papéis distintos:

- `backend/src/main/resources/import.sql`
- `backend/src/main/resources/traffic_data.json`
- `traffic_data.json` (raiz)
- `generator.py` (geração auxiliar)

## O que o backend usa no estado atual

### Persistência principal

- Banco: PostgreSQL (`application.properties`)
- Entidade: `TrafficData`
- Tabela: `traffic_data`

### Carga via endpoint

- `POST /traffic/load` chama `TrafficService.loadData()`
- Fluxo atual consome SPTrans (`/Corredor`) e não JSON local

### Massa SQL

- Existe `import.sql` com registros de tráfego e geometria (`POINT`)
- Mantido como baseline para ambiente local e testes de consistência

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

Observação:

- O DTO permanece útil para carga JSON, mas o fluxo de `POST /traffic/load` na branch atual está orientado a SPTrans.

## Fluxo de transformação para resposta da API

`TrafficData` -> `TrafficResponse`

A resposta exposta para frontend prioriza:

- metadados de via/tráfego
- coordenadas `lat/lng`
- sem exposição direta de `geom`

## Divergências e pontos de atenção

- Existem arquivos de dados em duplicidade (raiz e `backend/resources`).
- O fluxo historicamente documentado como JSON local não representa mais o endpoint de carga atual.
- Parte do comportamento depende de APIs externas (SPTrans, TomTom, geocoding), com fallback em caso de falha.

## Recomendações técnicas

1. Definir oficialmente uma única fonte de massa estática (`import.sql` ou `traffic_data.json` versionado no backend).
2. Documentar quando usar carga SPTrans vs massa local.
3. Criar `schema` de entrada versionado para JSON (se o fluxo voltar a ser usado em produção).
4. Evitar versionamento de dados sensíveis e tokens no repositório.
