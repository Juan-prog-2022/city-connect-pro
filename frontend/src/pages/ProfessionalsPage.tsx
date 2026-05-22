import { LocateFixed, Mail, MapPin, RefreshCw, Search, Star } from 'lucide-react'
import { useEffect, useMemo, useState } from 'react'
import { useForm } from 'react-hook-form'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { api } from '../lib/api'
import type { NearbySearch, Professional } from '../types'

export function ProfessionalsPage() {
  const { token } = useAuth()
  const [professionals, setProfessionals] = useState<Professional[]>([])
  const [query, setQuery] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const { register, handleSubmit, setValue, formState: { isSubmitting } } = useForm<NearbySearch>({
    defaultValues: {
      latitude: -22.5164,
      longitude: -63.8013,
      radiusKm: 10,
    },
  })

  const professionalsWithLocation = useMemo(
    () => professionals.filter((professional) => professional.latitude != null && professional.longitude != null).length,
    [professionals],
  )

  const filteredProfessionals = useMemo(() => {
    const value = query.trim().toLowerCase()

    if (!value) {
      return professionals
    }

    return professionals.filter((professional) => {
      const searchable = [
        professional.firstName,
        professional.lastName,
        professional.email,
        professional.city,
        professional.address,
        professional.specialty,
        ...(professional.skills ?? []),
      ].filter(Boolean).join(' ').toLowerCase()

      return searchable.includes(value)
    })
  }, [professionals, query])

  useEffect(() => {
    void loadProfessionals()
  }, [])

  async function loadProfessionals() {
    if (!token) {
      return
    }

    setLoading(true)
    setError(null)

    try {
      const data = await api.getProfessionals(token)
      setProfessionals(data)
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'No se pudieron cargar los profesionales')
    } finally {
      setLoading(false)
    }
  }

  async function searchNearby(values: NearbySearch) {
    if (!token) {
      return
    }

    setError(null)

    try {
      const data = await api.getNearbyProfessionals(token, values)
      setProfessionals(data)
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'No se pudo buscar por ubicación')
    }
  }

  function useCurrentLocation() {
    if (!navigator.geolocation) {
      setError('Tu navegador no soporta geolocalización')
      return
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        setValue('latitude', Number(position.coords.latitude.toFixed(6)))
        setValue('longitude', Number(position.coords.longitude.toFixed(6)))
      },
      () => setError('No se pudo obtener tu ubicación'),
    )
  }

  return (
    <div className="space-y-5">
      <section className="grid gap-4 md:grid-cols-3">
        <Metric label="Profesionales" value={professionals.length} />
        <Metric label="Con ubicación" value={professionalsWithLocation} />
        <Metric label="Radio activo" value="10 km" />
      </section>

      <section className="rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
        <div className="flex flex-col gap-3 md:flex-row md:items-end md:justify-between">
          <div>
            <h1 className="text-xl font-semibold">Búsqueda por ubicación</h1>
            <p className="mt-1 text-sm text-slate-500">Consulta profesionales activos dentro de un radio en kilómetros.</p>
          </div>
          <button
            type="button"
            onClick={loadProfessionals}
            className="inline-flex h-10 items-center justify-center gap-2 rounded-md border border-slate-200 px-3 text-sm font-medium text-slate-700 hover:bg-slate-50"
          >
            <RefreshCw size={16} />
            Ver todos
          </button>
        </div>

        <form onSubmit={handleSubmit(searchNearby)} className="mt-4 grid gap-3 md:grid-cols-[1fr_1fr_160px_auto_auto]">
          <Field label="Latitud">
            <input
              type="number"
              step="any"
              className="h-10 w-full rounded-md border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10"
              {...register('latitude', { valueAsNumber: true })}
            />
          </Field>
          <Field label="Longitud">
            <input
              type="number"
              step="any"
              className="h-10 w-full rounded-md border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10"
              {...register('longitude', { valueAsNumber: true })}
            />
          </Field>
          <Field label="Radio">
            <input
              type="number"
              min="1"
              className="h-10 w-full rounded-md border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10"
              {...register('radiusKm', { valueAsNumber: true })}
            />
          </Field>
          <button
            type="button"
            onClick={useCurrentLocation}
            className="inline-flex h-10 items-center justify-center gap-2 self-end rounded-md border border-slate-200 px-3 text-sm font-medium text-slate-700 hover:bg-slate-50"
          >
            <LocateFixed size={16} />
            Ubicarme
          </button>
          <button
            type="submit"
            disabled={isSubmitting}
            className="inline-flex h-10 items-center justify-center gap-2 self-end rounded-md bg-emerald-600 px-4 text-sm font-semibold text-white hover:bg-emerald-700 disabled:opacity-60"
          >
            <Search size={16} />
            Buscar
          </button>
        </form>

        <div className="mt-4">
          <label className="relative block">
            <Search className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={17} />
            <input
              value={query}
              onChange={(event) => setQuery(event.target.value)}
              placeholder="Buscar por nombre, especialidad, ciudad o habilidad"
              className="h-11 w-full rounded-md border border-slate-300 pl-10 pr-3 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10"
            />
          </label>
        </div>

        {error && (
          <div className="mt-4 rounded-md border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">
            {error}
          </div>
        )}
      </section>

      <section>
        {loading ? (
          <div className="rounded-lg border border-slate-200 bg-white p-4 text-sm text-slate-500 shadow-sm">Cargando profesionales...</div>
        ) : filteredProfessionals.length === 0 ? (
          <div className="rounded-lg border border-slate-200 bg-white p-4 text-sm text-slate-500 shadow-sm">No hay profesionales para mostrar.</div>
        ) : (
          <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
            {filteredProfessionals.map((professional) => (
              <ProfessionalCard key={professional.id} professional={professional} />
            ))}
          </div>
        )}
      </section>
    </div>
  )
}

