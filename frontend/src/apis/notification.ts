import authFetcher from './authFetcher';

export const requestGetNotifications = async (size: number, pageParam: number) => {
  const { data } = await authFetcher.get(`/notifications?size=${size}&page=${pageParam}`);

  return data;
};

export const requestGetNotificationExists = async () => {
  const { data } = await authFetcher.get('/notifications/check');

  return data.existence;
};

export const requestDeleteNotification = (id: string) => authFetcher.delete(`/notifications/${id}`);
