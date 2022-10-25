import { useInfiniteQuery, QueryKey, UseInfiniteQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetSearchPosts } from '@/apis/post';
import QUERY_KEYS from '@/constants/queries';

type Query = string;
type Size = number;

interface ResponseData {
  posts: Post[];
  lastPage: boolean;
}

const useSearchPosts = ({
  storeCode,
  options,
}: {
  storeCode: [Query, Size];
  options?: UseInfiniteQueryOptions<ResponseData, AxiosError, Post, ResponseData, [QueryKey, Query, Size]>;
}) =>
  useInfiniteQuery(
    [QUERY_KEYS.POSTS, ...storeCode],
    ({ pageParam = 0, queryKey: [, query, size] }) => requestGetSearchPosts(query, size, pageParam),
    {
      select: data => ({
        pages: data.pages.flatMap((page: ResponseData) => page.posts),
        pageParams: [...data.pageParams, data.pageParams.length],
      }),
      getNextPageParam: (lastPage, allPages) => (lastPage.lastPage ? undefined : allPages.length),
      ...options,
    },
  );

export default useSearchPosts;
