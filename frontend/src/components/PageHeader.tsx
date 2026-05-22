export function PageHeader({ title, description }: { title: string, description: string }) {
  return (
    <div className="mb-5">
      <h1 className="text-xl font-semibold">{title}</h1>
      <p className="mt-1 text-sm text-slate-500">{description}</p>
    </div>
  )
}
