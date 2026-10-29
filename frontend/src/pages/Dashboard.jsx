import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api, { getErrorMessage } from "../api/client.js";
import { useAuth } from "../context/AuthContext.jsx";

export default function Dashboard() {
  const { logout } = useAuth();
  const navigate = useNavigate();
  const [info, setInfo] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .get("/dashboard")
      .then((res) => {
        setInfo(res.data);
      })
      .catch((err) => {
        setError(getErrorMessage(err));
        if (err.response?.status === 401) {
          navigate("/login");
        }
      });
  }, [navigate]);

  const handleLogout = async () => {
    await logout();
    navigate("/login");
  };

  if (error) return <div className="alert alert-error">{error}</div>;
  if (!info) return <p className="muted">Loading dashboard...</p>;

  return (
    <div className="card dashboard-card">
      <div className="dashboard-header">
        <div>
          <h2>Welcome, {info.fullName}!</h2>
          <p className="subtitle">
            <span className={`badge badge-${info.role.toLowerCase()}`}>{info.role}</span>
            {" · "}
            <span>Member since {info.memberSince}</span>
          </p>
        </div>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <span className="stat-label">Full Name</span>
          <span className="stat-value">{info.fullName}</span>
        </div>
        <div className="stat-card">
          <span className="stat-label">Email Address</span>
          <span className="stat-value">{info.email}</span>
        </div>
        <div className="stat-card">
          <span className="stat-label">System Role</span>
          <span className="stat-value">{info.role}</span>
        </div>
        <div className="stat-card">
          <span className="stat-label">Total Registered Users</span>
          <span className="stat-value stat-highlight">{info.totalUsers}</span>
        </div>
      </div>

      <div className="actions" style={{ marginTop: "1.5rem" }}>
        <Link className="btn" to="/profile">
          Edit my profile
        </Link>
        {info.role === "ADMIN" && (
          <Link className="btn btn-outline" to="/admin/users">
            View all users (Admin)
          </Link>
        )}
        <button className="btn btn-danger" onClick={handleLogout}>
          Logout
        </button>
      </div>
    </div>
  );
}
