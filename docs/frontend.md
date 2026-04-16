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
- `frontend/src/utils/*` (gráficos)

## Estado atual da interface

### App

`App.jsx` alterna entre Home e Login por estado local:

- `statusLogin = false` => renderiza Home
- `statusLogin = true` => renderiza Login

No estado atual, a Home é a tela inicial exibida por padrão.

### Home

A tela Home entrega:

- identidade visual do projeto
- seleção `LOCAL` x `TRAJETO`
- listagem e detalhe de dados com modal
- visualizações gráficas (barra, linha, pizza) via utilitários locais

Fonte de dados atual da Home:

- import direto de `traffic_data.json` da raiz

Observação importante:

- chamadas `fetch` para backend existem no código, mas estão desativadas (comentadas).

### Login

A tela Login possui:

- layout de login
- botão de conta Google

Observação:

- a integração efetiva de login com backend ainda não está conectada ao fluxo de navegação da UI.

## Integração com API (status)

- Integração parcial / em evolução
- Backend possui endpoints prontos, porém o frontend ainda utiliza majoritariamente dados locais
- Integrações prioritárias:
- `/auth/login`
- `/traffic/insights`
- `/traffic/filter`
- `/traffic/traffic-volume-area`

## Como executar

No diretório `frontend`:

```bash
npm install
npm run dev
```

Ambiente local padrão:

- `http://localhost:5173`

## Dependências confirmadas no `package.json`

- `react`
- `react-dom`
- `leaflet`
- `react-leaflet`

## Riscos e ajustes recomendados para Demo Day

1. Conectar Home com `GET /traffic` e `GET /traffic/insights` (retirar dependência do JSON local para o fluxo principal).
2. Conectar Login com `POST /auth/login` e armazenamento de token para chamadas autenticadas.
3. Definir variável de ambiente de frontend para base URL da API.
4. Atualizar `frontend/README.md` (atualmente template padrão do Vite).
