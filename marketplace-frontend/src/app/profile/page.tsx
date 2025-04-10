
"use client";
import { useEffect, useState } from "react";
import { useSession } from "next-auth/react";
import { cancelBooking } from "@/utils/api";
import LoadingScreen from "../components/LoadingScreen";
import Notification from "../components/Notification";
import { motion } from "framer-motion";

export default function Profile() {
    const [bookings, setBookings] = useState<any[]>([]);
    const [loading,setLoading] = useState(false)
    const [notification, setNotification] = useState<{
        message: string;
        type: "success" | "error";
    } | null>(null)
    const { data: session } = useSession() as any;

    useEffect(() => {

        const fetchBookings = async () => {
            const response = await fetch(`http://localhost:8080/bookings/user/${session.user.id}`);
            const data = await response.json();
            console.log(data)
            setBookings(data);
        };

        fetchBookings();
    }, [session]);

    const handleCancel = async (bookingId: string) => {
        setLoading(true)
        try{
            await cancelBooking(bookingId);
            setNotification({message:"Успешная отмена",type:"success"})
            setBookings(bookings.filter((b) => b.id !== bookingId));
        } catch(e) {
            console.error(e)
            setNotification({message:"Не удалось отменить",type:"error"})
        } finally {
            setLoading(false)
        }
    };

    return (
        <div className="min-h-screen w-screen overflow-x-hidden bg-gray-100 py-8">
            <LoadingScreen isLoading={loading} />

            {notification && (
                <Notification message={notification.message} type={notification.type} />
            )}

            <div className="max-w-4xl mx-auto mt-16 px-4">
                {/* Анимированный заголовок */}
                <motion.h1
                    initial={{ opacity: 0, y: -20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}
                    className="text-3xl font-bold mb-8 text-slate-700 text-center"
                >
                    Профиль пользователя{" "}
                    <span className="text-blue-500">{session?.user?.username}</span>
                </motion.h1>

                {/* Анимированный список бронирований */}
                <motion.div
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ duration: 0.5, delay: 0.2 }}
                    className="space-y-4"
                >
                    {bookings.length === 0 ? (
                        <p className="text-center text-gray-600">Бронирования не найдены.</p>
                    ) : (
                        bookings.map((booking) => (
                            <motion.div
                                key={booking.id}
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.5, delay: 0.1 * booking.id }}
                                className="bg-white p-4 rounded-lg shadow-md flex justify-between items-center"
                            >
                                <div>
                                    <strong className="block text-slate-700">
                                        {booking.route.origin} → {booking.route.destination}
                                    </strong>
                                    <span className="block text-sm text-gray-600">
                                        Отправление:{" "}
                                        {new Date(booking.route.departureTime).toLocaleString()}
                                    </span>
                                    <span className="block text-sm text-gray-600">
                                        Транспорт: {booking.route.transportType}
                                    </span>
                                </div>
                                <button
                                    onClick={() => handleCancel(booking.id)}
                                    className="bg-red-500 text-white py-2 px-4 rounded-lg hover:bg-red-600 transition duration-300"
                                >
                                    Отменить
                                </button>
                            </motion.div>
                        ))
                    )}
                </motion.div>
            </div>
        </div>
    );
}