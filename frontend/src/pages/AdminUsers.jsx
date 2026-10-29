import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api, { getErrorMessage } from "../api/client.js";

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    // Slide 41: Call /api/dashboard/users with role="ADMIN"
    api
      .get("/dashboard/users")
      .then((res) => setUsers(res.data))
      .catch((err) => setError(getErrorMessage(err)))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="muted">Loading users...</p>;
  if (error) return <div className="alert alert-error">{error}</div>;

  return (
    <div className="card">
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "1rem" }}>
        <h2>All Registered Users ({users.length})</h2>
        <Link to="/dashboard" className="btn btn-outline" style={{ fontSize: "0.85rem", padding: "0.4rem 0.8rem" }}>
          ← Back to Dashboard
        </Link>
      </div>
      <p className="subtitle" style={{ marginBottom: "1rem" }}>
        Admin-only directory populated from <code>GET /api/dashboard/users</code>
      </p>
      <div style={{ overflowX: "auto" }}>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Full Name</th>
              <th>Email</th>
              <th>Role</th>
            </tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id}>
                <td>{u.id}</td>
                <td><strong>{u.fullName}</strong></td>
                <td>{u.email}</td>
                <td>
                  <span className={`badge badge-${u.role.toLowerCase()}`}>{u.role}</span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
