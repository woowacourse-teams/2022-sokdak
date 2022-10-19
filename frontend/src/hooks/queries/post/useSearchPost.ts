import { QueryKey, UseQueryOptions, useQuery } from 'react-query';

import axios, { AxiosResponse, AxiosError } from 'axios';

import QUERY_KEYS from '@/constants/queries';

type Query = string;
type Size = number;
type Page = number;

interface ResponseData {
  posts: Post[];
  totalPostCount: number;
}

const usePostsByQuery = ({
  storeCode,
  options,
}: {
  storeCode: [Query, Size, Page];
  options?: UseQueryOptions<AxiosResponse<ResponseData>, AxiosError, ResponseData, [QueryKey, Query, Size, Page]>;
}) => {
  return useQuery(
    [QUERY_KEYS.POSTS, ...storeCode],
    ({ queryKey: [, query, size, page] }) =>
      axios.get(`/posts?query=${query.replaceAll(' ', '|')}&size=${size}&page=${page}`),
    {
      select: data => data.data,
      ...options,
    },
  );
};

export default usePostsByQuery;
