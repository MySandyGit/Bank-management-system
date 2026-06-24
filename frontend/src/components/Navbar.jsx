import { Link, useNavigate } from "react-router-dom";
import AuthService from "../services/AuthService";

export default function Navbar() {
  const navigate = useNavigate();
  const role = AuthService.getRole();
  const username = AuthService.getUsername();

  function handleLogout() {
    AuthService.logout();
    navigate("/login");
  }

  return (
    <nav className="navbar">
      <Link to={role === "ADMIN" ? "/admin" : "/dashboard"} className="brand">
        🏦 Simple Bank
      </Link>

      <div className="nav-links">
        {role === "ADMIN" ? (
          <Link to="/admin">Admin Panel</Link>
        ) : (
          <>
            <Link to="/dashboard">Dashboard</Link>
            <Link to="/deposit">Deposit</Link>
            <Link to="/withdraw">Withdraw</Link>
            <Link to="/transactions">History</Link>
            <Link to="/profile">Profile</Link>
          </>
        )}
        <span className="badge">{username} · {role}</span>
        <button className="btn btn-outline btn-sm" onClick={handleLogout} style={{ color: "white", borderColor: "rgba(255,255,255,0.4)" }}>
          Logout
        </button>
      </div>
    </nav>
  );
}
