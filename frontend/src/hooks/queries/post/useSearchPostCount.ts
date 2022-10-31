import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import { requestGetSearchPostCount } from '@/api/post';
import QUERY_KEYS from '@/constants/queries';

type Query = string;

interface ResponseData {
  totalPostCount: number;
}

const useSearchPostCount = ({
  storeCode,
  options,
}: {
  storeCode: [Query];
  options?: UseQueryOptions<AxiosResponse<ResponseData>, AxiosError, ResponseData, [QueryKey, Query]>;
}) =>
  useQuery([QUERY_KEYS.POST, ...storeCode], ({ queryKey: [, query] }) => requestGetSearchPostCount(query), {
    ...options,
  });

export default useSearchPostCount;
