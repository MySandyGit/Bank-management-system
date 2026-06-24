import api from "./api";

const AdminService = {
  getAllCustomers() {
    return api.get("/admin/customers").then((res) => res.data);
  },

  getCustomer(id) {
    return api.get(`/admin/customers/${id}`).then((res) => res.data);
  },

  getCustomerAccount(id) {
    return api.get(`/admin/customers/${id}/account`).then((res) => res.data);
  },

  getCustomerTransactions(id) {
    return api.get(`/admin/customers/${id}/transactions`).then((res) => res.data);
  },

  createCustomer(data) {
    return api.post("/admin/customers", data).then((res) => res.data);
  },

  updateCustomer(id, data) {
    return api.put(`/admin/customers/${id}`, data).then((res) => res.data);
  },

  deleteCustomer(id) {
    return api.delete(`/admin/customers/${id}`).then((res) => res.data);
  },
};

export default AdminService;
