# Estágio de Build
FROM node:20 as build-stage
WORKDIR /app

# Copia tudo o que está na pasta (incluindo a subpasta smartTrafficFlow)
COPY . .

# Entra na pasta correta, instala e builda
RUN cd smartTrafficFlow && npm install && npm run build

# Estágio de Produção
FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*

# Copia o resultado do build para o Nginx
# O Vite costuma gerar a pasta 'dist' dentro da pasta do projeto
COPY --from=build-stage /app/smartTrafficFlow/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]