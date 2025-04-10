
export const backendUrl = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080"


export interface Route {
    id: string;
    origin: string;
    destination: string;
    departureTime: string;
    transportType:string
    arrivalTime: string;
    isCancelled:boolean;
}

interface SearchParams {
    origin: string;
    destination: string;
    departureTime: string;
    transportType?: string;
    userId:number
}




export const searchRoutes = async (params: SearchParams) => {
    const urlParams = new URLSearchParams({
        "transportType":params.transportType ?? "mixed",
        "userId":params.userId.toString()
    })
    if (params.departureTime) urlParams.append('departureTime',params.departureTime)
    if (params.origin) urlParams.append('origin',params.origin)
    if (params.destination) urlParams.append('destination',params.destination)
    const response = await fetch(`http://localhost:8080/routes/search?` + urlParams.toString())

    if (!response.ok) {
        console.log(await response.text())
        throw new Error("Failed to fetch")
    }
    const res = await response.json()
    return res
}



export const bookTicket = async (routeIds: number[],userId:number) => {
    const params = new URLSearchParams({
        "userId":String(userId)
    })
    for (const r of routeIds) {
        params.append("routeIds",String(r))
    }
    const response = await fetch(`http://localhost:8080/bookings`, {
        method: "POST",
        headers: { "Content-Type":"application/x-www-form-urlencoded" },
        body: params
    })


    if (!response.ok) {
        throw new Error("Failed to book ticket");
    }

    return response.json();
};

export const cancelBooking = async (bookingId:string)=>{
    console.log(bookingId)
    const response = await fetch(`http://localhost:8080/bookings/${bookingId}`, {
        method:"DELETE"
    })

    if (!response.ok) {
        throw new Error("Failed to cancel booking")
    }

}


export const searchRoutesByDate = async (params: {
    departureTime:string;
    userId:string
}) => {
    const response = await fetch(`http://localhost:8080/routes/by-date?`+new URLSearchParams({
        "departureTime":params.departureTime,
        "userId":params.userId
    }));

    if (!response.ok) {
        throw new Error("Failed to fetch routes by date");
    }

    return response.json();
};



export const findOptimalRoute = async (
    params: { origin: string; destination: string; desiredDepartureTime: string
     }
) => {
    const urlParams = new URLSearchParams({
        origin: params.origin,
        destination: params.destination,
        desiredDepartureTime: params.desiredDepartureTime
    });

    const response = await fetch(`http://localhost:8080/routes/optimal?${urlParams.toString()}`);

    if (!response.ok) {
        throw new Error("Failed to find optimal route");
    }

    return response.json();
};


export const fetchTotalUsers = async () => {
    const response = await fetch(`http://localhost:8080/stats/total-users`);
    return await response.json();
};

export const fetchTotalRoutes = async () => {
    const response = await fetch(`http://localhost:8080/stats/total-routes`);
    return await response.json();
};

export const fetchAverageBookingsPerUser = async () => {
    const response = await fetch(`http://localhost:8080/stats/average-bookings-per-user`);
    return await response.json();
};

export const fetchPopularRoutes = async () => {
    const response = await fetch(`http://localhost:8080/stats/popular-routes`);
    return await response.json();
};

export const fetchBookingsByTransportType = async () => {
    const response = await fetch(`http://localhost:8080/stats/bookings-by-transport-type`);
    return await response.json();
};