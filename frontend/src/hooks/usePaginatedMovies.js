import { useEffect, useState } from "react";
import { browseMovies } from "../services/movieApi";

function usePaginatedMovies({ section, genre, query, enabled = true, size = 12 }) {
  const [items, setItems] = useState([]);
  const [page, setPage] = useState(0);
  const [hasNext, setHasNext] = useState(true);
  const [initialLoading, setInitialLoading] = useState(true);
  const [loadingMore, setLoadingMore] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!enabled) {
      setItems([]);
      setPage(0);
      setHasNext(false);
      setInitialLoading(false);
      return;
    }

    let ignore = false;

    const loadFirstPage = async () => {
      try {
        setInitialLoading(true);
        setError("");
        const response = await browseMovies({
          section,
          genre,
          query,
          page: 0,
          size,
        });

        if (ignore) {
          return;
        }

        setItems(response.content);
        setPage(response.page);
        setHasNext(response.hasNext);
      } catch {
        if (!ignore) {
          setError("Unable to load this row right now.");
        }
      } finally {
        if (!ignore) {
          setInitialLoading(false);
        }
      }
    };

    loadFirstPage();

    return () => {
      ignore = true;
    };
  }, [enabled, genre, query, section, size]);

  const loadMore = async () => {
    if (!enabled || initialLoading || loadingMore || !hasNext) {
      return;
    }

    try {
      setLoadingMore(true);
      const nextPage = page + 1;
      const response = await browseMovies({
        section,
        genre,
        query,
        page: nextPage,
        size,
      });

      setItems((currentItems) => [...currentItems, ...response.content]);
      setPage(response.page);
      setHasNext(response.hasNext);
    } catch {
      setError("Unable to load more movies.");
    } finally {
      setLoadingMore(false);
    }
  };

  return {
    items,
    initialLoading,
    loadingMore,
    error,
    hasNext,
    loadMore,
  };
}

export default usePaginatedMovies;
