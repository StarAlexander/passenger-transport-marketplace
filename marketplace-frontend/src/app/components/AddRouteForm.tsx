import { useState } from "react";

interface AddRouteFormProps {
    onAddRoute: (route: any) => void;
}

const AddRouteForm: React.FC<AddRouteFormProps> = ({ onAddRoute }) => {
    const [origin, setOrigin] = useState("");
    const [destination, setDestination] = useState("");
    const [transportType, setTransportType] = useState("airplane");
    const [departureTime, setDepartureTime] = useState(new Date().toISOString().slice(0, 16));
    const [arrivalTime, setArrivalTime] = useState(new Date().toISOString().slice(0, 16));

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onAddRoute({
            origin,
            destination,
            transportType,
            departureTime,
            arrivalTime,
        });
        setOrigin("");
        setDestination("");
        setDepartureTime(new Date().toISOString().slice(0, 16));
        setArrivalTime(new Date().toISOString().slice(0, 16));
    };

    return (
        <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md space-y-4">
            <h2 className="text-xl font-bold text-slate-700">Добавить новый маршрут</h2>
            <input
                type="text"
                placeholder="Откуда"
                value={origin}
                onChange={(e) => setOrigin(e.target.value)}
                className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
            />
            <input
                type="text"
                placeholder="Куда"
                value={destination}
                onChange={(e) => setDestination(e.target.value)}
                className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
            />
            <select
                value={transportType}
                onChange={(e) => setTransportType(e.target.value)}
                className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
            >
                <option value="AIRPLANE">Самолет</option>
                <option value="TRAIN">Поезд</option>
                <option value="BUS">Автобус</option>
            </select>
            <input
                type="datetime-local"
                value={departureTime}
                onChange={(e) => setDepartureTime(e.target.value)}
                className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
            />
            <input
                type="datetime-local"
                value={arrivalTime}
                onChange={(e) => setArrivalTime(e.target.value)}
                className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
            />
            <button
                type="submit"
                className="w-full bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition duration-300"
            >
                Добавить
            </button>
        </form>
    );
};

export default AddRouteForm;