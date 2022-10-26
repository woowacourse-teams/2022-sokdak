import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { requestGetIDCheck } from '@/apis/member';
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
    ({ queryKey: [, username] }) => requestGetIDCheck(String(username)),
    {
      ...options,
    },
  );

export default useIDCheck;
