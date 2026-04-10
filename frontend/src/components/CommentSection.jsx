import { useEffect, useState } from "react";
import useAuth from "../hooks/useAuth";
import { addComment, getMovieComments } from "../services/movieApi";
import SkeletonLoader from "./SkeletonLoader";

function CommentSection({ movieId }) {
  const { isAuthenticated, user } = useAuth();
  const [comments, setComments] = useState([]);
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadComments = async () => {
      try {
        setLoading(true);
        const response = await getMovieComments(movieId);
        setComments(response);
      } catch (requestError) {
        setError("Unable to load comments.");
      } finally {
        setLoading(false);
      }
    };

    loadComments();
  }, [movieId]);

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!content.trim()) {
      return;
    }

    try {
      setSubmitting(true);
      setError("");
      const created = await addComment(movieId, content.trim());
      setComments((currentComments) => [created, ...currentComments]);
      setContent("");
    } catch (requestError) {
      setError("Unable to post your comment.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <section className="rounded-[2rem] border border-white/10 bg-slate-900/70 p-6">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">Community</p>
          <h2 className="mt-2 font-display text-4xl uppercase tracking-[0.08em] text-white">Comments</h2>
        </div>
        <p className="text-sm text-slate-400">{comments.length} voices in the thread</p>
      </div>

      {isAuthenticated ? (
        <form onSubmit={handleSubmit} className="mt-6 space-y-4">
          <textarea
            value={content}
            onChange={(event) => setContent(event.target.value)}
            placeholder={`Share your take, ${user?.username}...`}
            rows={4}
            className="w-full rounded-[1.5rem] border border-white/10 bg-slate-950/60 p-4 text-sm text-white outline-none transition focus:border-cyan-400/50"
          />
          <button
            type="submit"
            disabled={submitting}
            className="rounded-full bg-cyan-400 px-6 py-3 text-sm font-semibold uppercase tracking-[0.3em] text-slate-950 transition hover:bg-cyan-300 disabled:cursor-not-allowed disabled:opacity-60"
          >
            {submitting ? "Posting..." : "Post Comment"}
          </button>
        </form>
      ) : (
        <p className="mt-6 text-sm text-slate-400">Log in to join the conversation.</p>
      )}

      {error && <p className="mt-4 text-sm text-red-300">{error}</p>}

      <div className="mt-8 space-y-4">
        {loading && <SkeletonLoader variant="comments" count={3} />}
        {!loading &&
          comments.map((comment) => (
            <article key={comment.id} className="rounded-[1.5rem] border border-white/10 bg-slate-950/60 p-5">
              <div className="flex flex-wrap items-center justify-between gap-2">
                <p className="text-sm font-semibold uppercase tracking-[0.24em] text-cyan-200">
                  {comment.username}
                </p>
                <p className="text-xs uppercase tracking-[0.24em] text-slate-500">
                  {new Date(comment.createdAt).toLocaleString()}
                </p>
              </div>
              <p className="mt-3 text-sm leading-7 text-slate-200">{comment.content}</p>
            </article>
          ))}
        {!loading && comments.length === 0 && (
          <p className="rounded-[1.5rem] border border-white/10 bg-slate-950/60 p-5 text-sm text-slate-400">
            No comments yet. Be the first to leave one.
          </p>
        )}
      </div>
    </section>
  );
}

export default CommentSection;
