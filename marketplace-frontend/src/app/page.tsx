
"use client";
import { FC, useState } from "react";
import SearchForm from "./components/SearchForm";
import RouteList from "./components/RouteList";
import { searchRoutes, Route, bookTicket,searchRoutesByDate, findOptimalRoute} from "../utils/api";
import { useSession } from "next-auth/react";
import LoadingScreen from "./components/LoadingScreen";
import Notification from "./components/Notification";
import { motion } from "framer-motion";




const Home: FC = () => {
    const { data: session } = useSession() as any;
    const [bookedRoutes, setBookedRoutes] = useState<number[]>([]);
    const [loading, setLoading] = useState(false);
    const [notification, setNotification] = useState<{
        message: string;
        type: "success" | "error";
    } | null>(null);

    const [routes, setRoutes] = useState<any[]>([]);

    const handleSearch = async (params: {
        origin: string;
        destination: string;
        departureTime: string;
        transportType?: string;
    }) => {
        setLoading(true);
        try {
            const results = await searchRoutes({ ...params, userId: session.user.id });
            setRoutes(results);
        } catch (e) {
            console.error(e);
            setNotification({ message: "Ошибка поиска", type: "error" });
        } finally {
            setLoading(false);
        }
    };

    const handleSearchByDate = async (params: { departureTime: string }) => {
        setLoading(true);
        try {
            const results = await searchRoutesByDate({
                ...params,
                userId: session?.user?.id.toString() || "",
            });
            setRoutes(results);
        } catch (error) {
            console.error("Ошибка поиска по дате:", error);
            setNotification({ message: "Ошибка поиска по дате", type: "error" });
        } finally {
            setLoading(false);
        }
    };

    const handleSearchWithConnections = async (params: {origin:string, destination:string, departureTime:string}) => {
        setLoading(true)

        try {
            const results = await findOptimalRoute({
                ...params,
                desiredDepartureTime:params.departureTime
            })

            setRoutes(results)
            
        }
        catch (e) {
            console.log(e)
            setNotification({ message: "Ошибка поиска с пересадками", type: "error" })
        }
        finally {
            setLoading(false)
        }
    }

    const handleBookTicket = async (routeIds: number[]) => {
        if (!session?.user?.id) {
            console.error("User ID is not available in the session");
            return;
        }

        setLoading(true);
        try {
                await bookTicket(routeIds, session.user.id);
                setNotification({ message: "Бронирование успешно", type: "success" });
                setBookedRoutes((prev) => [...prev, ...routeIds]);
            }
             catch (error) {
            console.error("Ошибка бронирования:", error);
            setNotification({ message: "Ошибка бронирования", type: "error" });
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <div className="min-h-screen w-screen overflow-x-visible bg-gray-100 py-8">
                <LoadingScreen isLoading={loading} />
                {notification && (
                    <Notification message={notification.message} type={notification.type} />
                )}
                <div className="max-w-4xl mx-auto mt-16 px-4">
                    <motion.div
                        initial={{ opacity: 0, y: -20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5 }}
                    >
                        <SearchForm onSearchWithConnections={handleSearchWithConnections} onSearch={handleSearch} onSearchByDate={handleSearchByDate} />
                    </motion.div>
                    <motion.div
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        transition={{ duration: 0.5, delay: 0.2 }}
                        className="mt-8"
                    >
                        <RouteList
                            routes={routes}
                            onBookTicket={handleBookTicket}
                            bookedRoutes={bookedRoutes}
                        />
                    </motion.div>
                </div>
            </div>
        </>
    );
};

export default Home;