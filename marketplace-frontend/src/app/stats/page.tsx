"use client"


import React, { useEffect, useState } from "react";
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
import { fetchTotalUsers, fetchTotalRoutes, fetchAverageBookingsPerUser, fetchPopularRoutes, fetchBookingsByTransportType } from "@/utils/api";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const StatsPage = () => {
  const [totalUsers, setTotalUsers] = useState<number | null>(null);
  const [totalRoutes, setTotalRoutes] = useState<number | null>(null);
  const [averageBookings, setAverageBookings] = useState<number | null>(null);
  const [popularRoutes, setPopularRoutes] = useState<any[]>([]);
  const [bookingsByTransportType, setBookingsByTransportType] = useState<any[][]>([[]]);

  useEffect(() => {
    const fetchData = async () => {
      setTotalUsers(await fetchTotalUsers());
      setTotalRoutes(await fetchTotalRoutes());
      setAverageBookings(await fetchAverageBookingsPerUser());
      setPopularRoutes(await fetchPopularRoutes());
      const bks = await fetchBookingsByTransportType()
      console.log(bks)
      setBookingsByTransportType(bks);
    };
    fetchData();
  }, []);

  const transportTypeData = {
    labels: bookingsByTransportType.map((el:any)=>el[0]),
    datasets: [
      {
        label: "Бронирования по типу транспорта",
        data: bookingsByTransportType.map((el:any)=>el[1]),
        backgroundColor: ["rgba(75, 192, 192, 0.6)", "rgba(255, 99, 132, 0.6)", "rgba(54, 162, 235, 0.6)"],
      },
    ],
  };

  return (
    <div className="min-h-screen bg-gray-100 py-8">
      <div className="max-w-4xl mx-auto mt-16 px-4">
        <h1 className="text-3xl font-bold mb-8 text-slate-700 text-center">Статистика</h1>

        {/* Общая статистика */}
        <div className="bg-white p-6 rounded-lg shadow-md mb-8 space-y-4">
          <h2 className="text-xl font-bold text-slate-700">Общие показатели</h2>
          <p>Всего пользователей: {totalUsers}</p>
          <p>Все маршрутов: {totalRoutes}</p>
          <p>Среднее кол-во бронирований на пользователя: {averageBookings?.toFixed(2)}</p>
        </div>

        {/* Популярные маршруты */}
        <div className="bg-white p-6 rounded-lg shadow-md mb-8">
          <h2 className="text-xl font-bold text-slate-700 mb-4">Популярные маршруты</h2>
          <ul>
            {popularRoutes.map(([origin, destination, count], index) => (
              <li key={index}>
                {origin} → {destination} ({count} бронирований)
              </li>
            ))}
          </ul>
        </div>

        {/* График бронирований по типам транспорта */}
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-bold text-slate-700 mb-4">Бронирования по типу транспорта</h2>
          <Bar data={transportTypeData} />
        </div>
      </div>
    </div>
  );
};

export default StatsPage;