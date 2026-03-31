package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.WeatherDTO;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;


    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    // busca na api
    public WeatherResponse getWeather(double lat, double lon) {
        try {
            // LOG DE DEBUG: Verifique se as chaves foram carregadas
            System.out.println("API URL: " + apiUrl);
            System.out.println("API KEY: " + (apiKey != null ? "Carregada" : "NULA"));

            if (apiUrl == null || apiKey == null) {
                throw new RuntimeException("Configurações da OpenWeather ausentes no application.properties");
            }

            // Use %s para lat e lon para evitar problemas de formatação de localidade (vírgula vs ponto)
            String url = String.format("%s?lat=%s&lon=%s&appid=%s&units=metric",
                    apiUrl, lat, lon, apiKey);

            return restTemplate.getForObject(url, WeatherResponse.class);
        } catch (Exception e) {
            System.err.println("ERRO REAL DA OPENWEATHER:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public WeatherDTO getClimaAmigavel(double lat, double lon) {
        try {
            WeatherResponse response = getWeather(lat, lon);

            // 1. CHECAGEM DE SEGURANÇA (Se isso falhar, não tenta dar o build)
            if (response == null || response.getMain() == null || response.getWeather() == null || response.getWeather().length == 0) {
                System.err.println(">>> Erro: Resposta da API OpenWeather veio incompleta.");
                return null;
            }

            // 2. VARIÁVEIS AUXILIARES (Facilita o debug se der erro de tipo)
            String condicaoOriginal = response.getWeather()[0].getMain();
            String traducao = traduzirCondicao(condicaoOriginal);
            double impacto = calcularImpacto(condicaoOriginal);

            String descricao =
                    response.getWeather()[0].getDescription() != null
                    ? response.getWeather()[0].getDescription()
                    : "Sem descrição";

            // 3. O BUILDER (Aqui é onde o erro 500 costuma acontecer)
            return WeatherDTO.builder()
                    .temperatura(response.getMain().getTemp())
                    .umidade(response.getMain().getHumidity()) // Garante que é int
                    .condicao(traducao)
                    .descricao(descricao)
                    .fatorImpacto(impacto)
                    .build();

        } catch (Exception e) {
            // ISSO VAI MOSTRAR O ERRO REAL NO CONSOLE DO INTELLIJ
            System.err.println(">>> ERRO NO TRADUTOR: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

        private String traduzirCondicao(String condicao) {
            return switch (condicao) {
                case "Clouds" -> "Nublado";
                case "Rain" -> "Chuvoso";
                case "Clear" -> "Céu Limpo";
                case "Thunderstorm" -> "Tempestade";
                case "Drizzle" -> "Garoa";
                default -> condicao;
            };
        }

    private double calcularImpacto(String condicao) {
        return switch (condicao){
            case "Rain", "Thunderstorm" -> 0.6; //reduz velocidade em 40%
            case "Clouds", "Drizzle" -> 0.9; //reduz em 10%
            default -> 1.0;
        };
    }
}
