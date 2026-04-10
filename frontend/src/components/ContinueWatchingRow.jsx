import MovieCarousel from "./MovieCarousel";

function ContinueWatchingRow({ progress }) {
  const movies = progress.map((item) => ({
    id: item.movieId,
    title: item.title,
    thumbnailUrl: item.thumbnailUrl,
    genre: item.genre,
    releaseYear: item.releaseYear,
    rating: item.rating,
    description: `Resume watching from ${item.timestamp} seconds.`,
  }));

  const progressMap = progress.reduce((accumulator, item) => {
    accumulator[item.movieId] = item.timestamp;
    return accumulator;
  }, {});

  return (
    <MovieCarousel
      eyebrow="For You"
      title="Continue Watching"
      movies={movies}
      progressMap={progressMap}
      emptyMessage="Your watch history will appear here once you start streaming."
    />
  );
}

export default ContinueWatchingRow;
