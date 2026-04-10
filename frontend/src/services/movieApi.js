import apiClient from "./apiClient";

export const getAllMovies = async (genre) => {
  const response = await apiClient.get("/movies", {
    params: genre ? { genre } : {},
  });
  return response.data;
};

export const getMovieById = async (id) => {
  const response = await apiClient.get(`/movies/${id}`);
  return response.data;
};

export const browseMovies = async ({ section, genre, query, page = 0, size = 12 }) => {
  const response = await apiClient.get("/movies/browse", {
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
    params: { query },
  });
  return response.data;
};

export const getRecommendedMovies = async (id) => {
  const response = await apiClient.get(`/movies/recommend/${id}`);
  return response.data;
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
  const response = await apiClient.get(`/movies/${id}/comments`);
  return response.data;
};

export default apiClient;
