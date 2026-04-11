import { createContext, useEffect, useMemo, useState } from "react";
import { loginUser, registerUser } from "../services/authApi";
import { AUTH_STORAGE_KEY } from "../services/apiClient";

export const AuthContext = createContext(null);

const readStoredAuth = () => {
  try {
    const stored = sessionStorage.getItem(AUTH_STORAGE_KEY);
    return stored ? JSON.parse(stored) : null;
  } catch {
    sessionStorage.removeItem(AUTH_STORAGE_KEY);
    return null;
  }
};

function AuthProvider({ children }) {
  const [auth, setAuth] = useState(() => readStoredAuth());

  useEffect(() => {
    if (auth) {
      sessionStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(auth));
      return;
    }

    sessionStorage.removeItem(AUTH_STORAGE_KEY);
  }, [auth]);

  const login = async (payload) => {
    const response = await loginUser(payload);
    setAuth(response);
    return response;
  };

  const register = async (payload) => {
    const response = await registerUser(payload);
    setAuth(response);
    return response;
  };

  const logout = () => {
    setAuth(null);
  };

  const value = useMemo(
    () => ({
      auth,
      token: auth?.token ?? null,
      user: auth
        ? {
            id: auth.userId,
            username: auth.username,
            email: auth.email,
            role: auth.role,
          }
        : null,
      isAuthenticated: Boolean(auth?.token),
      isAdmin: auth?.role === "ADMIN",
      login,
      register,
      logout,
    }),
    [auth],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export default AuthProvider;
