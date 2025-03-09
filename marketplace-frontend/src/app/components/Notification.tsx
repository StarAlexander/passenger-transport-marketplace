// components/Notification.tsx
import { useState, useEffect } from "react";

interface NotificationProps {
    message: string;
    type: "success" | "error";
}

const Notification: React.FC<NotificationProps> = ({ message, type }) => {
    const [visible, setVisible] = useState(true);

    useEffect(() => {
        const timer = setTimeout(() => setVisible(false), 3000);
        return () => clearTimeout(timer);
    }, []);

    if (!visible) return null;

    return (
        <div
            className={`fixed bottom-8 right-8 p-4 rounded-lg shadow-md ${
                type === "success" ? "bg-green-500 text-white" : "bg-red-500 text-white"
            }`}
        >
            {message}
        </div>
    );
};

export default Notification;