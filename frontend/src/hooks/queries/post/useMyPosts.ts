import { QueryKey, useQuery, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetMyPost } from '@/apis/post';
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
  options?: UseQueryOptions<ResponseData, AxiosError<ErrorResponse>, ResponseData, [QueryKey, Size, Page]>;
}) =>
  useQuery([QUERY_KEYS.MY_POSTS, ...storeCode], ({ queryKey: [, size, page] }) => requestGetMyPost(size, page), {
    ...options,
  });

export default useMyPosts;
