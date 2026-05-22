import { ExternalLink, RefreshCw, Search } from 'lucide-react'
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { DataState } from '../components/DataState'
import { PageHeader } from '../components/PageHeader'
import { useAuth } from '../context/AuthContext'
import { api } from '../lib/api'
import { formatDateTime, formatMoney, statusClass } from '../lib/format'
import type { Payment } from '../types'

type PaymentSearch = {
  appointmentId: number
}

export function PaymentsPage() {
  const { token } = useAuth()
  const [payments, setPayments] = useState<Payment[]>([])
  const [error, setError] = useState<string | null>(null)
  const [searched, setSearched] = useState(false)
  const { register, handleSubmit, getValues, formState: { isSubmitting } } = useForm<PaymentSearch>({
    defaultValues: { appointmentId: 1 },
  })

  async function search(values: PaymentSearch) {
    if (!token) return
    setError(null)
    setSearched(true)

    try {
      setPayments(await api.getPaymentsByAppointment(token, values.appointmentId))
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'No se pudieron cargar los pagos')
    }
  }

  async function createCheckout() {
    if (!token) return
    const { appointmentId } = getValues()
    setError(null)

    try {
      await api.createCheckout(token, appointmentId)
      await search({ appointmentId })
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'No se pudo crear el checkout')
    }
  }

  return (
    <div>
      <PageHeader title="Pagos" description="Consulta pagos por turno y genera checkout de Mercado Pago." />

      <form onSubmit={handleSubmit(search)} className="mb-4 flex flex-col gap-3 rounded-lg border border-slate-200 bg-white p-4 shadow-sm md:flex-row md:items-end">
        <label className="block md:w-52">
          <span className="mb-1 block text-xs font-medium text-slate-600">ID del turno</span>
          <input type="number" min="1" className="h-10 w-full rounded-md border border-slate-300 px-3 text-sm outline-none focus:border-emerald-600 focus:ring-4 focus:ring-emerald-600/10" {...register('appointmentId', { valueAsNumber: true })} />
        </label>
        <button type="submit" disabled={isSubmitting} className="inline-flex h-10 items-center justify-center gap-2 rounded-md bg-slate-950 px-4 text-sm font-semibold text-white hover:bg-slate-800">
          <Search size={16} />
          Buscar pagos
        </button>
        <button type="button" onClick={() => void createCheckout()} className="inline-flex h-10 items-center justify-center gap-2 rounded-md border border-slate-200 px-4 text-sm font-medium hover:bg-slate-50">
          <RefreshCw size={16} />
          Crear checkout
        </button>
      </form>

      {error && <DataState message={error} />}
      {!error && searched && payments.length === 0 && <DataState message="No hay pagos para ese turno." />}

      <div className="grid gap-3">
        {payments.map((payment) => (
          <article key={payment.id} className="rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
            <div className="flex flex-col gap-3 md:flex-row md:items-start md:justify-between">
              <div>
                <div className="flex flex-wrap items-center gap-2">
                  <h2 className="font-semibold">Pago #{payment.id}</h2>
                  <span className={`rounded-md px-2 py-1 text-xs font-medium ${statusClass(payment.status)}`}>{payment.status}</span>
                </div>
                <p className="mt-1 text-sm text-slate-500">{payment.description}</p>
                <p className="mt-1 text-sm text-slate-500">{formatDateTime(payment.createdAt)}</p>
              </div>
              <div className="flex items-center gap-2">
                <span className="h-9 rounded-md border border-slate-200 px-3 pt-2 text-sm">{formatMoney(payment.amount, payment.currencyId)}</span>
                {payment.paymentUrl && (
                  <a href={payment.paymentUrl} target="_blank" rel="noreferrer" className="grid size-9 place-items-center rounded-md border border-slate-200 text-slate-700 hover:bg-slate-50" title="Abrir pago">
                    <ExternalLink size={16} />
                  </a>
                )}
              </div>
            </div>
          </article>
        ))}
      </div>
    </div>
  )
}
