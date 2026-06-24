import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import TransactionTable from "../components/TransactionTable";
import TransactionService from "../services/TransactionService";

export default function Transactions() {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    TransactionService.getHistory()
      .then(setTransactions)
      .catch(() => setError("Could not load transaction history"))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div>
      <Navbar />
      <div className="page">
        <h1>Transaction History</h1>
        <p>Every deposit and withdrawal you've made.</p>

        {error && <div className="alert alert-error">{error}</div>}

        <div className="card">
          {loading ? <p>Loading...</p> : <TransactionTable transactions={transactions} />}
        </div>
      </div>
    </div>
  );
}
