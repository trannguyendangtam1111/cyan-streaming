import { Link } from "react-router-dom";

function MovieCard({ movie }) {
  return (
    <article
      aria-label={movie.title}
      className="group relative min-w-[220px] overflow-hidden rounded-2xl border border-white/10 bg-slate-900/70 shadow-lg shadow-slate-950/40 transition duration-300 hover:-translate-y-2 hover:border-cyan-400/40 focus-within:ring-2 focus-within:ring-cyan-400 focus-within:ring-offset-2 focus-within:ring-offset-slate-950"
    >
      <div className="aspect-[2/3] overflow-hidden">
        <img
          src={movie.thumbnailUrl}
          alt={movie.title}
          loading="lazy"
          decoding="async"
          className="h-full w-full object-cover transition duration-500 group-hover:scale-110"
        />
      </div>
      <div className="absolute inset-0 bg-gradient-to-t from-slate-950 via-slate-950/25 to-transparent opacity-70" />
      <div className="absolute inset-x-0 bottom-0 p-5">
        <div className="mb-3 flex items-center justify-between text-xs uppercase tracking-[0.28em] text-slate-300">
          <span>{movie.genre}</span>
          <span>{movie.rating.toFixed(1)}</span>
        </div>
        <h3 className="font-display text-3xl uppercase leading-none tracking-[0.08em] text-white">
          {movie.title}
        </h3>
        <p className="mt-2 text-sm text-slate-300">{movie.releaseYear}</p>
        <div className="mt-4 flex gap-3 opacity-100 md:opacity-0 md:transition md:group-hover:opacity-100 md:group-focus-within:opacity-100">
          <Link
            to={`/watch/${movie.id}`}
            className="rounded-full bg-cyan-400 px-4 py-2 text-xs font-semibold uppercase tracking-[0.28em] text-slate-950 focus:outline-none focus-visible:ring-2 focus-visible:ring-cyan-200"
          >
            Play
          </Link>
          <Link
            to={`/movie/${movie.id}`}
            className="rounded-full border border-white/15 bg-white/10 px-4 py-2 text-xs font-semibold uppercase tracking-[0.28em] text-white focus:outline-none focus-visible:ring-2 focus-visible:ring-cyan-200"
          >
            Details
          </Link>
        </div>
      </div>
    </article>
  );
}

export default MovieCard;
