import api from '.';

export const requestGetHashTags = async (limit: number, include: string) => {
  const { data } = await api.get(`/hashtags/popular?limit=${limit}&include=${include}`);

  return data;
};
