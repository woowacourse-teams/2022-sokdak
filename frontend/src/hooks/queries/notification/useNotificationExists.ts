import { useQuery, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetNotificationExists } from '@/apis/notification';
import QUERY_KEYS from '@/constants/queries';

const useNotificationExists = ({
  options,
}: {
  options?: Omit<
    UseQueryOptions<
      { existence: boolean },
      AxiosError<{ message: string }>,
      boolean,
      typeof QUERY_KEYS['NOTIFICATION_EXISTS']
    >,
    'queryKey' | 'queryFn'
  >;
}) =>
  useQuery(QUERY_KEYS.NOTIFICATION_EXISTS, () => requestGetNotificationExists(), {
    ...options,
  });

export default useNotificationExists;
