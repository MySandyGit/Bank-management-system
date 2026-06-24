import api from "./api";

const AccountService = {
  getMyProfile() {
    return api.get("/customers/me").then((res) => res.data);
  },

  updateMyProfile(data) {
    return api.put("/customers/me", data).then((res) => res.data);
  },

  getMyAccount() {
    return api.get("/accounts/me").then((res) => res.data);
  },
};

export default AccountService;
