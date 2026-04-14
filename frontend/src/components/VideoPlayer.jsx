import { useState } from "react";

function VideoPlayer({ movie }) {
  const [started, setStarted] = useState(false);

  if (!movie) {
    return null;
  }

  const embedUrl = movie.youtubeEmbedUrl.includes("?")
    ? `${movie.youtubeEmbedUrl}&autoplay=1&rel=0&modestbranding=1`
    : `${movie.youtubeEmbedUrl}?autoplay=1&rel=0&modestbranding=1`;

  if (!started) {
    return (
      <div
        role="button"
        tabIndex={0}
        aria-label={`Play ${movie.title}`}
        onClick={() => setStarted(true)}
        onKeyDown={(event) => {
          if (event.key === "Enter" || event.key === " ") {
            event.preventDefault();
            setStarted(true);
          }
        }}
        className="relative aspect-video w-full cursor-pointer overflow-hidden rounded-[2rem] border border-white/10 bg-slate-900/70 shadow-2xl shadow-cyan-950/30 focus:outline-none focus-visible:ring-2 focus-visible:ring-cyan-400"
      >
        <img
          src={movie.thumbnailUrl}
          alt={`Thumbnail for ${movie.title}`}
          loading="lazy"
          decoding="async"
          className="h-full w-full object-cover"
        />
        <div className="absolute inset-0 flex items-center justify-center bg-black/40">
          <div className="flex h-20 w-20 items-center justify-center rounded-full bg-cyan-400 shadow-lg">
            <svg
              viewBox="0 0 24 24"
              fill="currentColor"
              aria-hidden="true"
              className="h-8 w-8 pl-1 text-slate-950"
            >
              <path d="M8 5v14l11-7z" />
            </svg>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="overflow-hidden rounded-[2rem] border border-white/10 bg-slate-900/70 shadow-2xl shadow-cyan-950/30">
      <div className="aspect-video w-full">
        <iframe
          className="h-full w-full"
          src={embedUrl}
          title={movie.title}
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
          allowFullScreen
        />
      </div>
    </div>
  );
}

export default VideoPlayer;
