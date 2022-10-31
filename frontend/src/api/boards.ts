import api from '.';

export const requestGetBoards = async () => {
  const { data } = await api.get('/boards');

  return data.boards;
};
