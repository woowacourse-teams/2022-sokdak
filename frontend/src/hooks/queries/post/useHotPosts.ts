import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis/authFetcher';
import QUERY_KEYS from '@/constants/queries';

const useHotPosts = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<
    AxiosResponse<{ posts: Post[]; lastPage: boolean }>,
    AxiosError,
    { posts: Post[]; lastPage: boolean },
    QueryKey[]
  >;
}) =>
  useQuery([QUERY_KEYS.POST, storeCode], ({ queryKey: [, size] }) => authFetcher.get(`/boards/1/posts?size=${size}`), {
    select: data => data.data,
    ...options,
  });

export default useHotPosts;
