import api from '.';

export interface BoardsResponse {
  boards: Board[];
}

export const requestGetBoards = async () => {
  const { data } = await api.get<BoardsResponse>('/boards');

  return data.boards;
};
