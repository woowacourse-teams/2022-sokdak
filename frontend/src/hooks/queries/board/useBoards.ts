import { useQuery, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetBoards } from '@/api/boards';
import QUERY_KEYS from '@/constants/queries';

const useBoards = ({ options }: { options?: UseQueryOptions<Board[], AxiosError, Board[], string> }) =>
  useQuery(QUERY_KEYS.BOARDS, () => requestGetBoards(), {
    staleTime: Infinity,
    cacheTime: Infinity,
    ...options,
  });

export default useBoards;
