# API

Este documento concentra os endpoints, comportamento atual e contrato principal do backend.

## Base URL

Ambiente local padrao:

- `http://localhost:8080`

Base path atual:

- `/traffic`

## Endpoints Disponiveis

### `GET /traffic`

Retorna todos os registros persistidos em formato simplificado para o frontend.

Exemplo:

```bash
curl http://localhost:8080/traffic
```

### `GET /traffic/filter`

Filtra registros com base nos parametros atualmente suportados pelo backend.

Parametros opcionais:

- `clima`
- `nivel`
- `alerta`

Exemplos:

```bash
curl "http://localhost:8080/traffic/filter?clima=CHUVA_LEVE"
```

```bash
curl "http://localhost:8080/traffic/filter?nivel=70"
```

```bash
curl "http://localhost:8080/traffic/filter?alerta=ANOMALIA"
```

Comportamento atual:

- se `clima` e `nivel` forem enviados juntos, o backend usa `findByClimaAndNivelGreaterThan(...)`
- se apenas `clima` for enviado, usa `findByClima(...)`
- se apenas `nivel` for enviado, usa `findByNivelGreaterThan(...)`
- se apenas `alerta` for enviado, usa `findByAlerta(...)`
- sem parametros, retorna todos os registros

### `POST /traffic`

Cria um novo registro manualmente.

Payload de exemplo:

```json
{
  "idvia": 1,
  "nome": "Av. Central",
  "tipo": "ARTERIAL",
  "hora": "2026-03-28T08:00:00",
  "clima": "LIMPO",
  "volume": 920,
  "capacidade": 1000,
  "nivel": 92.0,
  "status": "CONGESTIONADO",
  "alerta": "CRITICO"
}
```

Observacoes:

- o backend trabalha com enums para `tipo`, `clima`, `status` e `alerta`
- a entidade persistida tambem possui `geom`, mas os endpoints de listagem e filtro nao expõem esse objeto bruto

### `POST /traffic/load`

Tenta carregar dados de um arquivo JSON e persisti-los evitando duplicidade por `idvia + hora`.

Fluxo implementado:

1. o controller chama `TrafficService.loadData()`
2. o service le `traffic_data.json` do classpath em uma lista de `TrafficDataDTO`
3. cada DTO e convertido para `TrafficData`
4. `lat/lng` sao convertidos em `Point`
5. o registro so e salvo se `existsByIdviaAndHora(...)` retornar falso

Estado atual:

- a implementacao existe
- o arquivo JSON exigido por esse fluxo ainda nao esta em `backend/src/main/resources`
- na pratica, a carga inicial operacional continua acontecendo via `import.sql`

## Fluxo de Consulta

```mermaid
sequenceDiagram
    participant Client as Cliente
    participant Controller as TrafficController
    participant Service as TrafficService
    participant Repo as TrafficRepository
    participant DB as H2/H2GIS

    Client->>Controller: GET /traffic/filter?clima=LIMPO&nivel=70
    Controller->>Service: findByFilters(clima, nivel, alerta)
    Service->>Repo: findByClimaAndNivelGreaterThan(...)
    Repo->>DB: SELECT com filtros
    DB-->>Repo: registros
    Repo-->>Service: List<TrafficData>
    Service-->>Controller: List<TrafficResponse>
    Controller-->>Client: 200 OK + JSON
```

## Modelo Atual da Entidade

```mermaid
classDiagram
    class TrafficData {
        +Long id
        +Integer idvia
        +String nome
        +TypeOfRoute tipo
        +LocalDateTime hora
        +Climate clima
        +int volume
        +int capacidade
        +double nivel
        +StatusTrafego status
        +TrafficAlert alerta
        +Point geom
    }
```

## Dependencias Relevantes do Backend

- Spring Boot Web
- Spring Data JPA
- H2
- Hibernate Spatial
- H2GIS

## Insights do MVP

### `GET /traffic/insights`

Retorna insights simples para o MVP com base nos registros persistidos.

Resposta atual:

- `totalRegistros`: quantidade total de registros persistidos
- `horarioPico`: hora com maior soma de volume
- `volumeHorarioPico`: soma de volume no horario de pico
- `viaMaisMovimentada`: via com maior media de volume
- `mediaVolumeViaMaisMovimentada`: media de volume da via mais movimentada

Exemplo:

```json
{
  "totalRegistros": 72,
  "horarioPico": "2026-03-28T18:00:00",
  "volumeHorarioPico": 1919,
  "viaMaisMovimentada": "Rodovia do Aeroporto",
  "mediaVolumeViaMaisMovimentada": 568.46
}
```

## Ajustes da API de trafego

### Resposta simplificada de `GET /traffic` e `GET /traffic/filter`

Os endpoints de listagem e filtro retornam um payload simplificado para facilitar o consumo no frontend.

A resposta nao expoe mais o objeto bruto de `geom`.
Quando houver localizacao, o backend retorna apenas:

- `lat`
- `lng`

Exemplo:

```json
[
  {
    "id": 1,
    "idvia": 1,
    "nome": "Av. Central",
    "tipo": "ARTERIAL",
    "hora": "2026-03-28T00:00:00",
    "clima": null,
    "volume": 202,
    "capacidade": 1000,
    "nivel": 20.2,
    "status": null,
    "alerta": "NORMAL",
    "lat": -23.5505,
    "lng": -46.6333
  }
]
```

### Filtro por alerta

O endpoint `GET /traffic/filter?alerta=ANOMALIA` passa a usar o tipo correto do dominio no backend.

Com isso:

- o filtro deixa de depender de comparacao incorreta por `String`
- valores invalidos de alerta passam a ser tratados como erro de requisicao
