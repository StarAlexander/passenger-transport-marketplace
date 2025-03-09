// components/RouteList.tsx
import { Route } from "@/utils/api"
import { FaPlane, FaTrain, FaBus } from "react-icons/fa"
interface RouteListProps {
    routes: Route[];
    onBookTicket: (routeId: string) => void;
    bookedRoutes: number[]
}

const RouteList: React.FC<RouteListProps> = ({ routes, onBookTicket,bookedRoutes }) => {
    console.log(routes)
    if (routes.length === 0) {
        return <p className="text-center text-gray-600">No routes found.</p>;
    }

    const getTransportIcon = (transportType: string) => {
        switch (transportType.toLowerCase()) {
            case "airplane":
                return <FaPlane className="inline mr-2 text-blue-500" />
            case "train":
                return <FaTrain className="inline mr-2 text-green-500" />
            case "bus":
                return <FaBus className="inline mr-2 text-yellow-500" />
            default:
                return null
        }
    }

    return (
        <div className="space-y-4">
            {routes.map((route:any) => (
                !bookedRoutes.includes(Number(route.id)) && (
                <div
                    key={route.id}
                    className="bg-white p-4 rounded-lg shadow-md flex justify-between items-center"
                >
                    <div>
                        <strong className="block text-slate-700">
                            {route.origin} → {route.destination}
                        </strong>
                        <span className="block text-sm text-gray-600">
                            {getTransportIcon(route.transportType)}
                            {route.transportType.charAt(0).toUpperCase() + route.transportType.slice(1)}
                        </span>
                        <span className="block text-sm text-gray-600">
                            Departure: {new Date(route.departureTime).toLocaleString()}
                        </span>
                        <span className="block text-sm text-gray-600">
                            Arrival: {new Date(route.arrivalTime).toLocaleString()}
                        </span>
                    </div>
                    <div>
                            <button
                                onClick={() => onBookTicket(route.id)}
                                className="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition duration-300"
                            >
                                Book
                            </button>
                    </div>
                </div>
            )))}
        </div>
    );
};

export default RouteList;