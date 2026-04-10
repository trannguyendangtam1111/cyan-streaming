import MovieCard from "./MovieCard";

function MovieRow({ title, movies }) {
  if (!movies?.length) {
    return null;
  }

  return (
    <section className="space-y-5">
      <div className="flex items-center justify-between gap-4">
        <div>
          <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">Collection</p>
          <h2 className="font-display text-4xl uppercase tracking-[0.08em] text-white">{title}</h2>
        </div>
        <p className="hidden text-sm text-slate-400 sm:block">{movies.length} titles ready to stream</p>
      </div>

      <div className="scrollbar-hide flex gap-5 overflow-x-auto pb-3">
        {movies.map((movie) => (
          <MovieCard key={movie.id} movie={movie} />
        ))}
      </div>
    </section>
  );
}

export default MovieRow;
