import { useEffect, useState } from "react";

const emptyMovie = {
  title: "",
  description: "",
  thumbnailUrl: "",
  youtubeEmbedUrl: "",
  genre: "",
  releaseYear: "",
  rating: "",
};

function MovieForm({ movie, onSubmit, onCancel, submitting }) {
  const [form, setForm] = useState(emptyMovie);

  useEffect(() => {
    if (movie) {
      setForm({
        title: movie.title ?? "",
        description: movie.description ?? "",
        thumbnailUrl: movie.thumbnailUrl ?? "",
        youtubeEmbedUrl: movie.youtubeEmbedUrl ?? "",
        genre: movie.genre ?? "",
        releaseYear: movie.releaseYear ?? "",
        rating: movie.rating ?? "",
      });
      return;
    }

    setForm(emptyMovie);
  }, [movie]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    onSubmit({
      ...form,
      releaseYear: Number(form.releaseYear),
      rating: Number(form.rating),
    });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 rounded-[2rem] border border-white/10 bg-slate-900/70 p-6">
      <div>
        <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">Admin Form</p>
        <h2 className="mt-2 font-display text-4xl uppercase tracking-[0.08em] text-white">
          {movie ? "Edit Movie" : "Add Movie"}
        </h2>
      </div>

      {[
        ["title", "Title"],
        ["genre", "Genre"],
        ["thumbnailUrl", "Thumbnail URL"],
        ["youtubeEmbedUrl", "YouTube Embed URL"],
        ["releaseYear", "Release Year"],
        ["rating", "Rating"],
      ].map(([name, label]) => (
        <label key={name} className="block space-y-2">
          <span className="text-xs uppercase tracking-[0.24em] text-slate-400">{label}</span>
          <input
            name={name}
            value={form[name]}
            onChange={handleChange}
            className="w-full rounded-2xl border border-white/10 bg-slate-950/60 px-4 py-3 text-sm text-white outline-none transition focus:border-cyan-400/40"
            required
          />
        </label>
      ))}

      <label className="block space-y-2">
        <span className="text-xs uppercase tracking-[0.24em] text-slate-400">Description</span>
        <textarea
          name="description"
          rows={5}
          value={form.description}
          onChange={handleChange}
          className="w-full rounded-2xl border border-white/10 bg-slate-950/60 px-4 py-3 text-sm text-white outline-none transition focus:border-cyan-400/40"
          required
        />
      </label>

      <div className="flex gap-3">
        <button
          type="submit"
          disabled={submitting}
          className="rounded-full bg-cyan-400 px-6 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-slate-950 transition hover:bg-cyan-300 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {submitting ? "Saving..." : movie ? "Update Movie" : "Create Movie"}
        </button>
        {movie && (
          <button
            type="button"
            onClick={onCancel}
            className="rounded-full border border-white/15 bg-white/5 px-6 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-white transition hover:bg-white/10"
          >
            Cancel
          </button>
        )}
      </div>
    </form>
  );
}

export default MovieForm;
