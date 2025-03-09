"use client";
import Link from "next/link";
import { useSession } from "next-auth/react";
import { usePathname } from "next/navigation";
import LogoutButton from "./LogoutButton";

const Header = () => {
    const { data: session } = useSession() as any;
    const pathname = usePathname()
    return (
        <header className="bg-white shadow-md py-4 w-screen px-6 absolute top-0 z-50">
            <div className="max-w-4xl mx-auto flex justify-between items-center">
                <Link href="/" className="text-2xl font-bold text-slate-700">
                    PT Marketplace
                </Link>

                {session?.user?.id && pathname !== "/profile" ? (
                    <div className="flex space-x-4 items-center">
                    <Link
                        href="/profile"
                        className="flex items-center space-x-2 bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition duration-300"
                    >
                        <span>Profile</span>
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
                    <LogoutButton/>
                    </div>
                ) : (
                    session?.user?.id && pathname === "/profile" && (
                        <span className="text-blue-500 font-medium">{session.user.name || "User"}</span>
                    )
                )}
            </div>
        </header>
    );
};

export default Header;