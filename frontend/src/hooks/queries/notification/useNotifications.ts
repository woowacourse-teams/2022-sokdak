import { useInfiniteQuery, QueryKey, UseInfiniteQueryOptions, useQueryClient } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetNotifications } from '@/api/notification';
import type { GetNotificationsResponse } from '@/api/notification';
import QUERY_KEYS from '@/constants/queries';

type Size = number;

const useNotifications = ({
  storeCode,
  options,
}: {
  storeCode: [Size];
  options?: UseInfiniteQueryOptions<
    GetNotificationsResponse,
    AxiosError,
    Notice,
    GetNotificationsResponse,
    [QueryKey, Size]
  >;
}) => {
  const queryClient = useQueryClient();

  return useInfiniteQuery(
    [QUERY_KEYS.NOTIFICATIONS, ...storeCode],
    ({ pageParam = 0, queryKey: [, size] }) => requestGetNotifications(size, pageParam),
    {
      select: data => ({
        pages: data.pages.flatMap((page: { notifications: Notice[]; lastPage: boolean }) => page.notifications),
        pageParams: [...data.pageParams, data.pageParams.length],
      }),
      getNextPageParam: (lastPage, allPages) => (lastPage.lastPage ? undefined : allPages.length),
      onSuccess: () => {
        queryClient.refetchQueries(QUERY_KEYS.NOTIFICATION_EXISTS);
      },
      ...options,
    },
  );
};

export default useNotifications;
