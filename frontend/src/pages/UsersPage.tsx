import { RefreshCw, Trash2 } from 'lucide-react'
import { useEffect, useState } from 'react'
import { DataState } from '../components/DataState'
import { PageHeader } from '../components/PageHeader'
import { useAuth } from '../context/AuthContext'
import { api } from '../lib/api'
import type { User } from '../types'

export function UsersPage() {
  const { token, username } = useAuth()
  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    void loadUsers()
  }, [])

  async function loadUsers() {
    if (!token) return
    setLoading(true)
    setError(null)

    try {
      setUsers(await api.getUsers(token))
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'No se pudieron cargar los usuarios')
    } finally {
      setLoading(false)
    }
  }

  async function deleteUser(id: number) {
    if (!token) return
    await api.deleteUser(token, id)
    await loadUsers()
  }

  return (
    <div>
      <PageHeader title="Usuarios" description="Administración básica de cuentas registradas." />
      <div className="mb-4 flex justify-end">
        <button onClick={loadUsers} className="inline-flex h-10 items-center gap-2 rounded-md border border-slate-200 bg-white px-3 text-sm font-medium hover:bg-slate-50">
          <RefreshCw size={16} />
          Actualizar
        </button>
      </div>

      {error && <DataState message={error} />}
      {loading && <DataState message="Cargando usuarios..." />}
      {!loading && !error && users.length === 0 && <DataState message="No hay usuarios cargados." />}

      {!loading && !error && users.length > 0 && (
        <div className="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm">
          <div className="divide-y divide-slate-100">
            {users.map((user) => (
              <article key={user.id} className="grid gap-3 px-4 py-4 md:grid-cols-[1fr_auto] md:items-center">
                <div>
                  <h2 className="font-semibold">{user.firstName} {user.lastName}</h2>
                  <p className="mt-1 text-sm text-slate-500">{user.username} · {user.email}</p>
                </div>
                <button
                  type="button"
                  disabled={user.username === username}
                  onClick={() => void deleteUser(user.id)}
                  className="inline-flex h-9 items-center justify-center gap-2 rounded-md border border-red-200 px-3 text-sm font-medium text-red-700 hover:bg-red-50 disabled:cursor-not-allowed disabled:opacity-40"
                >
                  <Trash2 size={16} />
                  Eliminar
                </button>
              </article>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
