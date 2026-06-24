import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import TransactionTable from "../components/TransactionTable";
import AdminService from "../services/AdminService";

const emptyCreateForm = {
  fullName: "",
  email: "",
  phone: "",
  address: "",
  username: "",
  password: "",
};

const emptyEditForm = { fullName: "", email: "", phone: "", address: "" };

export default function AdminDashboard() {
  const [customers, setCustomers] = useState([]);
  const [accounts, setAccounts] = useState({}); // customerId -> account
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  // create modal
  const [showCreate, setShowCreate] = useState(false);
  const [createForm, setCreateForm] = useState(emptyCreateForm);

  // edit modal
  const [editingId, setEditingId] = useState(null);
  const [editForm, setEditForm] = useState(emptyEditForm);

  // transaction view modal
  const [viewingId, setViewingId] = useState(null);
  const [viewingTransactions, setViewingTransactions] = useState([]);

  useEffect(() => {
    loadCustomers();
  }, []);

  async function loadCustomers() {
    setLoading(true);
    setError("");
    try {
      const list = await AdminService.getAllCustomers();
      setCustomers(list);

      // fetch each customer's account so we can show balance in the table
      const accountResults = await Promise.all(
        list.map((c) => AdminService.getCustomerAccount(c.id).catch(() => null))
      );
      const accountMap = {};
      list.forEach((c, i) => (accountMap[c.id] = accountResults[i]));
      setAccounts(accountMap);
    } catch (err) {
      setError("Could not load customers");
    } finally {
      setLoading(false);
    }
  }

  // ---------- Create ----------

  async function handleCreate(e) {
    e.preventDefault();
    setError("");
    setSuccess("");
    try {
      await AdminService.createCustomer(createForm);
      setSuccess("Customer created successfully");
      setShowCreate(false);
      setCreateForm(emptyCreateForm);
      loadCustomers();
    } catch (err) {
      const data = err.response?.data;
      setError(data?.message || (data && Object.values(data)[0]) || "Could not create customer");
    }
  }

  // ---------- Update ----------

  function startEdit(customer) {
    setEditingId(customer.id);
    setEditForm({
      fullName: customer.fullName,
      email: customer.email,
      phone: customer.phone || "",
      address: customer.address || "",
    });
  }

  async function handleUpdate(e) {
    e.preventDefault();
    setError("");
    setSuccess("");
    try {
      await AdminService.updateCustomer(editingId, editForm);
      setSuccess("Customer updated successfully");
      setEditingId(null);
      loadCustomers();
    } catch (err) {
      setError(err.response?.data?.message || "Could not update customer");
    }
  }

  // ---------- Delete ----------

  async function handleDelete(id) {
    if (!window.confirm("Delete this customer and all their data? This cannot be undone.")) return;
    setError("");
    setSuccess("");
    try {
      await AdminService.deleteCustomer(id);
      setSuccess("Customer deleted successfully");
      loadCustomers();
    } catch (err) {
      setError(err.response?.data?.message || "Could not delete customer");
    }
  }

  // ---------- View transactions ----------

  async function handleViewTransactions(id) {
    setViewingId(id);
    try {
      const data = await AdminService.getCustomerTransactions(id);
      setViewingTransactions(data);
    } catch {
      setViewingTransactions([]);
    }
  }

  return (
    <div>
      <Navbar />
      <div className="page">
        <div className="flex-between">
          <div>
            <h1>Admin Panel</h1>
            <p>Manage all bank customers and accounts.</p>
          </div>
          <button className="btn btn-primary" onClick={() => setShowCreate(true)}>
            + New Customer
          </button>
        </div>

        {error && <div className="alert alert-error">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}

        <div className="card">
          {loading ? (
            <p>Loading...</p>
          ) : customers.length === 0 ? (
            <div className="empty-state">No customers yet. Create the first one!</div>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Account No.</th>
                  <th>Balance</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {customers.map((c) => (
                  <tr key={c.id}>
                    <td>{c.fullName}</td>
                    <td>{c.email}</td>
                    <td className="account-number">{accounts[c.id]?.accountNumber || "-"}</td>
                    <td>₹{accounts[c.id]?.balance?.toFixed(2) ?? "-"}</td>
                    <td>
                      <div style={{ display: "flex", gap: 6, flexWrap: "wrap" }}>
                        <button className="btn btn-outline btn-sm" onClick={() => handleViewTransactions(c.id)}>
                          History
                        </button>
                        <button className="btn btn-outline btn-sm" onClick={() => startEdit(c)}>
                          Edit
                        </button>
                        <button className="btn btn-danger btn-sm" onClick={() => handleDelete(c.id)}>
                          Delete
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>

      {/* Create Customer Modal */}
      {showCreate && (
        <div className="modal-overlay" onClick={() => setShowCreate(false)}>
          <div className="modal-box" onClick={(e) => e.stopPropagation()}>
            <h2>New Customer</h2>
            <form onSubmit={handleCreate}>
              <div className="form-group">
                <label>Full Name</label>
                <input
                  value={createForm.fullName}
                  onChange={(e) => setCreateForm({ ...createForm, fullName: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  value={createForm.email}
                  onChange={(e) => setCreateForm({ ...createForm, email: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Phone</label>
                <input
                  value={createForm.phone}
                  onChange={(e) => setCreateForm({ ...createForm, phone: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Address</label>
                <input
                  value={createForm.address}
                  onChange={(e) => setCreateForm({ ...createForm, address: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Username</label>
                <input
                  value={createForm.username}
                  onChange={(e) => setCreateForm({ ...createForm, username: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Password</label>
                <input
                  type="password"
                  value={createForm.password}
                  onChange={(e) => setCreateForm({ ...createForm, password: e.target.value })}
                  required
                />
              </div>
              <div style={{ display: "flex", gap: 8 }}>
                <button type="button" className="btn btn-outline btn-block" onClick={() => setShowCreate(false)}>
                  Cancel
                </button>
                <button className="btn btn-primary btn-block">Create</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Edit Customer Modal */}
      {editingId && (
        <div className="modal-overlay" onClick={() => setEditingId(null)}>
          <div className="modal-box" onClick={(e) => e.stopPropagation()}>
            <h2>Edit Customer</h2>
            <form onSubmit={handleUpdate}>
              <div className="form-group">
                <label>Full Name</label>
                <input
                  value={editForm.fullName}
                  onChange={(e) => setEditForm({ ...editForm, fullName: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  value={editForm.email}
                  onChange={(e) => setEditForm({ ...editForm, email: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Phone</label>
                <input
                  value={editForm.phone}
                  onChange={(e) => setEditForm({ ...editForm, phone: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Address</label>
                <input
                  value={editForm.address}
                  onChange={(e) => setEditForm({ ...editForm, address: e.target.value })}
                />
              </div>
              <div style={{ display: "flex", gap: 8 }}>
                <button type="button" className="btn btn-outline btn-block" onClick={() => setEditingId(null)}>
                  Cancel
                </button>
                <button className="btn btn-primary btn-block">Save</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* View Transactions Modal */}
      {viewingId && (
        <div className="modal-overlay" onClick={() => setViewingId(null)}>
          <div className="modal-box" style={{ maxWidth: 600 }} onClick={(e) => e.stopPropagation()}>
            <h2>Transaction History</h2>
            <TransactionTable transactions={viewingTransactions} />
            <button className="btn btn-outline btn-block" style={{ marginTop: 12 }} onClick={() => setViewingId(null)}>
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
