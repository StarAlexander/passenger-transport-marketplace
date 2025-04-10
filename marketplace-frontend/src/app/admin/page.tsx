"use client"

import React, { useEffect, useState } from "react";
import AddRouteForm from "../components/AddRouteForm";
import UserManagement from "../components/UserManagement";
import StatsChart from "../components/StatsChart";

const AdminDashboard = () => {
    const [users, setUsers] = useState<any[]>([]);
    const [routes, setRoutes] = useState<any[]>([]);

    // Загрузка данных пользователей и маршрутов
    useEffect(() => {
        fetch("http://localhost:8080/users")
            .then((res) => res.json())
            .then((data) => setUsers(data));

        fetch("http://localhost:8080/routes")
            .then((res) => res.json())
            .then((data) => setRoutes(data));
    }, []);

    // Обработчики для управления маршрутами
    const handleAddRoute = async (route: any) => {
        try {
            const response = await fetch("http://localhost:8080/admin/routes/add", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(route),
            });
            if (!response.ok) throw new Error("Failed to add route");
            setRoutes([...routes, route]);
        } catch (error) {
            console.error("Error adding route:", error);
        }
    };

    // Обработчики для активации/деактивации пользователей
    const handleActivateUser = async (userId: number) => {
        await fetch(`http://localhost:8080/admin/users/${userId}/activate`, { method: "PUT" });
        setUsers((prev) =>
            prev.map((user) => (user.id === userId ? { ...user, active: true } : user))
        );
    };

    const handleDeactivateUser = async (userId: number) => {
        await fetch(`http://localhost:8080/admin/users/${userId}/deactivate`, { method: "PUT" });
        setUsers((prev) =>
            prev.map((user) => (user.id === userId ? { ...user, active: false } : user))
        );
    };

    return (
        <div className="min-h-screen bg-gray-100 py-8">
            <div className="max-w-6xl mx-auto mt-16 px-4">
                {/* Заголовок */}
                <h1 className="text-3xl font-bold mb-8 text-slate-700 text-center">
                    Админ-панель
                </h1>

                {/* Секция статистики */}
                <div className="bg-white p-6 rounded-lg shadow-md mb-8">
                    <h2 className="text-xl font-bold text-slate-700 mb-4">Статистика</h2>
                    <StatsChart />
                </div>

                {/* Секция добавления маршрутов */}
                <div className="bg-white p-6 rounded-lg shadow-md mb-8">
                    <h2 className="text-xl font-bold text-slate-700 mb-4">Добавить новый маршрут</h2>
                    <AddRouteForm onAddRoute={handleAddRoute} />
                </div>

                {/* Секция управления пользователями */}
                <div className="bg-white p-6 rounded-lg shadow-md">
                    <h2 className="text-xl font-bold text-slate-700 mb-4">Управление пользователями</h2>
                    <UserManagement
                        users={users}
                        onActivate={handleActivateUser}
                        onDeactivate={handleDeactivateUser}
                    />
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;