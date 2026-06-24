import api from "./api";

const AuthService = {
  async login(username, password) {
    const res = await api.post("/auth/login", { username, password });
    localStorage.setItem("token", res.data.token);
    localStorage.setItem("username", res.data.username);
    localStorage.setItem("role", res.data.role);
    return res.data;
  },

  async register(data) {
    const res = await api.post("/auth/register", data);
    localStorage.setItem("token", res.data.token);
    localStorage.setItem("username", res.data.username);
    localStorage.setItem("role", res.data.role);
    return res.data;
  },

  logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("role");
  },

  getRole() {
    return localStorage.getItem("role");
  },

  getUsername() {
    return localStorage.getItem("username");
  },

  isLoggedIn() {
    return !!localStorage.getItem("token");
  },
};

export default AuthService;
