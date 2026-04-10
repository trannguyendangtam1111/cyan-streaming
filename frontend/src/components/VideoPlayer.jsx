function VideoPlayer({ movie }) {
  if (!movie) {
    return null;
  }

  const embedUrl = movie.youtubeEmbedUrl.includes("?")
    ? `${movie.youtubeEmbedUrl}&autoplay=1&rel=0&modestbranding=1`
    : `${movie.youtubeEmbedUrl}?autoplay=1&rel=0&modestbranding=1`;

  return (
    <div className="overflow-hidden rounded-[2rem] border border-white/10 bg-slate-900/70 shadow-2xl shadow-cyan-950/30">
      <div className="aspect-video w-full">
        <iframe
          className="h-full w-full"
          src={embedUrl}
          title={movie.title}
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
          allowFullScreen
        />
      </div>
    </div>
  );
}

export default VideoPlayer;
