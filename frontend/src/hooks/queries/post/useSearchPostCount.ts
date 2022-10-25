import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import api from '@/apis';
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
  useQuery(
    [QUERY_KEYS.POST, ...storeCode],
    ({ queryKey: [, query] }) =>
      api.get(`/posts/count?query=${query.replaceAll(' ', '%7C').replaceAll('|', '%7C').replaceAll('+', '%7C')}`),
    {
      select: data => data.data,
      ...options,
    },
  );

export default useSearchPostCount;
