import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import TransactionService from "../services/TransactionService";

export default function Withdraw() {
  const [amount, setAmount] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setSuccess("");
    setLoading(true);
    try {
      const result = await TransactionService.withdraw(parseFloat(amount));
      setSuccess(`Withdrew ₹${amount} successfully! New balance: ₹${result.balanceAfter.toFixed(2)}`);
      setAmount("");
    } catch (err) {
      setError(err.response?.data?.message || "Withdrawal failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <Navbar />
      <div className="page" style={{ maxWidth: 480 }}>
        <h1>Withdraw Money</h1>
        <p>Take money out of your account.</p>

        <div className="card">
          {error && <div className="alert alert-error">{error}</div>}
          {success && <div className="alert alert-success">{success}</div>}

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Amount (₹)</label>
              <input
                type="number"
                min="1"
                step="0.01"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                placeholder="Enter amount to withdraw"
                required
              />
            </div>

            <button className="btn btn-danger btn-block" disabled={loading}>
              {loading ? "Processing..." : "Withdraw"}
            </button>
          </form>

          <button
            className="btn btn-outline btn-block"
            style={{ marginTop: 10 }}
            onClick={() => navigate("/dashboard")}
          >
            Back to Dashboard
          </button>
        </div>
      </div>
    </div>
  );
}
