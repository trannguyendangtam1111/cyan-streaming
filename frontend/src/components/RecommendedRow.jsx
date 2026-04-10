import MovieCarousel from "./MovieCarousel";

function RecommendedRow({ movies }) {
  return (
    <MovieCarousel
      eyebrow="Because You Watched"
      title="Recommended Next"
      movies={movies}
      emptyMessage="No similar recommendations yet."
    />
  );
}

export default RecommendedRow;
