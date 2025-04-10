import { useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const StatsChart = () => {
  const [averageVisits, setAverageVisits] = useState<number | null>(null);

  useEffect(() => {
    fetch("http://localhost:8080/admin/stats/average-visits")
      .then((res) => res.json())
      .then((data) => {
        setAverageVisits(data);
      })
      .catch((error) => {
        console.error("Error fetching average visits:", error);
      });
  }, []);

  if (averageVisits === null) {
    return <p>Загрузка...</p>;
  }

  const data = {
    labels: ["Среднее кол-во бронирований"],
    datasets: [
      {
        label: "Среднее кол-во бронирований на пользователя",
        data: [averageVisits],
        backgroundColor: "rgba(75, 192, 192, 0.6)",
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: "top" as const,
      },
      title: {
        display: true,
        text: "Среднее кол-во бронирований на пользователя",
      },
    },
  };

  return <Bar data={data} options={options} />;
};

export default StatsChart;