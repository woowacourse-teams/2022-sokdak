import { InfiniteData, useMutation, UseMutationOptions, useQueryClient } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis';
import { SIZE } from '@/constants/api';
import QUERY_KEYS from '@/constants/queries';

interface DeleteNotificationProps {
  id: number;
}

const useDeleteNotification = (
  options?: UseMutationOptions<AxiosResponse<never>, AxiosError<{ message: string }>, DeleteNotificationProps>,
) => {
  const queryClient = useQueryClient();

  return useMutation(({ id }) => authFetcher.delete(`/notifications/${id}`), {
    ...options,
    onSuccess: (_, variables) => {
      queryClient.getQueryData(QUERY_KEYS.NOTIFICATIONS);
      queryClient.setQueryData<InfiniteData<AxiosResponse<{ notifications: Notice[]; lastPage: boolean }>>>(
        [QUERY_KEYS.NOTIFICATIONS, SIZE.NOTIFICATION_LOAD],
        data =>
          ({
            pageParams: data?.pageParams,
            pages: data?.pages.map(page => ({
              ...page,
              data: {
                ...page.data,
                notifications: page.data.notifications.filter(notice => notice.id !== variables.id),
              },
            })),
          } as InfiniteData<AxiosResponse<{ notifications: Notice[]; lastPage: boolean }>>),
      );
    },
  });
};

export default useDeleteNotification;
