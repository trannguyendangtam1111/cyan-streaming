import apiClient from "./apiClient";

const publicRequest = { skipAuth: true };

// Backend endpoints that are supposed to return List<MovieResponse>.
// If the response shape ever changes (paged object, error JSON, HTML),
// we coerce to an empty array so the UI doesn't crash on `.map`.
const expectArray = (data, endpoint) => {
  if (Array.isArray(data)) return data;
  // eslint-disable-next-line no-console
  console.warn(
    `[movieApi] ${endpoint} expected an array, received:`,
    data,
    "— check VITE_API_URL and the backend response shape.",
  );
  return [];
};

export const getAllMovies = async (genre) => {
  const response = await apiClient.get("/movies", {
    ...publicRequest,
    params: genre ? { genre } : {},
  });
  return expectArray(response.data, "GET /movies");
};

export const getMovieById = async (id) => {
  const response = await apiClient.get(`/movies/${id}`, publicRequest);
  return response.data;
};

export const browseMovies = async ({ section, genre, query, page = 0, size = 12 }) => {
  const response = await apiClient.get("/movies/browse", {
    ...publicRequest,
    params: {
      section,
      genre,
      query,
      page,
      size,
    },
  });
  return response.data;
};

export const searchMovies = async (query) => {
  const response = await apiClient.get("/movies/search", {
    ...publicRequest,
    params: { query },
  });
  return expectArray(response.data, "GET /movies/search");
};

export const getRecommendedMovies = async (id) => {
  const response = await apiClient.get(`/movies/recommend/${id}`, publicRequest);
  return expectArray(response.data, `GET /movies/recommend/${id}`);
};

export const createMovie = async (movie) => {
  const response = await apiClient.post("/admin/movies", movie);
  return response.data;
};

export const updateMovie = async (id, movie) => {
  const response = await apiClient.put(`/admin/movies/${id}`, movie);
  return response.data;
};

export const deleteMovie = async (id) => apiClient.delete(`/admin/movies/${id}`);

export const rateMovie = async (id, value) => {
  const response = await apiClient.post(`/movies/${id}/rating`, { value });
  return response.data;
};

export const addComment = async (id, content) => {
  const response = await apiClient.post(`/movies/${id}/comment`, { content });
  return response.data;
};

export const getMovieComments = async (id) => {
  const response = await apiClient.get(`/movies/${id}/comments`, publicRequest);
  return response.data;
};

export default apiClient;
