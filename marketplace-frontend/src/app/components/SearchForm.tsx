import { FC, useState } from "react";

interface SearchFormProps {
    onSearch: (params: { origin: string; destination: string; departureTime: string; transportType?: string }) => void;
    onSearchByDate: (params: { departureTime: string }) => void;
    onSearchWithConnections: (params : {origin:string, destination:string, departureTime:string}) => void
}


const cities = [
    "Moscow", "Berlin", "Tokyo", "Osaka", "London", "Toronto", "New York", "Madrid",
    "Paris", "Rome"
];

const SearchForm: FC<SearchFormProps> = ({ onSearch, onSearchByDate,onSearchWithConnections }) => {
    const [mode, setMode] = useState<"params" | "date" | "connections">("params");
    const [origin, setOrigin] = useState("");
    const [isTypingOrigin,setIsTypingOrigin] = useState(false)
    const [isTypingDestination,setIsTypingDestination] = useState(false)
    const [destination, setDestination] = useState("");
    const [departureTime, setDepartureTime] = useState('');
    const [transportType, setTransportType] = useState("mixed");


    const getFilteredCities = (inputValue: string) => {
        return cities.filter((city) =>
            city.toLowerCase().includes(inputValue.toLowerCase())
        );
    };
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (mode === "params") {
            onSearch({ origin, destination, departureTime, transportType });
        } else if (mode === "date") {
            onSearchByDate({ departureTime });
        }
        else if (mode === "connections") {
            onSearchWithConnections({ origin, destination, departureTime });
        }
    };

    return (
        <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md space-y-4">
            <div className="flex space-x-4 border-b border-gray-200 pb-4">
                <button
                    type="button"
                    onClick={() => setMode("params")}
                    className={`${
                        mode === "params" ? "border-b-2 border-blue-500 bg-white text-blue-500" :"bg-blue-500 text-white"
                    } pb-2 transition duration-300`}
                >
                    Прямой
                </button>

                <button
                type="button"
                onClick={() => setMode("connections")}
                className={`${
                    mode === "connections" ? "border-b-2 border-blue-500 bg-white text-blue-500" :"bg-blue-500 text-white"
                } pb-2 transition duration-300`}
            >
                Оптимальный
            </button>
                <button
                    type="button"
                    onClick={() => setMode("date")}
                    className={`${
                        mode === "date" ? "border-b-2 border-blue-500 bg-white text-blue-500" :"bg-blue-500 text-white"
                    } pb-2 transition duration-300`}
                >
                    По дате
                </button>

                
            </div>
            {(mode === "params" || mode === "connections") && (
                <>
                    <div className="relative">
                        <input
                            type="text"
                            placeholder="Откуда"
                            value={origin}
                            onChange={(e) => setOrigin(e.target.value)}
                            onFocus={()=> setIsTypingOrigin(true)}
                            onBlur={()=>setTimeout(() => setIsTypingOrigin(false), 200)}
                            className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
                        />
                        {origin && isTypingOrigin && (
                            <ul className="absolute z-10 w-full bg-white border border-gray-300 rounded-lg mt-1 max-h-40 overflow-y-auto">
                                {getFilteredCities(origin).map((city) => (
                                    <li
                                        key={city}
                                        onClick={(e) => {
                                            e.stopPropagation()
                                            setOrigin(city)
                                            setIsTypingOrigin(false)
                                        }}
                                        className="p-2 cursor-pointer hover:bg-gray-100"
                                    >
                                        {city}
                                    </li>
                                ))}
                            </ul>
                        )}
                    </div>

                    {/* Поле "Куда" */}
                    <div className="relative">
                        <input
                            type="text"
                            placeholder="Куда"
                            value={destination}
                            onChange={(e) => setDestination(e.target.value)}
                            onFocus={()=>setIsTypingDestination(true)}
                            onBlur={()=>setTimeout(() => setIsTypingDestination(false), 200)}
                            className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
                        />
                        {destination && isTypingDestination && (
                            <ul className="absolute z-10 w-full bg-white border border-gray-300 rounded-lg mt-1 max-h-40 overflow-y-auto">
                                {getFilteredCities(destination).map((city) => (
                                    <li
                                        key={city}
                                        onClick={(e) => {
                                            e.stopPropagation()
                                            setDestination(city)
                                            setIsTypingDestination(false)
                                        }}
                                        className="p-2 cursor-pointer hover:bg-gray-100"
                                    >
                                        {city}
                                    </li>
                                ))}
                            </ul>
                        )}
                    </div>
                    <select
                        value={transportType}
                        onChange={(e) => setTransportType(e.target.value)}
                        className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    >
                        <option value="airplane">Самолет</option>
                        <option value="train">Поезд</option>
                        <option value="bus">Автобус</option>
                        <option value="mixed">Любой</option>
                    </select>
                </>
            )}
            <input
                type="datetime-local"
                value={departureTime}
                onChange={(e) => setDepartureTime(e.target.value)}
                className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
            />
            <button
                type="submit"
                onClick={handleSubmit}
                className="w-full bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition duration-300"
            >
                Искать
            </button>
        </form>
    );
};

export default SearchForm;