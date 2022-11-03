import authFetcher from './fetcher/auth';
import extractDataFromAxios from './util/extractor';

export interface GetNotificationsResponse {
  notifications: Notice[];
  lastPage: boolean;
}

export const requestGetNotifications = (size: number, pageParam: number) =>
  extractDataFromAxios<GetNotificationsResponse>(authFetcher.get(`/notifications?size=${size}&page=${pageParam}`));

export interface GetNotificationExistsResponse {
  existence: boolean;
}

export const requestGetNotificationExists = () =>
  extractDataFromAxios<GetNotificationExistsResponse>(
    authFetcher.get<GetNotificationExistsResponse>('/notifications/check'),
  ).then(data => data.existence);

export const requestDeleteNotification = (id: string) =>
  extractDataFromAxios<null>(authFetcher.delete(`/notifications/${id}`));
