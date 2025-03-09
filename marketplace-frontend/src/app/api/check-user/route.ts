import { getServerSession } from "next-auth";
import { NextRequest, NextResponse } from "next/server";
import { authOptions } from "@/authOptions";
import { backendUrl } from "@/utils/api";


export async function GET(req: NextRequest) {
    try {
        const session = await getServerSession(authOptions)
        if (!session?.user?.email) {
            return NextResponse.json({exists:false}, {status:401})
        }

        const response = await fetch(`${backendUrl}/users/${session.user.id}`)
        if (!response.ok) {
        return NextResponse.json({exists:false}, {status:401})
        }
        return NextResponse.json({exists:true}, {status:200})

    } catch(error) {
        console.log(error)
        return NextResponse.json({error:"Internal Server Error"}, {status:500})
    }
}