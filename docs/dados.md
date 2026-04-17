# Dados

Este documento descreve o estado atual das fontes de dados e da persistência na branch `main`.

## Fontes de Dados no Repositório

- `backend/src/main/resources/import.sql`
- `backend/src/main/resources/traffic_data.json`
- `traffic_data.json` (raiz)
- Dados GTFS em `microservice/data/*`

## Persistência Principal (Backend Java)

- Banco: PostgreSQL
- Tabela principal: `traffic_data`
- Entidade: `TrafficData`
- Restrição de unicidade: `idvia + hora`

Campos principais de `TrafficData`:

- `id`
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
- `geom` (`geometry(Point,4326)`)

Observações:

- `lat` e `lng` são derivados de `geom` nos getters da entidade.
- `geom` não é exposto diretamente no JSON de resposta.

## Fluxos de Carga

### Carga operacional

- Endpoint: `POST /traffic/load`
- Implementação: `TrafficService.loadData()`
- Fonte atual: API SPTrans (`/Corredor`)

### Massa local

- `import.sql` é usado como baseline local
- JSONs locais continuam disponíveis para apoio e consistência

## Transformação para Resposta de API

Fluxo principal:

- `TrafficData` -> `TrafficResponse`

A resposta ao frontend prioriza:

- metadados de via/tráfego
- coordenadas `lat/lng`
- ocultação do campo espacial bruto (`geom`)

## Integrações Externas com Impacto em Dados

No backend Java:

- SPTrans
- TomTom
- OpenWeather
- Google OAuth

No microservice Python:

- datasets GTFS por cidade (`sp`, `rj`, `porto`)

## Pontos de Atenção para Avaliação

- Existe duplicidade de arquivos de massa entre raiz e `backend/src/main/resources`.
- Alguns fluxos operam com fallback/mock quando provedores externos falham.
- Frontend principal consome backend Java e microservice Python em paralelo.

## Recomendações Pós-Entrega

1. Definir uma estratégia única para massa estática (SQL vs JSON).
2. Criar um `.env.example` consolidado para setup rápido de ambiente.
3. Documentar política de versão para arquivos GTFS grandes.
