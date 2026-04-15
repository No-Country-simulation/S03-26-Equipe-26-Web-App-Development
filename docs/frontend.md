# Frontend

Este documento descreve o estado atual da camada frontend na branch `dev`.

## Stack

- React 19
- Vite 8
- ESLint
- Leaflet 1.9.4
- React-Leaflet 5.0.0

## Estrutura principal

- `frontend/src/App.jsx`
- `frontend/src/pages/home/Home.jsx`
- `frontend/src/pages/login/Login.jsx`
- `frontend/src/utils/*` (graficos)

## Estado atual da interface

### App

`App.jsx` alterna entre Home e Login por estado local:

- `statusLogin = false` => renderiza Home
- `statusLogin = true` => renderiza Login

No estado atual, a Home e a tela inicial exibida por padrao.

### Home

A tela Home entrega:

- identidade visual do projeto
- selecao `LOCAL` x `TRAJETO`
- listagem e detalhe de dados com modal
- visualizacoes graficas (barra, linha, pizza) via utilitarios locais

Fonte de dados atual da Home:

- import direto de `traffic_data.json` da raiz

Observacao importante:

- chamadas `fetch` para backend existem no codigo, mas estao desativadas (comentadas).

### Login

A tela Login possui:

- layout de login
- botao de conta Google

Observacao:

- a integracao efetiva de login com backend ainda nao esta conectada ao fluxo de navegacao da UI.

## Integracao com API (status)

- Integracao parcial / em evolucao
- Backend possui endpoints prontos, porem o frontend ainda utiliza majoritariamente dados locais
- Integracoes prioritarias:
- `/auth/login`
- `/traffic/insights`
- `/traffic/filter`
- `/traffic/traffic-volume-area`

## Como executar

No diretorio `frontend`:

```bash
npm install
npm run dev
```

Ambiente local padrao:

- `http://localhost:5173`

## Dependencias confirmadas no `package.json`

- `react`
- `react-dom`
- `leaflet`
- `react-leaflet`

## Riscos e ajustes recomendados para Demo Day

1. Conectar Home com `GET /traffic` e `GET /traffic/insights` (retirar dependencia do JSON local para o fluxo principal).
2. Conectar Login com `POST /auth/login` e armazenamento de token para chamadas autenticadas.
3. Definir variavel de ambiente de frontend para base URL da API.
4. Atualizar `frontend/README.md` (atualmente template padrao do Vite).
