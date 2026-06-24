function formatDate(dateString) {
  if (!dateString) return "-";
  const d = new Date(dateString);
  return d.toLocaleString();
}

export default function TransactionTable({ transactions }) {
  if (!transactions || transactions.length === 0) {
    return <div className="empty-state">No transactions yet. Make a deposit to get started!</div>;
  }

  return (
    <table>
      <thead>
        <tr>
          <th>Type</th>
          <th>Amount</th>
          <th>Balance After</th>
          <th>Date</th>
        </tr>
      </thead>
      <tbody>
        {transactions.map((t) => (
          <tr key={t.id}>
            <td>
              <span className={`tag ${t.type === "DEPOSIT" ? "tag-deposit" : "tag-withdraw"}`}>
                {t.type}
              </span>
            </td>
            <td>
              {t.type === "DEPOSIT" ? "+" : "-"}₹{t.amount.toFixed(2)}
            </td>
            <td>₹{t.balanceAfter.toFixed(2)}</td>
            <td>{formatDate(t.transactionDate)}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
