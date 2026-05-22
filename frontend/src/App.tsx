import { Navigate, Route, Routes } from 'react-router-dom'
import { AppShell } from './components/AppShell'
import { ProtectedRoute } from './components/ProtectedRoute'
import { AppointmentsPage } from './pages/AppointmentsPage'
import { AppointmentFormPage } from './pages/AppointmentFormPage'
import { LoginPage } from './pages/LoginPage'
import { PaymentsPage } from './pages/PaymentsPage'
import { ProfessionalsPage } from './pages/ProfessionalsPage'
import { RegisterPage } from './pages/RegisterPage'
import { ReviewsPage } from './pages/ReviewsPage'
import { UsersPage } from './pages/UsersPage'

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route element={<ProtectedRoute />}>
        <Route element={<AppShell />}>
          <Route index element={<Navigate to="/professionals" replace />} />
          <Route path="/professionals" element={<ProfessionalsPage />} />
          <Route path="/appointments" element={<AppointmentsPage />} />
          <Route path="/appointments/new" element={<AppointmentFormPage />} />
          <Route path="/reviews" element={<ReviewsPage />} />
          <Route path="/payments" element={<PaymentsPage />} />
          <Route path="/users" element={<UsersPage />} />
        </Route>
      </Route>
      <Route path="*" element={<Navigate to="/professionals" replace />} />
    </Routes>
  )
}
