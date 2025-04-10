'use client'

import { useSession } from "next-auth/react";
import { usePathname, useRouter } from "next/navigation";
import { useEffect } from "react";





export default function AuthProvider({children}: {children:React.ReactNode}) {

    const {status} = useSession()
    const router = useRouter()
    const pathname = usePathname()

    useEffect(()=> {
        if (pathname == "/auth/signin" || pathname == "/auth/signup") return;
        if (status == "unauthenticated") router.replace('/auth/signin')
    }, [status])


    if (status == "loading") return null

    return children

}