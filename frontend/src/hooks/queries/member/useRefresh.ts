import { useQuery, UseQueryOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import { STORAGE_KEY } from '@/constants/localStorage';
import QUERY_KEYS from '@/constants/queries';

const useRefresh = ({
  options,
}: {
  options?: Omit<
    UseQueryOptions<AxiosResponse<never>, AxiosError<{ message: string }>, AxiosResponse<never, never>, string>,
    'queryKey' | 'queryFn'
  >;
}) => {
  return useQuery(QUERY_KEYS.MEMBER_REFRESH, () => axios.get<never>(`refresh`), {
    onSuccess(data) {
      if (data?.headers.authorization) {
        axios.defaults.headers.common['Authorization'] = data?.headers.authorization;
        localStorage.setItem(STORAGE_KEY.ACCESS_TOKEN, data.headers.authorization);
      }
    },
    onError() {
      localStorage.setItem(STORAGE_KEY.ACCESS_TOKEN, '');
      localStorage.setItem(STORAGE_KEY.REFRESH_TOKEN, '');
    },
    ...options,
    enabled: true,
  });
};

export default useRefresh;
