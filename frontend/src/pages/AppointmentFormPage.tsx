import { CalendarPlus } from 'lucide-react'
import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { DataState } from '../components/DataState'
import { PageHeader } from '../components/PageHeader'
import { useAuth } from '../context/AuthContext'
import { api } from '../lib/api'
import type { CreateAppointmentRequest, Professional, User } from '../types'

type AppointmentFormValues = {
  clientId: number
  professionalId: number
  appointmentDateTime: string
  duration: number
  type: 'ONLINE' | 'IN_PERSON'
  reason: string
}

export function AppointmentFormPage() {
  const { roles, token, userId } = useAuth()
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const [professionals, setProfessionals] = useState<Professional[]>([])
  const [users, setUsers] = useState<User[]>([])
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)
  const isProfessional = roles.includes('ROLE_PRO')
  const isClient = roles.includes('ROLE_USER')
  const isAdmin = roles.includes('ROLE_ADMIN')
  const requestedProfessionalId = Number(searchParams.get('professionalId') ?? 0)

  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm<AppointmentFormValues>({
    defaultValues: {
      clientId: userId ?? 0,
      professionalId: requestedProfessionalId || 0,
      appointmentDateTime: '',
      duration: 60,
      type: 'IN_PERSON',
      reason: '',
    },
  })

  useEffect(() => {
    void loadInitialData()
  }, [])

  async function loadInitialData() {
    if (!token) return
    setLoading(true)
    setError(null)

    try {
      const [professionalsData, usersData] = await Promise.all([
        api.getProfessionals(token),
        isProfessional || isAdmin ? api.getUsers(token) : Promise.resolve([]),
      ])

      let professionalId = requestedProfessionalId || 0

      if (isProfessional) {
        const myProfile = await api.getMyProfessionalProfile(token)
        professionalId = myProfile.id
      }

      setProfessionals(professionalsData)
      setUsers(usersData)
      reset({
        clientId: isClient ? userId ?? 0 : 0,
        professionalId,
        appointmentDateTime: '',
        duration: 60,
        type: 'IN_PERSON',
        reason: '',
      })
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'No se pudo preparar el formulario')
    } finally {
      setLoading(false)
    }
  }

  async function onSubmit(values: AppointmentFormValues) {
    if (!token) return
    setError(null)

    const payload: CreateAppointmentRequest = {
      ...values,
      clientId: Number(values.clientId),
      professionalId: Number(values.professionalId),
      duration: Number(values.duration),
    }

    try {
      await api.createAppointment(token, payload)
      navigate('/appointments')
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'No se pudo crear el turno')
    }
  }

  return (
    <div>
      <PageHeader
        title="Nuevo turno"
        description={isProfessional ? 'Carga un turno asistido para un cliente.' : 'Reserva una consulta con un profesional.'}
      />

      {loading && <DataState message="Preparando formulario..." />}
      {error && <div className="mb-4"><DataState message={error} /></div>}

      {!loading && (
        <form onSubmit={handleSubmit(onSubmit)} className="max-w-3xl rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
          <div className="grid gap-4 md:grid-cols-2">
            <label className="block">
              <span className="mb-1 block text-sm font-medium text-slate-700">Cliente</span>
              {isClient ? (
                <input
                  type="number"
                  readOnly
                  className="h-10 w-full rounded-md border border-slate-300 bg-slate-50 px-3 text-sm text-slate-500"
                  {...register('clientId', { valueAsNumber: true, required: true })}
                />
              ) : (
                <select
                  className="h-10 w-full rounded-md border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10"
                  {...register('clientId', { valueAsNumber: true, min: 1 })}
                >
                  <option value={0}>Selecciona un cliente</option>
                  {users.map((user) => (
                    <option key={user.id} value={user.id}>{user.firstName} {user.lastName} · {user.username}</option>
                  ))}
                </select>
              )}
              {errors.clientId && <p className="mt-1 text-sm text-red-600">Selecciona un cliente</p>}
            </label>

            <label className="block">
              <span className="mb-1 block text-sm font-medium text-slate-700">Profesional</span>
              <select
                disabled={isProfessional}
                className="h-10 w-full rounded-md border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10 disabled:bg-slate-50 disabled:text-slate-500"
                {...register('professionalId', { valueAsNumber: true, min: 1 })}
              >
                <option value={0}>Selecciona un profesional</option>
                {professionals.map((professional) => (
                  <option key={professional.id} value={professional.id}>
                    {professional.firstName} {professional.lastName} · {professional.specialty}
                  </option>
                ))}
              </select>
              {errors.professionalId && <p className="mt-1 text-sm text-red-600">Selecciona un profesional</p>}
            </label>

            <label className="block">
              <span className="mb-1 block text-sm font-medium text-slate-700">Fecha y hora</span>
              <input
                type="datetime-local"
                className="h-10 w-full rounded-md border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10"
                {...register('appointmentDateTime', { required: 'Selecciona fecha y hora' })}
              />
              {errors.appointmentDateTime && <p className="mt-1 text-sm text-red-600">{errors.appointmentDateTime.message}</p>}
            </label>

            <label className="block">
              <span className="mb-1 block text-sm font-medium text-slate-700">Duración</span>
              <select
                className="h-10 w-full rounded-md border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10"
                {...register('duration', { valueAsNumber: true })}
              >
                <option value={30}>30 minutos</option>
                <option value={60}>60 minutos</option>
                <option value={90}>90 minutos</option>
              </select>
            </label>

            <label className="block">
              <span className="mb-1 block text-sm font-medium text-slate-700">Modalidad</span>
              <select
                className="h-10 w-full rounded-md border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10"
                {...register('type')}
              >
                <option value="IN_PERSON">Presencial</option>
                <option value="ONLINE">Online</option>
              </select>
            </label>

            <label className="block md:col-span-2">
              <span className="mb-1 block text-sm font-medium text-slate-700">Motivo</span>
              <textarea
                rows={4}
                className="w-full resize-none rounded-md border border-slate-300 px-3 py-2 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10"
                {...register('reason', { maxLength: 255 })}
              />
            </label>
          </div>

          <div className="mt-5 flex justify-end gap-2">
            <button
              type="button"
              onClick={() => navigate('/appointments')}
              className="h-10 rounded-md border border-slate-200 px-4 text-sm font-medium hover:bg-slate-50"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={isSubmitting}
              className="inline-flex h-10 items-center gap-2 rounded-md bg-emerald-600 px-4 text-sm font-semibold text-white hover:bg-emerald-700 disabled:opacity-60"
            >
              <CalendarPlus size={16} />
              Crear turno
            </button>
          </div>
        </form>
      )}
    </div>
  )
}
