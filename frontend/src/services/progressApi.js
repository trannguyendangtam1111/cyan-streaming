import apiClient from "./apiClient";

export const getProgress = async () => {
  const response = await apiClient.get("/progress");
  return response.data;
};

export const saveProgress = async (payload) => {
  const response = await apiClient.post("/progress", payload);
  return response.data;
};
