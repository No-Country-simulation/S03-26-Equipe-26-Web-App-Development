import { useEffect, useState } from "react";

function TrafficInsights() {
  const [insights, setInsights] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/traffic/insights")
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Erro HTTP ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        setInsights(data);
        setError("");
      })
      .catch((err) => {
        console.error("Erro ao carregar insights:", err);
        setError("Nao foi possivel carregar os insights.");
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Carregando insights...</p>;
  if (error) return <p>{error}</p>;
  if (!insights) return <p>Nenhum insight disponivel.</p>;

  return (
    // <section>
    //   <h2>Insights do Trafego</h2>
    //   <p>Total de registros: {insights.totalRegistros}</p>
    //   <p>Horario de pico: {insights.horarioPico}</p>
    //   <p>Volume no horario de pico: {insights.volumeHorarioPico}</p>
    //   <p>Via mais movimentada: {insights.viaMaisMovimentada}</p>
    //   <p>Media de volume da via mais movimentada: {insights.mediaVolumeViaMaisMovimentada}</p>
    // </section>
    insights
  );
}

export default TrafficInsights;