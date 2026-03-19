# 📊 Documentação do Motor de Dados (v2.0)

Este documento descreve a estrutura do arquivo `traffic_data.json` gerado pelo script `generator.py`.

## 🧠 Lógica de Simulação
O motor simula o tráfego de 24 horas baseando-se em:
- **Picos Comerciais:** 08h e 18h (Vias Arteriais).
- **Picos Logísticos:** Madrugada e Final da noite (Rodovia do Aeroporto).
- **Eventos Aleatórios:** Simulação de incidentes (ex: 03h na Av. Central).

## 🗂️ Dicionário de Dados (Campos do JSON)

| Campo | Descrição | Exemplo |
| :--- | :--- | :--- |
| `id_via` | Identificador único da rua/avenida. | `1` |
| `nome` | Nome da via monitorada. | `Av. Central` |
| `tipo` | Categoria da via (Arterial, Rodovia, Residencial). | `Rodovia` |
| `hora` | Janela de tempo da medição. | `14:00` |
| `volume` | Quantidade de veículos detectados. | `850` |
| `capacidade` | Limite máximo de veículos da via. | `1000` |
| `nivel_congestionamento` | Porcentagem de ocupação (Volume/Capacidade). | `85.00` |
| `status` | Classificação visual (Livre, Moderado, Crítico). | `Crítico` |
| `alerta` | **IA de Detecção:** Mensagem de status ou anomalia. | `🚨 ANOMALIA` |

## ⚠️ Regras de Alerta para o Front-end
O campo `alerta` deve ser usado para disparar notificações na interface:
1. **Normal:** Fluxo dentro do esperado.
2. **Livre/Moderado:** Ícones verdes ou amarelos.
3. **🚨 ANOMALIA:** Fluxo alto em horário atípico (ex: madrugada). **Ação sugerida: Exibir Pop-up de Incidente.**
4. **⚠️ CRÍTICO:** Via operando acima de 90% da capacidade.