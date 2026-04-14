import { Link } from "react-router-dom";

function NotFoundPage() {
  return (
    <section className="mx-auto flex min-h-[70vh] max-w-xl flex-col items-center justify-center px-4 py-16 text-center sm:px-6">
      <p className="text-xs uppercase tracking-[0.4em] text-cyan-300">404</p>
      <h1 className="mt-4 font-display text-7xl uppercase tracking-[0.08em] text-white">Lost</h1>
      <p className="mt-6 text-sm leading-7 text-slate-400">
        This page doesn't exist. The reel may have snapped.
      </p>
      <Link
        to="/"
        className="mt-8 rounded-full bg-cyan-400 px-7 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-slate-950 transition hover:bg-cyan-300"
      >
        Back to Home
      </Link>
    </section>
  );
}

export default NotFoundPage;
