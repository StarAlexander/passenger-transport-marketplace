"use client";
import Link from "next/link";
import { useSession } from "next-auth/react";
import { usePathname } from "next/navigation";
import LogoutButton from "./LogoutButton";
import { motion } from "framer-motion";

const Header = () => {
    const { data: session } = useSession() as any;
    const pathname = usePathname();
    if (pathname == "/auth/signin" || pathname == "/auth/signup") return null
    return (
        <header className="bg-white shadow-md w-screen py-4 px-6 sticky top-0 z-50">
            <div className="max-w-4xl mx-auto flex justify-between items-center">
                <Link href="/" className="text-2xl font-bold text-slate-700 hover:text-blue-500 transition duration-300">
                    PT Marketplace
                </Link>
                {session?.user?.id && pathname !== "/profile" ? (
                    <div className="flex space-x-4 items-center">
                        <Link
                            href="/profile"
                            className="flex items-center space-x-2 bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition duration-300 transform hover:scale-105"
                        >
                            <span>Профиль</span>
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 24 24"
                                stroke="currentColor"
                                className="w-5 h-5"
                            >
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth={2}
                                    d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                                />
                            </svg>
                        </Link>

                        <Link href="/stats" className="flex items-center space-x-2 bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition duration-300 transform hover:scale-105">
                        Статистика
                        </Link>
                        <LogoutButton />
                    </div>
                ) : (
                    session?.user?.id && pathname === "/profile" && (
                                            <motion.div
                        initial={{ opacity: 0, scale: 0.8 }}
                        animate={{ opacity: 1, scale: 1 }}
                        transition={{ duration: 0.5 }}
                        className="flex flex-col items-center justify-center mb-8"
                    >
                        <div className="relative">
                            <div className="w-24 h-24 rounded-full bg-blue-500 flex items-center justify-center text-white text-2xl font-bold uppercase">
                                {session?.user?.name?.charAt(0) || "U"}
                            </div>
                        </div>
                        <h2 className="text-xl font-semibold mt-2 text-slate-700">
                            Добро пожаловать, <span className="text-blue-500">{session?.user?.name}</span>!
                        </h2>
                    </motion.div>
                    )
                )}

                {session?.user?.name == "admin" && pathname !== "/admin" && <div
            className="bg-indigo-700 text-white py-2 px-4 rounded-lg hover:bg-white border-2 border-indigo-700 hover:text-indigo-700 transition duration-300">
            <Link
            href={"/admin"}
            >
                Админ-панель
            </Link>
        </div>}
            </div>
        </header>
    );
};

export default Header;