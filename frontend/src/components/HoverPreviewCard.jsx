import { useRef, useState } from "react";
import { Link } from "react-router-dom";
import { trackAnalyticsEvent } from "../services/analyticsApi";

const formatTimestamp = (seconds) => {
  if (!seconds && seconds !== 0) {
    return null;
  }

  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;
  return `${mins}m ${secs}s`;
};

function HoverPreviewCard({ movie, progressTimestamp }) {
  const [imageLoaded, setImageLoaded] = useState(false);
  const hasTrackedHover = useRef(false);

  const handleHover = () => {
    if (hasTrackedHover.current) {
      return;
    }

    hasTrackedHover.current = true;
    trackAnalyticsEvent({
      eventType: "movie_hover",
      movieId: movie.id,
      source: "carousel",
    }).catch(() => {});
  };

  return (
    <article
      onMouseEnter={handleHover}
      className="group relative min-w-[240px] overflow-hidden rounded-[1.75rem] border border-white/10 bg-slate-900/70 shadow-xl shadow-slate-950/50 transition duration-300 hover:-translate-y-2 hover:border-cyan-400/40"
    >
      <div className="relative aspect-[16/10] overflow-hidden">
        <div
          className={`absolute inset-0 bg-slate-700/40 blur-2xl transition duration-500 ${
            imageLoaded ? "opacity-0" : "opacity-100"
          }`}
        />
        <img
          src={movie.thumbnailUrl}
          alt={movie.title}
          loading="lazy"
          decoding="async"
          sizes="(max-width: 768px) 78vw, (max-width: 1280px) 42vw, 24vw"
          onLoad={() => setImageLoaded(true)}
          className={`h-full w-full object-cover transition duration-500 group-hover:scale-110 ${
            imageLoaded ? "scale-100 blur-0" : "scale-105 blur-xl"
          }`}
        />
      </div>
      <div className="absolute inset-0 bg-gradient-to-t from-slate-950 via-slate-950/50 to-transparent opacity-80" />
      <div className="absolute inset-0 flex flex-col justify-end p-5">
        <div className="flex flex-wrap items-center gap-2 text-[11px] uppercase tracking-[0.28em] text-slate-300">
          <span>{movie.genre}</span>
          <span>{movie.releaseYear}</span>
          <span>{movie.rating?.toFixed?.(1) ?? movie.rating}</span>
        </div>
        <h3 className="mt-3 font-display text-3xl uppercase leading-none tracking-[0.08em] text-white">
          {movie.title}
        </h3>
        <p className="mt-2 text-[11px] uppercase tracking-[0.3em] text-cyan-200">Quick preview</p>
        <p className="mt-3 line-clamp-3 max-w-xs text-sm leading-6 text-slate-200 opacity-0 transition duration-300 group-hover:opacity-100">
          {movie.description}
        </p>
        {progressTimestamp !== undefined && progressTimestamp !== null && (
          <p className="mt-3 text-xs uppercase tracking-[0.3em] text-cyan-200">
            Resume from {formatTimestamp(progressTimestamp)}
          </p>
        )}
        <div className="mt-4 flex translate-y-4 gap-3 opacity-0 transition duration-300 group-hover:translate-y-0 group-hover:opacity-100">
          <Link
            to={`/watch/${movie.id}`}
            className="rounded-full bg-cyan-400 px-4 py-2 text-xs font-semibold uppercase tracking-[0.28em] text-slate-950"
          >
            Play
          </Link>
          <Link
            to={`/movie/${movie.id}`}
            className="rounded-full border border-white/15 bg-white/10 px-4 py-2 text-xs font-semibold uppercase tracking-[0.28em] text-white"
          >
            Details
          </Link>
        </div>
      </div>
    </article>
  );
}

export default HoverPreviewCard;
