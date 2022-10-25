import { useInfiniteQuery, QueryKey, UseInfiniteQueryOptions } from 'react-query';

import { AxiosResponse, AxiosError } from 'axios';

import api from '@/apis';
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
  options?: UseInfiniteQueryOptions<
    AxiosResponse<ResponseData>,
    AxiosError,
    Post,
    AxiosResponse<ResponseData>,
    [QueryKey, Query, Size]
  >;
}) =>
  useInfiniteQuery(
    [QUERY_KEYS.POSTS, ...storeCode],
    ({ pageParam = 0, queryKey: [, query, size] }) =>
      api.get(
        `/posts?query=${query
          .replaceAll(' ', '%7C')
          .replaceAll('|', '%7C')
          .replaceAll('+', '%7C')}&size=${size}&page=${pageParam}`,
      ),
    {
      select: data => ({
        pages: data.pages.flatMap((page: AxiosResponse) => page.data.posts),
        pageParams: [...data.pageParams, data.pageParams.length],
      }),
      getNextPageParam: (lastPage, allPages) => (lastPage.data.lastPage ? undefined : allPages.length),
      suspense: true,
      ...options,
    },
  );

export default useSearchPosts;
