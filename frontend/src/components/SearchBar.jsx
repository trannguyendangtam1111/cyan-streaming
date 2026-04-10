function SearchBar({ value, onChange }) {
  return (
    <label className="flex min-w-[260px] flex-1 items-center gap-3 rounded-2xl border border-white/10 bg-slate-900/80 px-4 py-3 text-sm text-slate-200 shadow-lg shadow-slate-950/30">
      <span className="text-cyan-300">Search</span>
      <input
        type="text"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        placeholder="Titles, moods, or genres"
        className="w-full border-none bg-transparent outline-none placeholder:text-slate-500"
      />
    </label>
  );
}

export default SearchBar;
