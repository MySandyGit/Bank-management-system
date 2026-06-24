export default function AccountCard({ account }) {
  if (!account) return null;

  return (
    <div className="card">
      <p className="card-title">Available Balance</p>
      <div className="balance-amount">₹{account.balance.toFixed(2)}</div>
      <div style={{ marginTop: 12 }}>
        <p style={{ marginBottom: 2 }}>Account Number</p>
        <div className="account-number">{account.accountNumber}</div>
      </div>
      <div style={{ marginTop: 8 }}>
        <span className="tag tag-deposit">{account.accountType}</span>
      </div>
    </div>
  );
}
