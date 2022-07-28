import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import QUERY_KEYS from '@/constants/queries';

const useNicknameCheck = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<AxiosResponse<{ unique: boolean }>, AxiosError<{ message: string }>, boolean, QueryKey[]>;
}) =>
  useQuery(
    [QUERY_KEYS.MEMBER_NICKNAME_CHECK, storeCode],
    ({ queryKey: [, nickname] }) => axios.get(`members/signup/exists?nickname=${nickname}`),
    {
      select: data => data.data.unique,
      ...options,
    },
  );

export default useNicknameCheck;
