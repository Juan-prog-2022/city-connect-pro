import { CheckCircle2, RefreshCw, XCircle } from 'lucide-react'
import { useEffect, useState } from 'react'
import { DataState } from '../components/DataState'
import { PageHeader } from '../components/PageHeader'
import { useAuth } from '../context/AuthContext'
import { api } from '../lib/api'
import { formatDateTime, statusClass } from '../lib/format'
import type { Appointment, PageResponse } from '../types'

export function AppointmentsPage() {
  const { roles, token } = useAuth()
  const [data, setData] = useState<PageResponse<Appointment> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    void loadAppointments()
  }, [])

  async function loadAppointments() {
    if (!token) return
    setLoading(true)
    setError(null)

    try {
      if (roles.includes('ROLE_PRO')) {
        setData(await api.getMyProfessionalAppointments(token))
      } else if (roles.includes('ROLE_USER')) {
        setData(await api.getMyClientAppointments(token))
      } else {
        setData(await api.getAppointments(token))
      }
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'No se pudieron cargar los turnos')
    } finally {
      setLoading(false)
    }
  }

  async function cancelAppointment(id: number) {
    if (!token) return
    await api.cancelAppointment(token, id)
    await loadAppointments()
  }

  async function completeAppointment(id: number) {
    if (!token) return
    await api.completeAppointment(token, id)
    await loadAppointments()
  }

  const canComplete = roles.includes('ROLE_PRO') || roles.includes('ROLE_ADMIN')
  const canCancel = roles.includes('ROLE_USER') || roles.includes('ROLE_ADMIN')
  const title = roles.includes('ROLE_PRO') ? 'Turnos de clientes' : roles.includes('ROLE_USER') ? 'Mis turnos' : 'Turnos'

  return (
    <div>
      <PageHeader title={title} description="Seguimiento de reservas, estados y pagos asociados." />
      <div className="mb-4 flex flex-wrap justify-end gap-2">
        <Link to="/appointments/new" className="inline-flex h-10 items-center gap-2 rounded-md bg-emerald-600 px-3 text-sm font-semibold text-white hover:bg-emerald-700">
          Nuevo turno
        </Link>
        <button onClick={loadAppointments} className="inline-flex h-10 items-center gap-2 rounded-md border border-slate-200 bg-white px-3 text-sm font-medium hover:bg-slate-50">
          <RefreshCw size={16} />
          Actualizar
        </button>
      </div>

      {error && <DataState message={error} />}
      {loading && <DataState message="Cargando turnos..." />}
      {!loading && !error && data?.content.length === 0 && <DataState message="No hay turnos cargados." />}

      {!loading && !error && Boolean(data?.content.length) && (
        <div className="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm">
          <div className="divide-y divide-slate-100">
            {data?.content.map((appointment) => (
              <article key={appointment.id} className="grid gap-3 px-4 py-4 md:grid-cols-[1fr_auto] md:items-center">
                <div>
                  <div className="flex flex-wrap items-center gap-2">
                    <h2 className="font-semibold">Turno #{appointment.id}</h2>
                    <span className={`rounded-md px-2 py-1 text-xs font-medium ${statusClass(appointment.status)}`}>
                      {appointment.status}
                    </span>
                    <span className={`rounded-md px-2 py-1 text-xs font-medium ${statusClass(appointment.paymentStatus)}`}>
                      {appointment.paymentStatus}
                    </span>
                  </div>
                  <p className="mt-1 text-sm text-slate-500">{formatDateTime(appointment.appointmentDateTime)}</p>
                  <p className="mt-1 text-sm text-slate-500">
                    Cliente #{appointment.clientId} · Profesional #{appointment.professionalId} · {appointment.duration} min
                  </p>
                </div>
                <div className="flex flex-wrap gap-2 md:justify-end">
                  {canComplete && appointment.status !== 'COMPLETED' && appointment.status !== 'CANCELLED' && (
                    <button
                      type="button"
                      onClick={() => void completeAppointment(appointment.id)}
                      className="inline-flex h-9 items-center justify-center gap-2 rounded-md border border-emerald-200 px-3 text-sm font-medium text-emerald-700 hover:bg-emerald-50"
                    >
                      <CheckCircle2 size={16} />
                      Completar
                    </button>
                  )}
                  {canCancel && appointment.status !== 'CANCELLED' && appointment.status !== 'COMPLETED' && (
                    <button
                      type="button"
                      onClick={() => void cancelAppointment(appointment.id)}
                      className="inline-flex h-9 items-center justify-center gap-2 rounded-md border border-red-200 px-3 text-sm font-medium text-red-700 hover:bg-red-50"
                    >
                      <XCircle size={16} />
                      Cancelar
                    </button>
                  )}
                </div>
              </article>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
import { Link } from 'react-router-dom'
