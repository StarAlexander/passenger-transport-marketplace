import axios from "axios";

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
    const response = await fetch("http://localhost:8080/routes/search?" + new URLSearchParams({
        "origin":params.origin,
        "destination":params.destination,
        "departureTime":params.departureTime,
        "transportType":params.transportType ?? "mixed",
        "userId":params.userId.toString()
    }).toString())

    if (!response.ok) {
        console.log(await response.json())
        throw new Error("Failed to fetch")
    }
    const res = await response.json()
    console.log(res)
    return res
}



export const bookTicket = async (routeId: string,userId:number) => {
    const response = await fetch(`http://localhost:8080/bookings`, {
        method: "POST",
        headers: { "Content-Type":"application/x-www-form-urlencoded" },
        body: new URLSearchParams({
            "userId":String(userId),
            "routeId":routeId
        })
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