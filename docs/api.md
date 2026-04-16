# API

Este documento consolida o contrato HTTP atual do backend na branch `dev`.

## Base URL

- Local: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`

## Autenticação e Controle de Acesso

Matriz de acesso atual (`SecurityConfig`):

- Rotas públicas:
- `POST /auth/**`
- `GET /v3/api-docs/**`
- `GET /swagger-ui/**`
- `GET /swagger-ui.html`
- `GET /traffic/traffic-volume`
- `GET /traffic/traffic-volume-area`
- `GET /traffic/sptrans/posicao`
- Rotas autenticadas (JWT Bearer):
- `GET|POST /traffic/**` (exceto rotas públicas explicitadas acima)
- `GET|POST /api/**`
- `GET /insights`

Para rotas autenticadas, enviar:

- Header `Authorization: Bearer <token>`

## Endpoints de Autenticação

### `POST /auth/register`

Cria usuário local (provider `LOCAL`) e retorna token JWT.

Exemplo de body:

```json
{
  "nome": "Maria",
  "email": "maria@email.com",
  "senha": "123456"
}
```

### `POST /auth/login`

Autentica credenciais locais e retorna token JWT.

```json
{
  "email": "maria@email.com",
  "senha": "123456"
}
```

### `POST /auth/google`

Autenticação federada via Google ID Token.

```json
{
  "idToken": "TOKEN_GOOGLE"
}
```

## Endpoints de Tráfego (`/traffic`)

### `POST /traffic/load`

Executa ingestão de dados da SPTrans (`/Corredor`) e persiste registros no banco.

Observação:

- Não carrega `traffic_data.json` nesse fluxo atual.

### `GET /traffic`

Retorna lista de `TrafficResponse`.

### `POST /traffic`

Persiste um `TrafficData` recebido no corpo.

Observação:

- No estado atual, recebe entidade diretamente (não DTO de criação).

### `GET /traffic/filter`

Filtros opcionais por query params:

- `clima` (enum `Climate`)
- `nivel` (double, `>=`)
- `alerta` (texto, comparação case-insensitive sobre o nome do enum)

Exemplo:

```bash
curl "http://localhost:8080/traffic/filter?clima=NORMAL&nivel=30&alerta=ANOMALIA"
```

### `GET /traffic/insights`

Resumo analítico (`TrafficInsightsResponse`):

- total de registros
- horário de pico
- volume no pico
- via mais movimentada
- média da via mais movimentada

### `GET /traffic/news?query=...`

Retorna payload JSON de notícias relacionadas a trânsito.

Observação:

- Implementação atual utiliza retorno mock (`news: []`).

### `GET /traffic/dashboard?q=...&clima=...&nivel=...`

Retorna agregado assíncrono (`CompletableFuture<DashboardDTO>`) com:

- insights
- dados filtrados
- bloco de notícias

### `GET /traffic/sptrans?endpoint=Posicao`

Proxy HTTP genérico para SPTrans.

### `GET /traffic/sptrans/posicao`

Busca posição de ônibus na SPTrans.

Observação:

- Em indisponibilidade do provedor externo, retorna payload mock de fallback.

### `GET /traffic/route?cidade=...&origem=...&destino=...&modo=transit`

Calcula rota e retorna `RouteResponse`.

Observação:

- Lógica atual opera em modo mock controlado.

### `GET /traffic/traffic-volume?lat=...&lon=...`

Consulta volume de tráfego por coordenada (`TrafficVolumeResponse[]`), com tentativa em TomTom e fallback estimado.

### `GET /traffic/traffic-volume-area`

Consulta volume de tráfego para áreas predefinidas.

Observação:

- Controller retorna mock diretamente no estado atual da branch.

## Endpoint adicional de Insights

### `GET /insights`

Endpoint alternativo de insights disponibilizado por `TrafficInsightsController`.

## Endpoints de Transporte

Base path: `/api/transporte`

- `GET /api/transporte/cidades`
- `GET /api/transporte/stops/{cidade}?limit=100&offset=0`
- `GET /api/transporte/routes/{cidade}?limit=50`
- `GET /api/transporte/stops/{cidade}/nearby?lat=...&lon=...&radius=1.0`
- `POST /api/transporte/calculate-route`

Body de `calculate-route`:

```json
{
  "origin": "Av Paulista, Sao Paulo",
  "destination": "Aeroporto de Congonhas, Sao Paulo",
  "cidade": "sao-paulo"
}
```

## Endpoints GTFS

Base path: `/api/gtfs`

- `GET /api/gtfs/cidades`
- `GET /api/gtfs/stops/{cidade}`
- `GET /api/gtfs/routes/{cidade}`
- `GET /api/gtfs/heatmap/{cidade}`

## Endpoints de Analytics

- `GET /api/analytics/crowd-flow`

## Endpoints de Clima

Base path: `/api/test/clima`

- `GET /api/test/clima?lat=...&lon=...`
- `GET /api/test/clima/atual?lat=...&lon=...`

## DTOs de Resposta importantes

### `TrafficResponse`

Campos principais:

- `id`, `idvia`, `nome`, `tipo`, `hora`
- `clima`, `volume`, `capacidade`, `nivel`, `status`, `alerta`
- `lat`, `lng`

### `TrafficInsightsResponse`

- `totalRegistros`
- `horarioPico`
- `volumeHorarioPico`
- `viaMaisMovimentada`
- `mediaVolumeViaMaisMovimentada`

### `TrafficVolumeResponse`

- `location`, `hour`, `volume`
- `currentSpeed`, `freeFlowSpeed`
- `status`, `confidence`

## Observações para Demo Day

- Parte dos endpoints opera com fallback/mock em função de dependências externas.
- A matriz de acesso (público vs autenticado) deve ser respeitada durante validações de contrato.
- Recomendação de validação funcional: `auth`, `traffic/insights`, `traffic-volume` e `dashboard`.
