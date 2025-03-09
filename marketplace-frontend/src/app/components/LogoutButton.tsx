"use client";
import { signOut } from "next-auth/react";

const LogoutButton = () => {
    const handleSignOut = async () => {
        try {
            await signOut({
                redirect: true, 
                callbackUrl: "/auth/signin", 
            });
        } catch (error) {
            console.error("Error signing out:", error);
        }
    };

    return (
        <button
            onClick={handleSignOut}
            className="bg-red-500 text-white py-2 px-4 rounded-lg hover:bg-red-600 transition duration-300"
        >
            Log Out
        </button>
    );
};

export default LogoutButton;