import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import QUERY_KEYS from '@/constants/queries';

const useIDCheck = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<AxiosResponse<{ unique: boolean }>, AxiosError<{ message: string }>, boolean, QueryKey[]>;
}) =>
  useQuery(
    [QUERY_KEYS.MEMBER_ID_CHECK, storeCode],
    ({ queryKey: [, username] }) => axios.get(`members/signup/exists?username=${username}`),
    {
      select: data => data.data.unique,
      ...options,
    },
  );

export default useIDCheck;
