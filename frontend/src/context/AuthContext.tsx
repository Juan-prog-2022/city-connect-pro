import { createContext, useContext, useMemo, useState } from 'react'
import { api } from '../lib/api'
import type { AuthResponse, LoginRequest, Role } from '../types'

type AuthState = {
  token: string | null
  userId: number | null
  username: string | null
  roles: Role[]
}

type AuthContextValue = AuthState & {
  isAuthenticated: boolean
  login: (payload: LoginRequest) => Promise<void>
  logout: () => void
}

const STORAGE_KEY = 'city-connect-auth'
const AuthContext = createContext<AuthContextValue | null>(null)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [auth, setAuth] = useState<AuthState>(() => readStoredAuth())

  const value = useMemo<AuthContextValue>(() => ({
    ...auth,
    isAuthenticated: Boolean(auth.token),
    async login(payload) {
      const response = await api.login(payload)
      const nextAuth = toAuthState(response)
      localStorage.setItem(STORAGE_KEY, JSON.stringify(nextAuth))
      setAuth(nextAuth)
    },
    logout() {
      localStorage.removeItem(STORAGE_KEY)
      setAuth({ token: null, userId: null, username: null, roles: [] })
    },
  }), [auth])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)

  if (!context) {
    throw new Error('useAuth must be used within AuthProvider')
  }

  return context
}

function toAuthState(response: AuthResponse): AuthState {
  return {
    token: response.token,
    userId: response.userId,
    username: response.username,
    roles: response.roles ?? [],
  }
}

function readStoredAuth(): AuthState {
  const fallback = { token: null, userId: null, username: null, roles: [] }
  const stored = localStorage.getItem(STORAGE_KEY)

  if (!stored) {
    return fallback
  }

  try {
    return JSON.parse(stored) as AuthState
  } catch {
    localStorage.removeItem(STORAGE_KEY)
    return fallback
  }
}
