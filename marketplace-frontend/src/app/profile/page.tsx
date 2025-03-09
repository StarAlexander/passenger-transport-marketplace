// app/profile/page.tsx
"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useSession } from "next-auth/react";
import { cancelBooking } from "@/utils/api";
import Header from "../components/Header";
import LoadingScreen from "../components/LoadingScreen";
import Notification from "../components/Notification";

export default function Profile() {
    const [bookings, setBookings] = useState<any[]>([]);
    const [loading,setLoading] = useState(false)
    const [notification, setNotification] = useState<{
        message: string;
        type: "success" | "error";
    } | null>(null)
    const { data: session } = useSession() as any;
    const router = useRouter();

    useEffect(() => {
        if (!session?.user?.id) {
            router.push("/auth/signin");
            return;
        }

        const fetchBookings = async () => {
            const response = await fetch(`http://localhost:8080/bookings/user/${session.user.id}`);
            const data = await response.json();
            setBookings(data);
        };

        fetchBookings();
    }, [session, router]);

    const handleCancel = async (bookingId: string) => {
        setLoading(true)
        try{
            await cancelBooking(bookingId);
            setNotification({message:"Cancellation successful",type:"success"})
            setBookings(bookings.filter((b) => b.id !== bookingId));
        } catch(e) {
            console.error(e)
            setNotification({message:"Cancellation failed",type:"error"})
        } finally {
            setLoading(false)
        }
    };

    return (
        <div className="min-h-screen w-screen overflow-x-hidden bg-gray-100 py-8">
            <LoadingScreen isLoading={loading}/>


            {notification && (
                <Notification message={notification.message} type={notification.type}/>
            )}
            <Header/>
            <div className="max-w-4xl mx-auto mt-16 px-4">
                <h1 className="text-3xl font-bold mb-8 text-slate-700 text-center">
                    Your Bookings
                </h1>
                <div className="space-y-4">
                    {bookings.length === 0 ? (
                        <p className="text-center text-gray-600">No bookings found.</p>
                    ) : (
                        bookings.map((booking) => (
                            <div
                                key={booking.id}
                                className="bg-white p-4 rounded-lg shadow-md flex justify-between items-center"
                            >
                                <div>
                                    <strong className="block text-slate-700">
                                        {booking.route.origin} → {booking.route.destination}
                                    </strong>
                                    <span className="block text-sm text-gray-600">
                                        Departure:{" "}
                                        {new Date(booking.route.departureTime).toLocaleString()}
                                    </span>
                                    <span className="block text-sm text-gray-600">
                                        Transport: {booking.route.transportType}
                                    </span>
                                </div>
                                <button
                                    onClick={() => handleCancel(booking.id)}
                                    className="bg-red-500 text-white py-2 px-4 rounded-lg hover:bg-red-600 transition duration-300"
                                >
                                    Cancel
                                </button>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
}