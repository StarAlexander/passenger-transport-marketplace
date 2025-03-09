import NextAuth from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";
import axios from "axios";

const authOptions = {
    providers: [
        CredentialsProvider({
            name: "Credentials",
            credentials: {
                email: { label: "Email", type: "text" },
                password: { label: "Password", type: "password" },
            },
            async authorize(credentials) {
                try {
                    const response = await axios.post("http://localhost:8080/users/login", {
                        email: credentials?.email,
                        password: credentials?.password,
                    });
                    if (response.status == 401) return null
                    const user = response.data;
                    if (user) {
                        return { id: user.id.toString(), email: user.email, name: user.username };
                    } else {
                        return null;
                    }
                } catch (error) {
                    console.error("Login error:", error);
                    return null;
                }
            },
        }),
    ],
    callbacks: {
        async jwt({ token, user }) {
            if (user) {
                token.id = user.id
                token.email = user.email
                token.name = user.name
            }
            return token;
        },
        async session({ session, token }) {
            if (session?.user && token?.id) {
                session.user.id = token.id
                session.user.email = token.email
                session.user.name = token.name
            }
            return session;
        },
    },
    pages: {
        signIn: "/auth/signin",
    },
    secret: process.env.NEXTAUTH_SECRET,
};

const handler = NextAuth(authOptions);

export { handler as GET, handler as POST, authOptions };
