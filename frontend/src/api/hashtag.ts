import fetcher from './fetcher';

export interface GetHashTagsResponse {
  hashtags: Hashtag[];
}

export const requestGetHashTags = async (limit: number, include: string) => {
  const { data } = await fetcher.get<GetHashTagsResponse>(`/hashtags/popular?limit=${limit}&include=${include}`);

  return data;
};
