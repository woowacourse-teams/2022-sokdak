import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

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
  useQuery([QUERY_KEYS.POST, ...storeCode], ({ queryKey: [, query] }) => axios.get(`/posts/count?query=${query}`), {
    select: data => data.data,
    ...options,
  });

export default useSearchPostCount;
