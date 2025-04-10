import { FaPlane, FaTrain, FaBus } from "react-icons/fa";

interface Route {
    id: number;
    origin: string;
    destination: string;
    departureTime: string;
    arrivalTime: string;
    transportType: string;
}

interface RouteResponse {
    id: number;
    origin: string;
    destination: string;
    departureTime: string;
    arrivalTime: string;
    transportType: string;
    children: Route[];
}



interface RouteListProps {
    routes: RouteResponse[];
    onBookTicket: (routeIds: number[]) => void;
    bookedRoutes: number[];
}

const RouteList: React.FC<RouteListProps> = ({ routes, onBookTicket, bookedRoutes }) => {
    const getTransportIcon = (transportType: string) => {
        switch (transportType.toLowerCase()) {
            case "airplane":
                return <FaPlane className="inline mr-2 text-blue-500" />;
            case "train":
                return <FaTrain className="inline mr-2 text-green-500" />;
            case "bus":
                return <FaBus className="inline mr-2 text-yellow-500" />;
            default:
                return null;
        }
    };

    // Проверка, является ли маршрут полностью забронированным
    const isFullyBooked = (route: RouteResponse) => {
        const allRouteIds = [route.id, ...route.children.map((child) => child.id)];
        return allRouteIds.every((id) => bookedRoutes.includes(id));
    };

    // Проверка, является ли маршрут частично забронированным
    const isPartiallyBooked = (route: RouteResponse) => {
        const allRouteIds = [route.id, ...route.children.map((child) => child.id)];
        return allRouteIds.some((id) => bookedRoutes.includes(id)) && !isFullyBooked(route);
    };

    return (
        <div className="space-y-4">
            {routes.length === 0 ? (
                <p className="text-center text-gray-600">Маршруты не найдены.</p>
            ) : (
                routes.map((route) => {
                    if (isFullyBooked(route)) return null; // Пропускаем полностью забронированные маршруты

                    const partiallyBooked = isPartiallyBooked(route);

                    return (
                        <div
                            key={route.id}
                            className="bg-white p-4 rounded-lg shadow-md flex flex-col space-y-2 hover:shadow-lg transition duration-300"
                        >
                            <h3 className="text-lg font-bold text-slate-700">
                                Маршрут (Кол-во пересадок: {route.children.length})
                                {partiallyBooked && (
                                    <span className="ml-2 text-sm text-yellow-600">(Частично забронирован)</span>
                                )}
                            </h3>

                            {/* Основной маршрут */}
                            <div className="pl-4 border-l-2 border-gray-200">
                                <strong className="block text-slate-700">
                                    {getTransportIcon(route.transportType)}
                                    {route.origin} → {route.destination}
                                </strong>
                                <span className="block text-sm text-gray-600">
                                    Отправление: {new Date(route.departureTime).toLocaleString()}
                                </span>
                                <span className="block text-sm text-gray-600">
                                    Прибытие: {new Date(route.arrivalTime).toLocaleString()}
                                </span>
                                {bookedRoutes.includes(route.id) && (
                                    <span className="block text-sm text-red-500">Забронировано</span>
                                )}
                            </div>

                            {/* Дочерние маршруты (пересадки) */}
                            {route.children.map((child) => (
                                <div key={child.id} className="pl-4 border-l-2 border-gray-200">
                                    <strong className="block text-slate-700">
                                        {getTransportIcon(child.transportType)}
                                        {child.origin} → {child.destination}
                                    </strong>
                                    <span className="block text-sm text-gray-600">
                                        Отправление: {new Date(child.departureTime).toLocaleString()}
                                    </span>
                                    <span className="block text-sm text-gray-600">
                                        Прибытие: {new Date(child.arrivalTime).toLocaleString()}
                                    </span>
                                    {bookedRoutes.includes(child.id) && (
                                        <span className="block text-sm text-red-500">Забронировано</span>
                                    )}
                                </div>
                            ))}

                            {/* Кнопка бронирования */}
                            {!isFullyBooked(route) && (
                                <button
                                    onClick={() =>
                                        onBookTicket([route.id, ...route.children.map((r) => r.id)])
                                    }
                                    className="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition duration-300 transform hover:scale-105 mt-4"
                                >
                                    Забронировать
                                </button>
                            )}
                        </div>
                    );
                })
            )}
        </div>
    );
};

export default RouteList;