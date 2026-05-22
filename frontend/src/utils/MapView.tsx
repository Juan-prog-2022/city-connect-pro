import { useEffect, useState } from 'react'
import { MapContainer, Marker, Popup, TileLayer } from 'react-leaflet'

export default function MapView() {
  const [userPosition, setUserPosition] = useState<[number, number] | null>(null)

  useEffect(() => {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        setUserPosition([position.coords.latitude, position.coords.longitude])
      },
      console.error,
    )
  }, [])

  if (!userPosition) {
    return <p className="text-sm text-slate-500">Cargando mapa...</p>
  }

  return (
    <MapContainer center={userPosition} zoom={13} className="h-[500px] w-full">
      <TileLayer
        attribution="&copy; OpenStreetMap contributors"
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <Marker position={userPosition}>
        <Popup>Tu ubicación</Popup>
      </Marker>
    </MapContainer>
  )
}
