function MovieTable({ movies, onEdit, onDelete }) {
  return (
    <div className="overflow-hidden rounded-[2rem] border border-white/10 bg-slate-900/70">
      <div className="overflow-x-auto">
        <table className="min-w-full text-left text-sm text-slate-200">
          <thead className="bg-white/5 text-xs uppercase tracking-[0.28em] text-slate-400">
            <tr>
              <th className="px-5 py-4">Title</th>
              <th className="px-5 py-4">Genre</th>
              <th className="px-5 py-4">Year</th>
              <th className="px-5 py-4">Rating</th>
              <th className="px-5 py-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            {movies.map((movie) => (
              <tr key={movie.id} className="border-t border-white/5">
                <td className="px-5 py-4">
                  <div>
                    <p className="font-semibold text-white">{movie.title}</p>
                    <p className="mt-1 max-w-md text-xs text-slate-400">{movie.description}</p>
                  </div>
                </td>
                <td className="px-5 py-4">{movie.genre}</td>
                <td className="px-5 py-4">{movie.releaseYear}</td>
                <td className="px-5 py-4">{movie.rating}</td>
                <td className="px-5 py-4">
                  <div className="flex justify-end gap-3">
                    <button
                      type="button"
                      onClick={() => onEdit(movie)}
                      className="rounded-full border border-cyan-400/40 bg-cyan-400/10 px-4 py-2 text-xs font-semibold uppercase tracking-[0.24em] text-cyan-200"
                    >
                      Edit
                    </button>
                    <button
                      type="button"
                      onClick={() => onDelete(movie)}
                      className="rounded-full border border-red-400/30 bg-red-400/10 px-4 py-2 text-xs font-semibold uppercase tracking-[0.24em] text-red-200"
                    >
                      Delete
                    </button>
                  </div>
                </td>
              </tr>
            ))}
            {movies.length === 0 && (
              <tr>
                <td colSpan="5" className="px-5 py-10 text-center text-slate-400">
                  No movies available yet.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default MovieTable;
