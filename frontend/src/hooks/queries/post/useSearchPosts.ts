import { useInfiniteQuery, QueryKey, UseInfiniteQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { GetSearchPostsResponse, requestGetSearchPosts } from '@/api/post';
import QUERY_KEYS from '@/constants/queries';

type Query = string;
type Size = number;

const useSearchPosts = ({
  storeCode,
  options,
}: {
  storeCode: [Query, Size];
  options?: UseInfiniteQueryOptions<
    GetSearchPostsResponse,
    AxiosError,
    Post,
    GetSearchPostsResponse,
    [QueryKey, Query, Size]
  >;
}) =>
  useInfiniteQuery(
    [QUERY_KEYS.POSTS, ...storeCode],
    ({ pageParam = 0, queryKey: [, query, size] }) => requestGetSearchPosts(query, size, pageParam),
    {
      select: data => ({
        pages: data.pages.flatMap((page: GetSearchPostsResponse) => page.posts),
        pageParams: [...data.pageParams, data.pageParams.length],
      }),
      getNextPageParam: (lastPage, allPages) => (lastPage.lastPage ? undefined : allPages.length),
      ...options,
    },
  );

export default useSearchPosts;
