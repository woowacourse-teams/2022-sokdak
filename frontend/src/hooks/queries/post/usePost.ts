import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import authFetcher from '@/apis';
import QUERY_KEYS from '@/constants/queries';

const usePost = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseQueryOptions<AxiosResponse<Post>, AxiosError, Post, QueryKey[]>;
}) =>
  useQuery([QUERY_KEYS.POST, storeCode], ({ queryKey: [, id] }) => authFetcher.get(`/posts/${id}`), {
    select: data => data.data,
    ...options,
  });

export default usePost;
