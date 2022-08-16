import { QueryKey, useQuery, UseQueryOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import QUERY_KEYS from '@/constants/queries';

type Size = number;
type Page = number;

const useMyPosts = ({
  storeCode,
  options,
}: {
  storeCode: [Size, Page];
  options?: UseQueryOptions<AxiosResponse, AxiosError<ErrorResponse>, Post, [QueryKey, Size, Page]>;
}) =>
  useQuery(
    [QUERY_KEYS.MY_POSTS, ...storeCode],
    ({ queryKey: [, size, page] }) => axios.get(`/posts/me?size=${size}&page=${page}`),
    {
      select: data => data.data,
      ...options,
    },
  );

export default useMyPosts;
