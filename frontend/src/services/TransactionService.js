import api from "./api";

const TransactionService = {
  deposit(amount) {
    return api.post("/transactions/deposit", { amount }).then((res) => res.data);
  },

  withdraw(amount) {
    return api.post("/transactions/withdraw", { amount }).then((res) => res.data);
  },

  getHistory() {
    return api.get("/transactions/history").then((res) => res.data);
  },
};

export default TransactionService;
