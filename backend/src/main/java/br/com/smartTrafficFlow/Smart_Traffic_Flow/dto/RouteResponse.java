package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import java.util.List;

public class RouteResponse {
    private String origin;
    private String destination;
    private String mode;
    private List<TransportOption> options;
    private TrafficInfo trafficInfo;

    // Construtores
    public RouteResponse() {}

    public RouteResponse(String origin, String destination, String mode, List<TransportOption> options) {
        this.origin = origin;
        this.destination = destination;
        this.mode = mode;
        this.options = options;
    }

    // Getters e Setters
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<TransportOption> getOptions() {
        return options;
    }

    public void setOptions(List<TransportOption> options) {
        this.options = options;
    }

    public TrafficInfo getTrafficInfo() {
        return trafficInfo;
    }

    public void setTrafficInfo(TrafficInfo trafficInfo) {
        this.trafficInfo = trafficInfo;
    }

    // Classe interna para Opções de Transporte
    public static class TransportOption {
        private String type;        // "transporte_publico", "carro", "caminhada"
        private String vehicle;     // "Ônibus", "Metrô", "Carro"
        private String line;        // "8000-10", "Linha 1-Azul"
        private String distance;    // "8.5 km"
        private String duration;    // "35 minutos"
        private String price;       // "R$ 4,40"
        private String instructions; // Instruções gerais
        private List<String> steps;  // Passo a passo detalhado

        // Construtores
        public TransportOption() {}

        // Getters e Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVehicle() {
            return vehicle;
        }

        public void setVehicle(String vehicle) {
            this.vehicle = vehicle;
        }

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        public List<String> getSteps() {
            return steps;
        }

        public void setSteps(List<String> steps) {
            this.steps = steps;
        }
    }

    // Classe interna para Informações de Tráfego
    public static class TrafficInfo {
        private String status;      // "LIVRE", "MODERADO", "CONGESTIONADO"
        private String message;     // Mensagem descritiva

        public TrafficInfo() {}

        public TrafficInfo(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
