import { ArrowRight, LockKeyhole, MapPin } from 'lucide-react'
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import type { LoginRequest } from '../types'

export function LoginPage() {
  const { isAuthenticated, login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [error, setError] = useState<string | null>(null)
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<LoginRequest>({
    defaultValues: {
      usernameOrEmail: 'admin',
      password: 'admin123',
    },
  })

  if (isAuthenticated) {
    return <Navigate to="/professionals" replace />
  }

  async function onSubmit(values: LoginRequest) {
    setError(null)

    try {
      await login(values)
      const redirectTo = (location.state as { from?: Location } | null)?.from?.pathname ?? '/professionals'
      navigate(redirectTo, { replace: true })
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'No se pudo iniciar sesión')
    }
  }

  return (
    <main className="grid min-h-screen bg-[#eef3f8] text-slate-950 lg:grid-cols-[1fr_480px]">
      <section className="hidden items-end bg-[linear-gradient(135deg,#0f172a_0%,#17453f_55%,#e5b454_100%)] p-10 text-white lg:flex">
        <div className="max-w-2xl">
          <div className="mb-6 grid size-12 place-items-center rounded-lg bg-white/15">
            <MapPin size={26} />
          </div>
          <h1 className="max-w-xl text-5xl font-semibold leading-tight">City Connect Pro</h1>
          <p className="mt-4 max-w-lg text-base text-white/78">
            Gestiona profesionales, turnos, reseñas y pagos desde un panel preparado para trabajo diario.
          </p>
        </div>
      </section>

      <section className="flex items-center justify-center px-5 py-10">
        <form
          onSubmit={handleSubmit(onSubmit)}
          className="w-full max-w-md rounded-lg border border-slate-200 bg-white p-6 shadow-sm"
        >
          <div className="mb-8">
            <div className="mb-4 grid size-11 place-items-center rounded-lg bg-emerald-600 text-white">
              <LockKeyhole size={22} />
            </div>
            <h2 className="text-2xl font-semibold">Ingresar</h2>
            <p className="mt-1 text-sm text-slate-500">Usa las credenciales del administrador de prueba.</p>
          </div>

          <label className="block text-sm font-medium text-slate-700" htmlFor="usernameOrEmail">
            Usuario o email
          </label>
          <input
            id="usernameOrEmail"
            className="mt-2 h-11 w-full rounded-md border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10"
            {...register('usernameOrEmail', { required: 'Ingresa tu usuario o email' })}
          />
          {errors.usernameOrEmail && <p className="mt-2 text-sm text-red-600">{errors.usernameOrEmail.message}</p>}

          <label className="mt-5 block text-sm font-medium text-slate-700" htmlFor="password">
            Contraseña
          </label>
          <input
            id="password"
            type="password"
            className="mt-2 h-11 w-full rounded-md border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10"
            {...register('password', { required: 'Ingresa tu contraseña' })}
          />
          {errors.password && <p className="mt-2 text-sm text-red-600">{errors.password.message}</p>}

          {error && (
            <div className="mt-5 rounded-md border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">
              {error}
            </div>
          )}

          <button
            type="submit"
            disabled={isSubmitting}
            className="mt-6 flex h-11 w-full items-center justify-center gap-2 rounded-md bg-slate-950 px-4 text-sm font-semibold text-white hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-60"
          >
            {isSubmitting ? 'Ingresando...' : 'Entrar'}
            <ArrowRight size={18} />
          </button>

          <p className="mt-5 text-center text-sm text-slate-500">
            ¿No tienes cuenta?{' '}
            <Link to="/register" className="font-semibold text-emerald-700 hover:text-emerald-800">
              Registrarse
            </Link>
          </p>
        </form>
      </section>
    </main>
  )
}