function Metric({ label, value }: { label: string, value: number | string }) {
  return (
    <div className="rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
      <p className="text-xs font-medium uppercase text-slate-500">{label}</p>
      <p className="mt-2 text-2xl font-semibold">{value}</p>
    </div>
  )
}

function Field({ label, children }: { label: string, children: React.ReactNode }) {
  return (
    <label className="block">
      <span className="mb-1 block text-xs font-medium text-slate-600">{label}</span>
      {children}
    </label>
  )
}

function ProfessionalCard({ professional }: { professional: Professional }) {
  const fullName = `${professional.firstName} ${professional.lastName}`
  const hasLocation = professional.latitude != null && professional.longitude != null

  return (
    <article className="flex min-h-64 flex-col justify-between rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
      <div>
        <div className="flex items-start justify-between gap-3">
          <div>
            <h3 className="font-semibold">{fullName}</h3>
            <p className="mt-1 text-sm text-slate-500">{professional.yearsOfExperience} años de experiencia</p>
          </div>
          {professional.specialty && (
            <span className="rounded-md bg-slate-100 px-2 py-1 text-xs font-medium text-slate-600">
              {professional.specialty}
            </span>
          )}
        </div>
        <div className="mt-4 space-y-2 text-sm text-slate-500">
          <p className="flex items-center gap-2"><Mail size={15} />{professional.email}</p>
          <p className="flex items-center gap-2"><MapPin size={15} />{[professional.city, professional.address].filter(Boolean).join(' - ') || 'Sin dirección cargada'}</p>
        </div>
        <div className="mt-4 flex flex-wrap gap-2">
          {(professional.skills ?? []).slice(0, 3).map((skill) => (
            <span key={skill} className="rounded-md bg-emerald-50 px-2 py-1 text-xs font-medium text-emerald-700">{skill}</span>
          ))}
        </div>
      </div>

      <div className="mt-5 flex flex-wrap items-center gap-2">
        <span className="inline-flex h-8 items-center gap-1 rounded-md border border-slate-200 px-2 text-sm">
          <Star size={15} className="text-amber-500" />
          {Number(professional.averageRating ?? 0).toFixed(1)}
        </span>
        <span className="h-8 rounded-md border border-slate-200 px-2 pt-1.5 text-sm">
          {professional.currency} {Number(professional.hourlyRate ?? 0).toLocaleString('es-AR')}
        </span>
        <span className={`h-8 rounded-md px-2 pt-1.5 text-sm ${
          hasLocation ? 'bg-emerald-50 text-emerald-700' : 'bg-slate-100 text-slate-500'
        }`}>
          {hasLocation ? 'Con GPS' : 'Sin GPS'}
        </span>
      </div>
      <Link
        to={`/appointments/new?professionalId=${professional.id}`}
        className="mt-4 inline-flex h-10 items-center justify-center rounded-md bg-slate-950 px-4 text-sm font-semibold text-white hover:bg-slate-800"
      >
        Crear turno
      </Link>
    </article>
  )
}
