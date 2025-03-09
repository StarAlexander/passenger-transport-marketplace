
"use client";
import { FC, useEffect, useState } from "react";
import SearchForm from "./components/SearchForm";
import RouteList from "./components/RouteList";
import { searchRoutes, Route, bookTicket,searchRoutesByDate} from "../utils/api";
import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import Header from "./components/Header";
import LoadingScreen from "./components/LoadingScreen";
import Notification from "./components/Notification";





const Home: FC = () => {
    const router = useRouter();
    const {data:session} = useSession() as any
    const [bookedRoutes, setBookedRoutes] = useState<number[]>([])
    const [loading, setLoading] = useState(false);
    const [notification, setNotification] = useState<{
        message: string;
        type: "success" | "error";
    } | null>(null);
    useEffect(() => {
        
        const checkUserExists = async () => {
            setLoading(true)
            try {
                await new Promise((resolve) => setTimeout(resolve, 1000));
                const response = await fetch("/api/check-user");
                const data = await response.json();
                if (!data.exists) {
                    router.push("/auth/signin");
                }
            } catch (error) {
                console.error("Error checking user existence:", error);
                router.push("/auth/signin");
            } finally {
                setLoading(false)
            }
        };
        checkUserExists();
    }, [router]);

    const [routes, setRoutes] = useState<Route[]>([]);
    const handleSearch = async (params: {
        origin: string;
        destination: string;
        departureTime: string;
        transportType?: string;
    }) => {
        setLoading(true)
        try{
            const results = await searchRoutes({...params,userId:session.user.id});
            setRoutes(results);
        } catch(e) {
            console.log(e)
            setNotification({message:"Search failed",type:"error"})
        } finally {
            setLoading(false)
        }
        
    };


    const handleSearchByDate = async (params: { departureTime:string }) => {
        setLoading(true);
        try {
            const results = await searchRoutesByDate({
                ...params,
                userId: session?.user?.id.toString() || "",
            });
            setRoutes(results);
        } catch (error) {
            console.error("Search by date error:", error);
            setNotification({ message: "Search by date failed", type: "error" });
        } finally {
            setLoading(false); 
        }
    };


    const handleBookTicket = async (routeId: string) => {
        if (!session?.user?.id) {
            console.error("User ID is not available in the session")
            return
        }
        setLoading(true)
        try {
            await bookTicket(routeId, session.user.id)
            setNotification({message:"Booking successful",type:"success"})
            setBookedRoutes(prev => [...prev,Number(routeId)])
        } catch (error) {
            console.error("Booking error:",error)
            setNotification({message:"Booking failed",type:"error"})
        } finally {
            setLoading(false)
        }
    }


    return (
        <>
        <div className="min-h-screen w-screen overflow-x-visible bg-gray-100 py-8">
            <LoadingScreen isLoading={loading}/>

            {notification && (
                <Notification message={notification.message} type={notification.type}/>
            )}
        <Header/>
            <div className="max-w-4xl mx-auto mt-16 px-4">
                <SearchForm onSearch={handleSearch} onSearchByDate={handleSearchByDate} />
                <div className="mt-8">
                    <RouteList routes={routes}
                    onBookTicket={handleBookTicket}
                    bookedRoutes = {bookedRoutes} />
                </div>
            </div>
        </div>
        </>);
};

export default Home;