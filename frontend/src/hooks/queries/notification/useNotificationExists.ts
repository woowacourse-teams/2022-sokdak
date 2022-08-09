import { useQuery, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis';

const useNotificationExists = ({
  options,
}: {
  options?: Omit<
    UseQueryOptions<
      AxiosResponse<{ existence: boolean }>,
      AxiosError<{ message: string }>,
      boolean,
      'notification-exists'
    >,
    'queryKey' | 'queryFn'
  >;
}) =>
  useQuery('notification-exists', () => authFetcher.get('/notifications/check'), {
    select: data => data.data.existence,
    ...options,
  });

export default useNotificationExists;
