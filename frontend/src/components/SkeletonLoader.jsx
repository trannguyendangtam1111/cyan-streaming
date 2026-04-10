function SkeletonLoader({ variant = "card", count = 1 }) {
  if (variant === "hero") {
    return (
      <div className="relative overflow-hidden rounded-[2rem] border border-white/10 bg-white/5 p-10">
        <div className="animate-pulse space-y-5">
          <div className="h-5 w-40 rounded-full bg-white/10" />
          <div className="h-16 w-2/3 rounded-3xl bg-white/10" />
          <div className="h-5 w-1/2 rounded-full bg-white/10" />
          <div className="h-24 w-full max-w-2xl rounded-3xl bg-white/10" />
          <div className="flex gap-4">
            <div className="h-12 w-32 rounded-full bg-white/10" />
            <div className="h-12 w-32 rounded-full bg-white/10" />
          </div>
        </div>
      </div>
    );
  }

  if (variant === "details") {
    return (
      <div className="animate-pulse space-y-6 rounded-[2rem] border border-white/10 bg-white/5 p-8">
        <div className="h-5 w-32 rounded-full bg-white/10" />
        <div className="h-16 w-2/3 rounded-3xl bg-white/10" />
        <div className="h-6 w-1/3 rounded-full bg-white/10" />
        <div className="h-40 w-full rounded-[1.5rem] bg-white/10" />
      </div>
    );
  }

  if (variant === "comments") {
    return (
      <div className="space-y-4">
        {Array.from({ length: count }).map((_, index) => (
          <div
            key={`comment-skeleton-${index + 1}`}
            className="animate-pulse rounded-[1.5rem] border border-white/10 bg-white/5 p-5"
          >
            <div className="h-4 w-32 rounded-full bg-white/10" />
            <div className="mt-4 h-4 w-full rounded-full bg-white/10" />
            <div className="mt-3 h-4 w-5/6 rounded-full bg-white/10" />
          </div>
        ))}
      </div>
    );
  }

  return (
    <div className="scrollbar-hide flex gap-5 overflow-x-auto pb-3">
      {Array.from({ length: count }).map((_, index) => (
        <div
          key={`skeleton-${index + 1}`}
          className="min-w-[220px] animate-pulse overflow-hidden rounded-2xl border border-white/10 bg-white/5"
        >
          <div className="aspect-[2/3] bg-white/10" />
          <div className="space-y-3 p-5">
            <div className="h-4 w-1/3 rounded-full bg-white/10" />
            <div className="h-8 w-3/4 rounded-full bg-white/10" />
            <div className="h-4 w-1/2 rounded-full bg-white/10" />
          </div>
        </div>
      ))}
    </div>
  );
}

export default SkeletonLoader;
