import { Navigate } from "react-router-dom";
import AuthService from "../services/AuthService";

/**
 * Wrap a page with <PrivateRoute>...</PrivateRoute> to require login.
 * Pass requireAdmin to also require the ADMIN role.
 */
export default function PrivateRoute({ children, requireAdmin = false }) {
  if (!AuthService.isLoggedIn()) {
    return <Navigate to="/login" replace />;
  }

  if (requireAdmin && AuthService.getRole() !== "ADMIN") {
    return <Navigate to="/dashboard" replace />;
  }

  return children;
}
