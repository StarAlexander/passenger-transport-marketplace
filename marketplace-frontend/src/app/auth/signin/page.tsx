"use client";
import Notification from "@/app/components/Notification";
import { signIn } from "next-auth/react";
import { useState } from "react";

export default function SignIn() {
    const [notification, setNotification] = useState<{ message: string; type: "success" | "error" } | null>(null);

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const formData = new FormData(event.currentTarget);
        const email = formData.get("email") as string;
        const password = formData.get("password") as string;

        try {
            const result = await signIn("credentials", {
                email,
                password,
                redirect: false,
            });

            if (result?.error) {
                throw new Error(result.error);
            }

            setNotification({ message: "Авторизация успешна!", type: "success" });
            window.location.href = "/"; // Перенаправление после успешного входа
        } catch (error) {
            console.error("Sign-in error:", error);
            setNotification({ message: "Не удалось авторизоваться", type: "error" });
        }
    };

    return (
        <div className="h-screen bg-gray-100 flex items-center justify-center">
            {notification && <Notification message={notification.message} type={notification.type} />}
            <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
                <h1 className="text-2xl font-bold mb-6 text-slate-700 text-center">Авторизация</h1>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <input
                        type="email"
                        placeholder="Email"
                        name="email"
                        required
                        className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500 transition duration-300"
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        name="password"
                        required
                        className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500 transition duration-300"
                    />
                    <button
                        type="submit"
                        className="w-full bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition duration-300 transform hover:scale-105"
                    >
                        Войти
                    </button>
                </form>
                <p className="mt-4 text-center text-sm text-gray-600">
                    Нет аккаунта?{" "}
                    <a href="/auth/signup" className="text-blue-500 hover:underline">
                        Зарегистрироваться
                    </a>
                </p>
            </div>
        </div>
    );
}