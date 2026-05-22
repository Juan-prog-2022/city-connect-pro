import { BriefcaseBusiness, CalendarDays, CreditCard, LogOut, MapPin, ShieldCheck, Star, Users } from 'lucide-react'
import { NavLink, Outlet } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

const navigation = [
  { to: '/professionals', label: 'Profesionales', icon: BriefcaseBusiness },
  { to: '/appointments', label: 'Turnos', icon: CalendarDays },
  { to: '/reviews', label: 'Reseñas', icon: Star },
  { to: '/payments', label: 'Pagos', icon: CreditCard },
  { to: '/users', label: 'Usuarios', icon: Users },
]

export function AppShell() {
  const { logout, roles, username } = useAuth()

  return (
    <div className="min-h-screen bg-[#f5f7fb] text-slate-950">
      <aside className="fixed inset-y-0 left-0 hidden w-72 border-r border-slate-200 bg-white lg:block">
        <div className="flex h-16 items-center gap-3 border-b border-slate-200 px-5">
          <div className="grid size-10 place-items-center rounded-lg bg-emerald-600 text-white">
            <MapPin size={21} />
          </div>
          <div>
            <p className="text-sm font-semibold leading-5">City Connect Pro</p>
            <p className="text-xs text-slate-500">Panel operativo</p>
          </div>
        </div>

        <nav className="space-y-1 px-3 py-4">
          {navigation.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                `flex h-10 items-center gap-3 rounded-md px-3 text-sm font-medium ${
                  isActive ? 'bg-slate-950 text-white' : 'text-slate-600 hover:bg-slate-100 hover:text-slate-950'
                }`
              }
            >
              <item.icon size={18} />
              {item.label}
            </NavLink>
          ))}
        </nav>
      </aside>

      <div className="lg:pl-72">
        <header className="sticky top-0 z-10 flex h-16 items-center justify-between border-b border-slate-200 bg-white/95 px-4 backdrop-blur md:px-6">
          <div>
            <p className="text-sm font-semibold">Profesionales locales</p>
            <p className="text-xs text-slate-500">Backend conectado a Spring Security + JWT</p>
          </div>

          <div className="flex items-center gap-3">
            <div className="hidden items-center gap-2 rounded-md border border-slate-200 px-3 py-2 text-sm md:flex">
              <ShieldCheck size={16} className="text-emerald-600" />
              <span className="font-medium">{username}</span>
              <span className="text-slate-400">{roles.join(', ')}</span>
            </div>
            <button
              type="button"
              onClick={logout}
              className="grid size-10 place-items-center rounded-md border border-slate-200 bg-white text-slate-600 hover:bg-slate-100"
              title="Cerrar sesión"
            >
              <LogOut size={18} />
            </button>
          </div>
        </header>

        <main className="px-4 py-6 md:px-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
