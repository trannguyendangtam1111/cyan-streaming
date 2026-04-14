import { useEffect, useRef, useState } from "react";
import { Link, NavLink, useLocation } from "react-router-dom";
import useAuth from "../hooks/useAuth";

const navLinkClass = ({ isActive }) =>
  `transition-colors ${isActive ? "text-cyan-300" : "text-slate-300 hover:text-white"}`;

function Navbar() {
  const { isAuthenticated, isAdmin, logout, user } = useAuth();
  const [menuOpen, setMenuOpen] = useState(false);
  const menuContainerRef = useRef(null);
  const location = useLocation();

  const closeMenu = () => setMenuOpen(false);

  // Close mobile menu when navigating
  useEffect(() => {
    setMenuOpen(false);
  }, [location.pathname]);

  // Close when clicking outside the menu area
  useEffect(() => {
    if (!menuOpen) return undefined;

    const handleClickOutside = (event) => {
      if (menuContainerRef.current && !menuContainerRef.current.contains(event.target)) {
        setMenuOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [menuOpen]);

  const handleLogout = () => {
    closeMenu();
    logout();
  };

  return (
    <header
      ref={menuContainerRef}
      className="fixed inset-x-0 top-0 z-50 border-b border-white/10 bg-slate-950/75 backdrop-blur-xl"
    >
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

        <nav
          aria-label="Primary navigation"
          className="hidden items-center gap-6 text-sm font-medium uppercase tracking-[0.28em] md:flex"
        >
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

        <button
          type="button"
          className="md:hidden flex h-10 w-10 items-center justify-center rounded-xl border border-white/10 text-slate-300 transition hover:bg-white/10 focus:outline-none focus-visible:ring-2 focus-visible:ring-cyan-400"
          aria-label={menuOpen ? "Close menu" : "Open menu"}
          aria-expanded={menuOpen}
          aria-controls="mobile-nav"
          onClick={() => setMenuOpen((current) => !current)}
        >
          <span className="sr-only">{menuOpen ? "Close menu" : "Open menu"}</span>
          <span className="relative block h-4 w-5">
            <span
              className={`absolute left-0 top-0 block h-0.5 w-full bg-current transition-transform duration-200 ${
                menuOpen ? "translate-y-1.5 rotate-45" : ""
              }`}
            />
            <span
              className={`absolute left-0 top-1.5 block h-0.5 w-full bg-current transition-opacity duration-200 ${
                menuOpen ? "opacity-0" : "opacity-100"
              }`}
            />
            <span
              className={`absolute left-0 top-3 block h-0.5 w-full bg-current transition-transform duration-200 ${
                menuOpen ? "-translate-y-1.5 -rotate-45" : ""
              }`}
            />
          </span>
        </button>
      </div>

      {menuOpen && (
        <nav
          id="mobile-nav"
          aria-label="Mobile navigation"
          className="md:hidden border-t border-white/10 bg-slate-950/95 px-4 py-4 flex flex-col gap-4 text-sm font-medium uppercase tracking-[0.28em]"
        >
          <NavLink to="/" className={navLinkClass} onClick={closeMenu}>
            Home
          </NavLink>
          {!isAuthenticated ? (
            <NavLink to="/login" className={navLinkClass} onClick={closeMenu}>
              Login
            </NavLink>
          ) : (
            <span className="text-slate-300">{user?.username}</span>
          )}
          {!isAuthenticated && (
            <NavLink to="/register" className={navLinkClass} onClick={closeMenu}>
              Register
            </NavLink>
          )}
          {isAdmin && (
            <NavLink to="/admin" className={navLinkClass} onClick={closeMenu}>
              Studio
            </NavLink>
          )}
          {isAuthenticated && (
            <button
              type="button"
              onClick={handleLogout}
              className="self-start rounded-full border border-white/15 bg-white/5 px-4 py-2 text-xs text-slate-200 transition hover:bg-white/10"
            >
              Logout
            </button>
          )}
        </nav>
      )}
    </header>
  );
}

export default Navbar;
