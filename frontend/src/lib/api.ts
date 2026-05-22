import type {
  Appointment,
  AuthResponse,
  CreateAppointmentRequest,
  LoginRequest,
  NearbySearch,
  PageResponse,
  Payment,
  Professional,
  RegisterRequest,
  Review,
  User,
} from '../types'

const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

type RequestOptions = RequestInit & {
  token?: string | null
}

async function request<T>(path: string, options: RequestOptions = {}): Promise<T> {
  const headers = new Headers(options.headers)
  headers.set('Accept', 'application/json')

  if (options.body && !headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json')
  }

  if (options.token) {
    headers.set('Authorization', `Bearer ${options.token}`)
  }

  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers,
  })

  if (!response.ok) {
    const message = await readErrorMessage(response)
    throw new Error(message)
  }

  if (response.status === 204) {
    return undefined as T
  }

  return response.json() as Promise<T>
}

async function readErrorMessage(response: Response) {
  try {
    const body = await response.json()
    return body.message ?? body.error ?? 'No se pudo completar la solicitud'
  } catch {
    return 'No se pudo completar la solicitud'
  }
}

export const api = {
  login(payload: LoginRequest) {
    return request<AuthResponse>('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify(payload),
    })
  },

  register(payload: RegisterRequest) {
    return request<AuthResponse>('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify(payload),
    })
  },

  getProfessionals(token: string) {
    return request<Professional[]>('/api/professionals', { token })
  },

  getMyProfessionalProfile(token: string) {
    return request<Professional>('/api/professionals/me', { token })
  },

  getNearbyProfessionals(token: string, search: NearbySearch) {
    const params = new URLSearchParams({
      latitude: String(search.latitude),
      longitude: String(search.longitude),
      radiusKm: String(search.radiusKm),
    })

    return request<Professional[]>(`/api/professionals/nearby?${params.toString()}`, { token })
  },

  getAppointments(token: string, page = 0, size = 10) {
    return request<PageResponse<Appointment>>(`/api/appointments?page=${page}&size=${size}`, { token })
  },

  createAppointment(token: string, payload: CreateAppointmentRequest) {
    return request<Appointment>('/api/appointments', {
      method: 'POST',
      token,
      body: JSON.stringify(payload),
    })
  },

  getMyProfessionalAppointments(token: string, page = 0, size = 10) {
    return request<PageResponse<Appointment>>(`/api/appointments/professional/me?page=${page}&size=${size}`, { token })
  },

  getMyClientAppointments(token: string, page = 0, size = 10) {
    return request<PageResponse<Appointment>>(`/api/appointments/client/me?page=${page}&size=${size}`, { token })
  },

  cancelAppointment(token: string, id: number) {
    return request<void>(`/api/appointments/${id}/cancel`, {
      method: 'PATCH',
      token,
    })
  },

  completeAppointment(token: string, id: number) {
    return request<Appointment>(`/api/appointments/${id}/complete`, {
      method: 'PATCH',
      token,
    })
  },

  getReviews(token: string, page = 0, size = 10) {
    return request<PageResponse<Review>>(`/api/reviews?page=${page}&size=${size}`, { token })
  },

  approveReview(token: string, id: number) {
    return request<Review>(`/api/reviews/${id}/approve`, {
      method: 'PATCH',
      token,
    })
  },

  rejectReview(token: string, id: number) {
    return request<Review>(`/api/reviews/${id}/reject`, {
      method: 'PATCH',
      token,
    })
  },

  getPaymentsByAppointment(token: string, appointmentId: number) {
    return request<Payment[]>(`/api/payments/appointments/${appointmentId}`, { token })
  },

  createCheckout(token: string, appointmentId: number) {
    return request<Payment>(`/api/payments/appointments/${appointmentId}/checkout`, {
      method: 'POST',
      token,
    })
  },

  getUsers(token: string) {
    return request<User[]>('/api/users', { token })
  },

  deleteUser(token: string, id: number) {
    return request<void>(`/api/users/${id}`, {
      method: 'DELETE',
      token,
    })
  },
}
