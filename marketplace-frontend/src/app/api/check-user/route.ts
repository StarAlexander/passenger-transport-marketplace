import { getServerSession } from "next-auth";
import { NextRequest, NextResponse } from "next/server";
import { authOptions } from "../auth/[...nextauth]/route";





export async function GET(req: NextRequest) {
    try {
        const session = await getServerSession(authOptions)
        console.log(session)
        if (!session?.user?.email) {
            return NextResponse.json({exists:false}, {status:401})
        }

        const response = await fetch(`http://localhost:8080/users/${session.user.id}`)
        if (!response.ok) {
            console.log("LSkdjflksjdlf")
            console.log(await response.json())
        return NextResponse.json({exists:false}, {status:401})
        }

        console.log(await response.json())
        return NextResponse.json({exists:true}, {status:200})

    } catch(error) {
        console.log(error)
        return NextResponse.json({error:"Internal Server Error"}, {status:500})
    }
}