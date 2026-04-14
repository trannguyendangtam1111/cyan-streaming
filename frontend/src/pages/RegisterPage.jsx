import { useState } from "react";
import { Helmet } from "react-helmet-async";
import { Link, Navigate, useNavigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";

function RegisterPage() {
  const navigate = useNavigate();
  const { register, isAuthenticated, isAdmin } = useAuth();
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
  });
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
      const response = await register(form);
      navigate(response.role === "ADMIN" ? "/admin" : "/");
    } catch (requestError) {
      setError(requestError.response?.data?.message ?? "Unable to create your account.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <section className="mx-auto max-w-xl px-4 py-14 sm:px-6 lg:px-8">
      <Helmet>
        <title>Create Account — Cyan</title>
      </Helmet>
      <form
        onSubmit={handleSubmit}
        aria-describedby={error ? "register-error-msg" : undefined}
        className="space-y-5 rounded-[2rem] border border-white/10 bg-slate-900/70 p-8 shadow-2xl shadow-slate-950/40"
      >
        <div>
          <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">Join Cyan</p>
          <h1 className="mt-3 font-display text-5xl uppercase tracking-[0.08em] text-white">Register</h1>
        </div>

        <label className="block space-y-2">
          <span className="text-xs uppercase tracking-[0.24em] text-slate-400">Username</span>
          <input
            value={form.username}
            onChange={(event) => setForm((current) => ({ ...current, username: event.target.value }))}
            className="w-full rounded-2xl border border-white/10 bg-slate-950/60 px-4 py-3 text-sm text-white outline-none transition focus:border-cyan-400/40"
            autoComplete="username"
            required
          />
        </label>

        <label className="block space-y-2">
          <span className="text-xs uppercase tracking-[0.24em] text-slate-400">Email</span>
          <input
            type="email"
            value={form.email}
            onChange={(event) => setForm((current) => ({ ...current, email: event.target.value }))}
            className="w-full rounded-2xl border border-white/10 bg-slate-950/60 px-4 py-3 text-sm text-white outline-none transition focus:border-cyan-400/40"
            autoComplete="email"
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
            autoComplete="new-password"
            required
          />
        </label>

        {error && (
          <p id="register-error-msg" role="alert" className="text-sm text-red-300">
            {error}
          </p>
        )}

        <button
          type="submit"
          disabled={submitting}
          className="w-full rounded-full bg-cyan-400 px-6 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-slate-950 transition hover:bg-cyan-300 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {submitting ? "Creating Account..." : "Create Account"}
        </button>

        <p className="text-sm text-slate-400">
          Already have an account?{" "}
          <Link to="/login" className="text-cyan-300 hover:text-cyan-200">
            Log in
          </Link>
        </p>
      </form>
    </section>
  );
}

export default RegisterPage;
