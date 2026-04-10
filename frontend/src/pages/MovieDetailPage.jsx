import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import CommentSection from "../components/CommentSection";
import RatingStars from "../components/RatingStars";
import RecommendedRow from "../components/RecommendedRow";
import SkeletonLoader from "../components/SkeletonLoader";
import { getMovieById, getRecommendedMovies } from "../services/movieApi";

function MovieDetailPage() {
  const { id } = useParams();
  const [movie, setMovie] = useState(null);
  const [recommended, setRecommended] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

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
      } catch {
        setError("Movie not found or the backend is unavailable.");
      } finally {
        setLoading(false);
      }
    };

    loadMovie();
  }, [id]);

  if (loading) {
    return (
      <div className="mx-auto max-w-6xl px-4 py-12 sm:px-6 lg:px-8">
        <SkeletonLoader variant="details" />
      </div>
    );
  }

  if (error || !movie) {
    return (
      <div className="mx-auto max-w-5xl px-4 py-16 sm:px-6 lg:px-8">
        <div className="rounded-[2rem] border border-red-400/30 bg-red-400/10 p-8 text-red-100">{error}</div>
      </div>
    );
  }

  return (
    <section className="mx-auto max-w-7xl space-y-10 px-4 py-10 sm:px-6 lg:px-8">
      <div
        className="overflow-hidden rounded-[2rem] border border-white/10"
        style={{
          backgroundImage: `linear-gradient(90deg, rgba(2,6,23,0.98) 0%, rgba(2,6,23,0.86) 50%, rgba(2,6,23,0.45) 100%), url(${movie.thumbnailUrl})`,
          backgroundPosition: "center",
          backgroundSize: "cover",
        }}
      >
        <div className="grid gap-8 px-6 py-10 sm:px-10 lg:grid-cols-[1.1fr_0.9fr] lg:px-12 lg:py-14">
          <div className="max-w-2xl">
            <p className="text-xs uppercase tracking-[0.4em] text-cyan-300">Movie Detail</p>
            <h1 className="mt-5 font-display text-5xl uppercase leading-[0.9] tracking-[0.08em] text-white sm:text-7xl">
              {movie.title}
            </h1>
            <div className="mt-5 flex flex-wrap gap-3 text-sm uppercase tracking-[0.28em] text-slate-300">
              <span>{movie.genre}</span>
              <span>{movie.releaseYear}</span>
              <span>{movie.rating.toFixed(1)} rating</span>
            </div>
            <p className="mt-8 max-w-xl text-base leading-8 text-slate-200">{movie.description}</p>
            <div className="mt-10 flex flex-wrap gap-4">
              <Link
                to={`/watch/${movie.id}`}
                className="rounded-full bg-cyan-400 px-7 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-slate-950 transition hover:bg-cyan-300"
              >
                Play Movie
              </Link>
              <Link
                to="/"
                className="rounded-full border border-white/15 bg-white/5 px-7 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-white transition hover:bg-white/10"
              >
                Back Home
              </Link>
            </div>
          </div>

          <div className="space-y-6">
            <div className="rounded-[1.75rem] border border-white/10 bg-slate-950/55 p-5 backdrop-blur-xl">
              <img
                src={movie.thumbnailUrl}
                alt={movie.title}
                className="aspect-[16/10] w-full rounded-[1.4rem] object-cover"
              />
              <div className="mt-5 space-y-3 text-sm text-slate-300">
                <div className="flex items-center justify-between rounded-2xl border border-white/10 bg-white/5 px-4 py-3">
                  <span className="uppercase tracking-[0.3em]">Genre</span>
                  <span>{movie.genre}</span>
                </div>
                <div className="flex items-center justify-between rounded-2xl border border-white/10 bg-white/5 px-4 py-3">
                  <span className="uppercase tracking-[0.3em]">Release</span>
                  <span>{movie.releaseYear}</span>
                </div>
                <div className="flex items-center justify-between rounded-2xl border border-white/10 bg-white/5 px-4 py-3">
                  <span className="uppercase tracking-[0.3em]">Rating</span>
                  <span>{movie.rating.toFixed(1)} / 10</span>
                </div>
              </div>
            </div>

            <RatingStars
              movieId={movie.id}
              onRated={(response) =>
                setMovie((currentMovie) => ({
                  ...currentMovie,
                  rating: response.averageRating,
                }))
              }
            />
          </div>
        </div>
      </div>

      <RecommendedRow movies={recommended} />
      <CommentSection movieId={movie.id} />
    </section>
  );
}

export default MovieDetailPage;
