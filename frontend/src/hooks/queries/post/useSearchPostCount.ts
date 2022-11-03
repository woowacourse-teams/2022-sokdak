import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetSearchPostCount } from '@/api/post';
import QUERY_KEYS from '@/constants/queries';

type Query = string;

const useSearchPostCount = ({
  storeCode,
  options,
}: {
  storeCode: [Query];
  options?: UseQueryOptions<number, AxiosError, number, [QueryKey, Query]>;
}) =>
  useQuery([QUERY_KEYS.POST, ...storeCode], ({ queryKey: [, query] }) => requestGetSearchPostCount(query), {
    ...options,
  });

export default useSearchPostCount;
