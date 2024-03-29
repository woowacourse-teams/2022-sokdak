import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetNicknameCheck } from '@/api/member';
import QUERY_KEYS from '@/constants/queries';

const useNicknameCheck = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<boolean, AxiosError, boolean, QueryKey[]>;
}) =>
  useQuery(
    [QUERY_KEYS.MEMBER_NICKNAME_CHECK, storeCode],
    ({ queryKey: [, nickname] }) => requestGetNicknameCheck(String(nickname)),
    {
      ...options,
      cacheTime: 0,
      staleTime: 0,
      suspense: false,
    },
  );

export default useNicknameCheck;
