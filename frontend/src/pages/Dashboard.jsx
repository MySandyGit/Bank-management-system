import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import AccountCard from "../components/AccountCard";
import TransactionTable from "../components/TransactionTable";
import AccountService from "../services/AccountService";
import TransactionService from "../services/TransactionService";

export default function Dashboard() {
  const [account, setAccount] = useState(null);
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadData();
  }, []);

  async function loadData() {
    setLoading(true);
    try {
      const [accountData, historyData] = await Promise.all([
        AccountService.getMyAccount(),
        TransactionService.getHistory(),
      ]);
      setAccount(accountData);
      setTransactions(historyData.slice(0, 5));
    } catch (err) {
      setError("Could not load your account data");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <Navbar />
      <div className="page">
        <h1>Dashboard</h1>
        <p>Welcome back! Here's a summary of your account.</p>

        {error && <div className="alert alert-error">{error}</div>}

        {loading ? (
          <p>Loading...</p>
        ) : (
          <>
            <div className="grid grid-2 section-gap">
              <AccountCard account={account} />

              <div className="card">
                <p className="card-title">Quick Actions</p>
                <div style={{ display: "flex", flexDirection: "column", gap: 10, marginTop: 10 }}>
                  <Link to="/deposit" className="btn btn-success btn-block">+ Deposit Money</Link>
                  <Link to="/withdraw" className="btn btn-danger btn-block">− Withdraw Money</Link>
                  <Link to="/transactions" className="btn btn-outline btn-block">View Full History</Link>
                </div>
              </div>
            </div>

            <div className="card">
              <div className="flex-between">
                <p className="card-title" style={{ margin: 0 }}>Recent Transactions</p>
                <Link to="/transactions" style={{ fontSize: 13 }}>View all →</Link>
              </div>
              <TransactionTable transactions={transactions} />
            </div>
          </>
        )}
      </div>
    </div>
  );
}
