import { useInfiniteQuery, QueryKey, UseInfiniteQueryOptions, useQueryClient } from 'react-query';

import { AxiosResponse, AxiosError } from 'axios';

import authFetcher from '@/apis';
import QUERY_KEYS from '@/constants/queries';

type Size = number;

const useNotifications = ({
  storeCode,
  options,
}: {
  storeCode: [Size];
  options?: UseInfiniteQueryOptions<
    AxiosResponse<{ notifications: Notice[]; lastPage: boolean }>,
    AxiosError,
    Notice,
    AxiosResponse<{ notifications: Notice[]; lastPage: boolean }>,
    [QueryKey, Size]
  >;
}) => {
  const queryClient = useQueryClient();

  return useInfiniteQuery(
    [QUERY_KEYS.NOTIFICATIONS, ...storeCode],
    ({ pageParam = 0, queryKey: [, size] }) => authFetcher.get(`/notifications?size=${size}&page=${pageParam}`),
    {
      select: data => ({
        pages: data.pages.flatMap(
          (page: AxiosResponse<{ notifications: Notice[]; lastPage: boolean }>) => page.data.notifications,
        ),
        pageParams: [...data.pageParams, data.pageParams.length],
      }),
      getNextPageParam: (lastPage, allPages) => (lastPage.data.lastPage ? undefined : allPages.length),
      onSuccess: () => {
        queryClient.refetchQueries(QUERY_KEYS.NOTIFICATION_EXISTS);
      },
      ...options,
    },
  );
};

export default useNotifications;
