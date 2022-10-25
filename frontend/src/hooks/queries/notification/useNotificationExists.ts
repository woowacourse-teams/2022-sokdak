import { useQuery, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis/authFetcher';
import QUERY_KEYS from '@/constants/queries';

const useNotificationExists = ({
  options,
}: {
  options?: Omit<
    UseQueryOptions<
      AxiosResponse<{ existence: boolean }>,
      AxiosError<{ message: string }>,
      boolean,
      typeof QUERY_KEYS['NOTIFICATION_EXISTS']
    >,
    'queryKey' | 'queryFn'
  >;
}) =>
  useQuery(QUERY_KEYS.NOTIFICATION_EXISTS, () => authFetcher.get('/notifications/check'), {
    select: data => data.data.existence,
    ...options,
  });

export default useNotificationExists;
