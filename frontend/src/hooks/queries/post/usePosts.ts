import { useInfiniteQuery, QueryKey, UseInfiniteQueryOptions } from 'react-query';

import axios, { AxiosResponse, AxiosError } from 'axios';

import QUERY_KEYS from '@/constants/queries';

const usePosts = ({
  storeCode,
  options,
}: {
  storeCode: QueryKey;
  options?: UseInfiniteQueryOptions<
    AxiosResponse<{ posts: Post; lastPage: boolean }>,
    AxiosError,
    Post,
    AxiosResponse<{ posts: Post; lastPage: boolean }>,
    QueryKey[]
  >;
}) =>
  useInfiniteQuery(
    [QUERY_KEYS.POSTS, storeCode],
    ({ pageParam = 0, queryKey: [, size] }) => axios.get(`/posts?size=${size}&page=${pageParam}`),
    {
      select: data => ({
        pages: data.pages.flatMap((x: AxiosResponse) => x.data.posts),
        pageParams: [...data.pageParams, data.pageParams.length],
      }),
      enabled: false,
      getNextPageParam: (lastPage, allPages) => (lastPage.data.lastPage ? undefined : allPages.length),
      ...options,
    },
  );

export default usePosts;
