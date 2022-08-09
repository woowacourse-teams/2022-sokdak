import { useQuery, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis';

const useNotificationExists = ({
  options,
}: {
  options?: Omit<
    UseQueryOptions<
      AxiosResponse<{ exists: boolean }>,
      AxiosError<{ message: string }>,
      boolean,
      'notification-exists'
    >,
    'queryKey' | 'queryFn'
  >;
}) =>
  useQuery('notification-exists', () => authFetcher.get('/notifications/exists'), {
    select: data => data.data.exists,
    ...options,
  });

export default useNotificationExists;
