import { QueryKey, useQuery, UseQueryOptions } from 'react-query';

import axios, { AxiosError, AxiosResponse } from 'axios';

import QUERY_KEYS from '@/constants/queries';

type Size = number;
type Page = number;

interface ResponseData {
  posts: Post[];
  totalPageCount: number;
}

const useMyPosts = ({
  storeCode,
  options,
}: {
  storeCode: [Size, Page];
  options?: UseQueryOptions<AxiosResponse, AxiosError<ErrorResponse>, ResponseData, [QueryKey, Size, Page]>;
}) =>
  useQuery(
    [QUERY_KEYS.MY_POSTS, ...storeCode],
    ({ queryKey: [, size, page] }) => axios.get(`/posts/me?size=${size}&page=${page - 1}`),
    {
      select: data => data.data,
      ...options,
    },
  );

export default useMyPosts;
