import { CheckCircle2, RefreshCw, Star, XCircle } from 'lucide-react'
import { useEffect, useState } from 'react'
import { DataState } from '../components/DataState'
import { PageHeader } from '../components/PageHeader'
import { useAuth } from '../context/AuthContext'
import { api } from '../lib/api'
import { formatDateTime, statusClass } from '../lib/format'
import type { PageResponse, Review } from '../types'

export function ReviewsPage() {
  const { token } = useAuth()
  const [data, setData] = useState<PageResponse<Review> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    void loadReviews()
  }, [])

  async function loadReviews() {
    if (!token) return
    setLoading(true)
    setError(null)

    try {
      setData(await api.getReviews(token))
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'No se pudieron cargar las reseñas')
    } finally {
      setLoading(false)
    }
  }

  async function moderate(id: number, action: 'approve' | 'reject') {
    if (!token) return
    if (action === 'approve') {
      await api.approveReview(token, id)
    } else {
      await api.rejectReview(token, id)
    }
    await loadReviews()
  }

  return (
    <div>
      <PageHeader title="Reseñas" description="Moderación de opiniones y calificaciones de clientes." />
      <div className="mb-4 flex justify-end">
        <button onClick={loadReviews} className="inline-flex h-10 items-center gap-2 rounded-md border border-slate-200 bg-white px-3 text-sm font-medium hover:bg-slate-50">
          <RefreshCw size={16} />
          Actualizar
        </button>
      </div>

      {error && <DataState message={error} />}
      {loading && <DataState message="Cargando reseñas..." />}
      {!loading && !error && data?.content.length === 0 && <DataState message="No hay reseñas cargadas." />}

      {!loading && !error && Boolean(data?.content.length) && (
        <div className="grid gap-3">
          {data?.content.map((review) => (
            <article key={review.id} className="rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
              <div className="flex flex-col gap-3 md:flex-row md:items-start md:justify-between">
                <div>
                  <div className="flex flex-wrap items-center gap-2">
                    <h2 className="font-semibold">{review.title}</h2>
                    <span className={`rounded-md px-2 py-1 text-xs font-medium ${statusClass(review.status)}`}>
                      {review.status}
                    </span>
                  </div>
                  <p className="mt-1 text-sm text-slate-500">{review.clientName} sobre {review.professionalName}</p>
                  <p className="mt-3 text-sm text-slate-700">{review.comment}</p>
                  <p className="mt-3 text-xs text-slate-500">{formatDateTime(review.reviewDate)}</p>
                </div>
                <div className="flex items-center gap-2">
                  <span className="inline-flex h-9 items-center gap-1 rounded-md border border-slate-200 px-2 text-sm">
                    <Star size={15} className="text-amber-500" />
                    {review.rating}
                  </span>
                  <button onClick={() => void moderate(review.id, 'approve')} className="grid size-9 place-items-center rounded-md border border-emerald-200 text-emerald-700 hover:bg-emerald-50" title="Aprobar">
                    <CheckCircle2 size={17} />
                  </button>
                  <button onClick={() => void moderate(review.id, 'reject')} className="grid size-9 place-items-center rounded-md border border-red-200 text-red-700 hover:bg-red-50" title="Rechazar">
                    <XCircle size={17} />
                  </button>
                </div>
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
  )
}
