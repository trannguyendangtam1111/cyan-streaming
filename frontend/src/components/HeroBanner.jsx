import { Link } from "react-router-dom";

function HeroBanner({ movie }) {
  if (!movie) {
    return (
      <section className="mx-auto flex min-h-[70vh] max-w-7xl items-end px-4 pb-16 pt-24 sm:px-6 lg:px-8">
        <div className="w-full rounded-[2rem] border border-white/10 bg-white/5 p-10 backdrop-blur">
          <p className="text-sm uppercase tracking-[0.35em] text-slate-400">Loading featured movie</p>
        </div>
      </section>
    );
  }

  return (
    <section id="featured" className="mx-auto max-w-7xl px-4 pb-12 pt-8 sm:px-6 lg:px-8">
      <div
        className="relative overflow-hidden rounded-[2rem] border border-white/10 shadow-2xl shadow-cyan-950/40"
        style={{
          backgroundImage: `linear-gradient(90deg, rgba(2,6,23,0.96) 0%, rgba(2,6,23,0.88) 42%, rgba(2,6,23,0.38) 100%), url(${movie.thumbnailUrl})`,
          backgroundPosition: "center",
          backgroundSize: "cover",
        }}
      >
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_left,_rgba(34,211,238,0.18),_transparent_30%)]" />
        <div className="relative flex min-h-[74vh] items-end px-6 py-12 sm:px-10 lg:px-14">
          <div className="max-w-2xl">
            <span className="inline-flex rounded-full border border-cyan-400/40 bg-cyan-400/10 px-4 py-2 text-xs font-semibold uppercase tracking-[0.4em] text-cyan-200">
              Featured Tonight
            </span>
            <h1 className="mt-6 font-display text-5xl uppercase leading-[0.88] tracking-[0.08em] text-white sm:text-7xl">
              {movie.title}
            </h1>
            <div className="mt-4 flex flex-wrap items-center gap-4 text-sm uppercase tracking-[0.3em] text-slate-300">
              <span>{movie.genre}</span>
              <span>{movie.releaseYear}</span>
              <span>{movie.rating.toFixed(1)} rating</span>
            </div>
            <p className="mt-6 max-w-xl text-base leading-7 text-slate-200 sm:text-lg">{movie.description}</p>
            <p className="mt-4 max-w-lg text-sm uppercase tracking-[0.28em] text-slate-400">
              Cinematic playlists, responsive playback, and curated rows designed around how people actually browse.
            </p>
            <div className="mt-8 flex flex-wrap gap-4">
              <Link
                to={`/watch/${movie.id}`}
                className="rounded-full bg-cyan-400 px-7 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-slate-950 transition hover:bg-cyan-300"
              >
                Play Now
              </Link>
              <Link
                to={`/movie/${movie.id}`}
                className="rounded-full border border-white/15 bg-white/5 px-7 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-white transition hover:bg-white/10"
              >
                More Info
              </Link>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}

export default HeroBanner;
