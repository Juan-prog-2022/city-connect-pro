export function formatDateTime(value?: string | null) {
  if (!value) {
    return 'Sin fecha'
  }

  return new Intl.DateTimeFormat('es-AR', {
    dateStyle: 'medium',
    timeStyle: 'short',
  }).format(new Date(value))
}

export function formatMoney(amount?: number | null, currency = 'ARS') {
  return new Intl.NumberFormat('es-AR', {
    style: 'currency',
    currency,
  }).format(amount ?? 0)
}

export function statusClass(status?: string | null) {
  const normalized = status ?? ''

  if (['APPROVED', 'COMPLETED', 'PAID'].includes(normalized)) {
    return 'bg-emerald-50 text-emerald-700'
  }

  if (['PENDING'].includes(normalized)) {
    return 'bg-amber-50 text-amber-700'
  }

  if (['CANCELLED', 'REJECTED', 'FAILED'].includes(normalized)) {
    return 'bg-red-50 text-red-700'
  }

  return 'bg-slate-100 text-slate-600'
}
