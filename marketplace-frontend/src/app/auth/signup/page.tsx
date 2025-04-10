"use client";
import { useRouter } from "next/navigation";
import { useState } from "react";
import Notification from "@/app/components/Notification";

export default function SignUp() {
    const router = useRouter();
    const [notification, setNotification] = useState<{ message: string; type: "success" | "error" } | null>(null);
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const formData = new FormData(event.currentTarget);
        const username = formData.get("username") as string;
        const email = formData.get("email") as string;
        const password = formData.get("password") as string;

        if (!username || !email || !password) {
            setNotification({ message: "Все поля обязательны", type: "error" });
            return;
        }

        setIsLoading(true);
        try {
            const response = await fetch(`http://localhost:8080/users/register`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, email, password }),
            });

            if (!response.ok) {
                throw new Error("Registration failed");
            }

            setNotification({ message: "Регистрация успешна!", type: "success" });
            setTimeout(() => router.push("/auth/signin"), 500); // Задержка перед перенаправлением
        } catch (error) {
            console.error("Registration error:", error);
            setNotification({ message: "Не удалось зарегистрироваться", type: "error" });
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center">
            {notification && <Notification message={notification.message} type={notification.type} />}
            <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
                <h1 className="text-2xl font-bold mb-6 text-slate-700 text-center">Регистрация</h1>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <input
                        type="text"
                        placeholder="Username"
                        name="username"
                        required
                        className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500 transition duration-300"
                    />
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
                        disabled={isLoading}
                        className={`w-full bg-blue-500 text-white py-2 px-4 rounded-lg ${
                            isLoading ? "opacity-50 cursor-not-allowed" : "hover:bg-blue-600"
                        } transition duration-300`}
                    >
                        {isLoading ? "Регистрация..." : "Зарегистрироваться"}
                    </button>
                </form>
                <p className="mt-4 text-center text-sm text-gray-600">
                    Уже есть аккаунт?{" "}
                    <a href="/auth/signin" className="text-blue-500 hover:underline">
                        Войти
                    </a>
                </p>
            </div>
        </div>
    );
}