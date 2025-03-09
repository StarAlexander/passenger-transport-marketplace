import NextAuth from "next-auth"
import { authOptions } from "@/authOptions"


export const GET = (req,res) => NextAuth(req,res,authOptions)
export const POST = (req,res) => NextAuth(req,res,authOptions)