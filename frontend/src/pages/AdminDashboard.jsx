import { useEffect, useState } from "react";
import MovieForm from "../components/MovieForm";
import MovieTable from "../components/MovieTable";
import { createMovie, deleteMovie, getAllMovies, updateMovie } from "../services/movieApi";

function AdminDashboard() {
  const [movies, setMovies] = useState([]);
  const [selectedMovie, setSelectedMovie] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const loadMovies = async () => {
    try {
      setLoading(true);
      const response = await getAllMovies();
      setMovies(response);
    } catch {
      setError("Unable to load the admin catalog.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadMovies();
  }, []);

  const handleSubmit = async (payload) => {
    try {
      setSubmitting(true);
      setError("");
      setSuccess("");

      if (selectedMovie) {
        await updateMovie(selectedMovie.id, payload);
        setSuccess("Movie updated successfully.");
      } else {
        await createMovie(payload);
        setSuccess("Movie created successfully.");
      }

      setSelectedMovie(null);
      await loadMovies();
    } catch (requestError) {
      setError(requestError.response?.data?.message ?? "Unable to save the movie.");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (movie) => {
    if (!window.confirm(`Delete "${movie.title}"?`)) {
      return;
    }

    try {
      setError("");
      setSuccess("");
      await deleteMovie(movie.id);
      setSuccess("Movie deleted successfully.");
      if (selectedMovie?.id === movie.id) {
        setSelectedMovie(null);
      }
      await loadMovies();
    } catch (requestError) {
      setError(requestError.response?.data?.message ?? "Unable to delete the movie.");
    }
  };

  return (
    <section className="mx-auto max-w-7xl space-y-8 px-4 py-10 sm:px-6 lg:px-8">
      <div>
        <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">Admin Studio</p>
        <h1 className="mt-3 font-display text-5xl uppercase tracking-[0.08em] text-white">Manage Catalog</h1>
      </div>

      {error && (
        <div className="rounded-[2rem] border border-red-400/30 bg-red-400/10 p-6 text-red-100">{error}</div>
      )}
      {success && (
        <div className="rounded-[2rem] border border-cyan-400/20 bg-cyan-400/10 p-6 text-cyan-100">{success}</div>
      )}

      <div className="grid gap-8 xl:grid-cols-[1.1fr_0.9fr]">
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="font-display text-3xl uppercase tracking-[0.08em] text-white">Movies</h2>
            <button
              type="button"
              onClick={() => setSelectedMovie(null)}
              className="rounded-full border border-white/15 bg-white/5 px-4 py-2 text-xs font-semibold uppercase tracking-[0.28em] text-white transition hover:bg-white/10"
            >
              New Movie
            </button>
          </div>
          {loading ? (
            <div className="rounded-[2rem] border border-white/10 bg-white/5 p-8 text-slate-300">Loading catalog...</div>
          ) : (
            <MovieTable movies={movies} onEdit={setSelectedMovie} onDelete={handleDelete} />
          )}
        </div>

        <MovieForm
          movie={selectedMovie}
          onSubmit={handleSubmit}
          onCancel={() => setSelectedMovie(null)}
          submitting={submitting}
        />
      </div>
    </section>
  );
}

export default AdminDashboard;
