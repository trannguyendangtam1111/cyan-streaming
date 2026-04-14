import { useState } from "react";
import { Helmet } from "react-helmet-async";
import { Link, Navigate, useLocation, useNavigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";

function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { login, isAuthenticated, isAdmin } = useAuth();
  const [form, setForm] = useState({ identifier: "", password: "" });
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  if (isAuthenticated) {
    return <Navigate to={isAdmin ? "/admin" : "/"} replace />;
  }

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      setSubmitting(true);
      setError("");
      const response = await login(form);
      navigate(response.role === "ADMIN" ? "/admin" : location.state?.from ?? "/");
    } catch (requestError) {
      setError(requestError.response?.data?.message ?? "Unable to sign in.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <section className="mx-auto max-w-xl px-4 py-14 sm:px-6 lg:px-8">
      <Helmet>
        <title>Login — Cyan</title>
      </Helmet>
      <form
        onSubmit={handleSubmit}
        aria-describedby={error ? "login-error-msg" : undefined}
        className="space-y-5 rounded-[2rem] border border-white/10 bg-slate-900/70 p-8 shadow-2xl shadow-slate-950/40"
      >
        <div>
          <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">Welcome Back</p>
          <h1 className="mt-3 font-display text-5xl uppercase tracking-[0.08em] text-white">Login</h1>
        </div>

        <label className="block space-y-2">
          <span className="text-xs uppercase tracking-[0.24em] text-slate-400">Username or Email</span>
          <input
            value={form.identifier}
            onChange={(event) => setForm((current) => ({ ...current, identifier: event.target.value }))}
            className="w-full rounded-2xl border border-white/10 bg-slate-950/60 px-4 py-3 text-sm text-white outline-none transition focus:border-cyan-400/40"
            autoComplete="username"
            required
          />
        </label>

        <label className="block space-y-2">
          <span className="text-xs uppercase tracking-[0.24em] text-slate-400">Password</span>
          <input
            type="password"
            value={form.password}
            onChange={(event) => setForm((current) => ({ ...current, password: event.target.value }))}
            className="w-full rounded-2xl border border-white/10 bg-slate-950/60 px-4 py-3 text-sm text-white outline-none transition focus:border-cyan-400/40"
            autoComplete="current-password"
            required
          />
        </label>

        {error && (
          <p id="login-error-msg" role="alert" className="text-sm text-red-300">
            {error}
          </p>
        )}

        <button
          type="submit"
          disabled={submitting}
          className="w-full rounded-full bg-cyan-400 px-6 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-slate-950 transition hover:bg-cyan-300 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {submitting ? "Signing In..." : "Sign In"}
        </button>

        <p className="text-sm text-slate-400">
          Need an account?{" "}
          <Link to="/register" className="text-cyan-300 hover:text-cyan-200">
            Create one
          </Link>
        </p>
      </form>
    </section>
  );
}

export default LoginPage;
