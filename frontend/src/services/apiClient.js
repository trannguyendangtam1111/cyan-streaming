import axios from "axios";

export const AUTH_STORAGE_KEY = "cyan.auth";

const baseURL = import.meta.env.VITE_API_URL;
if (!baseURL) {
  throw new Error(
    "VITE_API_URL is not set. Define it in .env.local or Vercel env settings. " +
      "Expected format: https://api.streaming.cyan.engineer/api",
  );
}

const apiClient = axios.create({
  baseURL,
  timeout: 10000,
  // Auth is Bearer JWT in the Authorization header (set by the request interceptor below).
  // No cookies are sent or expected, so leave withCredentials OFF.
  withCredentials: false,
});

apiClient.interceptors.request.use((config) => {
  if (config.skipAuth) {
    if (config.headers?.Authorization) {
      delete config.headers.Authorization;
    }
    return config;
  }

  try {
    const stored = sessionStorage.getItem(AUTH_STORAGE_KEY);

    if (stored) {
      const { token } = JSON.parse(stored);
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
  } catch {
    sessionStorage.removeItem(AUTH_STORAGE_KEY);
  }

  return config;
});

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      try {
        sessionStorage.removeItem(AUTH_STORAGE_KEY);
      } catch {
        // ignore storage failures
      }

      if (
        typeof window !== "undefined" &&
        !window.location.pathname.startsWith("/login")
      ) {
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  },
);

export default apiClient;
