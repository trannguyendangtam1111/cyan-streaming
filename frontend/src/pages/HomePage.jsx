import { useDeferredValue, useEffect, useMemo, useState } from "react";
import ContinueWatchingRow from "../components/ContinueWatchingRow";
import GenreFilter from "../components/GenreFilter";
import HeroBanner from "../components/HeroBanner";
import MovieCarousel from "../components/MovieCarousel";
import SearchBar from "../components/SearchBar";
import SkeletonLoader from "../components/SkeletonLoader";
import SortingOptions from "../components/SortingOptions";
import useAuth from "../hooks/useAuth";
import useDebounce from "../hooks/useDebounce";
import { trackAnalyticsEvent } from "../services/analyticsApi";
import { getAllMovies, searchMovies } from "../services/movieApi";
import { getProgress } from "../services/progressApi";

const sortMovies = (movies, sortBy) => {
  const sorted = [...movies];

  if (sortBy === "rating") {
    sorted.sort((first, second) => second.rating - first.rating);
  } else if (sortBy === "releaseYear") {
    sorted.sort((first, second) => second.releaseYear - first.releaseYear);
  }

  return sorted;
};

function HomePage() {
  const { isAuthenticated } = useAuth();
  const [allMovies, setAllMovies] = useState([]);
  const [movies, setMovies] = useState([]);
  const [progress, setProgress] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [query, setQuery] = useState("");
  const [genre, setGenre] = useState("All");
  const [sortBy, setSortBy] = useState("featured");

  const deferredQuery = useDeferredValue(query);
  const debouncedQuery = useDebounce(deferredQuery, 300);

  useEffect(() => {
    const bootstrapCatalog = async () => {
      try {
        setLoading(true);
        const data = await getAllMovies();
        setAllMovies(data);
        setMovies(data);
      } catch {
        setError("Unable to load movies. Make sure the backend is running on port 8080.");
      } finally {
        setLoading(false);
      }
    };

    bootstrapCatalog();
  }, []);

  useEffect(() => {
    if (!allMovies.length && !debouncedQuery) {
      return;
    }

    const loadCatalog = async () => {
      try {
        setLoading(true);
        setError("");

        let data;
        if (debouncedQuery.trim()) {
          data = await searchMovies(debouncedQuery.trim());
          if (genre !== "All") {
            data = data.filter((movie) => movie.genre === genre);
          }
        } else if (genre !== "All") {
          data = await getAllMovies(genre);
        } else {
          data = allMovies;
        }

        setMovies(data);
      } catch {
        setError("Unable to update the catalog filters right now.");
      } finally {
        setLoading(false);
      }
    };

    loadCatalog();
  }, [allMovies, debouncedQuery, genre]);

  useEffect(() => {
    if (!isAuthenticated) {
      setProgress([]);
      return;
    }

    const loadProgress = async () => {
      try {
        const response = await getProgress();
        setProgress(response);
      } catch {
        setProgress([]);
      }
    };

    loadProgress();
  }, [isAuthenticated]);

  const featuredMovie = useMemo(() => allMovies[0], [allMovies]);

  const genres = useMemo(
    () => [...new Set(allMovies.map((movie) => movie.genre))].sort((first, second) => first.localeCompare(second)),
    [allMovies],
  );

  const sortedMovies = useMemo(() => sortMovies(movies, sortBy), [movies, sortBy]);

  const isFiltering = debouncedQuery.trim() || genre !== "All";

  useEffect(() => {
    if (!debouncedQuery.trim()) {
      return;
    }

    trackAnalyticsEvent({
      eventType: "search_usage",
      query: debouncedQuery.trim(),
      source: "home",
    }).catch(() => {});
  }, [debouncedQuery]);

  return (
    <div className="pb-16">
      {loading && !featuredMovie ? (
        <section className="mx-auto max-w-7xl px-4 pb-12 pt-8 sm:px-6 lg:px-8">
          <SkeletonLoader variant="hero" />
        </section>
      ) : (
        <HeroBanner movie={featuredMovie} />
      )}

      <section className="mx-auto max-w-7xl space-y-10 px-4 sm:px-6 lg:px-8">
        <div className="grid gap-4 rounded-[2rem] border border-white/10 bg-slate-900/60 p-5 backdrop-blur-xl lg:grid-cols-[1.4fr_0.8fr_0.8fr]">
          <SearchBar value={query} onChange={setQuery} />
          <GenreFilter genres={genres} value={genre} onChange={setGenre} />
          <SortingOptions value={sortBy} onChange={setSortBy} />
        </div>

        {error && (
          <div className="rounded-[2rem] border border-red-400/30 bg-red-400/10 p-8 text-red-100">
            {error}
          </div>
        )}

        {isAuthenticated && progress.length > 0 && <ContinueWatchingRow progress={progress} />}

        {loading ? (
          <SkeletonLoader count={4} />
        ) : (
          <>
            {isFiltering ? (
              <MovieCarousel
                eyebrow="Filtered Catalog"
                title={debouncedQuery ? "Search Results" : `${genre} Picks`}
                movies={sortedMovies}
                emptyMessage="Try another search or filter combination."
              />
            ) : (
              <>
                <MovieCarousel eyebrow="Now Streaming" title="Trending" section="TRENDING" />
                <MovieCarousel eyebrow="Critics" title="Top Rated" section="TOP_RATED" />
                <MovieCarousel eyebrow="Fresh Drop" title="New Releases" section="NEW_RELEASES" />
                <MovieCarousel eyebrow="Pulse" title="Action" section="ACTION" />
                <MovieCarousel eyebrow="Lift" title="Comedy" section="COMEDY" />
                <MovieCarousel eyebrow="After Dark" title="Drama" section="DRAMA" />
              </>
            )}
          </>
        )}
      </section>
    </div>
  );
}

export default HomePage;
