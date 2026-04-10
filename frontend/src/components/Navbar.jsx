import { Link, NavLink } from "react-router-dom";
import useAuth from "../hooks/useAuth";

const navLinkClass = ({ isActive }) =>
  `transition-colors ${isActive ? "text-cyan-300" : "text-slate-300 hover:text-white"}`;

function Navbar() {
  const { isAuthenticated, isAdmin, logout, user } = useAuth();

  return (
    <header className="fixed inset-x-0 top-0 z-50 border-b border-white/10 bg-slate-950/75 backdrop-blur-xl">
      <div className="mx-auto flex max-w-7xl items-center justify-between px-4 py-4 sm:px-6 lg:px-8">
        <Link to="/" className="flex items-center gap-3">
          <div className="flex h-11 w-11 items-center justify-center rounded-2xl border border-cyan-400/40 bg-cyan-400/10 shadow-cyan">
            <span className="font-display text-2xl tracking-[0.18em] text-cyan-300">C</span>
          </div>
          <div>
            <p className="font-display text-3xl uppercase leading-none tracking-[0.22em] text-white">
              Cyan
            </p>
            <p className="text-xs uppercase tracking-[0.35em] text-slate-400">Stream Beyond</p>
          </div>
        </Link>

        <nav className="hidden items-center gap-6 text-sm font-medium uppercase tracking-[0.28em] md:flex">
          <NavLink to="/" className={navLinkClass}>
            Home
          </NavLink>
          {!isAuthenticated ? (
            <NavLink to="/login" className={navLinkClass}>
              Login
            </NavLink>
          ) : (
            <span className="text-slate-300">{user?.username}</span>
          )}
          {!isAuthenticated && (
            <NavLink to="/register" className={navLinkClass}>
              Register
            </NavLink>
          )}
          {isAdmin && (
            <NavLink to="/admin" className={navLinkClass}>
              Studio
            </NavLink>
          )}
          {isAuthenticated && (
            <button
              type="button"
              onClick={logout}
              className="rounded-full border border-white/15 bg-white/5 px-4 py-2 text-xs text-slate-200 transition hover:bg-white/10"
            >
              Logout
            </button>
          )}
        </nav>
      </div>
    </header>
  );
}

export default Navbar;
