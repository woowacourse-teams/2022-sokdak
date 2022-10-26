import { useQuery, QueryKey, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetHashTags } from '@/apis/hashtag';
import QUERY_KEYS from '@/constants/queries';

type Limit = number;
type Include = string;

const useSearchHashtags = ({
  storeCode,
  options,
}: {
  storeCode: [Limit, Include];
  options?: UseQueryOptions<{ hashtags: Hashtag[] }, AxiosError, { hashtags: Hashtag[] }, [QueryKey, Limit, Include]>;
}) => {
  return useQuery(
    [QUERY_KEYS.HASHTAGS, ...storeCode],
    ({ queryKey: [, limit, include] }) => requestGetHashTags(limit, include),
    {
      staleTime: 1000 * 40,
      ...options,
    },
  );
};

export default useSearchHashtags;
