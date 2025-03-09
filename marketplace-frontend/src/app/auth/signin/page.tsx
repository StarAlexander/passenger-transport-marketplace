"use client";
import Notification from "@/app/components/Notification";
import { signIn } from "next-auth/react";
import { useState } from "react";

export default function SignIn() {

    const [notification, setNotification] = useState<{
        message: string;
        type: "success" | "error";
    } | null>(null)
    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const formData = new FormData(event.currentTarget);

        const result = await signIn("credentials", {
            email: formData.get("email"),
            password: formData.get("password"),
            redirect: true,
            callbackUrl: "/",
        });

        if (result?.error) {
            setNotification({message:"Failed to authenticate",type:"error"})
            console.error("Sign-in error:", result.error);
        }
    };

    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center">
            {notification && <Notification message={notification?.message} type={notification?.type}/> }
            <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
                <h1 className="text-2xl font-bold mb-6 text-slate-700 text-center">Sign In</h1>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <input
                        type="email"
                        placeholder="Email"
                        name="email"
                        required
                        className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        name="password"
                        required
                        className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
                    />
                    <button
                        type="submit"
                        className="w-full bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition duration-300"
                    >
                        Sign In
                    </button>
                </form>
                <p className="mt-4 text-center text-sm text-gray-600">
                    Don’t have an account?{" "}
                    <a href="/auth/signup" className="text-blue-500 hover:underline">
                        Sign Up
                    </a>
                </p>
            </div>
        </div>
    );
}