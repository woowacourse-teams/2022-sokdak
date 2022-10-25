import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import api from '@/apis';
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
    ({ queryKey: [, nickname] }) => api.get(`members/signup/exists?nickname=${nickname}`),
    {
      select: data => data.data.unique,
      ...options,
    },
  );

export default useNicknameCheck;
