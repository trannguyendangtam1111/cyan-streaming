function GenreFilter({ genres, value, onChange }) {
  return (
    <label className="flex items-center gap-3 rounded-2xl border border-white/10 bg-slate-900/80 px-4 py-3 text-sm text-slate-200 shadow-lg shadow-slate-950/30">
      <span className="text-cyan-300">Genre</span>
      <select
        value={value}
        onChange={(event) => onChange(event.target.value)}
        className="bg-transparent text-slate-100 outline-none"
      >
        <option value="All" className="bg-slate-900">
          All
        </option>
        {genres.map((genre) => (
          <option key={genre} value={genre} className="bg-slate-900">
            {genre}
          </option>
        ))}
      </select>
    </label>
  );
}

export default GenreFilter;
