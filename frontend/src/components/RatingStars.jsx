import { useState } from "react";
import useAuth from "../hooks/useAuth";
import { rateMovie } from "../services/movieApi";

function RatingStars({ movieId, onRated }) {
  const { isAuthenticated } = useAuth();
  const [submitting, setSubmitting] = useState(false);
  const [current, setCurrent] = useState(0);
  const [error, setError] = useState("");

  const handleRate = async (value) => {
    if (!isAuthenticated || submitting) {
      return;
    }

    try {
      setSubmitting(true);
      setError("");
      setCurrent(value);
      const response = await rateMovie(movieId, value);
      onRated?.(response);
    } catch (requestError) {
      setError("Unable to save your rating right now.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="rounded-[1.75rem] border border-white/10 bg-slate-900/70 p-6">
      <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">Your Rating</p>
      <div className="mt-4 flex items-center gap-2">
        {[1, 2, 3, 4, 5].map((value) => (
          <button
            key={value}
            type="button"
            onClick={() => handleRate(value)}
            disabled={!isAuthenticated || submitting}
            className={`text-3xl transition ${
              value <= current ? "text-cyan-300" : "text-slate-600"
            } ${isAuthenticated ? "hover:scale-110" : "cursor-not-allowed"}`}
          >
            ★
          </button>
        ))}
      </div>
      {!isAuthenticated && (
        <p className="mt-4 text-sm text-slate-400">Log in to rate this movie and personalize your catalog.</p>
      )}
      {error && <p className="mt-4 text-sm text-red-300">{error}</p>}
    </div>
  );
}

export default RatingStars;
