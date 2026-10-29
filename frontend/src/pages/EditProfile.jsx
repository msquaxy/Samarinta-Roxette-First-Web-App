import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import api, { getErrorMessage } from "../api/client.js";
import { useAuth } from "../context/AuthContext.jsx";

export default function EditProfile() {
  const { user, updateUser } = useAuth();
  const [fullName, setFullName] = useState(user?.fullName ?? "");
  const [email, setEmail] = useState(user?.email ?? "");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    api
      .get("/users/me")
      .then((res) => {
        setFullName(res.data.fullName);
        setEmail(res.data.email);
        updateUser(res.data);
      })
      .catch(() => {
        if (user) {
          setFullName(user.fullName || "");
          setEmail(user.email || "");
        }
      });
  }, []); // Only fetch once on mount

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");
    setLoading(true);
    try {
      const { data } = await api.put("/users/me", { fullName });
      updateUser(data);
      setFullName(data.fullName);
      setMessage("Profile updated successfully!");
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card form-card">
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "1rem" }}>
        <h2>Edit My Profile</h2>
        <Link to="/dashboard" className="btn btn-outline" style={{ fontSize: "0.85rem", padding: "0.4rem 0.8rem" }}>
          ← Back
        </Link>
      </div>

      {message && <div className="alert alert-success">{message}</div>}
      {error && <div className="alert alert-error">{error}</div>}

      <form onSubmit={handleSubmit}>
        <label>
          Email (read-only)
          <input value={email} disabled style={{ backgroundColor: "#f1f5f9", cursor: "not-allowed" }} />
        </label>
        <label>
          Full name
          <input
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            placeholder="Enter new full name"
            required
          />
        </label>
        <button className="btn" type="submit" disabled={loading}>
          {loading ? "Saving changes..." : "Save changes"}
        </button>
      </form>
    </div>
  );
}
