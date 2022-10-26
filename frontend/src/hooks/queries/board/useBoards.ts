import { useQuery, UseQueryOptions } from 'react-query';

import { AxiosError } from 'axios';

import { requestGetBoards } from '@/apis/boards';
import QUERY_KEYS from '@/constants/queries';

interface BoardsResponse {
  boards: Board[];
}

const useBoards = ({ options }: { options?: UseQueryOptions<BoardsResponse, AxiosError, Board[], string> }) =>
  useQuery(QUERY_KEYS.BOARDS, () => requestGetBoards(), {
    ...options,
  });

export default useBoards;
