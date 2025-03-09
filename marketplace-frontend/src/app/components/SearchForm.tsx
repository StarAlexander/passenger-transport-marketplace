import { FC, useState } from "react";

interface SearchFormProps {
    onSearch: (params: {
        origin: string;
        destination: string;
        departureTime: string;
        transportType?: string;
    }) => void;
    onSearchByDate: (params: { departureTime:string }) => void
}

const SearchForm: FC<SearchFormProps> = ({ onSearch,onSearchByDate }) => {
    const [origin, setOrigin] = useState("");
    const [mode, setMode] = useState<"params"|"date">("params")
    const [destination, setDestination] = useState("");
    const [departureTime, setDepartureTime] = useState(new Date().toISOString().slice(0, 16));
    const [transportType, setTransportType] = useState("mixed");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (mode == "params")
        onSearch({ origin, destination, departureTime, transportType });
        else if (mode == "date") onSearchByDate({departureTime})
    };

    return (
        <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md">
            
            <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                    Select search mode:
                </label>
                <div className="flex space-x-4">
                    <button
                        type="button"
                        onClick={() => setMode("params")}
                        className={`${
                            mode === "params" ? "bg-blue-500 text-white" : "bg-gray-200 text-gray-700"
                        } py-2 px-4 rounded-lg hover:bg-blue-600 transition duration-300`}
                    >
                        By Parameters
                    </button>
                    <button
                        type="button"
                        onClick={() => setMode("date")}
                        className={`${
                            mode === "date" ? "bg-blue-500 text-white" : "bg-gray-200 text-gray-700"
                        } py-2 px-4 rounded-lg hover:bg-blue-600 transition duration-300`}
                    >
                        By Date/Time
                    </button>
                </div>
            </div>
            {mode == "params" && (
                <div className="space-y-4">
                <input
                    type="text"
                    placeholder="Origin"
                    value={origin}
                    onChange={(e) => setOrigin(e.target.value)}
                    className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
                />
                <input
                    type="text"
                    placeholder="Destination"
                    value={destination}
                    onChange={(e) => setDestination(e.target.value)}
                    className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
                />
                <select
                    value={transportType}
                    onChange={(e) => setTransportType(e.target.value)}
                    className="w-full p-2 border border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
                >
                    <option value="airplane">Aviation</option>
                    <option value="train">Railway</option>
                    <option value="bus">Bus</option>
                    <option value="mixed">Mixed</option>
                </select>
                </div>
            )}
            
                <input
                    type="datetime-local"
                    value={departureTime}
                    onChange={(e) => setDepartureTime(e.target.value)}
                    className="w-full p-2 border my-4 border-gray-300 rounded-lg text-slate-700 focus:ring-2 focus:ring-blue-500"
                />
                
                <button
                    type="submit"
                    className="w-full bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 transition duration-300"
                >
                    Search
                </button>
        </form>
    );
};

export default SearchForm;
