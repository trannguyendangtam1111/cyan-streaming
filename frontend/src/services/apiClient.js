import axios from "axios";

export const AUTH_STORAGE_KEY = "cyan.auth";

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? "/api",
  timeout: 10000,
});

apiClient.interceptors.request.use((config) => {
  if (config.skipAuth) {
    if (config.headers?.Authorization) {
      delete config.headers.Authorization;
    }
    return config;
  }

  try {
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);

    if (stored) {
      const { token } = JSON.parse(stored);
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
  } catch {
    localStorage.removeItem(AUTH_STORAGE_KEY);
  }

  return config;
});

export default apiClient;
