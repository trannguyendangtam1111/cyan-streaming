import { useEffect, useMemo, useRef, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import VideoPlayer from "../components/VideoPlayer";
import useAuth from "../hooks/useAuth";
import { trackAnalyticsEvent } from "../services/analyticsApi";
import { getMovieById, getRecommendedMovies } from "../services/movieApi";
import { saveProgress } from "../services/progressApi";

const formatElapsed = (seconds) => {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = seconds % 60;
  return `${minutes}m ${remainingSeconds}s`;
};

function PlayerPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [movie, setMovie] = useState(null);
  const [recommended, setRecommended] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [progressSeconds, setProgressSeconds] = useState(0);
  const [autoplayNext, setAutoplayNext] = useState(true);
  const [countdown, setCountdown] = useState(null);
  const latestProgressRef = useRef(0);
  const playerContainerRef = useRef(null);
  const trackedMovieRef = useRef(null);

  useEffect(() => {
    const loadMovie = async () => {
      try {
        setLoading(true);
        const [movieResponse, recommendedResponse] = await Promise.all([
          getMovieById(id),
          getRecommendedMovies(id),
        ]);
        setMovie(movieResponse);
        setRecommended(recommendedResponse);
        setCountdown(null);
        setProgressSeconds(0);
      } catch {
        setError("Unable to start playback. Confirm the backend is running.");
      } finally {
        setLoading(false);
      }
    };

    loadMovie();
  }, [id]);

  useEffect(() => {
    latestProgressRef.current = progressSeconds;
  }, [progressSeconds]);

  useEffect(() => {
    if (!movie || trackedMovieRef.current === movie.id) {
      return;
    }

    trackedMovieRef.current = movie.id;
    trackAnalyticsEvent({
      eventType: "movie_play",
      movieId: movie.id,
      source: "player",
    }).catch(() => {});
  }, [movie]);

  useEffect(() => {
    if (!movie || !isAuthenticated) {
      return undefined;
    }

    const interval = setInterval(() => {
      setProgressSeconds((current) => {
        const next = current + 5;
        if (next > 0 && next % 15 === 0) {
          saveProgress({ movieId: movie.id, timestamp: next }).catch(() => {});
        }
        return next;
      });
    }, 5000);

    return () => {
      clearInterval(interval);
      if (latestProgressRef.current > 0) {
        saveProgress({ movieId: movie.id, timestamp: latestProgressRef.current }).catch(() => {});
      }
    };
  }, [isAuthenticated, movie]);

  useEffect(() => {
    const nextMovie = recommended[0];

    if (!autoplayNext || !nextMovie) {
      setCountdown(null);
      return;
    }

    if (progressSeconds >= 90 && countdown === null) {
      setCountdown(8);
    }
  }, [autoplayNext, countdown, progressSeconds, recommended]);

  useEffect(() => {
    const nextMovie = recommended[0];

    if (countdown === null || !nextMovie) {
      return undefined;
    }

    if (countdown <= 0) {
      navigate(`/watch/${nextMovie.id}`);
      return undefined;
    }

    const timeout = setTimeout(() => {
      setCountdown((current) => (current === null ? null : current - 1));
    }, 1000);

    return () => clearTimeout(timeout);
  }, [countdown, navigate, recommended]);

  useEffect(() => {
    const handleKeyDown = (event) => {
      if (event.key.toLowerCase() === "f") {
        if (!document.fullscreenElement) {
          playerContainerRef.current?.requestFullscreen?.();
        } else {
          document.exitFullscreen?.();
        }
      }

      if (event.key.toLowerCase() === "n" && recommended[0]) {
        navigate(`/watch/${recommended[0].id}`);
      }

      if (event.key.toLowerCase() === "a") {
        setAutoplayNext((current) => !current);
      }
    };

    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [navigate, recommended]);

  const progressLabel = useMemo(() => formatElapsed(progressSeconds), [progressSeconds]);
  const nextMovie = recommended[0];

  const toggleFullscreen = async () => {
    if (!document.fullscreenElement) {
      await playerContainerRef.current?.requestFullscreen?.();
      return;
    }

    await document.exitFullscreen?.();
  };

  return (
    <section className="mx-auto max-w-7xl px-4 py-10 sm:px-6 lg:px-8">
      <div className="mb-8 flex flex-wrap items-center justify-between gap-4">
        <div>
          <p className="text-xs uppercase tracking-[0.4em] text-cyan-300">Now Playing</p>
          <h1 className="mt-3 font-display text-5xl uppercase leading-none tracking-[0.08em] text-white">
            {movie?.title ?? "Loading"}
          </h1>
        </div>
        <Link
          to={movie ? `/movie/${movie.id}` : "/"}
          className="rounded-full border border-white/15 bg-white/5 px-6 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-white transition hover:bg-white/10"
        >
          Back
        </Link>
      </div>

      {loading && (
        <div className="rounded-[2rem] border border-white/10 bg-white/5 p-8 text-slate-300">
          Loading player...
        </div>
      )}

      {error && (
        <div className="rounded-[2rem] border border-red-400/30 bg-red-400/10 p-8 text-red-100">
          {error}
        </div>
      )}

      {!loading && !error && movie && (
        <div className="space-y-8">
          <div ref={playerContainerRef} className="space-y-4">
            <VideoPlayer movie={movie} />
            <div className="flex flex-wrap items-center gap-3">
              <button
                type="button"
                onClick={toggleFullscreen}
                className="rounded-full border border-white/15 bg-white/5 px-5 py-3 text-xs font-semibold uppercase tracking-[0.3em] text-white transition hover:bg-white/10"
              >
                Fullscreen
              </button>
              <button
                type="button"
                onClick={() => setAutoplayNext((current) => !current)}
                className="rounded-full border border-cyan-400/30 bg-cyan-400/10 px-5 py-3 text-xs font-semibold uppercase tracking-[0.3em] text-cyan-100 transition hover:bg-cyan-400/15"
              >
                Autoplay {autoplayNext ? "On" : "Off"}
              </button>
              {nextMovie && (
                <button
                  type="button"
                  onClick={() => navigate(`/watch/${nextMovie.id}`)}
                  className="rounded-full border border-white/15 bg-white/5 px-5 py-3 text-xs font-semibold uppercase tracking-[0.3em] text-white transition hover:bg-white/10"
                >
                  Next Movie
                </button>
              )}
            </div>
          </div>

          {countdown !== null && nextMovie && (
            <div className="rounded-[2rem] border border-cyan-400/20 bg-cyan-400/10 p-5 text-cyan-50">
              <p className="text-xs uppercase tracking-[0.3em] text-cyan-200">Up Next</p>
              <p className="mt-2 text-lg">
                {nextMovie.title} starts in {countdown}s.
              </p>
              <button
                type="button"
                onClick={() => {
                  setAutoplayNext(false);
                  setCountdown(null);
                }}
                className="mt-4 rounded-full border border-white/20 px-4 py-2 text-xs font-semibold uppercase tracking-[0.28em] text-white"
              >
                Cancel
              </button>
            </div>
          )}

          <div className="grid gap-6 lg:grid-cols-[1.2fr_0.8fr]">
            <div className="rounded-[2rem] border border-white/10 bg-slate-900/70 p-6">
              <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">Synopsis</p>
              <p className="mt-4 text-base leading-8 text-slate-200">{movie.description}</p>
            </div>
            <div className="rounded-[2rem] border border-white/10 bg-slate-900/70 p-6">
              <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">Session</p>
              <div className="mt-4 space-y-3 text-sm text-slate-300">
                <div className="flex items-center justify-between rounded-2xl border border-white/10 bg-white/5 px-4 py-3">
                  <span>Genre</span>
                  <span>{movie.genre}</span>
                </div>
                <div className="flex items-center justify-between rounded-2xl border border-white/10 bg-white/5 px-4 py-3">
                  <span>Release Year</span>
                  <span>{movie.releaseYear}</span>
                </div>
                <div className="flex items-center justify-between rounded-2xl border border-white/10 bg-white/5 px-4 py-3">
                  <span>Tracked Time</span>
                  <span>{progressLabel}</span>
                </div>
              </div>
              <p className="mt-5 text-sm text-slate-400">
                {isAuthenticated
                  ? "Your watch progress is saved automatically while this page stays open."
                  : "Log in if you want Cyan to remember your progress."}
              </p>
              <div className="mt-5 rounded-[1.5rem] border border-white/10 bg-white/5 p-4 text-xs uppercase tracking-[0.24em] text-slate-400">
                Shortcuts: <span className="text-slate-200">F</span> fullscreen, <span className="text-slate-200">N</span> next movie, <span className="text-slate-200">A</span> toggle autoplay
              </div>
            </div>
          </div>
        </div>
      )}
    </section>
  );
}

export default PlayerPage;
