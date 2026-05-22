export type Role = 'ROLE_ADMIN' | 'ROLE_USER' | 'ROLE_PRO' | string

export type AuthResponse = {
  token: string
  username: string
  userId: number
  roles: Role[]
  message: string
}

export type LoginRequest = {
  usernameOrEmail: string
  password: string
}

export type RegisterRequest = {
  firstName: string
  lastName: string
  dni: string
  email: string
  username: string
  role: Role
  specialty?: string
  licenseNumber?: string
  password: string
}

export type Professional = {
  id: number
  firstName: string
  lastName: string
  email: string
  phone?: string | null
  skills: string[]
  certifications: string[]
  city?: string | null
  address?: string | null
  latitude?: number | null
  longitude?: number | null
  hourlyRate: number
  currency: string
  yearsOfExperience: number
  averageRating: number
  reviewCount: number
  specialty?: string | null
}

export type NearbySearch = {
  latitude: number
  longitude: number
  radiusKm: number
}

export type PageResponse<T> = {
  content: T[]
  page?: {
    size: number
    number: number
    totalElements: number
    totalPages: number
  }
}

export type Appointment = {
  id: number
  clientId: number
  professionalId: number
  appointmentDateTime: string
  duration: number
  status: string
  paymentStatus: string
  type: string
  reason?: string | null
  createdAt: string
}

export type CreateAppointmentRequest = {
  clientId: number
  professionalId: number
  appointmentDateTime: string
  duration: number
  type: 'ONLINE' | 'IN_PERSON'
  reason?: string
}

export type Review = {
  id: number
  clientId: number
  clientName: string
  professionalId: number
  professionalName: string
  title: string
  comment: string
  rating: number
  status: string
  reviewDate: string
  updatedAt?: string | null
}

export type Payment = {
  id: number
  appointmentId: number
  provider: string
  status: string
  amount: number
  currencyId: string
  description: string
  paymentUrl?: string | null
  externalPreferenceId?: string | null
  externalPaymentId?: string | null
  mpStatusDetail?: string | null
  createdAt: string
  updatedAt?: string | null
}

export type User = {
  id: number
  firstName: string
  lastName: string
  email: string
  username: string
}
