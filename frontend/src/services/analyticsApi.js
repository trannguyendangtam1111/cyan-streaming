import apiClient from "./apiClient";

export const trackAnalyticsEvent = async (payload) => {
  const response = await apiClient.post("/analytics/event", payload);
  return response.data;
};
