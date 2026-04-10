import { useState } from "react";
import useIntersectionObserver from "../hooks/useIntersectionObserver";
import usePaginatedMovies from "../hooks/usePaginatedMovies";
import HoverPreviewCard from "./HoverPreviewCard";
import SkeletonLoader from "./SkeletonLoader";

function MovieCarousel({
  eyebrow = "Collection",
  title,
  movies,
  emptyMessage,
  progressMap = {},
  section,
  genre,
  query,
  pageSize = 12,
}) {
  const shouldPaginate = Boolean(section || genre || query);
  const [carouselRoot, setCarouselRoot] = useState(null);
  const { items, initialLoading, loadingMore, error, hasNext, loadMore } = usePaginatedMovies({
    section,
    genre,
    query,
    enabled: shouldPaginate,
    size: pageSize,
  });

  const resolvedMovies = shouldPaginate ? items : movies ?? [];

  const endRef = useIntersectionObserver(
    () => {
      if (shouldPaginate && hasNext) {
        loadMore();
      }
    },
    {
      root: carouselRoot,
      rootMargin: "240px",
      threshold: 0.5,
    },
  );

  const scrollCarousel = (direction) => {
    if (!carouselRoot) {
      return;
    }

    carouselRoot.scrollBy({
      left: direction * carouselRoot.clientWidth * 0.82,
      behavior: "smooth",
    });
  };

  if (initialLoading) {
    return (
      <section className="space-y-5">
        <div>
          <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">{eyebrow}</p>
          <h2 className="font-display text-4xl uppercase tracking-[0.08em] text-white">{title}</h2>
        </div>
        <SkeletonLoader count={4} />
      </section>
    );
  }

  if (!resolvedMovies.length) {
    return (
      <section className="space-y-5">
        <div>
          <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">{eyebrow}</p>
          <h2 className="font-display text-4xl uppercase tracking-[0.08em] text-white">{title}</h2>
        </div>
        <div className="rounded-[1.75rem] border border-white/10 bg-white/5 p-6 text-slate-400">
          {error || emptyMessage || "Nothing to show in this row yet."}
        </div>
      </section>
    );
  }

  return (
    <section className="space-y-5">
      <div className="flex items-center justify-between gap-4">
        <div>
          <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">{eyebrow}</p>
          <h2 className="font-display text-4xl uppercase tracking-[0.08em] text-white">{title}</h2>
        </div>

        <div className="flex items-center gap-3">
          <p className="hidden text-sm text-slate-400 sm:block">{resolvedMovies.length} titles ready to stream</p>
          <button
            type="button"
            onClick={() => scrollCarousel(-1)}
            className="rounded-full border border-white/15 bg-white/5 px-4 py-2 text-xs font-semibold uppercase tracking-[0.28em] text-white transition hover:bg-white/10"
          >
            Prev
          </button>
          <button
            type="button"
            onClick={() => scrollCarousel(1)}
            className="rounded-full border border-white/15 bg-white/5 px-4 py-2 text-xs font-semibold uppercase tracking-[0.28em] text-white transition hover:bg-white/10"
          >
            Next
          </button>
        </div>
      </div>

      <div ref={setCarouselRoot} className="scrollbar-hide flex gap-5 overflow-x-auto scroll-smooth pb-3">
        {resolvedMovies.map((movie) => (
          <HoverPreviewCard
            key={movie.id}
            movie={movie}
            progressTimestamp={progressMap[movie.id]}
          />
        ))}
        {shouldPaginate && hasNext && <div ref={endRef} className="w-px shrink-0" />}
      </div>

      {loadingMore && <p className="text-sm text-slate-400">Loading more titles...</p>}
    </section>
  );
}

export default MovieCarousel;
