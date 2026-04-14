import { useEffect, useState } from "react";
import { Helmet } from "react-helmet-async";
import { Link, Navigate, useLocation, useNavigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";

const GUEST_CREDENTIALS = { identifier: "guest@cyan.com", password: "Guest@123" };

function GuestToast({ onDismiss }) {
  useEffect(() => {
    const timer = setTimeout(onDismiss, 3500);
    return () => clearTimeout(timer);
  }, [onDismiss]);

  return (
    <div
      role="status"
      aria-live="polite"
      className="fixed bottom-6 left-1/2 z-50 flex -translate-x-1/2 items-center gap-2 rounded-2xl border border-cyan-400/30 bg-slate-900/90 px-6 py-3 text-sm font-medium text-cyan-300 backdrop-blur-md"
      style={{ boxShadow: "0 0 24px rgba(0,188,212,0.35), 0 0 6px rgba(0,188,212,0.2)" }}
    >
      <span aria-hidden="true">👋</span>
      Welcome! You are browsing as a Guest.
    </div>
  );
}

function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { login, isAuthenticated, isAdmin } = useAuth();
  const [form, setForm] = useState({ identifier: "", password: "" });
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [showGuestToast, setShowGuestToast] = useState(false);

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

  const handleGuestLogin = async () => {
    try {
      setSubmitting(true);
      setError("");
      setForm(GUEST_CREDENTIALS);
      await login(GUEST_CREDENTIALS);
      setShowGuestToast(true);
      await new Promise((r) => setTimeout(r, 1800));
      navigate(location.state?.from ?? "/");
    } catch (requestError) {
      setError(requestError.response?.data?.message ?? "Unable to sign in as guest.");
      setSubmitting(false);
    }
  };

  return (
    <>
      <style>{`
        @keyframes guestGlowPulse {
          0%, 100% { box-shadow: 0 0 10px rgba(0,188,212,0.4), 0 0 24px rgba(0,188,212,0.15); }
          50%       { box-shadow: 0 0 20px rgba(0,188,212,0.7), 0 0 44px rgba(0,188,212,0.28); }
        }
        .guest-btn {
          animation: guestGlowPulse 2.6s ease-in-out infinite;
          transition: transform 0.18s ease, box-shadow 0.18s ease;
        }
        .guest-btn:hover:not(:disabled) {
          animation: none;
          box-shadow: 0 0 26px rgba(0,188,212,0.8), 0 0 52px rgba(0,188,212,0.35);
          transform: scale(1.02);
        }
      `}</style>

      {showGuestToast && <GuestToast onDismiss={() => setShowGuestToast(false)} />}

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

          <div className="relative flex items-center gap-3">
            <div className="h-px flex-1 bg-white/10" />
            <span className="text-xs uppercase tracking-[0.2em] text-slate-500">or</span>
            <div className="h-px flex-1 bg-white/10" />
          </div>

          <button
            type="button"
            onClick={handleGuestLogin}
            disabled={submitting}
            className="guest-btn w-full rounded-full border border-cyan-400/40 bg-cyan-950/30 px-6 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-cyan-300 backdrop-blur-sm disabled:cursor-not-allowed disabled:opacity-60"
          >
            {submitting ? "Signing In..." : "✦ Login as Guest"}
          </button>

          <p className="text-sm text-slate-400">
            Need an account?{" "}
            <Link to="/register" className="text-cyan-300 hover:text-cyan-200">
              Create one
            </Link>
          </p>
        </form>
      </section>
    </>
  );
}

export default LoginPage;
