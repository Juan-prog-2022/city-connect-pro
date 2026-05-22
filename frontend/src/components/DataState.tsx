export function DataState({ message }: { message: string }) {
  return (
    <div className="rounded-lg border border-slate-200 bg-white p-4 text-sm text-slate-500 shadow-sm">
      {message}
    </div>
  )
}
