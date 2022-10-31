import { AxiosResponse } from 'axios';

import authFetcher from './authFetcher';

export interface GetNotificationsResponse {
  notifications: Notice[];
  lastPage: boolean;
}

export const requestGetNotifications = async (size: number, pageParam: number) => {
  const { data } = await authFetcher.get<GetNotificationsResponse, AxiosResponse<GetNotificationsResponse>>(
    `/notifications?size=${size}&page=${pageParam}`,
  );

  return data;
};

export interface GetNotificationExistsResponse {
  existence: boolean;
}

export const requestGetNotificationExists = async () => {
  const { data } = await authFetcher.get<GetNotificationExistsResponse>('/notifications/check');

  return data.existence;
};

export const requestDeleteNotification = (id: string) =>
  authFetcher.delete<null, AxiosResponse<null>>(`/notifications/${id}`);
