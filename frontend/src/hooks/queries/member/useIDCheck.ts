import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetIDCheck } from '@/api/member';
import QUERY_KEYS from '@/constants/queries';

const useIDCheck = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<boolean, AxiosError<Error>, boolean, QueryKey[]>;
}) =>
  useQuery(
    [QUERY_KEYS.MEMBER_ID_CHECK, storeCode],
    ({ queryKey: [, username] }) => requestGetIDCheck(String(username)),
    {
      ...options,
    },
  );

export default useIDCheck;
