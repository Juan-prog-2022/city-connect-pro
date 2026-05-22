import { ArrowLeft, UserPlus } from 'lucide-react'
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { api } from '../lib/api'
import type { RegisterRequest } from '../types'

export function RegisterPage() {
  const { isAuthenticated } = useAuth()
  const navigate = useNavigate()

  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors, isSubmitting },
  } = useForm<RegisterRequest>({
    defaultValues: {
      firstName: '',
      lastName: '',
      dni: '',
      email: '',
      username: '',
      password: '',
      role: 'CLIENT',
      specialty: '',
      licenseNumber: '',
    },
  })

  const role = watch('role')

  if (isAuthenticated) {
    return <Navigate to="/professionals" replace />
  }

  async function onSubmit(values: RegisterRequest) {
    setError(null)
    setSuccess(null)

    try {
      await api.register(values)

      setSuccess('Cuenta creada correctamente. Ya puedes iniciar sesión.')

      setTimeout(() => {
        navigate('/login')
      }, 900)

    } catch (exception) {
      setError(
        exception instanceof Error
          ? exception.message
          : 'No se pudo crear la cuenta'
      )
    }
  }

  return (
    <main className="flex min-h-screen items-center justify-center bg-[#eef3f8] px-5 py-10 text-slate-950">
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="w-full max-w-2xl rounded-xl border border-slate-200 bg-white p-6 shadow-sm"
      >
        <Link
          to="/login"
          className="mb-6 inline-flex items-center gap-2 text-sm font-medium text-slate-500 hover:text-slate-950"
        >
          <ArrowLeft size={16} />
          Volver al login
        </Link>

        {/* HEADER */}
        <div className="mb-8">
          <div className="mb-4 grid size-11 place-items-center rounded-lg bg-emerald-600 text-white">
            <UserPlus size={22} />
          </div>

          <h1 className="text-2xl font-semibold">
            {role === 'PROFESSIONAL'
              ? 'Crear cuenta profesional'
              : 'Crear cuenta de cliente'}
          </h1>

          <p className="mt-1 text-sm text-slate-500">
            {role === 'PROFESSIONAL'
              ? 'Regístrate para ofrecer servicios profesionales.'
              : 'Regístrate para reservar turnos con profesionales locales.'}
          </p>
        </div>

        {/* ROLE SELECT */}
        <div className="mb-8">
          <p className="mb-3 text-sm font-medium text-slate-700">
            Tipo de cuenta
          </p>

          <div className="grid gap-4 md:grid-cols-2">

            {/* CLIENT */}
            <label
              className={`cursor-pointer rounded-xl border p-4 transition ${
                role === 'CLIENT'
                  ? 'border-emerald-600 bg-emerald-50'
                  : 'border-slate-300 hover:border-emerald-400'
              }`}
            >
              <input
                type="radio"
                value="CLIENT"
                className="hidden"
                {...register('role')}
              />

              <div>
                <h3 className="font-semibold text-slate-900">
                  Cliente
                </h3>

                <p className="mt-1 text-sm text-slate-500">
                  Reserva turnos y encuentra profesionales cerca tuyo.
                </p>
              </div>
            </label>

            {/* PROFESSIONAL */}
            <label
              className={`cursor-pointer rounded-xl border p-4 transition ${
                role === 'PROFESSIONAL'
                  ? 'border-emerald-600 bg-emerald-50'
                  : 'border-slate-300 hover:border-emerald-400'
              }`}
            >
              <input
                type="radio"
                value="PROFESSIONAL"
                className="hidden"
                {...register('role')}
              />

              <div>
                <h3 className="font-semibold text-slate-900">
                  Profesional
                </h3>

                <p className="mt-1 text-sm text-slate-500">
                  Publica tus servicios y recibe clientes.
                </p>
              </div>
            </label>
          </div>
        </div>

        {/* FORM */}
        <div className="grid gap-4 md:grid-cols-2">

          <Field label="Nombre" error={errors.firstName?.message}>
            <input
              className={inputClass}
              {...register('firstName', {
                required: 'Ingresa tu nombre',
                maxLength: 50,
              })}
            />
          </Field>

          <Field label="Apellido" error={errors.lastName?.message}>
            <input
              className={inputClass}
              {...register('lastName', {
                required: 'Ingresa tu apellido',
                maxLength: 50,
              })}
            />
          </Field>

          <Field label="DNI" error={errors.dni?.message}>
            <input
              className={inputClass}
              {...register('dni', {
                required: 'Ingresa tu DNI',
              })}
            />
          </Field>

          <Field label="Email" error={errors.email?.message}>
            <input
              type="email"
              className={inputClass}
              {...register('email', {
                required: 'Ingresa tu email',
                pattern: {
                  value: /\S+@\S+\.\S+/,
                  message: 'Email inválido',
                },
              })}
            />
          </Field>

          <Field label="Usuario" error={errors.username?.message}>
            <input
              className={inputClass}
              {...register('username', {
                required: 'Ingresa un usuario',
                minLength: {
                  value: 4,
                  message: 'Mínimo 4 caracteres',
                },
                maxLength: {
                  value: 20,
                  message: 'Máximo 20 caracteres',
                },
              })}
            />
          </Field>

          <Field label="Contraseña" error={errors.password?.message}>
            <input
              type="password"
              className={inputClass}
              {...register('password', {
                required: 'Ingresa una contraseña',
                minLength: {
                  value: 8,
                  message: 'Mínimo 8 caracteres',
                },
              })}
            />
          </Field>

          {/* PROFESSIONAL FIELDS */}
          {role === 'PROFESSIONAL' && (
            <>
              <Field
                label="Especialidad"
                error={errors.specialty?.message}
              >
                <input
                  className={inputClass}
                  placeholder="Ej: Electricista, Psicólogo, Abogado..."
                  {...register('specialty', {
                    required: 'Ingresa tu especialidad',
                  })}
                />
              </Field>

              <Field
                label="Matrícula (opcional)"
                error={errors.licenseNumber?.message}
              >
                <input
                  className={inputClass}
                  placeholder="Número de matrícula"
                  {...register('licenseNumber')}
                />
              </Field>
            </>
          )}
        </div>

        {/* ERROR */}
        {error && (
          <div className="mt-5 rounded-md border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">
            {error}
          </div>
        )}

        {/* SUCCESS */}
        {success && (
          <div className="mt-5 rounded-md border border-emerald-200 bg-emerald-50 px-3 py-2 text-sm text-emerald-700">
            {success}
          </div>
        )}

        {/* SUBMIT */}
        <button
          type="submit"
          disabled={isSubmitting}
          className="mt-6 flex h-11 w-full items-center justify-center rounded-md bg-slate-950 px-4 text-sm font-semibold text-white transition hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {isSubmitting
            ? 'Creando cuenta...'
            : role === 'PROFESSIONAL'
              ? 'Crear cuenta profesional'
              : 'Registrarse'}
        </button>
      </form>
    </main>
  )
}

const inputClass =
  'mt-2 h-11 w-full rounded-md border border-slate-300 px-3 text-sm outline-none transition focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10'

function Field({
  label,
  error,
  children,
}: {
  label: string
  error?: string
  children: React.ReactNode
}) {
  return (
    <label className="block text-sm font-medium text-slate-700">
      {label}

      {children}

      {error && (
        <p className="mt-2 text-sm text-red-600">
          {error}
        </p>
      )}
    </label>
  )
}