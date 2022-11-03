import fetcher from './fetcher';
import extractDataFromAxios from './util/extractor';

export interface GetBoardsResponse {
  boards: Board[];
}

export const requestGetBoards = () =>
  extractDataFromAxios<GetBoardsResponse>(fetcher.get('/boards')).then(data => data.boards);
